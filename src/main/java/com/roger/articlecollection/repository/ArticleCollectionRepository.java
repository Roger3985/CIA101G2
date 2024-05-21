package com.roger.articlecollection.repository;

import com.roger.articlecollection.entity.ArticleCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCollectionRepository extends JpaRepository<ArticleCollection, Integer> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM ArticleCollection a WHERE a.compositeArticleCollection = :compositeArticleCollection")
    boolean existsByCompositeArticleCollection(@Param("compositeArticleCollection") ArticleCollection.CompositeArticleCollection compositeArticleCollection);
}
