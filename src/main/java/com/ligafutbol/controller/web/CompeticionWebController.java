package com.ligafutbol.controller.web;

import com.ligafutbol.model.Competicion;
import com.ligafutbol.service.CompeticionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Paginas HTML de Competicion: listar, detalle (con sus clubes), crear, editar y eliminar. */
@Controller
@RequestMapping("/competiciones")
public class CompeticionWebController {

    private static final String FORM = "competiciones/form";
    private static final String LISTA = "redirect:/competiciones";

    private final CompeticionService service;

    public CompeticionWebController(CompeticionService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("competiciones", service.listar());
        return "competiciones/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("competicion", new Competicion());
        model.addAttribute("accion", "/competiciones");
        return FORM;
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        model.addAttribute("competicion", service.buscar(id));
        model.addAttribute("clubes", service.clubes(id));
        return "competiciones/detalle";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("competicion") Competicion competicion, BindingResult result,
                        Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "/competiciones");
            return FORM;
        }
        service.crear(competicion);
        flash.addFlashAttribute("ok", "Competicion creada correctamente");
        return LISTA;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable String id, Model model) {
        model.addAttribute("competicion", service.buscar(id));
        model.addAttribute("accion", "/competiciones/" + id);
        return FORM;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable String id, @Valid @ModelAttribute("competicion") Competicion competicion,
                             BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "/competiciones/" + id);
            return FORM;
        }
        service.actualizar(id, competicion);
        flash.addFlashAttribute("ok", "Competicion actualizada correctamente");
        return LISTA;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable String id, RedirectAttributes flash) {
        try {
            service.eliminar(id);
            flash.addFlashAttribute("ok", "Competicion eliminada correctamente");
        } catch (ResponseStatusException e) {
            flash.addFlashAttribute("error", e.getReason());
        }
        return LISTA;
    }
}
