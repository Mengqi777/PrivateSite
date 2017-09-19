package com.heu.cs.poet.dao;

import com.google.gson.Gson;
import com.heu.cs.poet.pojo.ChapterPojo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChapterDaoImpl extends BaseDao implements ChapterDao{
    @Override
    public String queryAllChapter(String novelDBName) {
        System.out.println("数据库名称："+novelDBName);
        String query="select uuid,chapter from "+novelDBName;
        List<ChapterPojo> chapterPojos =new ArrayList<>();
        ResultSet rs=this.executeQuery(query,null);

        try{
            while (rs.next()){
                int chapterId=rs.getInt("uuid");
                String chapterName=rs.getString("chapter");
                ChapterPojo chapterPojo =new ChapterPojo(chapterId,chapterName);
                chapterPojos.add(chapterPojo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.close();
        }
        Collections.reverse(chapterPojos);
        return new Gson().toJson(chapterPojos,List.class);

    }

    @Override
    public ChapterPojo queryChapter(String novelDBName, String chapterId) {
        String query="select chapter,content from "+novelDBName+" where uuid="+chapterId;
        ResultSet rs=this.executeQuery(query,null);
        ChapterPojo chapterPojo =new ChapterPojo();
        try{
            while (rs.next()){
                String chapterName=rs.getString("chapter");
                String content=rs.getString("content");
                chapterPojo =new ChapterPojo(Integer.parseInt(chapterId),chapterName,content);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.close();
        }
        return chapterPojo;
    }

    @Override
    public ChapterPojo queryPreChapter(String novelDBName, String chapterId) {
        String query="select uuid,chapter from "+novelDBName+" where uuid = (select max(uuid) from "+novelDBName+" where uuid <"+chapterId+")";
        ResultSet rs=this.executeQuery(query,null);
        ChapterPojo chapterPojo =new ChapterPojo();
        try{
            while (rs.next()){
                String chapterName=rs.getString("chapter");
                int uuid=rs.getInt("uuid");
                chapterPojo =new ChapterPojo(uuid,chapterName);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.close();
        }
        return chapterPojo;
    }

    @Override
    public ChapterPojo queryNextChapter(String novelDBName, String chapterId) {
        String query="select uuid,chapter from "+novelDBName+" where uuid = (select min(uuid) from "+novelDBName+" where uuid >"+chapterId+")";
        ResultSet rs=this.executeQuery(query,null);
        ChapterPojo chapterPojo =new ChapterPojo();
        try{
            while (rs.next()){
                String chapterName=rs.getString("chapter");
                int uuid=rs.getInt("uuid");
                chapterPojo =new ChapterPojo(uuid,chapterName);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.close();
        }
        return chapterPojo;
    }


}
