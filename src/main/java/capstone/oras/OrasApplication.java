package capstone.oras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
@EnableSwagger2
public class OrasApplication {
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(OrasApplication.class);
		String port = System.getenv("PORT");
		if (port != null){
			port = "8080";
		}
		app.setDefaultProperties(Collections.singletonMap("server.port",port));
		app.run();
//		SpringApplication.run(OrasApplication.class, args);
	}

//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer() {
//		return (container -> {
//			container.setContextPath("/nemswiftsvc");
//			container.setPort(Integer.valueOf(System.getenv("PORT")));
//		});
//	}

}