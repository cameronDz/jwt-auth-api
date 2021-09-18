package org.md.api.auth.controller;

import org.md.api.auth.model.Token;
import org.md.api.auth.model.TokenDetails;
import org.md.api.auth.model.UserCreationDetails;
import org.md.api.auth.model.UserCreationToken;
import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;
import org.md.api.auth.model.exception.InvalidTokenException;
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
    private ITokenGeneratorService tokenGeneratorService;
    
    @Autowired
    private ITokenVerificationService tokenVerificationService;
    
    @Autowired
    private IUserCreatorService userCreatorService;

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
            status = HttpStatus.OK;
        } catch (InvalidTokenException e) {
            status = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<TokenDetails>(details, status);
    }

    @ApiOperation(value = "Create a new user credentials based off request body.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "token validated") })
    @RequestMapping(path="/create", method=RequestMethod.PUT)
    public ResponseEntity<UserCreationDetails> createUser(@RequestBody UserCreationToken userCreationToken) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        UserCreationDetails details = new UserCreationDetails();
        try {
            details = userCreatorService.createUserCredentials(userCreationToken);
        } catch (InvalidCredentialsException e) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<UserCreationDetails>(details, status);
    }

    @ApiOperation(value = "Create a new user credentials based off request body.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "token validated") })
    @RequestMapping(path="/v2/create", method=RequestMethod.PUT)
    public ResponseEntity<UserCreationDetails> secureCreateUser(@RequestBody UserCreationToken userCreationToken) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        UserCreationDetails details = new UserCreationDetails();
        try {
            details = userCreatorService.createSecureUserCredentials(userCreationToken);
        } catch (InvalidCredentialsException e) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<UserCreationDetails>(details, status);
    }

    @ApiOperation(value = "Request a JWT token for a user.")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "token was successfully created") })
    @RequestMapping(path="/v2/signIn", method=RequestMethod.POST)
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
}
