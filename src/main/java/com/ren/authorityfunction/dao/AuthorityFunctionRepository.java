package com.ren.authorityfunction.dao;

import com.ren.authorityfunction.entity.AuthorityFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityFunctionRepository extends JpaRepository<AuthorityFunction, Integer> {

}
