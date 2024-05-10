package com.ren.productcategory.service;

import java.util.List;
import java.util.Set;

import com.Entity.ProductCategory;
import com.ren.product.dao.ProductRepository;
import com.ren.productcategory.dao.ProductCategoryRepository;
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
	public ProductCategory getOneProductCatagory(Integer pCatNo) {
		// TODO Auto-generated method stub
		return productCategoryRepository.findById(pCatNo).orElse(null);
	}

	@Override
	public List<ProductCategory> getAll() {
		return productCategoryRepository.findAll();
	}

//	@Override
//	public Set<Product> getProductsBypCatNo(Integer pCatNo) {
//		// TODO Auto-generated method stub
//		return productRepository.findById(pCatNo);
//	}

	@Override
	public ProductCategory updateProductCategory(ProductCategory productCategory) {
		return productCategoryRepository.save(productCategory);
	}

	@Override
	public void deleteProductCategory(Integer pCatNo) {
		// TODO Auto-generated method stub
		productCategoryRepository.deleteById(pCatNo);
	}

}
