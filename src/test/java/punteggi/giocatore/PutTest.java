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

public class PutTest {
    
    private String giocatore;
    private WebTarget punteggi;
    
    public PutTest() { 
        // Assegnamento nome giocatore default
        this.giocatore = "testPutGiocatore";;
        
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
    public void testPutOk() throws ParseException {    
        int punteggio = 89789;
        // Aggiornamento del punteggio relativo a "giocatore"
        Response rPut = punteggi.path("/"+giocatore)
                            .queryParam("punteggio", punteggio)
                            .request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));
        // Verifica che la risposta sia "200 Ok"
        assertEquals(Status.OK.getStatusCode(), rPut.getStatus());
        
        // Reperimento del punteggio aggiunto
        Response rGet = punteggi.path("/"+giocatore)
                            .request()
                            .get();
        // Verifica che il record sia stato aggiornato correttamente
        JSONParser parser = new JSONParser();
        JSONObject p = (JSONObject) parser.parse(rGet.readEntity(String.class));
        Long punteggioCreato = (Long) p.get("punteggio");
        assertEquals(punteggio, punteggioCreato.intValue());
    }
    
    @Test
    public void testPutNotFound() {
        // Tentativo di PUT su risorsa non esistente
        Response rPut = punteggi.path("/"+giocatore+giocatore)
                            .queryParam("punteggio", 89789)
                            .request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));

        // Verifica che la risposta sia "404 Not Found"
        assertEquals(Status.NOT_FOUND.getStatusCode(), rPut.getStatus());       
    }
    
    @Test
    public void testPutBadRequest() {
        // Tentativo di PUT senza indicare il nuovo punteggio
        Response rPut = punteggi.path("/"+giocatore)
                            .request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));

        // Verifica che la risposta sia "405 Bad Request"
        assertEquals(Status.BAD_REQUEST.getStatusCode(), rPut.getStatus());
        
        // Tentativo di PUT con punteggio inferiore a zero
        rPut = punteggi.path("/"+giocatore)
                            .queryParam("punteggio", -456)
                            .request()
                            .put(Entity.entity("", MediaType.TEXT_PLAIN));

        // Verifica che la risposta sia "405 Bad Request"
        assertEquals(Status.BAD_REQUEST.getStatusCode(), rPut.getStatus());
    }
    
    @After
    public void eliminaGiocatore() {
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();
    } 
}
