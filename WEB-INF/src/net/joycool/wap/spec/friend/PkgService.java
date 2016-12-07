package net.joycool.wap.spec.friend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class PkgService {

	public boolean addPkg(PkgBean bean) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = "INSERT INTO mcoolgame.pkg(user_id,type,title,content,item,create_time,open_time) VALUES(?,?,'','','',now(),'2008-12-25 8:00')";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getType());
			pstmt.execute();
			bean.setId(dbOp.getLastInsertId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	// 更新礼包内容，分3个函数，分别更新文字和时间和物品
	public boolean updatePkg(PkgBean bean) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = "update mcoolgame.pkg set title=?,content=? where id=" + bean.getId();

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	public boolean updatePkg2(PkgBean bean) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = "update mcoolgame.pkg set send_time=?,open_time=? where id=" + bean.getId();

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setTimestamp(1, new Timestamp(bean.getSendTime()));
			pstmt.setTimestamp(2, new Timestamp(bean.getOpenTime()));
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	public boolean updatePkg3(PkgBean bean) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = "update mcoolgame.pkg set item=?,money=?,item_name=? where id=" + bean.getId();

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getItem());
			pstmt.setInt(2, bean.getMoney());
			pstmt.setString(3, bean.getItemName());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}

	public PkgBean getPkg(String cond) {
		PkgBean bean = null;
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("SELECT * from mcoolgame.pkg where " + cond);
		try{
			if(rs.next()) {
				bean = getPkg(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return bean;
	}
	
	public PkgBean getPkg(ResultSet rs) throws SQLException {
		PkgBean bean = new PkgBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setToId(rs.getInt("to_id"));
		bean.setMoney(rs.getInt("money"));
		bean.setStatus(rs.getInt("status"));
		bean.setType(rs.getInt("type"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setSendTime(rs.getTimestamp("send_time").getTime());
		bean.setOpenTime(rs.getTimestamp("open_time").getTime());
		bean.setTitle(rs.getString("title"));
		bean.setContent(rs.getString("content"));
		bean.setItem(rs.getString("item"));
		bean.setItemName(rs.getString("item_name"));
		bean.initItem();
		return bean;
	}
	
	public List getPkgTypeList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(4);
		ResultSet rs = db.executeQuery("SELECT * from mcoolgame.pkg_type where " + cond);
		try{
			while(rs.next()) {
				list.add(getPkgType(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	
	public PkgTypeBean getPkgType(ResultSet rs) throws SQLException {
		PkgTypeBean bean = new PkgTypeBean();
		bean.setId(rs.getInt("id"));
		bean.setCount(rs.getInt("count"));
		bean.setFlag(rs.getInt("flag"));
		bean.setPrice(rs.getInt("price"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		return bean;
	}
}
