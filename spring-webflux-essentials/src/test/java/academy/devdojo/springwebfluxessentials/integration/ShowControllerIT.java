package academy.devdojo.springwebfluxessentials.integration;

import academy.devdojo.springwebfluxessentials.domain.Show;
import academy.devdojo.springwebfluxessentials.repository.ShowRepository;
import academy.devdojo.springwebfluxessentials.service.ShowService;
import academy.devdojo.springwebfluxessentials.service.util.ShowCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(ShowService.class)
public class ShowControllerIT {

    private final Show show = ShowCreator.validShow();


    @MockBean // REAL LIFE SCENARIO WOULD DEFENETLY USE A EMBEDDED DB INSTEAD OF MOCK
    private ShowRepository showRepository;
    @Autowired
    private WebTestClient testClient;

    @BeforeAll
    public static void setupBlockhound(){
        BlockHound.install();
    }

    @BeforeEach
    public void mockitoSetup(){
        Mockito.when(showRepository.findAll())
                .thenReturn(Flux.just(show));
        Mockito.when(showRepository.findByName("testShow"))
                .thenReturn(Mono.just(show));
        Mockito.when(showRepository.findByName("testShow2"))
                .thenReturn(Mono.empty());
        Mockito.when(showRepository.findById(1))
                .thenReturn(Mono.just(show));
        Mockito.when(showRepository.findById(2))
                .thenReturn(Mono.empty());
        Mockito.when(showRepository.save(ShowCreator.toBeSaved()))
                .thenReturn(Mono.just(show));
        Mockito.when(showRepository.delete(ArgumentMatchers.any(Show.class)))
                .thenReturn(Mono.empty());
        Mockito.when(showRepository.save(ShowCreator.toUpdateShow()))
                .thenReturn(Mono.empty());
    }

    @Test
    public void blockhoundTest(){
        try{
            Mono.delay(Duration.ofSeconds(1))
                    .doOnNext(it -> {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .block();
        }catch (Exception e){
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }


    @Test
    @DisplayName("ListAll returns a flux of Shows")
    public void listAllReturnFluxOfŜhowWhenSuccessfull(){
        testClient
                .get()
                .uri("/shows")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(show.getId())
                .jsonPath("$.[0].name").isEqualTo(show.getName());

    }

    @Test
    @DisplayName("find by name returns a Mono of Shows")
    public void findByValidNameReturnFluxOfŜhowWhenSuccessfull(){
        testClient
                .get()
                .uri("/shows/testShow")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo(show.getId())
                .jsonPath("$.name").isEqualTo(show.getName());
    }

    @Test
    @DisplayName("create returns a Mono of Shows")
    public void createReturnFluxOfŜhowWhenSuccessfull(){
        testClient
                .post()
                .uri("/shows")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ShowCreator.toBeSaved()))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(show.getId())
                .jsonPath("$.name").isEqualTo(show.getName());
    }

    @Test
    @DisplayName("create returns error when name is empty")
    public void createReturnFErrorWhenNameIsEmpty(){
        testClient
                .post()
                .uri("/shows")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Show()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Validation failure");
    }

    @Test
    @DisplayName("find by name returns Error When it does not exist")
    public void findByValidNameReturnErrorOfŜhowWhenItDoesNotExist(){
        testClient
                .get()
                .uri("/shows/testShow2")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName("update returns no content of Shows")
    public void updateReturnNoContentWhenSuccessfull(){
        testClient
                .put()
                .uri("/shows/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ShowCreator.toUpdateShow()))
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @DisplayName("create returns error when name is empty")
    public void updateReturnErrorWhenNotFound(){
        testClient
                .put()
                .uri("/shows/2")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Show(2, "oi")))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("show not found");
    }

    @Test
    @DisplayName("delete returns no content of Shows")
    public void deleteReturnNoContentWhenSuccessfull(){
        testClient
                .delete()
                .uri("/shows/1")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @DisplayName("delete returns error when name is empty")
    public void deleteReturnErrorWhenNotFound(){
        testClient
                .delete()
                .uri("/shows/2")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("show not found");
    }


}