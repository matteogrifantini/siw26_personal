package it.uniroma3.siw.init;

import it.uniroma3.siw.model.*;
import it.uniroma3.siw.repository.*;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private DestinazioneRepository destinazioneRepository;

    @Autowired
    private CategoriaViaggioRepository categoriaRepository;

    @Autowired
    private ViaggioRepository viaggioRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (destinazioneRepository.count() > 0) {
            return;
        }

        // 1. Utenti e Credenziali
        Utente adminUtente = new Utente("Mario", "Rossi", "admin@siwtravel.it", "+39 333 1234567");
        Credentials adminCreds = new Credentials("admin", "admin", Credentials.ADMIN_ROLE, adminUtente);
        credentialsService.saveAdminCredentials(adminCreds);

        Utente userUtente = new Utente("Luca", "Bianchi", "user@siwtravel.it", "+39 340 7654321");
        Credentials userCreds = new Credentials("user", "user", Credentials.DEFAULT_ROLE, userUtente);
        credentialsService.saveCredentials(userCreds);

        // 2. Destinazioni
        Destinazione tokyo = new Destinazione("Tokyo", "Giappone", "Asia", 
                "Metropoli affascinante tra templi antichi, quartieri futuristici e gastronomia unica.", 
                "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=600");
        Destinazione maldive = new Destinazione("Atollo di Malé", "Maldive", "Asia", 
                "Spiagge bianche incontaminate, mare cristallino e barriere coralline mozzafiato.", 
                "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?w=600");
        Destinazione parigi = new Destinazione("Parigi", "Francia", "Europa", 
                "La Ville Lumière, celebre per la Torre Eiffel, il Louvre e le passeggiate sulla Senna.", 
                "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=600");
        Destinazione newyork = new Destinazione("New York", "Stati Uniti", "America", 
                "La città che non dorme mai: Times Square, Central Park, Broadway e musei imperdibili.", 
                "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=600");
        Destinazione cairo = new Destinazione("Il Cairo", "Egitto", "Africa", 
                "La magia delle Grandi Piramidi di Giza, la Sfinge e il fascino millenario del fiume Nilo.", 
                "https://images.unsplash.com/photo-1539650116574-8efeb43e2750?w=600");

        destinazioneRepository.saveAll(Arrays.asList(tokyo, maldive, parigi, newyork, cairo));

        // 3. Categorie Viaggio
        CategoriaViaggio culturale = new CategoriaViaggio("Tour Culturale", "Itinerari ricchi di storia, musei e monumenti storici.");
        CategoriaViaggio relax = new CategoriaViaggio("Mare & Relax", "Vacanze all'insegna della tranquillità su spiagge paradisiache.");
        CategoriaViaggio avventura = new CategoriaViaggio("Avventura & Natura", "Esperienze all'aperto, trekking e contatto con la natura.");
        CategoriaViaggio cityBreak = new CategoriaViaggio("City Break", "Weekend brevi ed intensi alla scoperta delle capitali mondiali.");

        categoriaRepository.saveAll(Arrays.asList(culturale, relax, avventura, cityBreak));

        // 4. Viaggi (Pacchetti E-commerce)
        Viaggio v1 = new Viaggio(
                "Giappone: Sakura e Tradizione",
                "Uno splendido itinerario di 10 giorni alla scoperta di Tokyo, Kyoto e il Monte Fuji nel periodo della fioritura.",
                10,
                1890.00,
                LocalDate.of(2026, 10, 15),
                LocalDate.of(2026, 10, 25),
                14,
                tokyo,
                "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=600"
        );
        v1.getCategorie().addAll(Arrays.asList(culturale, avventura));

        Viaggio v2 = new Viaggio(
                "Sogno alle Maldive: Relax Totale",
                "Soggiorno di lusso in overwater villa con trattamento all-inclusive, snorkeling ed escursioni in barca.",
                7,
                1450.00,
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 8),
                8,
                maldive,
                "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?w=600"
        );
        v2.getCategorie().add(relax);

        Viaggio v3 = new Viaggio(
                "Weekend Romantico a Parigi",
                "3 giorni nella città dell'amore con crociera privata sulla Senna e ingresso prioritario al Museo del Louvre.",
                3,
                380.00,
                LocalDate.of(2026, 9, 20),
                LocalDate.of(2026, 9, 23),
                18,
                parigi,
                "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=600"
        );
        v3.getCategorie().addAll(Arrays.asList(cityBreak, culturale));

        Viaggio v4 = new Viaggio(
                "New York City Experience",
                "La Grande Mela a 360 gradi: tour dei quartieri storici, musical a Broadway e vista panoramica dall'Empire State Building.",
                6,
                1290.00,
                LocalDate.of(2026, 10, 5),
                LocalDate.of(2026, 10, 11),
                12,
                newyork,
                "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=600"
        );
        v4.getCategorie().addAll(Arrays.asList(cityBreak, avventura));

        Viaggio v5 = new Viaggio(
                "I Tesori dei Faraoni sul Nilo",
                "Crociera guidata sul Nilo con visita alle Piramidi di Giza, Luxor, Karnak e Valle dei Re con egittologo parlante italiano.",
                8,
                980.00,
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 18),
                15,
                cairo,
                "https://images.unsplash.com/photo-1539650116574-8efeb43e2750?w=600"
        );
        v5.getCategorie().add(culturale);

        viaggioRepository.saveAll(Arrays.asList(v1, v2, v3, v4, v5));

        // 5. Prenotazione di prova per 'user'
        Prenotazione pren = new Prenotazione(
                "TRV-DEMO1234",
                2,
                760.00,
                LocalDate.now().minusDays(5),
                v3,
                userUtente
        );
        prenotazioneRepository.save(pren);

        // 6. Recensione di prova
        Recensione rec = new Recensione(
                "Esperienza indimenticabile!",
                5,
                "Parigi è meravigliosa e l'organizzazione dell'itinerario è stata impeccabile. Hotel centrale e guida gentilissima.",
                LocalDate.now().minusDays(2),
                v3,
                userUtente
        );
        recensioneRepository.save(rec);
    }
}
