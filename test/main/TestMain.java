/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.delaval.interactionlogserver.ServerProperties;
import com.delaval.interactionlogserver.jms.AppConfig;
import com.delaval.interactionlogserver.jms.producer.JmsMessageCreator;
import com.delaval.interactionlogserver.websocket.InteractionLogWebSocket;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author delaval
 */
public class TestMain {
    AnnotationConfigApplicationContext ctx;

    public TestMain(){
        // init spring context
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("app-context.xml");
        // get bean from context
//        jmsMessageSender = (JmsMessageSender) ctx.getBean("jmsMessageSender");


        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
//        sendWithResourceFactory();
        sendWithJmsCreator();
//        sendWithJmsMessageSender();
//        listenToQueue();
        // close spring application context
//        ((ClassPathXmlApplicationContext) ctx).close();
//        ctx.close();
    }

    public void sendWithResourceFactory(){

        String content = "{\"messType\":\"userLog\",\"content\":\"{\\\"x\\\":640,\\\"y\\\":680,\\\"id\\\":\\\"\\\",\\\"className\\\":\\\"row\\\"}\"}";
        InteractionLogWebSocket interactionLogWebSocket = new InteractionLogWebSocket();
        interactionLogWebSocket.handleMessage(null, content);
//        try {
//            JmsMessageCreator messageCreator = new JmsMessageCreator("pinging jms-queue with factory");
//            JmsTemplate jmsTemplate = JmsResourceFactory.getInstance().getJmsTemplate();
//            jmsTemplate.send(AppConfig.JMS_DESTINATION, messageCreator);
//        }
//        finally {
//            JmsResourceFactory.getInstance().closeContext();
//        }
    }

   

    public void sendWithJmsCreator(){
        JmsMessageCreator messageCreator = new JmsMessageCreator("pinging jms-queue");
        JmsTemplate jmsTemplate = ctx.getBean(JmsTemplate.class);
        jmsTemplate.send(ServerProperties.getInstance().getProp(ServerProperties.PropKey.JMS_QUEUE_DEST), messageCreator);
    }

    public void listenToQueue(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        TestMain demoMain = new TestMain();
    }
    
}
