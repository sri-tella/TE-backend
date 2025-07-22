package TeApp.TeBackend.dto;

import lombok.Data;

@Data
public class changePasswordDTO {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
