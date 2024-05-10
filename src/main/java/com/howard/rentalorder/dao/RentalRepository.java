package com.howard.rentalorder.dao;

import com.changhoward.cia10108springboot.Entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface RentalRepository extends JpaRepository<Rental, Integer> {



}
