package academy.devdojo.springwebfluxessentials.repository;

import academy.devdojo.springwebfluxessentials.domain.Show;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ShowRepository extends ReactiveCrudRepository<Show, Integer> {


    Mono<Show> findByName(final String name);


}
