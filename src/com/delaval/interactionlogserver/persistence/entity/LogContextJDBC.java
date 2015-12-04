package com.delaval.interactionlogserver.persistence.entity;

import com.delaval.interactionlogserver.util.DateUtil;
import com.delaval.interactionlogserver.websocket.WebSocketMessage;
import simpleorm.dataset.SFieldString;
import simpleorm.dataset.SFieldTimestamp;
import simpleorm.dataset.SRecordMeta;
import simpleorm.sessionjdbc.SSessionJdbc;

import java.util.Date;
import java.util.Optional;

import static simpleorm.dataset.SFieldFlags.SDESCRIPTIVE;
import static simpleorm.dataset.SFieldFlags.SPRIMARY_KEY;

/**
 * Entity that mirrors LogContext-table
 */
public class LogContextJDBC {


    private String id;
    private String username;
    private String target;
    private String client;
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
