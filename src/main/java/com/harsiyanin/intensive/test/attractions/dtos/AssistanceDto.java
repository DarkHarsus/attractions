package com.harsiyanin.intensive.test.attractions.dtos;

import com.harsiyanin.intensive.test.attractions.entities.AssistanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssistanceDto {
    private Long id;
    private AssistanceType type;
    private String description;
    private String provider;
}
