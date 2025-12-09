package com.springboot.credit_application_service.infrastructure.mapper;

import com.springboot.credit_application_service.domain.model.RiskEvaluation;
import com.springboot.credit_application_service.infrastructure.persistence.entity.RiskEvaluationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RiskEvaluationMapper {

    RiskEvaluationEntity toEntity(RiskEvaluation domain);

    RiskEvaluation toDomain(RiskEvaluationEntity entity);
}
