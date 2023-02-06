package com.stockback.controller;

import com.stockback.dto.CategorieDto;
import com.stockback.response.ResponseBody;
import com.stockback.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/categories")
public class CategorieController {

    private final CategorieService categorieService;

    @Autowired
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @PostMapping
    public ResponseEntity<ResponseBody> createEntreprise(@RequestBody CategorieDto categorieDto) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Catégorie enregistrée avec succès");
        categorieService.save(categorieDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

}
