package com.ligafutbol.controller.web;

import com.ligafutbol.model.Entrenador;
import com.ligafutbol.service.EntrenadorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Paginas HTML de Entrenador: listar, crear, editar y eliminar. */
@Controller
@RequestMapping("/entrenadores")
public class EntrenadorWebController {

    private static final String FORM = "entrenadores/form";
    private static final String LISTA = "redirect:/entrenadores";

    private final EntrenadorService service;

    public EntrenadorWebController(EntrenadorService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("entrenadores", service.listar());
        return "entrenadores/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("entrenador", new Entrenador());
        model.addAttribute("accion", "/entrenadores");
        return FORM;
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("entrenador") Entrenador entrenador, BindingResult result,
                        Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "/entrenadores");
            return FORM;
        }
        service.crear(entrenador);
        flash.addFlashAttribute("ok", "Entrenador creado correctamente");
        return LISTA;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable String id, Model model) {
        model.addAttribute("entrenador", service.buscar(id));
        model.addAttribute("accion", "/entrenadores/" + id);
        return FORM;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable String id, @Valid @ModelAttribute("entrenador") Entrenador entrenador,
                             BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "/entrenadores/" + id);
            return FORM;
        }
        service.actualizar(id, entrenador);
        flash.addFlashAttribute("ok", "Entrenador actualizado correctamente");
        return LISTA;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable String id, RedirectAttributes flash) {
        try {
            service.eliminar(id);
            flash.addFlashAttribute("ok", "Entrenador eliminado correctamente");
        } catch (ResponseStatusException e) {
            flash.addFlashAttribute("error", e.getReason());
        }
        return LISTA;
    }
}
