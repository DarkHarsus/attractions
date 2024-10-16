package com.harsiyanin.intensive.test.attractions.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.harsiyanin.intensive.test.attractions.entities.AttractionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDto {
    private Long id;
    private String name;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate creationDate;

    private String description;
    private AttractionType type;
    private List<AssistanceDto> assistanceList;
    private Long localityId;
}
