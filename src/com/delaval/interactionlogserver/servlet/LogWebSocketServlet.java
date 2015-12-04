package com.delaval.interactionlogserver.servlet;

import com.delaval.interactionlogserver.websocket.InteractionLogWebSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet that registers the websocket-class
 */
@SuppressWarnings("serial")
@WebServlet(name = "LogWebsocketServlet WebSocket Servlet", urlPatterns = { "/ws" })
public class LogWebSocketServlet extends WebSocketServlet {

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(InteractionLogWebSocket.class);
    }
}
