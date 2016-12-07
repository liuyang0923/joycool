package net.joycool.wap.spec.buyfriends;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.joycool.wap.util.db.DbOperation;

public class DAOInvitation {

	/**
	 * 记录邀请用户
	 * @param userId
	 * @param inviteUserId
	 */
	public void addInvite(int userId, int inviteUserId) {
		DbOperation db = new DbOperation();
		db.init("mcoolwap");
		String query = "INSERT INTO jc_room_invite(user_id, mobile, name, content, send_datetime, mark, send_mark, success_mark, new_user_mark, invitee_id) values(?,?,?,?,now(),1,1,1,1,?)";
		if(!db.prepareStatement(query)) {
			
			db.release();
			return;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, userId);
			pstmt.setString(2, "mobile");
			pstmt.setString(3, "name");
			pstmt.setString(4, "content");
			pstmt.setInt(5, inviteUserId);
			pstmt.execute();
			
		}catch(SQLException e) {
			e.printStackTrace();
			return;
		}
		db.release();
		return;
		
	}
	
}
