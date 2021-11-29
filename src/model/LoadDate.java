package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadDate {
    public static String loadDate(){
    Date date = new Date();
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(date);
    }
}
