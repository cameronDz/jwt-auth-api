package org.md.api.auth.service;

import javax.xml.bind.DatatypeConverter;

import org.md.api.auth.model.Token;
import org.md.api.auth.model.TokenDetails;
import org.md.api.auth.model.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@SpringBootConfiguration
public class TokenVerificationService implements ITokenVerificationService {

    @Value("${jwt.secret.key}")
    private String secretKey;
    
    public TokenDetails verifyTokenDetails(Token token) throws InvalidTokenException {
        if (token == null || "".equals(token.getToken())) {
            throw new InvalidTokenException();
        }
        Claims claims = decodeJWT(token.getToken());
        return new TokenDetails(claims.toString(), claims.getExpiration(), 0);
    }    

    private Claims decodeJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
