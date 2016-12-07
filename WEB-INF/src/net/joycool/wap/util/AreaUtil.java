/*
 * Created on 2006-4-11
 *
 */
package net.joycool.wap.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.joycool.wap.bean.AreaBean;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *
 */
public class AreaUtil {
    /**
     * 根据手机号取得地区。
     * 
     * @param mobile
     * @return
     */
    public static AreaBean getAreaByMobile(String mobile){
        if(mobile == null){
            return null;
        }
        if(mobile.length() != 11){
            return null;
        }
        
        mobile = mobile.substring(0, 7);
        
        AreaBean area = null;
        DbOperation dbOp = new DbOperation();
        dbOp.init();
        
        //根据手机号取得城市
        String query = "select placeno, cityname, cityno from phonemap where prefixno = '" + mobile + "'";
        ResultSet rs = dbOp.executeQuery(query);
        try {
            if(rs.next()){
                area = new AreaBean();
                area.setCityname(rs.getString("cityname"));
                area.setCityno(rs.getInt("cityno"));
                area.setPlaceno(rs.getInt("placeno"));            
            }
        } catch (SQLException e) {           
            e.printStackTrace();
        }
        
        dbOp.release();
        return area;
    }
}
