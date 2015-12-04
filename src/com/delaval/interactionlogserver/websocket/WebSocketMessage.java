package com.delaval.interactionlogserver.websocket;

/**
 * Dataholder for the message from the webclient
 */
public class WebSocketMessage {

    private String jsonContent;
    private String client;
    private String username;
    private String messType;
    private String target;

    public String getJsonContent() {
        return jsonContent;
    }

    public String getClient() {
        return client;
    }

    public String getTarget() {
        return target;
    }

    public String getUsername() {
        return username;

    }

    public String getMessType() {
        return messType;
    }

    public String getType() {

        return messType;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Messtype:").append(getType())
                .append(", client:" + getClient())
                .append(", username:" + getUsername())
                .append(", target:" + getTarget())
                .append(", Content:").append(getJsonContent());
        return sb.toString();
    }
}
