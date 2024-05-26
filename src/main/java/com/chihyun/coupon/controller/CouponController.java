package com.chihyun.coupon.controller;

import com.chihyun.coupon.entity.Coupon;
import com.chihyun.coupon.model.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backend/coupon") //此路徑會長到下面mapping路徑的前面
public class CouponController {

    @Autowired
    CouponService couponSvc;

    @GetMapping("/selectCoupon")
    public String select_page(Model model) {
        return "backend/coupon/selectCoupon";
    }

    /*
     * This method will serve as addEmp.html handler.
     */
    @GetMapping("addCoupon") // /coupon/addCoupon
    public String addCoupon(ModelMap model) {
        Coupon coupon = new Coupon();
        model.addAttribute("coupon", coupon);
        return "backend/coupon/addCoupon";
    }

    /*
     * This method will be called on add Coupon.html form submission, handling POST request It also validates the user input
     */
    @PostMapping("/insert")
    public String insert(@Valid Coupon coupon, BindingResult result, ModelMap modelMap,
                         @RequestParam(name = "coupExpDate") String coupExpDate,
                         @RequestParam(name = "coupRelDate") String coupRelDate) throws IOException {
//
//        if (result.hasFieldErrors()) {
//            List<String> errorMessage = new ArrayList<>();
//            for (FieldError error : result.getFieldErrors()) {
//                errorMessage.add(error.getDefaultMessage());
//                modelMap.addAttribute("errors", errorMessage);
//                return "backend/coupon/addCoupon";
//            }
//        }

        result = removeFieldError(coupon, result, "coupExpDate");
        result = removeFieldError(coupon, result, "coupRelDate");

        Timestamp coupExpDateParm = null;
        Timestamp coupRelDateParm = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            Date parseDate1 = dateFormat.parse(coupExpDate); // Thu May 02 14:42:00 CST 2024
            Date parseDate2 = dateFormat.parse(coupRelDate);

            coupExpDateParm = new Timestamp(parseDate1.getTime()); // 2024-05-02 14:42:00.0
            coupRelDateParm = new Timestamp(parseDate2.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Timestamp today = Timestamp.valueOf(LocalDateTime.now());

        if (!modelMap.containsAttribute("errorMessage")) {
            modelMap.addAttribute("errorMessage", "");
        }


        if (coupExpDate.isEmpty()) {
            modelMap.addAttribute("errorMessage1", "失效日期請勿空白");
            return "backend/coupon/addCoupon";
        } else {
            coupon.setCoupExpDate(coupExpDateParm);
        }

        if (coupRelDate.isEmpty()) {
            modelMap.addAttribute("errorMessage2", "發放日期請勿空白");
            return "backend/coupon/addCoupon";
        }
//        else if (coupExpDateParm.compareTo(coupRelDateParm) <= 0) {
//            modelMap.addAttribute("errorMessage2", "優惠券發放日期不得早於失效日期");
//            return "backend/coupon/addCoupon";
//        }
        else {
            coupon.setCoupRelDate(coupRelDateParm);
        }

        if (result.hasErrors()) {
            return "backend/coupon/addCoupon";
        }
        System.out.println(modelMap);
        System.out.println(result); // org.springframework.validation.BeanPropertyBindingResult: 0 errors
        System.out.println(coupExpDate); // 2024-05-02T14:42
        System.out.println("===============");

        // 將 HTML 表單提交的日期時間字串轉換為 Timestamp 物件
        coupon.setCoupAddDate(Timestamp.valueOf(LocalDateTime.now()));

        /*************************** 2.開始新增資料 *****************************************/
        //  CouponService empSvc = new  CouponService(); //已經在line32 autowired
        couponSvc.addCoupon(coupon);
        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<Coupon> list = couponSvc.getAll();
        modelMap.addAttribute("couponListData", list);
        modelMap.addAttribute("success", "- (新增成功)");
        return "redirect:/backend/coupon/couponlist"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/coupon/listAllCoupon")
    }

    /*
     * This method will be called on listAll Coupon.html form submission, handling POST request
     */
    @PostMapping("/updateCoupon")
    public String getOne_For_Update(@RequestParam("coupNo") Integer coupNo, ModelMap model) {
        Coupon coupon = couponSvc.getOneCoupon(coupNo);
        model.addAttribute("coupon", coupon);
        return "backend/coupon/updateCoupon"; // 查詢完成後轉交updateCoupon.html
    }

    /*
     * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
     */
    @PostMapping("/update")
    public String update(@Valid Coupon coupon, BindingResult result, ModelMap modelMap,
                         @RequestParam(name = "coupExpDate") String coupExpDate,
                         @RequestParam(name = "coupRelDate") String coupRelDate) throws IOException {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        coupon.setCoupAddDate(Timestamp.valueOf(LocalDateTime.now()));
        // 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
        result = removeFieldError(coupon, result, "coupExpDate");
        result = removeFieldError(coupon, result, "coupRelDate");

        Timestamp coupExpDateParm = null;
        Timestamp coupRelDateParm = null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            Date parseDate1 = dateFormat.parse(coupExpDate); // Thu May 02 14:42:00 CST 2024
            Date parseDate2 = dateFormat.parse(coupRelDate);

            coupExpDateParm = new Timestamp(parseDate1.getTime()); // 2024-05-02 14:42:00.0
            coupRelDateParm = new Timestamp(parseDate2.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Timestamp today = Timestamp.valueOf(LocalDateTime.now());

        if (!modelMap.containsAttribute("errorMessage")) {
            modelMap.addAttribute("errorMessage", "");
        }


        if (coupExpDate.isEmpty()) {
            modelMap.addAttribute("errorMessage1", "失效日期請勿空白");
            return "updateCoupon";
        } else {
            coupon.setCoupExpDate(coupExpDateParm);
        }

        if (coupRelDate.isEmpty()) {
            modelMap.addAttribute("errorMessage2", "發放日期請勿空白");
            return "updateCoupon";
        }
//        else if (coupExpDateParm.compareTo(coupRelDateParm) <= 0) {
//            modelMap.addAttribute("errorMessage2", "優惠券發放日期不得早於失效日期");
//            return "backend/coupon/addCoupon";
//        }
        else {
            coupon.setCoupRelDate(coupRelDateParm);
        }

        if (result.hasErrors()) {
            return "updateCoupon";
        }

        couponSvc.updateCoupon(coupon);

        modelMap.addAttribute("success", "- (修改成功)");
        coupon = couponSvc.getOneCoupon(Integer.valueOf(coupon.getCoupNo()));
        modelMap.addAttribute("coupon", coupon);
        return "backend/coupon/listOneCoupon"; // 修改成功後轉交listOne Coupon.html
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("coupNo") Integer coupNo, ModelMap model) {
        couponSvc.deleteCoupon(coupNo);
        List<Coupon> list = couponSvc.getAll();
        model.addAttribute("couponListData", list);
        model.addAttribute("success", "-(刪除成功)");
        return "backend/coupon/listAllCoupon";
    }


    @GetMapping("/couponlist")
    public String showList(ModelMap modelMap) {
        return "backend/coupon/listAllCoupon";
    }

    /*
     * 第一種作法 Method used to populate the List Data in view. 如 :
     * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
     */
    @ModelAttribute("couponListData")
    protected List<Coupon> referenceListData() {
        // DeptService deptSvc = new DeptService();
        List<Coupon> list = couponSvc.getAll();
        return list;
    }



    /*
     * 【 第二種作法 】 Method used to populate the Map Data in view. 如 :
     * <form:select path="deptno" id="deptno" items="${depMapData}" />
     */
//    @ModelAttribute("deptMapData") //
//    protected Map<Integer, String> referenceMapData() {
//        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//        map.put(10, "財務部");
//        map.put(20, "研發部");
//        map.put(30, "業務部");
//        map.put(40, "生管部");
//        return map;
//    }

    // 去除BindingResult中某個欄位的FieldError紀錄
    public BindingResult removeFieldError(Coupon coupon, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(coupon, "coupon");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

    /*
     * This method will be called on selectCoupon.html form submission, handling POST request
     */
    @PostMapping("/listCoupons_ByCompositeQuery") //邏輯名稱，網頁抬頭路徑
    public String listAllCoupon(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<Coupon> list = couponSvc.getAll(map);    //selectpage 121行, service 49行
        model.addAttribute("couponListData", list); // for listAllCoupon.html 第85行用
        return "backend/coupon/listAllCoupon"; //還內部資源視圖路徑，從templates起算
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    //@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "<br>");
        }
        List<Coupon> list = couponSvc.getAll();
        model.addAttribute("couponListData", list);     // for selectCoupon.html 第97 109行用
        String message = strBuilder.toString();
        return new ModelAndView("backend/coupon/selectCoupon", "errorMessage", "請修正以下錯誤:<br>" + message);
    }


}
