package com.pucrs.iat1back.mlp.service;

import com.pucrs.iat1back.dto.CalculoDTO;
import com.pucrs.iat1back.enumerator.StatusEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MlpService {

    public ResponseEntity<CalculoDTO> calcular(List<String> matriz) {

        return ResponseEntity.ok(
                CalculoDTO.builder()
                        .status(StatusEnum.CONTINUA)
                        .build());
    }
}
