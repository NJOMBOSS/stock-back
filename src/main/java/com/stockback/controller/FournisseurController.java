package com.stockback.controller;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.FournisseurDto;
import com.stockback.response.ResponseBody;
import com.stockback.response.RestResponse;
import com.stockback.service.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/fournisseurs")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @Autowired
    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping
    public RestResponse getFournisseurs(
            @RequestParam(value = "pageNumber", defaultValue = AppsConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppsConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppsConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppsConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return fournisseurService.getFournisseurs(pageNumber, pageSize, sortBy, sortDir);
    }

    @PostMapping
    public ResponseEntity<com.stockback.response.ResponseBody> createFournisseur(@RequestBody FournisseurDto fournisseurDto) {
        com.stockback.response.ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Fournisseur enregistré avec succès");
        fournisseurService.addFournisseur(fournisseurDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<FournisseurDto>> getFournisseur(@PathVariable Integer id) {
        return new ResponseEntity<>(fournisseurService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody> updateFournisseur(@RequestBody FournisseurDto fournisseurDto, @PathVariable Integer id) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Fournisseur modifié avec succès");
        fournisseurService.updateFournisseur(fournisseurDto, id);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> deleteFournisseur(@PathVariable Integer id) {
        ResponseBody response = new ResponseBody();
        try {
            fournisseurService.deleteFournisseur(id);
            response.setStatus("success");
            response.setMessage("Fournisseur supprimé  avec succès");
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Erreur lors de la suppression du fournisseur");
            return ResponseEntity.status(500).body(response);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
