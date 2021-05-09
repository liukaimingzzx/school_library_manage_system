package com.database.test.util;

import java.io.DataInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {


    public String getCurrentTime(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=simpleDateFormat.format(calendar.getTime());
        return time;
    }

    public int calLateDays(String oldTime,String NewTime) {
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat1.parse(oldTime);
            date2 = simpleDateFormat1.parse(NewTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.add(Calendar.DATE,31);
        Date ddl = cal.getTime();
        int days = (int) ((date2.getTime() - ddl.getTime()) / (1000*3600*24));
        System.out.println(days);
        return days;

    }

}
