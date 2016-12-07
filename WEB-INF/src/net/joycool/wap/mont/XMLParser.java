package net.joycool.wap.mont;

import java.io.IOException;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * xml交易处理类
 * 
 * @author antie
 */
public class XMLParser {

    protected SAXBuilder sb = new SAXBuilder();

    private Document reqDoc;

    private Element reqRoot;

    /** 根据文件名生成请求xml */
    public XMLParser(String reqXmlFile) {
        System.out.println("init trans...");
        try {
            InputStream inputstream = getClass()
                    .getResourceAsStream(reqXmlFile);
            reqDoc = sb.build(inputstream);
            reqRoot = reqDoc.getRootElement();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public XMLParser(InputStream inputStream) {
        System.out.println("init trans...");
        try {
            reqDoc = sb.build(inputStream);
            reqRoot = reqDoc.getRootElement();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    public Document getReqDoc() {
        return reqDoc;
    }

    /**
     * @param document
     */
    public void setReqDoc(Document document) {
        reqDoc = document;
    }
    
    
    /**
     * @return Returns the reqRoot.
     */
    public Element getReqRoot() {
        return reqRoot;
    }
    /**
     * @param reqRoot The reqRoot to set.
     */
    public void setReqRoot(Element reqRoot) {
        this.reqRoot = reqRoot;
    }
}
