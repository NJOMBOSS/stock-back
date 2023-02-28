package com.stockback.service;

import com.stockback.dto.CategorieDto;
import com.stockback.dto.FournisseurDto;
import com.stockback.response.RestResponse;

import java.util.Optional;

public interface FournisseurService {

    RestResponse getFournisseurs(int pageNo, int pageSize, String sortBy, String sortDir);

    FournisseurDto addFournisseur(FournisseurDto fournisseurDto);

    FournisseurDto updateFournisseur(FournisseurDto fournisseurDto, Integer id);

    Optional<FournisseurDto> findById(Integer id);

    FournisseurDto deleteFournisseur(Integer id);
}
