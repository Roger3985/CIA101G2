package com.ren.productcategory.service;

import java.util.List;
import java.util.Set;

import com.ren.product.entity.Product;
import com.ren.productcategory.entity.ProductCategory;

public interface ProductCategoryService_interface {

	ProductCategory addProductCategory(ProductCategory productCategory);

	List<ProductCategory> getAll();

	ProductCategory getOneProductCatagory(Integer pCatNo);

//	Set<Product> getProductsBypCatNo(Integer pCatNo);

	ProductCategory updateProductCategory(ProductCategory productCategory);
	
	void deleteProductCategory(Integer pCatNo);
	
}
