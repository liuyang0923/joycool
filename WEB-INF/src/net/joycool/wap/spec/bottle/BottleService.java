package net.joycool.wap.spec.bottle;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class BottleService {
	/**
	 * 根据瓶子ID取得一个瓶子的所有信息
	 * 
	 * @param id
	 * @return BottleBean(一个完整的瓶子)
	 */
	public BottleBean getBottleById(int id) {
		// ArrayList alReply = new ArrayList();
		BottleBean bb = null;
		// BottleReply br;

		DbOperation db = new DbOperation(5);

		// 首先，取得瓶子的基本信息

		ResultSet rs = db.executeQuery("select * from bottle_info where id="
				+ id);
		try {
			while (rs.next()) {
				bb = getBottle(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}

		// 其次，取得瓶子的浏览记录

		// rs = db.executeQuery("select * from bottle_viewer where bottle_id="
		// + id);
		// try {
		// while (rs.next()) {
		// br = new BottleReply();
		//
		// br.setId(rs.getInt("id"));
		// br.setBottleId(rs.getInt("bottle_id"));
		// br.setUserId(rs.getInt("user_id"));
		// br.setViewTime(rs.getTimestamp("view_time").getTime());
		// br.setReply(rs.getString("reply"));
		//
		// alReply.add(br);
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// return null;
		// } finally {
		// db.release();
		// }
		//
		// bb.setReply(alReply);
		return bb;
	}

	/**
	 * 取得全部的瓶子（不分页）
	 * 
	 * @return ArrayList->所有的瓶子
	 */
	public ArrayList getBottles() {
		DbOperation db = new DbOperation(5);

		ResultSet rs = db
				.executeQuery("select * from bottle_info where status=1");
		ArrayList al = new ArrayList();
		try {
			while (rs.next()) {
				al.add(getBottle(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return al;
	}

	/**
	 * 取得全部的瓶子（分页）
	 * 
	 * @param thePage：要显示的页码
	 * @param pageInfoCount：每页显示的信息量
	 * 
	 * @return ArrayList->所有的瓶子
	 */
	public ArrayList getBottles(int thePage, int pageInfoCount) {
		DbOperation db = new DbOperation(5);

		ResultSet rs = db
				.executeQuery("select * from bottle_info order by id desc limit "
						+ (thePage * pageInfoCount) + "," + pageInfoCount);
		ArrayList al = new ArrayList();
		try {
			while (rs.next()) {
				al.add(getBottle(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return al;
	}

	BottleBean getBottle(ResultSet rs) throws SQLException {
		BottleBean bb = new BottleBean();
		bb.setId(rs.getInt("id"));
		bb.setUserId(rs.getInt("user_id"));
		bb.setTitle(rs.getString("title"));
		bb.setContent(rs.getString("content"));
		bb.setMood(rs.getString("mood"));
		bb.setCreateTime(rs.getTimestamp("create_time").getTime());
		bb.setLastPickupTime(rs.getTimestamp("last_pickup_time").getTime());
		bb.setReplyCount(rs.getInt("reply_count"));
		bb.setStatus(rs.getInt("status"));
		bb.setType(rs.getInt("type"));
		return bb;
	}

	// 根据ID，取得瓶子(分页)
	public ArrayList getBottlesById(int id, int thePage, int pageInfoCount) {
		BottleBean bb;
		DbOperation db = new DbOperation(5);

		ResultSet rs = db
				.executeQuery("select * from bottle_info where user_id=" + id
						+ " order by id desc limit "
						+ (thePage * pageInfoCount) + "," + pageInfoCount);
		ArrayList al = new ArrayList();
		try {
			while (rs.next()) {
				bb = new BottleBean();
				bb.setId(rs.getInt("id"));
				bb.setUserId(rs.getInt("user_id"));
				bb.setTitle(rs.getString("title"));
				bb.setCreateTime(rs.getTimestamp("create_time").getTime());
				bb.setReplyCount(rs.getInt("reply_count"));
				al.add(bb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return al;
	}

	// 创建一个瓶子
	public boolean createBottle(BottleBean bb) {
		DbOperation db = new DbOperation(5);

		boolean result = db
				.executeUpdate("insert into bottle_info (user_id,title,content,mood,create_time,last_pickup_time,reply_count,status) values ("
						+ bb.getUserId()
						+ ",'"
						+ StringUtil.toSql(bb.getTitle())
						+ "','"
						+ StringUtil.toSql(bb.getContent())
						+ "','"
						+ StringUtil.toSql(bb.getMood()) + "',now(),now(),0,1)");
		bb.setId(db.getLastInsertId());
		db.release();
		return result;
	}

	// 保存留言
	public boolean saveReply(int bottle_id, int user_id, String reply) {
		DbOperation db = new DbOperation(5);
		// 保存留言
		boolean result = db
				.executeUpdate("insert into bottle_viewer (bottle_id,user_id,view_time,reply) values ("
						+ bottle_id
						+ ","
						+ user_id
						+ ",now(),'"
						+ StringUtil.toSql(reply) + "')");

		// 更新瓶子的总回复数
		db
				.executeUpdate("update bottle_info set reply_count=reply_count+1 where id="
						+ bottle_id);

		db.release();
		return result;
	}

	// 修改上次浏览时间
	public boolean modifyLastPickupTime(int id) {
		// 根据id，修改一个瓶子上次打开的时间

		DbOperation db = new DbOperation(5);
		boolean result = db
				.executeUpdate("update bottle_info set last_pickup_time=now() where id="
						+ id);
		db.release();

		return result;
	}

	// 改变状态，销毁一个瓶子(暂时废弃)
	public boolean changeStatus(int id) {
		// 销毁一个瓶子
		DbOperation db = new DbOperation(5);
		boolean result = db
				.executeUpdate("update bottle_info set status=0 where id=" + id);
		db.release();

		return result;
	}

	// 根据瓶子ID，取得其所有留言数量
	public int getReplyCountById(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select count(id) from bottle_viewer where bottle_id="
						+ id);
		try {
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return 0;
	}

	// 根据瓶子ID，取得其所有留言（分页）
	public ArrayList getReplyById(int id, int thePage, int pageInfoCount) {
		DbOperation db = new DbOperation(5);
		ArrayList al = new ArrayList();
		ResultSet rs = db
				.executeQuery("select id,bottle_id,user_id,view_time,reply from bottle_viewer where bottle_id="
						+ id
						+ " order by id desc limit "
						+ (thePage * pageInfoCount) + "," + pageInfoCount);
		try {
			while (rs.next()) {
				al.add(getReply(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return al;
	}

	// 取得所有回复
	public ArrayList getReplyList(String cond) {
		DbOperation db = new DbOperation(5);
		ArrayList al = new ArrayList();
		ResultSet rs = db
				.executeQuery("select id,bottle_id,user_id,view_time,reply from bottle_viewer "
						+ cond);
		try {
			while (rs.next()) {
				al.add(getReply(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return al;
	}

	private BottleReply getReply(ResultSet rs) throws SQLException {
		BottleReply br = new BottleReply();
		br.setId(rs.getInt("id"));
		br.setBottleId(rs.getInt("bottle_id"));
		br.setUserId(rs.getInt("user_id"));
		br.setViewTime(rs.getTimestamp("view_time").getTime());
		br.setReply(rs.getString("reply"));
		return br;
	}

	// 根据用户ID，取得瓶子
	public ArrayList getBottleByUid(int uid) {
		BottleBean bb;
		DbOperation db = new DbOperation(5);
		ArrayList al = new ArrayList();
		ResultSet rs = db
				.executeQuery("select * from bottle_info where user_id=" + uid);
		try {
			while (rs.next()) {
				bb = new BottleBean();
				bb.setId(rs.getInt("id"));
				bb.setCreateTime(rs.getTimestamp("create_time").getTime());
				al.add(bb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return al;
	}

	// 根据用户ID，取得瓶子(分页)
	public ArrayList getBottleByUidp(int uid, int thePage, int pageInfoCount) {
		DbOperation db = new DbOperation(5);
		ArrayList al = new ArrayList();
		ResultSet rs = db
				.executeQuery("select * from bottle_info where user_id=" + uid
						+ " order by id desc limit "
						+ (thePage * pageInfoCount) + "," + pageInfoCount);
		try {
			while (rs.next()) {
				al.add(getBottle(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return al;
	}

	// 根据ID，取得某一用户回复过的所有瓶子(分页)
	public ArrayList getRevBottleById(int uid, int thePage, int pageInfoCount) {
		BottleBean bb;

		DbOperation db = new DbOperation(5);
		ArrayList al = new ArrayList();
		ResultSet rs = db
				.executeQuery("select distinct bottle_id from bottle_viewer where user_id="
						+ uid
						+ " order by id desc limit "
						+ (thePage * pageInfoCount) + "," + pageInfoCount);
		try {
			while (rs.next()) {
				bb = new BottleBean();
				// 根据UserID，取得它回复过的瓶子的ID，再根据这个ID,来取得一个瓶子。
				bb = this.getBottleById(rs.getInt("bottle_id"));
				al.add(bb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return al;
	}

	// 根据回复id，取得一个回复
	public BottleReply getReplyById(int id) {
		BottleReply br = new BottleReply();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from bottle_viewer where id="
				+ id);
		try {
			if (rs.next()) {
				br.setId(rs.getInt("id"));
				br.setBottleId(rs.getInt("bottle_id"));
				br.setUserId(rs.getInt("user_id"));
				br.setViewTime(rs.getTimestamp("view_time").getTime());
				br.setReply(rs.getString("reply"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return br;
	}

	// 根据用户id，取得所放的瓶子的总数
	public int getMyBottleCount(int uid) {
		int result = 0;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select count(id) idCount from bottle_info where user_id="
						+ uid);
		try {
			if (rs.next()) {
				result = rs.getInt("idCount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}

	// 根据用户id，取得其最后一个瓶子的最后浏览时间
	public long getLastBottlePickupTime(int uid) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select last_pickup_time from bottle_info where user_id="
						+ uid + " order by id desc limit 1");
		long result = 0l;
		try {
			if (rs.next()) {
				result = rs.getTimestamp("last_pickup_time").getTime();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}

	// 取机取得5个瓶子
	public ArrayList getRandomBottles(int randomBound) {
		BottleBean bb;

		DbOperation db = new DbOperation(5);
		ArrayList al = new ArrayList();
		ResultSet rs = db
				.executeQuery("select id from bottle_info order by id limit "
						+ RandomUtil.nextInt(randomBound) + ",5");
		try {
			while (rs.next()) {
				bb = new BottleBean();
				// 根据UserID，取得它回复过的瓶子的ID，再根据这个ID,来取得一个瓶子。
				bb = this.getBottleById(rs.getInt("id"));
				al.add(bb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return al;
	}

	// 根据id,删除一个瓶子
	public boolean deleteBottle(int id) {
		DbOperation db = new DbOperation(5);
		boolean result = db.executeUpdate("delete from bottle_info where id="
				+ id);
		db.release();
		return result;
	}

	// 根据id,删除一个留言
	public boolean deleteReply(int id) {
		DbOperation db = new DbOperation(5);
		boolean result = db.executeUpdate("delete from bottle_viewer where id="
				+ id);
		db.release();
		return result;
	}

	// 回复次数-1
	public boolean decreaseReplyCount(int id) {
		DbOperation db = new DbOperation(5);
		boolean result = db
				.executeUpdate("update bottle_info set reply_count=reply_count - 1 where id="
						+ id);
		db.release();
		return result;
	}

	// 根据瓶子id，删除其所有留言
	public boolean deleteAllReplyByBottleId(int id) {
		DbOperation db = new DbOperation(5);
		boolean result = db
				.executeUpdate("delete from bottle_viewer where bottle_id="
						+ id);
		db.release();
		return result;
	}
}
