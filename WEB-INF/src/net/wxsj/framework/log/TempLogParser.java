/*
 * Created on 2007-2-5
 *
 */
package net.wxsj.framework.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJaLineService;
import net.wxsj.util.StringUtil;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-2-5
 * 
 * 说明：
 */
public class TempLogParser {
    public static String resultDirStr = "/usr/local/joycool-rep/joycoolLog/tempLogParse";

    public static String logFileStr = "/usr/local/joycool-rep/joycoolLog/pv.log";

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-2-5
     * 
     * 说明：分析
     * 
     * 参数及返回值说明：
     * 
     * @param date
     * @param request
     * @param response
     */
    public static void parse(String date) {
        //日期
        if (date == null) {
            return;
        }

        //log file
        File logFile = new File(logFileStr + "." + date);
        if (!logFile.exists()) {
            return;
        }

        File outputFile = new File(resultDirStr + "/" + date + ".txt");

        //统计
        Hashtable columnPvs = new Hashtable();

        try {
            BufferedReader br = new BufferedReader(new FileReader(logFile));
            String str = br.readLine();
            int columnId;
            String sessionId = null;
            String userId = null;
            String mobile = null;
            Integer count = null;
            Pattern p = Pattern.compile("columnId=[0-9]*");
            Matcher m = null;
            JaLineBean line = null;
            TempParseResultBean tpr = null;
            while (str != null) {
                //其他url
                if (str.indexOf(":http://wap.joycool.net/Column.do") == -1) {
                    str = br.readLine();
                    continue;
                }

                m = p.matcher(str);
                //有参数
                if (m.find()) {
                    String s = m.group();
                    columnId = StringUtil.toInt(s.substring(9));
                    //参数不正确
                    if (columnId == -1) {
                        str = br.readLine();
                        continue;
                    }
                    try {
                        String str1 = str
                                .substring(str.indexOf("pv.Log  - ") + 10);
                        String[] ss = str1.split(":");
                        sessionId = ss[ss.length - 2];
                        mobile = ss[0];
                        userId = ss[1];
                    } catch (Exception e) {
                        str = br.readLine();
                        continue;
                    }

                    //开始统计，啦啦啦啦
                    line = getRootLine(columnId);
                    if (line != null) {
                        tpr = (TempParseResultBean) columnPvs.get(""
                                + line.getId());
                        if (tpr == null) {
                            tpr = new TempParseResultBean();
                            columnPvs.put("" + line.getId(), tpr);
                        }

                        //log记录
                        tpr.setTotalPv(tpr.getTotalPv() + 1);
                        count = (Integer) tpr.getSessionPvs().get(sessionId);
                        //原来有
                        if (count == null) {
                            count = new Integer(1);
                            tpr.getSessionPvs().put(sessionId, count);
                        }
                        //原来没有
                        else {
                            count = new Integer(count.intValue() + 1);
                            tpr.getSessionPvs().put(sessionId, count);
                        }

                        count = (Integer) tpr.getUserPvs().get(userId);
                        //原来有
                        if (count == null) {
                            count = new Integer(1);
                            tpr.getUserPvs().put(userId, count);
                        }
                        //原来没有
                        else {
                            count = new Integer(count.intValue() + 1);
                            tpr.getUserPvs().put(userId, count);
                        }

                        count = (Integer) tpr.getMobilePvs().get(mobile);
                        //原来有
                        if (count == null) {
                            count = new Integer(1);
                            tpr.getMobilePvs().put(mobile, count);
                        }
                        //原来没有
                        else {
                            count = new Integer(count.intValue() + 1);
                            tpr.getMobilePvs().put(mobile, count);
                        }
                    }
                }
                str = br.readLine();
            }
            br.close();

            //写入文件
            resultToFile(columnPvs, outputFile);
            //放入缓存
            getResults().put(date, columnPvs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-2-5
     * 
     * 说明：写入文件
     * 
     * 参数及返回值说明：
     * 
     * @param ht
     * @param file
     */
    public static void resultToFile(Hashtable ht, File file) throws Exception {
        JaLineBean line = null;
        Enumeration enu = ht.keys();
        int columnId = -1;
        PrintWriter pw = new PrintWriter(new FileWriter(file));
        TempParseResultBean tpr = null;
        while (enu.hasMoreElements()) {
            columnId = StringUtil.toInt((String) enu.nextElement());
            line = getRootLine(columnId);
            if (line != null) {
                pw.println(line.getName());
                tpr = (TempParseResultBean) ht.get("" + columnId);
                pw.println("总pv：" + tpr.getTotalPv());
                pw.println("总session数：" + tpr.getSessionPvs().size());
                if (tpr.getSessionPvs().size() > 0) {
                    pw.println("session平均pv数："
                            + (tpr.getTotalPv() / tpr.getSessionPvs().size()));
                }
                pw.println("详细记录：");
                Enumeration enu0 = tpr.getSessionPvs().keys();
                String sessionId = null;
                int count = 0;
                while (enu0.hasMoreElements()) {
                    sessionId = (String) enu0.nextElement();
                    count = ((Integer) tpr.getSessionPvs().get(sessionId))
                            .intValue();
                    pw.println(sessionId + "：" + count);
                }
                pw.println();
            }
        }
        pw.flush();
        pw.close();
    }

    public static Hashtable results;

    public static Hashtable getResults() {
        if (results == null) {
            results = new Hashtable();
        }

        return results;
    }

    public static Hashtable getDateResult(String date) {
        if (date == null) {
            return null;
        }
        Hashtable ht = (Hashtable) getResults().get(date);
        if (ht == null) {
            parse(date);
            ht = (Hashtable) getResults().get(date);
        }
        return ht;
    }

    public static JaLineBean getRootLine(int id) {
        IJaLineService lineService = ServiceFactory.createJaLineService();
        JaLineBean line = lineService.getLine(id);
        if (line != null && line.getParentId() != 0) {
            return getRootLine(line.getParentId());
        }
        return line;
    }

    public static JaLineBean getLine(int id) {
        IJaLineService lineService = ServiceFactory.createJaLineService();
        JaLineBean line = lineService.getLine(id);
        return line;
    }

    public static void main(String[] args) {
        Pattern p = Pattern.compile("id=[0-9]*");
        Matcher m = null;
        String str = "15188 [2007-02-05 13:30:02,622] ERROR access.Log  - http://wap.ebinf.com/column.jsp?id=594&aligadga=1170653396012785	sId:9A5B780656F41FD3357AB5A0C80B6EA0";
        m = p.matcher(str);
        if (m.find()) {
            String s = m.group();
            System.out.println(s.substring(3));
        }

        int i = str.indexOf("sId:");
        String sessionId = str.substring(i + 4);
        System.out.println(sessionId);
    }
}
