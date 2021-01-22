
import static citec.wikipedia.annotation.linking.EntityAnnotation.PRONOUN;
import citec.wikipedia.annotation.utils.Pronoun;
import citec.wikipedia.extractor.utils.FormatAndMatch;
import java.util.HashSet;
import java.util.Set;
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
public class FormatAndMatchTest {

    public static void main(String[] args) {
        /*String input = "<div>this is a <b>good</b> apple</div>";
      System.out.println(input);
      String s = replaceBetween(input, "<b>", "</b>", "big");
      System.out.println(s);
      s = replaceBetween(input, "<b>", "</b>", true, true, "big");
      System.out.println(s);

      String input2 = "there's more than one way to skin a cat";
      System.out.println(input2);
      String s2 = replaceBetween(input2, "more", "skin a", " to ");
      System.out.println(s2);
      s2 = replaceBetween(input2, "more", "skin a", true, true, "no");
      System.out.println(s2);

      System.out.println("-- without regex --");
      replaceBetweenWithoutRegex(input2, "more", "skin a", true, true, "no");
      System.out.println(s2);*/

        String text=null;
        text = "He is the chairman and president of The Trump Organization, which is the principal holding company for his real estate ventures and other business interests.";
        //text = "Donald John Trump (born June 14, 1946) is an American businessman, author, television producer, politician, and the Republican Party nominee for President of the United States in the 2016 election.";

        text = text.toLowerCase().replace(" ", "_");
        Set<String> pronouns = new HashSet<String>();
        pronouns.add("he");
        Pair<String, String> test = Pronoun.replacePronoun(text, PRONOUN, pronouns);
        System.out.println(test);

    }

   

}
