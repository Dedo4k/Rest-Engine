package com.app.restengine.engine;

import com.app.restengine.dao.ServiceDataRepository;
import com.app.restengine.domain.ServiceConfiguration;
import com.app.restengine.domain.ServiceData;
import com.app.restengine.service.RestEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Arrays;

public class RestEngineTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(RestEngineTask.class);
    private ServiceConfiguration configuration;
    private RestTemplate restTemplate;
    private ServiceDataRepository dataRepository;
    private RestEngineService engineService;

    public RestEngineTask(ServiceConfiguration configuration,
                          RestTemplate restTemplate,
                          ServiceDataRepository dataRepository,
                          RestEngineService engineService) {
        this.configuration = configuration;
        this.restTemplate = restTemplate;
        this.dataRepository = dataRepository;
        this.engineService = engineService;
    }

    @Override
    public void run() {
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(configuration.getUri());
            configuration.getQueryParams().forEach(uriBuilder::queryParam);
            HttpHeaders headers = new HttpHeaders();
            configuration.getHeaders().forEach((key, value) -> headers.addAll(key, Arrays.asList(value.split(","))));
            HttpEntity<Object> request = new HttpEntity<>(configuration.getBody(), headers);
            ResponseEntity<String> response = null;
            switch (configuration.getMethod()) {
                case GET -> response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
                case POST, PUT, DELETE ->
                        response = restTemplate.postForEntity(uriBuilder.toUriString(), request, String.class);
            }
            dataRepository.save(new ServiceData(configuration.getId(), Instant.now(), response));
        } catch (Exception e) {
            logger.error("Service interruption due to error. Service configuration ID: " + configuration.getId(), e);
            engineService.stopService(configuration.getId());
        }
    }

    public ServiceConfiguration getConfiguration() {
        return configuration;
    }
}
