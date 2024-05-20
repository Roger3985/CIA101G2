package com.roger.clicklike.repository;

import com.roger.clicklike.entity.ClickLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickLikeRepository extends JpaRepository<ClickLike, Integer> {

    boolean findClickLikeByCompositeClickLikeLike(ClickLike.CompositeClickLike compositeClickLike);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClickLike c WHERE c.compositeClickLike = :compositeClickLike")
    boolean existsByCompositeClickLike(@Param("compositeClickLike") ClickLike.CompositeClickLike compositeClickLike);

}
