package com.ligafutbol.controller.api;

import com.ligafutbol.model.Club;
import com.ligafutbol.model.Competicion;
import com.ligafutbol.service.CompeticionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** API REST de Competicion: JSON en /api/competiciones. */
@RestController
@RequestMapping("/api/competiciones")
public class CompeticionApiController {

    private final CompeticionService service;

    public CompeticionApiController(CompeticionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Competicion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Competicion buscar(@PathVariable String id) {
        return service.buscar(id);
    }

    /** Lado inverso de @ManyToMany: clubes que participan en la competicion. */
    @GetMapping("/{id}/clubes")
    public List<Club> clubes(@PathVariable String id) {
        return service.clubes(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Competicion crear(@Valid @RequestBody Competicion competicion) {
        return service.crear(competicion);
    }

    @PutMapping("/{id}")
    public Competicion actualizar(@PathVariable String id, @Valid @RequestBody Competicion competicion) {
        return service.actualizar(id, competicion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        service.eliminar(id);
    }
}
