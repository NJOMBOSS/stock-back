package com.stockback.repository;


import com.stockback.entity.Categorie;
import com.stockback.entity.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {

    @Query("SELECT count(e) FROM Entreprise e WHERE e.isDeleted = true")
    long numberDeleteEntreprise();
    Optional<Entreprise> findEntrepriseByNom(String nom);
}
