/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.entity.linking;

import citec.wikipedia.entity.utils.FileUtilsAnno;
import citec.wikipedia.writer.constants.DirectoryLocation;
import static citec.wikipedia.writer.utils.FileFolderUtils.appendToFile;
import citec.wikipedia.writer.utils.FormatAndMatch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 */
public class LinkingDictionary {

    private Map<String, TreeMap<String, List<String>>> objectToAnchorsAlphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();
    private Map<String, TreeMap<String, List<String>>> termToObjectsAlphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();

    public static final String UNICODE = "UTF-8";
    //private static String anchors = "src/main/resources/dbpedia/anchors/";
    private String anchors = null;
    private String inputDir = null;
    private String outputDir = null;
    private String fileExtension = null;
    private List<File> allFiles = new ArrayList<File>();
    private static String termDir = "term/";
    private static String objectDir = "object/";
    private static String achorFileTsv = "anchors_sorted_by_frequency.tsv";
    public static Set<String> alphabetSets = Stream.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z").collect(Collectors.toCollection(HashSet::new));

    public LinkingDictionary(String anchors, String fileExtension) throws IOException {
        this.anchors = anchors;
        this.inputDir = anchors + termDir;
        this.outputDir = anchors + objectDir;
        this.fileExtension=fileExtension;
        this.allFiles = FileUtilsAnno.getFiles(inputDir, fileExtension);
    }
    
    public void prepareTermObjectsDictioarny() throws IOException, Exception {
        List<File> alphabetFiles = FileUtilsAnno.getFiles(inputDir, fileExtension);
        for (File file : alphabetFiles) {
            TreeMap<String, List<String>> alphabet = fileKeyToValue(inputDir + file.getName());
            String alphabetStr = file.getName().replaceAll(fileExtension, "");
            termToObjectsAlphabetInfo.put(alphabetStr, alphabet);
        }
    }

    public void prepareObjectAnchorsDictioarny() throws IOException {
        for (File file : allFiles) {
            Integer index = 0;
            //System.out.println("filename:" + file.getName());
            TreeMap<String, List<String>> objectTermList = fileValueToKey(inputDir + file.getName());
            for (String object : objectTermList.keySet()) {
                List<String> terms = objectTermList.get(object);
                //System.out.println("object:" + object);
                //System.out.println("term:" + objectTermList.get(object));
                String alphabetFileName = null;
                object = object.strip().trim();
                if (object.isEmpty()) {
                    continue;
                }
                if (object.length() < 2) {
                    continue;
                }

                Character ch = object.charAt(0);
                String str = String.valueOf(ch).toLowerCase().strip().trim();

                if (alphabetSets.contains(str)) {
                    alphabetFileName = outputDir + str + ".txt";
                } else {
                    alphabetFileName = outputDir + "other" + ".txt";
                }

                index = index + 1;
                /*if (index > 10) {
                    break;
                }*/

                object = object.stripLeading();
                String line = object + " = " + terms.toString().replace(",", "+");
                appendToFile(alphabetFileName, line);

            }

            //String alphabetStr = file.getName().replaceAll(fileExtension, "");
            //alphabetInfo.put(alphabetStr, alphabet);
        }

        /*for (String key : alphabetInfo.keySet()) {
            //System.out.println(key);
            TreeMap<String, List<String>> hash = alphabetInfo.get(key);
            for (String term : hash.keySet()) {
               // System.out.println("term:" + term);
                //System.out.println("kb:" + hash.get(term));
            }
        }*/
    }

    public static TreeMap<String, List<String>> fileKeyToValue(String fileName) throws FileNotFoundException, IOException {
        TreeMap<String, List<String>> hash = new TreeMap<String, List<String>>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    if (line.contains("=")) {
                        String[] info = line.split("=");
                        String key = info[0];
                        String value = info[1];
                        key = FormatAndMatch.format(key);
                        List<String> values = new ArrayList<String>();
                        if (hash.containsKey(key)) {
                            values = hash.get(key);
                        }
                        values.add(value);
                        hash.put(key, values);
                    }

                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public static TreeMap<String, List<String>> fileValueToKey(String fileName) throws FileNotFoundException, IOException {
        TreeMap<String, List<String>> hash = new TreeMap<String, List<String>>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    if (line.contains("=")) {
                        String[] info = line.split("=");
                        String term = info[0];
                        term = term.strip().trim().toLowerCase().replace(" ", "_");
                        term = term.stripLeading().strip().stripTrailing();
                        String object = info[1];
                        //object = FormatAndMatch.format(object);
                        List<String> termList = new ArrayList<String>();
                        if (hash.containsKey(object)) {
                            termList = hash.get(object);
                        }
                        termList.add(term);
                        hash.put(object, termList);
                    }

                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hash;
    }

    private  Map<String, TreeMap<String, String>> getAlphabetic(String fileName, Set<String> alphabetSets) {
        Map<String, TreeMap<String, String>> alphabeticAnchors = new TreeMap<String, TreeMap<String, String>>();
        BufferedReader reader;
        String line = "";
        String firstLetter = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            Integer index = 0;
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    if (line.contains("\t")) {
                        String[] info = line.split("\t");
                        String anchor = info[1];
                        anchor = anchor.replace("\"", "");

                        String kb = info[0];
                        Character ch = null;
                        String str = null;
                        String alphabetFileName = null;
                        if (anchor.length() >= 1) {
                            ch = anchor.charAt(0);
                            str = String.valueOf(ch).toLowerCase().trim();

                            if (alphabetSets.contains(str)) {
                                alphabetFileName = anchors + str + ".txt";
                            } else {
                                alphabetFileName = anchors + "other" + ".txt";
                            }

                            index = index + 1;
                            //System.out.println(index + "line= " + line);

                            anchor = anchor.stripLeading();
                            line = anchor + " = " + kb;
                            appendToFile(alphabetFileName, line);
                        }
                    }
                }
            }
            //System.out.println("total= " + index);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alphabeticAnchors;
    }

    public Map<String, TreeMap<String, List<String>>> getAlphabetInfo() {
        List<File> alphabaticFiles = FileUtilsAnno.getFiles(this.outputDir, fileExtension);

         for (File file : alphabaticFiles) {
              //System.out.println(file);
              String alphabet=file.getName().replace(".txt","");
               //if(!alphabet.contains("a"))
               //  continue;
              TreeMap<String, List<String>> objectTerms=this.readAlphabeticDictionary(file);
              this.objectToAnchorsAlphabetInfo.put(alphabet, objectTerms);
         }
        
        //System.out.println(this);
        return objectToAnchorsAlphabetInfo;
    }

    private TreeMap<String, List<String>> readAlphabeticDictionary(File file) {
        TreeMap<String, List<String>> hash = new TreeMap<String, List<String>>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    if (line.contains("=")) {
                        String[] info = line.split("=");
                        String object = info[0];
                        String terms = info[1];
                        List<String> termList = this.getTermList(terms);
                        hash.put(object, termList);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hash;
    }

    private List<String> getTermList(String patternStr) {
        List<String> termList = new ArrayList<String>();
        String terms = StringUtils.substringBetween(patternStr, "[", "]");
        terms = terms.replace("+", " ");
        if (terms.contains(" ")) {
             String[] info = terms.split(" ");
            termList = Arrays.asList(info);
            
        } else {
           termList.add(terms);
        }
        return termList;
    }
    
    public static void main(String[] agrs) throws IOException, Exception {
        //System.out.println(DirectoryLocation.anchors);
        LinkingDictionary linkingDictionary = new LinkingDictionary(DirectoryLocation.anchors, ".txt");
        //linkingDictionary.prepareDictioarny();
        Map<String, TreeMap<String, List<String>>> alphabetInfo =linkingDictionary.getAlphabetInfo();
        for (String alphabet : alphabetInfo.keySet()) {
            System.out.println("alphabet:"+alphabet);
            TreeMap<String, List<String>> hash = alphabetInfo.get(alphabet);
            for (String object : hash.keySet()) {
                //System.out.println("alphabet:"+alphabet+ "object:" + object+"!!!!!!!!!!!!!");
                //System.out.println("terms:" + hash.get(object));
            }
        }
        System.out.println(alphabetInfo.keySet());
    }

    public Map<String, TreeMap<String, List<String>>> getObjectToAnchorsAlphabetInfo() {
        return objectToAnchorsAlphabetInfo;
    }

    public Map<String, TreeMap<String, List<String>>> getTermToObjectsAlphabetInfo() {
        return termToObjectsAlphabetInfo;
    }

}
