package com.app.restengine.dao;

import com.app.restengine.domain.ServiceData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDataRepository extends ElasticsearchRepository<ServiceData, String> {
}
