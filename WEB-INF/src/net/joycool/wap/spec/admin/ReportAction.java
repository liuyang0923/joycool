/*
 * Created on 2009-12-10
 *
 */
package net.joycool.wap.spec.admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.db.DbOperation;


/**
 * 
 * 说明：用户举报
 */
public class ReportAction extends CustomAction {
    
	public ReportAction(HttpServletRequest request) {
		super(request);
	}
    public static List getReportList(String cond) {
        DbOperation dbOp = new DbOperation(5);
        ResultSet rs = null;
        List list = new ArrayList();
        rs = dbOp.executeQuery("select * from user_report where " + cond);

        try {
            while (rs.next()) {
            	list.add(getReport(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
        return list;
    }
    public static ReportBean getReport(ResultSet rs) throws SQLException {
    	ReportBean g = new ReportBean();
    	g.setId(rs.getInt("id"));
    	g.setCid(rs.getInt("cid"));
    	g.setCid2(rs.getInt("cid2"));
    	g.setUserId(rs.getInt("user_id"));
    	g.setType(rs.getInt("type"));
    	g.setStatus(rs.getInt("status"));
    	
    	g.setBak(rs.getString("bak"));
    	g.setInfo(rs.getString("info"));
    	g.setReason(rs.getString("reason"));
    	return g;
    }
    
    public static boolean addReport(int userId, int type, int cid, int cid2, String info, String reason, String bak) {
    	if(reason == null)
    		reason = "";
    	if(info == null)
    		info = "";
    	if(bak == null)
    		bak = "";
    	ReportBean bean = new ReportBean();
    	bean.setUserId(userId);
    	bean.setType(type);
    	bean.setCid(cid);
    	bean.setCid2(cid2);
    	bean.setInfo(info);
    	bean.setBak(bak);
    	bean.setReason(reason);
    	return addReport(bean);
    }
    
	public static boolean addReport(ReportBean bean) {
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into user_report (user_id,cid,cid2,type,status,bak,info,reason,create_time)values(?,?,?,?,?,?,?,?,now())";
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getCid());
			pstmt.setInt(3, bean.getCid2());
			pstmt.setInt(4, bean.getType());
			pstmt.setInt(5, bean.getStatus());
			pstmt.setString(6, bean.getBak());
			pstmt.setString(7, bean.getInfo());
			pstmt.setString(8, bean.getReason());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		dbOp.release();
		return true;
	}
}
