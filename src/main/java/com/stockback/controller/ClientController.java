package com.stockback.controller;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.ClientDto;
import com.stockback.response.ResponseBody;
import com.stockback.response.RestResponse;
import com.stockback.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public RestResponse getClients(
            @RequestParam(value = "pageNumber", defaultValue = AppsConstant.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppsConstant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppsConstant.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppsConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return clientService.getClients(pageNumber, pageSize, sortBy, sortDir);
    }

    @PostMapping
    public ResponseEntity<com.stockback.response.ResponseBody> createClient(@RequestBody ClientDto clientDto) {
        com.stockback.response.ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Catégorie enregistrée avec succès");
        clientService.addClient(clientDto);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ClientDto>> getClient(@PathVariable Integer id) {
        return new ResponseEntity<>(clientService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody> updateClient(@RequestBody ClientDto clientDto, @PathVariable Integer id) {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("success");
        responseBody.setMessage("Client modifié avec succès");
        clientService.updateClient(clientDto, id);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> deleteClient(@PathVariable Integer id) {
        ResponseBody response = new ResponseBody();
        try {
            clientService.deleteClient(id);
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
