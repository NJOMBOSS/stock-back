package com.stockback.service;



import com.stockback.dto.CategorieDto;

import java.util.List;

public interface CategorieService {

  CategorieDto save(CategorieDto dto);

  CategorieDto updateEntreprise(CategorieDto categorieDto, Long id);
  CategorieDto findById(Integer id);

  CategorieDto findByCode(String code);

  List<CategorieDto> findAll();

  void delete(Integer id);

}
