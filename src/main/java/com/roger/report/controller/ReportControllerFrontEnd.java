package com.roger.report.controller;

import com.roger.member.entity.Member;
import com.roger.report.entity.Report;
import com.roger.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/frontend/report")
public class ReportControllerFrontEnd {

    @Autowired
    ReportService reportService;

    /**
     * 前往新增回覆檢舉頁面。
     * 此方法創建一個新的 `Report` 物件，並將其添加到 `ModelMap` 中，
     * 然後返回用於新增回覆檢舉的視圖名稱。
     *
     * @param modelMap 用於將 `Report` 物件添加到模型中的 `ModelMap`。
     * @return 用於新增回覆檢舉的視圖名稱。
     */
    @GetMapping("/addReportData")
    public String addReportData(ModelMap modelMap) {

        // 創建新的 Report 物件
        Report report = new Report();

        // 將 Report 物件添加到 ModelMap 中
        modelMap.addAttribute("report", report);

        // 返回新增回覆檢舉頁面的視圖名稱
        return "frontend/report/addReport";
    }

    /**
     * 顯示當前登錄會員提交的所有回覆檢舉。
     * 此方法根據當前登錄的會員查找其提交的所有回覆檢舉，並將結果添加到模型中。
     *
     * @param model 用於在視圖中顯示數據的模型。
     * @param session 當前會員的會話，用於從會話中獲取當前登錄的會員。
     * @return 返回顯示會員檢舉列表的視圖名稱。
     */
    @GetMapping("/findAllReportById")
    public String findAllReportById(Model model,
                                    HttpSession session) {
        Member member = (Member) session.getAttribute("loginsuccess");
        List<Report> list = reportService.getAllByMem(member);
        model.addAttribute("reportListDataById", list);
        return "frontend/report/findAllReportById";
    }

    /**
     * 處理新增回覆檢舉的POST請求。
     * 此方法接收來自前端的 `Report` 物件，並通過服務層將其保存到資料庫中。
     * 如果表單驗證失敗，則返回新增頁面以便用戶進行更正。
     * 如果表單驗證成功，則將報告與當前會話的登錄用戶關聯，然後重定向到顯示用戶檢舉列表的頁面。
     *
     * @param report 前端提交的 `Report` 物件，包含新的檢舉數據。
     * @param result `BindingResult` 參數，用於驗證 `report` 物件是否符合前端表單的要求。
     * @param modelMap 用於向視圖傳遞數據的 `ModelMap`。
     * @param session 當前用戶會話，用於獲取當前登錄的用戶。
     * @return 當表單驗證失敗時，返回新增回覆檢舉頁面。當驗證成功時，重定向到用戶的檢舉列表頁面。
     * @throws IOException 當執行過程中發生I/O異常時拋出。
     */
    @PostMapping("/addReport")
    public String addReport(@Valid Report report,
                            BindingResult result,
                            ModelMap modelMap,
                            HttpSession session) throws IOException {

        System.out.println(result);

        // 檢查驗證結果
        if (result.hasErrors()) {
            // 如果表單驗證失敗，返回新增回覆檢舉頁面
            return "frontend/report/addReport";
        }

        // 從會話中獲取當前登錄的會員
        Member member = (Member) session.getAttribute("loginsuccess");

        // 將回覆檢舉與當前登錄用戶關聯
        report.setMember(member);

        // 通過服務層保存回覆檢舉到資料庫中
        reportService.addReport(report);

        // 會取所有的回覆檢舉列表，並從模型中傳遞成功信息
        List<Report> list = reportService.getAll();
        modelMap.addAttribute("success", "- (新增成功)");

        // 重定向到會員的檢舉列表頁面
        return "redirect:/frontend/report/findAllReportById";
    }

    /**
     * 設置模型屬性 `reportListData` 為所有回覆檢舉的列表。
     * 此方法調用 `reportService.getAll()` 獲取所有回覆檢舉的列表，並將該列表設置為模型屬性 `reportListData`。
     * 這樣，所有的報告列表可以在相關的視圖中使用。
     *
     * @return 包含所有回覆檢舉的列表。
     */
    @ModelAttribute("reportListData")
    protected List<Report> referenceListData() {
        List<Report> list = reportService.getAll();
        return list;
    }


}
