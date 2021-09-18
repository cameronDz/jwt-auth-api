package org.md.api.auth.controller;

import org.md.api.auth.model.Token;
import org.md.api.auth.model.TokenDetails;
import org.md.api.auth.model.UserCreationDetails;
import org.md.api.auth.model.UserCreationToken;
import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.md.api.auth.model.exception.InvalidTokenException;
import org.md.api.auth.repository.ICredentialRepository;
import org.md.api.auth.service.ITokenGeneratorService;
import org.md.api.auth.service.ITokenVerificationService;
import org.md.api.auth.service.IUserCreatorService;
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
    private ICredentialRepository credentialRepository;

    @Autowired
    private ITokenGeneratorService tokenGeneratorService;

    @Autowired
    private ITokenVerificationService tokenVerificationService;

    @Autowired
    private IUserCreatorService userCreatorService;

    @ApiOperation(value = "Create a new user credentials based off request body.")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "user created"),
        @ApiResponse(code = 400, message = "request could not be processed"),
        @ApiResponse(code = 422, message = "credentials could not be processed")
    })
    @RequestMapping(path="/create", method=RequestMethod.POST)
    public ResponseEntity<UserCreationDetails> secureCreateUser(@RequestBody UserCreationToken userCreationToken) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        UserCreationDetails details = null;
        try {
            details = userCreatorService.createSecureUserCredentials(userCreationToken);
            status = HttpStatus.CREATED;
        } catch (InvalidCredentialsException e) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<UserCreationDetails>(details, status);
    }

    @ApiOperation(value = "Request a JWT token for a secure user.")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "token was successfully created"),
        @ApiResponse(code = 400, message = "request could not be processed"),
        @ApiResponse(code = 401, message = "credentials not authorized")
    })
    @RequestMapping(path="/token", method=RequestMethod.POST)
    public ResponseEntity<Token> secureSignIn(@RequestBody UserCredentials credentials) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Token token = null;
        try {
            token = tokenGeneratorService.generateSecureToken(credentials);
            status = HttpStatus.CREATED;
        } catch (InvalidCredentialsException e) {
            status = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Token>(token, status);
    }

    @ApiOperation(value = "Verifies the validity of a JWT token.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "token validated"),
        @ApiResponse(code = 400, message = "request could not be processed"),
        @ApiResponse(code = 401, message = "token not authorized")
    })
    @RequestMapping(path="/verify", method=RequestMethod.POST)
    public ResponseEntity<TokenDetails> verify(@RequestBody Token token) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        TokenDetails details = null;
        try {
            details = tokenVerificationService.verifyTokenDetails(token);
            status = HttpStatus.OK;
        } catch (InvalidTokenException e) {
            status = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<TokenDetails>(details, status);
    }

    @ApiOperation(value = "Verifies both API and storage api are alive.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful check"),
        @ApiResponse(code = 400, message = "request could not be processed")
    })
    @RequestMapping(path="/liveness", method=RequestMethod.POST)
    public ResponseEntity<Boolean> liveness() {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        boolean alive = false;
        try {
            alive = credentialRepository.repositoryIsLive();
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Boolean>(alive, status);
    }
}
