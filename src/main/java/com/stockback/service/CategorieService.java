package com.stockback.service;



import com.stockback.dto.CategorieDto;
import com.stockback.response.RestResponse;

import java.util.Optional;

public interface CategorieService {


  RestResponse getCategories(int pageNo, int pageSize, String sortBy, String sortDir);
  CategorieDto addCategorie( CategorieDto categorieDto);
  CategorieDto updateCategorie(CategorieDto categorieDto, Integer id);
  Optional<CategorieDto>findById(Integer id);

  Optional<CategorieDto> findByCode(String code);

  CategorieDto deleteCategorie(Integer id);




}
