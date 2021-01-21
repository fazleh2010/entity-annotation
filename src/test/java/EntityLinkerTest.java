/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elahi
 */
import citec.wikipedia.entity.linking.AbstractEntityLinker;
import citec.wikipedia.entity.linking.LinkingDictionary;
import citec.wikipedia.entity.utils.FileUtilsAnno;
import citec.wikipedia.writer.analyzer.TextAnalyzer;
import citec.wikipedia.writer.constants.DirectoryLocation;
import citec.wikipedia.writer.constants.Property;
import citec.wikipedia.writer.table.DBpediaEntity;
import citec.wikipedia.writer.utils.FileFolderUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author elahi
 */
public class EntityLinkerTest implements TextAnalyzer, DirectoryLocation {

    private static Set<String> classFileNames = new HashSet<String>();
    private static Integer windowSize = 5, nGram = 5;
    private static Map<String, TreeMap<String, List<String>>> objectToAnchorsAlphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();
    private static Map<String, TreeMap<String, List<String>>> termToObjectsAlphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();

   

    @Ignore
    public void PREPARE_ANCHOR_DICTIONARY_TEST() throws IOException, Exception {
        System.out.println(DirectoryLocation.anchors);
        LinkingDictionary linkingDictionary = new LinkingDictionary(DirectoryLocation.anchors, ".txt");
        linkingDictionary.prepareObjectAnchorsDictioarny();
    }

    public static void main(String[] args) throws IOException, Exception {
        LinkingDictionary linkingDictionary = new LinkingDictionary(DirectoryLocation.anchors, ".txt");
        linkingDictionary.prepareTermObjectsDictioarny();
        termToObjectsAlphabetInfo = linkingDictionary.getTermToObjectsAlphabetInfo();

        if (termToObjectsAlphabetInfo.isEmpty()) {
            throw new Exception("no entity dictionay is available for entity annotation!!");
        }

        /*System.out.println(linkingDictionary.getTermToObjectsAlphabetInfo().keySet());
        for(String alphabet:linkingDictionary.getTermToObjectsAlphabetInfo().keySet()){
            TreeMap<String, List<String>> termObjects=linkingDictionary.getTermToObjectsAlphabetInfo().get(alphabet);
             for(String term:termObjects.keySet()){
                System.out.println("term:"+term);
                System.out.println("object list:"+termObjects.get(term));
             }

        }*/

 /*for (String dbo_Class : CLASSES.keySet()) {
            String classDir = FileFolderUtils.getClassDir(dbo_Class) + "/";
            String inputDir = dbpediaDir + classDir + rawFiles;
            String outputDir = dbpediaDir + classDir + patternDir;
            System.out.println("rawFilesDir:" + inputDir);
            System.out.println("outputDir:" + outputDir);
            Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileUtilsAnno.readTables(inputDir, JSON);
            AbstractEntityLinker main = new AbstractEntityLinker(linkingDictionary.getTermToObjectsAlphabetInfo(),dbo_Class, outputDir, windowSize, nGram, fileDBpediaEntities);
            break;
        }*/
 
        List<String> CLASSES = new ArrayList<String>();
        CLASSES.add(Property.dbo_City);
        //CLASSES.add(Property.dbo_Company);
       
        for (String dbo_Class : CLASSES) {
            String classDir = FileFolderUtils.getClassDir(dbo_Class) + "/";
            String inputDir = dbpediaDir + classDir + rawFiles;
            String outputDir = dbpediaDir + classDir + patternDir;
            Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileUtilsAnno.readTables(inputDir, JSON);
            AbstractEntityLinker main = new AbstractEntityLinker(termToObjectsAlphabetInfo, dbo_Class, outputDir, windowSize, nGram, fileDBpediaEntities);

        }

    }


    /*  @Test
    public void entityAnnotationAll() throws IOException, Exception {
        String rawFilesDir = dbpediaDir + rawFiles;
        String outputDir = dbpediaDir + patternDir;
        Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileFolderUtilsAnnotation.readAlphabetSplitTables(rawFilesDir);
        //AbstractEntityLinker main = new AbstractEntityLinker(dbo_Class, outputDir, windowSize, nGram, fileDBpediaEntities);

    }*/
 /* String rawFilesDir = dbpediaDir + DirectoryLocation.rawFilesDir;
        String outputDir = dbpediaDir + patternDir;
        Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileFolderUtilsAnnotation.readAlphabetSplitTables(rawFilesDir);
        AbstractEntityLinker main = new AbstractEntityLinker(dbo_Class, outputDir, windowSize, nGram, fileDBpediaEntities);*/
}
