package com.app.restengine.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper;

    @Autowired
    public HashMapConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> value) {
        String jsonValue;
        try {
            jsonValue = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonValue;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String jsonValue) {
        Map<String, Object> value;
        try {
            value = objectMapper.readValue(jsonValue, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return value;
    }
}