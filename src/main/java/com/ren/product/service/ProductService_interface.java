package com.ren.product.service;

import com.ren.product.entity.Product;
import org.springframework.data.domain.Page;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService_interface {

	/**
	 * 新增單項商品
	 *
	 * @param product 前端輸入商品資訊
	 * @return 返回新增後Entity
	 */
	Product addProduct(Product product);

	/**
	 * 批量新增商品
	 *
	 * @param list 商品清單
	 * @return 返回新增後清單
	 */
	List<Product> addProductList(List<Product> list);

	/**
	 * 查詢單筆資料
	 *
	 * @param productNo 商品編號
	 * @return 返回查詢Entity
	 */
	Product getOneProduct(Integer productNo);

	/**
	 * 根據關鍵字搜尋
	 *
	 * @param keyword 關鍵字
	 * @return 返回相關聯商品清單
	 */
	List<Product> searchByKeyword(String keyword);

	/**
	 * 根據商品類別編號搜尋相關連商品清單
	 *
	 * @param productCatNo 商品類別編號
	 * @return 返回相同商品類別編號的商品清單
	 */
	List<Product> getByProductCatNo(Integer productCatNo);

	/**
	 * 根據商品類別名稱搜尋相關連商品清單
	 *
	 * @param productCatName 商品類別名稱
	 * @return 返回相同商品類別名稱的商品清單
	 */
	List<Product> getByProductCatName(String productCatName);

	/**
	 * 搜尋所有上架商品(or 下架商品)
	 *
	 * @param productStat 商品上下架狀態
	 * @return 返回商品清單
	 */
	List<Product> getByProductStat(Byte productStat);

	/**
	 * 獲得最新上架的商品清單
	 *
	 * @return 最新上架的商品清單
	 */
	List<Product> getTopOnShelf();

	/**
	 * 獲得最新下架的的商品清單
	 *
	 * @return 最新下架的的商品清單
	 */
	List<Product> getTopOffShelf();

	/**
	 * 萬用複合查詢
	 *
	 * @return 返回相關聯商品清單
	 */
	List<Product> getComposite(Product product);

	/**
	 * 獲得全部商品資料
	 *
	 * @return 返回全部商品資料
	 */
	List<Product> getAll();

	/**
	 * 全文搜索
	 *
	 * @param keyword 關鍵字
	 * @param page 指定為第幾頁
	 * @param size 指定分頁要呈現多少筆資料
	 * @return 返回分頁搜尋結果
	 */
	Page<Product> searchProducts(String keyword, Integer page, Integer size);

	List<Product> getVisitProducts(Integer productCatNo, String productName);

	List<Product> getDifSize(Integer productCatNo, String productName, String productColor);

	List<Product> getDifColor(Integer productCatNo, String productName, Integer productSize);

	List<Product> getMyProduct(Integer productCatNo, String productName, String productColor, Integer productSize);

	List<Product> getProductsByColor(String productColor);

	List<Product> getProductsBySize(Integer productSize);

	List<Product> getProductsByPrice(BigDecimal minProductPrice, BigDecimal maxProductPrice);

	/**
	 * 更新單筆商品資料
	 *
	 * @param product 欲更新之Entity
	 * @return 更新後Entity
	 */
	Product updateProduct(Product product);

	/**
	 * 批量更新商品資料
	 *
	 * @param list 待更新商品清單
	 * @return 更新後清單
	 */
	List<Product> updateProducts(List<Product> list);

	/**
	 * 單件商品上架
	 *
	 * @param productNo 欲上架商品編號
	 */
	void onShelf(Integer productNo);

	/**
	 * 單件商品下架
	 *
	 * @param productNo 欲下架商品編號
	 */
	void offShelf(Integer productNo);

	/**
	 * 批量商品下架
	 *
	 * @param list 欲上架商品清單
	 */
	void listOnShelf(List<Integer> list);

	/**
	 * 批量商品下架
	 *
	 * @param list 欲下架商品清單
	 */
	void listOffShelf(List<Integer> list);

	/**
	 * 根據商品類別編號上架商品
	 *
	 * @param productCatNo 欲上架商品類別編號
	 */
	void OnShelfByProductCatNo(Integer productCatNo);

	/**
	 * 根據商品類別編號下架商品
	 *
	 * @param productCatNo 欲下架商品類別編號
	 */
	void OffShelfByProductCatNo(Integer productCatNo);

	/**
	 * 根據商品類別名稱上架商品
	 *
	 * @param productCatName 欲上架商品類別名稱
	 */
	void OnShelfByProductCatName(String productCatName);

	/**
	 * 根據商品類別名稱下架商品
	 *
	 * @param productCatName 欲下架商品類別名稱
	 */
	void OffShelfByProductCatName(String productCatName);

	/**
	 * 刪除單件商品
	 *
	 * @param productNo 欲刪除商品編號
	 */
	void deleteProduct(Integer productNo);

	/**
	 * 依商品類別編號刪除同一種類的商品
	 *
	 * @param productCatNo 欲刪除商品類別編號
	 */
	void deleteByProductCatNo(Integer productCatNo);

	/**
	 * 依商品類別名稱刪除同一種類的商品
	 *
	 * @param productCatName 欲刪除商品類別名稱
	 */
	void deleteByProductCatName(String productCatName);
}
