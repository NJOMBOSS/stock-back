package com.stockback.service.impl;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.CategorieDto;
import com.stockback.dto.ClientDto;
import com.stockback.dto.FournisseurDto;
import com.stockback.entity.Client;
import com.stockback.entity.Fournisseur;
import com.stockback.exception.EntityNotFoundException;
import com.stockback.exception.ErrorCodes;
import com.stockback.exception.InvalidEntityException;
import com.stockback.repository.FournisseurRepository;
import com.stockback.response.RestResponse;
import com.stockback.service.FournisseurService;
import com.stockback.validator.ClientValidator;
import com.stockback.validator.FournisseurValidator;
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
public class FournisseurServiceImpl implements FournisseurService {

    public static final String ID_NOT_NULL_MESSAGE = "Fournisseur ID est null";
    public static final String NOT_FOUND = "Aucun fournisseur trouvé avec cet id ";
    private final FournisseurRepository fournisseurRepository;

    @Autowired
    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @Override
    public RestResponse getFournisseurs(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Fournisseur> fournisseurs = fournisseurRepository.findAll(pageable);
        List<Fournisseur> listOfFournisseurs = fournisseurs.getContent();
        long deleted = fournisseurRepository.numberDeleteFournisseur();

        List<FournisseurDto> datas = listOfFournisseurs.stream().map(FournisseurDto::fromEntity).collect(Collectors.toList());
        RestResponse restResponse = new RestResponse();
        restResponse.setDatas(datas);
        restResponse.setPageNumber(fournisseurs.getNumber());
        restResponse.setPageSize(fournisseurs.getSize());
        restResponse.setTotalElements(fournisseurs.getTotalElements() - deleted);
        restResponse.setTotalPages(fournisseurs.getTotalPages());
        restResponse.setFirst(fournisseurs.isFirst());
        restResponse.setLast(fournisseurs.isLast());
        return restResponse;
    }

    @Override
    public FournisseurDto addFournisseur(FournisseurDto fournisseurDto) {
        try {
            List<String> errors = FournisseurValidator.validate(fournisseurDto);
            if (!errors.isEmpty()) {
                log.error("Le fournisseur n'est pas valide {}", fournisseurDto);
                throw new InvalidEntityException("Le fournisseur n'est pas valide", ErrorCodes.FOURNISSEUR_NOT_VALID, errors);
            }
            return FournisseurDto.fromEntity(fournisseurRepository.save(FournisseurDto.toEntity(fournisseurDto)));
        } catch (DataIntegrityViolationException e1) {
            throw new InvalidEntityException("Ce client existe déjà dans la base de données", ErrorCodes.FOURNISSEUR_NAME_IS_EXIST);
        }
    }

    @Override
    public FournisseurDto updateFournisseur(FournisseurDto fournisseurDto, Integer id) {
        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(id);
        if (fournisseur.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.FOURNISSEUR_NOT_FOUND
            );
        }
        List<String> errors = FournisseurValidator.validate(fournisseurDto);
        if (!errors.isEmpty()) {
            log.error("Le fournisseur n'est pas valide {}", fournisseurDto);
            throw new InvalidEntityException("Le fournisseur n'est pas valide", ErrorCodes.FOURNISSEUR_NOT_VALID, errors);
        }
        try {
            fournisseur.get().setNom(fournisseurDto.getNom());
            fournisseur.get().setAdresse(fournisseurDto.getAdresse());
            fournisseur.get().setPhoto(fournisseurDto.getPhoto());
            fournisseur.get().setMail(fournisseurDto.getMail());
            fournisseur.get().setNumTel(fournisseurDto.getNumTel());
            return fournisseurDto.fromEntity(fournisseurRepository.save(fournisseur.get()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidEntityException("Ce fournisseur existe déjà dans la base de données", ErrorCodes.FOURNISSEUR_NAME_IS_EXIST);
        }
    }

    @Override
    public Optional<FournisseurDto> findById(Integer id) {
        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(id);
        if (fournisseur.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.FOURNISSEUR_NOT_FOUND
            );
        }
        return Optional.ofNullable(FournisseurDto.fromEntity(fournisseur.get()));
    }


    @Override
    public FournisseurDto deleteFournisseur(Integer id) {
        if (id == null) {
            log.error(ID_NOT_NULL_MESSAGE);
            throw new EntityNotFoundException(
                    AppsConstant.ID_NOT_NULL_MESSAGE,
                    ErrorCodes.FOURNISSEUR_NOT_FOUND
            );
        }
        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(id);
        if (fournisseur.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.FOURNISSEUR_NOT_FOUND
            );
        }
        fournisseur.get().setDeleted(true);

        fournisseur.get().setNom(fournisseur.get().getNom() + "-del-" + fournisseur.get().getId());
        return FournisseurDto.fromEntity(fournisseurRepository.save(fournisseur.get()));
    }
}
