package org.md.api.auth.controller;

import org.md.api.auth.model.Token;
import org.md.api.auth.model.TokenDetails;
import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.md.api.auth.model.exception.InvalidTokenException;
import org.md.api.auth.service.ITokenGeneratorService;
import org.md.api.auth.service.ITokenVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
public class AuthController {
    
    @Autowired
    private ITokenGeneratorService tokenGeneratorService;
    
    @Autowired
    private ITokenVerificationService tokenVerificationService;

    @ApiOperation(value = "Request a JWT token for a user.")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "token was successfully created") })
    @RequestMapping(path="/signIn", method=RequestMethod.POST)
    public ResponseEntity<Token> signIn(@RequestBody UserCredentials credentials) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Token token = null;
        try {
            token = tokenGeneratorService.generateToken(credentials);
            status = HttpStatus.CREATED;
        } catch (InvalidCredentialsException e) {
            status = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Token>(token, status);
    }

    @ApiOperation(value = "Verifies the validity of a JWT token.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "token validated") })
    @RequestMapping(path="/verify", method=RequestMethod.POST)
    public ResponseEntity<TokenDetails> verify(@RequestBody Token token) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        TokenDetails details = null;
        try {
            details = tokenVerificationService.verifyTokenDetails(token);
            status = HttpStatus.CREATED;
        } catch (InvalidTokenException e) {
            status = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<TokenDetails>(details, status);
    }
}
