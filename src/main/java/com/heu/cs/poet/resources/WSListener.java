package com.heu.cs.poet.resources;

import com.google.gson.Gson;

import com.google.gson.JsonParser;
import com.heu.cs.poet.pojo.ServerMsgPojo;
import com.heu.cs.poet.pojo.WSListenserPool;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class   WSListener implements WebSocketListener {
    private String nickName;
    private Session session;
    private WSListenserPool listenserPool= WSListenserPool.INSTANCE;
    private String getNickName() {
        return nickName;
    }

    private void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private Session getSession() {
        return session;
    }

    private void setSession(Session session) {
        this.session = session;
    }



    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onWebSocketText(String s) {
        ConcurrentHashMap<String, Session> pool = listenserPool.getPool();
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
        listenserPool.listenserRemove(this.getNickName(),this.getSession());
        try {
            System.out.println("closed and refreshUsers");
            refreshUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.setNickName(session.getUpgradeRequest().getParameterMap().get("nickName").toString());
        this.setSession(session);
        listenserPool.listenserAdd(this.nickName,this.session);
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
        ConcurrentHashMap<String, Session> pool = listenserPool.getPool();
        ServerMsgPojo serverMsgPojo=new ServerMsgPojo();
        Gson gson=new Gson();
        JsonParser parser=new JsonParser();
        for(String nickName:pool.keySet()){
            serverMsgPojo.setFrom("server");
            serverMsgPojo.setType("userList");
            serverMsgPojo.setData(parser.parse(pool.keySet().toString()).getAsJsonArray());
            pool.get(nickName).getRemote().sendString(gson.toJson(serverMsgPojo,ServerMsgPojo.class));
        }
    }



    private void removeUnavailable(){
        Map<String,Session> removeMap=new HashMap<>();
        ConcurrentHashMap<String, Session> pool = listenserPool.getPool();
        for (String key : pool.keySet()) {
            Session session = pool.get(key);
            if (!session.isOpen()) {
                removeMap.put(key,session);
            }
        }
        for (String key : removeMap.keySet()) {
            listenserPool.listenserRemove(key,removeMap.get(key));
        }
    }
}
