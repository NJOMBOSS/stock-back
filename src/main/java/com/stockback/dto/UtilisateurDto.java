package com.stockback.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stockback.entity.*;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class UtilisateurDto {

    private Integer id;

    private String nom;

    private String prenom;

    private String email;

    private Instant dateDeNaissance;

    private String moteDePasse;

    private Adresse adresse;

    private String photo;

    private Entreprise entreprise;

    @JsonIgnore
    private List<Roles> roles;

    public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        return UtilisateurDto.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .email(utilisateur.getEmail())
                .dateDeNaissance(utilisateur.getDateDeNaissance())
                .moteDePasse(utilisateur.getMoteDePasse())
                .adresse(utilisateur.getAdresse())
                .photo(utilisateur.getPhoto())
                .entreprise(utilisateur.getEntreprise())
                .build();
    }

    public static Utilisateur toEntity(UtilisateurDto dto) {
        if (dto == null) {
            return null;
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(dto.getId());
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setDateDeNaissance(dto.getDateDeNaissance());
        utilisateur.setPhoto(dto.getPhoto());
        utilisateur.setMoteDePasse(dto.getMoteDePasse());
        utilisateur.setAdresse(dto.getAdresse());
        utilisateur.setPhoto(dto.getPhoto());
        utilisateur.setEntreprise(dto.getEntreprise());

        return utilisateur;
    }

}
