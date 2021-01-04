/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.writer.table;

import citec.wikipedia.writer.sparql.SparqlQueryImpl;
import citec.wikipedia.writer.utils.FileFolderUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import citec.wikipedia.writer.sparql.SparqlQuery;

/**
 *
 * @author elahi
 */
public class EntityTable {

    private List<DBpediaEntity> dbpediaEntities = new ArrayList<DBpediaEntity>();
    private String tableName;
    private String inputFileName;
    public List<String> urlsWithNoAbstract=new ArrayList<String>();

    public EntityTable(String inputFileName, String tableName, List<DBpediaEntity> dbpediaEntities) throws Exception {
        this.inputFileName = inputFileName;
        this.tableName = tableName;
        this.dbpediaEntities = dbpediaEntities;
        FileFolderUtils.convertToJson(dbpediaEntities, tableName);
    }

    public EntityTable(String inputFileName, String dbpediaDir, String freqClass, String freqProp, LinkedHashSet<String> entities, String POS_TAGGER,Integer limit) throws Exception {
        this.inputFileName = inputFileName;
        this.tableName = dbpediaDir + freqClass + "_" + freqProp;
        this.setProperties(entities, POS_TAGGER, freqClass,limit);
        FileFolderUtils.convertToJson(dbpediaEntities, tableName);
        if(!urlsWithNoAbstract.isEmpty())
           FileFolderUtils.listToFiles(urlsWithNoAbstract, tableName+".txt");
    }

    private void setProperties(LinkedHashSet<String> entities, String POS_TAGGER, String freqClass, Integer limit) throws Exception {
        Integer index = 0, total = entities.size();

        for (String entityString : entities) {
            entityString = "Donald_Trump";
            String entityUrl = DBpediaEntity.getEntityUrl(entityString);

            SparqlQuery curlSparqlQuery = new SparqlQueryImpl(entityUrl);

            if (curlSparqlQuery.isAbstractContains()) {
                DBpediaEntity dbpediaEntity = new DBpediaEntity(inputFileName, freqClass, entityString, curlSparqlQuery.getProperties(), POS_TAGGER);
                dbpediaEntities.add(dbpediaEntity);
            } else {
                urlsWithNoAbstract.add(entityUrl);
            }
            System.out.println(freqClass+" entity:" + entityUrl + " count:" + index + " total:" + total); 
              index=index+1;


            if (limit != -1) {
                if (index > limit) {
                    break;
                }
            }


        }
    }

    public void setDbpediaEntities(List<DBpediaEntity> dbpediaEntities) {
        this.dbpediaEntities = dbpediaEntities;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DBpediaEntity> getDbpediaEntities() {
        return dbpediaEntities;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getUrlsWithNoAbstract() {
        return urlsWithNoAbstract;
    }

    @Override
    public String toString() {
        return "{" + "tableName=" + tableName + "," + "dbpediaEntities=" + dbpediaEntities + '}';
    }

}
