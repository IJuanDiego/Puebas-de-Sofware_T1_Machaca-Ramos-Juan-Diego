package edu.pe.cibertec.infracciones;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Infractor;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.service.impl.InfractorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InfractorTest {

    @Mock
    private InfractorRepository repository;

    @InjectMocks
    private InfractorServiceImpl service;

    @Test
    @DisplayName("Pregunta 1: Verificar bloqueo")
    void verificarBloqueo_infractorConDosVencidasYTresPagadas() {

        Infractor infractor = new Infractor();
        infractor.setId(1L);
        infractor.setBloqueado(false);

        when(repository.findById(1L)).thenReturn(Optional.of(infractor));
        when(repository.multasPorInfractorYEstado(1L, EstadoMulta.VENCIDA)).thenReturn(2L);

        service.verificarBloqueo(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository,times(1)).multasPorInfractorYEstado(1L, EstadoMulta.VENCIDA);
        verify(repository, never()).save(any());
    }

}

