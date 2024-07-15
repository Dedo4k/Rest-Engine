package com.app.restengine.dto;

import com.app.restengine.constant.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfigurationCreationDTO {
    public String uri;
    public HttpMethod method;
    public HashMap<String, String> queryParams = new HashMap<>();
    public HashMap<String, String> headers = new HashMap<>();
    public HashMap<String, Object> body = new HashMap<>();
    public long initialDelay;
    public long period;
}
