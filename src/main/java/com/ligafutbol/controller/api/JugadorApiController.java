package com.ligafutbol.controller.api;

import com.ligafutbol.model.Jugador;
import com.ligafutbol.service.JugadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** API REST de Jugador: JSON en /api/jugadores. */
@RestController
@RequestMapping("/api/jugadores")
public class JugadorApiController {

    private final JugadorService service;

    public JugadorApiController(JugadorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Jugador> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Jugador buscar(@PathVariable String id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Jugador crear(@Valid @RequestBody Jugador jugador) {
        return service.crear(jugador);
    }

    @PutMapping("/{id}")
    public Jugador actualizar(@PathVariable String id, @Valid @RequestBody Jugador jugador) {
        return service.actualizar(id, jugador);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        service.eliminar(id);
    }
}
