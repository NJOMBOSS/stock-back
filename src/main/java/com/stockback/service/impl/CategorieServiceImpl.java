package com.stockback.service.impl;


import com.stockback.constant.AppsConstant;
import com.stockback.dto.CategorieDto;
import com.stockback.entity.Categorie;
import com.stockback.exception.EntityNotFoundException;
import com.stockback.exception.ErrorCodes;
import com.stockback.exception.InvalidEntityException;
import com.stockback.repository.CategorieRepository;
import com.stockback.response.RestResponse;
import com.stockback.service.CategorieService;
import com.stockback.validator.CategorieValidator;
import lombok.extern.slf4j.Slf4j;

import net.bytebuddy.matcher.InstanceTypeMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@Slf4j
public class CategorieServiceImpl implements CategorieService {

  public static final String ID_NOT_NULL_MESSAGE = "Categorie ID est null";
  public static final String NOT_FOUND = "Aucune categorie trouvée avec cet id ";
  private final CategorieRepository categorieRepository;

  @Autowired
  public CategorieServiceImpl(CategorieRepository categorieRepository) {
    this.categorieRepository = categorieRepository;
  }


  @Override
  public RestResponse getCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    Page<Categorie> categories = categorieRepository.findAll(pageable);
    List<Categorie> listOfCategories = categories.getContent();
    long deleted = categorieRepository.numberDeleteCategorie();

    List<CategorieDto> datas = listOfCategories.stream().map(CategorieDto::fromEntity).collect(Collectors.toList());
    RestResponse restResponse = new RestResponse();
    restResponse.setDatas(datas);
    restResponse.setPageNumber(categories.getNumber());
    restResponse.setPageSize(categories.getSize());
    restResponse.setTotalElements(categories.getTotalElements() - deleted);
    restResponse.setTotalPages(categories.getTotalPages());
    restResponse.setFirst(categories.isFirst());
    restResponse.setLast(categories.isLast());
    return restResponse;
  }

  @Override
  public CategorieDto addCategorie(CategorieDto categorieDto) {
    try {
      List<String> errors = CategorieValidator.validate(categorieDto);
      if (!errors.isEmpty()) {
        log.error("La categorie n'est pas valide {}", categorieDto);
        throw new InvalidEntityException("La categorie n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
      }

      log.info(categorieDto.toString());
      Categorie categorie = CategorieDto.toEntity(categorieDto);
     // categorie.setCreationDate(new Date().toInstant());
      log.info(String.valueOf(categorie));
      return CategorieDto.fromEntity(categorieRepository.save(categorie));
    } catch (DataIntegrityViolationException e1) {
      log.error(e1.getMessage());
      throw new InvalidEntityException("Cette categorie existe déjà dans la base de données", ErrorCodes.CATEGORY_NAME_IS_EXIST);
    }
  }

  @Override
  public CategorieDto updateCategorie(CategorieDto categorieDto, Integer id) {
    Optional<Categorie> categorie = categorieRepository.findById(id);
    if (categorie.isEmpty()) {
      throw new EntityNotFoundException(
              NOT_FOUND + id,
              ErrorCodes.CATEGORY_NOT_FOUND
      );
    }
    List<String> errors = CategorieValidator.validate(categorieDto);
    if (!errors.isEmpty()) {
      log.error("Categorie n'est pas valide {}", categorieDto);
      throw new InvalidEntityException("La categorie n'est pas valide", ErrorCodes.ENTREPRISE_NOT_VALID, errors);
    }
    try {
      categorie.get().setCode(categorieDto.getCode());
      categorie.get().setDesignation(categorieDto.getDesignation());
      return CategorieDto.fromEntity(categorieRepository.save(categorie.get()));
    } catch (DataIntegrityViolationException e) {
      throw new InvalidEntityException("Cette categorie existe déjà dans la base de données", ErrorCodes.CATEGORY_NAME_IS_EXIST);
    }
  }

  @Override
  public Optional<CategorieDto> findById(Integer id) {
    Optional<Categorie> categorie = categorieRepository.findById(id);
    if (categorie.isEmpty()) {
      throw new EntityNotFoundException(
              NOT_FOUND + id,
              ErrorCodes.CATEGORY_NOT_FOUND
      );
    }
    return Optional.ofNullable(CategorieDto.fromEntity(categorie.get()));
  }

  @Override
  public Optional<CategorieDto> findByCode(String code) {
    Categorie categorie = categorieRepository.findCategoryByCode(code).orElse(null);
    return Optional.ofNullable(CategorieDto.fromEntity(categorie));
  }

  @Override
  public CategorieDto deleteCategorie(Integer id) {
    if (id == null) {
      log.error(ID_NOT_NULL_MESSAGE);
      throw new EntityNotFoundException(
              AppsConstant.ID_NOT_NULL_MESSAGE,
              ErrorCodes.CATEGORY_NOT_FOUND
      );
    }
    Optional<Categorie> categorie = categorieRepository.findById(id);
    if (categorie.isEmpty()) {
      throw new EntityNotFoundException(
              NOT_FOUND + id,
              ErrorCodes.CATEGORY_NOT_FOUND
      );
    }
    categorie.get().setDeleted(true);
    categorie.get().setDesignation(categorie.get().getDesignation() + "-del-" + categorie.get().getId());
    return CategorieDto.fromEntity(categorieRepository.save(categorie.get()));
  }
}
