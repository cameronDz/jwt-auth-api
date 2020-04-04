package org.md.api.auth.model;

import java.util.Date;

public class TokenDetails {

    private String username;
    private Date expiration;
    private int authorizationLevel;
    
    public TokenDetails() {
        super();
    }
    
    public TokenDetails(String username, Date expiration, int authorizationLevel) {
        super();
        this.username = username;
        this.expiration = expiration;
        this.authorizationLevel = authorizationLevel;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Date getExpiration() {
        return expiration;
    }
    
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
    
    public int getAuthorizationLevel() {
        return authorizationLevel;
    }
    
    public void setAuthorizationLevel(int authorizationLevel) {
        this.authorizationLevel = authorizationLevel;
    }

    @Override
    public String toString() {
        return "TokenDetails [username=" + username + ", expiration=" + expiration + ", authorizationLevel="
                + authorizationLevel + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + authorizationLevel;
        result = prime * result + ((expiration == null) ? 0 : expiration.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TokenDetails other = (TokenDetails) obj;
        if (authorizationLevel != other.authorizationLevel)
            return false;
        if (expiration == null) {
            if (other.expiration != null)
                return false;
        } else if (!expiration.equals(other.expiration))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
