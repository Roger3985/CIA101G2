package com.ren.title.controller;

import com.ren.title.entity.Title;
import com.ren.title.service.impl.TitleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/backend/title")
public class TitleController {

    @Autowired
    private TitleServiceImpl titleSvc;

    @GetMapping("/selectTitle")
    public String toSelectTitle() {
        return "backend/title/selectTitle";
    }

    @GetMapping("/listOneTitle")
    public String listOne(@RequestParam Integer titleNo) {
        return "/backend/title/listOneTitle";
    }

    @GetMapping("/listAllTitles")
    public String listAll() {
        return "backend/title/listAllTitles";
    }

    @GetMapping("/addTitle")
    public String toAddTitle(ModelMap model) {
        model.addAttribute("title", new Title());
        return "backend/title/addTitle";
    }

    @PostMapping("/addTitle/add")
    public String toAddTitle(@Valid Title title,
                             BindingResult result,
                             ModelMap model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("title", title);
            model.addAttribute("errors", result.getAllErrors());
            return "backend/title/addTitle";
        }
        titleSvc.addTitle(title);
        redirectAttributes.addAttribute("success", "新增成功!");
        return "redirect:/backend/title/listAllTitles";
    }

    @GetMapping("/updateTitle/{titleNo}")
    public String toUpdateTitle(@PathVariable Integer titleNo,
                                ModelMap model) {
        model.addAttribute("title", titleSvc.getOneTitle(titleNo));
        return "backend/title/updateTitle";
    }

    @GetMapping("/updateTitle")
    public String toUpdateTitle(@ModelAttribute("titleList") List<Title> list,
                             ModelMap model) {
        model.addAttribute("title", list.get(0));
        return "backend/title/updateTitle";
    }

    @PostMapping("/updateTitle/update")
    public String updateTitle(@Valid Title title,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("title", title);
            model.addAttribute("errors", result.getAllErrors());
            return "backend/title/updateTitle";
        }

        titleSvc.updateTitle(title);
        redirectAttributes.addAttribute("success", "新增成功!");
        return "redirect:/backend/title/listAllTitles";
    }

//    @DeleteMapping("/titles/{titleNo}")
//    public void deleteTitle(@PathVariable Integer titleNo) {
//        titleSvc.deleteTitle(titleNo);
//    }

    @ModelAttribute("titleList")
    protected List<Title> getAllTitles() {
        return titleSvc.getAll();
    }

    /**
     * 去除指定字段的錯誤信息並返回更新後的 BindingResult。
     * 此方法會過濾掉 BindingResult 中與指定字段相關的錯誤信息，然後將剩餘的錯誤信息添加到新的 BindingResult 中。
     *
     * @param title 要操作的 Entity 對象。
     * @param result 目前的 BindingResult，其中包含 Entity 的錯誤信息。
     * @param removedFieldname 要去除錯誤信息的字段名稱。
     * @return 更新後的 BindingResult，其中去除了指定字段的錯誤信息。
     */
    private BindingResult removeFieldError(Title title, BindingResult result, String removedFieldname) {

        // 過濾掉指定字段的錯誤信息
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        // 創建新的 BindingResult，關聯 Entity 物件
        result = new BeanPropertyBindingResult(title, "title");
        // 將過濾後的錯誤信息添加到新的 BindingResult 中
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        // 返回修改後的 BindingResult
        return result;
    }
}
