package academy.devdojo.springwebfluxessentials.repository;

import academy.devdojo.springwebfluxessentials.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    Mono<User> findByUsername(final String username);

}
