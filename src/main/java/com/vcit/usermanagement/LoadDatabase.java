package com.vcit.usermanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
	
	public static final Logger log =LoggerFactory.getLogger(LoadDatabase.class);
			
	
	@Bean
	public CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
		return args ->{
			log.info("Preloading "+ employeeRepository.save(new Employee("Ramu Chandran", "Manager")));
			log.info("Preloading "+ employeeRepository.save(new Employee("Prerana Ramu", "Assistant Manager")));
		};
		
	}

}
