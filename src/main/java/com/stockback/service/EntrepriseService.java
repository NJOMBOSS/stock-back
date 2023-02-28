package com.stockback.service;

import com.stockback.dto.EntrepriseDto;
import com.stockback.response.RestResponse;

import java.util.Optional;

public interface EntrepriseService {

    RestResponse getEntreprises(int pageNo, int pageSize, String sortBy, String sortDir);

    EntrepriseDto addEntreprise(EntrepriseDto entrepriseDto);

    EntrepriseDto updateEntreprise(EntrepriseDto entrepriseDto, Integer id);

    Optional<EntrepriseDto> findById(Integer id);

    Optional<EntrepriseDto> findByNom(String nom);

    EntrepriseDto deleteEntreprise(Integer id);
}
