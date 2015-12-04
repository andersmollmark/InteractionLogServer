package main;

import com.delaval.interactionlogserver.persistence.ConnectionFactory;
import com.delaval.interactionlogserver.persistence.dao.LogDAO;
import com.delaval.interactionlogserver.websocket.Content;
import com.delaval.interactionlogserver.websocket.WebSocketMessage;
import com.google.gson.Gson;

import java.sql.*;

/**
 * Created by delaval on 12/2/2015.
 */
public class SimpleORMTest {


    public SimpleORMTest(){
//        doTheSelectCount();

        MyWebSocketMessage webSocketMessage = new MyWebSocketMessage();
        webSocketMessage.setClient("klienten");
        webSocketMessage.setUsername("LEIF USER");
        webSocketMessage.setTarget("Ipad");

        MyContent testContent = new MyContent();
        testContent.setCssClassName("cssClassName");
        testContent.setElementId("ettElementId");
        testContent.setX("x");
        testContent.setY("y");

        

        for(int i=0; i<10; i++) {
            java.util.Date date = new java.util.Date();
            testContent.setTimestamp(Long.toString(date.getTime()));
            String jsonContent = new Gson().toJson(testContent);
            webSocketMessage.setJsonContent(jsonContent);
            doInsert(webSocketMessage);
        }
        ConnectionFactory.getInstance().closeConnection();
        for(int i=0; i<10; i++) {
            java.util.Date date = new java.util.Date();
            testContent.setTimestamp(Long.toString(date.getTime()));
            String jsonContent = new Gson().toJson(testContent);
            webSocketMessage.setJsonContent(jsonContent);
            doInsert(webSocketMessage);
        }

    }

    private void doInsert(WebSocketMessage webSocketMessage){
        LogDAO logDAO = LogDAO.getInstance();
        logDAO.createLog(webSocketMessage);
    }

    private static class MyWebSocketMessage extends WebSocketMessage{
        private String jsonContent;
        private String client;
        private String username;
        private String messType;

        
        public String getClient() {
            return client;
        }

        
        public String getUsername() {
            return username;
        }

        
        public String getTarget() {
            return target;
        }

        
        public String getJsonContent() {
            return jsonContent;
        }

        
        public String getMessType() {
            return messType;
        }

        private String target;


        public void setJsonContent(String jsonContent) {
            this.jsonContent = jsonContent;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setMessType(String messType) {
            this.messType = messType;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }

    private static class MyContent {
        private String x;
        private String y;
        private String elementId;
        private String cssClassName;
        private String timestamp;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        
        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        
        public String getElementId() {
            return elementId;
        }

        public void setElementId(String elementId) {
            this.elementId = elementId;
        }

        
        public String getCssClassName() {
            return cssClassName;
        }

        public void setCssClassName(String cssClassName) {
            this.cssClassName = cssClassName;
        }

        
        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }


    public static void main(String[] args){
        new SimpleORMTest();
    }
}
