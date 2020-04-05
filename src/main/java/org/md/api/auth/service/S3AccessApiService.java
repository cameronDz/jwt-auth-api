package org.md.api.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.md.api.auth.model.UserCredentials;
import org.springframework.beans.factory.annotation.Value;

public class S3AccessApiService {

    @Value("${s3.access.api.url}")
    private String s3ApiUrl;

    @Value("${s3.access.api.token}")
    private String s3ApiToken;

    public List<UserCredentials> getUserCredentialsList() {
        return new ArrayList<UserCredentials>();
    }
}
