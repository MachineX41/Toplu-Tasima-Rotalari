package org.example.Data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonNodeData extends AbstractData{
    private final String veri;
    private final ObjectMapper objectMapper;

    public JsonNodeData(ObjectMapper objectMapper) throws IOException {
        super();
        this.veri = super.get_data();
        this.objectMapper = objectMapper;
    }

    public JsonNode get_node_data() throws JsonProcessingException {
        return this.objectMapper.readTree(this.veri);
    }
}
