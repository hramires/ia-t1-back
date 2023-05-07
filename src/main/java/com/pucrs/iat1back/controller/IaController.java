package com.pucrs.iat1back.controller;

import com.pucrs.iat1back.dto.CalculoDTO;
import com.pucrs.iat1back.dto.MatrizDTO;
import com.pucrs.iat1back.service.KnnService;
import com.pucrs.iat1back.service.MlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class IaController {

    @Autowired
    private KnnService knnService;

    @Autowired
    private MlpService mlpService;

    @PostMapping("/knn/calcular")
    public ResponseEntity<CalculoDTO> calcularKnn(@RequestBody MatrizDTO matriz) throws IOException {
        return knnService.calcular(matriz);
    }

    @PostMapping("/mlp/calcular")
    public ResponseEntity<CalculoDTO> calcularMlp(@RequestBody MatrizDTO matriz) throws IOException {
        return mlpService.calcular(matriz);
    }

}
