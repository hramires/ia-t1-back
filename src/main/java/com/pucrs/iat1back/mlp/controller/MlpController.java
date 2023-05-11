package com.pucrs.iat1back.mlp.controller;

import com.pucrs.iat1back.dto.CalculoDTO;
import com.pucrs.iat1back.mlp.service.MlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/mlp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class MlpController {

    @Autowired
    private MlpService mlpService;

    @PostMapping("/calcular")
    public ResponseEntity<CalculoDTO> calcularMlp(@RequestBody List<String> matriz) throws Exception {
        System.out.println("matriz: " + matriz);

        return mlpService.calcular(matriz);
    }

    @PostMapping("/avaliar")
    public ResponseEntity<String> avaliar() throws Exception {
        return mlpService.avaliar();
    }

}
