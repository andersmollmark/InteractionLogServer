package com.delaval.interactionlogserver.websocket;

import com.delaval.interactionlogserver.ServerProperties;
import com.delaval.interactionlogserver.jms.JmsResourceFactory;
import com.delaval.interactionlogserver.jms.producer.JmsMessageCreator;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 * The websocket that the webclient sends the message to.
 * If the message is a log that shall be saved, it post the message to the jms-queue.
 */
@WebSocket(maxTextMessageSize = 64 * 1024, maxIdleTime = 1000000)
public class InteractionLogWebSocket {

    private static Logger LOGGER = LoggerFactory.getLogger(InteractionLogWebSocket.class);

    @OnWebSocketConnect
    public void onConnect(Session session) {
        LOGGER.info("connecting session:" + (session.getLocalAddress() != null ? session.getLocalAddress().getAddress() : session.toString()));
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        LOGGER.info("closing websocket, status:" + statusCode + " reason:" + reason);
    }

    @OnWebSocketMessage
    public void handleMessage(Session session, String jsonMessage) {
        try {
            WebSocketMessage webSocketMessage = new Gson().fromJson(jsonMessage, WebSocketMessage.class);
            if (getProp(ServerProperties.PropKey.IDLE_POLL).equals(webSocketMessage.getType())) {
                session.getRemote().sendStringByFuture(jsonMessage);
            }
            else if (getProp(ServerProperties.PropKey.INTERACTION_LOG_TYPE).equals(webSocketMessage.getType())) {
                JmsMessageCreator messageCreator = new JmsMessageCreator(jsonMessage);
                JmsTemplate jmsTemplate = JmsResourceFactory.getInstance().getJmsTemplate();
                String jmsDestination = ServerProperties.getInstance().getProp(ServerProperties.PropKey.JMS_QUEUE_DEST);
                jmsTemplate.send(jmsDestination, messageCreator);
            } else {
                LOGGER.info("Unknown message:" + webSocketMessage.getType());
            }

        } catch (Exception e) {
            LOGGER.info("Something bad happened:" + e.getMessage());
        }
    }

    private String getProp(ServerProperties.PropKey propKey){
        return ServerProperties.getInstance().getProp(propKey);
    }
}
