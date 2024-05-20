package com.yu.rentalcategory.controller;

import com.yu.rental.entity.Rental;
import com.yu.rental.service.RentalServiceImpl;
import com.yu.rentalcategory.entity.RentalCategory;
import com.yu.rentalcategory.service.RentalCategoryServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/backend/rentalcategory") //對應資料夾路徑
public class RentalCategoryControllerBackEnd {

	@Autowired  // 自動裝配
	private RentalServiceImpl rentalService;
	@Autowired
	private RentalCategoryServiceImpl rentalCategoryService;


	//顯示首頁 (後台)
	@GetMapping("/backendIndex")
	public String backendIndex() {
		return "/backend/index";
	}


	//顯示查詢頁面 (後台)
	@GetMapping("selectRentalCategory")
	public String selectRentalCategory(ModelMap model) {
		RentalCategory rentalCategory = new RentalCategory();
		rentalCategory.setRentalCatNo(0); // 初始化RentalCatNo
		model.addAttribute("rentalCategory", rentalCategory);
		return "/backend/rentalcategory/selectRentalCategory";
	}

	//顯示全部租借品類別頁面 (後台)
	@GetMapping("listAllRentalCategory")
	public String listAllRentalCategory() {
		return "/backend/rentalcategory/listAllRentalCategory";
	}


//	//顯示後台 showOneRental.html
//	@GetMapping("listOneRentalCategory")  //required = true：請求參數不可為null(預設)
//	public String listOneRentalCat(@RequestParam(value = "rentalCatNo", required = true) Integer rentalCatNo, ModelMap model) {
//		//建立返回數據的對象
//		RentalCategory rentalCategory = rentalCategoryService.findByCatNo(rentalCatNo);
//		model.addAttribute("rentalCategory", rentalCategory);
//		return "/backend/rentalcategory/listOneRentalCategory";
//	}


	//顯示新增頁面 (後台)
	@GetMapping("addRentalCategory")
	public String addRentalCategory(ModelMap model) {
		RentalCategory rentalCategory = new RentalCategory();
		model.addAttribute("rentalCategory", rentalCategory);
		return "/backend/rentalcategory/addRentalCategory";
	}


	//處理單筆查詢(依rentalCatNo)
	@PostMapping("getOneDisplay")
	public String getOneDisplay(@RequestParam(value = "rentalCatNo",required = false) Integer rentalCatNo, ModelMap model) {

		RentalCategory rentalCategory = rentalCategoryService.findByCatNo(rentalCatNo);
		List<RentalCategory> rentalCatList = rentalCategoryService.findAll();
		model.addAttribute("rentalCatList", rentalCatList);

		List<Rental> rentalList = rentalService.findAll();
		model.addAttribute("rental", new Rental());
		model.addAttribute("rentalList", rentalList);

		if (rentalCategory == null) {
			model.addAttribute("errors", "查無資料");
			return "/backend/rentalcategory/selectRentalCategory";
		}
		model.addAttribute("rentalCategory", rentalCategory);
		return "/backend/rentalcategory/listOneRentalCategory";
	}


	//處理單筆修改(依rentalCatNo)
	@PostMapping("getOneUpdate")
	public String getOneUpdate(@RequestParam("rentalCatNo") Integer rentalCatNo, ModelMap model) {

		RentalCategory rentalCategory = rentalCategoryService.findByCatNo(rentalCatNo);
		List<RentalCategory> rentalCatList = rentalCategoryService.findAll();
		model.addAttribute("rentalCatList", rentalCatList);

		List<Rental> rentalList = rentalService.findAll();
		model.addAttribute("rental", new Rental());
		model.addAttribute("rentalList", rentalList);

		if (rentalCategory == null) {
			model.addAttribute("errors", "查無資料");
			return "/backend/rentalcategory/selectRentalCategory";
		}
		model.addAttribute("rentalCategory", rentalCategory);
		return "/backend/rentalcategory/updateRentalCategory";
	}


	// 處理修改資料
	@PostMapping("updateRentalCat")
	public String updateRentalCat(@Validated(RentalCategory.UpdateRentalCatGroup.class) RentalCategory rentalCategory,

								  BindingResult result, ModelMap model) {
		//驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
		if (rentalCategory != null) {
			if (result.hasErrors()) { //若有錯誤
				List<FieldError> fieldErrors = result.getFieldErrors();
				for (int i = 0, len = fieldErrors.size(); i < len; i++) {
					FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
					model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
					model.addAttribute("rentalCategory", rentalCategory);
				}
				return"/backend/rentalcategory/updateRentalCategory";
			}
		}else{
			return "/backend/rentalcategory/updateRentalCategory";
		}
		// 將資料添加到 ModelMap 中
		rentalCategoryService.updateRentalCat(rentalCategory);
		rentalCategory = rentalCategoryService.findByCatNo(rentalCategory.getRentalCatNo());
		model.addAttribute("rentalCategory", rentalCategory);
		return "/backend/rentalcategory/listOneRentalCategory";
	}


	// 處理新增資料
	@PostMapping("addRentalCat")
	public String addRentalCat(@Validated(RentalCategory.AddRentalCatGroup.class) RentalCategory rentalCategory,
							   BindingResult result, ModelMap model) {

		//驗證方式： 若屬性存在一個以上的驗證註解，為避免在驗證皆未通過。 搭配迴圈輸出完整的錯誤訊息
		if (rentalCategory != null) {
			if (result.hasErrors()) {
				List<FieldError> fieldErrors = result.getFieldErrors();
				for (int i = 0, len = fieldErrors.size(); i < len; i++) {
					FieldError field = fieldErrors.get(i); //依索引值放入個別錯誤
					model.addAttribute(i + "-" + field.getField(), field.getDefaultMessage()); //出錯的名稱&訊息放入。
					model.addAttribute("rentalCategory", rentalCategory);
				}
				return"/backend/rentalcategory/addRentalCategory";
			}
		}else{
			return "/backend/rentalcategory/addRentalCategory";
		}
		// 將資料添加到 ModelMap 中
		rentalCategoryService.addRentalCat(rentalCategory);
		rentalCategory = rentalCategoryService.findByCatNo(rentalCategory.getRentalCatNo());
		model.addAttribute("rentalCategory", rentalCategory);
		return "/backend/rentalcategory/listAllRentalCategory";
	}
	/**
	 *前端透過Ajax方式傳送Json資料，由此處控制器方法來接收JSON資料
	 *必須使用@RequestBody註釋
	 */
//	@PostMapping("/json")
//	public String handleJson(@RequestBody RentalCategory rentalCategory){
//		System.out.println(rentalCategory);
//		return "finish";
//	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////待處理	複合查詢
	/**
	 * Map中存储了请求参数的键值，键是参数的名称，值是参数的值数组
	 *通过HttpServletRequest获取了请求的参数Map，并将其传递给searchRentals()方法执行查询。
	 *查询结果存储在queryList中，然后将其添加到model中，以便在视图
	 *
	 */
	//處理複合查詢
	@PostMapping("search")
	public String search(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		//建立返回數據的對象
		List<RentalCategory> queryList = rentalCategoryService.searchRentalCats(map);
		model.addAttribute("queryList", queryList);
		return "/backend/rentalcategory/listAllRentalCategory"; //結果傳至listAllRental
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
	 * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
	 *
	 * @return 與rentalCatNo對應的rental資料庫
	 */
	@ModelAttribute("rentalCatData") // 以rentalNo搜索，取出對應的rental資料庫
	protected RentalCategory referenceData(@RequestParam(value = "rentalCatNo", required = false) Integer rentalCatNo) {
		if (rentalCatNo != null) {
			RentalCategory catList = rentalCategoryService.findByCatNo(rentalCatNo);
			return catList; //取得Rental列表
		}
		return null;
	}


	/**
	 * 提供所有租借品資料列表供視圖渲染使用。
	 * 使用 @ModelAttribute 註解，確保在處理請求時可用於視圖中的 productList 屬性。
	 * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
	 *
	 * @return RentalCategory列表。
	 */
	@ModelAttribute("rentalCatListData")
	protected List<RentalCategory> referenceListData() {
		List<RentalCategory> rentalCatList =rentalCategoryService.findAll();
		return rentalCatList;
	}


	/**
	 * 因 @ModelAttribute寫在方法上，故將此類別中的@GetMapping Method先加入model.addAttribute("...List",...Service.getAll());
	 * referenceListData()：回傳一個包含參考資料的列表或映射，透過View渲染到使用者介面上。
	 *
	 * @return rental所有資料庫
	 */
	@ModelAttribute("rentalListData") //取出rental資料庫
	protected List<Rental> referenceListDataRental() {
		List<Rental> rentalList = rentalService.findAll();
		return rentalList; //取得Rental列表
	}
}