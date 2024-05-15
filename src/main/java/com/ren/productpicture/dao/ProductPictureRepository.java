package com.ren.productpicture.dao;

import com.ren.productpicture.entity.ProductPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductPictureRepository extends JpaRepository<ProductPicture, Integer> {

    /**
     * 透過商品編號獲得該商品的所有照片
     *
     * @param productNo 欲查詢之商品編號
     * @return 返回商品照片清單
     */
    @Transactional
    List<ProductPicture> findProductPicturesByProduct_ProductNo(Integer productNo);

    /**
     * 根據商品編號刪除該商品所有商品照片
     *
     * @param productNo 欲刪除之商品編號
     */
    @Transactional
    @Modifying
    void deleteProductPicturesByProduct_ProductNo(Integer productNo);
}
