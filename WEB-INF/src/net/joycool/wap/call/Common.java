package net.joycool.wap.call;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 常用功能
 * 
 * @author bomb
 *  
 */
public class Common {

    /**
     * 一个随机整数
     * @param callParam<br>
     *    1、整数的输出格式
     *  
     */
    public static String randomInt(CallParam callParam) {
        String[] params = callParam.getParams();
        int max = StringUtil.toId(params[0]);
        int num = new Random().nextInt(max);
        if (params.length > 1) { // 返回一个格式化的数字
            DecimalFormat df = new DecimalFormat(params[1]);
            return df.format(num);
        }
        return String.valueOf(num);
    }

    /**
     * 一个随机小数
     * @param callParam<br>
     *    1、小数的输出格式
     *  
     */
    public static String randomFloat(CallParam callParam) {
        String[] params = callParam.getParams();
        float max = StringUtil.toFloat(params[0]);
        float num = new Random().nextFloat() * max;
        if (params.length > 1) { // 返回一个格式化的数字
            DecimalFormat df = new DecimalFormat(params[1]);
            return df.format(num);
        }
        return String.valueOf(num);
    }

    /**
     * 当前时间
     * @param callParam<br>
     *            1、时间的输出格式
     *  
     */
    public static String time(CallParam callParam) {
        String param = callParam.getParam();
        SimpleDateFormat sdf = new SimpleDateFormat(param);
        return sdf.format(new Date());
    }
    
    /**
     * 剩余时间
     * @param callParam<br>
     *            1、设定的时间, 格式yyyy-MM-dd HH:mm:ss
     *  
     */
    public static String timeLeft(CallParam callParam) {
        String param = callParam.getParam();
        Date d = DateUtil.parseTime(param);
        if(d == null)
        	return "??";
        // 大于1天的显示天数，小于的显示小时数或者分钟……
        long now = System.currentTimeMillis();
        int dd = DateUtil.dayDiff(now, d.getTime());
        if(dd != 0)
        	return dd + "天";
        int sec = (int)((d.getTime() - now) / 1000);
        if(sec < 0)
        	return "0秒";
        if(sec >= 3600)
        	return (sec / 3600) + "小时";
        if(sec >= 60)
        	return (sec / 60) + "分钟";
        return sec + "秒";
    }
    
    /**
     * 过去的时间
     * @param callParam<br>
     *            1、设定的时间, 格式yyyy-MM-dd HH:mm:ss
     *  
     */
    public static String timePast(CallParam callParam) {
        String param = callParam.getParam();
        Date d = DateUtil.parseTime(param);
        if(d == null)
        	return "??";
//      显示过去的天数
        long now = System.currentTimeMillis();
        int dd = DateUtil.dayDiff(d.getTime(), now);
        return dd + "天";
    }

    /**
     * 显示网页参数变量内容
     * @param callParam<br>
     *            1、参数名 <br>
     *            2、参数名 <br>
     *            ... 多个参数
     */
    public static String print(CallParam callParam) {
        String[] params = callParam.getParams();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < params.length; i++)
            sb.append(params[i]);
        return sb.toString();
    }

}