/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import citec.wikipedia.writer.sparql.SparqlQueryImpl;
import citec.wikipedia.writer.sparql.SparqlQuery;
import static citec.wikipedia.writer.sparql.SparqlQuery.DBO_ABSTRACT;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author elahi
 */
public class CurlSparqlQueryTest {

    @Ignore
    public void propertyGeneration_WhenAbstract() throws Exception {
        String entityUrl = "http://dbpedia.org/resource/Allan_Dwan";
        String actual = "Allan Dwan (3 April 1885 – 28 December 1981) was a pioneering Canadian-born American motion picture director, producer and screenwriter.";
        SparqlQuery sparql = new SparqlQueryImpl(entityUrl);
        System.out.println("abstract found!!");
        assertEquals(sparql.getProperties().containsKey(DBO_ABSTRACT), Boolean.TRUE);
        assertEquals(sparql.getProperties().get("dbo:abstract").iterator().next(), actual);
    }
     @Ignore
     public  void propertyGeneration_WhenNoAbstract() throws Exception {
        String entityUrl = "http://dbpedia.org/resource/A_Clockwork_Orange";
        SparqlQuery sparql = new SparqlQueryImpl(entityUrl);
        System.out.println("abstract NOT found!!");
        assertEquals(sparql.getProperties().containsKey(DBO_ABSTRACT), Boolean.FALSE);
    }

}
