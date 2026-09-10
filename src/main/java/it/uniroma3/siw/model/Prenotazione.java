package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "prenotazione")
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codicePrenotazione;

    @NotNull(message = "Il numero di partecipanti è obbligatorio")
    @Min(value = 1, message = "Devi prenotare per almeno 1 partecipante")
    @Column(nullable = false)
    private Integer numeroPartecipanti;

    @Column(nullable = false)
    private Double prezzoTotale;

    @Column(nullable = false)
    private LocalDate dataPrenotazione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoPrenotazione stato = StatoPrenotazione.CONFERMATA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    public Prenotazione() {
    }

    public Prenotazione(String codicePrenotazione, Integer numeroPartecipanti, Double prezzoTotale, 
                        LocalDate dataPrenotazione, Viaggio viaggio, Utente utente) {
        this.codicePrenotazione = codicePrenotazione;
        this.numeroPartecipanti = numeroPartecipanti;
        this.prezzoTotale = prezzoTotale;
        this.dataPrenotazione = dataPrenotazione;
        this.viaggio = viaggio;
        this.utente = utente;
        this.stato = StatoPrenotazione.CONFERMATA;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodicePrenotazione() { return codicePrenotazione; }
    public void setCodicePrenotazione(String codicePrenotazione) { this.codicePrenotazione = codicePrenotazione; }

    public Integer getNumeroPartecipanti() { return numeroPartecipanti; }
    public void setNumeroPartecipanti(Integer numeroPartecipanti) { this.numeroPartecipanti = numeroPartecipanti; }

    public Double getPrezzoTotale() { return prezzoTotale; }
    public void setPrezzoTotale(Double prezzoTotale) { this.prezzoTotale = prezzoTotale; }

    public LocalDate getDataPrenotazione() { return dataPrenotazione; }
    public void setDataPrenotazione(LocalDate dataPrenotazione) { this.dataPrenotazione = dataPrenotazione; }

    public StatoPrenotazione getStato() { return stato; }
    public void setStato(StatoPrenotazione stato) { this.stato = stato; }

    public Viaggio getViaggio() { return viaggio; }
    public void setViaggio(Viaggio viaggio) { this.viaggio = viaggio; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prenotazione that = (Prenotazione) o;
        return Objects.equals(codicePrenotazione, that.codicePrenotazione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codicePrenotazione);
    }
}
