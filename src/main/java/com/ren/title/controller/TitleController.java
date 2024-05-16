package com.ren.title.controller;

import com.ren.title.entity.Title;
import com.ren.title.service.impl.TitleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backend/title")
public class TitleController {

    @Autowired
    private TitleServiceImpl titleSvc;

    @GetMapping
    public String toSelectTitle() {
        return "backend/title/selectTitle";
    }

    @GetMapping("/listOneTitle")
    public String listOne(@PathVariable Integer titleNo) {
        return "/backend/title/listOneTitle";
    }

    @GetMapping("/listAllTitles")
    public List<Title> listAll() {
        return titleSvc.getAll();
    }

    @PostMapping("/addTitle")
    public Title addTitle(@RequestBody Title title) {
        return titleSvc.addTitle(title);
    }

    @PutMapping("/updateTitle")
    public Title updateTitle(@PathVariable Integer titleNo, @RequestBody Title title) {
        // Ensure the productNo in the path matches the productNo in the request body
        if (!titleNo.equals(title.getTitleNo())) {
            throw new IllegalArgumentException("Path variable productNo must match the productNo in the request body");
        }
        return titleSvc.updateTitle(title);
    }

//    @DeleteMapping("/titles/{titleNo}")
//    public void deleteTitle(@PathVariable Integer titleNo) {
//        titleSvc.deleteTitle(titleNo);
//    }

    @ModelAttribute("/")
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
