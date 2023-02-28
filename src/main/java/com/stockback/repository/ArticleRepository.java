package com.stockback.repository;


import com.stockback.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

/*  Optional<Article> findArticleByCodeArticle(String codeArticle);

  List<Article> findAllByCategoryId(Integer idCategory);*/

    @Query("SELECT count(a) FROM Article a WHERE a.isDeleted = true")
    long numberDeleteArticle();
}
