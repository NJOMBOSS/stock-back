package com.stockback.service;

import com.stockback.dto.ClientDto;
import com.stockback.dto.UtilisateurDto;
import com.stockback.response.RestResponse;

import java.util.Optional;

public interface UtilisateurService {


    RestResponse getUtilisateurs(int pageNo, int pageSize, String sortBy, String sortDir);

    UtilisateurDto addUtilisateur(UtilisateurDto utilisateurDto);

    UtilisateurDto updateUtilisateur(UtilisateurDto utilisateurDto, Integer id);

    Optional<UtilisateurDto> findById(Integer id);

    // Optional<ClientDto> findByNom(String nom);

    UtilisateurDto deleteUtilisateur(Integer id);
}
