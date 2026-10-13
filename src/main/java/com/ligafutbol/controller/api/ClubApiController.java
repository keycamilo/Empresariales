package com.ligafutbol.controller.api;

import com.ligafutbol.model.Club;
import com.ligafutbol.model.ClubRequest;
import com.ligafutbol.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** API REST de Club: JSON en /api/clubes (CRUD + todas las relaciones). */
@RestController
@RequestMapping("/api/clubes")
public class ClubApiController {

    private final ClubService service;

    public ClubApiController(ClubService service) {
        this.service = service;
    }

    // ---- CRUD
    @GetMapping
    public List<Club> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Club buscar(@PathVariable String id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Club crear(@Valid @RequestBody ClubRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public Club actualizar(@PathVariable String id, @Valid @RequestBody ClubRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        service.eliminar(id);
    }

    // ---- @OneToOne entrenador
    @PutMapping("/{id}/entrenador/{entrenadorId}")
    public Club asignarEntrenador(@PathVariable String id, @PathVariable String entrenadorId) {
        return service.asignarEntrenador(id, entrenadorId);
    }

    @DeleteMapping("/{id}/entrenador")
    public Club quitarEntrenador(@PathVariable String id) {
        return service.quitarEntrenador(id);
    }

    // ---- @OneToMany jugadores
    @PostMapping("/{id}/jugadores/{jugadorId}")
    public Club agregarJugador(@PathVariable String id, @PathVariable String jugadorId) {
        return service.agregarJugador(id, jugadorId);
    }

    @DeleteMapping("/{id}/jugadores/{jugadorId}")
    public Club quitarJugador(@PathVariable String id, @PathVariable String jugadorId) {
        return service.quitarJugador(id, jugadorId);
    }

    // ---- @ManyToOne asociacion
    @PutMapping("/{id}/asociacion/{asociacionId}")
    public Club asignarAsociacion(@PathVariable String id, @PathVariable String asociacionId) {
        return service.asignarAsociacion(id, asociacionId);
    }

    // ---- @ManyToMany competiciones
    @PostMapping("/{id}/competiciones/{competicionId}")
    public Club inscribir(@PathVariable String id, @PathVariable String competicionId) {
        return service.inscribirEnCompeticion(id, competicionId);
    }

    @DeleteMapping("/{id}/competiciones/{competicionId}")
    public Club retirar(@PathVariable String id, @PathVariable String competicionId) {
        return service.retirarDeCompeticion(id, competicionId);
    }
}
