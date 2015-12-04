package com.delaval.interactionlogserver.persistence.entity;

import com.delaval.interactionlogserver.util.DateUtil;
import com.delaval.interactionlogserver.websocket.WebSocketMessage;
import simpleorm.dataset.*;
import simpleorm.sessionjdbc.SSessionJdbc;

import java.util.Date;
import java.util.Optional;

import static simpleorm.dataset.SFieldFlags.SDESCRIPTIVE;
import static simpleorm.dataset.SFieldFlags.SPRIMARY_KEY;

/**
 * Entity that mirrors LogContext-table
 */
public class LogContext extends AbstractEntity {

    public static final SRecordMeta LOG_CONTEXT = new SRecordMeta(LogContext.class, "LogContext");
    public static final SFieldString ID = new SFieldString(LOG_CONTEXT, "id", 100, SPRIMARY_KEY);
    public static final SFieldString USERNAME = new SFieldString(LOG_CONTEXT, "username", 40, SDESCRIPTIVE);
    public static final SFieldString TARGET = new SFieldString(LOG_CONTEXT, "target", 40, SDESCRIPTIVE);
    public static final SFieldString CLIENT = new SFieldString(LOG_CONTEXT, "client", 40, SDESCRIPTIVE).setInitialValue("");
    public static final SFieldTimestamp TIMESTAMP = new SFieldTimestamp(LOG_CONTEXT, "timestamp").overrideSqlDataType("TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");

    @Override
	public SRecordMeta<LogContext> getMeta() {
            return LOG_CONTEXT;
        }

    /**
     * Finds or creates an instance of LogContext.
     * @param jdbcSession
     * @param webSocketMessage
     * @return
     */
    public static LogContext findOrCreateContext(SSessionJdbc jdbcSession, WebSocketMessage webSocketMessage){
        String logId = getLogContextId(webSocketMessage);
        jdbcSession.begin();
        Optional<LogContext> persistedLogContext = find(jdbcSession, logId);
        LogContext logContext = persistedLogContext.isPresent() ? persistedLogContext.get() : create(webSocketMessage, jdbcSession);
        jdbcSession.flush();
        jdbcSession.commit();
        return logContext;
    }


    public static Optional<LogContext> find(SSessionJdbc ses, String id) {
        LogContext logContext = (LogContext)ses.find(LogContext.LOG_CONTEXT, id);
        return Optional.ofNullable(logContext);
    }

    private static LogContext create(WebSocketMessage webSocketMessage, SSessionJdbc ses) {
        LogContext newLogContext = (LogContext) ses.create(LogContext.LOG_CONTEXT, getLogContextId(webSocketMessage));
        newLogContext.setString(LogContext.USERNAME, webSocketMessage.getUsername());
        newLogContext.setString(LogContext.CLIENT, webSocketMessage.getClient());
        newLogContext.setString(LogContext.TIMESTAMP, DateUtil.formatTimeStamp(new Date()));
        newLogContext.setString(LogContext.TARGET, webSocketMessage.getTarget());
        return newLogContext;
    }

}
