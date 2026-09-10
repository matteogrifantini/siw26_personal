package it.uniroma3.siw.controller;

import it.uniroma3.siw.dto.DestinazioneDto;
import it.uniroma3.siw.dto.ViaggioDto;
import it.uniroma3.siw.model.Destinazione;
import it.uniroma3.siw.model.Viaggio;
import it.uniroma3.siw.service.DestinazioneService;
import it.uniroma3.siw.service.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    private ViaggioService viaggioService;

    @Autowired
    private DestinazioneService destinazioneService;

    @GetMapping("/viaggi")
    public List<ViaggioDto> getAllViaggi() {
        List<Viaggio> viaggi = viaggioService.findAll();
        List<ViaggioDto> dtos = new ArrayList<>();
        for (Viaggio v : viaggi) {
            dtos.add(new ViaggioDto(v));
        }
        return dtos;
    }

    @GetMapping("/destinazioni")
    public List<DestinazioneDto> getAllDestinazioni() {
        List<Destinazione> destinazioni = destinazioneService.findAll();
        List<DestinazioneDto> dtos = new ArrayList<>();
        for (Destinazione d : destinazioni) {
            dtos.add(new DestinazioneDto(d));
        }
        return dtos;
    }
}
