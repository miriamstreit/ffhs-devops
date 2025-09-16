package ch.schuum.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SchuumApplication ist der Startpunkt der Web-Applikation
 *
 * @author Miriam Streit
 *
 */
@SpringBootApplication
@EnableScheduling
public class SchuumApplication {

	/**
	 * <p>Eintrittspunkt der Applikation</p>
	 * @param args Kommandozeilenargumente
	 * @author Miriam Streit
	 *
	 */
	public static void main(String[] args) {
		SpringApplication.run(SchuumApplication.class, args);
	}

}
