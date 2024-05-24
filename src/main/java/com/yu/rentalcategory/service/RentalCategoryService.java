package com.yu.rentalcategory.service;

import com.yu.rentalcategory.entity.RentalCategory;

import java.util.List;
import java.util.Map;

public interface RentalCategoryService {
	public RentalCategory getRentalCatName(String rentalCatName);//單筆查詢

	public RentalCategory findByCatNo(Integer rentalCatNo);//單筆查詢

	public List<RentalCategory> findAll();  //全部查詢(RentalCategory)

	public List<RentalCategory> searchRentalCats(Map<String, String[]> paramsMap); //複合查詢

//    List<RentalCategory> findAllSortDESC(); //以rentalPrice查詢，金額由大到小
//
//	List<RentalCategory> findAllSort(); //以rentalPrice查詢，金額由小到大

	//----------------------------------------------------------------------------------------------------------------------
	//主要為後端使用：增查改

	public RentalCategory addRentalCat(RentalCategory rentalCategory); //新增

	public RentalCategory updateRentalCat(RentalCategory rentalCategory); //修改

	public RentalCategory getOneRentalCat(Integer rentalCatNo);

}
