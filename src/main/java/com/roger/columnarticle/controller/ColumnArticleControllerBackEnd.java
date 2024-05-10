package com.roger.columnarticle.controller;

import com.ren.administrator.entity.Administrator;
import com.ren.administrator.service.AdministratorService;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.columnarticle.service.ColumnArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/backend/columnarticle")
public class ColumnArticleControllerBackEnd {

    @Autowired
    ColumnArticleService columnArticleService;

    @Autowired
    AdministratorService administratorService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 處理列出所有專欄文章的請求。
     * 此方法處理對 '/backend/columnarticle/listAllColumnArticle' 的 GET 請求，並返回顯示所有專欄文章的視圖。
     *
     * @param model 用於存儲和傳遞資料到視圖的模型物件。
     * @return `/backend/columnarticle/listAllColumnArticle` 視圖名稱，以顯示所有專欄文章的列表。
     */
    @GetMapping("/listAllColumnArticle")
    public String listAllColumnArticle(Model model) {
        return "backend/columnarticle/listAllColumnArticle";
    }

    /**
     * 前往專欄文章修改頁面。
     * 此方法處理 HTTP GET 請求到 '/backend/columnarticle/updateColumnArticleData' URL 路徑，
     * 並接收文章編號 'artNo'作為請求參數。
     *
     * @param modelMap 包含模型屬性的 'ModelMap'。
     * @param session HTTP 會話物件，用於訪問當前登錄的管理員帳號。
     * @param artNo 專欄文章的編號，作為請求參數傳入。
     * @return 如果管理員未登錄，則重定向至管理員登錄頁面；否則返回專欄文章修改頁面的視圖名稱。
     */
    @GetMapping("/updateColumnArticleData")
    public String updateColumnArticleData(ModelMap modelMap,
                                          HttpSession session,
                                          @RequestParam("artNo") String artNo) {

        // 從會話中獲取當前登錄的管理員
        // TODO 因為現在管理員頁面還沒做出來，所以先進行註解，之後需要再打開
//        Administrator currentAdmin = (Administrator) session.getAttribute("ValidAdministrator");
//        if (currentAdmin == null) {
//            // 未登錄管理員帳號，重定向到管理員的頁面
//            return "redirect:/backend/administrator/loginAdministrator";
//        }

        System.out.println(artNo);
        // 使用 artNo 查找對應的專欄文章
        ColumnArticle oldColumnArticle = columnArticleService.getOneColumnArticle(Integer.valueOf(artNo));
        System.out.println("test" + oldColumnArticle);

        // 檢查文章是否存在
//        if (oldColumnArticle == null) {
//            // 如果專欄文章不存在，則可以根據需要返回的錯誤頁面或進行其他的處理
//            modelMap.addAttribute("error", "找不到指定的文章。");
//            // TODO 尚未製作
//            return "backend/columnarticle/error";
//        }

        modelMap.addAttribute("data", oldColumnArticle);

        return "backend/columnarticle/updateColumnArticle";

    }

    /**
     * 更新專欄文章。
     *
     * @param columnArticle 專欄文章資料。
     * @param result 用來檢查表單資料的驗證結果。
     * @param modelMap 用來傳遞資料到視圖。
     * @param session 用來儲存會話狀態的會話物件。
     * @return 更新成功後的重定向路徑。
     * @throws IOException 如果發生 IO 錯誤。
     */
    @Transactional
    @PostMapping("/updateColumnArticle")
    public String updateColumnArticle(@ModelAttribute("data") @Valid ColumnArticle columnArticle,
                                      BindingResult result,
                                      ModelMap modelMap,
                                      HttpSession session) throws IOException {

        // FIXME 以下兩行非必要，因為在前往修改頁面的時候就已經擋掉沒有登入管理員
//        // 獲取當前會話中管理員設置的舊專欄文章資料
//        Administrator currentAdmin = (Administrator) session.getAttribute("ValidAdministrator");
//        // 設置管理專欄文章的管理員為當前登錄的管理員
//        columnArticle.setAdministrator(currentAdmin);

        if (result.hasErrors()) {
            // 紀錄驗證錯誤日誌
            log.error("驗證錯誤：{}", result.getAllErrors());
            // 將錯誤資訊和管理員提交資料添加到 modelMap 中，以便在視圖中顯示
            modelMap.addAttribute("errors", result.getAllErrors());
            modelMap.addAttribute("data", columnArticle);
            // 回到更新專欄文章的頁面
            return "backend/columnarticle/updateColumnArticle";
        }

        // 更新專欄文章
        ColumnArticle updateColumnArticle = columnArticleService.updateColumnArticle(columnArticle);

        // 將更新後的專欄文章資料添加到 modelMap 中，鍵為 "successData"
        // 這樣就可以將更新後的專欄文章資料傳遞到視圖層
        modelMap.addAttribute("successData", updateColumnArticle);

        // 如果更新成功，重定向到全部的專欄文章頁面
        return "redirect:/backend/columnarticle/listAllColumnArticle";
    }

    /**
     * 更新專欄文章的狀態（上下架）。
     * 該方法根據前端傳入的專欄文章編號（`artNo`）查找相關的專欄文章，
     * 然後根據該專欄文章的狀態（`artStat`）切換其上下架狀態。
     * 如果當前狀態為上架（`artStat = 0`），則設置為下架（`artStat = 1`），並將文章編號存儲到 Redis 中。
     * 如果當前狀態為下架（`artStat = 1`），則設置為上架（`artStat = 0`），並從 Redis 中刪除相關鍵。
     * 最後，重定向到專欄文章列表頁面。
     *
     * @param artNo 前端傳入的專欄文章編號。
     * @return 重定向到專欄文章列表頁面的 URL。
     */
    @Transactional
    @PostMapping("/updateColumnArticleStat")
    public String updateColumnArticleStat(@ModelAttribute("artNo") String artNo) {

        System.out.println(artNo);

        Integer articleNumber = null;

        try {
            articleNumber = Integer.valueOf(artNo);
        } catch (NumberFormatException e) {
            // 使用紀錄錯誤日誌
            log.error("專欄文章編號格式有誤:{}", artNo, e);
//            return "redirect:/backend/columnarticle/listAllColumnArticle";
        }

        // 查找與文章編號相關的專欄文章
        ColumnArticle columnArticle = columnArticleService.findColumnArticleByArtNo(Integer.valueOf(artNo));
        if (columnArticle == null) {
            // 使用 log 紀錄錯誤日誌
            log.error("找不到專欄文章:{}", artNo);
            return "redirect:/backend/columnarticle/listAllColumnArticle";
        }

        String redisKey = "noFun:columnarticles" + artNo;

        if (columnArticle.getArtStat().equals(Byte.valueOf("0"))) {

            // 將文章編號的文章狀態設置為下架(上架->下架)
            columnArticle.setArtStat(Byte.valueOf("1"));

            // 更新 Redis 儲存
            redisTemplate.opsForValue().set("noFun:columnarticles" + artNo, artNo);

        } else if (columnArticle.getArtStat().equals(Byte.valueOf("1"))) {

            // 將文章編號的文章狀態設置為上架(下架->上架)
            columnArticle.setArtStat(Byte.valueOf("0"));

            // 檢查 Redis 中的鍵是否存在，如果存在則刪除
            redisTemplate.delete("noFun:columnarticles" + artNo);

        }

        // 更新專欄文章
        columnArticleService.edit(columnArticle);

        // 重定向到專欄文章列表頁面
        return "redirect:/backend/columnarticle/listAllColumnArticle";
    }

    /**
     * 提供所有專欄文章資料列表供視圖渲染使用。
     * 此方法使用`@ModelAttribute`註解，確保在處理請求時可用於視圖中的`columnArticleListData`屬性。
     * 該屬性是一個包含所有專欄文章的列表，由`columnArticleService.findAll()`方法獲取。
     *
     * @return 包含所有專欄文章的列表。
     */
    @ModelAttribute("columnArticleListData")
    protected List<ColumnArticle> referenceListData() {
        List<ColumnArticle> list = columnArticleService.findAll();
        return list;
    }

    @ModelAttribute("administratorListData")
    protected List<Administrator> referenceListData2() {
        List<Administrator> list = administratorService.findAll();
        return list;
    }

}
