package com.delaval.interactionlogserver.persistence;

import com.delaval.interactionlogserver.ServerProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simpleorm.sessionjdbc.SSessionJdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Singleton.
 * Handles connection and sessions to db.
 */
public class ConnectionFactory {

    private static ConnectionFactory _instance;
    public static final String LOG_SERVER_CONN_NAME = "LogServer";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);

    private Connection theConnection = null;

    private ConnectionFactory() {
        createConnection();
    }

    public static ConnectionFactory getInstance() {
        if (_instance == null || isConnectionClosed()) {
            synchronized (ConnectionFactory.class) {
                if (_instance == null || isConnectionClosed()) {
                    _instance = new ConnectionFactory();
                }
            }
        }
        return _instance;
    }

    private static boolean isConnectionClosed() {
        try {
            return _instance.getConnection().isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void closeFactory() {
        LOGGER.info("Closing factory");
        _instance.closeConnection();

        _instance = null;
    }

    public SSessionJdbc getSession(String connectionName) {
        return SSessionJdbc.open(getConnection(), connectionName);
    }

    public void closeConnection() {
        LOGGER.info("Closing connection");
        if (getConnection() != null) {
            try {
                if (!getConnection().isClosed()) {
                    getConnection().close();
                }
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection:" + e.getMessage());
            }
        }
    }

    public Connection getConnection(){
        try {
            if(theConnection.isClosed()){
                createConnection();
            }
            return theConnection;
        } catch (SQLException e) {
            LOGGER.error("Error while fetching connection:" + e.getMessage());
        }
        return theConnection;
    }

    private void createConnection(){
        try {
            String jdbcUrl = "jdbc:mysql://" + getProp(ServerProperties.PropKey.DB_SERVER_HOST) +
                    ":" + getProp(ServerProperties.PropKey.DB_SERVER_PORT) + "/" +
                    getProp(ServerProperties.PropKey.DB_NAME);
            LOGGER.info("Creating db-connection:" + jdbcUrl);
            theConnection = java.sql.DriverManager.getConnection(jdbcUrl, getProp(ServerProperties.PropKey.DB_USER), getProp(ServerProperties.PropKey.DB_PWD));

            theConnection.setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.error("Error while creating connection:" + e);
        }
    }


    private String getProp(ServerProperties.PropKey propKey) {
        return ServerProperties.getInstance().getProp(propKey);
    }

}
