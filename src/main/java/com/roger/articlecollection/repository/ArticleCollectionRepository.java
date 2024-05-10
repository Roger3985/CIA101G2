package com.roger.articlecollection.repository;

import com.roger.articlecollection.entity.ArticleCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCollectionRepository extends JpaRepository<ArticleCollection, Integer> {
}
