package com.stockback.service;

import com.stockback.dto.MvtStockDto;
import com.stockback.dto.UtilisateurDto;
import com.stockback.response.RestResponse;

import java.util.Optional;

public interface MvtStockService {

    RestResponse getMvtStocks(int pageNo, int pageSize, String sortBy, String sortDir);

    MvtStockDto addMvtStock(MvtStockDto mvtStockDto);

    MvtStockDto updateMvtStock(MvtStockDto mvtStockDto, Integer id);

    Optional<MvtStockDto> findById(Integer id);

    // Optional<MvtStockDto> findByNom(String nom);

    MvtStockDto deleteMvtStock(Integer id);

}
