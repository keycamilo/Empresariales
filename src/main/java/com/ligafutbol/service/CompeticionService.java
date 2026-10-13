package com.ligafutbol.service;

import com.ligafutbol.model.Competicion;
import com.ligafutbol.model.Club;
import com.ligafutbol.repository.CompeticionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompeticionService {

    private final CompeticionRepository repository;
    private final ClubService clubService;

    public CompeticionService(CompeticionRepository repository, ClubService clubService) {
        this.repository = repository;
        this.clubService = clubService;
    }

    public List<Competicion> listar() {
        return repository.findAll();
    }

    public Competicion buscar(String id) {
        return repository.findById(id).orElseThrow(() -> noEncontrado(id));
    }

    public Competicion crear(Competicion competicion) {
        competicion.setId(null);
        return repository.save(competicion);
    }

    public Competicion actualizar(String id, Competicion competicion) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        competicion.setId(id);
        return repository.save(competicion);
    }

    public void eliminar(String id) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        clubService.desvincularCompeticion(id); // se saca de todos los clubes
        repository.deleteById(id);
    }

    /** Lado inverso de @ManyToMany: clubes que participan en la competicion. */
    public List<Club> clubes(String id) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        return clubService.clubesDeCompeticion(id);
    }

    private ResponseStatusException noEncontrado(String id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Competicion no encontrada: " + id);
    }
}
