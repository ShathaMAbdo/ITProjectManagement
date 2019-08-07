package se.BTH.services.sprintManag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableEurekaClient

//@EnableDiscoveryClient
//@EnableSwagger2
//@EnableAutoConfiguration
//@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class SprintManagApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "sprintManag-server");
		SpringApplication.run(SprintManagApplication.class, args);
	}
	/*@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("se.BTH.services.sprintManag.controllers"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("SprintManag API").description("Documentation SprintManag API v1.0").build());
	}*/

}
