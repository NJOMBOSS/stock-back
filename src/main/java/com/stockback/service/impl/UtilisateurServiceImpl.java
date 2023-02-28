package com.stockback.service.impl;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.UtilisateurDto;
import com.stockback.entity.Utilisateur;
import com.stockback.exception.EntityNotFoundException;
import com.stockback.exception.ErrorCodes;
import com.stockback.exception.InvalidEntityException;
import com.stockback.repository.UtilisateurRepository;
import com.stockback.response.RestResponse;
import com.stockback.service.UtilisateurService;
import com.stockback.validator.UtilisateurValidator;
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
public class UtilisateurServiceImpl implements UtilisateurService {

    public static final String ID_NOT_NULL_MESSAGE = "Utilisateur ID est null";
    public static final String NOT_FOUND = "Aucun utilisateur trouvé avec cet id ";

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public RestResponse getUtilisateurs(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Utilisateur> utilisateurs = utilisateurRepository.findAll(pageable);
        List<Utilisateur> listOfUtilisateurs = utilisateurs.getContent();
        long deleted = utilisateurRepository.numberDeleteUtilisateur();

        List<UtilisateurDto> datas = listOfUtilisateurs.stream().map(UtilisateurDto::fromEntity).collect(Collectors.toList());
        RestResponse restResponse = new RestResponse();
        restResponse.setDatas(datas);
        restResponse.setPageNumber(utilisateurs.getNumber());
        restResponse.setPageSize(utilisateurs.getSize());
        restResponse.setTotalElements(utilisateurs.getTotalElements() - deleted);
        restResponse.setTotalPages(utilisateurs.getTotalPages());
        restResponse.setFirst(utilisateurs.isFirst());
        restResponse.setLast(utilisateurs.isLast());
        return restResponse;
    }

    @Override
    public UtilisateurDto addUtilisateur(UtilisateurDto utilisateurDto) {
        try {
            List<String> errors = UtilisateurValidator.validate(utilisateurDto);
            if (!errors.isEmpty()) {
                log.error("L'utilisateur n'est pas valide {}", utilisateurDto);
                throw new InvalidEntityException("Le fournisseur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID, errors);
            }
            return UtilisateurDto.fromEntity(utilisateurRepository.save(UtilisateurDto.toEntity(utilisateurDto)));
        } catch (DataIntegrityViolationException e1) {
            throw new InvalidEntityException("Cet utilisateur existe déjà dans la base de données", ErrorCodes.UTILISATEUR_NAME_IS_EXIST);
        }
    }

    @Override
    public UtilisateurDto updateUtilisateur(UtilisateurDto utilisateurDto, Integer id) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        if (utilisateur.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.UTILISATEUR_NOT_FOUND
            );
        }
        List<String> errors = UtilisateurValidator.validate(utilisateurDto);
        if (!errors.isEmpty()) {
            log.error("L'utilisateur n'est pas valide {}", utilisateur);
            throw new InvalidEntityException("L'utilisateur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }
        try {
            utilisateur.get().setNom(utilisateurDto.getNom());
            utilisateur.get().setPrenom(utilisateurDto.getPrenom());
            utilisateur.get().setEmail(utilisateurDto.getEmail());
            utilisateur.get().setDateDeNaissance(utilisateurDto.getDateDeNaissance());
            utilisateur.get().setPhoto(utilisateurDto.getPhoto());
            utilisateur.get().setMoteDePasse(utilisateurDto.getMoteDePasse());
            utilisateur.get().setAdresse(utilisateurDto.getAdresse());
            utilisateur.get().setPhoto(utilisateurDto.getPhoto());
            utilisateur.get().setEntreprise(utilisateurDto.getEntreprise());
            return utilisateurDto.fromEntity(utilisateurRepository.save(utilisateur.get()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidEntityException("Ce fournisseur existe déjà dans la base de données", ErrorCodes.FOURNISSEUR_NAME_IS_EXIST);
        }
    }

    @Override
    public Optional<UtilisateurDto> findById(Integer id) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        if (utilisateur.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.UTILISATEUR_NOT_FOUND
            );
        }
        return Optional.ofNullable(UtilisateurDto.fromEntity(utilisateur.get()));
    }

    @Override
    public UtilisateurDto deleteUtilisateur(Integer id) {
        if (id == null) {
            log.error(ID_NOT_NULL_MESSAGE);
            throw new EntityNotFoundException(
                    AppsConstant.ID_NOT_NULL_MESSAGE,
                    ErrorCodes.UTILISATEUR_NOT_FOUND
            );
        }
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        if (utilisateur.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.UTILISATEUR_NOT_FOUND
            );
        }
        utilisateur.get().setDeleted(true);

        utilisateur.get().setNom(utilisateur.get().getNom() + "-del-" + utilisateur.get().getId());
        return UtilisateurDto.fromEntity(utilisateurRepository.save(utilisateur.get()));
    }
}
