package org.md.api.auth.repository;

import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.md.api.auth.service.S3AccessApiService;
import org.md.api.auth.utility.GeneralUtility;
import org.md.api.auth.utility.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class S3CredentialsRepository implements ICredentialRepository {

    @Value("${credential.salt.key}")
    private String saltKey;

    @Autowired
    private S3AccessApiService s3AccessApiService;

    @Override
    public void secureCredentialsAreValid(String username, String password) throws InvalidCredentialsException {
        String hashedPassword, storedPassword;
        UserCredentials credentials = new UserCredentials(username, password);
        try {
            UserCredentials storedCredentials = s3AccessApiService.getSecureUserCredentials(credentials);
            storedPassword = storedCredentials.getPassword();
            hashedPassword = PasswordEncoder.encode(password, saltKey);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
        if (GeneralUtility.stringIsNullOrEmpty(storedPassword) || GeneralUtility.stringIsNullOrEmpty(hashedPassword) || !hashedPassword.equals(storedPassword)) {
            throw new InvalidCredentialsException();
        }
    }

    @Override
    public boolean repositoryIsLive() throws Exception {
        return s3AccessApiService.isAlive();
    }
}
