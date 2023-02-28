package com.stockback.controller;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.UtilisateurDto;
import com.stockback.response.ResponseBody;
import com.stockback.response.RestResponse;
import com.stockback.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public RestResponse getUtilisateurs(
            @RequestParam(value = "pageNumber", defaultValue = AppsConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppsConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppsConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppsConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return utilisateurService.getUtilisateurs(pageNumber, pageSize, sortBy, sortDir);
    }

    @PostMapping
    public ResponseEntity<com.stockback.response.ResponseBody> createUtilisateur(@RequestBody UtilisateurDto utilisateurDto) {
        com.stockback.response.ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Utilisateur enregistré avec succès");
        utilisateurService.addUtilisateur(utilisateurDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UtilisateurDto>> getUtilisateur(@PathVariable Integer id) {
        return new ResponseEntity<>(utilisateurService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody> updateUtilisateur(@RequestBody UtilisateurDto utilisateurDto, @PathVariable Integer id) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Utilisateur modifié avec succès");
        utilisateurService.updateUtilisateur(utilisateurDto, id);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> deleteUtilisateur(@PathVariable Integer id) {
        ResponseBody response = new ResponseBody();
        try {
            utilisateurService.deleteUtilisateur(id);
            response.setStatus("success");
            response.setMessage("Utilisateur supprimé  avec succès");
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Erreur lors de la suppression de l'utilisateur");
            return ResponseEntity.status(500).body(response);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
