package org.mockitoapp.repositories;

import org.mockitoapp.Datos;
import org.mockitoapp.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class ExamenRepositoryImpl implements ExamenRepository{


    @Override
    public List<Examen> findAll() throws InterruptedException {
        System.out.println("ExamenRepositoryImpl.findAll");
        TimeUnit.SECONDS.sleep(5);
        return Datos.EXAMENES;
    }



    @Override
    public Examen guardar(Examen examen) {
        System.out.println("ExamenRepositoryImpl.guardar");
        return Datos.EXAMEN;
    }
}
