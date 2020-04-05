package org.md.api.auth.repository;

import java.util.List;

import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.md.api.auth.service.S3AccessApiService;
import org.md.api.auth.utility.CredentialValidatorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class S3CredentialsRepository implements ICredentialRepository {

    @Autowired
    private S3AccessApiService s3AccessApiService;
    
    @Override
    public void credentialsAreValid(String username, String password) throws InvalidCredentialsException {
        UserCredentials user = new UserCredentials(username, password);
        List<UserCredentials> userList;
        try {
            userList = s3AccessApiService.getUserCredentialsList();
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
        if (!CredentialValidatorUtility.usernameAndPasswordAreValid(user, userList)) {
            throw new InvalidCredentialsException();
        }
    }
}
