package jc.guest.wall;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class WallService {
	
	public WallBean getWallBean(String cond){
		WallBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wall where " + cond);
		try {
			if (rs.next()){
				bean = getWall(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getWallList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from wall where " + cond);
		try {
			while (rs.next()){
				list.add(getWall(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addWall(WallBean bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into wall(title,uid,short_cont,`content`,create_time,visible) values (?,?,?,?,now(),?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1,bean.getTitle());
			pstmt.setInt(2, bean.getUid());
			pstmt.setString(3, bean.getShortCont());
			pstmt.setString(4, bean.getContent());
			pstmt.setInt(5, bean.getVisible());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	WallBean getWall(ResultSet rs) throws SQLException{
		WallBean bean = new WallBean();
		bean.setId(rs.getInt("id"));
		bean.setTitle(rs.getString("title"));
		bean.setUid(rs.getInt("uid"));
		bean.setShortCont(rs.getString("short_cont"));
		bean.setContent(rs.getString("content"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setVisible(rs.getInt("visible"));
		return bean;
	}
}
