package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.CategoriaViaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaViaggioRepository extends JpaRepository<CategoriaViaggio, Long> {
    boolean existsByNome(String nome);
    Optional<CategoriaViaggio> findByNome(String nome);
}
