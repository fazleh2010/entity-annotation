/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.correlation.wikipedia.utils;

import citec.correlation.wikipedia.pattern.DBpediaEntityPattern;
import citec.wikipedia.writer.table.DBpediaEntity;
import citec.wikipedia.writer.utils.FileFolderUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class FileFolderUtilsAnnotation  {

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
            System.err.println("no DBpedia entities are found!!"+exp.getMessage());
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
