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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetTest {
    
    private String giocatore;
    private WebTarget punteggi;
    
    public GetTest() { 
        // Assegnamento nome giocatore default
        this.giocatore = "testGetGiocatore";;
        
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
    public void testGetOk() throws ParseException {    
        // Reperimento del punteggio relativo a "giocatore"
        Response rGet = punteggi.path("/"+giocatore)
                            .request()
                            .get();
        
        // Verifica che la risposta sia "200 Ok"
        assertEquals(Status.OK.getStatusCode(), rGet.getStatus());
        // Verifica che il record reperito sia 
        // <"testGetGiocatore", 1000>
        JSONParser parser = new JSONParser();
        JSONObject p = (JSONObject) parser.parse(rGet.readEntity(String.class));
        String giocatoreCreato = (String) p.get("giocatore"); 
        Long punteggioCreato = (Long) p.get("punteggio");
        assertEquals(giocatore, giocatoreCreato);
        assertEquals(1000, punteggioCreato.intValue());
    }
    
    @Test
    public void testGetNotFound() {
        // Tentativo di reperimento di una risorsa non esistente
        Response rGet = punteggi.path("/"+giocatore+giocatore)
                            .request()
                            .get();

        // Verifica che la risposta sia "404 Not Found"
        assertEquals(Status.NOT_FOUND.getStatusCode(), rGet.getStatus());       
    }
    
    @After
    public void eliminaGiocatore() {
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();
    } 
}
