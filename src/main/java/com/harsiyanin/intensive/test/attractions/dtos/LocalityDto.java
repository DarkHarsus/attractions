package com.harsiyanin.intensive.test.attractions.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocalityDto {
    private Long id;
    private String city;
    private String region;
    private List<AttractionDto> attractions;
    private Double latitude;
    private Double longitude;
}
