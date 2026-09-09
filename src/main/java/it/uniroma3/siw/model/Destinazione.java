package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "destinazione")
public class Destinazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome della destinazione è obbligatorio")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Il paese è obbligatorio")
    @Column(nullable = false)
    private String paese;

    @NotBlank(message = "Il continente è obbligatorio")
    @Column(nullable = false)
    private String continente;

    @Column(length = 2000)
    private String descrizione;

    private String immagineUrl;

    public Destinazione() {
    }

    public Destinazione(String nome, String paese, String continente, String descrizione, String immagineUrl) {
        this.nome = nome;
        this.paese = paese;
        this.continente = continente;
        this.descrizione = descrizione;
        this.immagineUrl = immagineUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPaese() { return paese; }
    public void setPaese(String paese) { this.paese = paese; }

    public String getContinente() { return continente; }
    public void setContinente(String continente) { this.continente = continente; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getImmagineUrl() { return immagineUrl; }
    public void setImmagineUrl(String immagineUrl) { this.immagineUrl = immagineUrl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destinazione that = (Destinazione) o;
        return Objects.equals(nome, that.nome) && Objects.equals(paese, that.paese);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, paese);
    }
}
