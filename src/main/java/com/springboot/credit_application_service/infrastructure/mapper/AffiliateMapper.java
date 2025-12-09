package com.springboot.credit_application_service.infrastructure.mapper;

import com.springboot.credit_application_service.domain.model.Affiliate;
import com.springboot.credit_application_service.infrastructure.persistence.entity.AffiliateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AffiliateMapper {
    AffiliateEntity toEntity(Affiliate domain);

    Affiliate toDomain(AffiliateEntity entity);
}
