package org.mockitoapp.repositories;

import java.util.List;

public interface PreguntaRepository {

    List<String> findPreguntarPorExamenId(Long id) throws InterruptedException;
    void guardarVarias(List<String> preguntas);
}
