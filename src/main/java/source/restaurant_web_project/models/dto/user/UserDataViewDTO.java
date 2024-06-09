package source.restaurant_web_project.models.dto.user;

import java.util.List;

public class UserDataViewDTO {
    private String email;
    private List<RoleDTO> roles;

    public UserDataViewDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
}
