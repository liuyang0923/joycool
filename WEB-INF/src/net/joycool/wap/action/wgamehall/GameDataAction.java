/*
 * Created on 2006-3-9
 *
 */
package net.joycool.wap.action.wgamehall;

import java.util.Hashtable;

import net.joycool.wap.bean.wgamehall.FootballDataBean;
import net.joycool.wap.bean.wgamehall.GoBangDataBean;
import net.joycool.wap.bean.wgamehall.OthelloDataBean;
import net.joycool.wap.bean.wgamehall.JinhuaDataBean;

/**
 * @author lbj
 *  
 */
public class GameDataAction {
    private static Hashtable gobangData;

    /**
     * 加入五子棋数据。
     * 
     * @param uniqueMark
     * @param data
     */
    public static void putGoBangData(String uniqueMark, GoBangDataBean data) {
        if (gobangData == null) {
            gobangData = new Hashtable();
        }
        gobangData.put(uniqueMark, data);
    }

    /**
     * 取出五子棋数据。
     * 
     * @param uniqueMark
     * @return
     */
    public static GoBangDataBean getGoBangData(String uniqueMark) {
        if (gobangData == null) {
            return null;
        }
        return (GoBangDataBean) gobangData.get(uniqueMark);
    }

    /**
     * 删除五子棋数据。
     * 
     * @param uniqueMark
     */
    public static void removeGoBangData(String uniqueMark) {
        if (gobangData != null) {
            gobangData.remove(uniqueMark);
        }
    }
    
    private static Hashtable othelloData;

    /**
     * 加入黑白棋数据。
     * 
     * @param uniqueMark
     * @param data
     */
    public static void putOthelloData(String uniqueMark, OthelloDataBean data) {
        if (othelloData == null) {
        	othelloData = new Hashtable();
        }
        othelloData.put(uniqueMark, data);
    }

    /**
     * 取出黑白棋数据。
     * 
     * @param uniqueMark
     * @return
     */
    public static OthelloDataBean getOthelloData(String uniqueMark) {
        if (othelloData == null) {
            return null;
        }
        return (OthelloDataBean) othelloData.get(uniqueMark);
    }

    /**
     * 删除黑白棋数据。
     * 
     * @param uniqueMark
     */
    public static void removeOthelloData(String uniqueMark) {
        if (othelloData != null) {
            othelloData.remove(uniqueMark);
        }
    }
    
    private static Hashtable footballData;

    /**
     * 加入点球数据。
     * 
     * @param uniqueMark
     * @param data
     */
    public static void putFootballData(String uniqueMark, FootballDataBean data) {
        if (footballData == null) {
        	footballData = new Hashtable();
        }
        footballData.put(uniqueMark, data);
    }

    /**
     * 取出点球数据。
     * 
     * @param uniqueMark
     * @return
     */
    public static FootballDataBean getFootballData(String uniqueMark) {
        if (footballData == null) {
            return null;
        }
        return (FootballDataBean) footballData.get(uniqueMark);
    }

    /**
     * 删除点球数据。
     * 
     * @param uniqueMark
     */
    public static void removeFootballData(String uniqueMark) {
        if (footballData != null) {
            footballData.remove(uniqueMark);
        }
    }
    
    private static Hashtable jinhuaData;
    
    /**
     * 加入金花数据。
     * 
     * @param uniqueMark
     * @param data
     */
    public static void putJinhuaData(String uniqueMark, JinhuaDataBean data) {
        if (jinhuaData == null) {
        	jinhuaData = new Hashtable();
        }
        jinhuaData.put(uniqueMark, data);
    }

    /**
     * 取出金花数据。
     * 
     * @param uniqueMark
     * @return
     */
    public static JinhuaDataBean getJinhuaData(String uniqueMark) {
        if (jinhuaData == null) {
            return null;
        }
        return (JinhuaDataBean) jinhuaData.get(uniqueMark);
    }

    /**
     * 删除金花数据。
     * 
     * @param uniqueMark
     */
    public static void removeJinhuaData(String uniqueMark) {
        if (jinhuaData != null) {
            jinhuaData.remove(uniqueMark);
        }
    }

	public static Hashtable getJinhuaData() {
		return jinhuaData;
	}

	public static void setJinhuaData(Hashtable jinhuaData) {
		GameDataAction.jinhuaData = jinhuaData;
	}
}
