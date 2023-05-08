package com.pucrs.iat1back.knn.controller;

import com.pucrs.iat1back.dto.CalculoDTO;
import com.pucrs.iat1back.knn.service.KnnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/v1/knn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class KnnController {

    @Autowired
    private KnnService knnService;

    @PostMapping("/calcular")
    public ResponseEntity<CalculoDTO> calcularKnn(@RequestBody List<String> matriz) throws IOException {
        return knnService.calcular(matriz);
    }

}
