package org.md.api.auth.service;

import org.md.api.auth.model.Token;
import org.md.api.auth.model.TokenDetails;
import org.md.api.auth.model.exception.InvalidTokenException;

public interface ITokenVerificationService {
    public TokenDetails verifyTokenDetails(Token token) throws InvalidTokenException;
}
