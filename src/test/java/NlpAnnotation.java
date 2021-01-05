
import citec.wikipedia.entity.linking.AbstractEntityLinker;
import citec.wikipedia.entity.linking.EntityAnnotation;
import citec.wikipedia.entity.linking.LinkingDictionary;
import citec.wikipedia.entity.linking.SentenceEntityLinker;
import citec.wikipedia.entity.utils.FileUtilsAnno;
import citec.wikipedia.entity.utils.NLPTools;
import citec.wikipedia.writer.analyzer.Analyzer;
import citec.wikipedia.writer.analyzer.TextAnalyzer;
import static citec.wikipedia.writer.analyzer.TextAnalyzer.POS_TAGGER_WORDS;
import citec.wikipedia.writer.constants.DirectoryLocation;
import static citec.wikipedia.writer.constants.DirectoryLocation.JSON;
import static citec.wikipedia.writer.constants.DirectoryLocation.dbpediaDir;
import static citec.wikipedia.writer.constants.DirectoryLocation.patternDir;
import static citec.wikipedia.writer.constants.DirectoryLocation.rawFiles;
import citec.wikipedia.writer.constants.Property;
import citec.wikipedia.writer.table.DBpediaEntity;
import citec.wikipedia.writer.utils.FileFolderUtils;
import citec.wikipedia.writer.utils.FormatAndMatch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.junit.Ignore;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elahi
 */
public class NlpAnnotation implements TextAnalyzer, DirectoryLocation {

    private static Set<String> classFileNames = new HashSet<String>();

    public static void main(String[] args) throws IOException, Exception {
        classFileNames.add(Property.dbo_Actor);

        for (String dbo_Class : classFileNames) {
            String classDir = FileFolderUtils.getClassDir(dbo_Class) + "/";
            //String inputDir = dbpediaDir + classDir + rawText;
            String inputDir = dbpediaDir + classDir + rawFiles;
            System.out.println("rawFilesDir:" + inputDir);
            //System.out.println("outputDir:" + outputDir);
            Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileUtilsAnno.readTables(inputDir, JSON);
            System.out.println("rawFilesDir:" + inputDir+" size:"+fileDBpediaEntities.size());

            break;
           // Map<String, List<DBpediaEntity>> fileDBpediaEntities = FileUtilsAnno.readTables(inputDir, JSON);
        }
    }
    
    public List<EntityAnnotation> annotateSentences(String dbo_Class, String entity, String text) throws Exception {
        List<EntityAnnotation> annotatedSentences = new ArrayList< EntityAnnotation>();
        List<String> sentenceLines = NLPTools.getSentencesFromText(text);
        if (sentenceLines.isEmpty()) {
            return new ArrayList<EntityAnnotation>();
        }
        Integer index = 0;
        for (String sentenceLine : sentenceLines) {
            index = index + 1;
            Analyzer analyzer = new Analyzer(sentenceLine, POS_TAGGER_WORDS, 5);
        }
        return annotatedSentences;
    }

}
