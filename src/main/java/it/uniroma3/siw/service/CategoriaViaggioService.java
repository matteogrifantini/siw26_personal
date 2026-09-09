package it.uniroma3.siw.service;

import it.uniroma3.siw.model.CategoriaViaggio;
import it.uniroma3.siw.repository.CategoriaViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaViaggioService {

    @Autowired
    private CategoriaViaggioRepository categoriaViaggioRepository;

    @Transactional(readOnly = true)
    public List<CategoriaViaggio> findAll() {
        return categoriaViaggioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CategoriaViaggio findById(Long id) {
        return categoriaViaggioRepository.findById(id).orElse(null);
    }

    @Transactional
    public CategoriaViaggio save(CategoriaViaggio categoria) {
        return categoriaViaggioRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public boolean exists(String nome) {
        return categoriaViaggioRepository.existsByNome(nome);
    }

    @Transactional
    public void deleteById(Long id) {
        categoriaViaggioRepository.deleteById(id);
    }
}
