package jc.friend.stranger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class ChatService {

	/**
	 * 聊天库
	 * 
	 * @param roomId
	 * @return
	 */
	// room_id='"+ StringUtil.toSql(roomId)+ "' and isshow=0 order by create_time desc
	public List getChatList(String coud) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from chats where " + coud);
		try {
			while (rs.next())
				list.add(getChatBean(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 
	 * @param coud
	 * @return
	 */
	// user_id="+ uid
	public ChtBean getChater(String coud) {
		ChtBean ctb = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from chat where " + coud);
		try {
			while (rs.next()) {
				ctb = getChtBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return ctb;
	}

	public boolean update(String sql) {
		DbOperation db = new DbOperation(5);
		boolean succ = db.executeUpdate(sql);
		db.release();
		return succ;
	}

	ChatBean getChatBean(ResultSet rs) throws SQLException {
		ChatBean cb = new ChatBean();
		cb.setId(rs.getInt("id"));
		cb.setRoomId(rs.getString("room_id"));
		cb.setFrom(rs.getInt("from_id"));
		cb.setTo(rs.getInt("to_id"));
		cb.setShow(rs.getInt("isshow"));
		cb.setCont(rs.getString("cont"));
		cb.setCreTime(rs.getDate("create_time").getTime());
		return cb;
	}

	ChtBean getChtBean(ResultSet rs) throws SQLException {
		ChtBean ctb = new ChtBean();
		ctb.setId(rs.getInt("id"));
		ctb.setRoomId(rs.getString("room_id"));
		ctb.setStrgid(rs.getInt("stranger_id"));
		ctb.setUid(rs.getInt("user_id"));
		return ctb;
	}

}
