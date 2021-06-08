package org.mockitoapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockitoapp.models.Examen;
import org.mockitoapp.repositories.ExamenRepositoryImpl;
import org.mockitoapp.repositories.PreguntaRepositoryImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {

    //*********************************************
    @Spy
    ExamenRepositoryImpl examenRepositoryImpl;

    @Spy
    PreguntaRepositoryImpl preguntaRepositoryImpl;
    //**********************************************

    @InjectMocks
    ExamenServiceImpl service;





    @Test
    void testSpy() throws InterruptedException {

        List<String> preguntas = Collections.singletonList("aritmetica");
        //when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepositoryImpl).findPreguntarPorExamenId(anyLong());


        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(1, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

        verify(examenRepositoryImpl).findAll();
        verify(preguntaRepositoryImpl).findPreguntarPorExamenId(anyLong());
    }
}













