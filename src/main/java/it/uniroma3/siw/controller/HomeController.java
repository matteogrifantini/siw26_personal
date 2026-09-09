package it.uniroma3.siw.controller;

import it.uniroma3.siw.service.DestinazioneService;
import it.uniroma3.siw.service.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ViaggioService viaggioService;

    @Autowired
    private DestinazioneService destinazioneService;

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("viaggiInEvidenza", viaggioService.findAll());
        model.addAttribute("destinazioni", destinazioneService.findAll());
        return "index";
    }
}
