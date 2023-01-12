package academy.devdojo.springwebfluxessentials.service;

import academy.devdojo.springwebfluxessentials.domain.Show;
import academy.devdojo.springwebfluxessentials.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowService {

    private final ShowRepository repository;

    public Flux<Show> findAll() {
        return repository.findAll();
    }

    public Mono<Show> findByName(final String name){
        return repository.findByName(name);
    }

    public Mono<Show> create(Show show) {
        return repository.save(show);
    }

    public Mono<Void> update(Show show, Integer id) {
        return repository.findById(id)
                .map(found -> show.withId(found.getId()))
                .flatMap(repository::save)
                .then();
    }

    public Mono<Void> delete(Integer id) {
        return repository.findById(id)
                .flatMap(el -> repository.deleteById(id));
    }
}
