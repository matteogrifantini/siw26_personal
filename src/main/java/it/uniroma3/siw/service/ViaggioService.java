package it.uniroma3.siw.service;

import it.uniroma3.siw.model.CategoriaViaggio;
import it.uniroma3.siw.model.Destinazione;
import it.uniroma3.siw.model.Viaggio;
import it.uniroma3.siw.repository.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViaggioService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    @Transactional(readOnly = true)
    public List<Viaggio> findAll() {
        return viaggioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Viaggio findById(Long id) {
        return viaggioRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Viaggio> findByDestinazione(Destinazione destinazione) {
        return viaggioRepository.findByDestinazione(destinazione);
    }

    @Transactional(readOnly = true)
    public List<Viaggio> findByCategoria(CategoriaViaggio categoria) {
        return viaggioRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public List<Viaggio> searchByTitolo(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }
        return viaggioRepository.findByTitoloContainingIgnoreCase(query.trim());
    }

    @Transactional(readOnly = true)
    public List<Viaggio> filterByPrezzoMax(Double maxPrezzo) {
        if (maxPrezzo == null) {
            return findAll();
        }
        return viaggioRepository.findByPrezzoLessThanEqual(maxPrezzo);
    }

    @Transactional
    public Viaggio save(Viaggio viaggio) {
        return viaggioRepository.save(viaggio);
    }

    @Transactional
    public void deleteById(Long id) {
        viaggioRepository.deleteById(id);
    }
}
