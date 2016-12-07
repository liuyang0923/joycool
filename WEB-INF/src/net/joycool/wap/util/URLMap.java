/**
 * 
 */
package net.joycool.wap.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.UrlMapBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;

/**
 * @author zhul_2006-07-28
 * 
 */
public class URLMap {

	private static ICatalogService catalogService = ServiceFactory
			.createCatalogService();

	private static HashMap URLCatalogMap = null;

	public static void init() {
		URLCatalogMap = new HashMap();
		Vector urlMapList = catalogService.getUrlMapList(null);
		Iterator it = urlMapList.iterator();
		while (it.hasNext()) {
			UrlMapBean url = (UrlMapBean) it.next();
			URLCatalogMap.put(new Integer(url.getCatalogId()), url);
		}
	}

	public static HashMap getURLCatalogMap() {
		if(URLCatalogMap == null)
			init();
		return URLCatalogMap;
	}

	public static void clearURLCatalogMap() {
		URLCatalogMap = null;
		init();
	}

	// liuyi 2006-12-01 程序优化 end

	/**
	 * 
	 * @param prefix
	 *            返回的上一级的前缀
	 * @param id
	 *            当前页的id
	 * @return 返回上一级的url,如果没有得到相应结果返回null
	 */
	public static String getBacktoURL(String prefix, int id) {

		if (URLCatalogMap == null) {
			init();
		}

		UrlMapBean urlMap = (UrlMapBean) URLCatalogMap.get(new Integer(id));
		if (urlMap == null) {
			if (prefix.equals("") || prefix == null) {
				return BaseAction.INDEX_URL;
			}
			CatalogBean catalog = catalogService.getCatalog(" id=" + id);
			if (catalog == null) {
				return BaseAction.INDEX_URL;
			} else {
				return prefix + catalog.getParentId();
			}
		} else {
			return urlMap.getReturnUrl();
		}
	}

}
