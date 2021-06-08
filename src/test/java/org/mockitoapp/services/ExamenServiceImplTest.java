package org.mockitoapp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.mockitoapp.Datos;
import org.mockitoapp.models.Examen;
import org.mockitoapp.repositories.ExamenRepository;
import org.mockitoapp.repositories.ExamenRepositoryImpl;
import org.mockitoapp.repositories.PreguntaRepository;
import org.mockitoapp.repositories.PreguntaRepositoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    //*********************************************
    @Mock
    ExamenRepositoryImpl examenRepositoryImpl;

    @Mock
    PreguntaRepositoryImpl preguntaRepositoryImpl;
    //**********************************************

    @Mock
    ExamenRepository examenRepository;

    @Mock
    PreguntaRepository preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Captor // captura el argumento y lo usa para validar
    ArgumentCaptor<Long> captor;


    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this); //habilitamos el uso de anotaciones para esta clase
        //examenRepository = mock(ExamenRepository.class);
        //preguntaRepository = mock(PreguntaRepository.class);
        //service = new ExamenServiceImpl(examenRepository,preguntaRepository);
    }


    @Test
    void findExamenPorNombre() throws InterruptedException {

        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertTrue(examen.isPresent());
        assertEquals(1L, examen.get().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }


    @Test
    void findExamenPorNombreListaVacia() throws InterruptedException {
        List<Examen> datos = Collections.emptyList();

        when(examenRepository.findAll()).thenReturn(datos);

        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");
        assertFalse(examen.isPresent());
    }


    @Test
    void testPreguntasExamen() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));
    }


    @Test
    void testPreguntasExamenVerify() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntarPorExamenId(anyLong());
    }


    @Test
    void testNoExisteExamenVerify() throws InterruptedException {
        //Given
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //When
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas 2");

        //Then
        assertNull(examen);

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntarPorExamenId(anyLong());
    }


    @Test
    void testGuardarExamen() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        //When
        Examen examen = service.guardar(newExamen);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }


    @Test
    void testManejoException() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntarPorExamenId(anyLong());
    }


    @Test
    void testArgumentMatchers() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(examenRepository).findAll();
        //verify(preguntaRepository).findPreguntarPorExamenId(argThat(arg -> arg != null && arg.equals(1L)));
        verify(preguntaRepository).findPreguntarPorExamenId(argThat(arg -> arg != null && arg >= 1L));
        verify(preguntaRepository).findPreguntarPorExamenId(eq(1L  ));
    }



    // Se obliga a que muestre el error
    @Test
    void testArgumentMatchers2() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntarPorExamenId(argThat(new MiArgsMatchers()));
    }

    // clase inner para realizar los match
    public static class MiArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "Es un mensaje personalizado de error que imprime mockito en caso de que falle el test";
        }
    }


    @Test
    void testArgumentCaptor() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntaRepository).findPreguntarPorExamenId(captor.capture());

        assertEquals(1L, captor.getValue());
    }


    // para metodos void
    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    @Test
    void testDoAnswer() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 1L? Datos.PREGUNTAS: null;
        }).when(preguntaRepository).findPreguntarPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertTrue(examen.getPreguntas().contains("geometria"));
        assertEquals(1L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(5,examen.getPreguntas().size());

        verify(preguntaRepository).findPreguntarPorExamenId(anyLong());
    }



    @Test
    void testDoAnswerGuardarExamen() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        doAnswer(new Answer<Examen>(){
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(examenRepository).guardar(any(Examen.class));

        //When
        Examen examen = service.guardar(newExamen);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }


    @Test
    void testDoCallRealMethod() throws InterruptedException {
        when(examenRepositoryImpl.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        doCallRealMethod().when(preguntaRepositoryImpl).findPreguntarPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(1L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }


    @Test
    void testSpy() throws InterruptedException {
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository,preguntaRepository);

        List<String> preguntas = Collections.singletonList("aritmetica");
        //when(preguntaRepository.findPreguntarPorExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPreguntarPorExamenId(anyLong());


        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(1, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntarPorExamenId(anyLong());
    }


    @Test
    void testOrdenInvocaciones() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Lenguaje");

        InOrder inOrder = inOrder(preguntaRepository); // verifica el orden de invocacion
        inOrder.verify(preguntaRepository).findPreguntarPorExamenId(1L);
        inOrder.verify(preguntaRepository).findPreguntarPorExamenId(2L);
    }


    @Test
    void testOrdenInvocaciones2() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Lenguaje");

        InOrder inOrder = inOrder(examenRepository, preguntaRepository); // verifica el orden de invocacion
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntaRepository).findPreguntarPorExamenId(1L);
        inOrder.verify(preguntaRepository).findPreguntarPorExamenId(2L);
    }


    @Test
    void testNumeroInvocaciones() throws InterruptedException {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(preguntaRepository, times(1)).findPreguntarPorExamenId(1L);
        verify(preguntaRepository, atLeast(1)).findPreguntarPorExamenId(1L);
        verify(preguntaRepository, atLeastOnce()).findPreguntarPorExamenId(1L);
        verify(preguntaRepository, atMost(1)).findPreguntarPorExamenId(1L);
        verify(preguntaRepository, atMostOnce()).findPreguntarPorExamenId(1L);
    }
}













