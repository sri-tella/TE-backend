package TeApp.TeBackend.dto;

import TeApp.TeBackend.entity.Roles;
import lombok.Data;

@Data
public class roleRequestViewDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Roles requestedRole;
}
