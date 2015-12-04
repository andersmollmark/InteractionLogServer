package com.delaval.interactionlogserver.jms.consumer;

import com.delaval.interactionlogserver.persistence.dao.LogDAO;
import com.delaval.interactionlogserver.websocket.WebSocketMessage;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Listens to a jms-queue and process the message as soon it gets a message.
 */
@Service
public class JmsMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsMessageListener.class);
    public static final String NAME_OF_PROCESS_METHOD = "processMessage";

    public void processMessage(String text) {
        // TODO validate message?
        LOGGER.info("processing message:" + text);
        try{
            WebSocketMessage webSocketMessage = new Gson().fromJson(text, WebSocketMessage.class);
            LogDAO.getInstance().createLog(webSocketMessage);
        }
        catch (Exception e){
            LOGGER.error("Something went wrong while pasing json from message:" + e.getMessage());
        }
    }

}