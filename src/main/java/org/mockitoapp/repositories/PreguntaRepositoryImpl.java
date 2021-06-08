package org.mockitoapp.repositories;

import org.mockitoapp.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntaRepositoryImpl implements PreguntaRepository{

    @Override
    public List<String> findPreguntarPorExamenId(Long id) throws InterruptedException {
        System.out.println("PreguntaRepositoryImpl.findPreguntarPorExamenId");
        TimeUnit.SECONDS.sleep(2);
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("PreguntaRepositoryImpl.guardarVarias");
    }
}
