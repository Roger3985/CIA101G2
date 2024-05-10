package com.ren.admauthority.dao;

import com.Entity.AdmAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmAuthorityRepository extends JpaRepository<AdmAuthority, Integer> {

}
