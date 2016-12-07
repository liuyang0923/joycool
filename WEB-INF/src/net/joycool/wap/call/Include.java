package net.joycool.wap.call;

import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;

import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * include jsp(s) or any that dispatcher can find
 * 
 * @author bomb
 *  
 */
public class Include {

	static IJaLineService lineService = ServiceFactory.createJaLineService();
	
    /**@Deprecated
     * 包含单独一个
     * 
     * @param callParam<br>
     * 1、包含地址
     */
    public static String single(CallParam callParam) {
    	RequestDispatcher rd = callParam.request.getRequestDispatcher(callParam.getParam());
    	if(rd == null)
    		return "******";
    	
    	try {
			rd.include(callParam.request, callParam.response);
		} catch (Exception e) {
			e.printStackTrace();
			return "******";
		}
        return "";
    }
    
    /**@Deprecated
     * 包含随机一个
     * 
     * @param callParam<br>
     * 1、多个包含地址
     */
    public static String random(CallParam callParam) {
    	String urls = callParam.getParam();
    	if(urls == null)
    		return "******";
    	String[] urlss = urls.split(";");
    	if(urlss.length == 0)
    		return "******";
    	String url = urlss[RandomUtil.nextInt(urlss.length)];
    	RequestDispatcher rd = callParam.request.getRequestDispatcher(url);
    	if(rd == null)
    		return "******";
    	
    	try {
			rd.include(callParam.request, callParam.response);
		} catch (Exception e) {
			e.printStackTrace();
			return "******";
		}
        return "";
    }
    
    /**
     * 包含单独一个父栏目
     * 
     * @param callParam<br>
     * 1、包含的父栏目id
     */
    
    public static String column(CallParam callParam) {

		int id = StringUtil.toId(callParam.getParam());
		if(id <= 0)
			throw new IllegalArgumentException();	
		
		return getLinesWml(id, callParam.request, callParam.response);
    }
    
    /**
     * 包含随机一个父栏目
     * 
     * @param callParam<br>
     * 1、多个包含的父栏目id
     */
    public static String randomColumn(CallParam callParam) {
    	
    	String urls = callParam.getParam();
    	if(urls == null)
    		throw new IllegalArgumentException();
    	String[] urlss = urls.split(";");
    	if(urlss.length == 0)
    		throw new IllegalArgumentException();
    	String url = urlss[RandomUtil.nextInt(urlss.length)];
    	int id = StringUtil.toId(url);
		if(id <= 0)
			throw new IllegalArgumentException();

		return getLinesWml(id, callParam.request, callParam.response);
    }
    
    /**
     * 包含随机一个父栏目下的父栏目
     * 
     * @param callParam<br>
     * 1、顶层父栏目id
     */
    public static String randomSubColumn(CallParam callParam) {
    	
    	int id = StringUtil.toId(callParam.getParam());
    	Vector lineList = getLineList(id);
		if(lineList.size() == 0)
			throw new IllegalArgumentException();
		
		JaLineBean line = (JaLineBean)lineList.get(RandomUtil.nextInt(lineList.size()));

		return getLineWml(line, callParam.request, callParam.response);
    }

    // 得到一整个父栏目下的内容
    public static String getLinesWml(int columnId, HttpServletRequest request, HttpServletResponse response) {
    	if(checkIterTwice(columnId, request))	// 如果第二次被显示，返回空
			return "";
    	StringBuilder sb = new StringBuilder(64);

    	Vector lineList = getLineList(columnId);

        for (int i = 0;i < lineList.size();i++) {
        	JaLineBean line = (JaLineBean) lineList.get(i);
            sb.append(StringUtil.getLineWml(line, request, response));
        }
        return sb.toString();
    }
    
    // 得到一整个父栏目下的内容，如果不是父栏目则得到本行内容
    public static String getLineWml(int columnId, HttpServletRequest request, HttpServletResponse response) {
    	JaLineBean line = lineService.getLine("id = " + columnId);
    	return getLineWml(line, request, response);
    }
    
    public static String getLineWml(JaLineBean line, HttpServletRequest request, HttpServletResponse response) {
    	if(line.getLinkType() != JaLineBean.LT_COLUMN) {
    		if(checkIterTwice(line.getId(), request))	// 如果第二次被显示，返回空
    			return "";
    		return StringUtil.getLineWml(line, request, response);
    	}

        return getLinesWml(line.getId(), request, response);
    }
    
    // 检查某个column或者父栏目，是否在同一个request中显示了多次，如果多次返回true
    private static String tcheck = "";
    private static boolean checkIterTwice(int id, HttpServletRequest request) {
    	String key = id + "tcheck";
    	if(request.getAttribute(key) != null)
    		return true;
		request.setAttribute(key, tcheck);
		return false;
	}

	public static Vector getLineList(int columnId) {
    	return lineService.getLineList("parent_id=" + columnId + " and if_display = 1 ORDER BY line_index");
    }
}