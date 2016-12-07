/*
 * Created on 2005-12-19
 *
 */
package net.joycool.wap.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.DiyBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IDiyService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *
 */
public class DiyServiceImpl implements IDiyService{

    /* (non-Javadoc)
     * @see net.joycool.wap.service.infc.IDiyService#getDiy(java.lang.String)
     */
    public DiyBean getDiy(String condition) {
        ICatalogService catalogService = ServiceFactory.createCatalogService();
        
        DiyBean diy = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM diy";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }
        if (sql.indexOf("LIMIT") == -1) {
            sql = sql + " LIMIT 0, 1";
        }

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
                diy = new DiyBean();
                diy.setId(rs.getInt("id"));
                diy.setUserId(rs.getInt("user_id"));
                diy.setCatalogId(rs.getInt("catalog_id"));
                diy.setDisplayOrder(rs.getInt("display_order"));
                diy.setCatalog(catalogService.getCatalog("id = " + diy.getCatalogId()));
                if(diy.getCatalog() == null){
                    dbOp.release();
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return diy;
    }
    /* (non-Javadoc)
     * @see net.joycool.wap.service.infc.IDiyService#getDiyList(java.lang.String)
     */
    public Vector getDiyList(String condition) {
        Vector diyList = new Vector();
        
        ICatalogService catalogService = ServiceFactory.createCatalogService();
        
        DiyBean diy = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return diyList;
        }

        //查询语句
        String sql = "SELECT * FROM diy";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return diyList;
        }

        //将结果保存
        try {
            while (rs.next()) {
                diy = new DiyBean();
                diy.setId(rs.getInt("id"));
                diy.setUserId(rs.getInt("user_id"));
                diy.setCatalogId(rs.getInt("catalog_id"));
                diy.setDisplayOrder(rs.getInt("display_order"));
                diy.setCatalog(catalogService.getCatalog("id = " + diy.getCatalogId()));
                if(diy.getCatalog() != null){
                    diyList.add(diy);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return diyList;
    }
}
