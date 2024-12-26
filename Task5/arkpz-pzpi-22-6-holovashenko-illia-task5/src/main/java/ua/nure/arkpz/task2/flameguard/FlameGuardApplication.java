package ua.nure.arkpz.task2.flameguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlameGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlameGuardApplication.class, args);
	}

}
