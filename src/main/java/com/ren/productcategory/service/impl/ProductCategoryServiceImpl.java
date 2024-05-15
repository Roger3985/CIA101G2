package com.ren.productcategory.service.impl;

import java.util.List;
import java.util.Set;

import com.ren.productcategory.entity.ProductCategory;
import com.ren.product.dao.ProductRepository;
import com.ren.productcategory.dao.ProductCategoryRepository;
import com.ren.productcategory.service.ProductCategoryService_interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Access;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService_interface {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public ProductCategory addProductCategory(ProductCategory productCategory) {
		return productCategoryRepository.save(productCategory);
	}

	@Override
	public List<ProductCategory> getByProductCatName(String productCatName) {
		return productCategoryRepository.findProductCategoriesByProductCatName(productCatName);
	}

	@Override
	public ProductCategory getOneProductCategory(Integer productCatNo) {
		return productCategoryRepository.findById(productCatNo).orElse(null);
	}

	@Override
	public List<ProductCategory> getAll() {
		return productCategoryRepository.findAll();
	}

	@Override
	public ProductCategory updateProductCategory(ProductCategory productCategory) {
		return productCategoryRepository.save(productCategory);
	}

	@Override
	public void deleteProductCategory(Integer pCatNo) {
		productCategoryRepository.deleteById(pCatNo);
	}

}
