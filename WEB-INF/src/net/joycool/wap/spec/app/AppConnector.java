package net.joycool.wap.spec.app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import net.joycool.wap.util.SqlUtil;

public class AppConnector {

	static String viaKey = "via";
	static String viaValue = "WTP/1.1 BJBJ-PS-WAP4-GW06.bj4.monternet.com (Nokia WAP Gateway 4.0/CD3/4.1.79)";
	static String forwardedKey = "x-forwarded-for";
	static String forwardedValue = "10.18.86.242";
	static String agentKey = "User-Agent";
	static String agentValue = "NokiaN73-1/3.0638.0.0.44_rm132 Series60/3.0 Profile/MIDP-2.0 ";
	static String proxyKey = "jcproxy";

//	public static HashMap cookies = new HashMap();

	public static HttpResponse toRequest(String requestURL, String method,
			byte[] param, Map submitHeader) {
		HttpResponse response = new HttpResponse();
		try {
//			if (method.equals("GET") && param != null)
//				requestURL += "?" + param;
			URL url = new URL(requestURL);
			String host = url.getHost();
//			String cookie = null;
//			cookie = (String) cookies.get(host); // 获取这个域名下的cookie
			URLConnection urlc = url.openConnection();//连接URL
			HttpURLConnection httpConnection = (HttpURLConnection) urlc;
//			if (cookie != null)
//				httpConnection.addRequestProperty("Cookie", cookie);

			httpConnection.setConnectTimeout(2000);
			httpConnection.setReadTimeout(10000);
			httpConnection.setInstanceFollowRedirects(false);
			
			httpConnection.setRequestMethod(method);//设置连接的各项属性
//			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setDoInput(true);
			
//			httpConnection.setRequestProperty(viaKey, viaValue);
//			httpConnection.setRequestProperty(forwardedKey, forwardedValue);
//			httpConnection.setRequestProperty(agentKey, agentValue);
			// httpConnection.setRequestProperty("x-source-id","BJGGSN54BNK");
			// httpConnection.setRequestProperty("x-nokia-connection_mode","TCP");
			// httpConnection.setRequestProperty("x-up-bearer-type","GPRS");
			// httpConnection.setRequestProperty("x-nokia-gateway-id","NWG/4.1/Build04");
			Iterator iter = submitHeader.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry e = (Map.Entry)iter.next();
				httpConnection.setRequestProperty((String)e.getKey(), (String)e.getValue());
			}

			if (method.equals("POST")) {//写入post的传递的参数
				httpConnection.setDoOutput(true);
				OutputStream out = httpConnection.getOutputStream();
				if(param != null) {
					out.write(param);
				}
				out.close();
			}
//			String setc = httpConnection.getHeaderField("Set-Cookie");
//			if (setc != null) {
//				synchronized (cookies) {
//					cookies.put(host, setc);
//				}
//			}
			
//			InputStream in = httpConnection.getInputStream();
			
			InputStream in = null;
			if(SqlUtil.isTest) {
				try {
					in = httpConnection.getInputStream();
				} catch(IOException ex1) {	// 调试模式下，如果出错，返回错误页面信息
					in = httpConnection.getErrorStream();
					if(in == null) {
						StringBuilder sb = new StringBuilder(64);
						sb.append("app connect failed (");
						sb.append(ex1.getMessage());
						sb.append(") : ");
						sb.append(requestURL);
						Iterator iter2 = submitHeader.values().iterator();
						while(iter2.hasNext()) {
							sb.append(',');
							sb.append(iter2.next());
						}
						System.out.println(sb.toString());
						return null;
					}
				}
			} else {
				in = httpConnection.getInputStream();
			}
			int totalLength = httpConnection.getContentLength();
			byte[] content;
			if(totalLength > 0) {
				int bytesRead;
				content = new byte[totalLength];
				for (int i = 0; i < totalLength; i += bytesRead) {
					bytesRead = in.read(content, i, totalLength - i);
					if (bytesRead < 1)
						break;
				}
			} else {
				// 会出现 contentLength<=0 的情况么？
				ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
				int i = in.read();//读取页面信息,并放入baos内
				while (i != -1) {
					baos.write(i);
					i = in.read();
				}
				baos.close();
				content = baos.toByteArray();
			}
			in.close();

			response.setContent(content);//设置response(一个页面bean)的值
			response.setResponseCode(httpConnection.getResponseCode());
			response.setResponseMessage(httpConnection.getResponseMessage());
			response.setHeaders(httpConnection.getHeaderFields());
			response.setURL(httpConnection.getURL());

			httpConnection.disconnect();

		} catch (IOException ex1) { // 连接失败
			//ex1.printStackTrace();
			StringBuilder sb = new StringBuilder(64);
			sb.append("app connect failed (");
			sb.append(ex1.getMessage());
			sb.append(") : ");
			sb.append(requestURL);
			Iterator iter = submitHeader.values().iterator();
			while(iter.hasNext()) {
				sb.append(',');
				sb.append(iter.next());
			}
			System.out.println(sb.toString());
			return null;
		}
		return response;
	}

	public static HttpResponse usePost(String url, String param) {
		return null;
	}

}
