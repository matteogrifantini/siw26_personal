package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Viaggio;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RecensioneService;
import it.uniroma3.siw.service.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recensioni")
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private ViaggioService viaggioService;

    @Autowired
    private CredentialsService credentialsService;

    private Utente getUtenteLoggato(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Credentials credentials = credentialsService.getCredentials(authentication.getName()).orElse(null);
        return (credentials != null) ? credentials.getUtente() : null;
    }

    @PostMapping("/salva")
    public String salvaRecensione(
            @RequestParam Long viaggioId,
            @RequestParam Integer voto,
            @RequestParam String titolo,
            @RequestParam String commento,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Utente utente = getUtenteLoggato(authentication);
        if (utente == null) return "redirect:/login";

        Viaggio viaggio = viaggioService.findById(viaggioId);
        if (viaggio == null) return "redirect:/viaggi";

        try {
            recensioneService.aggiungiRecensione(viaggio, utente, titolo, voto, commento);
            redirectAttributes.addFlashAttribute("successMessage", "Recensione pubblicata con successo!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/viaggi/" + viaggioId;
    }

    @GetMapping("/modifica/{id}")
    public String formModificaRecensione(@PathVariable Long id, Model model, Authentication authentication) {
        Recensione recensione = recensioneService.findById(id);
        Utente utente = getUtenteLoggato(authentication);

        if (recensione == null || utente == null || !recensione.getAutore().getId().equals(utente.getId())) {
            return "redirect:/viaggi";
        }

        model.addAttribute("recensione", recensione);
        return "recensione/modificaRecensione";
    }

    @PostMapping("/modifica/{id}")
    public String modificaRecensione(
            @PathVariable Long id,
            @RequestParam Integer voto,
            @RequestParam String titolo,
            @RequestParam String commento,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Utente utente = getUtenteLoggato(authentication);
        Recensione recensione = recensioneService.findById(id);
        if (recensione == null) return "redirect:/viaggi";

        try {
            recensioneService.modificaRecensione(id, voto, titolo, commento, utente);
            redirectAttributes.addFlashAttribute("successMessage", "Recensione aggiornata con successo!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/viaggi/" + recensione.getViaggio().getId();
    }

    @PostMapping("/elimina/{id}")
    public String eliminaRecensione(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Utente utente = getUtenteLoggato(authentication);
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Recensione recensione = recensioneService.findById(id);
        if (recensione == null) return "redirect:/viaggi";
        Long viaggioId = recensione.getViaggio().getId();

        try {
            recensioneService.eliminaRecensione(id, utente, isAdmin);
            redirectAttributes.addFlashAttribute("successMessage", "Recensione eliminata.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/viaggi/" + viaggioId;
    }
}
