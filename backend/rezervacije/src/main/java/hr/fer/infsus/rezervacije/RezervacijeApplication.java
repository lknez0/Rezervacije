package hr.fer.infsus.rezervacije;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
@EnableJpaRepositories(basePackages = "hr.fer.infsus.rezervacije.repository")
@EntityScan(basePackages = "hr.fer.infsus.rezervacije.models")
public class RezervacijeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RezervacijeApplication.class, args);
	}

}
