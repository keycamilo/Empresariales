package com.ligafutbol.controller.web;

import com.ligafutbol.model.Jugador;
import com.ligafutbol.service.JugadorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Paginas HTML de Jugador: listar, crear, editar y eliminar. */
@Controller
@RequestMapping("/jugadores")
public class JugadorWebController {

    private static final String FORM = "jugadores/form";
    private static final String LISTA = "redirect:/jugadores";

    private final JugadorService service;

    public JugadorWebController(JugadorService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("jugadores", service.listar());
        return "jugadores/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("accion", "/jugadores");
        return FORM;
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("jugador") Jugador jugador, BindingResult result,
                        Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "/jugadores");
            return FORM;
        }
        service.crear(jugador);
        flash.addFlashAttribute("ok", "Jugador creado correctamente");
        return LISTA;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable String id, Model model) {
        model.addAttribute("jugador", service.buscar(id));
        model.addAttribute("accion", "/jugadores/" + id);
        return FORM;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable String id, @Valid @ModelAttribute("jugador") Jugador jugador,
                             BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "/jugadores/" + id);
            return FORM;
        }
        service.actualizar(id, jugador);
        flash.addFlashAttribute("ok", "Jugador actualizado correctamente");
        return LISTA;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable String id, RedirectAttributes flash) {
        try {
            service.eliminar(id);
            flash.addFlashAttribute("ok", "Jugador eliminado correctamente");
        } catch (ResponseStatusException e) {
            flash.addFlashAttribute("error", e.getReason());
        }
        return LISTA;
    }
}
