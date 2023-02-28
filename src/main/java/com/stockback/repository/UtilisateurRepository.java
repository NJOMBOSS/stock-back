package com.stockback.repository;

import com.stockback.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    @Query("SELECT count(u) FROM Utilisateur u WHERE u.isDeleted = true")
    long numberDeleteUtilisateur ();
}
