package academy.devdojo.springwebfluxessentials.integration;

import academy.devdojo.springwebfluxessentials.domain.Show;
import academy.devdojo.springwebfluxessentials.domain.User;
import academy.devdojo.springwebfluxessentials.repository.ShowRepository;
import academy.devdojo.springwebfluxessentials.repository.UserRepository;
import academy.devdojo.springwebfluxessentials.service.util.ShowCreator;
import academy.devdojo.springwebfluxessentials.service.util.WebTestClientUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ShowControllerIT {

    private final static  String ADMIN = "admin";
    private final static  String USER = "user";

    private final Show show = ShowCreator.validShow();

    /**
     * REAL LIFE SCENARIO SHOULD USE EMBEDDED DB INSTEAD OF MOCK
     * IN THIS CASE IT IS NOT REALLY A INTEGRATION TEST
     */
    @MockBean
    private ShowRepository showRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WebTestClient testClient;

    @BeforeEach
    public void mockitoSetup() {

        Mockito.when(userRepository.findByUsername("admin"))
                .thenReturn(Mono.just(new User(1, "admin", "admin", "{bcrypt}$2a$10$4jxiuTEwJ2gs24qfekqUquaM7eQXj9SDqJZADDZKhDlVlXXRUqpxm", "ROLE_ADMIN,ROLE_USER")));
        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Mono.just(new User(2, "user", "user", "{bcrypt}$2a$10$4jxiuTEwJ2gs24qfekqUquaM7eQXj9SDqJZADDZKhDlVlXXRUqpxm", "ROLE_USER")));
        Mockito.when(userRepository.findByUsername("other"))
                .thenReturn(Mono.empty());
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
        Mockito.when(showRepository.saveAll(List.of(show, show, show)))
                .thenReturn((Flux.just(show, show, show)));
        Mockito.when(showRepository.saveAll(List.of(show, show, new Show().setName(""))))
                .thenReturn(Flux.just(show, show, new Show().setName("")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER", username = "user")
    @DisplayName("ListAll returns a flux of Shows")
    public void listAllReturnFluxOfŜhowWhenSuccessfull() {
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
    @WithMockUser(authorities = "ROLE_USER", username = "user")
    @DisplayName("find by name returns a Mono of Shows")
    public void findByValidNameReturnFluxOfŜhowWhenSuccessfull() {
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
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("create returns a Mono of Shows")
    public void createReturnFluxOfŜhowWhenSuccessfull() {
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
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("create returns error when name is empty")
    public void createReturnFErrorWhenNameIsEmpty() {
        testClient
                .post()
                .uri("/shows")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Show()))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER", username = "user")
    @DisplayName("find by name returns Error When it does not exist")
    public void findByValidNameReturnErrorOfŜhowWhenItDoesNotExist() {
        testClient
                .get()
                .uri("/shows/testShow2")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("update returns no content of Shows")
    public void updateReturnNoContentWhenSuccessfull() {
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
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("create returns error when name is empty")
    public void updateReturnErrorWhenNotFound() {
        testClient
                .put()
                .uri("/shows/2")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Show(2, "oi")))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("404 NOT_FOUND \"show not found\"");
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("create batch returns success when one or more names are valid")
    public void batchCreateIsSuccessfullWhenNamesAreValid() {
        testClient
                .post()
                .uri("/shows/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(List.of(show, show, new Show().setName(""))))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("400 BAD_REQUEST \"Invalid Name was spotted\"");
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("create returns is successfull when batch is valid")
    public void batchCreateReturnsErrorWhenNameInvalidExists() {
        testClient
                .post()
                .uri("/shows/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Arrays.asList(show, show, show)))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Show.class)
                .hasSize(3);
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("delete returns no content of Shows")
    public void deleteReturnNoContentWhenSuccessfull() {
        testClient
                .delete()
                .uri("/shows/1")
                .exchange()
                .expectStatus()
                .isNoContent();
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("delete returns error when name is empty")
    public void deleteReturnErrorWhenNotFound() {
        testClient
                .delete()
                .uri("/shows/2")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("404 NOT_FOUND \"show not found\"");
    }


}
