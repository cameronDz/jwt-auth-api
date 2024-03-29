package org.md.api.auth.repository;

import org.md.api.auth.model.exception.InvalidCredentialsException;

public interface ICredentialRepository {
    public void secureCredentialsAreValid(String username, String password) throws InvalidCredentialsException;

    public boolean repositoryIsLive() throws Exception;
}
