package com.howard.rentalorder.dao;

import com.changhoward.cia10108springboot.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MemberRepository extends JpaRepository<Member, Integer> {



}
