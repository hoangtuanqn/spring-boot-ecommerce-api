package mst.local.mstsoftware.modules.users.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Email is required!")
    @Email(message = "Email invalid!")
    private String email;

    @Size(min = 6, message = "Minimum of 6 character!")
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void getPassword(String password) {
        this.password = password;
    }

}
