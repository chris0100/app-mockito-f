package org.mockitoapp.services;

import org.mockitoapp.models.Examen;
import org.mockitoapp.repositories.ExamenRepository;
import org.mockitoapp.repositories.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{

    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;


    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }


    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) throws InterruptedException {
        return examenRepository.findAll()
                .stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) throws InterruptedException {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);

        Examen examen = null;
        if (examenOptional.isPresent()){
            examen = examenOptional.orElse(null);
            List<String> preguntas = preguntaRepository.findPreguntarPorExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }



    @Override
    public Examen guardar(Examen examen) {
        if (!examen.getPreguntas().isEmpty()){
            preguntaRepository.guardarVarias(examen.getPreguntas());
        }
        return examenRepository.guardar(examen);
    }
}
