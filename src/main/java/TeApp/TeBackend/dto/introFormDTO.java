package TeApp.TeBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class introFormDTO {
    private String observerFirstName;
    private String observerLastName;
    private String observerEmail;
    private String instructorFirstName;
    private String instructorLastName;
    private String courseTitle;
    private String courseDescription;
}
