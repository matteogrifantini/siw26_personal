package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
    name = "recensione",
    uniqueConstraints = @UniqueConstraint(columnNames = {"viaggio_id", "autore_id"})
)
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il titolo della recensione è obbligatorio")
    @Column(nullable = false)
    private String titolo;

    @NotNull(message = "Il voto è obbligatorio")
    @Min(value = 1, message = "Il voto minimo è 1")
    @Max(value = 5, message = "Il voto massimo è 5")
    @Column(nullable = false)
    private Integer voto;

    @NotBlank(message = "Il commento è obbligatorio")
    @Column(length = 2000, nullable = false)
    private String commento;

    @Column(nullable = false)
    private LocalDate dataRecensione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autore_id", nullable = false)
    private Utente autore;

    public Recensione() {
    }

    public Recensione(String titolo, Integer voto, String commento, LocalDate dataRecensione, Viaggio viaggio, Utente autore) {
        this.titolo = titolo;
        this.voto = voto;
        this.commento = commento;
        this.dataRecensione = dataRecensione;
        this.viaggio = viaggio;
        this.autore = autore;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public Integer getVoto() { return voto; }
    public void setVoto(Integer voto) { this.voto = voto; }

    public String getCommento() { return commento; }
    public void setCommento(String commento) { this.commento = commento; }

    public LocalDate getDataRecensione() { return dataRecensione; }
    public void setDataRecensione(LocalDate dataRecensione) { this.dataRecensione = dataRecensione; }

    public Viaggio getViaggio() { return viaggio; }
    public void setViaggio(Viaggio viaggio) { this.viaggio = viaggio; }

    public Utente getAutore() { return autore; }
    public void setAutore(Utente autore) { this.autore = autore; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recensione that = (Recensione) o;
        return Objects.equals(viaggio, that.viaggio) && Objects.equals(autore, that.autore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viaggio, autore);
    }
}
