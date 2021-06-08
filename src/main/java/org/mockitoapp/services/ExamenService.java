package org.mockitoapp.services;

import org.mockitoapp.models.Examen;

import java.util.Optional;

public interface ExamenService {

    Optional<Examen> findExamenPorNombre(String nombre) throws InterruptedException;
    Examen findExamenPorNombreConPreguntas(String nombre) throws InterruptedException;
    Examen guardar(Examen examen);
}
