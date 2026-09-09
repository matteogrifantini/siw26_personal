package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Destinazione;
import it.uniroma3.siw.service.DestinazioneService;
import it.uniroma3.siw.service.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DestinazioneController {

    @Autowired
    private DestinazioneService destinazioneService;

    @Autowired
    private ViaggioService viaggioService;

    @GetMapping("/destinazioni")
    public String getAllDestinazioni(Model model) {
        model.addAttribute("destinazioni", destinazioneService.findAll());
        return "destinazione/destinazioni";
    }

    @GetMapping("/destinazioni/{id}")
    public String getDestinazioneDetails(@PathVariable Long id, Model model) {
        Destinazione destinazione = destinazioneService.findById(id);
        if (destinazione == null) {
            return "redirect:/destinazioni";
        }
        model.addAttribute("destinazione", destinazione);
        model.addAttribute("viaggi", viaggioService.findByDestinazione(destinazione));
        return "destinazione/destinazione";
    }
}
