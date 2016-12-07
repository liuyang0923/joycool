/*
 * Created on 2005-12-22
 *
 */
package net.joycool.wap.mont;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.framework.JoycoolSessionListener;

/**
 * @author lbj
 *  
 */
public class OrderUtil {
    public static String getViewUrl(HttpServletRequest request) {
        String unique = request.getParameter("unique");
        if (unique == null) {
            return "http://wap.joycool.net";
        }

        Hashtable urlHash = JoycoolSessionListener.getUrlMap(request
                .getSession().getId());
        String url = null;
        if (urlHash != null) {
            url = (String) urlHash.get(unique);
        }
        if (url != null) {
            return url;
        } else {
            return "http://wap.joycool.net";
        }
    }
}
