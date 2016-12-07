/*
 * Created on 2005-11-24
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.news.NewsBean;

/**
 * @author lbj
 *
 */
public interface INewsService {
    public NewsBean getNews(String condition);
    public Vector getNewsList(String condition);
    public boolean updateNews(String set, String condition);
    public int getNewsCount(String condition);
}
