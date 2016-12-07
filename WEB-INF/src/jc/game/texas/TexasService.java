package jc.game.texas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class TexasService {

	public TexasUserBean getTexasUserBean(ResultSet rs) throws SQLException {
		TexasUserBean bean = new TexasUserBean();
		bean.setUserId(rs.getInt("user_id"));
		bean.setMoney(rs.getInt("money"));
		bean.setMoney2(rs.getInt("money2"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setPrizeCount(rs.getInt("prize_count"));
		bean.setMoneyTime(rs.getTimestamp("money_time").getTime());
		return bean;
	}
	
	public TexasUserBean getTexasUserBean(String cond) {
		TexasUserBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from texas_user where " + cond);
		try{
			if(rs.next())
				bean = getTexasUserBean(rs);
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	public boolean addTexasUserBean(TexasUserBean bean) {
		DbOperation dbOp = new DbOperation(5);
		
		String query = "INSERT INTO texas_user(user_id,money,money2,create_time) VALUES(?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getMoney());
			pstmt.setInt(3, bean.getMoney2());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		} finally {
			dbOp.release();
		}
		return true;
	}
	
	public TexasGame getTexasRecord(ResultSet rs) throws SQLException {
		TexasGame bean = new TexasGame();
		bean.setId(rs.getInt("id"));
		bean.setBoardId(rs.getInt("board_id"));
		bean.setMaxUser(rs.getInt("max_user"));
		bean.setWager(rs.getInt("wager"));
		bean.setDealer(rs.getInt("dealer"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setGameUserCount(rs.getInt("user_count"));
		bean.setCards(s2c(rs.getString("cards"), 5));
		return bean;
	}
	
	public TexasGame getTexasRecord(String cond) {
		TexasGame bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from texas_record where " + cond);
		try{
			if(rs.next())
				bean = getTexasRecord(rs);
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getTexasRecordList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from texas_record where " + cond);
		try{
			while(rs.next())
				list.add(getTexasRecord(rs));
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public TexasUser getTexasRecordUser(ResultSet rs) throws SQLException {
		TexasUser bean = new TexasUser();
		bean.setUserId(rs.getInt("user_id"));
		bean.setSeat(rs.getInt("seat"));
		bean.getUserCards()[0] = rs.getInt("card1");
		bean.getUserCards()[1] = rs.getInt("card2");
		bean.setType(rs.getInt("type"));
		bean.setStatus(rs.getInt("status"));
		bean.setScore(rs.getInt("score"));
		bean.setFinalCards(s2c(rs.getString("final_cards"), 5));
		bean.setWager(rs.getInt("wager"));
		bean.setWinWager(rs.getInt("win_wager"));
		bean.setMoney(rs.getInt("money"));
		return bean;
	}
	
	public void getTexasRecordUsers(TexasGame game) {
		TexasUser[] users = new TexasUser[game.getMaxUser()];
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from texas_record_user where record_id=" + game.getId());
		try{
			while(rs.next()) {
				TexasUser user = getTexasRecordUser(rs);
				users[user.getSeat()] = user;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		game.setUsers(users);
		game.load();
	}

	public boolean addTexasRecord(TexasGame bean) {
		DbOperation dbOp = new DbOperation(5);
		
		String query = "INSERT INTO texas_record(board_id,user_count,wager,dealer,cards,max_user,create_time,end_time) VALUES(?,?,?,?,?,?,?,now())";
		String query2 = "INSERT INTO texas_record_user(seat,user_id,card1,card2,record_id,type,status,score,final_cards,wager,win_wager,money) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getBoardId());
			pstmt.setInt(2, bean.getGameUserCount());
			pstmt.setInt(3, bean.getWager());
			pstmt.setInt(4, bean.getDealer());
			pstmt.setString(5, c2s(bean.getCards(), 5));
			pstmt.setInt(6, bean.getMaxUser());
			pstmt.setTimestamp(7, new Timestamp(bean.getCreateTime()));
			
			
			pstmt.execute();
			pstmt.close();
			int recordId = dbOp.getLastInsertId();
			if (dbOp.prepareStatement(query2)) {
				pstmt = dbOp.getPStmt();
				List users = bean.getRoundUsers();
				for(int i = 0;i < users.size();i++) {
					TexasUser user = (TexasUser)users.get(i);
					
					pstmt.setInt(1, user.getSeat());
					pstmt.setInt(2, user.getUserId());
					pstmt.setInt(3, user.getUserCards()[0]);
					pstmt.setInt(4, user.getUserCards()[1]);
					pstmt.setInt(5, recordId);
					pstmt.setInt(6, user.getType());
					pstmt.setInt(7, user.getStatus());
					pstmt.setInt(8, user.getScore());
					pstmt.setString(9, c2s(user.getFinalCards(), 5));
					pstmt.setInt(10, user.getWager());
					pstmt.setInt(11, user.getWinWager());
					pstmt.setInt(12, user.getMoney());
					pstmt.execute();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		} finally {
			dbOp.release();
		}
		return true;
	}
	
	// 工具函数，数据库字符串和int[]的互相转换
	public static int[] s2c(String c, int count) {
		int[] c2 = new int[count];
		if(c != null) {
			String[] s = c.split(",");
			for(int i = 0;i < s.length;i++) {
				c2[i] = StringUtil.toInt(s[i]);;
			}
		}
		return c2;
	}
	public static String c2s(int[] cards, int count) {
		if(cards == null)
			return "";
		StringBuilder sb = new StringBuilder(15);
		for(int i = 0;i < count;i++) {
			if(i != 0)
				sb.append(',');
			sb.append(cards[i]);
		}
		return sb.toString();
	}
}