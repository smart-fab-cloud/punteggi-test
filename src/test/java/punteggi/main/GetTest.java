package punteggi.main;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.Test;

public class GetTest {
    
    private WebTarget punteggi;
    
    public GetTest() { 
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        this.punteggi = cli.target("http://localhost:56476/punteggi");
    }    
    
    @Test
    public void testGetNotAllowed() {
        // Richiesta di Get sulla risorsa principale
        Response rGet = punteggi.request().get();
        
        // Verifica che la risposta sia "405 Not Allowed"
        assertEquals(405, rGet.getStatus());
    }
}
