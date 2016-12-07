package net.joycool.wap.call;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.framework.JoycoolSpecialUtil;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 页面相关，顶部底部等等
 * 
 * @author bomb
 *  
 */
public class Page {
	
    public static String top(CallParam callParam) {
    	
    	return BaseAction.getTop(callParam.request, callParam.response);
    }

    public static String bottom(CallParam callParam) {
    	
    	return BaseAction.getBottom(callParam.request, callParam.response);
    }    
    
    public static String bottomShort(CallParam callParam) {
    	
    	return BaseAction.getBottomShort(callParam.request, callParam.response);
    }
    
    public static String time(CallParam callParam) {
    	return DateUtil.getCurrentDatetimeAsStr();
    }
    
    public static String mainTop(CallParam callParam) {
    	return JoycoolSpecialUtil.getLoginMessage(callParam.request, callParam.response);
    }
}