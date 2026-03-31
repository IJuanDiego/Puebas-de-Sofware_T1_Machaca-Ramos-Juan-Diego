package edu.pe.cibertec.infracciones;

import edu.pe.cibertec.infracciones.dto.PagoResponseDTO;
import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.model.Pago;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.PagoRepository;
import edu.pe.cibertec.infracciones.service.impl.PagoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private MultaRepository multaRepository;

    @InjectMocks
    private PagoServiceImpl service;

    @Test
    @DisplayName("Pregunta3: Aplicar descuento del 20% y cambiar estado")
    void procesarPago_aplicaDescuentoYMarcaPagada() {

        Multa multa = new Multa();
        multa.setId(1L);
        multa.setMonto(500.0);
        multa.setFechaEmision(LocalDate.now());
        multa.setFechaVencimiento(LocalDate.now().plusDays(5)); // no vencida
        multa.setEstado(EstadoMulta.PENDIENTE);

        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));

        PagoResponseDTO response = service.procesarPago(1L);

        verify(multaRepository, times(1)).findById(1L);
        verify(pagoRepository, times(1)).save(any(Pago.class));
        verify(multaRepository, times(1)).save(multa);

        assertEquals(EstadoMulta.PAGADA, multa.getEstado());
    }

    @Test
    @DisplayName("Pregunta 4: Usando el argumentCaptor")
    void procesarPago_multaVencidaConRecargo_usandoArgumentCaptor() {

        Multa multa = new Multa();
        multa.setId(1L);
        multa.setMonto(500.0);
        multa.setFechaEmision(LocalDate.now().minusDays(12));
        multa.setFechaVencimiento(LocalDate.now().minusDays(2));
        multa.setEstado(EstadoMulta.PENDIENTE);

        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));
        service.procesarPago(1L);

        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);

        verify(pagoRepository, times(1)).save(pagoCaptor.capture());
        Pago pagoGuardado = pagoCaptor.getValue();

        assertEquals(75.0, pagoGuardado.getRecargo());
        assertEquals(0.0, pagoGuardado.getDescuentoAplicado());
        assertEquals(575.0, pagoGuardado.getMontoPagado());
    }

}
