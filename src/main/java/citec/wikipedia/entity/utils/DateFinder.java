/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package citec.wikipedia.entity.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.javatuples.Pair;

/**
 *
 * @author elahi
 */
public class DateFinder {

    private static Pair<Boolean,String> findYear(String mystring) throws ParseException {
        Calendar mydate = new GregorianCalendar();
        try {
            Date thedate = new SimpleDateFormat("yyyy", Locale.ENGLISH).parse(mystring);
            mydate.setTime(thedate);
            /*System.out.println("mydate -> " + mydate);
        System.out.println("year   -> " + mydate.get(Calendar.YEAR));
        System.out.println("month  -> " + mydate.get(Calendar.MONTH));
        System.out.println("dom    -> " + mydate.get(Calendar.DAY_OF_MONTH));
        System.out.println("dow    -> " + mydate.get(Calendar.DAY_OF_WEEK));
        System.out.println("hour   -> " + mydate.get(Calendar.HOUR));
        System.out.println("minute -> " + mydate.get(Calendar.MINUTE));
        System.out.println("second -> " + mydate.get(Calendar.SECOND));
        System.out.println("milli  -> " + mydate.get(Calendar.MILLISECOND));
        System.out.println("ampm   -> " + mydate.get(Calendar.AM_PM));
        System.out.println("hod    -> " + mydate.get(Calendar.HOUR_OF_DAY));*/
            Integer year=mydate.get(Calendar.YEAR);
            return new Pair<Boolean,String>(Boolean.TRUE,year.toString());
        }catch(Exception exp){
            return new Pair<Boolean,String>(Boolean.FALSE,mystring);
        }


    }
    
    public static void main(String [] args) throws ParseException{
        System.out.println(findYear("2002").getValue0());
        System.out.println(findYear("2002").getValue1());
    }

}
