package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Destinazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinazioneRepository extends JpaRepository<Destinazione, Long> {
    List<Destinazione> findAllByOrderByNomeAsc();
    boolean existsByNomeAndPaese(String nome, String paese);
}
