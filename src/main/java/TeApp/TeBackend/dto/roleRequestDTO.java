package TeApp.TeBackend.dto;

import TeApp.TeBackend.entity.Roles;
import lombok.Data;

@Data
public class roleRequestDTO {
    private Long id;
    private Roles requestedRole;
}
