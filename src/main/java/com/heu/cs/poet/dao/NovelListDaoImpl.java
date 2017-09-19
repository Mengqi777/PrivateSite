package com.heu.cs.poet.dao;

import com.google.gson.Gson;
import com.heu.cs.poet.pojo.NovelPojo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NovelListDaoImpl extends BaseDao implements NovelListDao {


    @Override
    public String toJson(List<NovelPojo> novelPojos) {
        return new Gson().toJson(novelPojos,List.class);
    }

    @Override
    public List<NovelPojo> queryNovelList() {
        List<NovelPojo> result=new ArrayList<>();
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
                String fmimg=rs.getString("fmimg");
                System.out.println("历史记录："+history);
                if(history==null){
                    history="未读过";
                    historyId="";
                }
                NovelPojo novelPojo =new NovelPojo(uuid,name,dbName,url,history,historyId,fmimg);
                result.add(novelPojo);
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
        System.out.println("更新历史记录："+history);
        this.executeUpdate(update,null);
    }

    @Override
    public void insertNovel(NovelPojo novelPojo) {
        List<NovelPojo> result=new ArrayList<>();
        String insert="insert into novellist(name,dbName,url) values(?,?,?)";
        List<Object> params=new ArrayList<>();
        params.add(novelPojo.getName());
        params.add(novelPojo.getDbName());
        params.add(novelPojo.getUrl());
        this.executeUpdate(insert,params);
    }

}
