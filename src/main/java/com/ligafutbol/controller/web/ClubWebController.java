package com.ligafutbol.controller.web;

import com.ligafutbol.model.Club;
import com.ligafutbol.model.ClubRequest;
import com.ligafutbol.model.Entrenador;
import com.ligafutbol.model.Jugador;
import com.ligafutbol.service.AsociacionService;
import com.ligafutbol.service.ClubService;
import com.ligafutbol.service.CompeticionService;
import com.ligafutbol.service.EntrenadorService;
import com.ligafutbol.service.JugadorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Paginas HTML de Club: CRUD, detalle y operaciones sobre sus 4 relaciones. */
@Controller
@RequestMapping("/clubes")
public class ClubWebController {

    private static final String FORM = "clubes/form";
    private static final String LISTA = "redirect:/clubes";

    private final ClubService service;
    private final EntrenadorService entrenadorService;
    private final JugadorService jugadorService;
    private final AsociacionService asociacionService;
    private final CompeticionService competicionService;

    public ClubWebController(ClubService service,
                             EntrenadorService entrenadorService,
                             JugadorService jugadorService,
                             AsociacionService asociacionService,
                             CompeticionService competicionService) {
        this.service = service;
        this.entrenadorService = entrenadorService;
        this.jugadorService = jugadorService;
        this.asociacionService = asociacionService;
        this.competicionService = competicionService;
    }

    // ------------------------------------------------------------- CRUD

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clubes", service.listar());
        return "clubes/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("clubForm", new ClubRequest());
        return formulario(model, "/clubes");
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("clubForm") ClubRequest request, BindingResult result,
                        Model model, RedirectAttributes flash) {
        if (!result.hasErrors()) {
            try {
                service.crear(request);
                flash.addFlashAttribute("ok", "Club creado correctamente");
                return LISTA;
            } catch (ResponseStatusException e) {
                result.reject("club.error", e.getReason());
            }
        }
        return formulario(model, "/clubes");
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        Club club = service.buscar(id);
        List<Club> todos = service.listar();

        Set<String> entrenadoresOcupados = todos.stream()
                .filter(c -> c.getEntrenador() != null)
                .map(c -> c.getEntrenador().getId())
                .collect(Collectors.toSet());
        Set<String> jugadoresOcupados = todos.stream()
                .flatMap(c -> c.getJugadores().stream())
                .map(Jugador::getId)
                .collect(Collectors.toSet());
        Set<String> competicionesDelClub = club.getCompeticiones().stream()
                .map(c -> c.getId())
                .collect(Collectors.toSet());

        model.addAttribute("club", club);
        model.addAttribute("entrenadoresLibres", entrenadorService.listar().stream()
                .filter((Entrenador e) -> !entrenadoresOcupados.contains(e.getId())).toList());
        model.addAttribute("jugadoresLibres", jugadorService.listar().stream()
                .filter(j -> !jugadoresOcupados.contains(j.getId())).toList());
        model.addAttribute("asociaciones", asociacionService.listar());
        model.addAttribute("competicionesDisponibles", competicionService.listar().stream()
                .filter(c -> !competicionesDelClub.contains(c.getId())).toList());
        return "clubes/detalle";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable String id, Model model) {
        model.addAttribute("clubForm", aRequest(service.buscar(id)));
        return formulario(model, "/clubes/" + id);
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable String id, @Valid @ModelAttribute("clubForm") ClubRequest request,
                             BindingResult result, Model model, RedirectAttributes flash) {
        if (!result.hasErrors()) {
            try {
                service.actualizar(id, request);
                flash.addFlashAttribute("ok", "Club actualizado correctamente");
                return LISTA;
            } catch (ResponseStatusException e) {
                result.reject("club.error", e.getReason());
            }
        }
        return formulario(model, "/clubes/" + id);
    }

    /** Borra el club y en cascada su entrenador y sus jugadores. */
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable String id, RedirectAttributes flash) {
        return ejecutar(flash, "Club eliminado correctamente", LISTA, () -> service.eliminar(id));
    }

    // ------------------------------------------------ @OneToOne entrenador

    @PostMapping("/{id}/entrenador")
    public String asignarEntrenador(@PathVariable String id, @RequestParam String entrenadorId,
                                    RedirectAttributes flash) {
        return ejecutar(flash, "Entrenador asignado", detalleDe(id), () -> service.asignarEntrenador(id, entrenadorId));
    }

    @PostMapping("/{id}/entrenador/quitar")
    public String quitarEntrenador(@PathVariable String id, RedirectAttributes flash) {
        return ejecutar(flash, "Entrenador desvinculado", detalleDe(id), () -> service.quitarEntrenador(id));
    }

    // ------------------------------------------------ @OneToMany jugadores

    @PostMapping("/{id}/jugadores")
    public String agregarJugador(@PathVariable String id, @RequestParam String jugadorId,
                                 RedirectAttributes flash) {
        return ejecutar(flash, "Jugador agregado a la plantilla", detalleDe(id), () -> service.agregarJugador(id, jugadorId));
    }

    @PostMapping("/{id}/jugadores/{jugadorId}/quitar")
    public String quitarJugador(@PathVariable String id, @PathVariable String jugadorId,
                                RedirectAttributes flash) {
        return ejecutar(flash, "Jugador retirado de la plantilla", detalleDe(id), () -> service.quitarJugador(id, jugadorId));
    }

    // ------------------------------------------------ @ManyToOne asociacion

    @PostMapping("/{id}/asociacion")
    public String asignarAsociacion(@PathVariable String id, @RequestParam String asociacionId,
                                    RedirectAttributes flash) {
        return ejecutar(flash, "Asociacion asignada", detalleDe(id), () -> service.asignarAsociacion(id, asociacionId));
    }

    // ------------------------------------------- @ManyToMany competiciones

    @PostMapping("/{id}/competiciones")
    public String inscribir(@PathVariable String id, @RequestParam String competicionId,
                            RedirectAttributes flash) {
        return ejecutar(flash, "Club inscrito en la competicion", detalleDe(id), () -> service.inscribirEnCompeticion(id, competicionId));
    }

    @PostMapping("/{id}/competiciones/{competicionId}/quitar")
    public String retirar(@PathVariable String id, @PathVariable String competicionId,
                          RedirectAttributes flash) {
        return ejecutar(flash, "Club retirado de la competicion", detalleDe(id), () -> service.retirarDeCompeticion(id, competicionId));
    }

    // ------------------------------------------------------- auxiliares

    private String formulario(Model model, String accion) {
        model.addAttribute("accion", accion);
        model.addAttribute("entrenadores", entrenadorService.listar());
        model.addAttribute("jugadores", jugadorService.listar());
        model.addAttribute("asociaciones", asociacionService.listar());
        model.addAttribute("competiciones", competicionService.listar());
        return FORM;
    }

    private static String detalleDe(String id) {
        return "redirect:/clubes/" + id;
    }

    /** Ejecuta la operacion y deja un mensaje flash de exito o de error (404/409 del servicio). */
    private static String ejecutar(RedirectAttributes flash, String mensajeOk, String destino, Runnable operacion) {
        try {
            operacion.run();
            flash.addFlashAttribute("ok", mensajeOk);
        } catch (ResponseStatusException e) {
            flash.addFlashAttribute("error", e.getReason());
        }
        return destino;
    }

    private static ClubRequest aRequest(Club club) {
        ClubRequest r = new ClubRequest();
        r.setNombre(club.getNombre());
        r.setEntrenadorId(club.getEntrenador() != null ? club.getEntrenador().getId() : null);
        r.setJugadoresIds(club.getJugadores().stream().map(Jugador::getId).collect(Collectors.toList()));
        r.setAsociacionId(club.getAsociacion() != null ? club.getAsociacion().getId() : null);
        r.setCompeticionesIds(club.getCompeticiones().stream().map(c -> c.getId()).collect(Collectors.toList()));
        return r;
    }
}
