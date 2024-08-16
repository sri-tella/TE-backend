package TeApp.TeBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class TeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeBackendApplication.class, args);
	}

}
