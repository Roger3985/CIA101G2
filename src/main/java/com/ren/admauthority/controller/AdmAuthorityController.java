package com.ren.admauthority.controller;

import com.ren.admauthority.entity.AdmAuthority;
import com.ren.admauthority.service.impl.AdmAuthorityServiceImpl;
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
@RequestMapping("/backend/admauthority")
public class AdmAuthorityController {

    @Autowired
    private AdmAuthorityServiceImpl admAuthoritySvc;

    @Autowired
    private TitleServiceImpl titleSvc;

    @Autowired
    private AuthorityFunctionServiceImpl authorityFunctionSvc;

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
     * @return 前往listOne頁面
     */
    @GetMapping("/listOneAdmAuthority")
    public String getAdmAuthority(@RequestParam Integer titleNo,
                                  @RequestParam Integer authFuncNo,
                                  BindingResult result,
                                  ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMsg", "查無此資料");
            return "redirect:/backend/";
        }
        AdmAuthority admAuthority = admAuthoritySvc.getOneAdmAuthority(titleNo, authFuncNo);
        model.addAttribute("admAuthority", admAuthority);
        return "backend/admauthority/listOneAdmAuthority";
    }

    /**
     * 前往所有管理員權限清單
     *
     * @return 前往所有管理員權限
     */
    @GetMapping("/listAllAdmAuthorities")
    public String getAllAdmAuthorities() {
        return "backend/admauthority/listAllAdmAuthorities";
    }

    @GetMapping("/addAdmAuthority")
    public String toAddAdmAuthority(ModelMap model) {
        model.addAttribute("admAuthority", new AdmAuthority());
        model.addAttribute("titleList", titleSvc.getAll());
        model.addAttribute("authFunctionList", authorityFunctionSvc.getAll());
        return "backend/admauthority/addAdmAuthority";
    }

    @PostMapping("/addAdmAuthority/add")
    public String addAdmAuthority(@Valid AdmAuthority admAuthority,
                                  RedirectAttributes redirectAttributes) {
        admAuthoritySvc.addAdmAuthority(admAuthority);
        redirectAttributes.addAttribute("success", "新增成功!");

        return "redirect:/backend/admauthority/listAllAdmAuthorities";
    }

    // 從listAll前往
    @GetMapping("/updateAdmAuthority/{titleNo}/{authFuncNo}")
    public String toUpdateAdmAuthority(@PathVariable Integer titleNo,
                                       @PathVariable Integer authFuncNo,
                                       ModelMap model) {
        model.addAttribute("admAuthority", admAuthoritySvc.getOneAdmAuthority(titleNo, authFuncNo));
        model.addAttribute("titleList", titleSvc.getAll());
        model.addAttribute("authFunctionList", authorityFunctionSvc.getAll());
        return "backend/admauthority/updateAdmAuthority";
    }

    // 從側邊欄前往
    @GetMapping("/updateAdmAuthority")
    public String toUpdateAdmAuthority(@ModelAttribute("admAuthorityList") List<AdmAuthority> list,
                                       ModelMap model) {
        model.addAttribute("admAuthority", list.get(0));
        model.addAttribute("titleList", titleSvc.getAll());
        model.addAttribute("authFunctionList", authorityFunctionSvc.getAll());
        return "backend/admauthority/updateAdmAuthority";
    }

    @PutMapping("/updateAdmAuthority/update")
    public String updateAdmAuthority(@Valid AdmAuthority admAuthority,
                                     RedirectAttributes redirectAttributes) {
        admAuthoritySvc.updateAdmAuthority(admAuthority);
        redirectAttributes.addAttribute("success", "修改成功!");

        return "redirect:/backend/admauthority/listAllAdmAuthorities";
    }

    @DeleteMapping("/delete")
    public void deleteFromListAll(@RequestParam Integer titleNo, @RequestParam Integer authFuncNo) {
        admAuthoritySvc.deleteAdmAuthority(titleNo, authFuncNo);
    }

    @ModelAttribute("admAuthorityList")
    protected List<AdmAuthority> getAllData() {
        return admAuthoritySvc.getAll();
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
