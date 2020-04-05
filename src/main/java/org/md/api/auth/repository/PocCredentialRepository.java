package org.md.api.auth.repository;

import java.util.Arrays;
import java.util.List;

import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.md.api.auth.utility.CredentialValidatorUtility;
import org.springframework.boot.SpringBootConfiguration;

/**
 * repository used to test usability of auth api
 */
@SpringBootConfiguration
public class PocCredentialRepository implements ICredentialRepository {

    // mocked up data for POC
    private final List<UserCredentials> USER_LIST = Arrays.asList(new UserCredentials[] {
            new UserCredentials("Cam", "SecretPassword"),
            new UserCredentials("TestUser", "TestPassword"),
            new UserCredentials("FakeUser", "FakePassword")
    });
    
    @Override
    public void credentialsAreValid(String username, String password) throws InvalidCredentialsException {
        if (!CredentialValidatorUtility.usernameAndPasswordAreValid(new UserCredentials(username, password), USER_LIST)) {
            throw new InvalidCredentialsException();
        }
    }
}
