package com.heu.cs.poet.dao;

import com.heu.cs.poet.pojo.ChapterPojo;

public interface ChapterDao {
    public String queryAllChapter(String novelDBName);
    public ChapterPojo queryChapter(String novelDBName, String chapterId);
    public ChapterPojo queryPreChapter(String novelDBName, String chapterId);
    public ChapterPojo queryNextChapter(String novelDBName, String chapterId);

}
