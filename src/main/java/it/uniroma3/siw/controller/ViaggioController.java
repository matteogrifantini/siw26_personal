package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.CategoriaViaggio;
import it.uniroma3.siw.model.Viaggio;
import it.uniroma3.siw.service.CategoriaViaggioService;
import it.uniroma3.siw.service.DestinazioneService;
import it.uniroma3.siw.service.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ViaggioController {

    @Autowired
    private ViaggioService viaggioService;

    @Autowired
    private DestinazioneService destinazioneService;

    @Autowired
    private CategoriaViaggioService categoriaService;

    @GetMapping("/viaggi")
    public String getCatalogoViaggi(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Double maxPrezzo,
            Model model) {

        List<Viaggio> viaggi;

        if (q != null && !q.trim().isEmpty()) {
            viaggi = viaggioService.searchByTitolo(q);
            model.addAttribute("query", q);
        } else if (categoriaId != null) {
            CategoriaViaggio categoria = categoriaService.findById(categoriaId);
            viaggi = viaggioService.findByCategoria(categoria);
            model.addAttribute("selectedCategoriaId", categoriaId);
        } else if (maxPrezzo != null) {
            viaggi = viaggioService.filterByPrezzoMax(maxPrezzo);
            model.addAttribute("selectedMaxPrezzo", maxPrezzo);
        } else {
            viaggi = viaggioService.findAll();
        }

        model.addAttribute("viaggi", viaggi);
        model.addAttribute("destinazioni", destinazioneService.findAll());
        model.addAttribute("categorie", categoriaService.findAll());

        return "viaggio/viaggi";
    }

    @GetMapping("/viaggi/{id}")
    public String getDettaglioViaggio(@PathVariable Long id, Model model) {
        Viaggio viaggio = viaggioService.findById(id);
        if (viaggio == null) {
            return "redirect:/viaggi";
        }
        model.addAttribute("viaggio", viaggio);
        return "viaggio/viaggio";
    }
}
