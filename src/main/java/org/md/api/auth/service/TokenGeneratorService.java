package org.md.api.auth.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.md.api.auth.model.Token;
import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.md.api.auth.repository.ICredentialRepository;
import org.md.api.auth.utility.GeneralUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootConfiguration
public class TokenGeneratorService implements ITokenGeneratorService {

    private final long SECOND = 1000;
    private final long MINUTE = 60 * SECOND;
    private final long TEN_MINUTE = 10 * MINUTE;
    private final long HOUR = 6 * TEN_MINUTE;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Autowired
    private ICredentialRepository credentialRepository;

    public Token generateToken(UserCredentials credentials) throws InvalidCredentialsException {
        credentialsExist(credentials);
        credentialRepository.credentialsAreValid(credentials.getUsername(), credentials.getPassword());
        return createJwtToken(credentials.getUsername());
    }

    public Token generateSecureToken(UserCredentials credentials) throws InvalidCredentialsException {
        credentialsExist(credentials);
        credentialRepository.secureCredentialsAreValid(credentials.getUsername(), credentials.getPassword());
        return createJwtToken(credentials.getUsername());
    }

    /**
     * create a jwt token based off a verified username
     * @param username name that has been verified against a credential repository
     * @return jwt token
     */
    private Token createJwtToken(String username) {
        return new Token(createJWT("123", "AUTH-API", "JWT", HOUR));
    }

    /**
     * throw exception if credentials are null or either username/password are null or empty
     * @param credentials user credentials being c
     * @throws Exception
     */
    private void credentialsExist(UserCredentials credentials) throws InvalidCredentialsException {
        if (credentials == null || GeneralUtility.stringIsNullOrEmpty(credentials.getPassword()) || GeneralUtility.stringIsNullOrEmpty(credentials.getUsername())) {
            throw new InvalidCredentialsException();
        }
    }

    //Sample method to construct a JWT (from okta)
    private String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

}
