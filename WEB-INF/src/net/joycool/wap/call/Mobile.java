package net.joycool.wap.call;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.util.Encoder;
import net.joycool.wap.util.SecurityUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 取号接口等
 * 
 * @author zhoujun
 *  
 */
public class Mobile {
	public static String URL_PREFIX = "http://221.179.205.25/caifu1/index.jsp?fr=";
	
    public static String getUA(CallParam callParam) {
        HttpServletRequest request = callParam.request;
        HttpServletResponse response = callParam.response;
        HttpSession session = request.getSession();
        int linkId = StringUtil.toId((String) session.getAttribute("linkId"));
        if(linkId == 0)
        	return "";
        int hasFetchCount = 0;
        //已经取过
        if (session.getAttribute("hasFetchMobile") != null) {	// 每个session只取2次
            hasFetchCount = ((Integer) session
                    .getAttribute("hasFetchMobile")).intValue();
            if (hasFetchCount < 0) {
                hasFetchCount = 0;
            }
            if (hasFetchCount >= 1) {
                return "";
            }
        }
        
        // 已经取到号，不取
        String mobile = (String)session.getAttribute("userMobile");
        if (mobile != null) {
            return "";
        }

        if (!SecurityUtil.isMobile(request)) { // 如果不是手机，没有必要取
            return "";
        }

//        //先判断还能不能取
//        if (!MobileInfc.canFetch()) {
//            return "";
//        }

        int type = StringUtil.toId(callParam.getParam());

        StringBuffer url = new StringBuffer(32);
        url.append("http://");
        url.append(request.getServerName() + ":" + request.getServerPort());
        url.append(request.getContextPath());
        url.append(response.encodeURL("/fpic3.jsp?type="));
        url.append(type);
        url.append("&sId=" + request.getSession().getId());
        String uaurl = null;

        String back = url.toString();

		try {
			uaurl = URL_PREFIX + URLEncoder.encode(Encoder.encrypt(back), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}

        uaurl = uaurl.replace("&", "&amp;");

        hasFetchCount++;
        session.setAttribute("hasFetchMobile", Integer.valueOf(hasFetchCount));
              return "<img src=\"" + uaurl + "\" alt=\"\"/>";
              //        return "<a href=\"" + uaurl + "\">testtesttest</a>";
    }

	public static String encrypt(String pwd) {
		String code = null;
		try {
			byte[] enc = Encoder.desEncrypt(pwd.getBytes());
			code = Encoder.byteArr2HexStr(enc);

		} catch (Exception e) {

		}
		return code;
	}
}