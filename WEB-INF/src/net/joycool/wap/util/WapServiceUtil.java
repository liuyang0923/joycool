/*
 * Created on 2005-12-21
 *
 */
package net.joycool.wap.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.WapServiceBean;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *
 */
public class WapServiceUtil {
    public static Vector wapServiceList = null;
    
    public static Vector getWapServiceList(){
        if(wapServiceList != null){
            return wapServiceList;
        }
        
        wapServiceList = new Vector();
        
        WapServiceBean wapService = null;
        
        CatalogBean catalog = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM sp_service ORDER BY id";

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            while (rs.next()) {
                wapService = new WapServiceBean();
                wapService.setId(rs.getInt("id"));
                wapService.setServiceId(rs.getString("service_id"));
                wapService.setName(rs.getString("name"));
                wapService.setOrderAddress(rs.getString("order_address"));   
                wapService.setType(rs.getInt("type"));
                wapServiceList.add(wapService);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return wapServiceList;
    }
    
    public static WapServiceBean getWapServiceById(int id){
        Vector serviceList = getWapServiceList();
        Iterator itr = serviceList.iterator();
        WapServiceBean wapService = null;
        while(itr.hasNext()){
            wapService = (WapServiceBean) itr.next();
            if(wapService.getId() == id){
                return wapService;
            }
        }
        return null;
    }
    
    public static WapServiceBean getWapServiceByServiceId(String serviceId){
        Vector serviceList = getWapServiceList();
        Iterator itr = serviceList.iterator();
        WapServiceBean wapService = null;
        while(itr.hasNext()){
            wapService = (WapServiceBean) itr.next();
            if(wapService.getServiceId().equals(serviceId)){
                return wapService;
            }
        }
        
        wapService = getWapService("service_id = '" + serviceId + "'");
        
        return wapService;        
    }
    
    public static WapServiceBean getWapService(String condition){        
        WapServiceBean wapService = null;
        
        CatalogBean catalog = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM sp_service WHERE " + condition;

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            if (rs.next()) {
                wapService = new WapServiceBean();
                wapService.setId(rs.getInt("id"));
                wapService.setServiceId(rs.getString("service_id"));
                wapService.setName(rs.getString("name"));
                wapService.setOrderAddress(rs.getString("order_address"));   
                wapService.setType(rs.getInt("type"));
                wapServiceList.add(wapService);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return wapService;
    }
}
