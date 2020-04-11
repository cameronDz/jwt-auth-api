package org.md.api.auth.model;

import java.util.Date;

/**
 * pojo returned to user when user is created
 */
public class UserCreationDetails {

    private String username;
    private String message;
    private Date creationDate;
    
    /**
     * empty constructor
     */
    public UserCreationDetails() {
        super();
        this.username = "";
        this.message = "";
        this.creationDate = null;
    }
            
    /**
     * loaded constructor
     * @param username
     * @param creationDate
     */
    public UserCreationDetails(String username, String message, Date creationDate) {
        super();
        this.username = username;
        this.message = message;
        this.creationDate = creationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "UserCreationDetails [username=" + username + ", message=" + message + ", creationDate=" + creationDate
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
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
        UserCreationDetails other = (UserCreationDetails) obj;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
}
