package com.listener;

import com.ren.administrator.entity.Administrator;
import com.ren.administrator.service.Impl.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

@Component
public class InitializerListener implements ServletContextListener {

    @Autowired
    private AdministratorServiceImpl administratorSvc;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 獲得ServletConext物件
        ServletContext context = sce.getServletContext();
        // 新增一個用於記錄在線管理員的參數
        context.setAttribute("onlineAdministrators", 0);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}