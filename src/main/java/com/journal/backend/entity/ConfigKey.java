package com.journal.backend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "app_config")
public class ConfigKey {
    private String key;
    private String value;
}