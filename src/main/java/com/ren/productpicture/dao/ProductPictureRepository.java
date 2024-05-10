package com.ren.productpicture.dao;

import com.Entity.ProductPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPictureRepository extends JpaRepository<ProductPicture, Integer> {

}
