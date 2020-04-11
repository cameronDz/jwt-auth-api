package org.md.api.auth.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.md.api.auth.model.UserCreationDetails;
import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
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
    
    private String SUCCESSFULLY_CREATE_USER_MESSAGE = "User was successfully created using token";
    private String USER_ALREADY_EXISTS_MESSAGE = "Could not create new credentials, username already exists";
    
    private String USERNAME_KEY = "username";
    private String PASSWORD_KEY = "password";
    private String USER_LIST_KEY = "userList";
    private String PAYLOAD_KEY = "payload";

    @Value("${s3.access.api.url.base}")
    private String s3ApiBaseUrl;

    @Value("${s3.access.api.url.endpoint.update}")
    private String s3ApiUpdateEndpoint;

    @Value("${s3.access.api.url.endpoint.list}")
    private String s3ApiListEndpoint;

    @Value("${s3.access.api.token}")
    private String s3ApiToken;

    public List<UserCredentials> getUserCredentialsList() throws JsonMappingException, JsonProcessingException {
        ArrayNode array = (ArrayNode) getUserCredentialsJsonObject();
        JsonNode arrayNode = null;
        UserCredentials user = null;
        int length = array.size();
        List<UserCredentials> users = new ArrayList<UserCredentials>();
        for (int i = 0; i < length; i++) {
            arrayNode = array.get(i);
            user = new UserCredentials(arrayNode.get(USERNAME_KEY).asText(), arrayNode.get(PASSWORD_KEY).asText());
            users.add(user);
        }
        return users;
    }
    
    public UserCreationDetails createUserCredentials(UserCredentials credentials) throws JsonMappingException, JsonProcessingException, InvalidCredentialsException {
        ArrayNode array = (ArrayNode) getUserCredentialsJsonObject();
        checkNodeForExistingUsername(array, credentials.getUsername());
        JsonNode newCredential = new ObjectMapper().valueToTree(credentials);
        array.add(newCredential);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(USER_LIST_KEY, array);
        updateCredentialsObjectWithNewUserCredentials(new ObjectMapper().valueToTree(map));
        return new UserCreationDetails(credentials.getUsername(), SUCCESSFULLY_CREATE_USER_MESSAGE, new Date());
    }
    
    private void checkNodeForExistingUsername(ArrayNode array, String username) throws InvalidCredentialsException {
        JsonNode arrayNode = null;
        int count = array.size();
        for (int i = 0; i < count; i++) {
            arrayNode = array.get(i);
            if (arrayNode.get(USERNAME_KEY).asText().equals(username)) {
                throw new InvalidCredentialsException(USER_ALREADY_EXISTS_MESSAGE);
            }
        }        
    }
    
    private JsonNode getUserCredentialsJsonObject() throws JsonMappingException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String listUrl = s3ApiBaseUrl + s3ApiListEndpoint;
        String result = restTemplate.getForObject(listUrl, String.class);
        return new ObjectMapper().readTree(result).get(PAYLOAD_KEY).get(USER_LIST_KEY);
    }
    
    private void updateCredentialsObjectWithNewUserCredentials(JsonNode node) {
        RestTemplate restTemplate = new RestTemplate();
        String url = s3ApiBaseUrl + s3ApiUpdateEndpoint;
        restTemplate.put(url, node);
    }
}
