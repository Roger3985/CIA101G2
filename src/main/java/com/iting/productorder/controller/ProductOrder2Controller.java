package com.iting.productorder.controller;

import com.iting.productorder.entity.ProductOrder;
import com.iting.productorder.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Controller
@Validated
@RequestMapping("/frontend/productorder")
public class ProductOrder2Controller {
    @Autowired
    ProductOrderService productOrderSvc;

 }

