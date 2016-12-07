package jc.guest.question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.bean.question.QuestionHistory;
import net.joycool.wap.bean.question.eum;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.db.DbOperation;

public class QuestionService {
	
	/*
	 * (non-Javadoc)
	 * 
	 * 	取得历史大排名的列表
	 */
	public List getTotalList() {
		// 查询语句
		String sql = "SELECT * FROM game_question_history order by totalValue DESC limit 0,1000";
		// lq_2007-02-08_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			List questionTotalList = (List) OsCacheUtil.get(key,"gamequestiontotal2", 60*24*60);
			if (questionTotalList != null) {
				return questionTotalList;
			}
		}
		// lq_2007-02-08_缓存_end
		List list = new ArrayList();
		// 数据操作类
		DbOperation dbOp = new DbOperation(6);

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			while (rs.next()) {
				eum temp = new eum();
				temp.setId(rs.getInt("id"));
				temp.setTotalvalue(rs.getInt("totalValue"));
				temp.setTodayvalue(rs.getInt("todayValue"));
				list.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

//		 lq_2007-02-08_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			OsCacheUtil.put(key, list, "gamequestiontotal2");
		}
//		 lq_2007-02-08_缓存_end

		// 返回结果
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 获取今日排名的列表
	 */
	public List getTodayList() {
		// 查询语句
		String sql = "SELECT * FROM game_question_history order by todayValue DESC limit 0,100";
//		 lq_2007-02-08_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			List questionTotalList = (List) OsCacheUtil.get(key,OsCacheUtil.GAME_QUESTION_TODAY_CACHE_GROUP, OsCacheUtil.GAME_QUESTION_TODAY_CACHE_FLUSH_PERIOD);
			if (questionTotalList != null) {
				return questionTotalList;
			}
		}
//		 lq_2007-02-08_缓存_end
		List list = new ArrayList();
		// 数据操作类
		DbOperation dbOp = new DbOperation(6);

		// 查询
		ResultSet rs = dbOp.executeQuery(sql);
		if (rs == null) {
			// 释放资源
			dbOp.release();
			return null;
		}

		// 将结果保存
		try {
			while (rs.next()) {
				eum temp = new eum();
				temp.setId(rs.getInt("id"));
				temp.setTotalvalue(rs.getInt("totalValue"));
				temp.setTodayvalue(rs.getInt("todayValue"));
				list.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

//		 lq_2007-02-08_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = sql;
			OsCacheUtil.put(key, list, OsCacheUtil.GAME_QUESTION_TODAY_CACHE_GROUP);
		}
//		 lq_2007-02-08_缓存_end
		// 返回结果
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 	获取指定id的历史积分
	 */
	public int getTotalValue(int id) {

		DbOperation dbOp = new DbOperation(6);
		int x = 0;
		String query = "select totalValue from game_question_history where id = " + id;

		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {

				x = rs.getInt("totalValue");

			} else {
				query = "insert into game_question_history (id)values("+ id + ")";
				dbOp.executeUpdate(query);
				x = 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();

		return x;
	}
	public QuestionHistory getHistory(int id) {
		QuestionHistory bean = null;
		DbOperation dbOp = new DbOperation(6);

		String query = "select * from game_question_history where id = " + id;

		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				bean = new QuestionHistory();
				bean.setTotal(rs.getInt("totalValue"));
				bean.setToday(rs.getInt("todayValue"));
				bean.setToday2(rs.getInt("today2"));

			} else {

				query = "insert into game_question_history (id)values(" + id + ")";
				dbOp.executeUpdate(query);
				bean = new QuestionHistory();
				bean.setId(id);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();

		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 	获取指定id的历史积分
	 */
	public int getTodayValue(int id) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(6);
		int x = 0;
		// 构建查询语句
		String query = "select todayValue from game_question_history where id = "
				+ Integer.toString(id);
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		// System.out.println("ResultSet=" + rs);
		try {
			// 结果不为空
			if (rs.next()) {
				// System.out.println("不为空");
				x = rs.getInt("todayValue");
			} else {
				// System.out.println("为空");
				// 构建插入语句
				query = "insert into game_question_history (id)values(" + id + ")";
				dbOp.executeUpdate(query);
				x = 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// System.out.println("xxxxxxxxx=" + x);
		// 释放资源
		dbOp.release();

		return x;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 	更新指定id的当前积分和历史积分
	 */
	public void setValue(int id, int totalvalue, int todayvalue) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(6);
//		int x = 0;
		// 构建查询语句
//		String query = "update game_question_history set todayValue = "
//				+ Integer.toString(todayvalue) + ",totalValue = "
//				+ Integer.toString(totalvalue) + " where id = "
//				+ Integer.toString(id);
		String query = "update game_question_history set todayValue = todayValue+1,totalValue = totalValue+1  where id = "+ Integer.toString(id);
		// 查询
		dbOp.executeUpdate(query);
		// System.out.println("xxxxxxxxx=" + x);
		// 释放资源
		dbOp.release();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * 	取得指定id的排名
	 */
	public int getOrderTotal(String value, int int_value) {
		// 数据库操作类
		ResultSet rs = null;
		int xxx = 0;
		DbOperation dbOp = new DbOperation(6);
//		int x = 0;
		// 构建查询语句
		String query = "select count(*) from game_question_history where "
				+ value + " > " + int_value;
		// System.out.println("sssssssss ="+query);
		try {
			// 查询
			rs = dbOp.executeQuery(query);
			// System.out.println("shhhhhhhhhhhhhhhhhhhhhhhh");
			while (rs.next()) {
				xxx = rs.getInt(1) + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();
		return xxx;
	}

	public void setNullToday()
	{
		// 数据库操作类
		DbOperation dbOp = new DbOperation(6);
		String query = "";
		try {
				query = "update game_question_history set todayValue = 0,today2=0";
				dbOp.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbOp.release();

	}
}




