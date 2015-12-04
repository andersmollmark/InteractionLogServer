/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.delaval.interactionlogserver;

import ch.qos.logback.classic.Level;
import com.delaval.interactionlogserver.persistence.ConnectionFactory;
import com.delaval.interactionlogserver.persistence.dao.InitDAO;
import com.delaval.interactionlogserver.persistence.dao.LogDAO;
import com.delaval.interactionlogserver.servlet.LogWebSocketServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * that initializes the Jetty-server.
 */
public class InteractionLogServer {

    private Server server;
    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionLogServer.class);

    private InteractionLogServer() {
        // empty by design
    }

    public void init() {
        try {
            if (isCreateTables()) {
                InitDAO initDAO = new InitDAO();
                initDAO.createTables();
            }
        } catch (Exception e) {
            LOGGER.error("FATAL ERROR, something went wrong while creating tables in db:" + e.getMessage());
            System.exit(0);
        } finally {
//            ConnectionFactory.closeFactory();
        }
        initServer();
    }

    private void initServer() {
        QueuedThreadPool threadPool = new QueuedThreadPool(); // TODO is this necessary??
        int threadPoolSize = Integer.parseInt(ServerProperties.getInstance().getProp(ServerProperties.PropKey.THREAD_POOL_SIZE));
        threadPool.setMaxThreads(threadPoolSize);

        int websocketPort = Integer.parseInt(ServerProperties.getInstance().getProp(ServerProperties.PropKey.WEBSOCKET_PORT));
        server = new Server(8082);

        initLogLevel();
        server.setHandler(createServletContextHandler());

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            LOGGER.error("FATAL ERROR, something happened while starting server, shutting down:" + e.getMessage());
            ConnectionFactory.closeFactory();
            System.exit(0);
        }

    }

    private ServletContextHandler createServletContextHandler() {
        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase("app");
        context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        context.setInitParameter("cacheControl", "no-cache");

        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        context.addServlet(DefaultServlet.class, "/");

        ServletHolder ws = context.addServlet(LogWebSocketServlet.class , "/ws");
        ws.setInitParameter("classpath", context.getClassPath());
        return context;
    }

    private void initLogLevel(){
        // TODO handle this in groovy as the rest of the servers?
        final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("org.eclipse.jetty");
        if (logger instanceof ch.qos.logback.classic.Logger) {
            ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) logger;
            logbackLogger.setLevel(Level.INFO);
        }
        else{
            throw new IllegalStateException("Logger-class was not instance of logback-Logger");
        }
    }

    private boolean isCreateTables() {
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection();
            LogDAO logDAO = LogDAO.getInstance();
            ResultSet resultSet = logDAO.getLogContextTable(connection);
            if (resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            return true;
        }
        return true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new InteractionLogServer().init();
    }

}
