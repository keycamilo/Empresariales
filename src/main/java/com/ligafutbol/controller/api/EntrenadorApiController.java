package com.ligafutbol.controller.api;

import com.ligafutbol.model.Entrenador;
import com.ligafutbol.service.EntrenadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** API REST de Entrenador: JSON en /api/entrenadores. */
@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorApiController {

    private final EntrenadorService service;

    public EntrenadorApiController(EntrenadorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Entrenador> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Entrenador buscar(@PathVariable String id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entrenador crear(@Valid @RequestBody Entrenador entrenador) {
        return service.crear(entrenador);
    }

    @PutMapping("/{id}")
    public Entrenador actualizar(@PathVariable String id, @Valid @RequestBody Entrenador entrenador) {
        return service.actualizar(id, entrenador);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        service.eliminar(id);
    }
}
