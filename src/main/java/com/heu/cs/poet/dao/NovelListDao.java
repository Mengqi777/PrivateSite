package com.heu.cs.poet.dao;


import com.heu.cs.poet.pojo.NovelPojo;

import java.util.List;

public interface NovelListDao {


    public String toJson(List<NovelPojo> novelPojos);
    public List<NovelPojo> queryNovelList();

    public void deleteNovel();

    public void updateNovel();

    public void updateHistory(String dbName,String chapterId,String history);

    public void insertNovel(NovelPojo novelPojo);
}
