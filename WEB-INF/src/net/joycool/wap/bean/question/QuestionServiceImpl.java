//20070207 liq
package net.joycool.wap.bean.question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class QuestionServiceImpl implements QuestionService {
	
	/*
	 * (non-Javadoc)
	 * 
	 * 	获取所有系统题库中的题目,放入缓存
	 */
	public HashMap getAllQuestionWareHouse(int grade) {
		QuestionWareHouseBean wareHouse = null;
		HashMap map = new HashMap();
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query;
		if (grade == 0)
			query = "select * from game_question where id < 10000";
		else
			query = "select * from game_question where id >= 10000";
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				wareHouse = new QuestionWareHouseBean();
				wareHouse.setId(rs.getInt("id"));
				wareHouse.setName(StringUtil.toWml(rs.getString("name")));
				wareHouse.setKey1(StringUtil.toWml(rs.getString("key1")));
				wareHouse.setKey2(StringUtil.toWml(rs.getString("key2")));
				wareHouse.setKey3(StringUtil.toWml(rs.getString("key3")));
				wareHouse.setKey4(StringUtil.toWml(rs.getString("key4")));
				wareHouse.setResult(rs.getInt("result"));
				wareHouse.setGrade(rs.getInt("grade"));
				map.put(new Integer(rs.getInt("id")), wareHouse);
//				System.out.println("name ="+rs.getString("name"));
//				System.out.println("key1 ="+rs.getString("key1"));
//				System.out.println("key2 ="+rs.getString("key2"));
//				System.out.println("key3 ="+rs.getString("key3"));
//				System.out.println("result ="+rs.getInt("result"));
//				System.out.println("grade ="+rs.getInt("grade"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return map;
	}

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
			List questionTotalList = (List) OsCacheUtil.get(key,OsCacheUtil.GAME_QUESTION_TOTAL_CACHE_GROUP, OsCacheUtil.GAME_QUESTION_TOTAL_CACHE_FLUSH_PERIOD);
			if (questionTotalList != null) {
				return questionTotalList;
			}
		}
		// lq_2007-02-08_缓存_end
		List list = new ArrayList();
		// 数据操作类
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

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
			OsCacheUtil.put(key, list, OsCacheUtil.GAME_QUESTION_TOTAL_CACHE_GROUP);
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
		DbOperation dbOp = new DbOperation();
		// 初始化
		if (!dbOp.init()) {
			return null;
		}

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

		DbOperation dbOp = new DbOperation();
		dbOp.init();
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
		DbOperation dbOp = new DbOperation(0);

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
		DbOperation dbOp = new DbOperation();
		dbOp.init();
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
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		int x = 0;
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
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		int x = 0;
		// 构建查询语句
		String query = "select count(*) from game_question_history where "
				+ value + " > " + Integer.toString(int_value) + " order by id";
		// System.out.println("sssssssss ="+query);
		try {
			// 查询
			rs = dbOp.executeQuery(query);
			// System.out.println("shhhhhhhhhhhhhhhhhhhhhhhh");
			while (rs.next()) {
				xxx = rs.getInt(1) + 1;
			}
			;
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
		DbOperation dbOp = new DbOperation();
		dbOp.init();
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




