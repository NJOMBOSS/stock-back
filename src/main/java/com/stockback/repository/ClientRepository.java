package com.stockback.repository;

import com.stockback.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("SELECT count(c) FROM Client c WHERE c.isDeleted = true")
    long numberDeleteClient();

}
