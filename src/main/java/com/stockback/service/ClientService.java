package com.stockback.service;

import com.stockback.dto.ClientDto;
import com.stockback.dto.EntrepriseDto;
import com.stockback.response.RestResponse;

import java.util.Optional;

public interface ClientService {

    RestResponse getClients(int pageNo, int pageSize, String sortBy, String sortDir);

    ClientDto addClient(ClientDto clientDto);

    ClientDto updateClient(ClientDto clientDto, Integer id);

    Optional<ClientDto> findById(Integer id);

   // Optional<ClientDto> findByNom(String nom);

    ClientDto deleteClient(Integer id);

}
