package com.ren.productpicture.service;

import com.ren.productpicture.entity.ProductPicture;

import java.util.List;

public interface ProductPictureService_interface {

	/**
	 * C:
	 * 增加商品照片
	 *
	 * @param productPicture
	 * @return 返回新增後Entity
	 */
	ProductPicture addProductPicture(ProductPicture productPicture);

	/**
	 * R:
	 * 根據商品照片編號查詢單項商品照片
	 *
	 * @param productPicNo 商品照片編號
	 * @return 返回查詢Entity
	 */
	ProductPicture getOneProductPicture(Integer productPicNo);

	/**
	 * R:
	 * 根據商品編號查詢該商品所有照片
	 *
	 * @param productNo 商品編號
	 * @return 返回該商品所有照片的清單
	 */
	List<ProductPicture> getByProductNo(Integer productNo);

	/**
	 * 查詢所有商品照片資料
	 *
	 * @return 返回商品照片清單
	 */
	List<ProductPicture> getAll();

	/**
	 * 更新商品照片資料
	 *
	 * @param productPicture 修改後商品照片Entity
	 * @return 返回修改後Entity
	 */
	ProductPicture updateProductPicture(ProductPicture productPicture);

	/**
	 * 刪除商品照片
	 *
	 * @param productPicNo 依商品照片編號刪除
	 */
	void deleteProductPicture(Integer productPicNo);

	/**
	 * 刪除單項商品的所有照片
	 *
	 * @param productNo 欲刪除之商品編號
	 */
	void deletByProductNo(Integer productNo);
	
}
