package com.yu.rentalset.dao;

import com.yu.rentalset.entity.RentSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

    @Repository
    public interface RentalSetRepository extends JpaRepository<RentSet, Integer> {

    }

