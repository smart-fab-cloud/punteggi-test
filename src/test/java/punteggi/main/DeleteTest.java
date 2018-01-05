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

public class DeleteTest {
    
    private WebTarget punteggi;
    
    public DeleteTest() { 
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        this.punteggi = cli.target("http://localhost:56476/punteggi");
    }    
    
    @Test
    public void testDeleteNotAllowed() {
        // Richiesta di Delete sulla risorsa principale
        Response rDelete = punteggi.request().delete();
        
        // Verifica che la risposta sia "405 Not Allowed"
        assertEquals(405, rDelete.getStatus());
    }
}
