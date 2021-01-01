package citec.correlation.wikipedia.linking;

import citec.correlation.wikipedia.pattern.DBpediaEntityPattern;
import citec.correlation.wikipedia.utils.FileFolderUtilsAnnotation;
import citec.correlation.wikipedia.utils.NLPTools;
import citec.wikipedia.writer.analyzer.Analyzer;
import static citec.wikipedia.writer.analyzer.TextAnalyzer.POS_TAGGER_WORDS;
import citec.wikipedia.writer.constants.DirectoryLocation;
import citec.wikipedia.writer.table.DBpediaEntity;
import citec.wikipedia.writer.table.DBpediaProperty;
import citec.wikipedia.writer.utils.FileFolderUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import citec.wikipedia.writer.utils.FormatAndMatch;
import citec.wikipedia.writer.utils.UrlUtils;
import java.io.File;
import java.util.HashSet;
import org.javatuples.Pair;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
public class AbstractEntityLinker {

    private Map<String, TreeMap<String, List<String>>> alphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();
    private Integer windowSize = 5;
    private Integer nGram = 3;

    public AbstractEntityLinker(String dbo_Class, String outputDir, Integer windowSize, Integer nGram, Map<String, List<DBpediaEntity>> fileDBpediaEntities) throws Exception {
        this.alphabetInfo = FileFolderUtils.getAlphabetInfo(DirectoryLocation.anchors, ".txt");
        this.windowSize = windowSize;
        this.nGram = nGram;
        if (this.alphabetInfo.isEmpty()) {
            throw new Exception("no entity dictionay is available for entity annotation!!");
        }
        try {
            addPatterns(dbo_Class, outputDir, fileDBpediaEntities, windowSize, nGram);
        } catch (Exception exp) {
          System.out.println(dbo_Class+" "+exp.getMessage());
        }

    }

    public List<EntityAnnotation> annotateSentences(String dbo_Class,String entity, String text, Integer windowSize, Integer nGram, Map<String, TreeMap<String, List<String>>> alphabetInfo, Set<String> propertyValues) throws Exception {
        List<EntityAnnotation> annotatedSentences = new ArrayList< EntityAnnotation>();
        List<String> sentenceLines = NLPTools.getSentencesFromText(text);
        if (sentenceLines.isEmpty()) {
            return new ArrayList<EntityAnnotation>();
        }
        Integer index = 0;
        for (String sentenceLine : sentenceLines) {
            index = index + 1;
            Analyzer analyzer = new Analyzer(sentenceLine, POS_TAGGER_WORDS, 5);
            Set<String> nouns = FormatAndMatch.format(analyzer.getNouns());
            EntityAnnotation entityAnnotation = new SentenceEntityLinker(dbo_Class,entity, index, sentenceLine, nouns, windowSize, nGram, alphabetInfo, propertyValues);
            annotatedSentences.add(entityAnnotation);
        }
        return annotatedSentences;
    }

    private void addPatterns(String dbo_Class,String outputDir, Map<String, List<DBpediaEntity>> fileDBpediaEntities, Integer windowSize, Integer nGram) throws Exception {

        for (String fileName : fileDBpediaEntities.keySet()) {
            List<DBpediaEntityPattern> correctedEntities = new ArrayList<DBpediaEntityPattern>();
            List<DBpediaEntity> DBpediaEntitys = fileDBpediaEntities.get(fileName);
            Integer total = DBpediaEntitys.size();
            Integer count = 0;
            for (DBpediaEntity dbpediaEntity : DBpediaEntitys) {
                String subject = getSubject(dbpediaEntity.getEntityUrl());
                System.out.println();
                System.out.println(subject + " count=" + count + " total=" + total + " " + fileName + "........................");
                System.out.println("entity:" + dbpediaEntity.getEntityUrl());
                String text = dbpediaEntity.getText();
                if (text.length() < 10) {
                    continue;
                }
                Pair<Set<String>, Map<Integer, String>> pair = getPropertyValues(subject, dbpediaEntity);
                Set<String> propertyValues = pair.getValue0();
                Map<Integer, String> tripples = pair.getValue1();
                List<EntityAnnotation> annotatedSentences = this.annotateSentences(dbo_Class,subject, text, windowSize, nGram, alphabetInfo, propertyValues);
                DBpediaEntityPattern DBpediaEntityPattern = this.prepareDBpediaEntityPattern(dbpediaEntity, tripples, annotatedSentences);
                correctedEntities.add(DBpediaEntityPattern);
                count = count + 1;

            }
            if (!correctedEntities.isEmpty()) {
                FileFolderUtilsAnnotation.convertToJson(correctedEntities, outputDir, fileName.replace(".json", "") + "_pattern");
            }

        }

    }

    private String getSubject(String entityUrl) {
        Pair<Boolean, String> pairSubject = UrlUtils.getLastPartOfUrl(entityUrl);
        if (pairSubject.getValue0()) {
            return pairSubject.getValue1();
        }
        return null;
    }

    private Pair<Set<String>, Map<Integer, String>> getPropertyValues(String subject, DBpediaEntity dbpediaEntity) {
        Set<String> propertyValues = new HashSet<String>();
        Map<Integer, String> tripples = new TreeMap<Integer, String>();
        Pair<Set<String>, Map<Integer, String>> value = new Pair<Set<String>, Map<Integer, String>>(propertyValues, tripples);

        Integer index = 0;
        for (String key : dbpediaEntity.getProperties().keySet()) {
            if (DBpediaProperty.isExcludedProperty(key)) {
                continue;
            }

            List<String> properties = dbpediaEntity.getProperties().get(key);

            for (String property : properties) {
                index = index + 1;
                Pair<Boolean, String> pair = UrlUtils.getLastPartOfUrl(property);
                if (pair.getValue0()) {
                    String kb = pair.getValue1();
                    propertyValues.add(kb);
                    String tripple = "s(" + subject + ")" + " " + key + " o'<" + kb + ">";
                    tripples.put(index, tripple);
                }

            }
        }
        return new Pair<Set<String>, Map<Integer, String>>(propertyValues, tripples);
    }

    private DBpediaEntityPattern prepareDBpediaEntityPattern(DBpediaEntity dbpediaEntity, Map<Integer, String> tripples, List<EntityAnnotation> annotatedSentences) {
        Map<Integer, String> annotatedEntities = new TreeMap<Integer, String>();
        Map<Integer, String> patterns = new TreeMap<Integer, String>();

        for (EntityAnnotation entityAnnotation : annotatedSentences) {
            annotatedEntities.put(entityAnnotation.getSentenceNumber(), entityAnnotation.getAnnotatedSentence());
            if (entityAnnotation.getPattern() != null) {
                patterns.put(entityAnnotation.getSentenceNumber(), entityAnnotation.getPattern());
            }
        }
        return new DBpediaEntityPattern(dbpediaEntity, tripples, patterns, annotatedEntities);
    }

}
