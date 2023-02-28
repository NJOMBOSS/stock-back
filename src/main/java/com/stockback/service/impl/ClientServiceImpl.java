package com.stockback.service.impl;

import com.stockback.constant.AppsConstant;

import com.stockback.dto.ClientDto;
import com.stockback.entity.Client;
import com.stockback.exception.EntityNotFoundException;
import com.stockback.exception.ErrorCodes;
import com.stockback.exception.InvalidEntityException;
import com.stockback.repository.ClientRepository;
import com.stockback.response.RestResponse;
import com.stockback.service.ClientService;;
import com.stockback.validator.ClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    public static final String ID_NOT_NULL_MESSAGE = "Client ID est null";

    public static final String NOT_FOUND = "Aucun client trouvé avec cet id ";
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public RestResponse getClients(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Client> clients = clientRepository.findAll(pageable);
        List<Client> listOfClients = clients.getContent();
        long deleted = clientRepository.numberDeleteClient();

        List<ClientDto> datas = listOfClients.stream().map(ClientDto::fromEntity).collect(Collectors.toList());
        RestResponse restResponse = new RestResponse();
        restResponse.setDatas(datas);
        restResponse.setPageNumber(clients.getNumber());
        restResponse.setPageSize(clients.getSize());
        restResponse.setTotalElements(clients.getTotalElements() - deleted);
        restResponse.setTotalPages(clients.getTotalPages());
        restResponse.setFirst(clients.isFirst());
        restResponse.setLast(clients.isLast());
        return restResponse;
    }

    @Override
    public ClientDto addClient(ClientDto clientDto) {
        try {
            List<String> errors = ClientValidator.validate(clientDto);
            if (!errors.isEmpty()) {
                log.error("Le client n'est pas valide {}", clientDto);
                throw new InvalidEntityException("Le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
            }
            return ClientDto.fromEntity(clientRepository.save(ClientDto.toEntity(clientDto)));
        } catch (DataIntegrityViolationException e1) {
            throw new InvalidEntityException("Ce client existe déjà dans la base de données", ErrorCodes.CLIENT_NAME_IS_EXIST);
        }
    }

    @Override
    public ClientDto updateClient(ClientDto clientDto, Integer id) {
        Optional<Client> client =clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.CLIENT_NOT_FOUND
            );
        }
        List<String> errors = ClientValidator.validate(clientDto);
        if (!errors.isEmpty()) {
            log.error("Le client n'est pas valide {}", clientDto);
            throw new InvalidEntityException("Le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
        }
        try {
            client.get().setNom(clientDto.getNom());
            client.get().setAdresse(clientDto.getAdresse());
            client.get().setPhoto(clientDto.getPhoto());
            client.get().setMail(clientDto.getMail());
            client.get().setNumTel(clientDto.getNumTel());
            return clientDto.fromEntity(clientRepository.save(client.get()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidEntityException("Ce client existe déjà dans la base de données", ErrorCodes.CLIENT_NAME_IS_EXIST);
        }
    }

    @Override
    public Optional<ClientDto> findById(Integer id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.CLIENT_NOT_FOUND
            );
        }
        return Optional.ofNullable(ClientDto.fromEntity(client.get()));
    }

  /*  @Override
    public Optional<ClientDto> findByNom(String nom) {
        return Optional.empty();
    }*/

    @Override
    public ClientDto deleteClient(Integer id) {
        if (id == null) {
            log.error(ID_NOT_NULL_MESSAGE);
            throw new EntityNotFoundException(
                    AppsConstant.ID_NOT_NULL_MESSAGE,
                    ErrorCodes.CLIENT_NOT_FOUND
            );
        }
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.CLIENT_NOT_FOUND
            );
        }
        client.get().setDeleted(true);

        client.get().setNom(client.get().getNom() + "-del-" + client.get().getId());
        return ClientDto.fromEntity(clientRepository.save(client.get()));
    }

}
