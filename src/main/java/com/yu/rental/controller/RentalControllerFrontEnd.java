package com.yu.rental.controller;

import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalcategory.entity.RentalCategory;
import com.yu.rentalcategory.service.RentalCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/frontend") //對應資料夾路徑
public class RentalControllerFrontEnd {

    /**前端網頁需求：
     *
     * 左上角的關鍵字查詢
     * 點入商品欄後，顯示個別商品、全部商品
     * 排序方法：最新上架、價格低~高、價格高~低
     * 點選"加入我的最愛"，即可新增查看
     *個別品項篩選 (針對類別、金額、Size)
     *
     */

    @Autowired  // 自動裝配
    private RentalServiceImpl rentalService;
    @Autowired
    private RentalCategoryServiceImpl rentalCategoryService;

    //顯示前台首頁
    @GetMapping("/frontend/index")
    public String index() {
        return "/frontend/index";
    }

    //瀏覽全部租借品頁面 - 列表 (前台)
    @GetMapping("/rental/rentalShop")
    public String rentalShop() {
        return "/frontend/rental/rentalShop";
    }

    //瀏覽全部租借品頁面 - 網格 (前台)
    @GetMapping("/rental/rentalShopGrid")
    public String rentalShopGrid() {
        return "/frontend/rental/rentalShopGrid";
    }

    //瀏覽租借須知頁面 (前台)
    @GetMapping("/rental/rentalInstructions")
    public String rentalInstructions() {
        return "/frontend/rental/rentalInstructions";
    }

    //瀏覽租借須知頁面 (前台)
    @GetMapping("/rental/rentalInstructionsSend")
    public String rentalInstructionsSend() {
        return "/frontend/rental/rentalInstructionsSend";
    }

    //瀏覽租借須知頁面 (前台)
    @GetMapping("/rental/rentalInstructionsSize")
    public String rentalInstructionsSize() {
        return "/frontend/rental/rentalInstructionsSize";
    }

    //瀏覽單一品項內的租借須知 (前台)
    @GetMapping("/rental/skipInstructions")
    public String skipInstructions() {
        return "/frontend/rental/skipInstructions";
    }


    //顯示單筆租借品頁面 (需求為查看商品頁面，故使用Get請求)
    @GetMapping("/rental/showOneRental")
    public String showOneRental(@RequestParam(value = "rentalNo") Integer rentalNo, ModelMap model) {
        //建立返回數據的對象
        Rental rental = rentalService.findByNo(rentalNo);
        model.addAttribute("rentalCategory", new RentalCategory());
        List<RentalCategory> rentalCatListData = rentalCategoryService.findAll();
        model.addAttribute("rentalCatListData",rentalCatListData); //所有租借品類別資訊
        if (rental == null) {
            model.addAttribute("errors", "errors");
            return "/frontend/rental/rentalShop";
        }
        model.addAttribute("rental", rental);
        return "/frontend/rental/showOneRental";
    }


    //排序方法：價格低~高、價格高~低
    @GetMapping("/rental/rentalShop/{sortType}")
    public String sortAllPrice(@PathVariable("sortType") String sortType,
                               @RequestParam(value = "rentalStat", required = false) Byte rentalStat,
                               Model model) {

        //判斷選擇哪種方式
        if("low_to_high".equals(sortType)){
            List<Rental> sortList = rentalService.findAllSort();
            for (Rental rental : sortList) {
                System.out.println(rental.getRentalName() +":"+rental.getRentalPrice());
            }
            model.addAttribute("rentalListData", sortList); // 顯示價格由小到大

        } else if("high_to_low".equals(sortType)){
            List<Rental> sortDESCList = rentalService.findAllSortDESC();
            model.addAttribute("rentalListData", sortDESCList); // 顯示價格由大到小

        } else if("newest".equals(sortType)){
            List<Rental> newestList = rentalService.findByRentalStatDESC(rentalStat);
            model.addAttribute("rentalListData", newestList); // 顯示最新品項

        } else {
            List<Rental> defaultSortList = rentalService.findAllSort();
            model.addAttribute("rentalListData", defaultSortList); // 預設價格由小到大

        }
        return "/frontend/rental/rentalShop"; // 查詢完成後轉交
    }

    //排序方法：價格低~高、價格高~低
    @GetMapping("/rental/rentalShopGrid/{sortType}")
    public String sortAll(@PathVariable("sortType") String sortType,
                          @RequestParam(value = "rentalStat", required = false) Byte rentalStat,
                          Model model) {

        //判斷選擇哪種方式
        if("low_to_high".equals(sortType)){
            List<Rental> sortList = rentalService.findAllSort();
            for (Rental rental : sortList) {
                System.out.println(rental.getRentalName() +":"+rental.getRentalPrice());
            }
            model.addAttribute("rentalListData", sortList); // 顯示價格由小到大

        } else if("high_to_low".equals(sortType)){
            List<Rental> sortDESCList = rentalService.findAllSortDESC();
            model.addAttribute("rentalListData", sortDESCList); // 顯示價格由大到小

        } else if("newest".equals(sortType)){
            List<Rental> newestList = rentalService.findByRentalStatDESC(rentalStat);
            model.addAttribute("rentalListData", newestList); // 顯示最新品項

        } else {
            List<Rental> defaultSortList = rentalService.findAllSort();
            model.addAttribute("rentalListData", defaultSortList); // 預設價格由小到大

        }
        return "/frontend/rental/rentalShopGrid"; // 查詢完成後轉交
    }


    //處理查詢(依租借品的顏色)
    @GetMapping("/findByColor")
    public ResponseEntity<List<Rental>> findByColor(@RequestParam String rentalColor) {
        List<Rental> rentals = rentalService.findByRentalColor(rentalColor);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }


    //處理查詢KeyWord(rentalColor, rentalSize)
    @GetMapping("/findByKeyWord")
    public String findByKeyWord(@RequestParam(value = "rentalColor", required = false) String rentalColor,
                                @RequestParam(value = "rentalSize", required = false) Integer rentalSize,
                                ModelMap model) {

        List<Rental> rentals = rentalService.findByKeyWord(rentalColor, rentalSize);
        model.addAttribute("rentalListData", rentals);

        System.out.println("這是你目前的選項：" + rentalColor);
        System.out.println("這是你目前的選項：" + rentalSize);

        return "frontend/rental/fragments/rentalList :: rental-list";
    }

    //處理查詢KeyWord(rentalColor, rentalSize)
    @GetMapping("/findByKeyWordToGrid")
    public String findByKeyWordToGrid(@RequestParam(value = "rentalColor", required = false) String rentalColor,
                                      @RequestParam(value = "rentalSize", required = false) Integer rentalSize,
                                      ModelMap model) {

        List<Rental> rentals = rentalService.findByKeyWord(rentalColor, rentalSize);
        model.addAttribute("rentalListData", rentals);

        System.out.println("這是你目前的選項：" + rentalColor);
        System.out.println("這是你目前的選項：" + rentalSize);

        return "frontend/rental/fragments/rentalGrid :: rental-grid";
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return 與rentalNo對應的rental資料庫
     */
    @ModelAttribute("rentalData") // 以rentalNo搜索，取出對應的rental資料庫
    protected Rental referenceData(@RequestParam(value = "rentalNo", required = false) Integer rentalNo) {
        if (rentalNo != null) {
            Rental list = rentalService.findByNo(rentalNo);
            return list; //取得Rental列表
        }
        return null;
    }

    /**
     * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
     * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return rental所有資料庫
     */
    @ModelAttribute("rentalListData") //取出rental資料庫
    protected List<Rental> referenceListData() {
        List<Rental> rentalList = rentalService.findAll();
        return rentalList; //取得Rental列表
    }

    /**
     * 提供所有租借品資料列表供視圖渲染使用。
     * 使用 @ModelAttribute 註解，確保在處理請求時可用於視圖中的 productList 屬性。
     * referenceMapData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
     *
     * @return RentalCategory列表。
     */
    @ModelAttribute("rentalCatListData")
    protected List<RentalCategory> referenceListDataCat() {
        List<RentalCategory> rentalCatList =rentalCategoryService.findAll();
        return rentalCatList;
    }

    /**
     *前端透過Ajax方式傳送Json資料，由此處控制器方法來接收JSON資料
     *必須使用@RequestBody註釋
     */
    @PostMapping("/json")
    public String handleJson(@RequestBody Rental rental){
        System.out.println(rental);
        return "finish";
    }
}
