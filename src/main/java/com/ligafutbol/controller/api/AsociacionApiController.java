package com.ligafutbol.controller.api;

import com.ligafutbol.model.Club;
import com.ligafutbol.model.Asociacion;
import com.ligafutbol.service.AsociacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** API REST de Asociacion: JSON en /api/asociaciones. */
@RestController
@RequestMapping("/api/asociaciones")
public class AsociacionApiController {

    private final AsociacionService service;

    public AsociacionApiController(AsociacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Asociacion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Asociacion buscar(@PathVariable String id) {
        return service.buscar(id);
    }

    /** Lado inverso de @ManyToOne: clubes afiliados a la asociacion. */
    @GetMapping("/{id}/clubes")
    public List<Club> clubes(@PathVariable String id) {
        return service.clubes(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Asociacion crear(@Valid @RequestBody Asociacion asociacion) {
        return service.crear(asociacion);
    }

    @PutMapping("/{id}")
    public Asociacion actualizar(@PathVariable String id, @Valid @RequestBody Asociacion asociacion) {
        return service.actualizar(id, asociacion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        service.eliminar(id);
    }
}
