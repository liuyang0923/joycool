package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.huangye.JCLinkHuangYeBean;
import net.joycool.wap.service.infc.IHuangYeService;
import net.joycool.wap.util.db.DbOperation;

public class HuangYeServiceImpl implements IHuangYeService {
	// 通过条件获取jc_link_huangye记录
	public Vector getJCLinkHuangYeList(String condition) {
		Vector jcLinkHuangYeList = new Vector();
		JCLinkHuangYeBean huangye = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "select id,number,link_id,mark,enter_datetime, (select name from link_record where link_id=a.link_id) as name from jc_link_huangye as a where " + condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				huangye = this.getJCLinkHuangYe(rs);
				jcLinkHuangYeList.add(huangye);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return jcLinkHuangYeList;
	}

	private JCLinkHuangYeBean getJCLinkHuangYe(ResultSet rs)
			throws SQLException {
		JCLinkHuangYeBean huangye = new JCLinkHuangYeBean();
		huangye.setId(rs.getInt("id"));
		huangye.setNumber(rs.getInt("number"));
		huangye.setLinkId(rs.getInt("link_id"));
		huangye.setMark(rs.getInt("mark"));
		huangye.setLinkName(rs.getString("name"));
		huangye.setEnterDateTime(rs.getString("enter_datetime"));
		return huangye;
	}

	public int getHuangYeRecords(int mark) {
		int count=0;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select count(id) as c_id from jc_link_huangye where mark="+mark;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count=rs.getInt("c_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			dbOp.release();
		}
		return count;

	}

	public Vector getHuangYeList(int pageNo, int perPageRecords,int mark) {
		Vector huangyeList = new Vector();
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql = "SELECT id,number,link_id,mark,enter_datetime, (SELECT url FROM link_record WHERE link_id=a.link_id) AS url, (SELECT name FROM link_record WHERE link_id=a.link_id) AS name FROM jc_link_huangye AS a WHERE mark="+mark+" ORDER BY number  limit "
				+ (pageNo - 1) * perPageRecords + "," + perPageRecords;
		ResultSet rs = dbOp.executeQuery(sql);
		try {
			JCLinkHuangYeBean hy = null;
			while (rs.next()) {
				hy = new JCLinkHuangYeBean();
				hy.setId(rs.getInt("id"));
				hy.setNumber(rs.getInt("number"));
				hy.setLinkId(rs.getInt("link_id"));
				hy.setMark(rs.getInt("mark"));
				hy.setLinkURL(rs.getString("url"));
				hy.setLinkName(rs.getString("name"));
				hy.setEnterDateTime(rs.getString("enter_datetime"));
				huangyeList.add(hy);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			dbOp.release();
		}
		
		return huangyeList;
	}

	public int getHYMaxNum(int mark) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql = "SELECT max(number) FROM jc_link_huangye WHERE mark="+mark;
		ResultSet rs = dbOp.executeQuery(sql);
		try {
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			dbOp.release();
		}
		
		return 0;
	}

	public JCLinkHuangYeBean numExist(int num, int mark) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql = "SELECT id,number,link_id,mark,enter_datetime FROM jc_link_huangye WHERE number="+num+" AND mark="+mark;
		ResultSet rs = dbOp.executeQuery(sql);
		try {
			if (rs.next())
			{
				JCLinkHuangYeBean hy=new  JCLinkHuangYeBean();
				hy.setId(rs.getInt("id"));
				hy.setNumber(rs.getInt("number"));
				hy.setLinkId(rs.getInt("link_id"));
				hy.setMark(rs.getInt("mark"));
				return hy;
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			dbOp.release();
		}
		
		return null;
	}

	// 判断输入的link_id是否在link_record中存在
	public boolean linkIdExist(String table,String condition) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql = "SELECT link_id FROM "+table+" "+condition;
		ResultSet rs = dbOp.executeQuery(sql);
		try {
			if (rs.next())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			dbOp.release();
		}
		
		return false;
	}

	public boolean addHY(JCLinkHuangYeBean hy) {
		boolean ok = false;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql = "INSERT INTO jc_link_huangye(number,link_id,mark,enter_datetime) values(?,?,?,now())";
		dbOp.prepareStatement(sql);
		PreparedStatement ps = dbOp.getPStmt();
		try {
			ps.setInt(1, hy.getNumber());
			ps.setInt(2, hy.getLinkId());
			ps.setInt(3, hy.getMark());
			ps.execute();
			ok = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			dbOp.release();
		}
		
		return ok;
	}

	public boolean alterHY(JCLinkHuangYeBean hy) {
		boolean ok = false;

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql = "UPDATE jc_link_huangye SET number=" + hy.getNumber()
				+ ",link_id=" + hy.getLinkId() + ",mark=" + hy.getMark()
				+ " WHERE id=" + hy.getId();
		if (dbOp.executeUpdate(sql))
			ok = true;
		dbOp.release();
		return ok;
	}
	
	public boolean deleteHY(int id)
	{
		boolean ok=false;

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql = "DELETE FROM jc_link_huangye WHERE id=" + id;
		if (dbOp.executeUpdate(sql))
			ok = true;
		dbOp.release();
		
		return ok;
	}
	
	public boolean changNumber(JCLinkHuangYeBean one,JCLinkHuangYeBean two)
	{
		boolean ok = false;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String sql1 = "UPDATE jc_link_huangye SET number="+one.getNumber()+",link_id="+one.getLinkId()+",mark="+one.getMark()+",enter_datetime=now() WHERE id=" + one.getId();
		dbOp.executeUpdate(sql1);
		String sql2 = "UPDATE jc_link_huangye SET number="+two.getNumber()+",link_id="+two.getLinkId()+",mark="+two.getMark()+",enter_datetime=now() WHERE id=" + two.getId();		
		if (dbOp.executeUpdate(sql2))
			ok = true;
		dbOp.release();
		
		return ok;
		
	}
}
