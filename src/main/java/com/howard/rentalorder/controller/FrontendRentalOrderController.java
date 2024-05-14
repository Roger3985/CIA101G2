package com.howard.rentalorder.controller;

import com.howard.rentalorder.service.impl.RentalCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontend/rentalorder")
@CrossOrigin(origins = "http://localhost:8080")
public class FrontendRentalOrderController {

    /*--------------------------所有方法共用-------------------------------*/
    @Autowired
    private RentalCartServiceImpl cartService;







}
