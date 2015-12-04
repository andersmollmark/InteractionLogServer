package com.delaval.interactionlogserver.jms;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * Singleton.
 * Makes it possible to get jms-templates in an easy way.
 */
public class JmsResourceFactory {

    private AnnotationConfigApplicationContext ctx;

    private static JmsResourceFactory _instance;

    private JmsResourceFactory(){
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    public static JmsResourceFactory getInstance() {
        if (_instance == null) {
            synchronized (JmsResourceFactory.class) {
                if (_instance == null) {
                    _instance = new JmsResourceFactory();
                }
            }
        }
        return _instance;
    }


    public AnnotationConfigApplicationContext getCtx(){
        return ctx;
    }

    public JmsTemplate getJmsTemplate(){
        return ctx.getBean(JmsTemplate.class);
    }

    public void closeContext(){
        ctx.close();
    }

}
