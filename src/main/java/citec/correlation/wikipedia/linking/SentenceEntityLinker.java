package citec.correlation.wikipedia.linking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.javatuples.Pair;
import citec.correlation.wikipedia.utils.Ngram;
import citec.wikipedia.writer.analyzer.Analyzer;
import citec.wikipedia.writer.utils.FormatAndMatch;
import static citec.wikipedia.writer.utils.FormatAndMatch.format;
import static citec.wikipedia.writer.analyzer.TextAnalyzer.CLASSES;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
public class SentenceEntityLinker implements EntityAnnotation {

    private String sentenceOriginal = null;
    private String sentenceAnnotated = null;
    private Map<String, Pair<String, String>> annotatedNgram = new HashMap<String, Pair<String, String>>();
    private Integer sentenceNumber = null;
    private String pattern = null;
    private Set<String> propertyValues = new HashSet<String>();
    private Map<String, TreeMap<String, List<String>>> alphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();
    private Map<String, Set<String>> alphabeticNgrams = new HashMap<String, Set<String>>();
    private String subjectTerm = null;
    private String subjectLink = null;
    private Map<String, String> objectLinks = new HashMap<String, String>();

    public SentenceEntityLinker(String dbo_Class, String subjectLink, Integer sentenceNumber, String sentenceOriginal, Set<String> nouns, Integer windowSize, Integer ngramSize, Map<String, TreeMap<String, List<String>>> alphabetInfo, Set<String> propertyValues) throws Exception {
        //System.out.println("sentenceOriginal:"+sentenceOriginal);
        this.sentenceNumber = sentenceNumber;
        this.sentenceOriginal = sentenceOriginal;
        this.alphabetInfo = alphabetInfo;
        this.propertyValues = propertyValues;
        this.alphabeticNgrams = this.getNgrams(sentenceOriginal, ngramSize);
        String sentenceAnnotatedForConextWord = annotateForContextWords(dbo_Class, subjectLink, sentenceOriginal, nouns);
        this.pattern = new ContextWordFinder(subjectLink, sentenceAnnotatedForConextWord, annotatedNgram, windowSize).getPatterns();
        this.sentenceAnnotated = this.formatForPresentation(subjectLink, sentenceAnnotatedForConextWord);
    }

    private void annotatedEntities() {
        for (String alphabet : alphabeticNgrams.keySet()) {
            if (alphabetInfo.containsKey(alphabet)) {
                Set<String> commonAlphabetTerms = FormatAndMatch.intersection(alphabeticNgrams.get(alphabet), alphabetInfo.get(alphabet).keySet());
                System.out.println("commonAlphabetTerms:"+commonAlphabetTerms);
                for (String common : commonAlphabetTerms) {
                    if (Analyzer.ENGLISH_STOPWORDS.contains(common)) {
                        continue;
                    }
                    List<String> kbs = alphabetInfo.get(alphabet).get(common);
                    /*if (!common.contains("2002")) {
                        continue;
                    }*/
                    Pair<Boolean, String> pair = this.isPropertiesAndEntityMatched(propertyValues, kbs);
                    //System.out.println("common:" + common + "match found:" + pair.getValue0() + "  kbs" + kbs);
                    if (pair.getValue0()) {
                        String kb = pair.getValue1();
                        //System.out.println(common+" ..."+kb);
                        objectLinks.put(common, kb);
                    }
                }
            } else {
                //System.out.println("alphabet:" + alphabet);
                //System.out.println("alphabet:" + alphabeticNgrams.get(alphabet));
                Set<String> commonNumericalTerms = FormatAndMatch.intersection(alphabeticNgrams.get(alphabet), propertyValues);
                System.out.println("commonNumericalTerms:" + commonNumericalTerms);
                for (String common : commonNumericalTerms) {
                    objectLinks.put(common, common);
                }
            }

        }
        //System.out.println("intersection:"+intersection);
    }

    public static Pair<Boolean, String> isPropertiesAndEntityMatched(Set<String> propertyValues, List<String> kbs) {
        for (String kb : kbs) {
            String modifyKb = FormatAndMatch.format(kb).trim();
            for (String propValue : propertyValues) {
                String modifyProvertyValue = FormatAndMatch.format(propValue).trim();
                //System.out.println("modifyProvertyValue:"+modifyProvertyValue);
                //  System.out.println("modifyKb:"+modifyKb);
                if (modifyKb.equals(modifyProvertyValue)) {
                    return new Pair<Boolean, String>(Boolean.TRUE, kb);
                }
            }
        }
        return new Pair<Boolean, String>(Boolean.FALSE, null);
    }

    public static Pair<Boolean, String> isPropertiesAndDateMatched(Set<String> propertyValues, List<String> kbs) {
        for (String kb : kbs) {
            String modifyKb = FormatAndMatch.format(kb).trim();
            for (String propValue : propertyValues) {
                String modifyProvertyValue = FormatAndMatch.format(propValue).trim();
                //System.out.println("modifyProvertyValue:"+modifyProvertyValue);
                //  System.out.println("modifyKb:"+modifyKb);
                if (modifyKb.equals(modifyProvertyValue)) {
                    return new Pair<Boolean, String>(Boolean.TRUE, kb);
                }
            }
        }
        return new Pair<Boolean, String>(Boolean.FALSE, null);
    }

    private String annotateForContextWords(String dbo_Class, String subjectLink, String text, Set<String> nouns) {
        annotatedEntities();

        text = FormatAndMatch.format(sentenceOriginal);
        this.subjectTerm = this.findSubjectTerm(text, subjectLink);
        text = text.replace(subjectTerm, SUBJECT);
        //System.out.println(dbo_Class);
        Set<String> pronouns = CLASSES.get(dbo_Class);
        Pair<String, String> pairPronoun = FormatAndMatch.replacePronoun(text, SUBJECT, pronouns);
        if (pairPronoun.getValue0() != null) {
            text = pairPronoun.getValue1();
        }

        List<String> words = new ArrayList<String>(objectLinks.keySet());
        String[] strings = words.toArray(String[]::new);
        Arrays.sort(strings, Comparator.comparingInt(String::length));
        Integer index = 0;
        for (int i = strings.length - 1; 0 <= i; i--) {
            index = index + 1;
            //System.out.println("strings[i]:"+strings[i]);
            if (strings[i].equals(SUBJECT)) {
                String id = SUBJECT;
                text = text.replace(strings[i], id);
                Pair<String, String> pair = new Pair<String, String>(strings[i], subjectLink);
                annotatedNgram.put(id, pair);
            } else if (FormatAndMatch.isValid(strings[i], text, nouns)) {
                String id = ENTITY + index++;
                text = text.replace(strings[i], id);
                Pair<String, String> pair = new Pair<String, String>(strings[i], objectLinks.get(strings[i]));
                annotatedNgram.put(id, pair);
            } else if (objectLinks.containsKey(strings[i])) {
                String id = ENTITY + index++;
                text = text.replace(strings[i], id);
                Pair<String, String> pair = new Pair<String, String>(strings[i], objectLinks.get(strings[i]));
                annotatedNgram.put(id, pair);
            }
        }

        text = text.replaceAll("_", " ");
        return text;
    }

    private String findSubjectTerm(String sentenceAnnotated, String subjectLink) {
        String subjectTerm = null;
        if (subjectLink.contains("_(")) {
            subjectTerm = subjectLink.replace("_(", " ");
            String[] info = subjectTerm.split(" ");
            subjectTerm = info[0];
        } else {
            subjectTerm = subjectLink;
        }
        subjectTerm = format(subjectTerm).trim();

        return subjectTerm;

    }

    private String formatForPresentation(String subjectLink, String sentenceAnnotated) {
        sentenceAnnotated = FormatAndMatch.format(sentenceAnnotated, this.annotatedNgram);
        return sentenceAnnotated.replace(SUBJECT, FormatAndMatch.format(subjectLink).replace("_", " ") + "<" + subjectLink + ">");
    }

    private Map<String, Set<String>> getNgrams(String sentenceOriginal, Integer ngramSize) {
        Ngram nGram = new Ngram(sentenceOriginal, ngramSize);
        return nGram.getAlphabetNgrams(nGram);
    }

    @Override
    public String getSentenceLineOriginal() {
        return sentenceOriginal;
    }

    @Override
    public String getAnnotatedSentence() {
        return sentenceAnnotated;
    }

    @Override
    public Integer getSentenceNumber() {
        return sentenceNumber;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public Map<String, Pair<String, String>> getAnnotatedNgram() {
        return annotatedNgram;
    }

}
