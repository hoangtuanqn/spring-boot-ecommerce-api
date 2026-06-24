package mst.local.mstsoftware.modules.users.dtos;

public class LoginRequest {
    private String email;
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
