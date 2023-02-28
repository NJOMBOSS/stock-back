package com.stockback.controller;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.ClientDto;
import com.stockback.dto.MvtStockDto;
import com.stockback.response.ResponseBody;
import com.stockback.response.RestResponse;
import com.stockback.service.MvtStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mvtstocks")
public class MvtStockController {

    private final MvtStockService mvtStockService;

    @Autowired
    public MvtStockController(MvtStockService mvtStockService) {
        this.mvtStockService = mvtStockService;
    }
    @GetMapping
    public RestResponse getMvtStocks(
            @RequestParam(value = "pageNumber", defaultValue = AppsConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppsConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppsConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppsConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return mvtStockService.getMvtStocks(pageNumber, pageSize, sortBy, sortDir);
    }

    @PostMapping
    public ResponseEntity<com.stockback.response.ResponseBody> createMvtStock(@RequestBody MvtStockDto mvtStockDto) {
        com.stockback.response.ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Libelle mouvement en stock enregistré avec succès");
        mvtStockService.addMvtStock(mvtStockDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<MvtStockDto>> getMvtStock(@PathVariable Integer id) {
        return new ResponseEntity<>(mvtStockService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody> updateMvtStock(@RequestBody MvtStockDto mvtStockDto, @PathVariable Integer id) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Libelle mouvement en stock modifié avec succès");
        mvtStockService.updateMvtStock(mvtStockDto, id);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> deleteMvtStock(@PathVariable Integer id) {
        ResponseBody response = new ResponseBody();
        try {
            mvtStockService.deleteMvtStock(id);
            response.setStatus("success");
            response.setMessage("Client supprimé  avec succès");
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Erreur lors de la suppression du client");
            return ResponseEntity.status(500).body(response);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
