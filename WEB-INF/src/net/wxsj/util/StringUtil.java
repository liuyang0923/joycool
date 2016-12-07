/*
 * Created on 2006-10-26
 *
 */

package net.wxsj.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：张毅
 * 
 * 创建日期：2007-1-25
 * 
 * 说明：
 */
public class StringUtil {
    public static String convertNull(String s) {
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }

    public static String cutString(String s, int count) {
        if (s == null) {
            return s;
        }
        if (s.length() < count) {
            return s;
        }
        s = s.substring(0, count);
        return s;
    }

    public static String trim(String s) {
        if (s == null) {
            return s;
        }
        s = s.replace("\r\n", "");
        s = s.replace("\n", "");
        s = s.replace(" ", "");
        return s;
    }

    public static String getURLContent(String url) {
        try {
            URL u = new URL(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(u
                    .openStream()));
            String str = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (str != null) {
                sb.append(str);
                str = br.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static double toDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean isLetterOrNumber(String s) {
        if (s == null) {
            return false;
        } else {
            for (int i = 0; i < s.length(); i++) {
                int a = s.charAt(i);
                if ((a >= 48 && a <= 57) || (a >= 65 && a <= 90)
                        || (a >= 97 && a <= 122)) {
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isMobile(String s) {
        if (s == null) {
            return false;
        }

        if (!s.startsWith("13") && !s.startsWith("15")) {
            return false;
        }

        if (s.length() != 11) {
            return false;
        }

        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static String toWml(String src) {
        if (src == null)
            return "";

        src = src.replace("&", "&amp;");
        src = src.replace("$", "$$");
        src = src.replace("<", "&lt;");
        src = src.replace(">", "&gt;");
        src = src.replace("\r\n", "<br/>");
        src = src.replace("\n", "<br/>");
        src = src.replace("\"", "“");

        return src;
    }

    public static String dealLink(String link, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            if (link == null) {
                return link;
            }
            if (!link.startsWith("http://")) {
                String domain = request.getServerName();
                if (link.startsWith("/")) {
                    link = "http://" + domain + link;
                }
            }

            String sessionId = request.getSession().getId();
            String newLink = null;
            if (link.indexOf("?") == -1) {
                if (link.substring(link.indexOf("//") + 2).indexOf("/") == -1) {
                    newLink = link + "/;jsessionid=" + sessionId;
                } else {
                    newLink = link + ";jsessionid=" + sessionId;
                }
            } else {
                newLink = link.substring(0, link.indexOf("?")) + ";jsessionid="
                        + sessionId + link.substring(link.indexOf("?"));
            }
            if (newLink.indexOf("aligadga") == -1) {
                if (newLink.indexOf("?") != -1) {
                    newLink += "&aligadga=" + getUnique();
                } else {
                    newLink += "?aligadga=" + getUnique();
                }
            }
            newLink = newLink.replace("&", "&amp;");
            return newLink;
        } catch (Exception e) {
            return "";
        }
        //return (link);
    }

    public static String dealRedirectLink(String link,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            if (link == null) {
                return link;
            }
            if (!link.startsWith("http://")) {
                String domain = request.getServerName();
                if (link.startsWith("/")) {
                    link = "http://" + domain + link;
                }
            }

            String sessionId = request.getSession().getId();
            String newLink = null;
            if (link.indexOf("?") == -1) {
                if (link.substring(link.indexOf("//") + 2).indexOf("/") == -1) {
                    newLink = link + "/;jsessionid=" + sessionId;
                } else {
                    newLink = link + ";jsessionid=" + sessionId;
                }
            } else {
                newLink = link.substring(0, link.indexOf("?")) + ";jsessionid="
                        + sessionId + link.substring(link.indexOf("?"));
            }

            if (newLink.indexOf("aligadga") == -1) {
                if (newLink.indexOf("?") != -1) {
                    newLink += "&aligadga=" + getUnique();
                } else {
                    newLink += "?aligadga=" + getUnique();
                }
            }

            return newLink;
        } catch (Exception e) {
            return "";
        }
        //return (link);
    }

    public static String getUnique() {
        Random rand = new Random();
        return System.currentTimeMillis() + "" + rand.nextInt(10) + ""
                + rand.nextInt(10) + "" + rand.nextInt(10);
    }

    public static boolean isNull(String s) {
        if (s == null) {
            return true;
        }
        if ("".equals(s.replace(" ", ""))) {
            return true;
        }
        return false;
    }

    public static boolean isChinese(char c) {
        if (c >= '\u4E00' && c <= '\u9FA5') {
            return true;
        }
        return false;
    }

    public static boolean isLetter(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return true;
        }
        return false;
    }

    public static boolean isNumber(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    public static boolean isChineseOrLetter(String s) {
        if (s == null) {
            return false;
        }

        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (!isChinese(cs[i]) && !isLetter(cs[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-9-25
     * 
     * 说明：统一处理一下用户提交上来的字符串参数，对一些可能影响数据库操作的特殊字符进行处理。
     * 
     * 参数及返回值说明：
     * 
     * @param param
     * @return
     */
    public static String dealParam(String param) {
        if (param == null) {
            return param;
        }

        param = param.replace("'", "\"");
        return param;
    }
    
    public static String dealUri(String uri){
        if(uri == null || "".equals(uri)){
            return "/";
        }
        
        if(uri.indexOf("//") != -1){
            uri = uri.replaceAll("//[/]*", "/");
        }
        
        if(uri.lastIndexOf("/") != -1){
            return uri.substring(0, uri.lastIndexOf("/") + 1);
        }
        
        return null;
    }
    
    public static String dealPhoneNumber(String phoneNumber){
        if(phoneNumber == null){
            return phoneNumber;
        }
        Pattern p = Pattern.compile("([^0-9])*");        
        Matcher m = p.matcher(phoneNumber);
        return m.replaceAll("");
    }
    
    public static void main(String[] args){             
    }
    
    public static String formatDouble(double d){
        DecimalFormat df = new DecimalFormat("##########0.##");
        return df.format(d);
    }
    
    public static boolean checkTelephone(String s){
        if(s == null){
            return false;
        }
        
        if(s.length() != 11){
            return false;
        }
        
        if(s.startsWith("13") || s.startsWith("15") || s.startsWith("0")){
            return true;
        }
        
        return false;
    }
}

