package punteggi.main;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import static junit.framework.Assert.assertEquals;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class PostTest {
    
    private WebTarget punteggi;
    
    public PostTest() { 
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        this.punteggi = cli.target("http://localhost:56476/punteggi");
    }
    
    @Test
    public void testPostCreated() throws ParseException {
        String giocatore = "giocatorePerUnitTestPostCreated";
        
        // Creazione del punteggio relativo a "giocatore"
        Response rPost = punteggi.queryParam("giocatore", giocatore)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        // Reperimento del punteggio aggiunto
        Response rGet = punteggi.path("/"+giocatore)
                            .request()
                            .get();
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();
        
        // Verifica che la risposta rPost ottenuta sia "201 Created"
        assertEquals(Status.CREATED.getStatusCode(), rPost.getStatus());
        // Verifica che il record sia stato creato
        assertEquals(Status.OK.getStatusCode(), rGet.getStatus());
        // Verifica che il record aggiunto sia 
        // <"giocatorePerUnitTestPostCreated", 1000>
        JSONParser parser = new JSONParser();
        JSONObject p = (JSONObject) parser.parse(rGet.readEntity(String.class));
        String giocatoreCreato = (String) p.get("giocatore"); 
        Long punteggioCreato = (Long) p.get("punteggio");
        assertEquals(giocatore, giocatoreCreato);
        assertEquals(1000, punteggioCreato.intValue());   
    }
    
    @Test
    public void testPostConPunteggioCreated() throws ParseException {
        String giocatore = "giocatorePerUnitTestPostConPunteggioCreated";
        int punteggio = 3456;
        // Creazione del punteggio relativo a "giocatore"
        Response rPost = punteggi.queryParam("giocatore", giocatore)
                            .queryParam("punteggio", punteggio)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        // Reperimento del punteggio aggiunto
        Response rGet = punteggi.path("/"+giocatore)
                            .request()
                            .get();
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();
        // Verifica che la risposta ottenuta sia "201 Created"
        assertEquals(Status.CREATED.getStatusCode(), rPost.getStatus());
        // Verifica che il record sia stato creato
        assertEquals(Status.OK.getStatusCode(), rGet.getStatus());
        // Verifica che il record aggiunto sia 
        // <"giocatorePerUnitTestPostCreated", 1000>
        JSONParser parser = new JSONParser();
        JSONObject p = (JSONObject) parser.parse(rGet.readEntity(String.class));
        String giocatoreCreato = (String) p.get("giocatore"); 
        Long punteggioCreato = (Long) p.get("punteggio");
        assertEquals(giocatore, giocatoreCreato);
        assertEquals(punteggio, punteggioCreato.intValue());
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
        // (senza punteggio)
        Response rPost = punteggi.queryParam("giocatore", giocatore)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));        
        // Ripetizione della creazione del punteggio relativo a "giocatore"
        // (con punteggio)
        Response rPost2 = punteggi.queryParam("giocatore", giocatore)
                            .queryParam("punteggio", 3456)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));        
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();
        // Verifica che (in entrambi i casi) la risposta 
        // ottenuta sia "409 Conflict"
        assertEquals(Status.CONFLICT.getStatusCode(), rPost.getStatus());
        assertEquals(Status.CONFLICT.getStatusCode(), rPost2.getStatus());
    }
    
}

