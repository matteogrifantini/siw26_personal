package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.*;
import it.uniroma3.siw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ViaggioController {

    @Autowired
    private ViaggioService viaggioService;

    @Autowired
    private DestinazioneService destinazioneService;

    @Autowired
    private CategoriaViaggioService categoriaService;

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping({"/viaggi", "/ricerca", "/ricerca-viaggi"})
    public String getCatalogoViaggi(Model model) {
        model.addAttribute("destinazioni", destinazioneService.findAll());
        return "viaggio/viaggi";
    }

    @GetMapping("/viaggi/{id}")
    public String getDettaglioViaggio(@PathVariable Long id, Model model, Authentication authentication) {
        Viaggio viaggio = viaggioService.findById(id);
        if (viaggio == null) {
            return "redirect:/viaggi";
        }
        List<Recensione> recensioni = recensioneService.findByViaggio(viaggio);

        model.addAttribute("viaggio", viaggio);
        model.addAttribute("recensioni", recensioni);

        boolean haGiaRecensito = false;
        Recensione propriaRecensione = null;

        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            Credentials creds = credentialsService.getCredentials(authentication.getName()).orElse(null);
            if (creds != null) {
                Utente utente = creds.getUtente();
                Optional<Recensione> recOpt = recensioneService.findByViaggioAndAutore(viaggio, utente);
                if (recOpt.isPresent()) {
                    haGiaRecensito = true;
                    propriaRecensione = recOpt.get();
                }
                model.addAttribute("utenteCorrente", utente);
            }
        }

        model.addAttribute("haGiaRecensito", haGiaRecensito);
        model.addAttribute("propriaRecensione", propriaRecensione);
        model.addAttribute("nuovaRecensione", new Recensione());

        return "viaggio/viaggio";
    }
}
