/*
 * Created on 2006-11-21
 *
 */
package net.wxsj.framework;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * @author lbj
 *  
 */
public class DateFrk {

    public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }

    public static ArrayList getMonthList() {
        ArrayList ml = new ArrayList();
        Calendar cal = Calendar.getInstance();
        cal.set(2006, 10, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");        
        Calendar now = Calendar.getInstance();
        while (cal.before(now)
                || cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {            
            ml.add(sdf.format(cal.getTime()));
            cal.add(Calendar.MONTH, 1);
        }

        return ml;
    }

    public static void main(String[] args) {
        ArrayList ml = getMonthList();
        Iterator itr = ml.iterator();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
    }
}
