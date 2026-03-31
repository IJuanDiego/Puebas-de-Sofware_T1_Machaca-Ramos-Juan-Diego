package edu.pe.cibertec.infracciones;

import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.service.impl.InfractorServiceImpl;
import edu.pe.cibertec.infracciones.service.impl.MultaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultaTest {

    @Mock
    private MultaRepository repository;

    @InjectMocks
    private MultaServiceImpl service;

    @Test
    @DisplayName("Multa pendiente a vencida segun fecha de vencimiento")
    void actualizarEstados_multaPendienteAVencida() {

        Multa multa = new Multa();
        multa.setEstado(EstadoMulta.PENDIENTE);
        multa.setFechaVencimiento(LocalDate.of(2026, 1, 1));

        when(repository.findByEstado(EstadoMulta.PENDIENTE))
                .thenReturn(List.of(multa));

        service.actualizarEstados();
        assertEquals(EstadoMulta.VENCIDA, multa.getEstado());

        verify(repository, times(1)).findByEstado(EstadoMulta.PENDIENTE);
        verify(repository, times(1)).save(multa);
    }
}
