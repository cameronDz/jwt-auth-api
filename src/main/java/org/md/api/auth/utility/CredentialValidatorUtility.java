package org.md.api.auth.utility;

import java.util.List;

import org.md.api.auth.model.UserCredentials;
import org.md.api.auth.model.exception.InvalidCredentialsException;

public class CredentialValidatorUtility {

    /**
     * cycle through list of username to check for a valid user
     * @param user container cred name and password
     * @param userList list of users
     * @return true if matching username and password can be found in list, false otherwise
     */
    public static boolean usernameAndPasswordAreValid(UserCredentials user, List<UserCredentials> userList) throws InvalidCredentialsException {
        checkUserNameAreNullOrEmpty(user);
        boolean isUserValidated = false;
        String username = user.getUsername();
        String password = user.getPassword();
        int length = userList != null ? userList.size() : 0;
        UserCredentials validUser = null;
        for (int i = 0; i < length; i++) {
            validUser = userList.get(i);
            if (!credentialsAreNullOrEmptyValues(validUser) && username.equals(validUser.getUsername()) && password.equals(validUser.getPassword())) {
                isUserValidated = true;
                break;
            }
        }
        return isUserValidated;
    }
    
    /**
     * check if a username is null or empty, threw exception if they are
     * @param user credentials being checked
     * @throws InvalidCredentialsException
     */
    public static void checkUserNameAreNullOrEmpty(UserCredentials user) throws InvalidCredentialsException {
        if (credentialsAreNullOrEmptyValues(user)) {
            throw new InvalidCredentialsException();
        }
    }
    
    public static boolean credentialsAreNullOrEmptyValues(UserCredentials user) {
        return user == null || GeneralUtility.stringIsNullOrEmpty(user.getUsername()) || GeneralUtility.stringIsNullOrEmpty(user.getPassword());
    }
}
