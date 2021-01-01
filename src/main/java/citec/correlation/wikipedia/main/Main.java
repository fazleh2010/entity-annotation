/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.main;

/**
 *
 * @author elahi
 */
import citec.correlation.wikipedia.linking.AbstractEntityLinker;
import citec.correlation.wikipedia.utils.FileFolderUtilsAnnotation;
import citec.wikipedia.writer.analyzer.TextAnalyzer;
import static citec.wikipedia.writer.analyzer.TextAnalyzer.CLASSES;
import citec.wikipedia.writer.constants.DirectoryLocation;
import static citec.wikipedia.writer.constants.DirectoryLocation.JSON;
import static citec.wikipedia.writer.constants.DirectoryLocation.dbpediaDir;
import static citec.wikipedia.writer.constants.DirectoryLocation.patternDir;
import static citec.wikipedia.writer.constants.DirectoryLocation.rawFiles;
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

    private static Map<String, TreeMap<String, List<String>>> alphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();
    private static Set<String> classFileNames = new HashSet<String>();
    private Integer windowSize = 5, nGram = 5;

    public void main(String []args) throws IOException, Exception {
        for (String dbo_Class : CLASSES.keySet()) {
            System.out.println("dbo_Class:" + dbo_Class);
            String classDir = FileFolderUtils.getClassDir(dbo_Class) + "/";
            String inputDir = dbpediaDir + classDir + rawFiles;
            String outputDir = dbpediaDir + classDir + patternDir;
            System.out.println("rawFilesDir:" + inputDir);
            System.out.println("outputDir:" + outputDir);
            Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileFolderUtilsAnnotation.readTables(inputDir, JSON);
            System.out.println(fileDBpediaEntities.size());
            AbstractEntityLinker main = new AbstractEntityLinker(dbo_Class, patternDir, windowSize, nGram, fileDBpediaEntities);
            break;
        }

    }

}
