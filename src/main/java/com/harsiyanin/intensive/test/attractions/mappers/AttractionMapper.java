package com.harsiyanin.intensive.test.attractions.mappers;

import com.harsiyanin.intensive.test.attractions.dtos.AttractionCreateDto;
import com.harsiyanin.intensive.test.attractions.dtos.AttractionDto;
import com.harsiyanin.intensive.test.attractions.entities.Attraction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AttractionMapper {

    AttractionMapper INSTANCE = Mappers.getMapper(AttractionMapper.class);
    @Mapping(source = "locality.id", target = "localityId")
    AttractionDto toDto(Attraction attraction);
    Attraction toEntity(AttractionCreateDto attractionCreateDto);
}
