package com.listener;

import com.ren.administrator.entity.Administrator;
import com.ren.administrator.service.Impl.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

@Component
public class InitializerListener implements ServletContextListener {

    @Autowired
    private AdministratorServiceImpl administratorSvc;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 新增用於記數在線人數
        sce.getServletContext().setAttribute("onlineUsers", 0);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}