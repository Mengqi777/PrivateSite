package com.heu.cs.poet.jettyserver;


import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.server.ServerConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.servlet.DispatcherType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.EnumSet;

public class JettyServer {

    public static void main(String[] args) throws Exception {

        String rootPath=System.getProperty("user.dir");

        String jks= rootPath+"/web/WEB-INF/api.mengqipoet.cn.jks";

        System.out.println(jks);
        String keystorePath = System.getProperty(
                "example.keystore", jks);
        File keystoreFile = new File(keystorePath);
        if (!keystoreFile.exists())
        {
            throw new FileNotFoundException(keystoreFile.getAbsolutePath());
        }

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
        http_config.setSendDateHeader(true);
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        http_config.setOutputBufferSize(32768);
        http_config.addCustomizer(new SecureRequestCustomizer());



        /**
         * 新建http连接来设置访问端口，超时时间等等。
         */
        ServerConnector http = new ServerConnector(jettyServer,
                new HttpConnectionFactory(http_config));
        http.setPort(80);
        http.setIdleTimeout(120000);






        // SSL Context Factory for HTTPS
        // SSL requires a certificate so we configure a factory for ssl contents
        // with information pointing to what keystore the ssl connection needs
        // to know about. Much more configuration is available the ssl context,
        // including things like choosing the particular certificate out of a
        // keystore to be used.
        SslContextFactory sslContextFactory=new SslContextFactory();

//        sslContextFactory.setKeyStorePath(jettyDistKeystore);
//        sslContextFactory.setKeyStorePassword("OBF:1v8s1x8g1vuh1kqj1kub1vut1x8e1v9u");
//        sslContextFactory.setKeyManagerPassword("OBF:1v8s1x8g1vuh1kqj1kub1vut1x8e1v9u");

        sslContextFactory.setKeyStorePath(jks);
        sslContextFactory.setKeyStorePassword("api.root");
        sslContextFactory.setKeyManagerPassword("api.root");


        // HTTPS Configuration
        // A new HttpConfiguration object is needed for the next connector and
        // you can pass the old one as an argument to effectively clone the
        // contents. On this HttpConfiguration object we add a
        // SecureRequestCustomizer which is how a new connector is able to
        // resolve the https connection before handing control over to the Jetty
        // Server.
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        SecureRequestCustomizer src = new SecureRequestCustomizer();
        src.setStsMaxAge(2000);
        src.setStsIncludeSubDomains(true);
        https_config.setSecurePort(8443);
        https_config.addCustomizer(src);

        // HTTPS connector
        // We create a second ServerConnector, passing in the http configuration
        // we just made along with the previously created ssl context factory.
        // Next we set the port and a longer idle timeout.
        ServerConnector https = new ServerConnector(jettyServer,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(https_config));
        https.setPort(443);
        https.setIdleTimeout(500000);

        // Here you see the server having multiple connectors registered with
        // it, now requests can flow into the server from both http and https
        // urls to their respective ports and be processed accordingly by jetty.
        // A simple handler is also registered with the server so the example
        // has something to pass requests off to.

        // Set the connectors
        jettyServer.setConnectors(new Connector[] {  https });


        /**
         * 设置整个web服务的根url，/ 表示 localhost:7012/  之后地址的是可访问的
         */
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

//        //设置监听器
//        context.getSessionHandler().addEventListener(new MyHttpSessionListener());
//        //设置过滤器，用来登录过滤
//         context.addFilter(new FilterHolder(new LoginFilter()), "/web/*", EnumSet.allOf(DispatcherType.class));



        /**
         * 添加动态servlet路径，处理我们自己写的动态的servlet
         */
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(1);

        // Tells the Jersey Servlet which REST resources/class to load.设置动态servlt加载的包
        jerseyServlet.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "com.heu.cs.poet.resources");

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
