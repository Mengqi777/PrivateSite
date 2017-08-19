package com.heu.cs.poet.dao;

import com.google.gson.Gson;
import com.heu.cs.poet.pojo.Chapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChapterDaoImpl extends BaseDao implements ChapterDao{
    @Override
    public String queryAllChapter(String novelDBName) {
        System.out.println("数据库名称："+novelDBName);
        String query="select uuid,chapter from "+novelDBName;
        List<Chapter> chapters=new ArrayList<>();
        ResultSet rs=this.executeQuery(query,null);

        try{
            while (rs.next()){
                int chapterId=rs.getInt("uuid");
                String chapterName=rs.getString("chapter");
                Chapter chapter=new Chapter(chapterId,chapterName);
                chapters.add(chapter);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.close();
        }
        Collections.reverse(chapters);
        return new Gson().toJson(chapters,List.class);

    }

    @Override
    public Chapter queryChapter(String novelDBName, String chapterId) {
        String query="select chapter,content from "+novelDBName+" where uuid="+chapterId;
        ResultSet rs=this.executeQuery(query,null);
        Chapter chapter=new Chapter();
        try{
            while (rs.next()){
                String chapterName=rs.getString("chapter");
                String content=rs.getString("content");
                chapter=new Chapter(Integer.parseInt(chapterId),chapterName,content);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.close();
        }
        return chapter;
    }

    @Override
    public Chapter queryPreChapter(String novelDBName, String chapterId) {
        String query="select uuid,chapter from "+novelDBName+" where uuid = (select max(uuid) from "+novelDBName+" where uuid <"+chapterId+")";
        ResultSet rs=this.executeQuery(query,null);
        Chapter chapter=new Chapter();
        try{
            while (rs.next()){
                String chapterName=rs.getString("chapter");
                int uuid=rs.getInt("uuid");
                chapter=new Chapter(uuid,chapterName);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.close();
        }
        return chapter;
    }

    @Override
    public Chapter queryNextChapter(String novelDBName, String chapterId) {
        String query="select uuid,chapter from "+novelDBName+" where uuid = (select min(uuid) from "+novelDBName+" where uuid >"+chapterId+")";
        ResultSet rs=this.executeQuery(query,null);
        Chapter chapter=new Chapter();
        try{
            while (rs.next()){
                String chapterName=rs.getString("chapter");
                int uuid=rs.getInt("uuid");
                chapter=new Chapter(uuid,chapterName);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.close();
        }
        return chapter;
    }


}
