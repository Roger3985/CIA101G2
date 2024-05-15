package com.ren.productcategory.dao;

import com.ren.product.entity.Product;
import com.ren.productcategory.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    @Transactional
    List<ProductCategory> findProductCategoriesByProductCatName(String productCatName);
}
