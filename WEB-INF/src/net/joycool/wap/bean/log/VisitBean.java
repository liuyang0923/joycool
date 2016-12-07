/*
 * Created on 2006-1-20
 *
 */
package net.joycool.wap.bean.log;

/**
 * @author lbj
 *
 */
public class VisitBean {
    String url;
    int count;
    
    
    /**
     * @return Returns the count.
     */
    public int getCount() {
        return count;
    }
    /**
     * @param count The count to set.
     */
    public void setCount(int count) {
        this.count = count;
    }
    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
