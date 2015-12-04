package com.delaval.interactionlogserver.persistence.dao;

import com.delaval.interactionlogserver.persistence.ConnectionFactory;
import com.delaval.interactionlogserver.persistence.entity.LogContent;
import com.delaval.interactionlogserver.persistence.entity.LogContext;
import com.delaval.interactionlogserver.persistence.entity.LogContextJDBC;
import com.delaval.interactionlogserver.websocket.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simpleorm.sessionjdbc.SSessionJdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Singleton.
 * Handles the persistence-logic of the LogContent-table and LogContext-table.
 */
public class LogDAO {

    private Logger LOGGER = LoggerFactory.getLogger(LogDAO.class);
    private static final String LOG_SERVER_CONN_NAME = "LogServer";

    private static LogDAO _instance;

    private LogDAO(){
        // Empty by design
    }

    public static LogDAO getInstance(){
        if (_instance == null) {
            synchronized (LogDAO.class) {
                if (_instance == null) {
                    _instance = new LogDAO();
                }
            }
        }
        return _instance;
    }


    /**
     * Creates a Log with the content from the WebSocketMessage.
     * @param webSocketMessage is the info from the client.
     */
    public void createLog(WebSocketMessage webSocketMessage){
        LOGGER.info("Saving log:" + webSocketMessage.toString());
        createWithSimpleOrm(webSocketMessage);
//        createWithJDBC(webSocketMessage);

    }

    private void createWithJDBC(WebSocketMessage webSocketMessage){

    }

//    private LogContextJDBC findOrCreateLogContext(WebSocketMessage webSocketMessage){
//
//    }

    private void createWithSimpleOrm(WebSocketMessage webSocketMessage){
        SSessionJdbc ses = null;
        try {
            ses = ConnectionFactory.getInstance().getSession(LOG_SERVER_CONN_NAME);
            ses.getStatistics();
            LogContext.findOrCreateContext(ses, webSocketMessage);
            LogContent.create(webSocketMessage, ses);
        }
        catch (Exception e){
            LOGGER.error("Something went wrong while saving log:" + e.getMessage());
            if(ses != null){
                ses.rollback();
            }
            return;

        }
        finally {
            if(ses != null){
                ses.close();
            }

        }
    }

    /**
     * Fetches the LogContext-table
     * @param connection is the db-connection
     * @return the resultset with the table
     */
    public ResultSet getLogContextTable(Connection connection){
        DatabaseMetaData metadata = null;
        ResultSet resultSet = null;
        try {
            metadata = connection.getMetaData();
            resultSet = metadata.getTables(null, null, LogContext.LOG_CONTEXT.getTableName(), null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
