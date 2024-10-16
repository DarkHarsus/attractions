package com.harsiyanin.intensive.test.attractions.mappers;

import com.harsiyanin.intensive.test.attractions.dtos.AssistanceDto;
import com.harsiyanin.intensive.test.attractions.entities.Assistance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssistanceMapper {
    AssistanceMapper INSTANCE = Mappers.getMapper(AssistanceMapper.class);
//    @Mapping(source = "attraction.id", target = "attractionId")
    AssistanceDto toDto(Assistance assistance);
    Assistance toEntity(AssistanceDto assistanceDto);
}
