package com.heu.cs.poet.dao;

import com.heu.cs.poet.pojo.Chapter;

public interface ChapterDao {
    public String queryAllChapter(String novelDBName);
    public Chapter queryChapter(String novelDBName, String chapterId);
    public Chapter queryPreChapter(String novelDBName, String chapterId);
    public Chapter queryNextChapter(String novelDBName, String chapterId);

}
