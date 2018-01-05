package punteggi.main;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

public class PutTest {
    
    private WebTarget punteggi;
    
    public PutTest() { 
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        this.punteggi = cli.target("http://localhost:56476/punteggi");
    }    
    
    @Test
    public void testPutNotAllowed() {
        // Richiesta di Put sulla risorsa principale
        Response rPut = punteggi.request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta sia "405 Not Allowed"
        assertEquals(405, rPut.getStatus());
    }
}
