/*
 * Created on 2005-11-28
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class JaLineServiceImpl implements IJaLineService {

	static final String tableName = "jc_page_tree";

	public static ICacheMap columnCache = CacheManage.column;
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.www.service.infc.IJaLineService#addLine(net.joycool.www.bean.LineBean)
	 */
	public boolean addLine(JaLineBean line) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "INSERT INTO "
				+ tableName
				+ " SET "
				+ "parent_id=?, name=?, title=?, top_icon=?, "
				+ "top_text=?, line_index=?, link_type=?, link=?, "
				+ "display_type=?, left_wap10=?, left_wap20=?, left_icon=?, "
				+ "center_wap10=?, center_wap20=?, center_icon=?, right_wap10=?, "
				+ "right_wap20=?, right_icon=?, child_count_per_page=?, child_paragraph_style=?, "
				+ "child_align=?, child_bgimage=?, child_bgmusic=?, date_control=?, "
				+ "date_start=?, date_end=?, time_control=?, time_start=?, "
				+ "time_end=?, description=?, line_end=?, back_to=?, "
				+ "ua_restrict=?, ip_restrict=?, login_restrict=?, mark=?, root_back_to=?";

		dbOp.prepareStatement(query);

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		int lineIndex = dbOp.getMaxId("line_index", tableName, "parent_id = "
				+ line.getParentId()) + 1;

		PreparedStatement pstmt = dbOp.getPStmt();

		try {
			pstmt.setInt(1, line.getParentId());
			pstmt.setString(2, line.getName());
			pstmt.setString(3, line.getTitle());
			pstmt.setString(4, line.getTopIcon());
			pstmt.setString(5, line.getTopText());
			pstmt.setInt(6, lineIndex);
			pstmt.setInt(7, line.getLinkType());
			pstmt.setString(8, line.getLink());
			pstmt.setInt(9, line.getDisplayType());
			pstmt.setString(10, line.getLeftWap10());
			pstmt.setString(11, line.getLeftWap20());
			pstmt.setString(12, line.getLeftIcon());
			pstmt.setString(13, line.getCenterWap10());
			pstmt.setString(14, line.getCenterWap20());
			pstmt.setString(15, line.getCenterIcon());
			pstmt.setString(16, line.getRightWap10());
			pstmt.setString(17, line.getRightWap20());
			pstmt.setString(18, line.getRightIcon());
			pstmt.setInt(19, line.getChildCountPerPage());
			pstmt.setInt(20, line.getChildParagraphStyle());
			pstmt.setString(21, line.getChildAlign());
			pstmt.setString(22, line.getChildBgimage());
			pstmt.setString(23, line.getChildBgmusic());
			pstmt.setInt(24, line.getDateControl());
			pstmt.setString(25, line.getDateStart());
			pstmt.setString(26, line.getDateEnd());
			pstmt.setInt(27, line.getTimeControl());
			pstmt.setInt(28, line.getTimeStart());
			pstmt.setInt(29, line.getTimeEnd());
			pstmt.setString(30, line.getDescription());
			pstmt.setString(31, line.getLineEnd());
			pstmt.setString(32, line.getBackTo());
			pstmt.setString(33, line.getUaRestrict());
			pstmt.setString(34, line.getIpRestrict());
			pstmt.setInt(35, line.getLoginRestrict());
			pstmt.setInt(36, line.getMark());
			pstmt.setString(37, line.getRootBackTo());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		boolean result = dbOp.executePstmt();

		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.www.service.infc.ILineService#deleteLine(java.lang.String)
	 */
	public boolean deleteLine(String condition) {
		boolean result = false;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		Vector lineList = getLineList(condition);
		if (lineList.size() == 0) {
			return true;
		}
		int count, i;
		count = lineList.size();
		JaLineBean line = null;
		Vector children = null;

		for (i = 0; i < count; i++) {
			line = (JaLineBean) lineList.get(i);
			condition = "parent_id = " + line.getId();
			children = getLineList(condition);
			lineList.addAll(children);
			count += children.size();
		}

		// 构建查询语句
		String query = "DELETE FROM " + tableName;

		for (i = 0; i < count; i++) {
			line = (JaLineBean) lineList.get(i);
			query += " WHERE id = " + line.getId();
			dbOp.executeUpdate(query);
		}

		// 释放资源
		dbOp.release();
		return true;
	}

	public JaLineBean getLine(ResultSet rs) throws SQLException {
		JaLineBean line = new JaLineBean();
		line.setId(rs.getInt("id"));
		line.setParentId(rs.getInt("parent_id"));
		line.setName(rs.getString("name"));
		line.setTitle(rs.getString("title"));
		line.setTopIcon(rs.getString("top_icon"));
		line.setTopText(rs.getString("top_text"));
		line.setLineIndex(rs.getInt("line_index"));
		line.setLinkType(rs.getInt("link_type"));
		line.setLink(rs.getString("link"));
		line.setDisplayType(rs.getInt("display_type"));
		line.setLeftWap10(rs.getString("left_wap10"));
		line.setLeftWap20(rs.getString("left_wap20"));
		line.setLeftIcon(rs.getString("left_icon"));
		line.setCenterWap10(rs.getString("center_wap10"));
		line.setCenterWap20(rs.getString("center_wap20"));
		line.setCenterIcon(rs.getString("center_icon"));
		line.setRightWap10(rs.getString("right_wap10"));
		line.setRightWap20(rs.getString("right_wap20"));
		line.setRightIcon(rs.getString("right_icon"));
		line.setChildCountPerPage(rs.getInt("child_count_per_page"));
		line.setChildParagraphStyle(rs.getInt("child_paragraph_style"));
		line.setChildAlign(rs.getString("child_align"));
		line.setChildBgimage(rs.getString("child_bgimage"));
		line.setChildBgmusic(rs.getString("child_bgmusic"));
		line.setDateControl(rs.getInt("date_control"));
		line.setDateStart(rs.getString("date_start"));
		line.setDateEnd(rs.getString("date_end"));
		line.setTimeControl(rs.getInt("time_control"));
		line.setTimeStart(rs.getInt("time_start"));
		line.setTimeEnd(rs.getInt("time_end"));
		line.setDescription(rs.getString("description"));
		line.setLineEnd(rs.getString("line_end"));
		line.setBackTo(rs.getString("back_to"));
		line.setRootBackTo(rs.getString("root_back_to"));
		line.setMark(rs.getInt("mark"));
		line.setUaRestrict(rs.getString("ua_restrict"));
		line.setIpRestrict(rs.getString("ip_restrict"));
		line.setLoginRestrict(rs.getInt("login_restrict"));
		line.setWapType(rs.getInt("wap_type"));
		line.setChildWapType(rs.getInt("child_wap_type"));
		return line;
	}
	
	public JaLineBean getLine(int id) {
		Integer key = Integer.valueOf(id);
		synchronized(columnCache) {
			JaLineBean line = (JaLineBean)columnCache.get(key);
			if(line == null) {
				line = getLineDB("id=" + id);
				if(line != null)
					columnCache.put(key, line);
			}
			return line;
		}
	}

	public JaLineBean getLine(String condition) {
		String key = condition;
		synchronized(columnCache) {
			JaLineBean line = (JaLineBean)columnCache.get(key);
			if(line == null) {
				line = getLineDB(condition);
				if(line != null)
					columnCache.put(key, line);
			}
			return line;
		}
	}
	
	public JaLineBean getLineDB(String condition) {
		String query = "SELECT * FROM " + tableName;
		if (condition != null) {
			query += " WHERE " + condition;
		}
		JaLineBean line = null;
		DbOperation dbOp = new DbOperation(true);
		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				line = getLine(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return line;
	}

	public Vector getLineList(String condition) {
		String query = "SELECT * FROM " + tableName;
		if (condition != null) {
			query += " WHERE " + condition;
		}
		String key = condition;
		synchronized(columnCache) {
			Vector lineList = (Vector) columnCache.get(key);
			if (lineList != null) {
				return lineList;
			}
			lineList = new Vector();
	
			DbOperation dbOp = new DbOperation(true);
			ResultSet rs = dbOp.executeQuery(query);
	
			try {
				while (rs.next()) {
					lineList.add(getLine(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();

			columnCache.put(key, lineList);
			return lineList;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.www.service.infc.ILineService#updateLine(net.joycool.www.bean.JaLineBean)
	 */
	public boolean updateLine(JaLineBean line) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "UPDATE "
				+ tableName
				+ " SET "
				+ "parent_id=?, name=?, title=?, top_icon=?, "
				+ "top_text=?, line_index=?, link_type=?, link=?, "
				+ "display_type=?, left_wap10=?, left_wap20=?, left_icon=?, "
				+ "center_wap10=?, center_wap20=?, center_icon=?, right_wap10=?, "
				+ "right_wap20=?, right_icon=?, child_count_per_page=?, child_paragraph_style=?, "
				+ "child_align=?, child_bgimage=?, child_bgmusic=?, date_control=?, "
				+ "date_start=?, date_end=?, time_control=?, time_start=?, "
				+ "time_end=?, description=?, line_end=?, back_to=?, "
				+ "ua_restrict=?, ip_restrict=?, login_restrict=?, mark=?, root_back_to=? WHERE id=?";

		dbOp.prepareStatement(query);

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();

		try {
			pstmt.setInt(1, line.getParentId());
			pstmt.setString(2, line.getName());
			pstmt.setString(3, line.getTitle());
			pstmt.setString(4, line.getTopIcon());
			pstmt.setString(5, line.getTopText());
			pstmt.setInt(6, line.getLineIndex());
			pstmt.setInt(7, line.getLinkType());
			pstmt.setString(8, line.getLink());
			pstmt.setInt(9, line.getDisplayType());
			pstmt.setString(10, line.getLeftWap10());
			pstmt.setString(11, line.getLeftWap20());
			pstmt.setString(12, line.getLeftIcon());
			pstmt.setString(13, line.getCenterWap10());
			pstmt.setString(14, line.getCenterWap20());
			pstmt.setString(15, line.getCenterIcon());
			pstmt.setString(16, line.getRightWap10());
			pstmt.setString(17, line.getRightWap20());
			pstmt.setString(18, line.getRightIcon());
			pstmt.setInt(19, line.getChildCountPerPage());
			pstmt.setInt(20, line.getChildParagraphStyle());
			pstmt.setString(21, line.getChildAlign());
			pstmt.setString(22, line.getChildBgimage());
			pstmt.setString(23, line.getChildBgmusic());
			pstmt.setInt(24, line.getDateControl());
			pstmt.setString(25, line.getDateStart());
			pstmt.setString(26, line.getDateEnd());
			pstmt.setInt(27, line.getTimeControl());
			pstmt.setInt(28, line.getTimeStart());
			pstmt.setInt(29, line.getTimeEnd());
			pstmt.setString(30, line.getDescription());
			pstmt.setString(31, line.getLineEnd());
			pstmt.setString(32, line.getBackTo());
			pstmt.setString(33, line.getUaRestrict());
			pstmt.setString(34, line.getIpRestrict());
			pstmt.setInt(35, line.getLoginRestrict());
			pstmt.setInt(36, line.getMark());
			pstmt.setString(37, line.getRootBackTo());
			pstmt.setInt(38, line.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		boolean result = dbOp.executePstmt();

		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateLine(String set, String condition) {
		boolean result = false;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "UPDATE " + tableName + " SET " + set + " WHERE "
				+ condition;

		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();
		return true;
	}

	public int getLineCount(String condition) {
		String query = "SELECT count(id) as c_id FROM " + tableName + " WHERE " + condition;

		int count = 0;
		synchronized(columnCache) {
			String key = "c " + condition;
			Integer c = (Integer) columnCache.get(key);
			if (c != null) {
				return c.intValue();
			}

			DbOperation dbOp = new DbOperation(true);
	
			ResultSet rs = dbOp.executeQuery(query);
	
			try {
				if (rs.next()) {
					count = rs.getInt("c_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
			dbOp.release();
	
			columnCache.put(key, new Integer(count));
			return count;
		}
	}
}
