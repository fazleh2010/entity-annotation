
import citec.correlation.core.analyzer.TextAnalyzer;
import citec.correlation.wikipedia.calculation.PatternCalculation;
import citec.correlation.wikipedia.dic.lexicon.Lexicon;
import citec.correlation.wikipedia.element.PropertyNotation;
import citec.correlation.wikipedia.evalution.Comparision;
import static citec.correlation.wikipedia.parameters.DirectoryLocation.allPoliticianFile;
import static citec.correlation.wikipedia.parameters.DirectoryLocation.dbpediaDir;
import static citec.correlation.wikipedia.parameters.DirectoryLocation.patternDir;
import static citec.correlation.wikipedia.parameters.DirectoryLocation.qald9Dir;
import static citec.correlation.wikipedia.parameters.MenuOptions.PATTERN_CALCULATION;
import citec.correlation.wikipedia.utils.FileFolderUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elahi
 */
public class PatternCalculationTest {

    private String dbo_ClassName = PropertyNotation.dbo_Politician;
    private String inputFile = allPoliticianFile;
    private String patternFileName = "pattern";

    @Test
    public void main() throws IOException, Exception {
         LinkedHashMap<String,String> classFileName = new LinkedHashMap<String,String>();
        //classFileName.put(PropertyNotation.dbo_Album,"albums.txt");
        //classFileName.put(PropertyNotation.dbo_Film,"flims.txt");
        //classFileName.put(PropertyNotation.dbo_TelevisionShow,"televisionShow.txt");
        classFileName.put(PropertyNotation.dbo_alliance, "alliance.txt");
        classFileName.put(PropertyNotation.dbo_Company, "Company.txt");
        classFileName.put(PropertyNotation.dbo_Writer, "Writer.txt");
        classFileName.put(PropertyNotation.dbo_battle, "battle.txt");
        classFileName.put(PropertyNotation.dbo_Colour, "Colour.txt");
        classFileName.put(PropertyNotation.dbo_Color, "Color.txt");
        classFileName.put(PropertyNotation.dbo_Currency, "Currency.txt");
        classFileName.put(PropertyNotation.dbo_state, "state.txt");
          classFileName.put(PropertyNotation.dbo_Location, "Location.txt");
        
        
        String classDir = FileFolderUtils.getClassDir(dbo_ClassName) + "/";
        String inputDir = dbpediaDir + classDir + patternDir;
        System.out.println(inputDir);
        PatternCalculation patternCalculation = new PatternCalculation(inputDir, inputFile, dbo_ClassName);
        //Lexicon lexicon = new Lexicon(qald9Dir);
        //lexicon.prepareLexiconForEvalution(patternCalculation.getPatternEntities(), patternFileName);
    }

}
