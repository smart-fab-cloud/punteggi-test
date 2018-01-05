package punteggi.main;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.Test;

public class PostTest {
    
    private WebTarget punteggi;
    
    public PostTest() { 
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        this.punteggi = cli.target("http://localhost:56476/punteggi");
    }
    
    @Test
    public void testPostCreated() {
        String giocatore = "giocatorePerUnitTestPostCreated";
        
        // Creazione del punteggio relativo a "giocatore"
        Response rPost = punteggi.queryParam("giocatore", giocatore)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta ottenuta sia "201 Created"
        assertEquals(Status.CREATED.getStatusCode(), rPost.getStatus());
        
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();   
    }
    
    @Test
    public void testPostBadRequest() {
        Response rPost = punteggi
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta ottenuta sia "400 Bad Request"
        assertEquals(Status.BAD_REQUEST.getStatusCode(), rPost.getStatus());
    }
    
    @Test
    public void testPostConflict() {
        String giocatore = "giocatorePerUnitTestPostConflict";
        
        // Creazione del punteggio relativo a "giocatore"
        punteggi.queryParam("giocatore", giocatore)
                .request()
                .post(Entity.entity("", MediaType.TEXT_PLAIN));

        // Ripetizione della creazione del punteggio relativo a "giocatore"
        Response rPost = punteggi.queryParam("giocatore", giocatore)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));        

        // Verifica che la risposta ottenuta sia "409 Conflict"
        assertEquals(Status.CONFLICT.getStatusCode(), rPost.getStatus());
        
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();
    }
    
}

