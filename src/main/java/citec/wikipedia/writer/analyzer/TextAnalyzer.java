/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.writer.analyzer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import citec.wikipedia.writer.constants.Property;
import java.util.LinkedHashMap;

/**
 *
 * @author elahi
 */
public interface TextAnalyzer {

    public static final String POS_TAGGER_WORDS = "POS_TAGGER_WORDS";
    public static final String POS_TAGGER_TEXT = "POS_TAGGER_TEXT";

    public static final String ADJECTIVE = "JJ";
    public static final String VERB = "VB";
    public static final String PRONOUN = "PRP";
    //public static final Set<String> PRONOUNS = new HashSet<String>(Arrays.asList("he",
    //        "she","the_album","this_album"));
    
    public static final LinkedHashMap<String, Set<String>> CLASSES = new LinkedHashMap<String, Set<String>>() {
        {
            put(Property.dbo_Album, new HashSet<String>(Arrays.asList("the_album_", "this_album_")));
            put(Property.dbo_Book, new HashSet<String>(Arrays.asList("the_book_", "this_book_")));
            put(Property.dbo_City, new HashSet<String>(Arrays.asList("the_city_", "this_city_")));
            put(Property.dbo_Colour, new HashSet<String>(Arrays.asList("the_color_", "this_color_", "it_is_", "this_is_")));
            put(Property.dbo_Company, new HashSet<String>(Arrays.asList("the_company_", "this_company_", "it_is_", "this_is_")));
            put(Property.dbo_Country, new HashSet<String>(Arrays.asList("the_country_", "this_country_", "it_is_", "this_is_")));
            put(Property.dbo_Currency, new HashSet<String>(Arrays.asList("the_currency_", "this_currency_", "it_is_", "this_is_")));
            put(Property.dbo_Film, new HashSet<String>(Arrays.asList("the_film_", "this_film_", "it_is_", "this_is_")));
            put(Property.dbo_Location, new HashSet<String>(Arrays.asList("the_location_", "this_location_", "it_is_", "this_is_")));
            put(Property.dbo_Mountain, new HashSet<String>(Arrays.asList("the_mountain_", "this_mountain_", "it_is_", "this_is_")));
            put(Property.dbo_Place, new HashSet<String>(Arrays.asList("the_place_", "this_place_", "it_is_", "this_is_")));
            put(Property.dbo_River, new HashSet<String>(Arrays.asList("the_river_", "this_river_", "it_is_", "this_is_")));
            put(Property.dbo_TelevisionShow, new HashSet<String>(Arrays.asList("the_television_show_", "this_television_show_", "the_show", "this_show")));
            put(Property.dbo_Actor, new HashSet<String>(Arrays.asList("he", "she")));
            put(Property.dbo_Politician, new HashSet<String>(Arrays.asList("he", "she")));
            put(Property.dbo_Person, new HashSet<String>(Arrays.asList("he", "she")));
        }
    };


    public static final String NOUN = "NN";
    public static final String WORD = "WORD";
    public static final String SENTENCE = "SENTENCE";
    public static final List<String> ENGLISH_STOPWORDS = Arrays.asList("i", "me", "my", "myself", "we", "our", "ours",
            "ourselves", "you", "your", "yours", "yourself",
            "yourselves", "he", "him", "his", "himself", "she",
            "her", "hers", "herself", "it", "its", "itself", "they",
            "them", "their", "theirs", "themselves", "what", "which",
            "who", "whom", "this", "that", "these", "those", "am",
            "is", "are", "was", "were", "be", "been", "being", "have",
            "has", "had", "having", "do", "does", "did", "doing", "a", "an",
            "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into",
            "through", "during", "before", "after", "above", "below", "to", "from",
            "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why",
            "how", "all", "any", "both", "each", "few", "more", "most", "other",
            "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now", "un", "ein", "und", "il", "est", "ist", " né", "à");

    public static final List<String> ENGLISH_SELECTED_STOPWORDS = Arrays.asList(
            "i", "me", "my", "myself", "we", "our", "ours",
            "ourselves", "you", "your", "yours", "yourself",
            "yourselves", "it", "its", "itself", "they",
            "them", "their", "theirs", "themselves", "what", "which",
            "who", "whom", "this", "that", "these", "those", "am",
            "is", "are", "was", "were", "be", "been", "being", "have",
            "has", "had", "having", "do", "does", "did", "doing", "a", "an",
            "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "further", "then", "once", "here", "there", "when", "where", "why",
            "how", "all", "any", "both", "each", "few", "more", "most", "other",
            "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now","again","her","currently","also");

    /*public static final Set<String> dbpPartyWords = new TreeSet<String>(Arrays.asList("labour", "party", "british", "parliament", "general",
            "liberal", "nova", "scotia", "minister", "progressive",
            "house", "ontario", "assembly", "legislative", "canada",
            "alberta", "provincial", "australian", "south", "new", "wales",
            "parliament", "conservative", "democratic", "american", "member",
            "state", "district", "politique", "homme", "membre", "français",
            "député", "alberta", "indian", "congress", "national", "constituency",
            "republican", "state", "australian", "legislative", "south"));*/
    public static final Set<String> dbpPartyWords = new TreeSet<String>(Arrays.asList("american", "democratic"));

    //public static final Map<String,Set<String>> propertySelectedWords = new TreeMap<String,Set<String>>();
    public String modelDir = "src/main/resources/models/";
    public static String posTagFile = "en-pos-maxent.bin";
    public static String lemmaDictionary = "en-lemmatizer.txt";

}
