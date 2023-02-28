package com.stockback.controller;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.EntrepriseDto;
import com.stockback.response.ResponseBody;
import com.stockback.response.RestResponse;
import com.stockback.service.EntrepriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/entreprises")
public class EntrepriseController {

    private final EntrepriseService entrepriseService;

    @Autowired
    public EntrepriseController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @GetMapping
    public RestResponse getEntreprises(
            @RequestParam(value = "pageNumber", defaultValue = AppsConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppsConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppsConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppsConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return entrepriseService.getEntreprises(pageNumber, pageSize, sortBy, sortDir);
    }

    @PostMapping
    public ResponseEntity<com.stockback.response.ResponseBody> createEntreprise(@RequestBody EntrepriseDto entrepriseDto) {
        com.stockback.response.ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Catégorie enregistrée avec succès");
        entrepriseService.addEntreprise(entrepriseDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<EntrepriseDto>> getEntreprise(@PathVariable Integer id) {
        return new ResponseEntity<>(entrepriseService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{nom}")
    public ResponseEntity<Optional<EntrepriseDto>> getEntrepriseCode(@PathVariable String nom) {
        return new ResponseEntity<>(entrepriseService.findByNom(nom), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody> updateEntreprise(@RequestBody EntrepriseDto entrepriseDto, @PathVariable Integer id) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Entreprise modifiée avec succès");
        entrepriseService.updateEntreprise(entrepriseDto, id);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> deleteEntreprise(@PathVariable Integer id) {
        ResponseBody response = new ResponseBody();
        try {
            entrepriseService.deleteEntreprise(id);
            response.setStatus("success");
            response.setMessage("Entreprise supprimée  avec succès");
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Erreur lors de la suppression de l'entreprise'");
            return ResponseEntity.status(500).body(response);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
