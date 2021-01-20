package citec.wikipedia.writer.analyzer;


import static citec.wikipedia.writer.analyzer.TextAnalyzer.POS_TAGGER_WORDS;
import citec.wikipedia.writer.table.DBpediaEntity;
import citec.wikipedia.writer.utils.FileFolderUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Paths;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elahi
 */
public class POStagging {
    
    private String outputDir=null;

    public POStagging(String dbo_Class, String outputDir, Map<String, List<DBpediaEntity>> fileDBpediaEntities) throws Exception {
        this.outputDir = outputDir;
        if (!Files.isDirectory(Paths.get(outputDir))) {
            FileFolderUtils.createDirectory(outputDir);
        }
        this.addPosTag(fileDBpediaEntities);
    }


    private void addPosTag(Map<String, List<DBpediaEntity>> fileDBpediaEntities) throws Exception {

        for (String fileName : fileDBpediaEntities.keySet()) {
            List<DBpediaEntity> correctedEntities = new ArrayList<DBpediaEntity>();
            List<DBpediaEntity> DBpediaEntitys = fileDBpediaEntities.get(fileName);
            Integer total = DBpediaEntitys.size();
            Integer count = 0;
            for (DBpediaEntity dbpediaEntity : DBpediaEntitys) {
                //System.out.println();
                //System.out.println(subject + " count=" + count + " total=" + total + " " + fileName + "........................");
                //System.out.println("entity:" + dbpediaEntity.getEntityUrl());
                String text = dbpediaEntity.getText();
                if (text.length() < 10) {
                    continue;
                }
                this.isPostagged(dbpediaEntity);

                if (!this.isPostagged(dbpediaEntity)) {
                    if (text != null) {
                        Analyzer analyzer = new Analyzer(text, POS_TAGGER_WORDS, 5);
                        Set<String> verbs = analyzer.getVerbs();
                        DBpediaEntity newDBpediaEntity = new DBpediaEntity(analyzer, dbpediaEntity);
                        correctedEntities.add(newDBpediaEntity);
                    } else {
                        correctedEntities.add(dbpediaEntity);
                    }

                }
                count = count + 1;

            }
            if (!DBpediaEntitys.isEmpty()) {
                FileFolderUtils.convertToJson(correctedEntities, this.outputDir+fileName.replace(".json", ""));
            }

        }

    }
   

    private Boolean isPostagged(DBpediaEntity dbpediaEntity) {
        if (dbpediaEntity.getNouns().isEmpty()
                || dbpediaEntity.getAdjectives().isEmpty()
                || !dbpediaEntity.getVerbs().isEmpty()) {
 
            return false;
        }
        return true;
    }


}
