package TeApp.TeBackend.dto;

import TeApp.TeBackend.entity.Section;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class recommendationDTO {
    private Long recId;
    private Long sectionId;
    private String description;
    private boolean selected;
}
