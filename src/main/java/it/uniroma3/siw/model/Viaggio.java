package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "viaggio")
public class Viaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il titolo del viaggio è obbligatorio")
    @Column(nullable = false)
    private String titolo;

    @Column(length = 3000)
    private String descrizione;

    @NotNull(message = "La durata in giorni è obbligatoria")
    @Min(value = 1, message = "La durata deve essere di almeno 1 giorno")
    @Column(nullable = false)
    private Integer durataGiorni;

    @NotNull(message = "Il prezzo è obbligatorio")
    @DecimalMin(value = "0.01", message = "Il prezzo deve essere maggiore di zero")
    @Column(nullable = false)
    private Double prezzo;

    @NotNull(message = "La data di partenza è obbligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dataInizio;

    @NotNull(message = "La data di ritorno è obbligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dataFine;

    @NotNull(message = "Il numero di posti disponibili è obbligatorio")
    @Min(value = 0, message = "I posti disponibili non possono essere negativi")
    @Column(nullable = false)
    private Integer postiDisponibili;

    private String immagineUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinazione_id")
    private Destinazione destinazione;

    @ManyToMany
    @JoinTable(
        name = "viaggio_categoria",
        joinColumns = @JoinColumn(name = "viaggio_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<CategoriaViaggio> categorie = new ArrayList<>();

    public Viaggio() {
    }

    public Viaggio(String titolo, String descrizione, Integer durataGiorni, Double prezzo, 
                   LocalDate dataInizio, LocalDate dataFine, Integer postiDisponibili, 
                   Destinazione destinazione, String immagineUrl) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.durataGiorni = durataGiorni;
        this.prezzo = prezzo;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.postiDisponibili = postiDisponibili;
        this.destinazione = destinazione;
        this.immagineUrl = immagineUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public Integer getDurataGiorni() { return durataGiorni; }
    public void setDurataGiorni(Integer durataGiorni) { this.durataGiorni = durataGiorni; }

    public Double getPrezzo() { return prezzo; }
    public void setPrezzo(Double prezzo) { this.prezzo = prezzo; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }

    public Integer getPostiDisponibili() { return postiDisponibili; }
    public void setPostiDisponibili(Integer postiDisponibili) { this.postiDisponibili = postiDisponibili; }

    public String getImmagineUrl() { return immagineUrl; }
    public void setImmagineUrl(String immagineUrl) { this.immagineUrl = immagineUrl; }

    public Destinazione getDestinazione() { return destinazione; }
    public void setDestinazione(Destinazione destinazione) { this.destinazione = destinazione; }

    public List<CategoriaViaggio> getCategorie() { return categorie; }
    public void setCategorie(List<CategoriaViaggio> categorie) { this.categorie = categorie; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Viaggio that = (Viaggio) o;
        return Objects.equals(titolo, that.titolo) && Objects.equals(dataInizio, that.dataInizio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo, dataInizio);
    }
}
