package net.joycool.wap.call;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.item.ShowBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.spec.shop.ItemBean;
import net.joycool.wap.spec.shop.ShopAction;
import net.joycool.wap.spec.shop.ShopService;
import net.joycool.wap.spec.shop.ShopUtil;
import net.joycool.wap.spec.shop.UserInfoBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 页面相关，顶部底部等等
 * 
 * @author bomb
 *  
 */
public class Shop {
	
	//public static List sugguestList;
	
	//public static List topList;
	
	public static List getSugguestList(int limit){
		ShopService shopService = ShopService.getInstance();
		
//		if(sugguestList == null) {
//			synchronized(Shop.class) {
//				if(sugguestList == null) {
//					sugguestList = shopService.getSugguest(limit);
//				}
//			}
//		}
		
		return shopService.getSugguest(limit);
	}
	
	public static List getTopSugguestList(int limit){
		ShopService shopService = ShopService.getInstance();
//		if(topList == null) {
//			synchronized(Shop.class) {
//				if(topList == null) {
//					topList = shopService.getTop(limit);
//				}
//			}
//		}
		return shopService.getTop(limit);
	}
	
    public static String gold(CallParam callParam) {
    	HttpSession session = callParam.request.getSession(false);
    	StringBuilder sb = new StringBuilder(256);
    	if(session != null) {
    		UserBean user = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
    		if(user != null) {
	    		UserInfoBean bean = ShopAction.shopService.getUserInfo(user.getId());
	    		if(bean == null) {
	    			bean = ShopAction.shopService.addUserInfo(user.getId());
	    		}
	    		sb.append("<a href=\"info.jsp\">个人信息</a>.<a href=\"favorite.jsp\">收藏夹</a><br/>您有酷币");
	    		sb.append(ShopUtil.GOLD_NAME);
	    		sb.append(bean.getGoldString());
	    		sb.append("【<a href=\"/pay/pay.jsp\">充值</a>.<a href=\"/pay/myOrder.jsp\">查询</a>】<br/>");
    		}
    	}
    	return sb.toString();
    }

    
    //广告
    public static String ads(CallParam callParam){
    	//HttpSession session = callParam.request.getSession(false);
    	StringBuilder sb = new StringBuilder(100);
    	List ads = ShopUtil.getAds();
    	if(ads.size() > 0) {
    		int j = (int)(Math.random() * ads.size());
    		j = (j >= ads.size()?j-1:j);
    		String[] strs = (String[])ads.get(j);
    		sb.append("*<a href=\"");
    		sb.append(strs[1]);
    		sb.append("\">");
    		sb.append(strs[2]);
    		sb.append("</a><br/>");
    	}
    	return sb.toString();
    }
    
    //特别推荐
    public static String special(CallParam callParam) {
    	StringBuilder sb = new StringBuilder(256);
    	String idStr = callParam.getParam();
    	
    	int id = StringUtil.toInt(idStr);
    	
    	ItemBean bean = ShopAction.shopService.getShopItemById(id);
    	sb.append("<img src=\"/rep/shop/");
    	sb.append(bean.getPhotoUrl());
    	sb.append("\" alt=\"");
    	sb.append(bean.getName());
    	sb.append("\"/><br/><a href=\"item.jsp?id=");
    	sb.append(id);
    	sb.append("\">");
    	sb.append(bean.getName());
    	sb.append("</a>");
    	sb.append(ShopUtil.GOLD_NAME);
    	sb.append(bean.getPriceString());
    	sb.append("<br/>");
    	sb.append(bean.getDesc());
    	sb.append("<br/>");
    	return sb.toString();
    }
    
    //导航
    public static String nav(CallParam callParam) {
    	StringBuilder sb = new StringBuilder(100);
    	String[] types = ShopUtil.getTypes();
    	int count = types.length;
    	for(int i = 1; i < count; i++) {
    		if(types[i] != null && types[i].length() > 0) {
    			if(ShopAction.shopService.getItemListByType(i).size() > 0){
    				sb.append(i==1?"":".");
    				sb.append("<a href=\"items.jsp?id=");
    				sb.append(i);
    				sb.append("\">");
    				sb.append(types[i]);
    				sb.append("</a>");
    			}
    		}
    	}
    	return sb.toString();
    }
    
    
    
    public static String sugguest(CallParam callParam) {
    	String limitStr = callParam.getParam();
    	int limit = StringUtil.toInt(limitStr);
    	StringBuilder sb = new StringBuilder(64 * 6);
    	List sugguestList = getSugguestList(limit);
    	for(int i = 0; i < sugguestList.size(); i++) {
    		Integer ii = (Integer)sugguestList.get(i);
    		ItemBean itemBean = ShopAction.shopService.getShopItemById(ii.intValue());
    		//DummyProductBean productBean = UserBagCacheUtil.getItem(itemBean.getItemId());
    		//<a href="item.jsp?id=<%=ii.intValue()%>&amp;t=<%=itemBean.getType()%>">
    		sb.append("<a href=\"item.jsp?id=");
    		sb.append(ii.intValue());
    		sb.append("&amp;t=");
    		sb.append(itemBean.getType());
    		sb.append("\">");
    		sb.append(itemBean.getName());
    		sb.append("</a>");
    		if(i < 3) {
    			sb.append("<img src=\"img/new.gif\" alt=\"推荐\"/>");
    		}
    		//<%if(i < 3){if(type == 0) {%><img src="img/new.gif" alt="推荐"/>
    		sb.append("<br/>");
    		
    	}
    	return sb.toString();
    }
    
    
    public static String top(CallParam callParam) {
    	String limitStr = callParam.getParam();
    	int limit = StringUtil.toInt(limitStr);
    	StringBuilder sb = new StringBuilder(64 * 3);
    	List topList = getTopSugguestList(limit);
    	for(int i = 0; i < topList.size(); i++) {
    		Integer ii = (Integer)topList.get(i);
    		ItemBean itemBean = ShopAction.shopService.getShopItemById(ii.intValue());
    		//DummyProductBean productBean = UserBagCacheUtil.getItem(itemBean.getItemId());
    		
    		sb.append("<a href=\"item.jsp?id=");
    		sb.append(ii.intValue());
    		sb.append("&amp;t=");
    		sb.append(itemBean.getType());
    		sb.append("\">");
    		sb.append(itemBean.getName());
    		sb.append("</a>");
    		if(i < 3) {
    			sb.append("<img src=\"img/top.gif\" alt=\"排行\"/>");
    		}
    		sb.append("<br/>");
    	}
    	return sb.toString();
    }
    
    
    static ShopService shopService = ShopService.getInstance();
	
	public static List getShopRecordList(int type, int limit){
		String key = "shopR" + type;
		List list = (List) OsCacheUtil.get(key, "latest", 300);
		if (list == null) {
			synchronized (Shop.class) {
				list = (List) OsCacheUtil.get(key, "latest", 300);
				if (list == null) {
					if(type == -1)
						list = SqlUtil.getIntsList("select a.uid,a.item_id from shop_gold_record a,shop_item b where a.item_id=b.id and a.type=0 order by a.id desc limit " + limit, 5);
					else
						list = SqlUtil.getIntsList("select a.uid,a.item_id from shop_gold_record a,shop_item b where a.item_id=b.id and a.type=0 and b.type=" + type + " order by a.id desc limit " + limit, 5);
					OsCacheUtil.put(key, list, "latest");
				}
			}
		}
		return list;
	}
    
    public static String lastest(CallParam callParam) {
    	String[] params = callParam.getParams();
    	int type = 1;
    	int limit = 5;
    	if(params.length > 0) {
    		type = StringUtil.toInt(params[0]);
        	if(params.length > 1) {
        		limit = StringUtil.toId(params[1]);
        	}
    	}
    	
    	List list = getShopRecordList(type, limit);
    	limit = list.size();
    	if(limit == 0)
    		return "";
    	int index = RandomUtil.nextInt(limit);
    	int[] ii = (int[])list.get(index);
    	UserBean userBean = UserInfoUtil.getUser(ii[0]);
    	StringBuilder sb = new StringBuilder(48);
    	sb.append("<a href=\"/shop/index.jsp\">");
    	sb.append(userBean.getNickNameWml());
    	sb.append("购买了");
    	HashMap map = UserBean.uService.getItemShowMap("1");
    	ItemBean itemBean = ShopAction.shopService.getShopItemById(ii[1]);
    	if(itemBean.getType() == 1) {
    		ShowBean bean = (ShowBean)map.get(new Integer(itemBean.getItemId()));
    		if(bean == null)
    			sb.append(itemBean.getName());
    		else {
    			sb.append("<img src=\"/rep/lx/e").append(bean.getItemId()).append(".gif\" alt=\"o");
    			sb.append(bean.getName());
    			sb.append("\"/>");
    		}
    	} else {
    		sb.append(itemBean.getName());
    	}
    	sb.append("</a>");
    	//
    	
    	return sb.toString();
    }
    
}