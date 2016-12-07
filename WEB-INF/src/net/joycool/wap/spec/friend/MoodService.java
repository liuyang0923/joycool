package net.joycool.wap.spec.friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class MoodService {

	// 取得所有心情
	public ArrayList getAllMood(int thePage, int pageInfoCount) {
		ArrayList list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from mood_list order by id desc limit "
						+ (thePage * pageInfoCount) + "," + pageInfoCount);
		try {
			while (rs.next()) {
				list.add(getMood(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	// 更新mood_user表
	public boolean updateLastMood(int userId, String mood,int type) {
		// 如果该表中有些用户的记录则更新他的心情，否则就将该用户加入
		int tmpUid = 0;
		boolean result = false;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select user_id from mood_user where user_id = "
						+ userId);
		try {
			if (rs.next()) {
				tmpUid = rs.getInt("user_id");
			}
			if (tmpUid == 0) {
				// 如果uid不存在，则将其加入表中
				result = db
						.executeUpdate("insert into mood_user (user_id,mood,create_time,type) values ("
								+ userId
								+ ",'"
								+ StringUtil.toSql(mood)
								+ "',now()," + type +")");
//				 System.out.println("insert into mood_user (user_id,mood,create_time,type) values ("
//						+ userId
//						+ ",'"
//						+ StringUtil.toSql(mood)
//						+ "',now()," + type +")");
			} else {
				// 更新表
				result = db.executeUpdate("update mood_user set mood='"
						+ StringUtil.toSql(mood)
						+ "',create_time=now(),type=" + type + " where user_id=" + userId);
//				System.out.println("update mood_user set mood='"
//						+ StringUtil.toSql(mood)
//						+ "',create_time=now(),type=" + type + " where user_id=" + userId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}

	// 删除mood_user表中的一条记录
	public boolean deleteLastMoodList(int uid) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("delete from mood_user where user_id=" + uid);
		db.release();
//		System.out.println("delete from mood_user where user_id=" + uid);
		return result;
	}

	// 根据uid，从mood_user表中取出一条记录
	public MoodUserBean getLastMoodBean(int uid) {
		MoodUserBean mub=null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from mood_user where user_id=" + uid);
		try {
			if (rs.next()) {
				mub=getLastMood(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return mub;
	}

	// 返回mood_user表中的所有记录(分页)
	public List getAllLastMood(int thePage, int pageInfoCount){
		List list=new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs=db.executeQuery("select * from mood_user limit " + (thePage * pageInfoCount) + "," + pageInfoCount);
		try {
			while(rs.next()){
				list.add(getLastMood(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	//返回好友心情时调用
	public List getMoodUserList(String cond){
		List list=new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs=db.executeQuery("select * from mood_user where " + cond);
		try {
			while(rs.next()){
				list.add(getLastMood(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	// 查找同心情的人
	public List getSameMood(int type,int thePage, int pageInfoCount){
		DbOperation db = new DbOperation(5);
		List list=new ArrayList();
//		System.out.println("select * from mood_user where type=" + type + " limit " + (thePage * pageInfoCount) + "," + pageInfoCount);
		ResultSet rs=db.executeQuery("select * from mood_user where type=" + type + " limit " + (thePage * pageInfoCount) + "," + pageInfoCount);
		try {
			while(rs.next()){
				list.add(getLastMood(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	// 写入一个心情
	public boolean createNewMood(int userId, String mood,int type) {
		boolean result;
		DbOperation db = new DbOperation(5);
		if (updateLastMood(userId, mood, type)) {
			result = db
					.executeUpdate("insert into mood_list (user_id,mood,create_time,view_count,reply_count,type) values ("
							+ userId
							+ ",'"
							+ StringUtil.toSql(mood)
							+ "',"
							+ "now(),0,0," + type + ")");
//			System.out
//					.println("insert into mood_list (user_id,mood,create_time,view_count,reply_count,type) values ("
//							+ userId
//							+ ",'"
//							+ StringUtil.toSql(mood)
//							+ "',"
//							+ "now(),0,0," + type + ")");
		} else {
			// mood_user表更新失败才会走到这里。还没想好要做什么。
			result=false;
		}
		db.release();
		return result;
	}

	// 根据心情id，写入一个回复
	public boolean replyById(int userId, int moodId, String reply) {
		DbOperation db = new DbOperation(5);
		boolean result = db
				.executeUpdate("insert into mood_reply (mood_id,user_id,create_time,reply) values ("
						+ moodId
						+ ","
						+ userId
						+ ",now(),'"
						+ StringUtil.toSql(reply) + "')");
//		System.out
//				.println("写入一个回复：insert into mood_reply (mood_id,user_id,create_time,reply) values ("
//						+ moodId + "," + userId + ",now(),'"
//						+ StringUtil.toSql(reply) + "')");
		db.release();
		return result;
	}

	// 根据id，取得一个回复
	public MoodReply getReplyById(int id) {
		MoodReply mr = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from mood_reply where id=" + id);
		try {
			if (rs.next()) {
				mr = getReply(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return mr;
	}

	// 根据id，取出一个心情
	public MoodBean getMoodById(int id) {
		MoodBean mb = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from mood_list where id=" + id);
		try {
			if (rs.next()) {
				mb = getMood(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return mb;
	}

	MoodBean getMood(ResultSet rs) throws SQLException {
		MoodBean mb = new MoodBean();
		mb.setId(rs.getInt("id"));
		mb.setUserId(rs.getInt("user_id"));
		mb.setMood(rs.getString("mood"));
		mb.setCreateTime(rs.getTimestamp("create_time").getTime());
		mb.setViewCount(rs.getInt("view_count"));
		mb.setReplyCount(rs.getInt("reply_count"));
		mb.setType(rs.getInt("type"));
		return mb;
	}

	MoodReply getReply(ResultSet rs) throws SQLException {
		MoodReply mr = new MoodReply();
		mr.setId(rs.getInt("id"));
		mr.setMoodId(rs.getInt("mood_id"));
		mr.setUserID(rs.getInt("user_id"));
		mr.setCreateTime(rs.getTimestamp("create_time").getTime());
		mr.setReply(rs.getString("reply"));
		return mr;
	}

	MoodUserBean getLastMood(ResultSet rs) throws SQLException{
		MoodUserBean mub=new MoodUserBean();
		mub.setUserId(rs.getInt("user_id"));
		mub.setMood(rs.getString("mood"));
		mub.setCreateTime(rs.getTimestamp("create_time").getTime());
		mub.setType(rs.getInt("type"));
		return mub;
	}
	
	// 根据用户id，取得其最新的心情
	public MoodBean getLastMoodByUid(int uid) {
		MoodBean mb = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from mood_list where user_id="
				+ uid + " order by id desc limit 1");
		try {
			if (rs.next()) {
				mb = getMood(rs);
//				System.out.println("最新心情：" + mb.getMood());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return mb;
	}

	// 取得所有心情的回复
	public List getAllMoodReply(int thePage, int pageInfoCount) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from mood_reply order by id desc limit "
						+ (thePage * pageInfoCount) + "," + pageInfoCount);
		try {
			while (rs.next()) {
				list.add(getReply(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	// 根据心情id，取得其所有回复(分页)
	public ArrayList getReplyById(int id, int thePage, int pageInfoCount) {
		ArrayList list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from mood_reply where mood_id=" + id
						+ " order by id desc limit "
						+ (thePage * pageInfoCount) + "," + pageInfoCount);
		try {
			while (rs.next()) {
				list.add(getReply(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	// 根据用户id,取得其所有的心情(分页)
	public ArrayList getAllMoodById(int uid, int thePage, int pageInfoCount) {
		ArrayList list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from mood_list where user_id="
				+ uid + " order by id desc limit " + (thePage * pageInfoCount)
				+ "," + pageInfoCount);
		try {
			while (rs.next()) {
				list.add(getMood(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	// 浏览次数+1
	public boolean increaseViewCount(int id) {
		DbOperation db = new DbOperation(5);
		boolean result = db
				.executeUpdate("update mood_list set view_count=view_count + 1 where id="
						+ id);
		db.release();
		return result;
	}

	// 回复次数+1
	public boolean increaseReplyCount(int id) {
		DbOperation db = new DbOperation(5);
		boolean result = db
				.executeUpdate("update mood_list set reply_count=reply_count + 1 where id="
						+ id);
		db.release();
		return result;
	}

	// 回复次数-1
	public boolean decreaseReplyCount(int id) {
		DbOperation db = new DbOperation(5);
		boolean result = db
				.executeUpdate("update mood_list set reply_count=reply_count - 1 where id="
						+ id);
		db.release();
		return result;
	}

	// 根据心情id，删除一个心情
	public boolean deleteMood(int id, int uid) {
		DbOperation db = new DbOperation(5);
		boolean result = db.executeUpdate("delete from mood_list where id=" + id);
		// 这个心情下的所有回复，也要一起删除
		result = db.executeUpdate("delete from mood_reply where mood_id=" + id);

//		// ==================这一部分还是很麻烦的==================
//
//		// 更新mood_user表
//		MoodBean mb = getLastMoodByUid(uid);
//		if (mb != null) {
//			result = updateLastMood(mb.getUserId(), mb.getMood(),mb.getType());
//		} else {
//			// 如果mb==null，则判定为该用户不存在，或该用户没有发表过心情，或该用户的心情已被删完
//			result = deleteLastMoodList(uid);
//		}
		db.release();
		return result;
	}
	
	//根据用户id，删除其最新心情
	public boolean deleteMood(int uid){
		DbOperation db = new DbOperation(5);
		//删除最新的心情
		boolean result = db.executeUpdate("delete from mood_user where user_id=" + uid);
//		System.out.println("delete from mood_user where user_id=" + uid);
		db.release();
		return result;
	}

	// 根据回复id，删除一个回复
	public boolean deleteReply(int id) {
		DbOperation db = new DbOperation(5);
		boolean result = db.executeUpdate("delete from mood_reply where id=" + id);
//		System.out.println("删除回复：delete from mood_reply where id=" + id);
		db.release();
		return result;
	}
}