/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elahi
 */
import citec.wikipedia.entity.utils.FileUtilsAnno;
import citec.wikipedia.writer.analyzer.POStagging;
import citec.wikipedia.writer.analyzer.TextAnalyzer;
import citec.wikipedia.writer.constants.DirectoryLocation;
import citec.wikipedia.writer.constants.Property;
import citec.wikipedia.writer.table.DBpediaEntity;
import citec.wikipedia.writer.utils.FileFolderUtils;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 *
 * @author elahi
 */
public class POStagTest implements TextAnalyzer, DirectoryLocation {

    private static Set<String> classFileNames = new HashSet<String>();
    private static String resources = "src/main/resources/";
    private static String stanfordModelFile = resources + "stanford-postagger-2015-12-09/models/english-left3words-distsim.tagger";
    private static MaxentTagger taggerModel = new MaxentTagger(stanfordModelFile);

   
    public static void main(String[] args) throws IOException, Exception {
        List<String> CLASSES = new ArrayList<String>();
        CLASSES.add(Property.dbo_Actor);
        CLASSES.add(Property.dbo_Album);
        CLASSES.add(Property.dbo_Athlete);
        CLASSES.add(Property.dbo_Book);
        CLASSES.add(Property.dbo_City);
        CLASSES.add(Property.dbo_Company);
        CLASSES.add(Property.dbo_Country);
        CLASSES.add(Property.dbo_Lake);
        CLASSES.add(Property.dbo_Location);
        CLASSES.add(Property.dbo_Mountain);
        CLASSES.add(Property.dbo_Person);
        CLASSES.add(Property.dbo_Politician);
         CLASSES.add(Property.dbo_Stadium);
         CLASSES.add(Property.dbo_TelevisionShow);
       
        for (String dbo_Class : CLASSES) {
            String classDir = FileFolderUtils.getClassDir(dbo_Class) + "/";
            String inputDir = dbpediaDir + classDir + rawFiles;
            String outputDir = dbpediaDir + classDir + nlpDir;
            System.out.println("rawFilesDir:" + inputDir);
            System.out.println("outputDir:" + outputDir);
            Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileUtilsAnno.readTables(inputDir, JSON);
            POStagging main = new POStagging(dbo_Class, outputDir, fileDBpediaEntities);
        }

    }

}
