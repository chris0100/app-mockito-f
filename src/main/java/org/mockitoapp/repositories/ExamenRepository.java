package org.mockitoapp.repositories;

import org.mockitoapp.models.Examen;

import java.util.List;

public interface ExamenRepository {

    List<Examen> findAll() throws InterruptedException;
    Examen guardar(Examen examen);
}
