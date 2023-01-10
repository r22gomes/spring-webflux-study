package academy.devdojo.springwebfluxessentials.controller;


import academy.devdojo.springwebfluxessentials.domain.Show;
import academy.devdojo.springwebfluxessentials.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("shows")
@Slf4j
@RequiredArgsConstructor
public class ShowController {

    private final ShowRepository showRepository;

    @GetMapping
    public Flux<Show> findAll(){
        return showRepository.findAll();
    }


}
