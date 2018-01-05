package punteggi.giocatore;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeleteTest {
    
    private String giocatore;
    private WebTarget punteggi;
    
    public DeleteTest() { 
        // Assegnamento nome giocatore default
        this.giocatore = "testDeleteGiocatore";;
        
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        this.punteggi = cli.target("http://localhost:56476/punteggi");
    }
        
    @Test
    public void testDeleteOk() {    
        // Creazione del punteggio relativo a "giocatore"
        punteggi.queryParam("giocatore", giocatore)
                .request()
                .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Eliminazione del punteggio relativo a "giocatore"
        Response rDelete = punteggi.path("/"+giocatore)
                            .request()
                            .delete();
        
        // Verifica che la risposta sia "200 Ok"
        assertEquals(Status.OK.getStatusCode(), rDelete.getStatus());
    }
    
    @Test
    public void testDeleteNotFound() {
        // Tentativo di reperimento di una risorsa non esistente
        Response rDelete = punteggi.path("/"+giocatore+giocatore)
                            .request()
                            .delete();

        // Verifica che la risposta sia "404 Not Found"
        assertEquals(Status.NOT_FOUND.getStatusCode(), rDelete.getStatus());       
    }
}
