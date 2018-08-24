package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCredentials {
    @JsonProperty("userRoles")
    List<UserRole> userRoles;
    @JsonProperty("username")
    String username;

    public boolean hasRole(String role) {
        for (UserRole r : this.userRoles) {
            if (r.getDisplayName().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRoleId(String roleId) {
        if (roleId != null) {
            for (UserRole r : this.userRoles) {
                if (roleId.equals(r.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getUsername() {
        return this.username;
    }

    public List<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}
