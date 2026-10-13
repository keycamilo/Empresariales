package com.ligafutbol.controller.web;

import com.ligafutbol.model.Asociacion;
import com.ligafutbol.service.AsociacionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Paginas HTML de Asociacion: listar, detalle (con sus clubes), crear, editar y eliminar. */
@Controller
@RequestMapping("/asociaciones")
public class AsociacionWebController {

    private static final String FORM = "asociaciones/form";
    private static final String LISTA = "redirect:/asociaciones";

    private final AsociacionService service;

    public AsociacionWebController(AsociacionService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("asociaciones", service.listar());
        return "asociaciones/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("asociacion", new Asociacion());
        model.addAttribute("accion", "/asociaciones");
        return FORM;
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable String id, Model model) {
        model.addAttribute("asociacion", service.buscar(id));
        model.addAttribute("clubes", service.clubes(id));
        return "asociaciones/detalle";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("asociacion") Asociacion asociacion, BindingResult result,
                        Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "/asociaciones");
            return FORM;
        }
        service.crear(asociacion);
        flash.addFlashAttribute("ok", "Asociacion creada correctamente");
        return LISTA;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable String id, Model model) {
        model.addAttribute("asociacion", service.buscar(id));
        model.addAttribute("accion", "/asociaciones/" + id);
        return FORM;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable String id, @Valid @ModelAttribute("asociacion") Asociacion asociacion,
                             BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "/asociaciones/" + id);
            return FORM;
        }
        service.actualizar(id, asociacion);
        flash.addFlashAttribute("ok", "Asociacion actualizada correctamente");
        return LISTA;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable String id, RedirectAttributes flash) {
        try {
            service.eliminar(id);
            flash.addFlashAttribute("ok", "Asociacion eliminada correctamente");
        } catch (ResponseStatusException e) {
            flash.addFlashAttribute("error", e.getReason());
        }
        return LISTA;
    }
}
