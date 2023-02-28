package com.stockback.service;

import com.stockback.dto.ArticleDto;
import com.stockback.response.RestResponse;

import java.util.Optional;

public interface ArticleService {

    RestResponse getArticles(int pageNo, int pageSize, String sortBy, String sortDir);

    ArticleDto addArticle(ArticleDto articleDto);

    ArticleDto updateArticle(ArticleDto articleDto, Integer id);

    Optional<ArticleDto> findById(Integer id);

    //Optional<ArticleDto> findByCode(String code);

    ArticleDto deleteArticle(Integer id);
}
