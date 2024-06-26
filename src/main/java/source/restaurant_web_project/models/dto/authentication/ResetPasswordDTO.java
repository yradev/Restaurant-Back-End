package source.restaurant_web_project.models.dto.authentication;


import jakarta.validation.constraints.Size;

public class ResetPasswordDTO {
    @Size(min = 5, message = "Password must be at least 5 symbols!")
    private String password;

    private String token;

    public ResetPasswordDTO(){};

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
