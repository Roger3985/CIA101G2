package com.yu.rentalpic.dao;

import com.yu.rentalpic.entity.RentalPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RentalPicRepository extends JpaRepository<RentalPic, Integer> {

}