package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecensioneRepository extends JpaRepository<Recensione, Long> {
    List<Recensione> findByViaggioOrderByDataRecensioneDesc(Viaggio viaggio);
    Optional<Recensione> findByViaggioAndAutore(Viaggio viaggio, Utente autore);
    boolean existsByViaggioAndAutore(Viaggio viaggio, Utente autore);
}
