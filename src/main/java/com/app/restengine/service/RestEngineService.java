package com.app.restengine.service;

import com.app.restengine.dao.ServiceConfigurationRepository;
import com.app.restengine.dao.ServiceDataRepository;
import com.app.restengine.domain.EngineInfo;
import com.app.restengine.domain.ServiceConfiguration;
import com.app.restengine.domain.TaskInfo;
import com.app.restengine.engine.RestEngine;
import com.app.restengine.engine.RestEngineScheduledFuture;
import com.app.restengine.engine.RestEngineTask;
import com.app.restengine.exception.ServiceConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class RestEngineService {

    private final Logger logger = LoggerFactory.getLogger(RestEngineService.class);
    private final int corePoolSize;
    private final int minPeriod;
    private final RestEngine executorService;
    private final Map<Integer, RestEngineScheduledFuture<?>> runningTasks;
    private final ServiceDataRepository serviceDataRepository;
    private final ServiceConfigurationRepository serviceConfigurationRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public RestEngineService(@Value("${rest-engine.threads}") int corePoolSize,
                             @Value("${rest-engine.min-period}") int minPeriod,
                             ServiceDataRepository serviceDataRepository,
                             ServiceConfigurationRepository serviceConfigurationRepository) {
        this.corePoolSize = corePoolSize;
        this.minPeriod = minPeriod;
        this.serviceDataRepository = serviceDataRepository;
        this.serviceConfigurationRepository = serviceConfigurationRepository;
        this.restTemplate = new RestTemplate();
        this.executorService = new RestEngine(this.corePoolSize);
        runningTasks = new HashMap<>();
    }

    public ServiceConfiguration runService(ServiceConfiguration configuration) {
        if (configuration.getPeriod() < minPeriod) {
            throw new ServiceConfigurationException("Period value must be greater than " + minPeriod);
        }
        if (configuration.getInitialDelay() < 0) {
            throw new ServiceConfigurationException("Initial delay cant be less than 0");
        }

        ServiceConfiguration service = serviceConfigurationRepository.save(configuration);

        RestEngineTask serviceTask = new RestEngineTask(configuration, restTemplate, serviceDataRepository, this);

        RestEngineScheduledFuture<?> scheduledTask = executorService.scheduleAtFixedRate(
                serviceTask,
                configuration.getInitialDelay(),
                configuration.getPeriod(),
                TimeUnit.SECONDS);
        logger.info("Service was started. Service configuration ID: " + configuration.getId());
        runningTasks.put(service.getId(), scheduledTask);
        return service;
    }

    public int stopService(int id) {
        try {
            boolean cancel = runningTasks.get(id).cancel(true);
            runningTasks.remove(id);
            logger.info("Service was stopped. Service configuration ID: " + id);
            return Boolean.compare(cancel, false);
        } catch (NullPointerException e) {
            throw new ServiceConfigurationException("Service configuration not found with id:" + id);
        }
    }

    public EngineInfo getInfo() {
        return new EngineInfo(
                executorService.getCorePoolSize(),
                executorService.getMaximumPoolSize(),
                executorService.getActiveCount(),
                executorService.getTaskCount(),
                executorService.getQueue().size(),
                runningTasks.entrySet().stream()
                        .map(runningTask -> new TaskInfo(runningTask.getKey()))
                        .collect(Collectors.toList()));
    }
}
