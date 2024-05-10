package com.ren.productpicture.service;

import com.ren.productpicture.entity.ProductPicture;

import java.util.List;

public interface ProductPictureService_interface {

	ProductPicture addProductPicture(ProductPicture productPicture);

	List<ProductPicture> getAll();

	ProductPicture getOneProductPicture(Integer productPicNo);

//	public Set<Product> getProductsByproductPicNo(Integer productPicNo);

	ProductPicture updateProductPicture(ProductPicture productPicture);
	
	void deleteProductPicture(Integer productPicNo);
	
}
