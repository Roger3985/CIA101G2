package com.ren.authorityfunction.controller;

import com.Entity.AuthorityFunction;
import com.ren.authorityfunction.service.AuthorityFunctionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/backend/authorityFunction")
public class AuthorityFunctionController {

    @Autowired
    private AuthorityFunctionServiceImpl authorityFunctionSvc;

    @GetMapping("/listOneAuthorityFunction")
    public AuthorityFunction getAuthorityFunction(@PathVariable Integer authFuncNo) {
        return authorityFunctionSvc.getOneAuthorityFunction(authFuncNo);
    }

    @GetMapping("/listAllAuthorityFunction")
    public List<AuthorityFunction> getAllAuthorityFunctions() {
        return authorityFunctionSvc.getAll();
    }

    @PostMapping("/addAuthorityFunction")
    public AuthorityFunction addAuthorityFunction(@RequestBody AuthorityFunction authorityFunction) {
        return authorityFunctionSvc.addAuthorityFunction(authorityFunction);
    }

    @PutMapping("/updateAuthorityFunction")
    public AuthorityFunction updateAuthorityFunction(@PathVariable Integer authFuncNo, @RequestBody AuthorityFunction authorityFunction) {
        // Ensure the productNo in the path matches the productNo in the request body
        if (!authFuncNo.equals(authorityFunction.getAuthFuncNo())) {
            throw new IllegalArgumentException("Path variable productNo must match the productNo in the request body");
        }
        return authorityFunctionSvc.updateAuthorityFunction(authorityFunction);
    }

//    @DeleteMapping("/authorityFunctions/{authFuncNo}")
//    public void deleteAuthorityFunction(@PathVariable Integer authFuncNo) {
//        authorityFunctionSvc.deleteAuthorityFunction(authFuncNo);
//    }

}
