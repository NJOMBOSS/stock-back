package com.stockback.repository;

import com.stockback.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {

  Optional<Categorie> findCategoryByCode(String code);

}
