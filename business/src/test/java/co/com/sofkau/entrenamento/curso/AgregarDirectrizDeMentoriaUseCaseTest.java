package co.com.sofkau.entrenamento.curso;


import co.com.sofka.business.generic.UseCaseHandler;
import co.com.sofka.business.repository.DomainEventRepository;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkau.entrenamiento.curso.commands.AgregarDirectrizDeMentoria;
import co.com.sofkau.entrenamiento.curso.events.CursoCreado;
import co.com.sofkau.entrenamiento.curso.events.DirectrizAgregadaAMentoria;
import co.com.sofkau.entrenamiento.curso.events.MentoriaCreada;
import co.com.sofkau.entrenamiento.curso.values.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgregarDirectrizDeMentoriaUseCaseTest {

    @InjectMocks
    private AgregarDirectrizDeMentoriaUseCase useCase;

    @Mock
    private DomainEventRepository repository;

    @BeforeEach
    void setUp() {
        useCase = new AgregarDirectrizDeMentoriaUseCase();
    }

    @Test
    void DirectrizSadPassIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Directiz directiz = new Directiz("");
        });
    }

    @Test
    void DirectrizSadPassIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Directiz directiz = new Directiz(null);
        });
    }

    @Test
    void AgregarDirectrizDeMentoriaHappyPass() {
        //arrange
        CursoId cursoId = CursoId.of("ddddd");
        MentoriaId mentoriaId = MentoriaId.of("yyyyy");
        Directiz directiz = new Directiz("Nueva directriz");
        var command = new AgregarDirectrizDeMentoria(cursoId, mentoriaId, directiz);
        when(repository.getEventsBy("ddddd")).thenReturn(history());
        useCase.addRepository(repository);
        //act
        var events = UseCaseHandler.getInstance()
                .syncExecutor(useCase, new RequestCommand<>(command))
                .orElseThrow()
                .getDomainEvents();
        //assert
        var directrizAgregadaAMentoria = (DirectrizAgregadaAMentoria) events.get(0);
        Assertions.assertEquals("yyyyy", directrizAgregadaAMentoria.getMentoriaId().value());
    }

    private List<DomainEvent> history() {
        Nombre nombreCurso = new Nombre("DDD");
        Descripcion descripcion = new Descripcion("Curso complementario para el training");
        var event1 = new CursoCreado(
                nombreCurso,
                descripcion
        );
        MentoriaId mentoriaId = MentoriaId.of("yyyyy");
        Nombre nombreMentoria = new Nombre("Mentoría lunes");
        Fecha fecha = new Fecha(LocalDateTime.now(), LocalDate.now());
        var event2 = new MentoriaCreada(
                mentoriaId,
                nombreMentoria,
                fecha
        );
        event1.setAggregateRootId("xxxxx");
        return List.of(event1, event2);
    }
}