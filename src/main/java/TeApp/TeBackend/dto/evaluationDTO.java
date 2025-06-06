package TeApp.TeBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class evaluationDTO {

    private Long observerId;
    private Long instructorId;
    private Long classId;
    private LocalDate date;
    private List<recDTO> recommendations;
}
