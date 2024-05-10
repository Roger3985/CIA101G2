//package com.iting.productmyfavorite.service.impl;
//
//import com.iting.productorderdetail.dao.ProductOrderDetailRepository;
//import com.iting.productorderdetail.entity.ProductOrderDetail;
//import com.iting.productorderdetail.service.ProductOrderDetailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service("productOrderDetailService")
//public class ProductMyFavoriteImpl implements ProductOrderDetailService {
//    @Autowired
//    ProductOrderDetailRepository repository;
//
//    @Override
//    public void addProductOrderDetail(ProductOrderDetail productOrderDetail) {
//        repository.save(productOrderDetail);
//    }
//    @Override
//    public void updateProductOrderDetail(ProductOrderDetail productOrderDetail) {
//        // 查询数据库中的订单详情对象
//        ProductOrderDetail existingOrderDetail = repository.findByproductOrdNoAndproductNo(
//                productOrderDetail.getCompositeKey().getProductOrdNo(),
//                productOrderDetail.getCompositeKey().getProductNo()
//        );
//
//        // 更新订单详情对象的属性
//        existingOrderDetail.setProductPrice(productOrderDetail.getProductPrice());
//        existingOrderDetail.setProductOrdQty(productOrderDetail.getProductOrdQty());
//        existingOrderDetail.setProductRealPrice(productOrderDetail.getProductRealPrice());
//        existingOrderDetail.setProductComContent(productOrderDetail.getProductComContent());
//        existingOrderDetail.setProductScore(productOrderDetail.getProductScore());
//
//        // 保存更新后的订单详情对象到数据库
//        repository.save(existingOrderDetail);
//    }
//
//
//@Override
//public ProductOrderDetail findByproductOrdNoAndproductNo(Integer productOrdNo,Integer productNo){
//    return repository.findByproductOrdNoAndproductNo(productOrdNo,productNo);
//}
//
//
//    @Override
//    public List<ProductOrderDetail> getAll() {
//        return repository.findAll();
//    }
//
//    @Override
//    public List<ProductOrderDetail> findByCompositeKey(Integer productNo) {
//        return repository.findByCompositeKey(productNo);
//    }
//
//
//
//
//}
