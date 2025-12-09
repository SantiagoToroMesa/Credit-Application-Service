package com.springboot.credit_application_service.infrastructure.mapper;

import com.springboot.credit_application_service.domain.model.CreditApplication;
import com.springboot.credit_application_service.infrastructure.persistence.entity.CreditApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AffiliateMapper.class, RiskEvaluationMapper.class})
public interface CreditApplicationMapper {

    @Mapping(target = "id", expression = "java(String.valueOf(entity.getId()))")
    CreditApplication toDomain(CreditApplicationEntity entity);

    @Mapping(target = "id", ignore = true) // lo genera la BD
    CreditApplicationEntity toEntity(CreditApplication domain);
}

