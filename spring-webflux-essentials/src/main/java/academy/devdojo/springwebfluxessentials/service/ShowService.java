package academy.devdojo.springwebfluxessentials.service;

import academy.devdojo.springwebfluxessentials.domain.Show;
import academy.devdojo.springwebfluxessentials.repository.ShowRepository;
import io.netty.util.internal.StringUtil;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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

    public Mono<Show> findByName(final String name) {
        return repository.findByName(name)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "show not found")
                ));
    }

    public Mono<Show> findById(final int id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "show not found")
                ));
    }

    public Mono<Show> create(Show show) {
        return repository.save(show);
    }

    public Mono<Void> update(Show show, Integer id) {
        return this.findById(id)
                .map(found -> show.withId(found.getId()))
                .flatMap(repository::save)
                .then();
    }

    public Mono<Void> delete(Integer id) {
        return this.findById(id)
                .flatMap(repository::delete);
    }

    @Transactional // just example to easilly see how this annotation works
    public Flux<Show> saveAll(List<Show> show) {
        return repository.saveAll(show)
                .doOnNext(this::throwResponseStatusExceptionWhenEmptyName);
    }


    private void throwResponseStatusExceptionWhenEmptyName(Show s) {
        if (StringUtil.isNullOrEmpty(s.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Name was spotted");
        }

    }
}
