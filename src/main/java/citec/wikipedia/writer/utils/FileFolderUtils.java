/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.writer.utils;

import citec.wikipedia.writer.table.DBpediaEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class FileFolderUtils {

    public static final String UNICODE = "UTF-8";

    private static String anchors = "src/main/resources/dbpedia/anchors/";
    private static String input = "input/";
    private static String achorFileTsv = "anchors_sorted_by_frequency.tsv";
    

    public static void createDirectory(String location) throws IOException {
        Path location_path = Paths.get(location);
        /*if (Files.exists(location_path)) {
            Files.delete(location_path);
        }*/
        Files.createDirectories(location_path);

    }

    public static List<File> getFiles(String fileDir, String ntriple) {
        File dir = new File(fileDir);
        FileFilter fileFilter = new WildcardFileFilter("*" + ntriple);
        File[] files = dir.listFiles(fileFilter);
        return List.of(files);
    }

    public static TreeSet<File> getFilesSet(String fileDir, String ntriple) {
        File dir = new File(fileDir);
        FileFilter fileFilter = new WildcardFileFilter("*" + ntriple);
        File[] files = dir.listFiles(fileFilter);
        return new TreeSet<File>(List.of(files));
    }
    
     public static String[] getFilesString(String fileDir, String ntriple) {
         String[] files = new File(fileDir).list();
        return files;
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

    public static List<String> getHash(String fileName) throws FileNotFoundException, IOException {
        List<String> lists = new ArrayList<String>();
        List<String> lines = new ArrayList<String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();

                if (line != null) {
                    line = line.replace("http", "\nhttp");
                    lines = IOUtils.readLines(new StringReader(line));
                    for (String value : lines) {
                        //System.out.println("test:" + value);
                        lists.add(value);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }


    public static List<String> getList(String fileName) throws FileNotFoundException, IOException {
        List<String> entities = new ArrayList<String>();

        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    String url = line.trim();
                    entities.add(url);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public static List<String> getSortedList(String fileName, Integer thresold, Integer listSize) throws FileNotFoundException, IOException {
        List<String> words = new ArrayList<String>();
        List<String> finalWords = new ArrayList<String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    if (line.contains(" ")) {
                        //System.out.println(line);
                        String[] info = line.split(" ");
                        Integer count = Integer.parseInt(info[0].trim());
                        String word = info[1].trim();

                        if (count > thresold) {
                            //System.out.println(line);
                            words.add(word);
                        }
                    }

                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer size = 0;
        for (String word : words) {

            if (size == listSize) {
                break;
            } else {
                finalWords.add(word);
            }
            size = size + 1;
        }
        return finalWords;
    }

    public static void listToFiles(List<String> list, String fileName) {
        String str = "";
        Integer number = -1, index = 0;
        for (String element : list) {
            if (element.contains("http")) {
                index++;
                String line = element + "\n";
                str += line;
                if (index == number) {
                    break;
                }
            }

        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            System.out.println(str);
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileFolderUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void stringToFiles(String str, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileFolderUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void writeToTextFile(String str, String filename) {
        if (str != null) {
            stringToFiles(str, filename);
        } else {
            return;
        }
    }

   

    public static void writeDictionaryToJsonFile(Map<String, Map<Integer, String>> units, String fileName) throws Exception {
        if (units.isEmpty()) {
            throw new Exception("no data found to write in the json file!!");
        }
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(Paths.get(fileName).toFile(), units);
    }

    public static String urlUnicodeToString(String url) throws Exception {
        URI uri = new URI(url);
        String urlStr = uri.getQuery();
        return urlStr;
    }

    public static String stringToUrlUnicode(String string) throws UnsupportedEncodingException {
        String encodedString = URLEncoder.encode(string, "UTF-8");
        return encodedString;
    }

    public static String readHtmlFile() {

        return null;
    }

    public static void appendToFile(String fileName, String line) {
        File file = new File(fileName);
        boolean b;
        if (file.exists()) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.append(System.getProperty("line.separator"));
                bw.append(line);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            stringToFiles(line, fileName);
        }
    }

    public static void convertToJson(List<DBpediaEntity> dbpediaEntities, String filename) {
        if (dbpediaEntities.isEmpty()) {
            System.out.println("the list is empty!!!");
            return;
        }
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(Paths.get(filename + ".json").toFile(), dbpediaEntities);
        } catch (IOException ex) {
            System.out.println("can not convert DBpediaEntity to Json file!!!");
        }
    }

    public static String getClassDir(String dbo_Politician) {
        return dbo_Politician.split(":")[1];
    }

}
