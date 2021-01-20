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
import citec.wikipedia.writer.analyzer.POStagging;
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
public class POStagTest implements TextAnalyzer, DirectoryLocation {

    private static Set<String> classFileNames = new HashSet<String>();

   
    public static void main(String[] args) throws IOException, Exception {
        List<String> CLASSES = new ArrayList<String>();
        CLASSES.add(Property.dbo_City);
        CLASSES.add(Property.dbo_Company);
       
        for (String dbo_Class : CLASSES) {
            String classDir = FileFolderUtils.getClassDir(dbo_Class) + "/";
            String inputDir = dbpediaDir + classDir + rawFiles;
            String outputDir = dbpediaDir + classDir + patternDir;
            System.out.println("rawFilesDir:" + inputDir);
            System.out.println("outputDir:" + outputDir);
            Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileUtilsAnno.readTables(inputDir, JSON);
            POStagging main = new POStagging(dbo_Class, outputDir, fileDBpediaEntities);

        }

    }

}
