package mst.local.mstsoftware.modules.users.dtos;

public class UserDTO {
    private final Long id;
    private final String email;

    public UserDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId(Long id) {
        return id;
    }

    public String getEmail() {
        return email;
    }

}
