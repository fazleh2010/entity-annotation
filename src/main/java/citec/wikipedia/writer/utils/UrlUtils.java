/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.writer.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.javatuples.Pair;

/**
 *
 * @author elahi
 */
public class UrlUtils {

    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://dbpedia.org/page/Donald_Trump");
        Path path = Paths.get("example/path/to/file");
        String lastSegment = path.getFileName().toString();
        System.out.println(getLastPartOfUrl("http://dbpedia.org/resource/The_Trump_Organization"));

    }
    
    public static Pair<Boolean, String> getLastPartOfUrl(String url) {
        Pair<Boolean, String> pair = new Pair<Boolean, String>(false, null);
        try {
            Path path = Paths.get(url);
            return new Pair<Boolean, String>(true, path.getFileName().toString());
        } catch (Exception exp) {
            return new Pair<Boolean, String>(false, null);
        }
    }

}
