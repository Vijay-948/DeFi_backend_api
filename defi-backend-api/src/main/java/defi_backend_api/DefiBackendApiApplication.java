package defi_backend_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class DefiBackendApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DefiBackendApiApplication.class, args);
	}

}
