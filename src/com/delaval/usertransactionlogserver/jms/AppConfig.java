package com.delaval.usertransactionlogserver.jms;

import com.delaval.usertransactionlogserver.ServerProperties;
import com.delaval.usertransactionlogserver.jms.consumer.JmsMessageListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.ConnectionFactory;

/**
 * Sets up the configuration for Spring-jms.
 * Configures which bean shall listen to the queue, the name of the queue and connectionfactory for ActiveMQ.
 */
@Configuration
public class AppConfig {

    @Bean
    ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(
                new ActiveMQConnectionFactory(ServerProperties.getInstance().getProp(ServerProperties.PropKey.JMS_CONNECTION)));
    }

    @Bean
    MessageListenerAdapter receiver() {
        return new MessageListenerAdapter(new JmsMessageListener()) {{
            setDefaultListenerMethod(JmsMessageListener.NAME_OF_PROCESS_METHOD);
        }};
    }

    @Bean
    JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }

    @Bean
    SimpleMessageListenerContainer container(final MessageListenerAdapter messageListener,
                                             final ConnectionFactory connectionFactory) {
        return new SimpleMessageListenerContainer() {{
            setMessageListener(messageListener);
            setConnectionFactory(connectionFactory);
            setDestinationName(ServerProperties.getInstance().getProp(ServerProperties.PropKey.JMS_QUEUE_DEST));
        }};
    }


}
