package com.pucrs.iat1back.dto;

import com.pucrs.iat1back.enumerator.StatusEnum;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CalculoDTO {

    private StatusEnum status;

}
