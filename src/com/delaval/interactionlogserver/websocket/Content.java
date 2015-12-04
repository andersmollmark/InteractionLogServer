package com.delaval.interactionlogserver.websocket;

import com.delaval.interactionlogserver.util.DateUtil;

import java.util.Date;

/**
 * Describes the content of the log from the webclient.
 */
public class Content {

    //    private String type; TODO needed?
    private String x;
    private String y;
    private String elementId;
    private String cssClassName;
    private String timestamp;

    public String getX() {
        return x;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getY() {
        return y;
    }

    public String getElementId() {
        return elementId;
    }

    public String getCssClassName() {
        return cssClassName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Content:")
                .append("x-pos:").append(getX())
                .append(", y-pos:").append(getY())
                .append(", elementId:").append(getElementId())
                .append(", cssClassName:").append(getCssClassName())
                .append(", time:").append(DateUtil.formatTimeStamp(Long.parseLong(getTimestamp())));
        return sb.toString();
    }


}
