package jc.qq;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {
    public static String getContent(String address, String encoding) {
    	HttpURLConnection conn = null;
        try {
            if(encoding == null){
                encoding = "UTF-8";
            }
            URL url = new URL(address);
            
            conn = (HttpURLConnection) url.openConnection();
            
            conn.setConnectTimeout(5*1000); //timeout 5s
            conn.setReadTimeout(15*1000);
            conn.connect();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                sb.append(line + "\n");
                line = br.readLine();
            }
            String s = sb.toString();
            return s;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(conn!=null){
        		conn.disconnect();
        		conn = null;
        	}
        }
        return null;
    }
    
    public static String getPostContent(String address, String post, String encoding) {
    	HttpURLConnection httpconnection = null;
        try {
            if(encoding == null){
                encoding = "UTF-8";
            }

            URL url = new URL(address);
            httpconnection = (HttpURLConnection) url
                    .openConnection();
            
            httpconnection.setConnectTimeout(5*1000); //timeout 5s
            httpconnection.setReadTimeout(5*1000);
     
            httpconnection.setRequestMethod("POST");
            httpconnection.setDoOutput(true);
            httpconnection.connect();
            DataOutputStream dataoutputstream = new DataOutputStream(
                    httpconnection.getOutputStream());
            String a = post;
            dataoutputstream.write(a.getBytes());
            dataoutputstream.flush();
            dataoutputstream.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    httpconnection.getInputStream(), encoding));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                sb.append(line + "\n");
                line = br.readLine();
            }
            String s = sb.toString();
            return s;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(httpconnection!=null){
        		httpconnection.disconnect();
        		httpconnection = null;
        	}
        }
        return null;
    }
    
    public static String getPostContent(HttpServletRequest request, String encoding){
        try {
            if(encoding == null){
                encoding = "UTF-8";
            }
            InputStream in = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
            String str = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (str != null) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(str);
                str = br.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "";
    }
}
