/*
 * Created on 2005-11-24
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.UrlMapBean;
import net.joycool.wap.bean.UrlSortBean;

/**
 * @author lbj
 *
 */
public interface ICatalogService {
    public CatalogBean getCatalog(String condition);    
    public Vector getCatalogList(String condition); 
    //han_yan_2006-10-24该方法用于电子书的项目,读取广告列表的功能_start
    public Vector getAdvertiseList(String condition);
    //han_yan_2006-10-24该方法用于电子书的项目,读取广告列表的功能_end
    
    //mcq_2007-7-28_增加页面返回上一级的方法_start
    public UrlMapBean getUrlMap(String condition);
    public Vector getUrlMapList(String condition);
    public boolean addUrlMap(UrlMapBean bean);
    public boolean delUrlMap(String condition);
    public boolean updateUrlMap(String set, String condition);
    public int getUrlMapCount(String condition);
    
    public UrlSortBean getUrlSort(String condition);
    //mcq_2007-7-28_增加页面返回上一级的方法_end
}
