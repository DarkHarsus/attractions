package com.harsiyanin.intensive.test.attractions.mappers;


import com.harsiyanin.intensive.test.attractions.dtos.LocalityDto;
import com.harsiyanin.intensive.test.attractions.entities.Locality;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocalityMapper {
    LocalityMapper INSTANCE = Mappers.getMapper(LocalityMapper.class);

    LocalityDto toDto(Locality locality);

    Locality toEntity(LocalityDto localityDto);
}
