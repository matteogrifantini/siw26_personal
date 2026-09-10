package it.uniroma3.siw.dto;

import it.uniroma3.siw.model.Viaggio;

public class ViaggioDto {
    private Long id;
    private String titolo;
    private Double prezzo;
    private Integer durataGiorni;
    private Integer postiDisponibili;
    private String destinazioneNome;
    private String paese;
    private String continente;
    private String dataInizio;
    private String dataFine;
    private String immagineUrl;
    private double mediaVoti;

    public ViaggioDto() {
    }

    public ViaggioDto(Viaggio v) {
        if (v != null) {
            this.id = v.getId();
            this.titolo = v.getTitolo();
            this.prezzo = v.getPrezzo();
            this.durataGiorni = v.getDurataGiorni();
            this.postiDisponibili = v.getPostiDisponibili();
            this.dataInizio = v.getDataInizio() != null ? v.getDataInizio().toString() : "";
            this.dataFine = v.getDataFine() != null ? v.getDataFine().toString() : "";
            this.immagineUrl = v.getImmagineUrl();
            this.mediaVoti = v.getMediaVoti();

            if (v.getDestinazione() != null) {
                this.destinazioneNome = v.getDestinazione().getNome();
                this.paese = v.getDestinazione().getPaese();
                this.continente = v.getDestinazione().getContinente();
            }
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public Double getPrezzo() { return prezzo; }
    public void setPrezzo(Double prezzo) { this.prezzo = prezzo; }

    public Integer getDurataGiorni() { return durataGiorni; }
    public void setDurataGiorni(Integer durataGiorni) { this.durataGiorni = durataGiorni; }

    public Integer getPostiDisponibili() { return postiDisponibili; }
    public void setPostiDisponibili(Integer postiDisponibili) { this.postiDisponibili = postiDisponibili; }

    public String getDestinazioneNome() { return destinazioneNome; }
    public void setDestinazioneNome(String destinazioneNome) { this.destinazioneNome = destinazioneNome; }

    public String getPaese() { return paese; }
    public void setPaese(String paese) { this.paese = paese; }

    public String getContinente() { return continente; }
    public void setContinente(String continente) { this.continente = continente; }

    public String getDataInizio() { return dataInizio; }
    public void setDataInizio(String dataInizio) { this.dataInizio = dataInizio; }

    public String getDataFine() { return dataFine; }
    public void setDataFine(String dataFine) { this.dataFine = dataFine; }

    public String getImmagineUrl() { return immagineUrl; }
    public void setImmagineUrl(String immagineUrl) { this.immagineUrl = immagineUrl; }

    public double getMediaVoti() { return mediaVoti; }
    public void setMediaVoti(double mediaVoti) { this.mediaVoti = mediaVoti; }
}
