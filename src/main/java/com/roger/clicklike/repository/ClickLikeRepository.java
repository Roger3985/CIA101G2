package com.roger.clicklike.repository;

import com.roger.clicklike.entity.ClickLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickLikeRepository extends JpaRepository<ClickLike, Integer> {
}
