package com.iting.productorderdetail.service.impl;

import com.iting.cart.entity.CartRedis;
import com.iting.productorder.dao.ProductOrderRepository;
import com.iting.productorder.entity.ProductOrder;
import com.iting.productorderdetail.dao.ProductOrderDetailRepository;
import com.iting.productorderdetail.entity.ProductOrderDetail;
import com.iting.productorderdetail.service.ProductOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("productOrderDetailService")
public class ProductOrderDetailServiceImpl implements ProductOrderDetailService {
    @Autowired
    ProductOrderDetailRepository repository;

    @Override
    public void addProductOrderDetail(ProductOrderDetail productOrderDetail) {
        repository.save(productOrderDetail);
    }
    @Override
    public void updateProductOrderDetail(ProductOrderDetail productOrderDetail) {
        // 查询数据库中的订单详情对象
        ProductOrderDetail existingOrderDetail = repository.findByproductOrdNoAndproductNo(
                productOrderDetail.getCompositeKey().getProductOrdNo(),
                productOrderDetail.getCompositeKey().getProductNo()
        );

        // 更新订单详情对象的属性
        existingOrderDetail.setProductPrice(productOrderDetail.getProductPrice());
        existingOrderDetail.setProductOrdQty(productOrderDetail.getProductOrdQty());
        existingOrderDetail.setProductRealPrice(productOrderDetail.getProductRealPrice());
        existingOrderDetail.setProductComContent(productOrderDetail.getProductComContent());
        existingOrderDetail.setProductScore(productOrderDetail.getProductScore());

        // 保存更新后的订单详情对象到数据库
        repository.save(existingOrderDetail);
    }


@Override
public ProductOrderDetail findByproductOrdNoAndproductNo(Integer productOrdNo,Integer productNo){
    return repository.findByproductOrdNoAndproductNo(productOrdNo,productNo);
}


    @Override
    public List<ProductOrderDetail> getAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductOrderDetail> findByCompositeKey(Integer productOrdNo) {
        return repository.findByCompositeKey(productOrdNo);
    }
    @Override
    public List<ProductOrderDetail> findByProductNo(Integer ProductNo) {
        return repository.findByProductNo(ProductNo);
    }

//    @Override
//    public List<ProductOrderDetail> cartaddProductOrderDetail(List<CartRedis> cartList) {
//        List<ProductOrderDetail> orderDetails = new ArrayList<>();
//
//        for (CartRedis cart : cartList) {
//            ProductOrderDetail orderDetail = new ProductOrderDetail();
//           orderDetail.setCompositeKey.(cart.getProductName());
//             orderDetail.setProductPrice(cart.getProductPrice());
//            orderDetail.setProductBuyQty(cart.getProductBuyQty());
//
//
//            // 将订单详情添加到列表中
//            orderDetails.add(orderDetail);
//        }
//
//        // 将订单详情列表保存到数据库
//        return repository.saveAll(orderDetails);
//    }

}
