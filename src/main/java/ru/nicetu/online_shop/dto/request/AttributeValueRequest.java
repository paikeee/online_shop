package ru.nicetu.online_shop.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Map;

@Getter
public class AttributeValueRequest {

    public AttributeValueRequest(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<Integer, String>> typeRef = new TypeReference<Map<Integer, String>>() {};
        this.values = mapper.readValue(json, typeRef);
    }

    @JsonProperty("map")
    private Map<Integer, String> values;

}
