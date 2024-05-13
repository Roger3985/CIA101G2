package com.ren.authorityfunction.controller;

import com.ren.admauthority.entity.AdmAuthority;
import com.ren.authorityfunction.entity.AuthorityFunction;
import com.ren.authorityfunction.service.AuthorityFunctionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 去除指定字段的錯誤信息並返回更新後的 BindingResult。
     * 此方法會過濾掉 BindingResult 中與指定字段相關的錯誤信息，然後將剩餘的錯誤信息添加到新的 BindingResult 中。
     *
     * @param authorityFunction 要操作的 Entity 對象。
     * @param result 目前的 BindingResult，其中包含 Entity 的錯誤信息。
     * @param removedFieldname 要去除錯誤信息的字段名稱。
     * @return 更新後的 BindingResult，其中去除了指定字段的錯誤信息。
     */
    private BindingResult removeFieldError(AuthorityFunction authorityFunction, BindingResult result, String removedFieldname) {

        // 過濾掉指定字段的錯誤信息
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        // 創建新的 BindingResult，關聯 Entity 物件
        result = new BeanPropertyBindingResult(authorityFunction, "authorityFunction");
        // 將過濾後的錯誤信息添加到新的 BindingResult 中
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        // 返回修改後的 BindingResult
        return result;
    }

}
