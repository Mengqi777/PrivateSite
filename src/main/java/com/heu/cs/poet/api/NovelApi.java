package com.heu.cs.poet.api;

import com.google.gson.Gson;
import com.heu.cs.poet.dao.ChapterDao;
import com.heu.cs.poet.dao.ChapterDaoImpl;
import com.heu.cs.poet.dao.NovelListDao;
import com.heu.cs.poet.dao.NovelListDaoImpl;
import com.heu.cs.poet.pojo.Chapter;
import com.heu.cs.poet.pojo.Novel;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Created by memgq on 2017/8/5.
 */
@Path("novel")
public class NovelApi {

    @Context
    UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;

    @GET
    @Path("index")
    @Produces("text/html;charset=utf-8")
    public void novelIndex() {
        NovelListDao novelListDao = new NovelListDaoImpl();
        response.setContentType("text/html;charset=UTF-8");
        String root = System.getProperty("user.dir") + File.separator + "/web";
        System.out.println("====rootpath:" + root);
        FileResourceLoader resourceLoader = new FileResourceLoader(root, "utf-8");
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            Template t = gt.getTemplate("novelindex.html");
            t.binding("novellist",novelListDao.toJson(novelListDao.queryNovelList()));
            t.renderTo(response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @GET
    @Path("{dbName}")
    @Produces("text/html;charset=utf-8")
    public void novelName(@PathParam("dbName") String dbNme) {
        ChapterDao chapterDao=new ChapterDaoImpl();
        response.setContentType("text/html;charset=UTF-8");
        String root = System.getProperty("user.dir") + File.separator + "/web";
        System.out.println("====rootpath:" + root);
        FileResourceLoader resourceLoader = new FileResourceLoader(root, "utf-8");
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            Template t = gt.getTemplate("noveldetail.html");
            t.binding("chapters",chapterDao.queryAllChapter(dbNme));
            t.binding("dbName",dbNme);
            t.renderTo(response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @GET
    @Path("{dbName}/{id}")
    @Produces("text/plain;charset=utf-8")
    public void read(@PathParam("dbName") String dbNme,
                     @PathParam("id") String id) {

        ChapterDao chapterDao=new ChapterDaoImpl();
        String chapterName=chapterDao.queryChapter(dbNme,id).getChapterName();
        NovelListDao novelListDao=new NovelListDaoImpl();
        novelListDao.updateHistory(dbNme,id,chapterName);
        response.setContentType("text/html;charset=UTF-8");
        String root = System.getProperty("user.dir") + File.separator + "/web";
        System.out.println("====rootpath:" + root);
        FileResourceLoader resourceLoader = new FileResourceLoader(root, "utf-8");
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            Template t = gt.getTemplate("chapter.html");
            t.binding("preChapter",chapterDao.queryPreChapter(dbNme, id).getChapterId());
            t.binding("nextChapter",chapterDao.queryNextChapter(dbNme, id).getChapterId());
            t.binding("chapterName",chapterName);
            t.binding("content",chapterDao.queryChapter(dbNme,id).getContent());
            t.binding("chapterId",chapterDao.queryChapter(dbNme,id).getChapterId());
            t.binding("dbName",dbNme);
            t.renderTo(response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @GET
    @Path("add")
    @Produces("text/plain;charset=utf-8")
    public void add() {
        response.setContentType("text/html;charset=UTF-8");
        String root = System.getProperty("user.dir") + File.separator + "/web";
        System.out.println("====rootpath:" + root);
        FileResourceLoader resourceLoader = new FileResourceLoader(root, "utf-8");
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            Template t = gt.getTemplate("add.html");
            t.renderTo(response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GET
    @Path("addnovel")
    @Produces("text/plain;charset=utf-8")
    public void addNovel(@QueryParam("name")String name,
                         @QueryParam("dbName")String dbName,
                         @QueryParam("url")String url) {
        Novel novel=new Novel(name,dbName,url);
        NovelListDao novelListDao=new NovelListDaoImpl();
        novelListDao.insertNovel(novel);
        try {
            response.sendRedirect("/web/novel/index");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
