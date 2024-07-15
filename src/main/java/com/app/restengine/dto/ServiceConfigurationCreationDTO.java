package com.app.restengine.dto;

import com.app.restengine.constant.HttpMethod;
import lombok.AllArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
public class ServiceConfigurationCreationDTO {
    public String uri;
    public HttpMethod method;
    public HashMap<String, String> queryParams;
    public HashMap<String, String> headers;
    public HashMap<String, Object> body;
}
