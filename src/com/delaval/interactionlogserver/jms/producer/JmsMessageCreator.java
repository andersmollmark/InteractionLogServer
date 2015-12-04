package com.delaval.interactionlogserver.jms.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * This is a datacarrier/wrapper when posting jms-message to queue
 */
public class JmsMessageCreator implements MessageCreator {

    private String message;
    private static final Logger LOGGER = LoggerFactory.getLogger(JmsMessageCreator.class);

    public JmsMessageCreator(String message){
        this.message = message;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        LOGGER.info("creating message with content:" + message);
        return session.createTextMessage(message);
    }
}
