package net.joycool.wap.action.jcforum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.service.impl.JcForumServiceImpl;
import net.joycool.wap.util.db.DbOperation;

public class ForumxService extends JcForumServiceImpl {

	public List getNewsTypeMap(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(2);

		ResultSet rs = dbOp.executeQuery("select * from forum_news_type where " + cond);
		try {
			// 结果不为空
			while (rs.next()) {
				list.add(getNewsType(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return list;
	}
	private NewsTypeBean getNewsType(ResultSet rs) throws SQLException {
		NewsTypeBean bean = new NewsTypeBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setCount(rs.getInt("count"));
		bean.setType(rs.getInt("type"));
		bean.setColumnId(rs.getInt("column_id"));
		bean.setColumnName(rs.getString("column_name"));
		bean.setChild(rs.getInt("child"));
		bean.setParentId(rs.getInt("parent_id"));
		return bean;
	}
	// 添加新闻
	public void addNews(int id, int type) {
		DbOperation dbOp = new DbOperation(2);
		dbOp.executeUpdate("insert ignore into forum_news set id=" + id + ",type=" + type);
		dbOp.release();
	}
	public void delNews(int id, int type) {
		DbOperation dbOp = new DbOperation(2);
		dbOp.executeUpdate("delete from forum_news where id=" + id + " and type=" + type);
		dbOp.release();
	}
}
