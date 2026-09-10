package it.uniroma3.siw.controller;

import it.uniroma3.siw.exception.PostiInsufficientiException;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private CredentialsService credentialsService;

    private Utente getUtenteLoggato(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Credentials credentials = credentialsService.getCredentials(authentication.getName()).orElse(null);
        return (credentials != null) ? credentials.getUtente() : null;
    }

    @GetMapping("/mie")
    public String getMiePrenotazioni(Model model, Authentication authentication) {
        Utente utente = getUtenteLoggato(authentication);
        if (utente == null) return "redirect:/login";

        List<Prenotazione> prenotazioni = prenotazioneService.findByUtente(utente);
        model.addAttribute("prenotazioni", prenotazioni);
        return "prenotazione/mie-prenotazioni";
    }

    @PostMapping("/effettua")
    public String effettuaPrenotazione(
            @RequestParam Long viaggioId,
            @RequestParam Integer numeroPartecipanti,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Utente utente = getUtenteLoggato(authentication);
        if (utente == null) return "redirect:/login";

        try {
            Prenotazione prenotazione = prenotazioneService.effettuaPrenotazione(viaggioId, numeroPartecipanti, utente);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Prenotazione confermata con successo! Codice prenotazione: " + prenotazione.getCodicePrenotazione());
            return "redirect:/prenotazioni/conferma/" + prenotazione.getId();
        } catch (PostiInsufficientiException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/viaggi/" + viaggioId;
        }
    }

    @GetMapping("/conferma/{id}")
    public String mostraConferma(@PathVariable Long id, Model model, Authentication authentication) {
        Utente utente = getUtenteLoggato(authentication);
        Prenotazione prenotazione = prenotazioneService.findById(id);

        if (prenotazione == null || utente == null || !prenotazione.getUtente().getId().equals(utente.getId())) {
            return "redirect:/viaggi";
        }

        model.addAttribute("prenotazione", prenotazione);
        return "prenotazione/conferma";
    }

    @PostMapping("/{id}/annulla")
    public String annullaPrenotazione(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Utente utente = getUtenteLoggato(authentication);
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        prenotazioneService.annullaPrenotazione(id, utente, isAdmin);
        redirectAttributes.addFlashAttribute("successMessage", "Prenotazione annullata con successo. I posti sono stati ripristinati.");
        return "redirect:/prenotazioni/mie";
    }
}
