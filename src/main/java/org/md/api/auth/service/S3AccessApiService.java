package org.md.api.auth.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.md.api.auth.model.UserCreationDetails;
import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.utility.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootConfiguration
public class S3AccessApiService {

    private String ACCESS_KEY = "access";
    private String DEFAULT_ACCESS = "READ,WRITE";
    private String PASSWORD_KEY = "password";
    private String PAYLOAD_KEY = "payload";
    private String SUCCESSFULLY_CREATE_USER_MESSAGE = "User was successfully created using token";
    private String USERNAME_KEY = "username";

    @Value("${credential.salt.key}")
    private String saltKey;

    @Value("${s3.access.api.url.base}")
    private String s3ApiBaseUrl;

    @Value("${s3.access.api.url.endpoint.create}")
    private String s3ApiCreateEndpoint;

    @Value("${s3.access.api.url.endpoint.get}")
    private String s3ApiGetEndpoint;

    @Value("${s3.access.api.token}")
    private String s3ApiToken;

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

    public UserCredentials getSecureUserCredentials(UserCredentials credentials) throws JsonMappingException, JsonProcessingException {
        String username = credentials.getUsername();
        JsonNode node = getSecureUserCredentialsJsonObject(username);
        String hashedPassword = node.get(PASSWORD_KEY).asText();
        return new UserCredentials(username, hashedPassword);
    }

    private void createCredentialsObjectWithNewUserCredentials(String username, Map<String, Object> node) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String body = new ObjectMapper().writeValueAsString(node);

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        String url = s3ApiBaseUrl + s3ApiCreateEndpoint + "/" + username;
        restTemplate.postForObject(url, entity, String.class);
    }

    private JsonNode getSecureUserCredentialsJsonObject(String username) throws JsonMappingException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        String url = s3ApiBaseUrl + s3ApiGetEndpoint + "/" + username;
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return new ObjectMapper().readTree(result.getBody()).get(PAYLOAD_KEY);
    }

    private HttpHeaders createHttpHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + s3ApiToken);
        return headers;
    }
}
