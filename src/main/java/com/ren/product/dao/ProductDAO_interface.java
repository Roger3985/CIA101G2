package com.ren.product.dao;

import com.Entity.Product;
import com.Entity.ServiceRobot;

import java.util.*;

public interface ProductDAO_interface {

    int insert(Product entity);

    Product getById(Integer id);

    List<Product> getAll();

    List<Product> getByCompositeQuery(Map<String, String> map);

    List<Product> getAll(int currentPage);

    int update(Product entity);

    int delete(Integer id);

    long getTotal();

}

