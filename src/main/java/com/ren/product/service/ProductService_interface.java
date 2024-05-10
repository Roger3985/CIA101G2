package com.ren.product.service;

import com.ren.product.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService_interface {

	// 新增商品(將前端request值放入VO由DAO執行SQL語法，並返回VO由Controller轉給View)
	Product addProduct(Product product);
	// 查詢單筆商品資料
	Product getOneProduct(Integer pNo);
	List<Product> getAll();
	// 查詢所有商品資料
	List<Product> getAll(int currentPage);
	// 複合查詢
	List<Product> getProductsByCompositeQuery(Map<String, String[]> map);
	// 修改商品(返回值由Controller轉給View)
	Product updateProduct(Product product);
	// 刪除商品
	void deleteProduct(Integer pNo);
}
