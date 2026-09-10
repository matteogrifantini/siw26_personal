package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Viaggio;
import it.uniroma3.siw.repository.RecensioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Transactional(readOnly = true)
    public List<Recensione> findByViaggio(Viaggio viaggio) {
        return recensioneRepository.findByViaggioOrderByDataRecensioneDesc(viaggio);
    }

    @Transactional(readOnly = true)
    public Optional<Recensione> findByViaggioAndAutore(Viaggio viaggio, Utente autore) {
        return recensioneRepository.findByViaggioAndAutore(viaggio, autore);
    }

    @Transactional(readOnly = true)
    public Recensione findById(Long id) {
        return recensioneRepository.findById(id).orElse(null);
    }

    @Transactional
    public Recensione aggiungiRecensione(Viaggio viaggio, Utente autore, String titolo, Integer voto, String commento) {
        if (recensioneRepository.existsByViaggioAndAutore(viaggio, autore)) {
            throw new IllegalStateException("Hai già inserito una recensione per questo viaggio.");
        }

        Recensione recensione = new Recensione(titolo, voto, commento, LocalDate.now(), viaggio, autore);
        return recensioneRepository.save(recensione);
    }

    @Transactional
    public Recensione modificaRecensione(Long id, Integer nuovoVoto, String nuovoTitolo, String nuovoCommento, Utente utenteRichiedente) {
        Recensione recensione = findById(id);
        if (recensione == null) {
            return null;
        }

        if (!recensione.getAutore().getId().equals(utenteRichiedente.getId())) {
            throw new IllegalStateException("Non hai i permessi per modificare questa recensione.");
        }

        recensione.setVoto(nuovoVoto);
        recensione.setTitolo(nuovoTitolo);
        recensione.setCommento(nuovoCommento);
        recensione.setDataRecensione(LocalDate.now());

        return recensioneRepository.save(recensione);
    }

    @Transactional
    public void eliminaRecensione(Long id, Utente utenteRichiedente, boolean isAdmin) {
        Recensione recensione = findById(id);
        if (recensione == null) {
            return;
        }

        if (!isAdmin && !recensione.getAutore().getId().equals(utenteRichiedente.getId())) {
            throw new IllegalStateException("Non hai i permessi per eliminare questa recensione.");
        }

        recensioneRepository.delete(recensione);
    }
}
