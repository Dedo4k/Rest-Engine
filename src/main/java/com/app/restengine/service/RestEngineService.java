package com.app.restengine.service;

import com.app.restengine.dao.ServiceConfigurationRepository;
import com.app.restengine.dao.ServiceDataRepository;
import com.app.restengine.domain.ServiceConfiguration;
import com.app.restengine.domain.ServiceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Scope("singleton")
public class RestEngineService {

    private final Logger logger = LoggerFactory.getLogger(RestEngineService.class);
    private final int corePoolSize;
    private final ScheduledExecutorService executorService;
    private final Map<Integer, ScheduledFuture<?>> runningTasks;
    private final ServiceDataRepository serviceDataRepository;
    private final ServiceConfigurationRepository serviceConfigurationRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public RestEngineService(@Value("${rest-engine.threads}") int corePoolSize,
                             ServiceDataRepository serviceDataRepository,
                             ServiceConfigurationRepository serviceConfigurationRepository) {
        this.corePoolSize = corePoolSize;
        this.serviceDataRepository = serviceDataRepository;
        this.serviceConfigurationRepository = serviceConfigurationRepository;
        this.restTemplate = new RestTemplate();
        this.executorService = Executors.newScheduledThreadPool(this.corePoolSize);
        runningTasks = new HashMap<>();
    }

    public ServiceConfiguration runService(ServiceConfiguration configuration) {
        ServiceConfiguration service = serviceConfigurationRepository.save(configuration);

        Runnable serviceTask = () -> {
            try {
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(configuration.getUri());
                configuration.getQueryParams().forEach(uriBuilder::queryParam);
                HttpHeaders headers = new HttpHeaders();
                configuration.getHeaders().forEach((key, value) -> headers.addAll(key, Arrays.asList(value.split(","))));
                HttpEntity<Object> request = new HttpEntity<>(configuration.getBody(), headers);
                ResponseEntity<String> response = null;
                switch (configuration.getMethod()) {
                    case GET -> response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
                    case POST, PUT, DELETE -> response = restTemplate.postForEntity(uriBuilder.toUriString(), request, String.class);
                }
                serviceDataRepository.save(new ServiceData(service.getId(), Instant.now(), response));
            } catch (Exception e) {
                logger.error("Service interruption due to error. Service configuration ID: " + configuration.getId(), e);
                stopService(configuration.getId());
            }
        };

        ScheduledFuture<?> scheduledTask = executorService.scheduleAtFixedRate(serviceTask, 0, 1, TimeUnit.SECONDS);
        logger.info("Service was started. Service configuration ID: " + configuration.getId());
        runningTasks.put(service.getId(), scheduledTask);
        return service;
    }

    public boolean stopService(int id) {
        boolean cancel = runningTasks.get(id).cancel(true);
        runningTasks.remove(id);
        logger.info("Service was stopped. Service configuration ID: " + id);
        return cancel;
    }
}
