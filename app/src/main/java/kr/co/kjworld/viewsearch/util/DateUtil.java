package kr.co.kjworld.viewsearch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    static public String changeDateString(String targetStr, String beforeType, String afterType)
    {
        SimpleDateFormat beforeDateFormat =  new SimpleDateFormat(beforeType);
        Date date = null;
        try {
            date = beforeDateFormat.parse(targetStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat afterDateFormat = new SimpleDateFormat(afterType);
        String dateStr = afterDateFormat.format(date);

        return dateStr;
    }
}
