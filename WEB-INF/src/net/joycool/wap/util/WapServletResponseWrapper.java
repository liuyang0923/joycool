package net.joycool.wap.util;

import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WapServletResponseWrapper extends HttpServletResponseWrapper {

	String prefix = null;
	boolean redirect;
	boolean needEncode;

	CharArrayWriter writer = new CharArrayWriter();

	static Pattern removePattern = Pattern.compile("[\r\n]+[ \t]*");
	static Pattern urlPattern = Pattern.compile("href=\"([^\"]+)\"");

	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(writer);
	}

	public WapServletResponseWrapper(ServletResponse response,
			ServletRequest request) {
		super((HttpServletResponse) response);
		//        jsessionid = ((HttpServletRequest)request).getSession().getId();
		prefix = "http://" + request.getServerName();
		HttpServletRequest hsr = (HttpServletRequest)request;
		needEncode = !hsr.isRequestedSessionIdFromCookie();	// sessionid来自cookie之外都要encode
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponseWrapper#encodeRedirectURL(java.lang.String)
	 */
	public String encodeRedirectURL(String url) {
		if (url.startsWith("http://")) { // absolute url
			if (url.startsWith("http://wap.joycool.net")) {
				url = prefix + url.substring(22);
			} else {
				return url;
			}
		}
		if(needEncode)
			return super.encodeRedirectURL(url);
		else
			return url;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponseWrapper#encodeURL(java.lang.String)
	 */
	public String encodeURL(String url) {
		if (url.startsWith("http://")) { // absolute url
			if (url.startsWith("http://wap.joycool.net")) {
				url = url.substring(22);
				if (url.length() < 2) // wap.joycool.net?
					url = "/wapIndex.jsp";
			} else if (url.startsWith(prefix)) {
				url = url.substring(prefix.length());
				if (url.length() < 2) // wap.joycool.net?
					url = "/wapIndex.jsp";
			} else {
				return url;
			}
		}
		if(needEncode)
			return super.encodeURL(url);
		else
			return url;
	}

	// wap20如果是true，表示是iphone或者ie访问，强制转换为text/html
	public void process(boolean should, boolean wap20) {
		try {
			String type = super.getContentType();
			if (type != null && !redirect) {
				if (type.startsWith("text/vnd")) {		// 1.0页面
					String content = writer.toString();
					if (should) {
						content = removeAll(content);
					}
					if(wap20) {		// 用wap20的都不再处理encodeURL
						super.setContentType("text/html");
						content = toWap20(content);
					} else if(needEncode)
						content = encodeAll(content);
					super.getWriter().write(content);
				} else if (type.startsWith("app")) {		// 2.0页面（但不能被ie打开）
					String content = writer.toString();
					if (should) {
						content = removeAll(content);
					}
					if(wap20) {		// 用wap20的都不再处理encodeURL
						super.setContentType("text/html");	// 仅仅设置即可，本来就是xhtml页面
					} else if(needEncode)
						content = encodeAll(content);
					super.getWriter().write(content);
				} else if (type.startsWith("text")) {
					String content = writer.toString();
					if(content.length() != 0)
						super.getWriter().write(content);
				} else {
					// 注意！！这里没有处理非text情况，估计gif等非text的输出是使用outputstream
					// 所以要输出的jsp文本等必须用getWriter来输出，而不能用outputstream
				}
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	
	public static String toWap20(String content) {
    	if(content.length() < 10)	// 可能是跳转redirect，总之没有xml头不能解析
    		return content;
    	try {	  
    		StringWriter sw = new StringWriter(content.length() + 400);	// 处理之后一般会变多
    		StreamResult b = new StreamResult(sw);
    		InputSource is = new InputSource(new StringReader(content));
    		synchronized(WapServletResponseWrapper.class) {
    			Document doc = builder.parse(is);
				DOMSource a = new DOMSource(doc);
				transformer.transform(a, b);
			}
//    		System.out.println(content.length() + 400 - sw.getBuffer().length());
			return sw.toString();
		} catch (Exception e) {
			System.out.println("towap20 error :");
			if(content.length() <= 500) {
				System.out.println(content);
			} else {
				System.out.println(content.substring(0, 500));
			}
			return content;
		}
	}
	
    public String removeAll(String content) {
    	Matcher m = removePattern.matcher(content);

        StringBuilder sb = new StringBuilder(content.length());
        int pos = 0;
        while (m.find()) {
        	sb.append(content.substring(pos, m.start(0)));
        	pos = m.end(0);
        }
        sb.append(content.substring(pos));
        return sb.toString();

    }
    
    public String encodeAll(String content) {
    	Matcher m = urlPattern.matcher(content);
        StringBuilder sb = new StringBuilder((int) (content.length() * 1.1));
        int pos = 0;
        while (m.find()) {
            sb.append(content.substring(pos, m.start(1)));
            sb.append(encodeURL(m.group(1)));
            pos = m.end(1);
        }
        sb.append(content.substring(pos));
        return sb.toString();
    }

	public void sendRedirect(String url) throws IOException {
		redirect = true;
		super.sendRedirect(encodeRedirectURL(url));
	}
	
    public static Transformer transformer;
    public static Templates transTemplate;
    public static DocumentBuilder builder;
	static byte[] nullDtd = "<?xml version='1.0' encoding='UTF-8'?>".getBytes();
	
	static {
		loadXsl();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);

		try {
			builder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		builder.setEntityResolver(new EntityResolver() {
			public InputSource resolveEntity(java.lang.String publicId,
					java.lang.String systemId) throws SAXException, java.io.IOException {
				if (publicId.equals("-//WAPFORUM//DTD WML 1.1//EN"))
					return new InputSource(new ByteArrayInputStream(nullDtd));
				else
					return null;
			}
		});
	}
	
    public static void loadXsl() {
		try {
			String xslFile = Constants.RESOURCE_ROOT_PATH_OLD + "source.xsl";
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transTemplate = transformerFactory.newTemplates(
					new StreamSource(new FileInputStream(xslFile)));
			
			transformer = transTemplate.newTransformer();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
