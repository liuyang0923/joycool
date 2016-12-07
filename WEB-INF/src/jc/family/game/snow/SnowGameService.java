package jc.family.game.snow;


import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jc.family.FamilyUserBean;
import jc.family.game.GameService;
import jc.family.game.MemberBean;
import net.joycool.wap.util.db.DbOperation;

public class SnowGameService extends GameService {

	/**
	 * 返回一个int型的属性
	 * 
	 * @param sql
	 * @return
	 */
	public int selectIntResult(String sql) {
		DbOperation db = new DbOperation(5);
		try {
			int c = db.getIntResult(sql);
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return 0;
	}

	/**
	 * 用户雪币修改及插入
	 * 
	 * @param money
	 * @param uid
	 * @return
	 */
	public boolean updateSnowMoney(int money, int uid) {
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_game_snow_money(uid, money)values("
				+ uid + "," + money + ") on duplicate key update money="
				+ money;
		boolean update = db.executeUpdate(query);
		db.release();
		return update;
	}
	
	// 修改用户最后雪币数
	public boolean setSnowMoney(int money, int uid) {
		DbOperation db = new DbOperation(5);
		String query = "update  fm_game_snow_money set money="+ money+" where uid="+uid;
		boolean update = db.executeUpdate(query);
		db.release();
		return update;
	}

	/**
	 * 根据赛事id，按积分和参赛人数排列参赛家族
	 * 
	 * @param mid
	 * @return
	 */
	public List selectFmByScore(int mid) {// 得到参加雪仗游戏的家族的id列表，按积分和参加人数排列
		DbOperation db = new DbOperation(5);
		String query = "select a.fid from fm_game_fmapply a left outer join fm_game_score b on a.fid=b.fmid where a.m_id="
				+ mid + " and a.total_apply>0  order by ifnull(b.snow_score,0) desc,a.total_apply desc";
		List list = new ArrayList();
		ResultSet rs = db.executeQuery(query);
		try {
			if(rs==null){
				return null;
			}
			while (rs.next()) {
				Integer fid = Integer.valueOf(rs.getInt(1));
				list.add(fid);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}
	
	public SnowBean selectOneMatch(int fid, int mid) {// 取到赛事统计信息
		DbOperation db = new DbOperation(5);
		String query = "select spend_time, prize, num_total, rank, snow_score,hold_time,fid2,game_point  from fm_game_game where fid1="
				+ fid + " and m_id=" + mid + " and game_type=2";
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				SnowBean bean = new SnowBean();
				bean.setFid1(fid);
				bean.setMid(mid);
				bean.setSpendTime(rs.getLong(1));
				bean.setPrize(rs.getLong(2));
				bean.setNumTotal(rs.getInt(3));
				bean.setRank(rs.getInt(4));
				bean.setScore(rs.getInt(5));
				bean.setHoldTime(rs.getDate(6).toString());
				bean.setFid2(rs.getInt(7));
				bean.setGamePoint(rs.getInt(8));
				return bean;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	// 比赛对抗列表,用于展示某期比赛，对抗家族的胜负情况
	public List selectGameList(int mid) {
		DbOperation db = new DbOperation(5);
		String query = "select fid1, fid2,rank from fm_game_game where game_type=2 and m_id="
				+ mid
				+ " and (rank=1 or rank=3) order by id asc";
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				SnowBean bean = new SnowBean();
				bean.setFid1(rs.getInt(1));
				bean.setFid2(rs.getInt(2));
				bean.setRank(rs.getInt(3));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	// 取到某个家族参加的各期比赛情况列表
	public List selectFmList(int fid, int getStartIndex, int countPerPage) {
		DbOperation db = new DbOperation(5);
		String query = "select fid1, fid2, rank,hold_time,m_id from fm_game_game where game_type=2 and fid1="
				+ fid
				+ " order by id desc limit "
				+ getStartIndex
				+ ","
				+ countPerPage;
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				SnowBean bean = new SnowBean();
				bean.setFid1(rs.getInt(1));
				bean.setFid2(rs.getInt(2));
				bean.setRank(rs.getInt(3));
				bean.setHoldTime(rs.getDate(4).toString());
				bean.setMid(rs.getInt(5));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	// 查出命中次数、扫雪的花费、做雪球花费的金额
	public List selectGameData(String cmd, int mid, int fid, int getStartIndex,
			int countPerPage) {
		DbOperation db = new DbOperation(5);
		String query = "select  uid,  "
				+ cmd
				+ " from fm_game_member  where m_id="
				+ mid + "  and fid=" + fid + " order by " + cmd
				+ " desc limit " + getStartIndex + "," + countPerPage;
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				MemberBean bean = new MemberBean();
				bean.setUid(rs.getInt(1));
				bean.setMid(mid);
				bean.setFid(fid);
				if (cmd.equals("total_hit")) {
					bean.setTotalHit(rs.getInt(2));
				} else if (cmd.equals("pay_sweep")) {
					bean.setPaySweep(rs.getInt(2));
				} else if (cmd.equals("pay_make")) {
					bean.setPayMake(rs.getInt(2));
				}
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 随机出题
	 * 
	 * @return
	 */
	public SnowQuestionBean selectQuestion() {
		DbOperation db = new DbOperation(5);
		String query = "select t1.id, t1.question, t1.answer from fm_game_snow_question as t1 join (select round(rand() * ((select max(id) from fm_game_snow_question)-(select min(id) from fm_game_snow_question))+(select min(id) from fm_game_snow_question)) as id) as t2 where t1.id >= t2.id order by t1.id limit 1";
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				SnowQuestionBean bean = new SnowQuestionBean();
				bean.setId(rs.getInt(1));
				bean.setQuestion(rs.getString(2));
				bean.setAnswer(rs.getInt(3));
				return bean;
			}
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
		return null;
	}

	// 查出一个问题的答案
	public SnowQuestionBean selectQuestionById(int id) {
		DbOperation db = new DbOperation(5);
		String query = "select answer from fm_game_snow_question where id="
				+ id;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				SnowQuestionBean bean = new SnowQuestionBean();
				bean.setAnswer(rs.getInt(1));
				return bean;
			}
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
		return null;
	}

	// 查出报名的人的列表
	public List selectApplyMembers(String cond) {
		DbOperation db = new DbOperation(5);
		String query = "select uid,fid from fm_game_apply where state=2 and " + cond;
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				MemberBean bean = new MemberBean();
				int uid = rs.getInt(1);
				bean.setUid(uid);
				bean.setFid(rs.getInt(2));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	// 返回一个类型的道具
	public SnowGameToolTypeBean selectToolType(int tid) {
		DbOperation db = new DbOperation(5);
		String query = "select id, t_name, use_time, action_type, spend_time, snow_effect, spend_money from fm_game_snow_tools where id="
				+ tid;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				SnowGameToolTypeBean bean = new SnowGameToolTypeBean();
				bean.setId(rs.getInt(1));
				bean.settName(rs.getString(2));
				bean.setUseTime(rs.getInt(3));
				bean.setActionType(rs.getInt(4));
				bean.setSpendTime(rs.getInt(5));
				bean.setSnowEffect(rs.getInt(6));
				bean.setSpendMoney(rs.getInt(7));
				return bean;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
		return null;
	}

	/**
	 * 查询用户扣钱没有
	 * 
	 * @param mid
	 * @param uid
	 * @return
	 */
	public boolean updatePay(int mid, int uid) {
		DbOperation db = new DbOperation(5);
		String query = "update fm_game_apply set is_Pay=1 where m_id=" + mid
				+ " and uid=" + uid;
		boolean update = db.executeUpdate(query);
		db.release();
		return update;
	}



	// 得到家族用户
	public FamilyUserBean selectfmUser(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select fm_id,gift_fm,con_fm,fm_name,fm_money_used,leave_fm_time,fm_state,fm_flags from fm_user where id="
						+ id);
		try {
			if (rs.next()) {
				FamilyUserBean fmuser = new FamilyUserBean();
				fmuser.setId(id);
				fmuser.setFm_id(rs.getInt(1));
				fmuser.setGift_fm(rs.getLong(2));
				fmuser.setCon_fm(rs.getInt(3));
				fmuser.setFm_name(rs.getString(4));
				fmuser.setFm_money_used(rs.getLong(5));
				fmuser.setLeave_fm_time(rs.getTimestamp(6));
				fmuser.setFm_state(rs.getInt(7));
				fmuser.setFm_flags(rs.getInt(8));
				return fmuser;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	// 判断家族是否参赛
	public boolean isAttend(int mid, int fid) {
		DbOperation db = new DbOperation(5);
		String query = "select count(id) from fm_game_apply where is_pay=1 and fid="
				+ fid + " and m_id=" + mid;// 成功进入游戏才会付费1代表付费,即有人参加了比赛
		try {
			int rs = db.getIntResult(query);
			if (rs > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
		return false;
	}
	
	/**
	 * 存入家族赛事
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertFmGame(SnowBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_game(m_id,fid1,fid2,num_total,rank,game_type,spend_time,prize,hold_time,snow_score,game_point) VALUES(?,?,?,?,?,?,?,?,now(),?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getMid());
			pstmt.setInt(2, bean.getFid1());
			pstmt.setInt(3, bean.getFid2());
			pstmt.setInt(4, bean.getNumTotal());
			pstmt.setInt(5, bean.getRank());
			pstmt.setInt(6, bean.getType());
			pstmt.setLong(7, bean.getSpendTime());
			pstmt.setLong(8, bean.getPrize());
			pstmt.setInt(9, bean.getScore());
			pstmt.setInt(10, bean.getGamePoint());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		return true;
	}

	public String getStartTime() {
		DbOperation db = new DbOperation(5);
		String query = "select start_hour, start_min effect from fm_game_weekgame ";
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				int hour = rs.getInt(1);
				int min = rs.getInt(2);
				String time =hour+"";
				if (hour < 10) {
					time = "0" + time+":";
				}else{
					time=hour+":";
				}
				if (min < 10) {
					time = time + "0";
				}
				time = time + min + ":00";
				return time;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "08:30";
		} finally {
			db.release();
		}
		return "08:30";
	}
}