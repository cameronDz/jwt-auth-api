package org.md.api.auth.service;

import org.md.api.auth.model.Token;
import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;

public interface ITokenGeneratorService {

    public Token generateToken(UserCredentials credentials) throws InvalidCredentialsException;

    public Token generateSecureToken(UserCredentials credentials) throws InvalidCredentialsException;
}
