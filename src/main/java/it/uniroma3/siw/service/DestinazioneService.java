package it.uniroma3.siw.service;
 
import it.uniroma3.siw.model.Destinazione;
import it.uniroma3.siw.repository.DestinazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DestinazioneService {

    @Autowired
    private DestinazioneRepository destinazioneRepository;

    @Transactional(readOnly = true)
    public List<Destinazione> findAll() {
        return destinazioneRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public Destinazione findById(Long id) {
        return destinazioneRepository.findById(id).orElse(null);
    }

    @Transactional
    public Destinazione save(Destinazione destinazione) {
        return destinazioneRepository.save(destinazione);
    }

    @Transactional(readOnly = true)
    public boolean exists(Destinazione destinazione) {
        return destinazioneRepository.existsByNomeAndPaese(destinazione.getNome(), destinazione.getPaese());
    }

    @Transactional
    public void deleteById(Long id) {
        destinazioneRepository.deleteById(id);
    }
}
