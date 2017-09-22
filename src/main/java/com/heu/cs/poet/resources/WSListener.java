package com.heu.cs.poet.resources;

import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.heu.cs.poet.pojo.ServerMsgPojo;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import javax.json.Json;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class   WSListener implements WebSocketListener {

    /**
     * 保存已打开的所有WebSocket Session
     */
    public static final ConcurrentHashMap<String, Session> pool = new ConcurrentHashMap<String, Session>();

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {
        removeUnavailable();
    }

    @Override
    public void onWebSocketText(String s) {
        removeUnavailable();
        for(String nickName:pool.keySet()){
            try {
                pool.get(nickName).getRemote().sendString(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onWebSocketClose(int i, String s) {
       removeUnavailable();
    }

    @Override
    public void onWebSocketConnect(Session session) {
        removeUnavailable();
        String nickName=session.getUpgradeRequest().getParameterMap().get("nickName").toString();
        pool.put(nickName,session);
        System.out.println();
        try {
            refreshUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebSocketError(Throwable throwable) {
        removeUnavailable();
    }



    private void refreshUsers() throws IOException {
        Map<String,Object> msg=new HashMap<>();
        for(String nickName:pool.keySet()){
            msg.put("session",pool.get(nickName));
            msg.put("type","userList");
            msg.put("data",pool.keySet());
            msg.put("from","server");
            send(msg);
        }
    }

    private void send(Map<String, Object> msg) {
        Session session= (Session) msg.get("session");
        if(session!=null){
            ServerMsgPojo serverMsgPojo=new ServerMsgPojo();
            Gson gson=new Gson();
            JsonParser parser=new JsonParser();
            try {
                serverMsgPojo.setFrom(msg.get("from").toString());
                serverMsgPojo.setType("userList");
                serverMsgPojo.setData(parser.parse(pool.keySet().toString()).getAsJsonArray());
                session.getRemote().sendString(gson.toJson(serverMsgPojo,ServerMsgPojo.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeUnavailable(){
        List<String> removedKeys = new ArrayList<>();
        for (String key : pool.keySet()) {
            Session session = pool.get(key);
            if (!session.isOpen()) {
                removedKeys.add(key);
            }
        }
        for (String key : removedKeys) {
            pool.remove(key);
        }
    }
}
