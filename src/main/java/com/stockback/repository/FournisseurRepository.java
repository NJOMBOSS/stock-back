package com.stockback.repository;

import com.stockback.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {

    @Query("SELECT count(f) FROM Fournisseur f WHERE f.isDeleted = true")
    long numberDeleteFournisseur();

}
