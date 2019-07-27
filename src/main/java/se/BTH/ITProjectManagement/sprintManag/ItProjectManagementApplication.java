package se.BTH.ITProjectManagement.sprintManag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableAutoConfiguration
@EnableDiscoveryClient
//@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})//add to config docker
public class ItProjectManagementApplication  {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "sprintmanag-server");
		SpringApplication.run(ItProjectManagementApplication.class, args);
	}

}
