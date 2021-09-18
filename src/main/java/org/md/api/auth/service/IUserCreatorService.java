package org.md.api.auth.service;

import org.md.api.auth.model.UserCreationDetails;
import org.md.api.auth.model.UserCreationToken;
import org.md.api.auth.model.exception.InvalidCredentialsException;

public interface IUserCreatorService {
    public UserCreationDetails createSecureUserCredentials(UserCreationToken userCreationToken) throws InvalidCredentialsException;
}
