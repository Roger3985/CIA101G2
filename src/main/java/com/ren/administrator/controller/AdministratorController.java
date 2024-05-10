package com.ren.administrator.controller;

import com.Entity.Administrator;
import com.ren.administrator.service.AdministratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.ren.util.Constants.FIRST;

@Controller
@RequestMapping("/backend/administrator")
public class AdministratorController {

    @Autowired
    private AdministratorServiceImpl administratorSvc;

    /**
     * 前往管理員頁面
     *
     * @return 管理員頁面
     */
    @GetMapping("/selectAdministrator")
    public String toSelect() {
        return "backend/administrator/selectAdministrator";
    }

    // 查看管理員資料
    @GetMapping("/listOneAdministrator")
    public String getAdministrator(@PathVariable Integer admNo, ModelMap modelMap) {
        administratorSvc.getOneAdministrator(admNo);
        return "backend/administrator/listOneAdministrator";
    }

    /**
     * 前往管理員清單
     *
     * @return
     */
    @GetMapping("/listAllAdministrators")
    public String listAll() {
        return "backend/administrator/listAllAdministrators";
    }

    /**
     * 前往新增頁面
     *
     * @param model 用於將Administrator物件傳到前端做資料封包
     * @return forward to addAdministrator
     */
    @GetMapping("/addAdministrator")
    public String toAddAdministrator(Model model) {
        model.addAttribute("Administrator", new Administrator());
        return "backend/administrator/addAdministrator";
    }

    /**
     * 此為有新增資料權限的管理員新增資料的方法
     *
     * @param administrator 前台輸入資料(在view層使用thymeleaf tag封包)，如果輸入格式錯誤，將返回View，不讓使用者再次輸入相同內容
     * @param result 錯誤訊息，於方法內確認是否輸入格式有無錯誤，如果有則傳回新增頁面
     * @param model 用於將前端輸入資料與錯誤訊息導至前端
     * @return 格式錯誤forward到新增頁面，成功則forward到列表
     */
    @PostMapping("addAdministrator/add")
    public String addAdministrator(@Valid Administrator administrator, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("administrator", administrator);
            model.addAttribute("errors", result.getAllErrors());
            return "backend/administrator/addAdministrator";
        } else {
            System.out.println("新增成功");
        }
        administratorSvc.addAdministrator(administrator);
        return "backend/administrator/listAllAdministrators";
    }

    // 從listAll那前往
    @GetMapping("/updateAdministrator/{administratorNo}")
    public String toUpdateAdministrator(ModelMap model, @PathVariable Integer administratorNo) {

        model.addAttribute("administrator", administratorSvc.getOneAdministrator(administratorNo));

        return "backend/administrator/updateAdministrator";
    }

    /**
     * 點選側邊欄連結前往更新頁面
     *
     * @param model
     * @return
     */
    @GetMapping("/updateAdministrator")
    public String toUpdateProduct(ModelMap model) {
        List<Administrator> list = administratorSvc.getAll();
        model.addAttribute("AdministratorList", list);
        model.addAttribute("Administrator", list.get(FIRST));
        return "backend/administrator/updateAdministrator";
    }


    // 更新管理員
    @PutMapping("update")
    public Administrator updateAdministrator(@PathVariable Integer admNo, @RequestBody Administrator administrator) {
        // Ensure the productNo in the path matches the productNo in the request body
        if (!admNo.equals(administrator.getAdmNo())) {
            throw new IllegalArgumentException("Path variable productNo must match the productNo in the request body");
        }
        return administratorSvc.updateAdministrator(administrator);
    }

    // 刪除管理員
    @DeleteMapping("/administrators/{admNo}")
    public void deleteAdministrator(@PathVariable Integer admNo) {
        administratorSvc.deleteAdministrator(admNo);
    }

    /**
     * 將所有管理員資料渲染到前端頁面
     *
     * @return 所有管理員資料到訪問 /backend/administrator url的視圖
     */
    @ModelAttribute("administratorList")
    protected List<Administrator> getAllAdministrators(){
        List<Administrator> list =administratorSvc.getAll();
        return list;
    }

    @PostMapping("/register")
    public String register(@Valid Administrator administrator, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("administrator", administrator);
            model.addAttribute("errors", result.getAllErrors());
            return "backend/product/updateProduct";
        }
        administratorSvc.register(administrator);
        model.addAttribute("registerSuccess", "註冊成功!");
        return "redirect:backend/login";
    }

    // 審核註冊
    // 設立四種權限
    // 唯讀，可查詢跟新增，可查詢跟新增跟修改，可查詢跟新增跟修改跟刪除這四個階級

    // 寄出修改通知
//    @PostMapping("/")
//    public

    // 核准下級請求

}