package com.heu.cs.poet.resources;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;

@Path("web/chat")
public class Chat {

    @Context
    UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse response;




    @GET
    @Produces("text/html;charset=utf-8")
    public String  novelIndex() {
        response.setContentType("text/html;charset=UTF-8");
        String root = System.getProperty("user.dir") + File.separator + "/web/chat";
        System.out.println("====rootpath:" + root);
        FileResourceLoader resourceLoader = new FileResourceLoader(root, "utf-8");
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            Template t = gt.getTemplate("chat.html");
            return t.render();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error 500";
        }
    }
}
