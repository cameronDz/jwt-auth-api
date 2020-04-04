package org.md.api.auth.repository;

import java.util.Arrays;
import java.util.List;

import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class CredentialRepository implements ICredentialRepository {

    // mocked up data for POC
    private final List<UserCredentials> USER_LIST = Arrays.asList(new UserCredentials[] {
            new UserCredentials("Cam", "SecretPassword"),
            new UserCredentials("TestUser", "TestPassword"),
            new UserCredentials("FakeUser", "FakePassword")
    });
    
    @Override
    public void credentialsAreValid(String username, String password) throws InvalidCredentialsException {
        if (!validatedCredentials(username, password)) {
            throw new InvalidCredentialsException();
        }
    }

    /**
     * cycle through list of username to check for a valid user
     * @param username cred name
     * @param password cred password
     * @return true if matching username and password can be found in list, false otherwise
     */
    private boolean validatedCredentials(String username, String password) {
        boolean isUserValidated = false;
        int length = USER_LIST.size();
        UserCredentials user = null;
        for (int i = 0; i < length; i++) {
            user = USER_LIST.get(i);
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                isUserValidated = true;
                break;
            }
        }
        return isUserValidated;
    }
}
