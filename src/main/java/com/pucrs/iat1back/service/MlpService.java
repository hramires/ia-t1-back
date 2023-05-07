package com.pucrs.iat1back.service;

import com.pucrs.iat1back.dto.CalculoDTO;
import com.pucrs.iat1back.dto.MatrizDTO;
import com.pucrs.iat1back.enumerator.StatusEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MlpService {

    public ResponseEntity<CalculoDTO> calcular(MatrizDTO matriz) {

        return ResponseEntity.ok(
                CalculoDTO.builder()
                        .status(StatusEnum.CONTINUA)
                        .build());
    }
}
