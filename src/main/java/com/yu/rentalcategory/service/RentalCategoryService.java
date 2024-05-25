package com.yu.rentalcategory.service;

import com.yu.rentalcategory.entity.RentalCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RentalCategoryService {

	public RentalCategory getRentalCatName(String rentalCatName);//單筆查詢

	public RentalCategory findByCatNo(Integer rentalCatNo);//單筆查詢

	public Optional<RentalCategory> findRentalCategory_RentalCatNo(Integer rentalCatNo);

	public List<RentalCategory> getRentalDesPrice(BigDecimal rentalDesPrice);//單筆查詢

	public List<RentalCategory> findAll();  //全部查詢(RentalCategory)

	public List<RentalCategory> searchRentalCats(Map<String, String[]> paramsMap); //複合查詢

	//----------------------------------------------------------------------------------------------------------------------
	//主要為後端使用：增查改

	public RentalCategory addRentalCat(RentalCategory rentalCategory); //新增

	public RentalCategory updateRentalCat(RentalCategory rentalCategory); //修改

	public RentalCategory getOneRentalCat(Integer rentalCatNo);

}
