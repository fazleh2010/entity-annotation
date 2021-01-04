/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.writer.sparql;

import java.util.List;
import java.util.Map;

/**
 *
 * @author elahi
 */
public interface SparqlQuery {

    public String END_POINT = "https://dbpedia.org/sparql";
    public String ABSTRACT_ATTRIBUTE = "http://dbpedia.org/ontology/abstract";
    public String ABSTRACT_PROPERTY = "ABSTRACT_PROPERTY";
    public String OTHER_PROPERTY = "OTHER_PROPERTY";
    public String DBO_ABSTRACT = "dbo:abstract";
    public Map<String, List<String>> getProperties();
    public Boolean isAbstractContains();

}
