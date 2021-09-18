package org.md.api.auth.service;

import org.md.api.auth.model.UserCreationDetails;
import org.md.api.auth.model.UserCreationToken;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.md.api.auth.utility.CredentialValidatorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class S3UserCreatorService implements IUserCreatorService {

    @Value("${credential.creation.token}")
    private String credentialCreationToken;

    @Autowired
    private S3AccessApiService s3AccessApiService;

    @Override
    public UserCreationDetails createSecureUserCredentials(UserCreationToken userCreationToken) throws InvalidCredentialsException {
        CredentialValidatorUtility.checkUserNameAreNullOrEmpty(userCreationToken);
        hasValidCreationToken(userCreationToken.getCreationToken());
        UserCreationDetails details = null;
        try {
            details = s3AccessApiService.createSecureUserCredentials(userCreationToken);
        } catch (Exception e) {
            throw new InvalidCredentialsException(e.getMessage());
        }
        return details;
    }

    private void hasValidCreationToken(String token) throws InvalidCredentialsException {
        if (credentialCreationToken == null || !credentialCreationToken.equals(token)) {
            throw new InvalidCredentialsException("Invalid credential creation token.");
        }
    }
}
