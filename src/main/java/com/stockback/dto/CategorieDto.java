package com.stockback.dto;

import com.stockback.entity.Categorie;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategorieDto {

  private Integer id;

  private String code;

  private String designation;

  private Integer idEntreprise;

/*  @JsonIgnore
  private List<ArticleDto> articles;*/

  public static CategorieDto fromEntity(Categorie category) {
    if (category == null) {
      return null;
      // TODO throw an exception
    }

    return CategorieDto.builder()
        .id(category.getId())
        .code(category.getCode())
        .designation(category.getDesignation())
        .idEntreprise(category.getIdEntreprise())
        .build();
  }

  public static Categorie toEntity(CategorieDto categoryDto) {
    if (categoryDto == null) {
      return null;
      // TODO throw an exception
    }

    Categorie category = new Categorie();
    category.setId(categoryDto.getId());
    category.setCode(categoryDto.getCode());
    category.setDesignation(categoryDto.getDesignation());
    category.setIdEntreprise(categoryDto.getIdEntreprise());

    return category;
  }
}
