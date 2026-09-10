package it.uniroma3.siw.dto;

import it.uniroma3.siw.model.Destinazione;

public class DestinazioneDto {
    private Long id;
    private String nome;
    private String paese;
    private String continente;

    public DestinazioneDto() {
    }

    public DestinazioneDto(Destinazione d) {
        if (d != null) {
            this.id = d.getId();
            this.nome = d.getNome();
            this.paese = d.getPaese();
            this.continente = d.getContinente();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPaese() { return paese; }
    public void setPaese(String paese) { this.paese = paese; }

    public String getContinente() { return continente; }
    public void setContinente(String continente) { this.continente = continente; }
}
