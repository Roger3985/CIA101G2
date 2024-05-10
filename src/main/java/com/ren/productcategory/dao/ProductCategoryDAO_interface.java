package com.ren.productcategory.dao;

import java.util.List;
import java.util.Set;

import com.Entity.*;

public interface ProductCategoryDAO_interface {

    // 新增商品類別
    void insert(ProductCategory productCategory);
    // 透過商品類別編號查詢商品類別
    ProductCategory findByPrimaryKey(Integer pCatNo);
    // 查詢全部商品類別
    List<ProductCategory> getAll();
    //查詢某類別的衣服(一對多)(回傳 Set)
    Set<Product> getProductsBypCatNo(Integer pCatNo);
    // 更新商品類別資料
    void update(ProductCategory productCategory);
    // 根據商品類別編號刪除
    void delete(Integer pCatNo);

}
