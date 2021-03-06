/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.annotation.utils;

import java.util.HashSet;
import java.util.Set;
import org.javatuples.Pair;

/**
 *
 * @author elahi
 */
public class Pronoun {

    public static Pair<String, String> replacePronoun(String text, String PRONOUN, Set<String> pronouns) {

        if (text.contains("_")) {
            String[] sentenceTokens = text.split("_");
            String firstToken = text.substring(0, text.indexOf("_"));
            Set<String> firstTokenSet = new HashSet<String>();
            firstTokenSet.add(firstToken);
            Set<String> common = intersection(firstTokenSet, pronouns);
            // System.out.println("pronouns:"+pronouns);
            //System.out.println("firstTokenSet:"+firstTokenSet);
            //System.out.println("common:"+common);

            if (!common.isEmpty()) {
                String secondToken = text.substring(1, text.indexOf("_"));
                
                if (firstToken.contains("the") && secondToken.contains("album")) {
                    text = prepareSentence2(sentenceTokens, PRONOUN);
                    return new Pair<String, String>("the_album", text);
                } else if (firstToken.contains("this") && secondToken.contains("album")) {
                    text = prepareSentence2(sentenceTokens, PRONOUN);
                    return new Pair<String, String>("this_album", text);
                } else if (firstToken.contains("this") && secondToken.contains("album's")) {
                    text = prepareSentence2(sentenceTokens, PRONOUN);
                    return new Pair<String, String>("this_album", text);
                } else if (firstToken.contains("this") && secondToken.contains("band")) {
                    text = prepareSentence2(sentenceTokens, PRONOUN);
                    return new Pair<String, String>("this_band", text);
                } else if (firstToken.contains("the") && secondToken.contains("band")) {
                    text = prepareSentence2(sentenceTokens, PRONOUN);
                    return new Pair<String, String>("the_band", text);
                }
                else{
                String pronounFound = common.iterator().next();
                text = prepareSentence(sentenceTokens, PRONOUN);
                return new Pair<String, String>(pronounFound, text); 
                }
            } else {
                return new Pair<String, String>(null, text);
            }
        } else {
            return new Pair<String, String>(null, text);
        }

    }

    public static Set<String> intersection(Set<String> sentenceTerms, Set<String> allTerms) {
        Set<String> intersection = new HashSet<String>(allTerms);
        intersection.retainAll(sentenceTerms);
        return intersection;
    }

    private static String prepareSentence(String[] sentenceTokens, String SUBJECT) {
        String str = "";
        Integer index = 0;
        for (String tokenStr : sentenceTokens) {
            String line = null;
            if (index == 0) {
                tokenStr = SUBJECT;
            }
            index = index + 1;
            if (index > sentenceTokens.length - 1) {
                line = tokenStr;
            } else {
                line = tokenStr + "_";
            }

            str += line;
        }
        str = str + "\n";
        return str;
    }
     private static String prepareSentence2(String[] sentenceTokens, String SUBJECT) {
        String str = "";
        Integer index = 0;
        for (String tokenStr : sentenceTokens) {
            String line = null;
            if (index == 0) {
               continue;
            }
            else if(index == 1){
                 tokenStr = SUBJECT;
            }
            index = index + 1;
            if (index > sentenceTokens.length - 1) {
                line = tokenStr;
            } else {
                line = tokenStr + "_";
            }

            str += line;
        }
        str = str + "\n";
        return str;
    }

}
