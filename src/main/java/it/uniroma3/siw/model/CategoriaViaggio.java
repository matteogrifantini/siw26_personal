package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categoria_viaggio")
public class CategoriaViaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome della categoria è obbligatorio")
    @Column(nullable = false, unique = true)
    private String nome;

    @Column(length = 1000)
    private String descrizione;

    @ManyToMany(mappedBy = "categorie")
    private List<Viaggio> viaggi = new ArrayList<>();

    public CategoriaViaggio() {
    }

    public CategoriaViaggio(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public List<Viaggio> getViaggi() { return viaggi; }
    public void setViaggi(List<Viaggio> viaggi) { this.viaggi = viaggi; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaViaggio that = (CategoriaViaggio) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}
