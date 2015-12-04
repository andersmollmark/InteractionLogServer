package com.delaval.interactionlogserver.persistence.entity;

import com.delaval.interactionlogserver.util.DateUtil;
import com.delaval.interactionlogserver.websocket.Content;
import com.delaval.interactionlogserver.websocket.WebSocketMessage;
import com.google.gson.Gson;
import simpleorm.dataset.*;
import simpleorm.sessionjdbc.SSessionJdbc;

import static simpleorm.dataset.SFieldFlags.SDESCRIPTIVE;
import static simpleorm.dataset.SFieldFlags.SPRIMARY_KEY;

/**
 * Entity that mirrors LogContent-table
 */
public class LogContent extends AbstractEntity {

    public static final SRecordMeta LOG_CONTENT = new SRecordMeta(LogContent.class, "LogContent");
    public static final SFieldString ID = new SFieldString(LOG_CONTENT, "id", 100, SPRIMARY_KEY);
    public static final SFieldString INTERACTION_TYPE = new SFieldString(LOG_CONTENT, "interactionType", 40, SDESCRIPTIVE);
    public static final SFieldString X = new SFieldString(LOG_CONTENT, "x", 5, SDESCRIPTIVE);
    public static final SFieldString Y = new SFieldString(LOG_CONTENT, "y", 5, SDESCRIPTIVE);
    public static final SFieldString CSS_CLASSNAME = new SFieldString(LOG_CONTENT, "cssClassname", 50, SDESCRIPTIVE);
    public static final SFieldString ELEMENT_ID = new SFieldString(LOG_CONTENT, "elementId", 50, SDESCRIPTIVE);
    public static final SFieldString LOG_CONTEXT_ID = new SFieldString(LOG_CONTENT, "logContextId", 100, SDESCRIPTIVE); // TODO check if we can bind this
    public static final SFieldTimestamp TIMESTAMP = new SFieldTimestamp(LOG_CONTENT, "timestamp").overrideSqlDataType("TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");

    @Override
    public SRecordMeta<LogContent> getMeta() {
        return LOG_CONTENT;
    }

    /**
     * Creates a new instance of LogContent from the message sent from webclient.
     * @param webSocketMessage
     * @param jdbcSession
     */
    public static void create(WebSocketMessage webSocketMessage, SSessionJdbc jdbcSession) {
        jdbcSession.begin();
        Content content = new Gson().fromJson(webSocketMessage.getJsonContent(), Content.class);
        LogContent newContent = (LogContent) jdbcSession.create(LogContent.LOG_CONTENT, getLogContentId(webSocketMessage, content));
        newContent.setString(LogContent.CSS_CLASSNAME, content.getCssClassName());
        newContent.setString(LogContent.ELEMENT_ID, content.getElementId());
        newContent.setString(LogContent.INTERACTION_TYPE, "ToBeChosen");
        newContent.setString(LogContent.LOG_CONTEXT_ID, getLogContextId(webSocketMessage));
        long timestamp = Long.parseLong(content.getTimestamp());
        newContent.setString(LogContent.TIMESTAMP, DateUtil.formatTimeStamp(timestamp));
        newContent.setString(LogContent.X, content.getX());
        newContent.setString(LogContent.Y, content.getY());
        jdbcSession.flush();
        jdbcSession.commit();
    }

    private static String getLogContentId(WebSocketMessage webSocketMessage, Content content){
        StringBuilder sb = new StringBuilder();
        sb.append(webSocketMessage.getUsername()).append(content.getTimestamp());
        return sb.toString();
    }


}
