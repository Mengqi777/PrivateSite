package com.heu.cs.poet.pojo;

public class Novel {
    private int uuid;
    private String name;
    private String dbName;
    private String url;
    private String history;
    private String historyId;

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Novel(int uuid, String name, String dbName, String url){
        super();
        this.uuid=uuid;
        this.name=name;
        this.dbName=dbName;
        this.url=url;
    }

    public Novel(int uuid, String name, String dbName, String url,String history,String historyId){
        super();
        this.uuid=uuid;
        this.name=name;
        this.dbName=dbName;
        this.url=url;
        this.history=history;
        this.historyId=historyId;
    }

    public Novel( String name, String dbName, String url){
        super();
        this.name=name;
        this.dbName=dbName;
        this.url=url;
    }
}
