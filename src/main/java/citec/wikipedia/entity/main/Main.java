/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.entity.main;

/**
 *
 * @author elahi
 */
import citec.wikipedia.entity.linking.AbstractEntityLinker;
import citec.wikipedia.entity.linking.LinkingDictionary;
import citec.wikipedia.entity.utils.FileUtilsAnno;
import citec.wikipedia.writer.analyzer.TextAnalyzer;
import static citec.wikipedia.writer.analyzer.TextAnalyzer.CLASSES;
import citec.wikipedia.writer.constants.DirectoryLocation;
import static citec.wikipedia.writer.constants.DirectoryLocation.JSON;
import static citec.wikipedia.writer.constants.DirectoryLocation.dbpediaDir;
import static citec.wikipedia.writer.constants.DirectoryLocation.patternDir;
import static citec.wikipedia.writer.constants.DirectoryLocation.rawFiles;
import citec.wikipedia.writer.constants.Property;
import citec.wikipedia.writer.table.DBpediaEntity;
import citec.wikipedia.writer.utils.FileFolderUtils;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Main implements TextAnalyzer, DirectoryLocation {

    private static Set<String> classFileNames = new HashSet<String>();
    private static Integer windowSize = 5, nGram = 4;
    private static Map<String, TreeMap<String, List<String>>> objectToAnchorsAlphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();
    private static Map<String, TreeMap<String, List<String>>> termToObjectsAlphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();

    public static void main(String[] args) throws IOException, Exception {
        LinkingDictionary linkingDictionary = new LinkingDictionary(DirectoryLocation.anchors, ".txt");
        linkingDictionary.prepareTermObjectsDictioarny();
        termToObjectsAlphabetInfo = linkingDictionary.getTermToObjectsAlphabetInfo();

        if (termToObjectsAlphabetInfo.isEmpty()) {
            throw new Exception("no entity dictionay is available for entity annotation!!");
        }
        
        //for (String dbo_Class : CLASSES.keySet()) {
            String dbo_Class=Property.dbo_Book;
            String classDir = FileFolderUtils.getClassDir(dbo_Class) + "/";
            String inputDir = dbpediaDir + classDir + rawFiles;
            String outputDir = dbpediaDir + classDir + patternDir;
            System.out.println("rawFilesDir:" + inputDir);
            System.out.println("outputDir:" + outputDir);
            Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileUtilsAnno.readTables(inputDir, JSON);
            AbstractEntityLinker main = new AbstractEntityLinker(termToObjectsAlphabetInfo, dbo_Class, outputDir, windowSize, nGram, fileDBpediaEntities);
        //}
    }

    public void PREPARE_ANCHOR_DICTIONARY_TEST() throws IOException, Exception {
        System.out.println(DirectoryLocation.anchors);
        LinkingDictionary linkingDictionary = new LinkingDictionary(DirectoryLocation.anchors, ".txt");
        linkingDictionary.prepareObjectAnchorsDictioarny();
    }

}
