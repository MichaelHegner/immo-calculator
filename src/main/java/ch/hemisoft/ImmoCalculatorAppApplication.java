package ch.hemisoft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import ch.hemisoft.immo.calc.backend.repository.ForecastConfigurationRepository;
import ch.hemisoft.immo.calc.backend.repository.UserRepository;
import ch.hemisoft.immo.domain.ForecastConfiguration;
import ch.hemisoft.immo.domain.User;

@SpringBootApplication
public class ImmoCalculatorAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImmoCalculatorAppApplication.class, args);
	}
	
	@Component
	class ApplicationStartupRunnerOne implements CommandLineRunner {
		@Autowired UserRepository userRepository;
		@Autowired ForecastConfigurationRepository forcastConfigurationRepository;
		
	    @Override
	    public void run(String... args) throws Exception {
	    	if(null == userRepository.findByUserName("user")) {
	    		String userName = "user";
	    		String password = "password";
				String email = "user@hemisoft.net";
				Boolean enabled = true;
				userRepository.save(new User(userName, password, email, enabled));
	    	}
	    	
	    	if(CollectionUtils.isEmpty(forcastConfigurationRepository.findAll())) {
	    		forcastConfigurationRepository.save(createAustrianConfig());
	    		forcastConfigurationRepository.save(createGermenConfig());
	    	}
	    }

	    // TODO: Make configurable
		private ForecastConfiguration createAustrianConfig() {
			ForecastConfiguration deConfig = new ForecastConfiguration();
			deConfig.setCountryCode("AT");
			deConfig.setDeprecation(2.0);
			deConfig.setRentalIncrease(3.0);
			deConfig.setRentalIncreaseFrequence(2);
			deConfig.setRunningCostIndex(1.5);
			deConfig.setTaxQuote(50.0);
			return deConfig;
		}

        // TODO: Make configurable
		private ForecastConfiguration createGermenConfig() {
			ForecastConfiguration deConfig = new ForecastConfiguration();
			deConfig.setCountryCode("DE");
			deConfig.setDeprecation(2.0);
			deConfig.setRentalIncrease(2.0);
			deConfig.setRentalIncreaseFrequence(3);
			deConfig.setRunningCostIndex(1.5);
			deConfig.setTaxQuote(43.0);
			return deConfig;
		}
	}
}
