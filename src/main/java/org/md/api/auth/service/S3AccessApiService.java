package org.md.api.auth.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.md.api.auth.model.UserCreationDetails;
import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.md.api.auth.utility.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
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

    private String ACCESS_KEY = "access";
    private String DEFAULT_ACCESS = "READ,WRITE";
    private String PASSWORD_KEY = "password";
    private String PAYLOAD_KEY = "payload";
    private String USERNAME_KEY = "username";
    private String USER_LIST_KEY = "userList";

    @Value("${credential.salt.key}")
    private String saltKey;

    @Value("${s3.access.api.url.base}")
    private String s3ApiBaseUrl;

    @Value("${s3.access.api.url.endpoint.create}")
    private String s3ApiCreateEndpoint;

    @Value("${s3.access.api.url.endpoint.get}")
    private String s3ApiGetEndpoint;

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

    public UserCredentials getSecureUserCredentials(UserCredentials credentials) throws JsonMappingException, JsonProcessingException {
		String username = credentials.getUsername();
		JsonNode node = getSecureUserCredentialsJsonObject(username);
		String hashedPassword = node.get(PASSWORD_KEY).asText();
		return new UserCredentials(username, hashedPassword);
    }

    public UserCreationDetails createUserCredentials(UserCredentials credentials) throws JsonMappingException, JsonProcessingException, InvalidCredentialsException {
        ArrayNode array = (ArrayNode) getUserCredentialsJsonObject();
        checkNodeForExistingUsername(array, credentials.getUsername());
        JsonNode newCredential = new ObjectMapper().valueToTree(credentials);
        array.add(newCredential);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(USER_LIST_KEY, array);
        updateCredentialsObjectWithNewUserCredentials(map);
        return new UserCreationDetails(credentials.getUsername(), SUCCESSFULLY_CREATE_USER_MESSAGE, new Date());
    }

    public UserCreationDetails createSecureUserCredentials(UserCredentials credentials) throws NoSuchAlgorithmException, JsonProcessingException, UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<String, Object>();
        String hashedPassword = PasswordEncoder.encode(credentials.getPassword(), saltKey);
        String username = credentials.getUsername();
        map.put(PASSWORD_KEY, hashedPassword);
        map.put(USERNAME_KEY, username);
        map.put(ACCESS_KEY, DEFAULT_ACCESS);
        createCredentialsObjectWithNewUserCredentials(username, map);
        return new UserCreationDetails(username, SUCCESSFULLY_CREATE_USER_MESSAGE, new Date());
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

    private JsonNode getSecureUserCredentialsJsonObject(String username) throws JsonMappingException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        String url = s3ApiBaseUrl + s3ApiGetEndpoint + "/" + username;
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return new ObjectMapper().readTree(result.getBody()).get(PAYLOAD_KEY);
    }

    private void createCredentialsObjectWithNewUserCredentials(String username, Map<String, Object> node) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String body = new ObjectMapper().writeValueAsString(node);

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        String url = s3ApiBaseUrl + s3ApiCreateEndpoint + "/" + username;
        restTemplate.postForObject(url, entity, String.class);
    }

    private void updateCredentialsObjectWithNewUserCredentials(Map<String, Object> node) throws RestClientException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String body = new ObjectMapper().writeValueAsString(node);

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        String url = s3ApiBaseUrl + s3ApiUpdateEndpoint;
        restTemplate.put(url, entity, String.class);
    }

    private HttpHeaders createHttpHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + s3ApiToken);
        return headers;
    }
}
