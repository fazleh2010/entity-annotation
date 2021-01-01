/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elahi
 */
import citec.correlation.wikipedia.linking.AbstractEntityLinker;
import citec.correlation.wikipedia.utils.FileFolderUtilsAnnotation;
import citec.wikipedia.writer.analyzer.TextAnalyzer;
import citec.wikipedia.writer.constants.DirectoryLocation;
import citec.wikipedia.writer.table.DBpediaEntity;
import citec.wikipedia.writer.utils.FileFolderUtils;
import java.io.IOException;
import java.util.HashSet;
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

    private static Map<String, TreeMap<String, List<String>>> alphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();
    private static Set<String> classFileNames = new HashSet<String>();
    private Integer windowSize = 5, nGram = 5;


    @Ignore
    public void entityAnnotation() throws IOException, Exception {
        String dbo_Class = "dbo:Company";
        System.out.println("dbo_Class:" + dbo_Class);
        String classDir = FileFolderUtils.getClassDir(dbo_Class) + "/";
        String inputDir = dbpediaTestDir + classDir + rawFiles;
        String outputDir = dbpediaTestDir + classDir + patternDir;
        System.out.println("rawFilesDir:" + inputDir);
        System.out.println("outputDir:" + outputDir);
        Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileFolderUtilsAnnotation.readTables(inputDir, JSON);
        System.out.println(fileDBpediaEntities.size());
        AbstractEntityLinker main = new AbstractEntityLinker(dbo_Class, outputDir, windowSize, nGram, fileDBpediaEntities);
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
