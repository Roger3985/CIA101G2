package com.ren.product.service;

import com.ren.product.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService_interface {

	Product addProduct(Product product);
	Product getOneProduct(Integer pNo);
	List<Product> getAll();
	List<Product> getAll(int currentPage);
	List<Product> getProductsByCompositeQuery(Map<String, String[]> map);
	Product updateProduct(Product product);

	/**
	 * 商品上架
	 *
	 * @param product
	 * @return
	 */
	Product onShelf(Product product);

	/**
	 * 商品下架
	 *
	 * @param product
	 * @return
	 */
	Product offShelf(Product product);
	void deleteProduct(Integer pNo);
}
