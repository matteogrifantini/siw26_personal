package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByUtenteOrderByDataPrenotazioneDesc(Utente utente);
    Optional<Prenotazione> findByCodicePrenotazione(String codicePrenotazione);
    List<Prenotazione> findByViaggio(Viaggio viaggio);
}
