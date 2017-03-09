package ch.hemisoft.immo.aspect.logging;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LoggingConfig {

	@Bean
	public BackendServiceLogger backendServiceLogger() {
		return new BackendServiceLogger();
	}

	@Bean
	public BusinessServiceLogger businessServiceLogger() {
		return new BusinessServiceLogger();
	}

	@Bean
	public ControllerLogger controllerLogger() {
		return new ControllerLogger();
	}

	@Bean
	public RepositoryLogger repositoryLogger() {
		return new RepositoryLogger();
	}
}
