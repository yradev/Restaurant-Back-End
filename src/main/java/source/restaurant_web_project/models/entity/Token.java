package source.restaurant_web_project.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class Token extends BaseEntity {
    private String token;
    private String email;

    private LocalDateTime expiryDate;

    public Token() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
