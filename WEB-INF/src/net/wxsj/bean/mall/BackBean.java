/*
 * Created on 2007-7-31
 *
 */
package net.wxsj.bean.mall;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-31
 * 
 * 说明：
 */
public class BackBean {
    String title;

    String url;

    public BackBean() {

    }

    public BackBean(String title, String url) {
        this.title = title;
        this.url = url;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
