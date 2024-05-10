package com.ren.productcategory.service;

import java.util.List;
import java.util.Set;

import com.Entity.Product;
import com.Entity.ProductCategory;

public interface ProductCategoryService_interface {

	public ProductCategory addProductCategory(ProductCategory productCategory);

	public List<ProductCategory> getAll();

	public ProductCategory getOneProductCatagory(Integer pCatNo);

//	public Set<Product> getProductsBypCatNo(Integer pCatNo);

	public ProductCategory updateProductCategory(ProductCategory productCategory);
	
	public void deleteProductCategory(Integer pCatNo);
	
}
