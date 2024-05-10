package com.ren.admauthority.controller;

import com.Entity.AdmAuthority;
import com.ren.admauthority.service.AdmAuthorityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/backend/admauthority")
public class AdmAuthorityController {

    @Autowired
    private AdmAuthorityServiceImpl admAuthoritySvc;

    @GetMapping("/selectAdmAuthority")
    public String toSelect() {
        return "backend/admauthority/selectAdmAuthority";
    }

    @GetMapping("/listOneAdmAuthority")
    public String getAdmAuthority(@PathVariable Integer titleNo) {
        return "backend/admauthority/listOneAdmAuthority";
    }

    @GetMapping("/listAllAdmAuthorities")
    public String getAllAdmAuthorities() {
        return "backend/admauthority/listAllAdmAuthorities";
    }

    @PostMapping("/addAdmAuthority")
    public String addAdmAuthority(@RequestBody AdmAuthority admAuthority) {
        return "";
    }

    @PutMapping("/updateAdmAuthority")
    public String updateAdmAuthority(@PathVariable Integer titleNo, @RequestBody AdmAuthority admAuthority) {
        // Ensure the productNo in the path matches the productNo in the request body
        if (!titleNo.equals(admAuthority.getTitle().getTitleNo())) {
            throw new IllegalArgumentException("Path variable productNo must match the productNo in the request body");
        }
        return "";
    }

//    @DeleteMapping("/titles/{titleNo}")
//    public void deleteAdmAuthority(@PathVariable Integer titleNo) {
//        admAuthoritySvc.deleteAdmAuthority(titleNo);
//    }

    @ModelAttribute("admAuthorityList")
    protected List<AdmAuthority> getAllData() {
        List<AdmAuthority> list = admAuthoritySvc.getAll();
        return list;
    }
    
}
