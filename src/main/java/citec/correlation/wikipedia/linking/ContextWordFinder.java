package citec.correlation.wikipedia.linking;

import static citec.correlation.wikipedia.linking.EntityAnnotation.SUBJECT;
import citec.correlation.wikipedia.utils.NLPTools;
import static citec.wikipedia.writer.analyzer.TextAnalyzer.ENGLISH_SELECTED_STOPWORDS;
import citec.wikipedia.writer.utils.FormatAndMatch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ContextWordFinder {

    private String pattern = null;
    //private DBpediaSpotLight entityInfo = new DBpediaSpotLight();

    public ContextWordFinder(String subjectLink, String annotatedSentence, Map<String, Pair<String, String>> annotatedNgram, Integer windowSize) throws Exception {
        List<String> sentences = NLPTools.getSentenceInList(annotatedSentence);
        annotatedSentence = this.findSubject(sentences, annotatedSentence, subjectLink, annotatedNgram);

        if (annotatedSentence.contains(SUBJECT)) {
            Pair<String, List<String>> pair = this.findContextWords(annotatedSentence, windowSize);
            String subject = subjectLink.toLowerCase().replace("_", " ");
            Pair<String,String> objectPair = this.getTerm(pair.getValue0(), annotatedNgram);
            String object=objectPair.getValue0();
            String objectLink=objectPair.getValue1();
            if (subject != null) {
                String stringToCheck = FormatAndMatch.format(subject, subjectLink,pair.getValue1(), object,objectLink);
                if (stringToCheck != null) {
                    this.pattern = stringToCheck;
                }
            }
        }
    }
    
    
     private Pair<String, List<String>> findContextWords(String annotatedSentence, Integer windowSize) {
           List<String> sentence = NLPTools.getSentenceInList(annotatedSentence);
       
      Integer checkStartIndex=this.findSubjectIndex(sentence);
     
       List<String> contextWords = new ArrayList<String>();
        String object = null;
        Integer limit = 0;
        if ((checkStartIndex + windowSize) < sentence.size()) {
            limit = checkStartIndex + windowSize;
        } else if ((checkStartIndex + windowSize) > sentence.size()) {
            limit = sentence.size();
        }

        for (Integer index = checkStartIndex; index < sentence.size(); index++) {
            String token =sentence.get(index);
            if(ENGLISH_SELECTED_STOPWORDS.contains(token)){
               continue; 
            }
            token=FormatAndMatch.deleteCharacters(token);
            if (token.contains(EntityAnnotation.ENTITY)) {
                object = token;
                break;
            } else if (index < limit) {
                contextWords.add(token);
            }

        }
        return new Pair<String, List<String>>(object, contextWords);
    }
    
    private String findSubject(List<String> sentenceTokens,String sentence, String subjectLink, Map<String, Pair<String, String>> annotatedNgram) {
        Integer index = 0;
        for (String string : sentenceTokens) {
            if (string.contains(EntityAnnotation.ENTITY)) {
                if (annotatedNgram.get(string) != null) {
                    Pair<String, String> link = annotatedNgram.get(string);
                    if (link.getValue1().contains(subjectLink)) {
                        return sentence.replace(string, SUBJECT);
                    }
                }
                
            } else if (string.contains(EntityAnnotation.SUBJECT)) {
               return sentence;
            }
        }
        return sentence;
    }

    public String getPatterns() {
        return pattern;
    }


    private Pair<String,String> getTerm(String string, Map<String, Pair<String, String>> annotatedNgram) {
        String subject = null,subjectLink=null;
        if (annotatedNgram.containsKey(string)) {
            subject = annotatedNgram.get(string).getValue0();
            subject=FormatAndMatch.formatTerm(subject);
            subjectLink=annotatedNgram.get(string).getValue1();
           return new Pair<String,String>(subject,subjectLink);
        }
        return new Pair<String,String>(null,null);
    }
    
    

    private String[] placeSubject(String[] sentence, Integer subjectIndex) {
        String[] modifySentence = new String[sentence.length];
        Integer index = 0;
        for (String sen : sentence) {
            if (index == subjectIndex) {
                modifySentence[index] = SUBJECT;
            } else {
                modifySentence[index] = sen;
                index = index + 1;
            }

        }
        
        return modifySentence;
    }

    private Integer findSubjectIndex(List<String> tokens) {
        Integer index = 0;
        for (String token : tokens) {
            if (token.contains(SUBJECT)) {
                return tokens.indexOf(SUBJECT)+1;
            }

        }
        return -1;
    }
    
    public static void main(String[] args) throws Exception {
        Integer index = 12;
        Integer nextindex = index + 1;

        Map<String, Pair<String,String>> annotatedNgram = new HashMap<String, Pair<String,String>>();
        Pair<String,String> subjectPair=new  Pair<String,String>("SUBJECT", "SubjectString");
        Pair<String,String> entityPair1=new  Pair<String,String>("objectString","objectKB");
        Pair<String,String> entityPair2=new  Pair<String,String>("objectString","objectKB");

        annotatedNgram.put("SUBJECT",subjectPair);
        annotatedNgram.put(EntityAnnotation.ENTITY + index.toString(),  entityPair1);
        annotatedNgram.put(EntityAnnotation.ENTITY + nextindex.toString(),entityPair2);
        List<String> annotatedSentences = new ArrayList<String>();
        String annotated1 = "SUBJECT (born june 14, 1946) is an " + EntityAnnotation.ENTITY + index.toString() + " businessman, author";
        String annotated2 = "SUBJECT " + EntityAnnotation.ENTITY + "(born june 14, 1946) is an  businessman, author";
        String annotated3 = "SUBJECT (born june 14, 1946) is an  businessman, author" + " " + EntityAnnotation.ENTITY + index.toString();
        String annotated4 = "SUBJECT (born" + " " + EntityAnnotation.ENTITY + index.toString();
        String annotated5 = EntityAnnotation.ENTITY + nextindex.toString() + " (born june 14, 1946) is an  businessman, author" + " " + EntityAnnotation.ENTITY + index.toString();
        
        
        annotatedSentences.add(annotated1);
        annotatedSentences.add(annotated2);
        annotatedSentences.add(annotated3);
        annotatedSentences.add(annotated4);
        annotatedSentences.add(annotated5);

        for (String annotatedSentence : annotatedSentences) {
            ContextWordFinder ContextWordFinder = new ContextWordFinder("Donald Trump",annotatedSentence, annotatedNgram, 5);
            System.out.println(annotatedSentence);
            System.out.println(ContextWordFinder.getPatterns());
        }

    }
    
}
