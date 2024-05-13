package com.ren.admauthority.controller;

import com.ren.admauthority.entity.AdmAuthority;
import com.ren.admauthority.service.impl.AdmAuthorityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backend/admauthority")
public class AdmAuthorityController {

    @Autowired
    private AdmAuthorityServiceImpl admAuthoritySvc;

    /**
     * 前往職位權限管理頁面
     *
     * @return forward to 管理頁面路徑
     */
    @GetMapping("/selectAdmAuthority")
    public String toSelect() {
        return "backend/admauthority/selectAdmAuthority";
    }

    /**
     * 前往獲得該職位所有權限清單頁面
     *
     * @param titleNo 職位編號
     * @return
     */
    @GetMapping("/listOneAdmAuthority")
    public String getAdmAuthority(@PathVariable Integer titleNo) {
        return "backend/admauthority/listOneAdmAuthority";
    }

    /**
     *
     *
     * @return
     */
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

    /**
     * 去除指定字段的錯誤信息並返回更新後的 BindingResult。
     * 此方法會過濾掉 BindingResult 中與指定字段相關的錯誤信息，然後將剩餘的錯誤信息添加到新的 BindingResult 中。
     *
     * @param admAuthority 要操作的 Entity 對象。
     * @param result 目前的 BindingResult，其中包含 Entity 的錯誤信息。
     * @param removedFieldname 要去除錯誤信息的字段名稱。
     * @return 更新後的 BindingResult，其中去除了指定字段的錯誤信息。
     */
    private BindingResult removeFieldError(AdmAuthority admAuthority, BindingResult result, String removedFieldname) {

        // 過濾掉指定字段的錯誤信息
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        // 創建新的 BindingResult，關聯 Entity 物件
        result = new BeanPropertyBindingResult(admAuthority, "admAuthority");
        // 將過濾後的錯誤信息添加到新的 BindingResult 中
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        // 返回修改後的 BindingResult
        return result;
    }
    
}
