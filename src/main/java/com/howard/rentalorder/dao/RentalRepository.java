package com.Cia101G2.howard.rentalorder.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface RentalRepository extends JpaRepository<Rental, Integer> {



}
