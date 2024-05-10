//package com.roger.notice.controller;
//
//import com.roger.member.entity.Member;
//import com.roger.member.service.MemberService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.List;
//
//@Controller
//public class NoticeControllerIndex {
//
//    @Autowired
//    MemberService memberService;
//
//    @GetMapping("/getAllMemNos")
//    @ResponseBody
//    public List<Member> getAllMemNos() {
//        return memberService.findAll();
//    }
//}
