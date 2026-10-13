package com.ligafutbol.service;

import com.ligafutbol.model.Jugador;
import com.ligafutbol.repository.JugadorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JugadorService {

    private final JugadorRepository repository;
    private final ClubService clubService;

    public JugadorService(JugadorRepository repository, ClubService clubService) {
        this.repository = repository;
        this.clubService = clubService;
    }

    public List<Jugador> listar() {
        return repository.findAll();
    }

    public Jugador buscar(String id) {
        return repository.findById(id).orElseThrow(() -> noEncontrado(id));
    }

    public Jugador crear(Jugador jugador) {
        jugador.setId(null);
        return repository.save(jugador);
    }

    public Jugador actualizar(String id, Jugador jugador) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        jugador.setId(id);
        return repository.save(jugador);
    }

    public void eliminar(String id) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        clubService.desvincularJugador(id);
        repository.deleteById(id);
    }

    private ResponseStatusException noEncontrado(String id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Jugador no encontrado: " + id);
    }
}
