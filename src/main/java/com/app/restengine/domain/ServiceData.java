package com.app.restengine.domain;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Data
@Document(indexName = "service-data")
public class ServiceData {
    @Id
    private String id;
    private int serviceId;
    @Field(type = FieldType.Date)
    @CreatedDate
    private Instant timestamp;
    @Field(type = FieldType.Object)
    private ResponseEntity<String> response;

    public ServiceData(int serviceId, Instant timestamp, ResponseEntity<String> response) {
        this.serviceId = serviceId;
        this.timestamp = timestamp;
        this.response = response;
    }
}
