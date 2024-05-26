package com.ren.monitor.rabbitmq.controller;

import com.ren.monitor.rabbitmq.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 用於動態註冊佇列用(提供專屬的佇列)
@Controller
@RequestMapping("/backend")
public class RabbitController {

    @Autowired
    private RabbitService rabbitSvc;

    @PostMapping("/createUserQueue")
    public ResponseEntity<String> createUserQueue(@RequestParam("userAdmNo") String userAdmNo) {
        rabbitSvc.createQueueForUser(userAdmNo);
        return ResponseEntity.status(HttpStatus.OK).body("已建立專屬佇列");
    }
}
