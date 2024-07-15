package com.app.restengine.domain;

import com.app.restengine.constant.HttpMethod;
import com.app.restengine.util.HashMapConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@Entity(name = "service_configuration")
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfiguration {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "uri", nullable = false)
    private String uri;
    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpMethod method;
    @Column(name = "query_params")
    @Convert(converter = HashMapConverter.class)
    private HashMap<String, String> queryParams = new HashMap<>();
    @Column(name = "headers")
    @Convert(converter = HashMapConverter.class)
    private HashMap<String, String> headers = new HashMap<>();
    @Column(name = "body")
    @Convert(converter = HashMapConverter.class)
    private HashMap<String, Object> body = new HashMap<>();

    public ServiceConfiguration(String uri, HttpMethod method, HashMap<String, String> queryParams,
                                HashMap<String, String> headers, HashMap<String, Object> body) {
        this.uri = uri;
        this.method = method;
        this.queryParams = queryParams;
        this.headers = headers;
        this.body = body;
    }
}
