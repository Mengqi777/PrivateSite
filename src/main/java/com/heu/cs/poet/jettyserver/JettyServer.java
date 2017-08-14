package com.heu.cs.poet.jettyserver;


import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {

    public static void main(String[] args) throws Exception {

        Server jettyServer = new Server();

        HttpConfiguration http_config = new HttpConfiguration();
        /**
         *http_config可以对服务器进行配置，比如设置https,BufferSize等等
         *        http_config.setSecureScheme("https");
         *        http_config.setSecurePort(8443);
         *        http_config.setOutputBufferSize(32768);
         *        http_config.setRequestHeaderSize(8192);
         *        http_config.setResponseHeaderSize(8192);
         */
        http_config.setSendServerVersion(true);
        http_config.setSendDateHeader(false);

        /**
         * 新建http连接来设置访问端口，超时时间等等。
         */
        ServerConnector httpServer = new ServerConnector(jettyServer,
                new HttpConnectionFactory(http_config));
        httpServer.setPort(10001);

        httpServer.setIdleTimeout(120000);
        jettyServer.addConnector(httpServer);

        /**
         * 设置整个web服务的根url，/ 表示 localhost:7012/  之后地址的是可访问的
         */
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

//        //设置监听器
//        context.getSessionHandler().addEventListener(new MyHttpSessionListener());
//        //设置过滤器，用来登录过滤
//         context.addFilter(new FilterHolder(new LoginFilter()), "/webapi/*", EnumSet.allOf(DispatcherType.class));



        /**
         * 添加动态servlet路径，处理我们自己写的动态的servlet
         */
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/web/*");
        jerseyServlet.setInitOrder(1);

        // Tells the Jersey Servlet which REST api/class to load.设置动态servlt加载的包
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.heu.cs.poet.api");
//        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
//                "UploadFileService;org.glassfish.jersey.media.multipart.MultiPartFeature");//也可单独设置加载某个类

        /**
         * 添加默认的servlet路径，处理不在动态servlet路径中的地址，一般都是一些可供访问的静态html,css,js资源文件
         */
        ServletHolder staticServlet = context.addServlet(DefaultServlet.class, "/static/*");
        staticServlet.setInitParameter("resourceBase", "src/main/resources");
        staticServlet.setInitParameter("pathInfoOnly", "true");
        staticServlet.setInitParameter("dirAllowed", "false");


        /**
         * Embedded Jetty还可以直接当作服务器，在上面部署已经发布的war包，这方面的资料国内挺多的，就不累述
         *
         * 其次，在我们的项目中是没有用到web.xml文件来进行webappde的配置，因为上面的设置并不能使得服务器访问web.xml，
         * 如果需要用到web.xml，则需要new一个WebAppContext,并对其进行配置，同时在下面的handlers中加上webAppContext
         *  WebAppContext webAppContext = new WebAppContext();
         *  设置描述符位置
         *  webAppContext.setDescriptor("./web/WEB-INF/web.xml");
         *  设置Web内容上下文路径
         *  webAppContext.setResourceBase("./web");
         *  设置上下文路径
         *  webAppContext.setContextPath("/");
         *  webAppContext.setParentLoaderPriority(true);
         */




        HandlerCollection handlers = new HandlerCollection();
        // handlers.setHandlers(new Handler[]{context,webAppContext});
        handlers.setHandlers(new Handler[]{context});
        jettyServer.setHandler(handlers);
        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}
