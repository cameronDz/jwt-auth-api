package org.md.api.auth.model;

/**
 * pojo expected payload from user creating an new username/password
 */
public class UserCreationToken extends UserCredentials {

    private String creationToken;
    
    public UserCreationToken() {
        super();
        this.creationToken = "";
    }

    public UserCreationToken(String username, String password, String creationToken) {
        super(username, password);
        this.creationToken = creationToken;
    }

    public String getCreationToken() {
        return creationToken;
    }

    public void setCreationToken(String creationToken) {
        this.creationToken = creationToken;
    }

    @Override
    public String toString() {
        return "UserCreationToken [creationToken=" + creationToken + ", " + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((creationToken == null) ? 0 : creationToken.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserCreationToken other = (UserCreationToken) obj;
        if (creationToken == null) {
            if (other.creationToken != null)
                return false;
        } else if (!creationToken.equals(other.creationToken))
            return false;
        return true;
    }
}
