package org.ged;

import org.ged.entities.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
public class UploadeFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadeFileApplication.class, args);
	}

}
