package academy.devdojo.springwebfluxessentials.controller;


import academy.devdojo.springwebfluxessentials.domain.Show;
import academy.devdojo.springwebfluxessentials.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("shows")
@Slf4j
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @GetMapping
    public Flux<Show> findAll(){
        return showService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Show> create(@Valid @RequestBody Show show){
        return showService.create(show)
                .log();
    }


    @PostMapping(value = "batch", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Show>> createAll(@RequestBody List<Show> show){ // not best approach but good to see transactional in action
            return showService.saveAll(show)
                    .collectList()
                    .log();
    }
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> put(@PathVariable Integer id, @Valid @RequestBody Show show){
        return showService.update(show, id)
                .log();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Integer id){
        return showService.delete(id)
                .log();
    }

    @GetMapping(path = "{name}")
    public Mono<ResponseEntity<Show>> findByName(@PathVariable String name){
        return showService.findByName(name)
                .map(s -> ResponseEntity.status(200).body(s))
                .switchIfEmpty(Mono.error(new NoSuchElementException("Could not find element with name " + name)))
                .log();
    }




}
