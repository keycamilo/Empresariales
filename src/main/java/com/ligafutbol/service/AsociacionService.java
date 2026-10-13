package com.ligafutbol.service;

import com.ligafutbol.model.Asociacion;
import com.ligafutbol.model.Club;
import com.ligafutbol.repository.AsociacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AsociacionService {

    private final AsociacionRepository repository;
    private final ClubService clubService;

    public AsociacionService(AsociacionRepository repository, ClubService clubService) {
        this.repository = repository;
        this.clubService = clubService;
    }

    public List<Asociacion> listar() {
        return repository.findAll();
    }

    public Asociacion buscar(String id) {
        return repository.findById(id).orElseThrow(() -> noEncontrado(id));
    }

    public Asociacion crear(Asociacion asociacion) {
        asociacion.setId(null);
        return repository.save(asociacion);
    }

    public Asociacion actualizar(String id, Asociacion asociacion) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        asociacion.setId(id);
        return repository.save(asociacion);
    }

    public void eliminar(String id) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        clubService.validarBorradoAsociacion(id); // 409 si tiene clubes afiliados
        repository.deleteById(id);
    }

    /** Lado inverso de @ManyToOne: clubes afiliados a la asociacion. */
    public List<Club> clubes(String id) {
        if (!repository.existsById(id)) throw noEncontrado(id);
        return clubService.clubesDeAsociacion(id);
    }

    private ResponseStatusException noEncontrado(String id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Asociacion no encontrada: " + id);
    }
}
