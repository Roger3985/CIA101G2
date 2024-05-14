package com.ren.product.service.impl;

import com.ren.product.entity.Product;
import com.ren.product.dao.ProductRepository;
import com.ren.product.service.ProductService_interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService_interface {

    private final int SUCCESS = 1;
    private final int FAILURE = -1;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getOneProduct(Integer pNo) {
        return productRepository.findById(pNo).orElse(null);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAll(int currentPage) {
        return null;
    }

    @Override
    public List<Product> getProductsByCompositeQuery(Map<String, String[]> map) {
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        Map<String, String> query = new HashMap<>();
//        Set<Map.Entry<String, String[]>> entry = map.entrySet();
//
//        for (Map.Entry<String, String[]> row : entry) {
//            String key = row.getKey();
//            // 因為請求參數裡包含了action，做個去除動作
//            if ("action".equals(key)) {
//                continue;
//            }
//            // 若是value為空即代表沒有查詢條件，做個去除動作
//            String value = row.getValue()[0];
//            if (value.isEmpty() || value == null) {
//                continue;
//            }
//            query.put(key, value);
//        }
//
//        try {
//            session.beginTransaction();
//            List<Product> list = dao.getByCompositeQuery(query);
//            session.getTransaction().commit();
//            return list;
//        } catch (Exception e) {
//            session.getTransaction().rollback();
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product onShelf(Product product) {
        return null;
    }

    @Override
    public Product offShelf(Product product) {
        return null;
    }

    @Override
    public void deleteProduct(Integer pNo) {
        productRepository.deleteById(pNo);
    }


}