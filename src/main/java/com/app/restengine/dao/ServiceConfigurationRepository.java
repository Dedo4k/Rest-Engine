package com.app.restengine.dao;

import com.app.restengine.domain.ServiceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceConfigurationRepository extends JpaRepository<ServiceConfiguration, Integer> {
}
