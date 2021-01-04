/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.writer.constants;

/**
 *
 * @author elahi
 */
public interface DirectoryLocation {

    public static String qald9Dir = "src/main/resources/qald9/data/";
    public static String testJson = "qald-9-test-multilingual.json";
    public static String trainingJson = "qald-9-train-multilingual.json";
    public static String dbpediaDir = "src/main/resources/dbpedia/";
    public static String dbpediaTestDir = "src/test/resources/dbpedia/";

    public static String dataDir = "data/";
    public static String entityTable = "entityTable/";
    public static String input = "input/";
    public static String output = "output/";
    public static String allPoliticianFile = dbpediaDir + input + "politicians.txt";
    public static String nameEntityDir = "src/main/resources/nameEntiry/";
    public static String anchors = "src/main/resources/dbpedia/anchors/";
    public static String achorFileTsv = "anchors_sorted_by_frequency.tsv";
    public static String JSON = ".json";
    public static String rawFiles = "rawFiles/";
    public static String patternDir = "pattern/";

}
