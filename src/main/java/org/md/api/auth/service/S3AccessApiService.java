package org.md.api.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.md.api.auth.model.UserCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@SpringBootConfiguration
public class S3AccessApiService {

    @Value("${s3.access.api.url}")
    private String s3ApiUrl;

    @Value("${s3.access.api.token}")
    private String s3ApiToken;

    public List<UserCredentials> getUserCredentialsList() throws JsonMappingException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(s3ApiUrl, String.class);
        JsonNode node = new ObjectMapper().readTree(result);
        ArrayNode array = (ArrayNode) node.get("payload").get("userList");
        JsonNode arrayNode = null;
        UserCredentials user = null;
        int length = array.size();
        List<UserCredentials> users = new ArrayList<UserCredentials>();
        for (int i = 0; i < length; i++) {
            arrayNode = array.get(i);
            user = new UserCredentials(arrayNode.get("username").asText(), arrayNode.get("password").asText());
            users.add(user);
        }
        return users;
    }
}
