package academy.devdojo.springwebfluxessentials.repository;

import academy.devdojo.springwebfluxessentials.domain.Show;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ShowRepository extends ReactiveCrudRepository<Show, Integer> {


}
