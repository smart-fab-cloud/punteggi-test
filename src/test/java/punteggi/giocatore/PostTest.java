package punteggi.giocatore;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PostTest {
    
    private String giocatore;
    private WebTarget punteggi;
    
    public PostTest() { 
        // Assegnamento nome giocatore default
        this.giocatore = "testPostGiocatore";;
        
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        this.punteggi = cli.target("http://localhost:56476/punteggi");
    }
    
    @Before
    public void creaGiocatore() {
        // Creazione del punteggio relativo a "giocatore"
        punteggi.queryParam("giocatore", giocatore)
                .request()
                .post(Entity.entity("", MediaType.TEXT_PLAIN));
    }
    
    @Test
    public void testPostNotAllowed() {    
        // Tentativo di POST sulla risorsa relativa a "giocatore"
        Response rPost = punteggi.path("/"+giocatore)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta sia "405 Not Allowed"
        assertEquals(405, rPost.getStatus());
    }
    
    @After
    public void eliminaGiocatore() {
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();
    } 
    
}
