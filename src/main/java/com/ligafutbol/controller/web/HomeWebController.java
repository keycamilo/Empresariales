package com.ligafutbol.controller.web;

import com.ligafutbol.repository.ClubRepository;
import com.ligafutbol.repository.JugadorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Paginas de inicio: panel principal (index.html) y resumen de datos. */
@Controller
public class HomeWebController {

    private final ClubRepository clubRepository;
    private final JugadorRepository jugadorRepository;

    public HomeWebController(ClubRepository clubRepository, JugadorRepository jugadorRepository) {
        this.clubRepository = clubRepository;
        this.jugadorRepository = jugadorRepository;
    }

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("clubes", clubRepository.findAll());
        model.addAttribute("totalClubes", clubRepository.count());
        model.addAttribute("totalJugadores", jugadorRepository.count());
        return "listar";
    }
}
