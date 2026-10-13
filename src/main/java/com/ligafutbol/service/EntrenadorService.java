package com.ligafutbol.service;

import com.ligafutbol.model.Entrenador;
import com.ligafutbol.repository.EntrenadorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EntrenadorService {

    private final EntrenadorRepository repository;
    private final ClubService clubService;

    public EntrenadorService(EntrenadorRepository repository, ClubService clubService) {
        this.repository = repository;
        this.clubService = clubService;
    }

    public List<Entrenador> listar() {
        return repository.findAll();
    }

    public Entrenador buscar(String id) {
        return repository.findById(id).orElseThrow(() -> noEncontrado(id));
    }

    public Entrenador crear(Entrenador entrenador) {
        entrenador.setId(null);
        return repository.save(entrenador);
    }

    public Entrenador actualizar(String id, Entrenador entrenador) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        entrenador.setId(id);
        return repository.save(entrenador);
    }

    public void eliminar(String id) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        clubService.desvincularEntrenador(id);
        repository.deleteById(id);
    }

    private ResponseStatusException noEncontrado(String id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrenador no encontrado: " + id);
    }
}
