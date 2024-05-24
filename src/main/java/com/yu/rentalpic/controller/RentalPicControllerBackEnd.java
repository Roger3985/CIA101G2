package com.yu.rentalpic.controller;

import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalmyfavorite.entity.RentalMyFavorite;
import com.yu.rentalpic.entity.RentalPic;
import com.yu.rentalpic.service.RentalPicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backend/rentalpic") //對應資料夾路徑
public class RentalPicControllerBackEnd {

    @Autowired  // 自動裝配
    private RentalPicServiceImpl rentalPicService;
    @Autowired  // 自動裝配
    private RentalServiceImpl rentalService;


    //顯示首頁 (後台)
    @GetMapping("/backendIndex")
    public String backendIndex() {
        return "/backend/index";
    }

    //顯示查詢頁面 (後台)
    @GetMapping("/selectRentalPic")
    public String selectPage(ModelMap model) {
        RentalPic rentalPic = new RentalPic();
        rentalPic.setRentalPicNo(0); // 初始化rentalPicNo
        model.addAttribute("rentalPic", rentalPic);
        return "/backend/rentalpic/selectRentalPic";
    }

    //顯示全部租借品照片頁面 (後台)
    @GetMapping("/listAllRentalPic")
    public String listAllRentalPic() {
        return "/backend/rentalpic/listAllRentalPic";
    }


    //顯示新增頁面 (後台)
    @GetMapping("/addRentalPic")
    public String addRentalPic(ModelMap model) {
        RentalPic rentalPic = new RentalPic();
        model.addAttribute("rentalPic", rentalPic);
        return "/backend/rentalpic/addRentalPic";
    }


    //處理單筆查詢(依rentalPicNo)
    @PostMapping("getOneDisplay")
    public String getOneDisplay(@RequestParam("rentalPicNo") Integer rentalPicNo, ModelMap model) {

        RentalPic rentalPic = rentalPicService.findByRentalPicNo(rentalPicNo);
        List<RentalPic> rentalPicList = rentalPicService.findAll();
        model.addAttribute("rentalPicList", rentalPicList);

        List<Rental> rentalListData = rentalService.findAll();
        model.addAttribute("rental", new Rental());
        model.addAttribute("rentalListData", rentalListData);

        if (rentalPic == null) {
            model.addAttribute("errors", "查無資料");
            return "/backend/rentalpic/selectRentalPic";
        }
        model.addAttribute("rentalPic", rentalPic);
        return "/backend/rentalpic/listOneRentalPic";
    }


    //處理單筆修改(依rentalPicNo)
    @PostMapping("getOneUpdate")
    public String getOneUpdate(@RequestParam("rentalPicNo") Integer rentalPicNo, ModelMap model) {

        RentalPic rentalPic = rentalPicService.findByRentalPicNo(rentalPicNo);
        List<RentalPic> rentalPicList = rentalPicService.findAll();
        model.addAttribute("rentalPicList", rentalPicList);

        List<Rental> rentalListData = rentalService.findAll();
        model.addAttribute("rental", new Rental());
        model.addAttribute("rentalListData", rentalListData);

        if (rentalPic == null) {
            model.addAttribute("errors", "查無資料");
            return "/backend/rentalpic/selectRentalPic";
        }
        model.addAttribute("rentalPic", rentalPic);
        return "/backend/rentalpic/updateRentalPic";
    }


    // 處理修改資料
    @PostMapping("updateRentalPic")
    public String updateRentalPic(@Valid RentalPic rentalPic, BindingResult result, ModelMap model,
                                  @RequestParam("rentalFile") MultipartFile[] parts) throws IOException {

        // 使用者未選擇要上傳的圖片時，判斷是否為空值
        if (parts.length == 0 || parts[0].isEmpty()) {
            model.addAttribute("errors", "照片: 請上傳照片");
        } else {
            // 如果不為空值，遍歷所有圖片
            for (MultipartFile multipartFile : parts) {
                byte[] buf = multipartFile.getBytes(); //SpringBoot有提供MultipartFile類負責檔案上傳，藉由getBytes()把檔案轉為位元組陣列
                rentalPic.setRentalFile(buf); //將位元組陣列儲存到實體對象中

                // 將資料添加到 ModelMap 中
                rentalPicService.updateRentalPic(rentalPic);
                rentalPic = rentalPicService.findByRentalPicNo(rentalPic.getRentalPicNo());
                model.addAttribute("rentalPic", rentalPic);
            }
        }
//        // 如果有驗證錯誤，直接返回錯誤
//        if (result.hasErrors()|| parts[0].isEmpty()) {
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            for (int i = 0, len = fieldErrors.size(); i < len; i++) {
//                FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
//                model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
//                model.addAttribute("rentalPic", rentalPic);
//            }
//            return "/backend/rentalpic/addRentalPic";
//        }
        return "/backend/rentalpic/listAllRentalPic";
    }


    // 處理新增
    @PostMapping("/addRentalPic")
    public String addRentalPic(@Valid RentalPic rentalPic, BindingResult result, ModelMap model,
                               @RequestParam("rentalFile") MultipartFile[] parts) throws IOException {
        // 使用者未選擇要上傳的圖片時，判斷是否為空值
        if (parts.length == 0 || parts[0].isEmpty()) {
            model.addAttribute("errors", "照片: 請上傳照片");
            return "/backend/rentalpic/addRentalPic"; // 返回錯誤頁面
        }

        // 如果不為空值，遍歷所有圖片
        for (MultipartFile multipartFile : parts) {
            if (!multipartFile.isEmpty()) {
                RentalPic newRentalPic = new RentalPic(); // 創建新的 RentalPic 對象
                byte[] buf = multipartFile.getBytes(); // SpringBoot有提供MultipartFile類負責檔案上傳，藉由getBytes()把檔案轉為位元組陣列
                newRentalPic.setRentalFile(buf); // 將位元組陣列儲存到實體對象中
                rentalPicService.addRentalPic(newRentalPic); // 將資料添加到數據庫中
            }
        }

        // 返回成功頁面
        return "redirect:/backend/rentalpic/listAllRentalPic"; // 假設成功後轉發到列表頁面
    }

    // 圖片處理 (與設置初始預設圖片)
    // 如果圖片存在，返回圖片的二進制；若不存在返回初始預設圖片
    @GetMapping("/DBGifReader")
    public void DBGifReader(@RequestParam("rentalPicNo") String rentalPicNo, HttpServletResponse response)
            throws IOException {
        // 找到指定編號的圖片，並將圖片數據寫入響應的輸出流
        RentalPic rentalPic = rentalPicService.findByRentalPicNo(Integer.valueOf(rentalPicNo));
        if (rentalPic != null && rentalPic.getRentalFile() != null) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE); // 設定響應內容的類型
            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(rentalPic.getRentalFile());
            }
        } else {
            // 如果找不到指定的圖片，返回404或者其他錯誤頁面
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 返回404響應碼
        }
    }


    //萬用查詢
    @GetMapping("/search")
    public String search(@RequestParam(value = "rentalPicNo", required = false) Integer rentalPicNo,
                         @RequestParam(value = "rentalNo", required = false) Integer rentalNo, ModelMap modelMap) {

        Map<String, Object> map = new HashMap<>();

        if (rentalPicNo != null) {
            map.put("rentalPicNo", rentalPicNo);
        } else if (rentalNo != null) {
            map.put("rentalNo", rentalNo);
        }

        List<RentalPic> rentalPicList = rentalPicService.searchRentalPics(map);
        modelMap.addAttribute("rentalPicList", rentalPicList);
        modelMap.addAttribute("search", "true");

        return "/backend/rentalpic/selectRentalPic";
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與rentalPicNo對應的RentalPic資料庫
     */
    @ModelAttribute("rentalPicData") // 以rentalPicNo搜索，取出對應的RentalPic資料庫
    protected RentalPic referenceData(@RequestParam(value = "rentalPicNo", required = false) Integer rentalPicNo) {
        if (rentalPicNo != null) {
            RentalPic rentalPicList = rentalPicService.findByRentalPicNo(rentalPicNo);
            return rentalPicList; //取得RentalPic列表
        }
        return null;
    }

    /**
     * 提供所有租借品資料列表供視圖渲染使用。
     * 使用 @ModelAttribute 註解，確保在處理請求時可用於視圖中的 rentalPicList 屬性。
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return RentalPic列表。
     */
    @ModelAttribute("rentalPicListData")
    protected List<RentalPic> referenceListData() {
        List<RentalPic> rentalPicList =rentalPicService.findAll();
        return rentalPicList;
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListDataRental()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return rental所有資料庫
     */
    @ModelAttribute("rentalListData")
    protected List<Rental> referenceListDataRental() {
        List<Rental> rentalList = rentalService.findAll();
        return rentalList;
    }


    // 去除BindingResult中某個欄位的FieldError紀錄
    public BindingResult removeFieldError(RentalPic rentalPic, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(rentalPic, "rentalPic");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

}