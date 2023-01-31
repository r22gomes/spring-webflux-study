package academy.devdojo.springwebfluxessentials.service;

import academy.devdojo.springwebfluxessentials.domain.Show;
import academy.devdojo.springwebfluxessentials.repository.ShowRepository;
import academy.devdojo.springwebfluxessentials.service.util.ShowCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(SpringExtension.class)
public class ShowServiceTest {

    @InjectMocks
    private ShowService instance;

    @Mock
    private ShowRepository showRepository;

    private final Show show = ShowCreator.validShow();

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
        Mockito.when(showRepository.findById(1))
                .thenReturn(Mono.just(show));
        Mockito.when(showRepository.save(ShowCreator.toBeSaved()))
                .thenReturn(Mono.just(show));
        Mockito.when(showRepository.delete(ArgumentMatchers.any(Show.class)))
                .thenReturn(Mono.empty());
        Mockito.when(showRepository.save(ShowCreator.toUpdateShow()))
                .thenReturn(Mono.empty());
        Mockito.when(showRepository.saveAll(List.of(show, show, show)))
                .thenReturn(Flux.just(show, show, show));
        Mockito.when(showRepository.saveAll(List.of(show, show, new Show().setName(""))))
                .thenReturn(Flux.just(show, show, new Show().setName("")));
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
        StepVerifier.create(instance.findAll())
                .expectSubscription()
                .expectNext(show)
                .verifyComplete();
    }

    @Test
    @DisplayName("find by name returns a Mono of Show")
    public void findByIdReturnsMonoOfShowWhenSuccessfull(){
        StepVerifier.create(instance.findByName("testShow"))
                .expectSubscription()
                .expectNext(show)
                .verifyComplete();
    }

    @Test
    @DisplayName("find by name returns Mono Empty When Show does not exist")
    public void findByIdReturnsMonoErrorWhenDoesNotExist(){
        Mockito.when(showRepository.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(instance.findByName("empty"))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("save creates an show when successfull")
    public void saveCreatesShowWhenSuccessfull(){
        Show toBeSaved = ShowCreator.toBeSaved();
        StepVerifier.create(instance.create(toBeSaved))
                .expectSubscription()
                .expectNext(show)
                .verifyComplete();
    }

    @Test
    @DisplayName("save batch when all shoes are valid")
    public void saveBatchShowWhenValid(){
        List<Show> show1 = List.of(show, show, show);
        StepVerifier.create(instance.saveAll(show1))
                .expectSubscription()
                .expectNext(show)
                .expectNext(show)
                .expectNext(show)
                .verifyComplete();
    }

    @Test
    @DisplayName("Fail batch when one or more shows are not valid")
    public void failBatchShowWhenInvalid(){
        List<Show> show1 = List.of(show, show, new Show().setName(""));
        StepVerifier.create(instance.saveAll(show1))
                .expectSubscription()
                .expectNext(show)
                .expectNext(show)
                .expectError(RuntimeException.class)
                .verify();
    }


    @Test
    @DisplayName("delete removes an show when successfull")
    public void deleteRemovesShowWhenSuccessfull(){
        StepVerifier.create(instance.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete does not return Error an show when entity does not exist")
    public void deleteDoesNotReturnErrorOnUnexisting(){
        Mockito.when(showRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(instance.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }


    @Test
    @DisplayName("update saves an updated show when successfull")
    public void updateSavesUpdatedShowWhenSuccessfull(){
        StepVerifier.create(instance.update(ShowCreator.toUpdateShow(),1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update does not return Error an show when entity does not exist")
    public void updateDoesNotReturnErrorOnUnexisting(){
        Mockito.when(showRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(instance.update(ShowCreator.toUpdateShow(), 1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }



}
