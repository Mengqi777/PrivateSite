package com.heu.cs.poet.dao;

import com.google.gson.Gson;
import com.heu.cs.poet.pojo.Novel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NovelListDaoImpl extends BaseDao implements NovelListDao {


    @Override
    public String toJson(List<Novel> novels) {
        return new Gson().toJson(novels,List.class);
    }

    @Override
    public List<Novel> queryNovelList() {
        List<Novel> result=new ArrayList<>();
        String query="select * from novellist";
        ResultSet rs = this.executeQuery(query,null);
        try{
            while (rs.next()){
                int uuid=rs.getInt("uuid");
                String name=rs.getString("name");
                String dbName=rs.getString("dbName");
                String url=rs.getString("url");
                String history=rs.getString("history");
                String historyId=rs.getString("historyId");
                System.out.println("历史记录："+history);
                if(history==null){
                    history="未读过";
                    historyId="";
                }
                Novel novel =new Novel(uuid,name,dbName,url,history,historyId);
                result.add(novel);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.close();
        }

        return result;

    }

    @Override
    public void deleteNovel() {

    }

    @Override
    public void updateNovel() {

    }

    @Override
    public void updateHistory(String dbName,String chapterId,String history) {
        String update="UPDATE novellist SET history='"+history+"', historyId="+chapterId+" where dbName='"+dbName+"'";
        this.executeUpdate(update,null);
    }

    @Override
    public void insertNovel(Novel novel) {
        List<Novel> result=new ArrayList<>();
        String insert="insert into novellist(name,dbName,url) values(?,?,?)";
        List<Object> params=new ArrayList<>();
        params.add(novel.getName());
        params.add(novel.getDbName());
        params.add(novel.getUrl());
        this.executeUpdate(insert,params);
    }

}
