package com.ren.productpicture.service.impl;

import com.ren.productpicture.entity.ProductPicture;
import com.ren.product.dao.ProductRepository;
import com.ren.productpicture.dao.ProductPictureRepository;
import com.ren.productpicture.service.ProductPictureService_interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductPictureServiceImpl implements ProductPictureService_interface {

	@Autowired
	private ProductPictureRepository productPictureRepository;

	/**
	 * 
	 * @param productPicture
	 * @return
	 */
	@Override
	public ProductPicture addProductPicture(ProductPicture productPicture) {
		return productPictureRepository.save(productPicture);
	}

	/**
	 *
	 * @param productPicNo
	 * @return
	 */
	@Override
	public ProductPicture getOneProductPicture(Integer productPicNo) {
		// TODO Auto-generated method stub
		return productPictureRepository.findById(productPicNo).orElse(null);
	}

	/**
	 *
	 * @param productNo
	 * @return
	 */
	@Override
	public List<ProductPicture> getByProductNo(Integer productNo) {
		return productPictureRepository.findProductPicturesByProduct_ProductNo(productNo);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<ProductPicture> getAll() {
		return productPictureRepository.findAll();
	}

	/**
	 *
	 * @param productPicture 修改後商品照片Entity
	 * @return
	 */
	@Override
	public ProductPicture updateProductPicture(ProductPicture productPicture) {
		return productPictureRepository.save(productPicture);
	}

	/**
	 *
	 * @param productPicNo 依商品照片編號刪除
	 */
	@Override
	public void deleteProductPicture(Integer productPicNo) {
		productPictureRepository.deleteById(productPicNo);
	}

	/**
	 * 刪除單項商品的所有照片
	 *
	 * @param productNo 欲刪除之商品編號
	 */
	@Override
	public void deletByProductNo(Integer productNo) {
		productPictureRepository.deleteProductPicturesByProduct_ProductNo(productNo);
	}
}
