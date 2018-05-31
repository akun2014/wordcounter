package com.example.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by akun on 2018/5/25.
 */
@WebListener
@Slf4j
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("{} init...", this.getClass().getSimpleName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
