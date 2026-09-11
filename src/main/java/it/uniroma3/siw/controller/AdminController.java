package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.CategoriaViaggio;
import it.uniroma3.siw.model.Destinazione;
import it.uniroma3.siw.model.Viaggio;
import it.uniroma3.siw.service.CategoriaViaggioService;
import it.uniroma3.siw.service.DestinazioneService;
import it.uniroma3.siw.service.PrenotazioneService;
import it.uniroma3.siw.service.ViaggioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ViaggioService viaggioService;

    @Autowired
    private DestinazioneService destinazioneService;

    @Autowired
    private CategoriaViaggioService categoriaService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("viaggi", viaggioService.findAll());
        model.addAttribute("destinazioni", destinazioneService.findAll());
        model.addAttribute("categorie", categoriaService.findAll());
        model.addAttribute("prenotazioni", prenotazioneService.findAll());
        return "admin/dashboard";
    }

    // GESTIONE DESTINAZIONI
    @GetMapping("/destinazioni/nuova")
    public String formNuovaDestinazione(Model model) {
        model.addAttribute("destinazione", new Destinazione());
        return "admin/formDestinazione";
    }

    @PostMapping("/destinazioni/salva")
    public String salvaDestinazione(
            @Valid @ModelAttribute("destinazione") Destinazione destinazione,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (destinazione.getId() == null && destinazioneService.exists(destinazione)) {
            bindingResult.rejectValue("nome", "error.destinazione", "Esiste già questa destinazione.");
        }

        if (bindingResult.hasErrors()) {
            return "admin/formDestinazione";
        }

        destinazioneService.save(destinazione);
        redirectAttributes.addFlashAttribute("successMessage", "Destinazione salvata con successo!");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/destinazioni/elimina/{id}")
    public String eliminaDestinazione(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        destinazioneService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Destinazione eliminata.");
        return "redirect:/admin/dashboard";
    }

    // GESTIONE CATEGORIE
    @GetMapping("/categorie/nuova")
    public String formNuovaCategoria(Model model) {
        model.addAttribute("categoria", new CategoriaViaggio());
        return "admin/formCategoria";
    }

    @PostMapping("/categorie/salva")
    public String salvaCategoria(
            @Valid @ModelAttribute("categoria") CategoriaViaggio categoria,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (categoria.getId() == null && categoriaService.exists(categoria.getNome())) {
            bindingResult.rejectValue("nome", "error.categoria", "Categoria già presente.");
        }

        if (bindingResult.hasErrors()) {
            return "admin/formCategoria";
        }

        categoriaService.save(categoria);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria salvata!");
        return "redirect:/admin/dashboard";
    }

    // GESTIONE VIAGGI
    @GetMapping("/viaggi/nuovo")
    public String formNuovoViaggio(Model model) {
        model.addAttribute("viaggio", new Viaggio());
        model.addAttribute("destinazioni", destinazioneService.findAll());
        model.addAttribute("categorie", categoriaService.findAll());
        return "admin/formViaggio";
    }

    @PostMapping("/viaggi/salva")
    public String salvaViaggio(
            @Valid @ModelAttribute("viaggio") Viaggio viaggio,
            BindingResult bindingResult,
            @RequestParam(required = false) List<Long> categoriaIds,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("destinazioni", destinazioneService.findAll());
            model.addAttribute("categorie", categoriaService.findAll());
            return "admin/formViaggio";
        }

        if (categoriaIds != null && !categoriaIds.isEmpty()) {
            viaggio.getCategorie().clear();
            for (Long catId : categoriaIds) {
                CategoriaViaggio cat = categoriaService.findById(catId);
                if (cat != null) {
                    viaggio.getCategorie().add(cat);
                }
            }
        }

        viaggioService.save(viaggio);
        redirectAttributes.addFlashAttribute("successMessage", "Pacchetto viaggio salvato con successo!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/viaggi/modifica/{id}")
    public String formModificaViaggio(@PathVariable Long id, Model model) {
        model.addAttribute("viaggio", viaggioService.findById(id));
        model.addAttribute("destinazioni", destinazioneService.findAll());
        model.addAttribute("categorie", categoriaService.findAll());
        return "admin/formViaggio";
    }

    @PostMapping("/viaggi/elimina/{id}")
    public String eliminaViaggio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        viaggioService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Viaggio eliminato dal catalogo.");
        return "redirect:/admin/dashboard";
    }
}
