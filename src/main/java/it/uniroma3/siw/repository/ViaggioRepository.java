package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.CategoriaViaggio;
import it.uniroma3.siw.model.Destinazione;
import it.uniroma3.siw.model.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {
    List<Viaggio> findByDestinazione(Destinazione destinazione);
    List<Viaggio> findByTitoloContainingIgnoreCase(String query);

    @Query("SELECT v FROM Viaggio v JOIN v.categorie c WHERE c = :categoria")
    List<Viaggio> findByCategoria(@Param("categoria") CategoriaViaggio categoria);

    @Query("SELECT v FROM Viaggio v WHERE v.prezzo <= :maxPrezzo ORDER BY v.prezzo ASC")
    List<Viaggio> findByPrezzoLessThanEqual(@Param("maxPrezzo") Double maxPrezzo);
}
