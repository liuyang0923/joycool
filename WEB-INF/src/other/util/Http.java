/*
 * Created on 2005-11-9
 *
 */
package other.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author lbj
 *  
 */
public class Http {
    public static void main(String[] args) throws Exception {
////    	doWork2();doWork2();doWork2();
////    	System.out.println("------end--------");
//        String requestURL = "http://localhost/";
//        String content = "phoneno=13661113233&placeno=1&spnum=8808501&phonetype=1&packageno=-99&msg_content=xOPV4rTOx8C1vbXEusXC68rHMaGjusXC68uz0PK13dT2o6y%2B4MDrz8K49rvxvbG147u509A5OTm49rrFwuujobzT083FtqOhv%2By72Li0QS9CL0O8zND4zbbGsbDJo6E%3D&linkid=1021";
//        Http.doRequest(requestURL, "POST", content, "UTF-8");
//        HttpResponse response = Http.doRequest(requestURL, "POST", content,"UTF-8");
//        if (response == null) {
//            System.out.println("error.");
//        } else {
//            System.out.println(response.getResponseCode());
//            System.out.println(response.getResponseMessage());
//            Set set = response.getHeaders().keySet();
//            Iterator itr = set.iterator();
//            while (itr.hasNext()) {
//                String s = (String) itr.next();
//                System.out.println(s + ": " + response.getHeaders().get(s));
//            }
//            System.out.println(new String(response.getContent()));
//        }
    }

    /**
     * @param requestURL
     *            //请求的URL
     * @param requestMethod
     *            //请求的方法POST/GET
     * @param postContent
     *            //如果是post，post的内容
     * @return
     */
    
    static String viaKey = "via";
    static String viaValue = "WTP/1.1 BJBJ-PS-WAP4-GW06.bj4.monternet.com (Nokia WAP Gateway 4.0/CD3/4.1.79)";
    static String forwardedKey = "x-forwarded-for";
    static String forwardedValue = "10.18.86.242";
    static String agentKey = "User-Agent";
    static String agentValue = "NokiaN73-1/3.0638.0.0.44_rm132 Series60/3.0 Profile/MIDP-2.0 ";
    static String proxyKey = "jcproxy";
    public static HttpResponse doRequest(String requestURL,
            String requestMethod, String content, String charset) {
        HttpResponse response = null;

//		System.out.println(" ----request---- url : " + requestURL + "?" + content);
        
        try {
            if (requestMethod.equalsIgnoreCase("GET") && content != null) {
                requestURL += "?" + content;
            }

            URL url = new URL(requestURL);

            response = new HttpResponse();

            try {

                URLConnection urlc = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) urlc;
                httpConnection.setConnectTimeout(10000);
                httpConnection.setReadTimeout(30000);
                httpConnection.setRequestMethod(requestMethod.toUpperCase());
                httpConnection.setInstanceFollowRedirects(false);
                httpConnection.setDoInput(true);
//                httpConnection.setRequestProperty(viaKey, viaValue);
//                httpConnection.setRequestProperty(forwardedKey, forwardedValue);
//                httpConnection.setRequestProperty("x-source-id", "BJGGSN54BNK");
//                httpConnection.setRequestProperty("x-nokia-connection_mode", "TCP");
//                httpConnection.setRequestProperty("x-up-bearer-type", "GPRS");
//                httpConnection.setRequestProperty("x-nokia-gateway-id", "NWG/4.1/Build04");
//                httpConnection.setRequestProperty(agentKey, agentValue);
                
                if (requestMethod.equalsIgnoreCase("POST")) {
                    if (content == null) {
                        return null;
                    }
                    if (charset == null) {
                        charset = "UTF-8";
                    }
                    httpConnection.setDoOutput(true);
                }
//                System.out.println("----------start mtlog 5-----------");
//				httpConnection.connect();
    			OutputStream out = null;
                if (requestMethod.equalsIgnoreCase("POST")) {
                    out = httpConnection.getOutputStream();
                    out.write(content.getBytes(charset));
                    out.flush();
                }

                InputStream in = httpConnection.getInputStream();
                
                response.setResponseCode(httpConnection.getResponseCode());
                response
                        .setResponseMessage(httpConnection.getResponseMessage());
                response.setHeaders(httpConnection.getHeaderFields());
                response.setURL(httpConnection.getURL());

//    			System.out.println("----------start mtlog 6-----------");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int i = in.read();
                while (i != -1) {
                    baos.write(i);
                    i = in.read();
                }
                in.close();
                baos.close();
                response.setContent(baos.toByteArray());
                if(out != null)
                	out.close();
                httpConnection.disconnect();
                try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

            } catch (IOException ex1) {	// 连接失败
                ex1.printStackTrace();	
                return null;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }

        return response;
    }
    
    
}
