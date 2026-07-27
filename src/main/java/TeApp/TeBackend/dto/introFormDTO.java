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
    private String instructorFirstName;
    private String instructorLastName;
    private String instructorEmail;
    private Long observerId;

    private String courseTitle;
    private String courseDescription;
    private String topic;
    private String date;
    private String time;

    private String goal;
    private String outline;
    private String help;
}
