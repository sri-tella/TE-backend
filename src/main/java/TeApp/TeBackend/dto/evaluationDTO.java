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

//    private Long observerId; // Assuming you will send this from the frontend
//    private Long instructorId; // Assuming you will send this from the frontend
//    private Long classId; // Assuming you will send this from the frontend
    private LocalDate date; // Can be sent from the frontend
    private List<recDTO> recommendations; // Assuming you will send this
}
