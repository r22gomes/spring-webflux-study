package academy.devdojo.springwebfluxessentials;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootApplication
public class SpringWebfluxEssentialsApplication {

    //	static {
//		BlockHound.install();
//	}
    public static void main(String[] args) {
        SpringApplication.run(SpringWebfluxEssentialsApplication.class, args);
    }

}
