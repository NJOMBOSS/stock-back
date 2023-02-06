package com.stockback.service.impl;


import com.stockback.dto.CategorieDto;
import com.stockback.exception.ErrorCodes;
import com.stockback.exception.InvalidEntityException;
import com.stockback.repository.CategorieRepository;
import com.stockback.service.CategorieService;
import com.stockback.validator.CategorieValidator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@Slf4j
public class CategorieServiceImpl implements CategorieService {

  private final CategorieRepository categorieRepository;

  @Autowired
  public CategorieServiceImpl(CategorieRepository categorieRepository) {
    this.categorieRepository = categorieRepository;
  }

  @Override
  public CategorieDto save(CategorieDto dto) {
    try {
      List<String> errors = CategorieValidator.validate(dto);
      if (!errors.isEmpty()) {
        log.error("La categorie n'est pas valide {}", dto);
        throw new InvalidEntityException("La categorie n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
      }
      return CategorieDto.fromEntity(categorieRepository.save(CategorieDto.toEntity(dto)));
    } catch (DataIntegrityViolationException e1) {
      throw new InvalidEntityException("Cette categorie existe déjà dans la base de données", ErrorCodes.CATEGORY_NAME_IS_EXIST);
    }
  }

  @Override
  public CategorieDto updateEntreprise(CategorieDto categorieDto, Long id) {
    return null;
  }

  @Override
  public CategorieDto findById(Integer id) {
    return null;
  }

  @Override
  public CategorieDto findByCode(String code) {
    return null;
  }

  @Override
  public List<CategorieDto> findAll() {
    return null;
  }

  @Override
  public void delete(Integer id) {

  }
}
