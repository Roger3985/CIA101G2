package com.ren.authorityfunction.controller;

import com.ren.authorityfunction.entity.AuthorityFunction;
import com.ren.authorityfunction.service.impl.AuthorityFunctionServiceImpl;
import com.ren.title.service.impl.TitleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backend/authorityfunction")
public class AuthorityFunctionController {

    @Autowired
    private AuthorityFunctionServiceImpl authorityFunctionSvc;

    @Autowired
    private TitleServiceImpl titleSvc;

    @GetMapping("/listOneAuthorityFunction")
    public AuthorityFunction getAuthorityFunction(@PathVariable Integer authFuncNo) {
        return authorityFunctionSvc.getOneAuthorityFunction(authFuncNo);
    }

    @GetMapping("/selectAuthorityFunction")
    public String toSelectAuthorityFunction() {
        return "backend/authorityfunction/selectAuthorityFunction";
    }

    @GetMapping("/listAllAuthorityFunctions")
    public String getAllAuthorityFunctions() {
        return "backend/authorityfunction/listAllAuthorityFunctions";
    }

    @GetMapping("/addAuthorityFunction")
    public String toAddAuthorityFunction(ModelMap model) {
        model.addAttribute("authorityFunction", new AuthorityFunction());
        return "backend/authorityfunction/addAuthorityFunction";
    }

    @PostMapping("/addAuthorityFunction/add")
    public String addAuthorityFunction(@Valid AuthorityFunction authorityFunction,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("authorityFunction", authorityFunction);
            model.addAttribute("errors", result.getAllErrors());
            return "backend/authorityfunction/addAuthorityFunction";
        }
        authorityFunctionSvc.addAuthorityFunction(authorityFunction);
        redirectAttributes.addAttribute("success", "新增成功!");
        return "redirect:/backend/authorityfunction/listAllAuthorityFunctions";
    }

    @PutMapping("/updateAuthorityFunction/{authFuncNo}")
    public String toUpdateAuthorityFunction(@PathVariable Integer authFuncNo,
                                                       ModelMap model) {
        model.addAttribute("authorityFunction", authorityFunctionSvc.getOneAuthorityFunction(authFuncNo));
        return "backend/authorityfunction/updateAuthorityFunction";
    }

    @GetMapping("/updateAuthorityFunction")
    public String toUpdateAuthorityFunction(@ModelAttribute("authorityFunctionList") List<AuthorityFunction> list,
                                            ModelMap model) {
        model.addAttribute("authorityFunction", list.get(0));
        return "backend/authorityfunction/updateAuthorityFunction";
    }

    @PostMapping("/updateAuthorityFunction/update")
    public String toUpdateAuthorityFunction(@Valid AuthorityFunction authorityFunction,
                                            BindingResult result,
                                            ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("authorityFunction", authorityFunction);
            model.addAttribute("errors", result.getAllErrors());
            return "backend/authirityfunction";
        }
        authorityFunctionSvc.updateAuthorityFunction(authorityFunction);
        return "redirect:/backend/authorityfunction/listAllAuthorityFunctions";
    }

    @ModelAttribute("authorityFunctionList")
    public List<AuthorityFunction> getAuthorityFunctionList() {
        return authorityFunctionSvc.getAll();
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
