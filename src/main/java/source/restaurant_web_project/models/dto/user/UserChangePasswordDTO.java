package source.restaurant_web_project.models.dto.user;

import jakarta.validation.constraints.Size;

public class UserChangePasswordDTO {
    private String currentPassword;
    @Size(min = 6)
    private String newPassword;

    public UserChangePasswordDTO(){}

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
