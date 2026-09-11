const { useState, useEffect } = React;

function RicercaViaggi() {
    const [viaggi, setViaggi] = useState([]);
    const [destinazioni, setDestinazioni] = useState([]);
    const [caricamento, setCaricamento] = useState(true);
    const [errore, setErrore] = useState(null);

    // Filtri
    const [filtroTesto, setFiltroTesto] = useState("");
    const [filtroContinente, setFiltroContinente] = useState("");
    const [filtroPrezzoMax, setFiltroPrezzoMax] = useState(2500);

    useEffect(() => {
        caricaDati();
    }, []);

    function caricaDati() {
        setCaricamento(true);
        setErrore(null);

        Promise.all([
            fetch("/api/viaggi").then(res => res.json()),
            fetch("/api/destinazioni").then(res => res.json())
        ])
        .then(([dataViaggi, dataDest]) => {
            setViaggi(dataViaggi);
            setDestinazioni(dataDest);
            setCaricamento(false);
        })
        .catch(err => {
            console.error("Errore caricamento dati:", err);
            setErrore("Impossibile caricare i dati dal server.");
            setCaricamento(false);
        });
    }

    const viaggiFiltrati = viaggi.filter(v => {
        const matchTesto = filtroTesto === "" ||
            (v.titolo && v.titolo.toLowerCase().includes(filtroTesto.toLowerCase())) ||
            (v.destinazioneNome && v.destinazioneNome.toLowerCase().includes(filtroTesto.toLowerCase())) ||
            (v.paese && v.paese.toLowerCase().includes(filtroTesto.toLowerCase()));

        const matchContinente = filtroContinente === "" ||
            (v.continente && v.continente.toLowerCase() === filtroContinente.toLowerCase());

        const matchPrezzo = v.prezzo <= filtroPrezzoMax;

        return matchTesto && matchContinente && matchPrezzo;
    });

    if (caricamento) {
        return (
            <div className="text-center py-5">
                <div className="spinner-border text-primary" role="status"></div>
                <p className="mt-2 text-muted">Ricerca pacchetti vacanza in corso...</p>
            </div>
        );
    }

    if (errore) {
        return (
            <div className="alert alert-danger">
                {errore}
                <button className="btn btn-sm btn-outline-danger ms-3" onClick={caricaDati}>Riprova</button>
            </div>
        );
    }

    return (
        <div>
            {/* Box Filtri */}
            <div className="card p-3 mb-4 bg-light border-0 shadow-sm">
                <div className="row g-3 align-items-center">
                    <div className="col-md-4">
                        <label className="form-label small fw-bold mb-1">Cerca Viaggio o Città</label>
                        <input
                            type="text"
                            className="form-control form-control-sm"
                            placeholder="es. Parigi, Giappone, Safari..."
                            value={filtroTesto}
                            onChange={e => setFiltroTesto(e.target.value)}
                        />
                    </div>

                    <div className="col-md-3">
                        <label className="form-label small fw-bold mb-1">Continente</label>
                        <select
                            className="form-select form-select-sm"
                            value={filtroContinente}
                            onChange={e => setFiltroContinente(e.target.value)}
                        >
                            <option value="">Tutti i continenti</option>
                            <option value="Europa">Europa</option>
                            <option value="Asia">Asia</option>
                            <option value="America">America</option>
                            <option value="Africa">Africa</option>
                        </select>
                    </div>

                    <div className="col-md-3">
                        <label className="form-label small fw-bold mb-1">
                            Prezzo max: <strong>{filtroPrezzoMax} €</strong>
                        </label>
                        <input
                            type="range"
                            className="form-range"
                            min="200"
                            max="3000"
                            step="50"
                            value={filtroPrezzoMax}
                            onChange={e => setFiltroPrezzoMax(Number(e.target.value))}
                        />
                    </div>

                    <div className="col-md-2 text-end pt-3 pt-md-0">
                        <button
                            className="btn btn-sm btn-outline-secondary w-100"
                            onClick={() => { setFiltroTesto(""); setFiltroContinente(""); setFiltroPrezzoMax(2500); }}
                        >
                            Azzera filtri
                        </button>
                    </div>
                </div>
            </div>

            <p className="text-muted small mb-3">
                Trovati <strong>{viaggiFiltrati.length}</strong> viaggi disponibili.
            </p>

            {viaggiFiltrati.length === 0 ? (
                <div className="alert alert-secondary text-center py-4">
                    Nessun viaggio corrisponde ai filtri selezionati.
                </div>
            ) : (
                <div className="row g-4">
                    {viaggiFiltrati.map(v => (
                        <div key={v.id} className="col-md-4">
                            <div className="card h-100 shadow-sm">
                                <img
                                    src={v.immagineUrl || "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=600"}
                                    className="card-img-top"
                                    alt={v.titolo}
                                    style={{ height: "180px", objectFit: "cover" }}
                                    onError={(e) => {
                                        e.target.onerror = null;
                                        e.target.src = "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=600";
                                    }}
                                />
                                <div className="card-body d-flex flex-column">
                                    <div className="d-flex justify-content-between align-items-center mb-2">
                                        <span className="badge bg-secondary">{v.destinazioneNome}</span>
                                        <span className="badge bg-light text-dark border">{v.durataGiorni} giorni</span>
                                    </div>

                                    <h5 className="card-title fw-bold text-dark mb-1">{v.titolo}</h5>
                                    <p className="small text-muted mb-2">
                                        {v.paese} ({v.continente})
                                    </p>

                                    <div className="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                                        <div>
                                            <span className="price-tag">{v.prezzo} €</span>
                                            <small className="text-muted d-block">{v.postiDisponibili} posti rimasti</small>
                                        </div>
                                        <a href={`/viaggi/${v.id}`} className="btn btn-sm btn-outline-primary">Dettagli & Prenota</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

const rootElement = document.getElementById("react-viaggi-root");
if (rootElement) {
    ReactDOM.createRoot(rootElement).render(<RicercaViaggi />);
}
