package it.uniroma3.siw.service;

import it.uniroma3.siw.exception.PostiInsufficientiException;
import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.StatoPrenotazione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Viaggio;
import it.uniroma3.siw.repository.PrenotazioneRepository;
import it.uniroma3.siw.repository.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private ViaggioRepository viaggioRepository;

    @Transactional(readOnly = true)
    public List<Prenotazione> findAll() {
        return prenotazioneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Prenotazione> findByUtente(Utente utente) {
        return prenotazioneRepository.findByUtenteOrderByDataPrenotazioneDesc(utente);
    }

    @Transactional(readOnly = true)
    public Prenotazione findById(Long id) {
        return prenotazioneRepository.findById(id).orElse(null);
    }

    @Transactional
    public Prenotazione effettuaPrenotazione(Long viaggioId, Integer partecipanti, Utente utente) {
        Viaggio viaggio = viaggioRepository.findById(viaggioId).orElse(null);
        if (viaggio == null) {
            throw new IllegalArgumentException("Viaggio non trovato con id: " + viaggioId);
        }

        if (partecipanti == null || partecipanti <= 0) {
            throw new IllegalArgumentException("Il numero di partecipanti deve essere almeno 1.");
        }

        if (viaggio.getPostiDisponibili() < partecipanti) {
            throw new PostiInsufficientiException(
                    "Posti insufficienti per questo viaggio. Posti rimasti: " + viaggio.getPostiDisponibili());
        }

        viaggio.setPostiDisponibili(viaggio.getPostiDisponibili() - partecipanti);
        viaggioRepository.save(viaggio);

        Double prezzoTotale = viaggio.getPrezzo() * partecipanti;
        String codice = "TRV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Prenotazione prenotazione = new Prenotazione(
                codice,
                partecipanti,
                prezzoTotale,
                LocalDate.now(),
                viaggio,
                utente
        );

        return prenotazioneRepository.save(prenotazione);
    }

    @Transactional
    public void annullaPrenotazione(Long prenotazioneId, Utente utenteRichiedente, boolean isAdmin) {
        Prenotazione prenotazione = findById(prenotazioneId);
        if (prenotazione == null) {
            return;
        }

        if (!isAdmin && !prenotazione.getUtente().getId().equals(utenteRichiedente.getId())) {
            throw new IllegalStateException("Non sei autorizzato ad annullare questa prenotazione.");
        }

        if (prenotazione.getStato() == StatoPrenotazione.CANCELLATA) {
            return;
        }

        Viaggio viaggio = prenotazione.getViaggio();
        viaggio.setPostiDisponibili(viaggio.getPostiDisponibili() + prenotazione.getNumeroPartecipanti());
        viaggioRepository.save(viaggio);

        prenotazione.setStato(StatoPrenotazione.CANCELLATA);
        prenotazioneRepository.save(prenotazione);
    }
}
