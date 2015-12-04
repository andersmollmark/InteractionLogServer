package com.delaval.interactionlogserver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton.
 * Holds all the properties needed in the InteractionLogServer.
 */
public class ServerProperties {

    private static ServerProperties _instance;

    private static final String PROP_FILE_NAME = "InteractionLogServer.properties";

    public enum PropKey {
        DB_SERVER_HOST("dbServerHost"),
        DB_SERVER_PORT("dbServerPort"),
        DB_NAME("dbName"),
        DB_USER("dbUser"),
        DB_PWD("dbPassword"),
        JMS_CONNECTION("jmsConnection"),
        JMS_QUEUE_DEST("jmsQueue"),
        INTERACTION_LOG_TYPE("interactionLog"),
        IDLE_POLL("IdlePoll"),
        THREAD_POOL_SIZE("threadPoolSize"),
        WEBSOCKET_PORT("websocketPort");

        private String myValue;

        PropKey(String value){
            myValue = value;
        }

        public String getMyValue(){
            return myValue;
        }
    }

    private Properties prop = new Properties();

    private ServerProperties(){
        setProperties();
    }

    public static ServerProperties getInstance(){
        if (_instance == null) {
            synchronized (ServerProperties.class) {
                if (_instance == null) {
                    _instance = new ServerProperties();
                }
            }
        }
        return _instance;
    }

    private String get(PropKey propType){
        return prop.getProperty(propType.getMyValue());
    }

    /**
     * Get serverproperty with a certain key
     * @param propkey
     * @return the value of the property with this key
     */
    public String getProp(PropKey propkey){
        return get(propkey);
    }

    private Properties setProperties() {
        try {
            prop.load(new FileInputStream(PROP_FILE_NAME));
        } catch (IOException ex) {
            try {
                // Create default values
                set(PropKey.DB_SERVER_HOST, "localhost");
                set(PropKey.DB_SERVER_PORT, "3306");
                set(PropKey.DB_NAME, "log_server");
                set(PropKey.DB_USER, "logAdmin");
                set(PropKey.DB_PWD, "admin");
                set(PropKey.THREAD_POOL_SIZE, "500");
//                set(PropKey.JMS_CONNECTION, "tcp://localhost:61616");
                set(PropKey.JMS_CONNECTION, "tcp://10.34.35.67:61616");
                set(PropKey.JMS_QUEUE_DEST, "InteractionLog");
                set(PropKey.INTERACTION_LOG_TYPE, "interactionLog");
                set(PropKey.IDLE_POLL, "IdlePoll");
                set(PropKey.WEBSOCKET_PORT, "8082");
                prop.store(new FileOutputStream(PROP_FILE_NAME), null);
            } catch (IOException e) {
                System.out.println("Got problem creating " + PROP_FILE_NAME + " file! ");
            }
        }
        return prop;
    }

    private void set(PropKey propType, String propValue){
        prop.setProperty(propType
                .getMyValue(), propValue);
    }
}
