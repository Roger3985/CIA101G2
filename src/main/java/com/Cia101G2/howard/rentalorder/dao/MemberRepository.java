package com.Cia101G2.howard.rentalorder.dao;

import com.Cia101G2.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MemberRepository extends JpaRepository<Member, Integer> {



}
