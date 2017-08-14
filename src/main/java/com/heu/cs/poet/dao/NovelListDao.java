package com.heu.cs.poet.dao;


import com.heu.cs.poet.pojo.Novel;

import java.util.List;

public interface NovelListDao {


    public String toJson(List<Novel> novels);
    public List<Novel> queryNovelList();

    public void deleteNovel();

    public void updateNovel();

    public void updateHistory(String dbName,String chapterId,String history);

    public void insertNovel(Novel novel);
}
