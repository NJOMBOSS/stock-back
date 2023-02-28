package com.stockback.repository;

import com.stockback.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
  Optional<Categorie> findCategoryByCode(String code);

  @Query("SELECT count(c) FROM Categorie c WHERE c.isDeleted = true")
  long numberDeleteCategorie();

}
