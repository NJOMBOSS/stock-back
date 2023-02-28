package com.stockback.repository;

import com.stockback.entity.MvtStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MvtStockRepository extends JpaRepository<MvtStock, Integer>{

    @Query("SELECT count(m) FROM MvtStock m WHERE m.isDeleted = true")
    long numberDeleteMvtStock();

}
