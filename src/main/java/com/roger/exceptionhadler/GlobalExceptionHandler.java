//package com.roger.exceptionhadler;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.ModelAndView;
//
///**
// * 全局異常處理器。
// * 此類使用 '@ControllerAdvice' 註解，對應用程式中的異常進行全局處理。
// * 根據異常類型定向到適當的錯誤頁面，並將錯誤消息傳遞到頁面。
// *
// * @Author Roger
// */
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    /**
//     * 處理 `NumberFormatException` 類型的異常。
//     * 當發生 `NumberFormatException` 時，定向到指定的錯誤頁面，並將錯誤消息傳遞到頁面。
//     *
//     * @param ex    `NumberFormatException` 異常物件。
//     * @param model 用於存儲模型數據的 `Model` 物件。
//     * @return      指定的視圖名稱和模型資料。
//     */
//    @ExceptionHandler(NumberFormatException.class)
//    public ModelAndView hadleNumberFormatException(NumberFormatException ex, Model model) {
//        // 創建 ModelAndView 物件，指定要返回的視圖名稱(例如:錯誤頁面)
//        ModelAndView modelAndView = new ModelAndView("/error/error");
//        // 設置模型資料，傳遞錯誤消息
//        model.addAttribute("errorMessage", "專欄文章編號格式錯誤：" + ex.getMessage());
//        // 返回 ModelAndView 物件
//        return modelAndView;
//    }
//
//    /**
//     * 處理其他類型的異常。
//     * 當發生其他異常時，定向到指定的錯誤頁面，並將錯誤消息傳遞到頁面。
//     *
//     * @param ex    異常對象。
//     * @param model 用於存儲模型數據的 `Model` 對象。
//     * @return      指定的視圖名稱和模型數據。
//     */
//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleException(Exception ex, Model model) {
//        // 創建 ModelAndView 物件，指定要返回的視圖名稱（例如錯誤頁面）
//        ModelAndView modelAndView = new ModelAndView("/error/error-404");
//        // 設置模型資料，傳遞錯誤消息
//        model.addAttribute("errorMessage", "發生錯誤：" + ex.getMessage());
//        // 返回 ModelAndView 物件
//        return modelAndView;
//    }
//}
