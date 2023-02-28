package com.stockback.controller;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.CategorieDto;
import com.stockback.response.ResponseBody;
import com.stockback.response.RestResponse;
import com.stockback.service.CategorieService;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/categories")
public class CategorieController {

    private final CategorieService categorieService;

    @Autowired
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping
    public RestResponse getCategories(
            @RequestParam(value = "pageNumber", defaultValue = AppsConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppsConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppsConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppsConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return categorieService.getCategories(pageNumber, pageSize, sortBy, sortDir);
    }
    @PostMapping
    public ResponseEntity<ResponseBody> createCategorie(@RequestBody CategorieDto categorieDto) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Catégorie enregistrée avec succès");
        categorieService.addCategorie(categorieDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CategorieDto>> getCategorie(@PathVariable Integer id) {
        return new ResponseEntity<>(categorieService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Optional<CategorieDto>> getCategorieCode(@PathVariable String code) {
        return new ResponseEntity<>(categorieService.findByCode(code), HttpStatus.OK);
    }

   /* @GetMapping
    public ResponseEntity<Optional<CategorieDto>> getCategorieCode(@Param("code") String code) {
        return new ResponseEntity<>(categorieService.findByCode(code), HttpStatus.OK);
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody> updateCategorie(@RequestBody CategorieDto categorieDto, @PathVariable Integer id) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Categorie modifiée avec succès");
        categorieService.updateCategorie(categorieDto, id);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> deleteCategorie(@PathVariable Integer id) {
        ResponseBody response = new ResponseBody();
        try {
            categorieService.deleteCategorie(id);
            response.setStatus("success");
            response.setMessage("Categorie supprimée  avec succès");
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Erreur lors de la suppression de la categorie");
            return ResponseEntity.status(500).body(response);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
