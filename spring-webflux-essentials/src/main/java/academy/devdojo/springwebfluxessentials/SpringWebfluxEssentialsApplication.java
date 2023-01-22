package academy.devdojo.springwebfluxessentials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
