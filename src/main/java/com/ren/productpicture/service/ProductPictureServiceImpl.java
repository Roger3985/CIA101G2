package com.ren.productpicture.service;

import com.Entity.ProductPicture;
import com.ren.product.dao.ProductRepository;
import com.ren.productpicture.dao.ProductPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductPictureServiceImpl implements ProductPictureService_interface {

	@Autowired
	private ProductPictureRepository productPictureRepository;

	@Override
	public ProductPicture addProductPicture(ProductPicture productPicture) {
		return productPictureRepository.save(productPicture);
	}

	@Override
	public ProductPicture getOneProductPicture(Integer productPicNo) {
		// TODO Auto-generated method stub
		return productPictureRepository.findById(productPicNo).orElse(null);
	}

	@Override
	public List<ProductPicture> getAll() {
		return productPictureRepository.findAll();
	}

//	@Override
//	public Set<Product> getProductsByproductPicNo(Integer productPicNo) {
//		// TODO Auto-generated method stub
//		return productRepository.findById(productPicNo);
//	}

	@Override
	public ProductPicture updateProductPicture(ProductPicture productPicture) {
		return productPictureRepository.save(productPicture);
	}

	@Override
	public void deleteProductPicture(Integer productPicNo) {
		// TODO Auto-generated method stub
		productPictureRepository.deleteById(productPicNo);
	}

}
