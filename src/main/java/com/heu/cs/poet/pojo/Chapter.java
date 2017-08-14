package com.heu.cs.poet.pojo;

public class Chapter {
    private String chapterName;
    private int chapterId;
    private String content;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public Chapter(int chapterId,String chapterName){
        super();
        this.chapterId=chapterId;
        this.chapterName=chapterName;
    }

    public Chapter(int chapterId,String chapterName,String content){
        super();
        this.chapterId=chapterId;
        this.chapterName=chapterName;
        this.content=content;
    }

    public Chapter(){
        super();
    }
}
