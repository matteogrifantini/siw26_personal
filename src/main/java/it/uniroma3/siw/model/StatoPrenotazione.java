package it.uniroma3.siw.model;

public enum StatoPrenotazione {
    CONFERMATA("Confermata"),
    CANCELLATA("Cancellata");

    private final String etichetta;

    StatoPrenotazione(String etichetta) {
        this.etichetta = etichetta;
    }

    public String getEtichetta() {
        return etichetta;
    }
}
