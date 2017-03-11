package ch.hemisoft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import ch.hemisoft.immo.calc.backend.repository.UserRepository;
import ch.hemisoft.immo.domain.User;

@SpringBootApplication
public class ImmoCalculatorAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImmoCalculatorAppApplication.class, args);
	}
	
	@Component
	class ApplicationStartupRunnerOne implements CommandLineRunner {
		@Autowired UserRepository userRepository;
		
	    @Override
	    public void run(String... args) throws Exception {
	    	if(null == userRepository.findByUserName("user")) {
	    		String userName = "user";
	    		String password = "password";
				String email = "user@hemisoft.net";
				Boolean enabled = true;
				userRepository.save(new User(userName, password, email, enabled));
	    	}
	    }
	}
}
