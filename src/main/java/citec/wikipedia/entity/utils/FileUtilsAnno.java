/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.entity.utils;

import citec.wikipedia.entity.pattern.DBpediaEntityPattern;
import citec.wikipedia.writer.table.DBpediaEntity;
import static citec.wikipedia.writer.utils.FileFolderUtils.appendToFile;
import citec.wikipedia.writer.utils.FormatAndMatch;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author elahi
 */
public class FileUtilsAnno {

    public static final String UNICODE = "UTF-8";

    private static String anchors = "src/main/resources/dbpedia/anchors/";
    private static String input = "input/";
    private static String objectDir = "object/";
    private static String achorFileTsv = "anchors_sorted_by_frequency.tsv";
    private static Set<String> alphabetSets = Stream.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z").collect(Collectors.toCollection(HashSet::new));

    public static void main(String a[]) throws IOException {
        String fileName = anchors + input + achorFileTsv;
    }

    /*public static Map<String, TreeMap<String, List<String>>> getAlphabetInfo(String anchors, String fileExtension) throws IOException {
        Map<String, TreeMap<String, List<String>>> alphabetInfo = new TreeMap<String, TreeMap<String, List<String>>>();
        String inputDir=anchors+input;

        List<File> allFiles = getFiles(inputDir, fileExtension);
        String outputDir=anchors+objectDir;
        
        for (File file : allFiles) {
            Integer index=0;
            System.out.println("filename:"+file.getName());
            TreeMap<String, List<String>> objectTermList = fileValueToKey(inputDir+ file.getName());
            for (String object : objectTermList.keySet()) {
                List<String> terms = objectTermList.get(object);
                System.out.println("object:" + object);
                System.out.println("term:" + objectTermList.get(object));
                String alphabetFileName = null;
                object=object.strip().trim();
                if(object.isEmpty())
                    continue;
                 if (object.length() < 2) 
                     continue;
                    
                Character ch = object.charAt(0);
                String str = String.valueOf(ch).toLowerCase().strip().trim();

                if (alphabetSets.contains(str)) {
                    alphabetFileName = outputDir + str + ".txt";
                } else {
                    alphabetFileName = outputDir + "other" + ".txt";
                }

                index = index + 1;
              

                object = object.stripLeading();
                String line = object + " = " + terms.toString().replace(",", "+");
                appendToFile(alphabetFileName, line);
                
            }

            //String alphabetStr = file.getName().replaceAll(fileExtension, "");
            //alphabetInfo.put(alphabetStr, alphabet);
        }
        
       
       
        return alphabetInfo;
    }*/
    public static Map<String, List<DBpediaEntity>> readTables(String rawFilesDir, String dbo_Class, String extension) {
        return readTables(getFiles(rawFilesDir, dbo_Class, extension));
    }

    public static Map<String, List<DBpediaEntity>> readTables(String rawFilesDir, String extension) {
        return readTables(getFiles(rawFilesDir, extension));
    }

    public static Map<String, List<DBpediaEntity>> readTables(List<File> list) {
        Map<String, List<DBpediaEntity>> fileDBpediaEntities = new TreeMap<String, List<DBpediaEntity>>();
        try {
            for (File file : list) {
                ObjectMapper mapper = new ObjectMapper();
                List<DBpediaEntity> dbpediaEntitys = mapper.readValue(file, new TypeReference<List<DBpediaEntity>>() {
                });
                fileDBpediaEntities.put(file.getName(), dbpediaEntitys);
            }
            return fileDBpediaEntities;
        } catch (Exception exp) {
            System.err.println("no DBpedia entities are found!!" + exp.getMessage());
        }
        return fileDBpediaEntities;
    }

    public static List<File> getFiles(String fileDir) {
        File dir = new File(fileDir);
        File[] files = dir.listFiles();
        return List.of(files);
    }

    public static List<File> getFiles(String fileDir, String extension) {
        String[] files = new File(fileDir).list();
        List<File> selectedFiles = new ArrayList<File>();
        for (String fileName : files) {
            if (fileName.contains(extension)) {
                selectedFiles.add(new File(fileDir + fileName));
            }
        }
        return selectedFiles;
    }

    public static List<File> getFiles(String fileDir, String category, String extension) {
        String[] files = new File(fileDir).list();
        List<File> selectedFiles = new ArrayList<File>();
        for (String fileName : files) {
            if (fileName.contains(category) && fileName.contains(extension)) {
                selectedFiles.add(new File(fileDir + fileName));
            }
        }
        return selectedFiles;
    }

    public static void convertToJson(List<DBpediaEntityPattern> correctedEntities, String dir, String filename) throws IOException, Exception {
        if (correctedEntities.isEmpty()) {
            throw new Exception("the list is empty!!!");
        }
        filename = dir + filename;
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(Paths.get(filename + ".json").toFile(), correctedEntities);
    }
    

}
