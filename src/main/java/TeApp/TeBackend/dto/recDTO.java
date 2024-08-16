package TeApp.TeBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class recDTO {

    private String description;
    private boolean selected;
    private String feedback; // Feedback for each recommendation
}
