package com.heu.cs.poet.pojo;

import com.heu.cs.poet.resources.WSListener;
import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public enum WSListenserPool {
    INSTANCE;
    private ConcurrentHashMap<String,Session> pool=new ConcurrentHashMap<>();
    private int onlineCount=0;

    public ConcurrentHashMap<String, Session> getPool() {
        return pool;
    }

    public void setPool(ConcurrentHashMap<String, Session> pool) {
        this.pool = pool;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }


    public void onlineCountAdd(){
        this.onlineCount++;
    }

    public void onlineCountRemove(){
        this.onlineCount--;
    }


    public void listenserAdd(String nickName,Session session){
        this.pool.put(nickName,session);
    }

    public void listenserRemove(String nickName,Session session){
        this.pool.remove(nickName,session);
    }
}
