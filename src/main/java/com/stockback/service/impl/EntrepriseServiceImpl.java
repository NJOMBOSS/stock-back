package com.stockback.service.impl;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.EntrepriseDto;
import com.stockback.entity.Entreprise;
import com.stockback.exception.EntityNotFoundException;
import com.stockback.exception.ErrorCodes;
import com.stockback.exception.InvalidEntityException;
import com.stockback.repository.EntrepriseRepository;
import com.stockback.response.RestResponse;
import com.stockback.service.EntrepriseService;
import com.stockback.validator.EntrepriseValidator;
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
public class EntrepriseServiceImpl implements EntrepriseService {

    public static final String ID_NOT_NULL_MESSAGE = "Categorie ID est null";
    public static final String NOT_FOUND = "Aucune categorie trouvée avec cet id ";
    private final EntrepriseRepository entrepriseRepository;

    @Autowired
    public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository) {
        this.entrepriseRepository = entrepriseRepository;
    }

    @Override
    public RestResponse getEntreprises(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Entreprise> entreprises = entrepriseRepository.findAll(pageable);
        List<Entreprise> listOfEntreprise = entreprises.getContent();
        long deleted = entrepriseRepository.numberDeleteEntreprise();

        List<EntrepriseDto> datas = listOfEntreprise.stream().map(EntrepriseDto::fromEntity).collect(Collectors.toList());
        RestResponse restResponse = new RestResponse();
        restResponse.setDatas(datas);
        restResponse.setPageNumber(entreprises.getNumber());
        restResponse.setPageSize(entreprises.getSize());
        restResponse.setTotalElements(entreprises.getTotalElements() - deleted);
        restResponse.setTotalPages(entreprises.getTotalPages());
        restResponse.setFirst(entreprises.isFirst());
        restResponse.setLast(entreprises.isLast());
        return restResponse;
    }

    @Override
    public EntrepriseDto addEntreprise(EntrepriseDto entrepriseDto) {
        try {
            List<String> errors = EntrepriseValidator.validate(entrepriseDto);
            if (!errors.isEmpty()) {
                log.error("L'entreprise n'est pas valide {}", entrepriseDto);
                throw new InvalidEntityException("L'entreprise n'est pas valide", ErrorCodes.ENTREPRISE_NOT_VALID, errors);
            }
            return EntrepriseDto.fromEntity(entrepriseRepository.save(EntrepriseDto.toEntity(entrepriseDto)));
        } catch (DataIntegrityViolationException e1) {
            throw new InvalidEntityException("Cet entreprise existe déjà dans la base de données", ErrorCodes.ENTREPRISE_NAME_IS_EXIST);
        }
    }

    @Override
    public EntrepriseDto updateEntreprise(EntrepriseDto entrepriseDto, Integer id) {
        Optional<Entreprise> entreprise =entrepriseRepository.findById(id);
        if (entreprise.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.CATEGORY_NOT_FOUND
            );
        }
        List<String> errors = EntrepriseValidator.validate(entrepriseDto);
        if (!errors.isEmpty()) {
            log.error("Categorie n'est pas valide {}", entrepriseDto);
            throw new InvalidEntityException("La categorie n'est pas valide", ErrorCodes.ENTREPRISE_NOT_VALID, errors);
        }
        try {
            entreprise.get().setNom(entrepriseDto.getNom());
            entreprise.get().setDescription(entrepriseDto.getDescription());
            entreprise.get().setAdresse(entrepriseDto.getAdresse());
            entreprise.get().setCodeFiscal(entrepriseDto.getCodeFiscal());
            entreprise.get().setPhoto(entrepriseDto.getPhoto());
            entreprise.get().setEmail(entrepriseDto.getEmail());
            entreprise.get().setNumTel(entrepriseDto.getNumTel());
            return EntrepriseDto.fromEntity(entrepriseRepository.save(entreprise.get()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidEntityException("Cet entreprise existe déjà dans la base de données", ErrorCodes.ENTREPRISE_NAME_IS_EXIST);
        }
    }

    @Override
    public Optional<EntrepriseDto> findById(Integer id) {
        Optional<Entreprise> entreprise = entrepriseRepository.findById(id);
        if (entreprise.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.ENTREPRISE_NOT_FOUND
            );
        }
        return Optional.ofNullable(EntrepriseDto.fromEntity(entreprise.get()));
    }

    @Override
    public Optional<EntrepriseDto> findByNom(String nom) {
        Entreprise entreprise = entrepriseRepository.findEntrepriseByNom(nom).orElse(null);
        return Optional.ofNullable(EntrepriseDto.fromEntity(entreprise));
    }

    @Override
    public EntrepriseDto deleteEntreprise(Integer id) {
        if (id == null) {
            log.error(ID_NOT_NULL_MESSAGE);
            throw new EntityNotFoundException(
                    AppsConstant.ID_NOT_NULL_MESSAGE,
                    ErrorCodes.ENTREPRISE_NOT_FOUND
            );
        }
        Optional<Entreprise> entreprise = entrepriseRepository.findById(id);
        if (entreprise.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.ENTREPRISE_NOT_FOUND
            );
        }
        entreprise.get().setDeleted(true);

        entreprise.get().setNom(entreprise.get().getNom() + "-del-" + entreprise.get().getId());
        return EntrepriseDto.fromEntity(entrepriseRepository.save(entreprise.get()));
    }

}
