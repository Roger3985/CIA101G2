package com.Cia101G2;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    public String handleConversionFailedException(ConversionFailedException ex,
                                                  RedirectAttributes attributes,
                                                  HttpServletRequest req) {
//        System.out.println("aaaaaaaaaa");
        String rentalNo = req.getParameter("rentalNo");
        String memNo = req.getParameter("memNo");
        String redirectUrl = req.getParameter("redirectUrl");

        attributes.addFlashAttribute("rentalNo", rentalNo);
        attributes.addFlashAttribute("memNo", memNo);
        attributes.addFlashAttribute("wrongDate", "請輸入日期!");
        return "redirect:" + redirectUrl;
    }

}
