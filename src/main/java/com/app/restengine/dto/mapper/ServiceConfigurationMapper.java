package com.app.restengine.dto.mapper;

import com.app.restengine.domain.ServiceConfiguration;
import com.app.restengine.dto.ServiceConfigurationCreationDTO;
import com.app.restengine.dto.ServiceConfigurationDTO;
import com.app.restengine.dto.ServiceConfigurationIdDTO;

public class ServiceConfigurationMapper {

    public static ServiceConfiguration fromDTO(ServiceConfigurationCreationDTO dto) {
        return new ServiceConfiguration(
                dto.uri,
                dto.method,
                dto.queryParams,
                dto.headers,
                dto.body,
                dto.initialDelay,
                dto.period);
    }

    public static ServiceConfigurationDTO toDTO(ServiceConfiguration entity) {
        return new ServiceConfigurationDTO(
                entity.getId(),
                entity.getUri(),
                entity.getMethod(),
                entity.getQueryParams(),
                entity.getHeaders(),
                entity.getBody());
    }

    public static ServiceConfigurationIdDTO toIdDTO(ServiceConfiguration entity) {
        return new ServiceConfigurationIdDTO(entity.getId());
    }
}
