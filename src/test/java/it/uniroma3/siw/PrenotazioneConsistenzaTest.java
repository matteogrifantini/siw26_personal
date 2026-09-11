package it.uniroma3.siw;

import it.uniroma3.siw.exception.PostiInsufficientiException;
import it.uniroma3.siw.model.Destinazione;
import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Viaggio;
import it.uniroma3.siw.repository.DestinazioneRepository;
import it.uniroma3.siw.repository.PrenotazioneRepository;
import it.uniroma3.siw.repository.UtenteRepository;
import it.uniroma3.siw.repository.ViaggioRepository;
import it.uniroma3.siw.service.PrenotazioneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PrenotazioneConsistenzaTest {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private ViaggioRepository viaggioRepository;

    @Autowired
    private DestinazioneRepository destinazioneRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    private Viaggio viaggioTest;
    private Utente utenteTest;

    @BeforeEach
    public void setup() {
        Destinazione dest = destinazioneRepository.save(
                new Destinazione("Londra", "Regno Unito", "Europa", "Descrizione", "https://img.jpg")
        );

        viaggioTest = viaggioRepository.save(new Viaggio(
                "Londra Express", "Weekend a Londra", 3, 250.0,
                LocalDate.now().plusDays(20), LocalDate.now().plusDays(23),
                5, dest, "https://img.jpg"
        ));

        utenteTest = utenteRepository.save(
                new Utente("Test", "User", "test" + System.currentTimeMillis() + "@test.it", "123")
        );
    }

    @Test
    public void testPrenotazioneSuccessoEDecrementoPosti() {
        // Prenotiamo per 3 persone su 5 posti disponibili
        Prenotazione p = prenotazioneService.effettuaPrenotazione(viaggioTest.getId(), 3, utenteTest);

        assertNotNull(p.getId());
        assertNotNull(p.getCodicePrenotazione());
        assertEquals(3, p.getNumeroPartecipanti());
        assertEquals(750.0, p.getPrezzoTotale()); // 250 * 3

        // Verifichiamo che i posti disponibili nel viaggio siano scesi da 5 a 2
        Viaggio aggiornato = viaggioRepository.findById(viaggioTest.getId()).orElseThrow();
        assertEquals(2, aggiornato.getPostiDisponibili());
    }

    @Test
    public void testPostiInsufficientiBloccoTransazione() {
        // Proviamo a prenotare 6 posti quando ce ne sono solo 5 disponibili
        assertThrows(PostiInsufficientiException.class, () -> {
            prenotazioneService.effettuaPrenotazione(viaggioTest.getId(), 6, utenteTest);
        });

        // Verifichiamo che i posti disponibili siano rimasti intatti (pari a 5)
        Viaggio nonModificato = viaggioRepository.findById(viaggioTest.getId()).orElseThrow();
        assertEquals(5, nonModificato.getPostiDisponibili());
    }

    @Test
    public void testAnnullamentoPrenotazioneERipristinoPosti() {
        Prenotazione p = prenotazioneService.effettuaPrenotazione(viaggioTest.getId(), 2, utenteTest);

        // Ora i posti sono 3
        Viaggio intermedio = viaggioRepository.findById(viaggioTest.getId()).orElseThrow();
        assertEquals(3, intermedio.getPostiDisponibili());

        // Annullamento da parte dell'utente
        prenotazioneService.annullaPrenotazione(p.getId(), utenteTest, false);

        // I posti devono essere tornati a 5
        Viaggio ripristinato = viaggioRepository.findById(viaggioTest.getId()).orElseThrow();
        assertEquals(5, ripristinato.getPostiDisponibili());
    }
}
