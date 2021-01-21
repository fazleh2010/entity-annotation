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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class POStagTest implements TextAnalyzer, DirectoryLocation {
    
    //just put new class folder in dbpedia folder and then add those already done in exlcude set.

    private static Set<String> classFileNames = new HashSet<String>();
    private static String resources = "src/main/resources/";
    private static String stanfordModelFile = resources + "stanford-postagger-2015-12-09/models/english-left3words-distsim.tagger";
    private static MaxentTagger taggerModel = new MaxentTagger(stanfordModelFile);
    private static List<String> done = new ArrayList<String>();
    private static List<String> CLASSES = new ArrayList<String>();

    public static void main(String[] args) throws IOException, Exception {
        exclude();
        //CLASSES = getClasses(dbpediaDir);
         CLASSES.add("dbo:MilitaryConflict");
         CLASSES.add("dbo:River");
         CLASSES.add("dbo:Place");
        for (String dbo_Class : CLASSES) {
            
            String classDir = FileFolderUtils.getClassDir(dbo_Class) + "/";
            String inputDir = dbpediaDir + classDir + rawFiles;
            String outputDir = dbpediaDir + classDir + nlpDir;
           
              System.out.println("inputDir:" + inputDir); 
              Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileUtilsAnno.readTables(inputDir, JSON);
              POStagging main = new POStagging(dbo_Class, outputDir, fileDBpediaEntities);
        }

    }

    private static void exclude() {
        done.add(Property.dbo_Actor);
        done.add(Property.dbo_Album);
        done.add(Property.dbo_Athlete);
        done.add(Property.dbo_Book);
        done.add(Property.dbo_City);
        done.add(Property.dbo_Company);
        done.add(Property.dbo_Country);
        done.add(Property.dbo_Lake);
        done.add(Property.dbo_Location);
        done.add(Property.dbo_Mountain);
        done.add(Property.dbo_Person);
        done.add(Property.dbo_Politician);
        done.add(Property.dbo_Stadium);
        done.add(Property.dbo_TelevisionShow);
        done.add(Property.dbo_Politician);
        done.add(Property.dbo_anchors);
    }

    private static List<String> getClasses(String dbpediaDir) {
        List<String> CLASSES = new ArrayList<String>();
        ArrayList<File> directories = new ArrayList<File>(
                Arrays.asList(
                        new File(dbpediaDir).listFiles(File::isDirectory)
                )
        );

        for (File file : directories) {
            String dbo_Class = file.getName().trim().strip();
            dbo_Class ="dbo:"+dbo_Class;
            if (done.contains(dbo_Class)) {
                continue;
            }
            CLASSES.add(dbo_Class);
        }
        return CLASSES;
    }

}
