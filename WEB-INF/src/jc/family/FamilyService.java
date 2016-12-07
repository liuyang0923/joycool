package jc.family;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

public class FamilyService {

	/**
	 * 得到用户
	 * 
	 * @param id
	 */
	public FamilyUserBean selectfmUser(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from fm_user where id="
						+ id);
		try {
			if (rs.next()) {
				return getFmUser(rs);
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}
	
	FamilyUserBean getFmUser(ResultSet rs) throws SQLException {
		FamilyUserBean fmuser = new FamilyUserBean();
		fmuser.setId(rs.getInt("id"));
		fmuser.setFm_id(rs.getInt("fm_id"));
		fmuser.setGift_fm(rs.getLong("gift_fm"));
		fmuser.setCon_fm(rs.getInt("con_fm"));
		fmuser.setFm_name(rs.getString("fm_name"));
		fmuser.setFm_money_used(rs.getLong("fm_money_used"));
		fmuser.setLeave_fm_time(rs.getTimestamp("leave_fm_time"));
		fmuser.setFm_state(rs.getInt("fm_state"));
		fmuser.setFm_flags(rs.getInt("fm_flags"));
		fmuser.setCreate_time(rs.getTimestamp("create_time"));
		fmuser.setAlive(rs.getInt("alive"));
		fmuser.setVisitTime(rs.getTimestamp("visit_time"));
		fmuser.setHonor(rs.getInt("honor"));
		fmuser.setSetting(rs.getInt("setting"));
		return fmuser;
	}

	/**
	 * 得到用户的ID
	 * 
	 * @param id
	 *            家族ID
	 * @param query
	 *            查询条件
	 * @return
	 */
	public List selectUserIdList(int id, String query) {
		DbOperation db = new DbOperation(5);
		try {
			return db.getIntList("select id from fm_user where fm_id=" + id + query);
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 得到用户的ID
	 * 
	 * @param id
	 *            家族ID
	 * @param query
	 *            查询条件
	 * @return
	 */
	public List selectFamilyIdList(String cond) {
		DbOperation db = new DbOperation(5);
		try {
			return db.getIntList("select id from fm_home where " + cond);
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 根据家族id连表查询活动积分
	 * 
	 * @param cond
	 * @return
	 */
	public List selectFmScoreList(String cond) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		ResultSet rs = db
				.executeQuery("select id,ask_score,boat_score,snow_score from fm_home a left join fm_game_score b on a.id=b.fmid where "
						+ cond);
		try {
			while (rs.next()) {
				list.add(getFmScore(rs));
			}
			return list;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 根据家族id连表查询活动积分
	 * 
	 * @param cond
	 * @return
	 */
	public FmScore selectFmScore(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id,ask_score,boat_score,snow_score from fm_home a left join fm_game_score b on a.id=b.fmid where "
						+ cond);
		try {
			if (rs.next()) {
				return getFmScore(rs);
			}
			return null;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 根据家族id连表查询挑战积分
	 * 
	 * @param cond
	 * @return
	 */
	public List selectFmVSScoreList(String cond) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		ResultSet rs = db.executeQuery("select id,score from fm_home a left join fm_vs_score b on a.id=b.fm_id and "
				+ cond);
		try {
			while (rs.next()) {
				list.add(getFmVsScore(rs));
			}
			return list;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 家族公告设置
	 * 
	 * @param id
	 * @return
	 */
	public boolean updateBulletin(int id, String bulletin, String name) {
		DbOperation db = new DbOperation(5);
		String query = "update fm_home set  bulletin=? where id=" + id;
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, bulletin);
			boolean result = db.executePstmt();
			if (!result) {
				return false;
			}
			// 历史
			String content = name + "将家族公告修改为:" + bulletin;
			query = "insert into fm_history (fm_id, event, event_time, fm_state)values(" + id + ",?,now(),5)";// 插入家族历史表
			if (!db.prepareStatement(query)) {
				return false;
			}
			// 传递参数
			pstmt = db.getPStmt();
			pstmt.setString(1, content);
			return db.executePstmt();
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 设置家族的名字
	 * 
	 * @param fmid
	 * @param name
	 * @return
	 */
	public boolean updateFmName(int userid, int fmid, String name, String username, String oldname) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();
		String query = "update fm_home set fm_name=? where id=" + fmid;
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		boolean result = false;
		// 传递参数
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, name);
			result = db.executePstmt();

			// 历史
			String content = username + "将家族更名为" + name;
			query = "insert into fm_history (fm_id, event, event_time, fm_state)values(" + fmid + ",?,now(),5)";// 插入家族历史表
			if (!db.prepareStatement(query)) {
				return false;
			}
			// 传递参数
			pstmt = db.getPStmt();
			pstmt.setString(1, content);
			result = db.executePstmt();
			if (!result) {
				return false;
			}

			// 动态
			query = "insert into fm_movement(userid,username,fm_id,fm_name,movement_time,fm_state)values(" + userid
					+ ",?," + fmid + ",?,now(),3)";
			// 准备
			if (!db.prepareStatement(query)) {
				return false;
			}
			// 传递参数
			pstmt = db.getPStmt();
			pstmt.setString(1, name);
			pstmt.setString(2, oldname);
			result = db.executePstmt();
			if (!result) {// 添加动态信息
				return false;
			}

			// 基金使用表里插入记录
			query = "insert into fm_fund_history(fm_id, userid, username, event, event_time,fm_state)values(?,?,?,?,now(),?)";
			if (!db.prepareStatement(query)) {
				return result;
			}
			pstmt = db.getPStmt();
			pstmt.setInt(1, fmid);
			pstmt.setInt(2, userid);
			pstmt.setString(3, username);
			pstmt.setString(4, "家族易帜扣除" + Constants.FM_RENAME);
			pstmt.setInt(5, 9);// 9改名易帜
			result = db.executePstmt();
			if (result) {
				db.commitTransaction();
			}
			return result;
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 增加通知
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertNotice(FamilyNoticeBean bean, long money, int id) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();
		String query = "insert into fm_notice (fm_id, userid, username, notice_content, notice_time) values(?,?,?,?,?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, bean.getFmid());
			pstmt.setInt(2, bean.getUserid());
			pstmt.setString(3, bean.getUsername());
			pstmt.setString(4, bean.getContent());
			pstmt.setTimestamp(5, new Timestamp(bean.getNoticetime().getTime()));
			boolean result = db.executePstmt();
			if (!result) {
				return false;
			}
			result = db.executeUpdate("update fm_home set money=money-" + money + " where id=" + id);
			if (!result) {
				return false;
			}
			// 基金使用表里插入记录
			query = "insert into fm_fund_history(fm_id, userid, username, event, event_time,fm_state)values(?,?,?,?,now(),?)";
			if (!db.prepareStatement(query)) {
				return result;
			}
			pstmt = db.getPStmt();
			pstmt.setInt(1, id);
			pstmt.setInt(2, bean.getUserid());
			pstmt.setString(3, bean.getUsername());
			pstmt.setString(4, "发通知扣除" + money);
			pstmt.setInt(5, 8);// 发通知扣钱,进入基金使用记录表里,状态为9
			result = db.executePstmt();
			if (result) {
				db.commitTransaction();
			}
			return result;
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 得到通知的列表
	 * 
	 * @param id
	 * @return
	 */
	public List selectNotice(int id, int getStartIndex, int countPerPage) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_notice where fm_id=" + id + " order by id desc limit "
				+ getStartIndex + "," + countPerPage);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				FamilyNoticeBean bean = new FamilyNoticeBean();
				bean.setId(rs.getInt("id"));
				bean.setFmid(rs.getInt("fm_id"));
				bean.setUserid(rs.getInt("userid"));
				bean.setUsername(rs.getString("username"));
				bean.setContent(rs.getString("notice_content"));
				bean.setNoticetime(rs.getTimestamp("notice_time"));
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
	 * 返回查询出来的总页数
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
	 * 添加新家族，更新族长信息
	 * 
	 * @param userid
	 * @param name
	 * @param fname
	 * @return
	 */
	public FamilyHomeBean insertfmHome(int fm_apply_id, int userid, String name, String fname) {
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_home(fm_name,fm_time,create_id,fm_member_num,leader_id,leader_name)values(?,now(),"
				+ userid + ",1," + userid + ",?)";
		// 准备
		if (!db.prepareStatement(query)) {
			db.release();
			return null;
		}
		// 传递参数
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, fname);
			pstmt.setString(2, name);
			boolean result = db.executePstmt();
			if (!result) {// 创建家族失败
				return null;
			}

			int fmid = db.getLastInsertId();// 得到最后的id;

			FamilyHomeBean fmHome = new FamilyHomeBean();
			fmHome.setFm_time(new Date());
			fmHome.setId(fmid);
			fmHome.setCreate_id(userid);
			fmHome.setLeader_id(userid);
			fmHome.setFm_member_num(1);
			fmHome.setFm_name(fname);
			fmHome.setLeader_name(name);
			fmHome.setLogoUrl("");
			fmHome.setFm_level(1);

			query = "insert into fm_user(id,fm_id,fm_name,fm_flags,create_time)values(" + userid + "," + fmid
					+ ",'族长'," + FamilyUserBean.allflages() + ",now()) on duplicate key update fm_id=" + fmid
					+ ",fm_name='族长',fm_flags=" + FamilyUserBean.allflages() + ",create_time=now()";
			result = db.executeUpdate(query);// 更新用户信息，如果不存在则插入
			if (!result) {// 创建用户失败
				return null;
			}

			// 查找fm_apply,把同意加入家族的取出来加入家族。删除原数据
			ResultSet rs = db
					.executeQuery("select id,userid,apply_time,is_apply from fm_apply_user where is_apply=1 and fm_apply_id="
							+ fm_apply_id + " group by 2");
			List list = new ArrayList();
			while (rs.next()) {
				FmApplyUser bean = new FmApplyUser();
				bean.setId(rs.getInt(1));
				bean.setUserid(rs.getInt(2));
				bean.setApply_time(rs.getTimestamp(3).getTime());
				bean.setIs_apply(rs.getInt(4));
				list.add(bean);
			}
			result = db.executeUpdate("delete from fm_apply_user where fm_apply_id=" + fm_apply_id);// 删除邀请表中信息
			if (!result) {
				return null;
			}

			int updatenumber = 1;
			for (int i = 0; i < list.size() && updatenumber < Constants.FM_LEVEL[1]; i++) {// 如果人数大于等于1级要求数
				FmApplyUser bean = (FmApplyUser) list.get(i);
				if (bean.getUserid() == userid) {
					continue;
				}
				UserStatusBean userStatus = UserInfoUtil.getUserStatus(bean.getUserid());
				if (userStatus == null || userStatus.getTong() != 0) {
					continue;
				}
				query = "insert into fm_user(id,fm_id,fm_name,create_time)values(" + bean.getUserid() + "," + fmid
						+ ",'创始人',now()) on duplicate key update create_time=now(),fm_name='创始人',fm_id=" + fmid;
				result = db.executeUpdate(query);// 更新用户信息，如果不存在则插入
				if (!result) {// 创建用户失败
					continue;
				}

				result = db.executeUpdate("update fm_apply_user set is_apply=2 where userid=" + bean.getUserid());// 拒绝其它家族的邀请
				if (!result) {
					continue;
				}
				updatenumber++;
			}
			result = db.executeUpdate("update fm_home set fm_member_num=" + updatenumber + " where id=" + fmid);// 家族总数加updatenumber
			if (!result) {
				return null;
			}
			result = db.executeUpdate("delete from fm_apply where id=" + fm_apply_id);// 家族总数加
			if (!result) {
				return null;
			}
			fmHome.setFm_member_num(updatenumber);
			return fmHome;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 得到家族信息
	 * 
	 * @param string
	 * @return
	 */
	public FamilyHomeBean selectFamily(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select fm_name,fm_member_num,fm_level,fm_time,create_id,leader_id,leader_name,leader_leave,invite_number,fm_exploit,money,limit_money,bulletin,game_num,forumId,logo_url,uv_self,uv,uv_self_yes,uv_yes,info,ally_count,ally_count2,flag,setting,short_name,chat_top,chat_open,max_member from fm_home where id="
						+ id);
		try {
			if (rs.next()) {
				FamilyHomeBean bean = new FamilyHomeBean();
				bean.setId(id);
				bean.setFm_name(rs.getString(1));
				bean.setFm_member_num(rs.getInt(2));
				bean.setFm_level(rs.getInt(3));
				bean.setFm_time(rs.getTimestamp(4));
				bean.setCreate_id(rs.getInt(5));
				bean.setLeader_id(rs.getInt(6));
				bean.setLeader_name(rs.getString(7));
				bean.setLeader_leave(rs.getTimestamp(8));
				bean.setInvite_number(rs.getInt(9));
				bean.setFm_exploit(rs.getInt(10));
				bean.setMoney(rs.getLong(11));
				bean.setLimit_money(rs.getLong(12));
				bean.setBulletin(rs.getString(13));
				bean.setGame_num(rs.getInt(14));
				bean.setForumId(rs.getInt(15));
				bean.setLogoUrl(rs.getString(16));
				bean.setUvSelf(rs.getInt(17));
				bean.setUv(rs.getInt(18));
				bean.setUvSelfYes(rs.getInt(19));
				bean.setUvYes(rs.getInt(20));
				bean.setInfo(rs.getString(21));
				bean.setAllyCount(rs.getInt(22));
				bean.setAllyCount2(rs.getInt(23));
				bean.setFlag(rs.getInt(24));
				bean.setSetting(rs.getInt(25));
				
				bean.setShortName(rs.getString(26));
				bean.setChatTop(rs.getString(27));
				bean.setChatOpen(rs.getInt(28));
				bean.setMaxMember(rs.getInt(29));
				return bean;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 得到家族用户List
	 * 
	 * @return
	 */
	public List selectFmUserList(int id, int getStartIndex, int countPerPage, String order) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from fm_user where fm_id="
						+ id + order + " limit " + getStartIndex + "," + countPerPage);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				list.add(getFmUser(rs));
			}
			return list;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 得到家族所有用户
	 * 
	 * @param id
	 * @return
	 */
	public List selectFmOnLineUserList(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id,fm_name from fm_user where fm_id=" + id + " order by fm_flags desc");
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyUserBean bean = new FamilyUserBean();
				bean.setId(rs.getInt(1));
				bean.setFm_name(rs.getString(2));
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
	 * 有多少请求加入新人
	 * 
	 * @return
	 */
	public int selectisFmNewMan(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select count(id) from fm_join where fm_id=" + id);
		try {
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			return 0;
		} finally {
			db.release();
		}
	}

	/**
	 * 删除一个家族
	 * 
	 * @param fmid
	 * @return
	 */
	public boolean deleteFmByID(int fmid, int userid, String name, String fm_name) {
		DbOperation db = new DbOperation(5);
		String query = "delete from fm_home where id=" + fmid;
		boolean result = db.executeUpdate(query);
		if (!result) {// 删除失败
			db.release();
			return false;
		}
		// 动态
		query = "insert into fm_movement(userid,username,fm_id,fm_name,movement_time,fm_state)values(" + userid + ",?,"
				+ fmid + ",?,now(),1)";
		// 准备
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, name);
			pstmt.setString(2, fm_name);
			result = db.executePstmt();
			if (!result) {// 添加动态信息
				return false;
			}
			// 历史
			result = db.executeUpdate("delete from fm_history where fm_id=" + fmid);
			if (!result) {// 添加动态信息
				return false;
			}
			result = db.executeUpdate("update fm_user set leave_fm_time=now() where fm_id=" + fmid + " and id="
					+ userid);
			if (!result) {// 修改族长离开族的时间
				return false;
			}
			result = db
					.executeUpdate("update fm_user set fm_id=0,gift_fm=0,con_fm=0,fm_name='',fm_money_used=0,fm_state=0,fm_flags=0 where fm_id="
							+ fmid);
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_user_exploit_history where fmid=" + fmid);// 删除个人功勋记录
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_home_exploit_history where fm_id=" + fmid);// 删除家族功勋记录
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_fund_history where fm_id=" + fmid);// 删除家族基金记录
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_game_game where fid1=" + fmid);// 删除家族游戏记录
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_game_member where fid=" + fmid);// 删除家族个人游戏记录
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_game_score where fmid=" + fmid);// 删除家族积分
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_invite where fm_id=" + fmid);// 删除家族邀请
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_join where fm_id=" + fmid);// 删除家族申请
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_notice where fm_id=" + fmid);// 删除家族通知
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_shaikh where fm_id=" + fmid);// 删除家族让位
			return result;
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 开除
	 * 
	 * @param fmid
	 * @param userid
	 * @return
	 */
	public boolean updatetoFireOut(int fmid, int userid) {
		DbOperation db = new DbOperation(5);
		String query = "update fm_user set fm_id=0,gift_fm=0,con_fm=0,fm_name='',fm_money_used=0,fm_state=0,fm_flags=0 where id="
				+ userid;
		boolean rs = db.executeUpdate(query);
		if (!rs) {
			db.release();
			return false;
		}
		rs = db.executeUpdate("update fm_home set fm_member_num=fm_member_num-1 where id=" + fmid);// 家族成员数据减一
		if (!rs) {
			db.release();
			return false;
		}
		rs = db.executeUpdate("delete from fm_user_exploit_history where userid=" + userid);// 删除个人功勋记录
		db.release();
		return rs;
	}

	/**
	 * 捐钱
	 * 
	 * @param id
	 * @param userid
	 * @param fund
	 *            (累加值)
	 * @return
	 */
	public boolean updateFundGive(int id, int userid, int fund) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();
		// 去掉捐款增加贡献度
		// int give = fund / 1000000;// 每捐款100万增加一点贡献
		boolean rs = db.executeUpdate("update fm_user set  gift_fm=gift_fm+" + fund + " where id=" + userid);
		if (!rs) {
			return false;
		}
		rs = db.executeUpdate("update fm_home set  money=(money+" + fund + ")  where id=" + id);
		db.commitTransaction();
		db.release();
		return rs;
	}

	/**
	 * 提取基金
	 * 
	 * @param id
	 * @param userid
	 * @param fund
	 * 
	 * @return
	 */
	public boolean updateFundUse(int id, int userid, int fund, FamilyFundBean bean) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();

		boolean result = false;
		// 基金使用表里插入记录
		String query = "insert into fm_fund_history(fm_id, userid, username, event, event_time,fm_state)values(?,?,?,?,now(),?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return result;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, bean.getFm_id());
			pstmt.setInt(2, bean.getUserid());
			pstmt.setString(3, bean.getUsername());
			pstmt.setString(4, bean.getEvent());
			pstmt.setInt(5, bean.getFm_State());

			result = db.executePstmt();
			if (!result) {
				return result;
			}
			// 修改用户表里该用户提款额度
			result = db.executeUpdate("update fm_user set  fm_money_used=(fm_money_used+" + fund + ")  where id="
					+ userid);
			if (!result) {
				return result;
			}
			// 修改家族表里家族基金总额
			result = db.executeUpdate("update fm_home set  money=(money-" + fund + ")  where id=" + id);
			if (!result) {
				return result;
			}
			db.commitTransaction();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 查询家族基金使用记录
	 * 
	 * @param id
	 * @param startindex
	 * @param endindex
	 * @param query
	 * @return
	 */
	public List selectFundUseList(int id, int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id,fm_id, userid, username, event, event_time ,fm_state from fm_fund_history where fm_id="
						+ id + " order by id desc limit " + startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyFundBean bean = new FamilyFundBean();
				bean.setId(rs.getInt("id"));
				bean.setFm_id(rs.getInt("fm_id"));
				bean.setUserid(rs.getInt("userid"));
				bean.setUsername(rs.getString("username"));
				bean.setEvent(rs.getString("event"));
				bean.setFm_State(rs.getInt("fm_state"));
				bean.setEvent_time(rs.getTimestamp("event_time"));
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
	 * 
	 * 家族官员列表
	 * 
	 * @param id
	 * @param startindex
	 * @param endindex
	 * @return
	 */
	public List selectOfficerList(int id, int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id,fm_name from fm_user where fm_id=" + id + " and fm_flags!=0 "
				+ " limit " + startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyUserBean bean = new FamilyUserBean();
				bean.setId(rs.getInt("id"));
				bean.setFm_name(rs.getString("fm_name"));
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
	 * 家族贡献榜
	 * 
	 * @param id
	 * @param startindex
	 * @param endindex
	 * @return
	 */
	public List selectFundGiveList(int id, int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id, fm_id, gift_fm, con_fm, fm_name, fm_money_used, leave_fm_time, fm_state, fm_flags from fm_user where fm_id="
						+ id + " order by con_fm desc  limit" + startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyFundBean bean = new FamilyFundBean();
				bean.setId(rs.getInt("id"));
				bean.setFm_id(rs.getInt("fm_id"));
				bean.setUserid(rs.getInt("userid"));
				bean.setUsername(rs.getString("username"));
				bean.setEvent(rs.getString("event"));
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
	 * 请求加入新人
	 * 
	 * @return
	 */
	public List selectfamilyJoin(int id, int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id,fm_id,userid,username,join_time from fm_join where fm_id=" + id
				+ " order by id asc limit " + startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyNewManBean bean = new FamilyNewManBean();
				bean.setId(rs.getInt(1));
				bean.setFm_id(rs.getInt(2));
				bean.setUserid(rs.getInt(3));
				bean.setUsername(rs.getString(4));
				bean.set_time(rs.getTimestamp(5));
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
	 * 邀请加入新人
	 * 
	 * @return
	 */
	public List selectfamilyInvite(int id, int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id,fm_id,userid,username,invite_time from fm_invite where fm_id=" + id
				+ " order by id asc limit " + startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyNewManBean bean = new FamilyNewManBean();
				bean.setId(rs.getInt(1));
				bean.setFm_id(rs.getInt(2));
				bean.setUserid(rs.getInt(3));
				bean.setUsername(rs.getString(4));
				bean.set_time(rs.getTimestamp(5));
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
	 * 有多少请求加入新人
	 * 
	 * @return
	 */
	public int selectfamilyJoinCount(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select count(id) from fm_join where fm_id=" + id);
		try {
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			return 0;
		} finally {
			db.release();
		}
	}

	/**
	 * 有多少人邀请加入
	 * 
	 * @return
	 */
	public int selectfamilyInviteCount(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select count(id) from fm_invite where fm_id=" + id);
		try {
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			return 0;
		} finally {
			db.release();
		}
	}

	/**
	 * 加入家族
	 * 
	 * @return
	 */
	public boolean insertFamilyUser(int id, int fid) {
		DbOperation db = new DbOperation(5);
		try {
			ResultSet rs = db.executeQuery("select fm_id,userid from fm_join where id=" + id);
			int fm_id = 0, userid = 0;
			if (rs.next()) {
				fm_id = rs.getInt(1);
				userid = rs.getInt(2);
			}
			if (fid != fm_id) {// 申请家族不一致
				return false;
			}

			rs = db.executeQuery("select fm_id from fm_user where id=" + userid);
			if (rs.next()) {
				fm_id = rs.getInt(1);
			} else {
				fm_id = 0;// 如果不存在就从来没有加入家族
			}
			if (fm_id != 0) {// 申请用户已加入家族
				return false;
			}
			String query = "insert into fm_user(id,fm_id,create_time)values(" + userid + "," + fid
					+ ",now()) on duplicate key update create_time=now(),fm_id=" + fid;
			boolean result = db.executeUpdate(query);// 更新用户信息，如果不存在则插入
			if (!result) {// 创建用户失败
				return false;
			}
			result = db.executeUpdate("delete from fm_join where id=" + id);// 删除加入表中信息
			if (!result) {
				return false;
			}
			result = db.executeUpdate("update fm_apply_user set is_apply=2 where userid=" + userid);// 更新邀请数据
			if (!result) {
				return false;
			}
			result = db.executeUpdate("update fm_home set fm_member_num=fm_member_num+1 where id=" + fid);// 家族总数加一
			if (!result) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 删除请求加入记录
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteFamilyJoin(int id, int fid) {
		DbOperation db = new DbOperation(5);
		String query = "delete from fm_join where id=" + id + " and fm_id=" + fid;
		boolean rs = db.executeUpdate(query);
		db.release();
		return rs;
	}

	/**
	 * 接受邀请加入家族
	 * 
	 * @param id
	 * @param fid
	 * @return
	 */
	public boolean insertFamilyInvite(int id, int fid) {
		DbOperation db = new DbOperation(5);
		try {
			ResultSet rs = db.executeQuery("select fm_id,userid from fm_invite where id=" + id);
			int fm_id = 0, userid = 0;
			if (rs.next()) {
				fm_id = rs.getInt(1);
				userid = rs.getInt(2);
			}
			if (fid != fm_id) {// 邀请家族不一致
				return false;
			}
			String query = "insert into fm_user(id,fm_id,create_time)values(" + userid + "," + fid
					+ ",now()) on duplicate key update create_time=now(),fm_id=" + fid;
			boolean result = db.executeUpdate(query);// 更新用户信息，如果不存在则插入
			if (!result) {// 创建用户失败
				return false;
			}

			result = db.executeUpdate("delete from fm_invite where id=" + id + " and fm_id=" + fid);// 删除邀请表中信息
			if (!result) {
				return false;
			}
			result = db.executeUpdate("update fm_apply_user set is_apply=2 where userid=" + userid);// 更新邀请数据
			if (!result) {
				return false;
			}
			result = db.executeUpdate("update fm_home set fm_member_num=fm_member_num+1 where id=" + fid);// 家族总数加一
			if (!result) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 邀请加入的用户信息
	 * 
	 * @return
	 */
	public boolean insertInvite(int id, String name, int fid, String fmname, String invitename) {
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_invite(fm_id,fm_name,userid,username,invitename,invite_time)values(" + fid
				+ ",?," + id + ",?,?,now()) ";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, fmname);
			pstmt.setString(2, name);
			pstmt.setString(3, invitename);
			boolean result = db.executePstmt();
			return result;
		} catch (SQLException s) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 删除邀请记录
	 * 
	 * @param id
	 * @param fid
	 * @return
	 */
	public boolean deleteFamilyInvite(int id, int fid) {
		DbOperation db = new DbOperation(5);
		String query = "delete from fm_invite where id=" + id + " and fm_id=" + fid;
		boolean rs = db.executeUpdate(query);
		db.release();
		return rs;
	}

	/**
	 * 获得申邀请表中用户名
	 * 
	 * @param id
	 * @param b
	 *            true 申请表 fasle 邀请表
	 * @return
	 */
	public FamilyNewManBean selectName(int id, boolean b) {
		DbOperation db = new DbOperation(5);
		String query = "select userid,username,fm_id,fm_name from ";
		if (b) {
			query += "fm_join";
		} else {
			query += "fm_invite";
		}
		query += " where id=" + id;
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				FamilyNewManBean bean = new FamilyNewManBean();
				bean.setUserid(rs.getInt(1));
				bean.setUsername(rs.getString(2));
				bean.setFm_id(rs.getInt(3));
				bean.setFm_name(rs.getString(4));
				return bean;
			}
			return null;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 判断是否已经申请加入
	 * 
	 * @param id
	 * @param b
	 *            true 申请表 fasle 邀请表
	 * @return
	 */
	public boolean selectisexist(int id, int uid, boolean b) {
		DbOperation db = new DbOperation(5);
		String query = "select id from ";
		if (b) {
			query += "fm_join";
		} else {
			query += "fm_invite";
		}
		query += " where fm_id=" + id + " and userid=" + uid;
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 增加家族的基金总数
	 * 
	 * @param id
	 * @param fund
	 * @return
	 */
	public boolean updateFmFund(int id, int fund) {
		return updateFmHome("money=(money+" + fund + ")", id);
	}

	/**
	 * 根据家族Id修改家族信息
	 * 
	 * @param cond
	 * @param fmid
	 * @return
	 */
	public boolean updateFmHome(String cond, int fmid) {
		DbOperation db = new DbOperation(5);
		boolean rs = db.executeUpdate("update fm_home set " + cond + " where id=" + fmid);
		db.release();
		return rs;
	}

	/**
	 * 修改家族用户信息
	 * 
	 * @param cond
	 * @param query
	 * @return
	 */
	public boolean updateFmUser(String cond, String query) {
		DbOperation db = new DbOperation(5);
		boolean rs = db.executeUpdate("update fm_user set " + cond + " where " + query);
		db.release();
		return rs;
	}

	/**
	 * 族长让位
	 * 
	 * @param id
	 *            新族长id
	 * @param fmid
	 *            家族id
	 * @param shaikhid
	 *            族长id
	 * @return
	 */
	public boolean updateshaikhAbdicate(int userid, int fmid, int shaikhid, String newname, String oldname) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();
		try {
			ResultSet rs = db.executeQuery("select userid from fm_shaikh where fm_id=" + fmid
					+ " order by id desc limit 1");
			int userid2 = 0;
			if (rs.next()) {
				userid2 = rs.getInt(1);
			}
			if (userid != userid2) {// 邀请家族不一致
				return false;
			}

			String query = "update fm_user set fm_name='族长',fm_flags=" + FamilyUserBean.allflages() + " where id="
					+ userid;
			boolean result = db.executeUpdate(query);// 更新用户信息
			if (!result) {// 新帮主即位
				return false;
			}
			query = "update fm_user set fm_name='老族长',fm_flags=0 where id=" + shaikhid;
			result = db.executeUpdate(query);// 更新用户信息
			if (!result) {// 老帮主退位
				return false;
			}
			result = db.executeUpdate("update fm_home set leader_name='" + newname + "',leader_id=" + userid
					+ " where id=" + fmid);// 更新家族信息
			if (!result) {// 通知帮里的兄弟们
				return false;
			}

			result = db.executeUpdate("delete from fm_shaikh where fm_id=" + fmid);// 更新让位表
			if (!result) {
				return false;
			}

			// 历史
			String content = oldname + "将族长之位让与" + newname;
			query = "insert into fm_history (fm_id, event, event_time, fm_state)values(" + fmid + ",?,now(),3)";// 插入家族历史表
			if (!db.prepareStatement(query)) {
				return false;
			}
			// 传递参数
			PreparedStatement pstmt = db.getPStmt();
			pstmt.setString(1, content);
			result = db.executePstmt();
			if (!result) {
				return false;
			}

			db.commitTransaction();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 插入家族转让表
	 * 
	 * @param id
	 *            新族长的id
	 * @param fmid
	 *            家族id
	 * @param uname
	 *            新族长昵称
	 * @param oldname
	 *            老族长昵称
	 * @return
	 */
	public boolean insertShaikhAbdicate(int id, int fmid, String uname, String oldname) {
		FamilyHomeBean familyHomeBean = selectFamily(fmid);
		DbOperation db = new DbOperation(5);
		boolean result = false;
		try {
			result = db
					.executeUpdate("insert into fm_shaikh(fm_id, fm_name, userid, username, abdicatename, invite_time)values("
							+ fmid
							+ ",'"
							+ familyHomeBean.getFm_name()
							+ "',"
							+ id
							+ ",'"
							+ uname
							+ "','"
							+ oldname
							+ "',now())");
			if (!result) {
				return false;
			}

		} finally {
			db.release();
		}
		return result;
	}

	/**
	 * 退出家族
	 * 
	 * @param userid
	 * @return
	 */
	public boolean updateexitFmaily(int userid, int fmid) {
		DbOperation db = new DbOperation(5);
		try {
			String query = "update fm_user set fm_id=0,gift_fm=0,con_fm=0,fm_name='',fm_money_used=0,fm_state=0,fm_flags=0 where id="
					+ userid;
			boolean result = db.executeUpdate(query);// 更新用户信息
			if (!result) {// 退出家族
				return false;
			}
			query = "update fm_home set fm_member_num=fm_member_num-1 where id=" + fmid;
			result = db.executeUpdate(query);// 更新家族
			if (!result) {
				return false;
			}
			result = db.executeUpdate("delete from fm_user_exploit_history where userid=" + userid);// 删除个人功勋记录
			return result;
		} finally {
			db.release();
		}
	}

	/**
	 * 家族动态
	 * 
	 * @param userid
	 * @param username
	 * @param fm_id
	 * @param fm_name
	 * @param movement
	 * @param fm_state
	 *            0 创建 1解散 2其他
	 * @return
	 */
	public boolean insertFamilyMovement(int userid, String username, int fm_id, String fm_name, String movement,
			String fm_url, int fm_state) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();
		String query = "insert into fm_movement(userid, username, fm_id, fm_name, movement,movement_time,fm_url,fm_state)values("
				+ userid + ",?," + fm_id + ",?,?,now(),?," + fm_state + ")";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, username);
			pstmt.setString(2, fm_name);
			pstmt.setString(3, movement);
			pstmt.setString(4, fm_url);
			boolean result = db.executePstmt();
			if (!result) {
				return false;
			}
			db.commitTransaction();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 家族动态列表
	 * 
	 * @param startindex
	 * @param endindex
	 * @return
	 */
	public List selectFmActivities(int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id, userid, username, fm_id, fm_name, movement, movement_time, fm_url, fm_state from fm_movement order by id desc limit "
						+ startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyActivityBean bean = new FamilyActivityBean();
				bean.setId(rs.getInt("id"));
				bean.setFm_id(rs.getInt("fm_id"));
				bean.setUserid(rs.getInt("userid"));
				bean.setUsername(rs.getString("username"));
				bean.setFm_name(rs.getString("fm_name"));
				bean.setMovement(rs.getString("movement"));
				bean.setFm_url(rs.getString("fm_url"));
				bean.setFm_state(rs.getInt("fm_state"));
				bean.setMovement_time(rs.getTimestamp("movement_time"));
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
	 * 家族历史
	 * 
	 * @param id
	 * @param startindex
	 * @param endindex
	 * @return
	 */
	public List selectFmHistoryList(int id, int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id, fm_id, event, event_time, fm_state from fm_history where fm_id="
				+ id + " order by id desc  limit " + startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyHistoryBean bean = new FamilyHistoryBean();
				bean.setId(rs.getInt("id"));
				bean.setFm_id(rs.getInt("fm_id"));
				bean.setEvent(rs.getString("event"));
				bean.setEvent_time(rs.getTimestamp("event_time"));
				bean.setFm_state(rs.getInt("fm_state"));
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
	 * 请求加入
	 * 
	 * @param id
	 * @param nickName
	 * @param fmid
	 * @return
	 */
	public boolean insertapplyoin(int id, String nickName, int fmid, String fn_name) {
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_join(fm_id, fm_name, userid, username,join_time)values(" + fmid + ",?," + id
				+ ",?,now())";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, fn_name);
			pstmt.setString(2, nickName);
			boolean result = db.executePstmt();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 得到一条邀请信息
	 * 
	 * @param id
	 * @param id2
	 * @return
	 */
	public FamilyNewManBean selectfamilyOneInvite(int userid, int fmid) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id,fm_id,fm_name,userid,username,invite_time,invitename from fm_invite where fm_id="
						+ fmid + " and userid=" + userid);
		try {
			if (rs.next()) {
				FamilyNewManBean bean = new FamilyNewManBean();
				bean.setId(rs.getInt(1));
				bean.setFm_id(rs.getInt(2));
				bean.setFm_name(rs.getString(3));
				bean.setUserid(rs.getInt(4));
				bean.setUsername(rs.getString(5));
				bean.set_time(rs.getTimestamp(6));
				bean.setInvitename(rs.getString(7));
				return bean;
			}
			return null;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 搜索家族
	 * 
	 * @param fmn
	 * @param startindex
	 * @param endindex
	 * @return
	 */
	public List selectsearchFamilyList(String fmn, int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id, fm_name, fm_member_num from fm_home where fm_name like '" + fmn
				+ "%' order by fm_member_num desc  limit " + startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyHomeBean bean = new FamilyHomeBean();
				bean.setId(rs.getInt("id"));
				bean.setFm_name(rs.getString("fm_name"));
				bean.setFm_member_num(rs.getInt("fm_member_num"));
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
	 * 得到家族人气的排行榜的列表
	 * 
	 * @param startindex
	 * @param endindex
	 * @return
	 */
	public List selectFmMoodsList(int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id, fm_id, fm_name from fm_list_moods order by id asc limit "
				+ startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyHomeBean bean = new FamilyHomeBean();
				bean.setId(rs.getInt("fm_id"));
				bean.setFm_name(rs.getString("fm_name"));
				bean.setFm_level(rs.getInt("id"));
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
	 * 得到家族贡献排行榜
	 * 
	 * @param id
	 * @param startindex
	 * @param endindex
	 * @return
	 */
	public List selectFmGiveList(int id, int startindex, int endindex) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id,gift_fm from fm_user where fm_id=" + id
				+ " order by gift_fm desc limit " + startindex + "," + endindex);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyUserBean bean = new FamilyUserBean();
				bean.setId(rs.getInt(1));
				bean.setGift_fm(rs.getLong(2));
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
	 * 根据家族id,按基金捐款数 从多到少,得到所有用户id
	 * 
	 * @param id
	 * @return
	 */
	public List selectAllFmList(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id  from fm_user where fm_id=" + id + " order by gift_fm desc");
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyUserBean bean = new FamilyUserBean();
				bean.setId(rs.getInt(1));
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
	 * 家族升级
	 * 
	 * @param level
	 * @param money
	 */
	public boolean updateLevel(int fmid, int level, long money, FamilyFundBean bean) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();

		boolean result = false;
		// 基金使用表里插入记录
		String query = "insert into fm_fund_history(fm_id,event, event_time,fm_state)values(?,?,now(),?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return result;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, bean.getFm_id());
			pstmt.setString(2, bean.getEvent());
			pstmt.setInt(3, bean.getFm_State());
			result = db.executePstmt();
			if (!result) {
				return result;
			}
			// 历史
			String content = "家族升级,现家族等级为" + level;
			query = "insert into fm_history (fm_id, event, event_time, fm_state)values(" + fmid + ",'" + content
					+ "',now(),4)";// 插入家族历史表
			result = db.executeUpdate(query);
			if (!result) {
				return result;
			}

			// 修改家族表里家族基金总额
			result = db.executeUpdate("update fm_home set fm_level=" + level + ", money=(money-" + money
					+ ")  where id=" + fmid);
			if (!result) {
				return result;
			}
			db.commitTransaction();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 增加个人贡献
	 * 
	 * @param fmid
	 * @param exploit
	 *            增加贡献点
	 * @param event
	 *            为什么增加贡献点
	 * @return
	 */
	public boolean updateFmUser(int userid, int exploit, String event) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();

		boolean result = false;
		// 插入用户功勋历史表
		String query = "insert into fm_user_exploit_history(userid,event,event_time)values(?,?,now())";
		if (!db.prepareStatement(query)) {
			db.release();
			return result;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, userid);
			pstmt.setString(2, event);
			result = db.executePstmt();
			if (!result) {
				return result;
			}
			// 修改用户功勋
			result = db.executeUpdate("update fm_user set con_fm=con_fm+" + exploit + " where id=" + userid);
			if (!result) {
				return result;
			}
			db.commitTransaction();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 增加家族贡献
	 * 
	 * @param fmid
	 * @param exploit
	 *            增加贡献点
	 * @param event
	 *            为什么增加贡献点
	 * @param event_type
	 *            增加类型，1:问答 2:龙舟3:雪仗4:排行榜
	 * @return
	 */
	public boolean updateFmHome(int fmid, int exploit, String event, int eventType, int mid) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();

		boolean result = false;
		// 插入家族功勋历史表
		String query = "insert into fm_home_exploit_history(userid,event,event_time,event_type,m_id)values(?,?,now(),?,?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return result;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, fmid);
			pstmt.setString(2, event);
			pstmt.setInt(3, eventType);
			pstmt.setInt(4, mid);
			result = db.executePstmt();
			if (!result) {
				return result;
			}
			// 修改家族功勋
			result = db.executeUpdate("update fm_user set fm_exploit=fm_exploit+" + exploit + " where id=" + fmid);
			if (!result) {
				return result;
			}
			db.commitTransaction();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 个人功勋历史
	 * 
	 * @param userid
	 * @param startIndex
	 * @param countPerPage
	 * @return
	 */
	public List selectFmUserExpHisList(int userid, int startIndex, int countPerPage) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select event,event_time from fm_user_exploit_history where userid=" + userid
				+ " order by id desc limit " + startIndex + "," + countPerPage);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyUserBean bean = new FamilyUserBean();
				bean.setFm_name(rs.getString(1));
				bean.setLeave_fm_time(rs.getTimestamp(2));
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
	 * 家族功勋历史
	 * 
	 * @param userid
	 * @param startIndex
	 * @param countPerPage
	 * @return
	 */
	public List selectExpHisList(int fm_id, int event_type, int startIndex, int countPerPage) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select event,event_time,m_id from fm_home_exploit_history where fm_id=" + fm_id
				+ " and event_type=" + event_type + " order by id desc limit " + startIndex + "," + countPerPage);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FamilyUserBean bean = new FamilyUserBean();
				bean.setFm_name(rs.getString(1));// 事件
				bean.setLeave_fm_time(rs.getTimestamp(2));// 事件时间
				bean.setFm_id(rs.getInt(3));// mid
				bean.setFm_flags(event_type);// 事件类型
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
	 * 添加基金使用记录
	 * 
	 * @param fmid
	 * @param userid
	 * @param username
	 * @param event
	 * @param state
	 * @return
	 */
	public boolean insertFundHistory(int fmid, int userid, String username, String event, int state) {
		DbOperation db = new DbOperation(5);
		boolean result = false;
		// 插入家族功勋历史表
		String query = "insert into fm_fund_history(fm_id,userid,username,event,event_time,fm_state)values(?,?,?,?,now(),?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return result;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, fmid);
			pstmt.setInt(2, userid);
			pstmt.setString(3, username);
			pstmt.setString(4, event);
			pstmt.setInt(5, state);
			result = db.executePstmt();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 更新家族logo
	 * 
	 * @param id
	 * @param fileName
	 * @return
	 */
	public boolean updateFamilyLogo(int id, String fileName) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();
		String query = "update fm_home set  logo_url=? where id=" + id;
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, fileName);
			boolean result = db.executePstmt();
			return result;
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 创建家族申请,同时把申请人加入到邀请加入表中
	 * 
	 * @param userid
	 * @param fname
	 * @return
	 */
	public int insertfamilyapply(int userid, String fname) {
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_apply(id,fm_name,send_total,is_ok)values(" + userid
				+ ",?,0,0)on duplicate key update fm_name=?,send_total=0,is_ok=0";
		// 准备
		if (!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		// 传递参数
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, fname);
			pstmt.setString(2, fname);
			boolean result = db.executePstmt();
			if (!result) {
				return 0;
			}
			query = "insert into fm_apply_user(fm_apply_id,userid,apply_time,is_apply)values(" + userid + "," + userid
					+ ",now(),1)";
			result = db.executeUpdate(query);
			if (!result) {// 创建用户失败
				return 0;
			}
			return userid;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			db.release();
		}
	}

	/**
	 * 根据家族邀请id得到所有的参与用户
	 * 
	 * @param userid
	 * @param startIndex
	 * @param countPerPage
	 * @return
	 */
	public List selectFamilyApplyUserList(int fm_apply_id, int startIndex, int countPerPage) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id,userid,apply_time,is_apply from fm_apply_user where fm_apply_id="
				+ fm_apply_id + " group by 2 order by id asc limit " + startIndex + "," + countPerPage);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FmApplyUser bean = new FmApplyUser();
				bean.setId(rs.getInt(1));
				bean.setUserid(rs.getInt(2));
				bean.setApply_time(rs.getTimestamp(3).getTime());
				bean.setIs_apply(rs.getInt(4));
				bean.setFm_apply_id(fm_apply_id);
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
	 * 根据用户id得到所有参与的家族
	 * 
	 * @param userid
	 * @param startIndex
	 * @param countPerPage
	 * @return
	 */
	public List selectFamilyApplyUserList(int userid) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id,fm_apply_id,userid,apply_time,is_apply from fm_apply_user where is_apply=1 and userid="
						+ userid + " group by fm_apply_id order by id desc limit 10");
		try {
			List list = new ArrayList();
			while (rs.next()) {
				FmApplyUser bean = new FmApplyUser();
				bean.setId(rs.getInt(1));
				bean.setFm_apply_id(rs.getInt(2));
				bean.setUserid(rs.getInt(3));
				bean.setApply_time(rs.getTimestamp(4).getTime());
				bean.setIs_apply(rs.getInt(5));
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
	 * 根据用户id得到邀请参加
	 * 
	 * @param userid
	 * @return
	 */
	public FmApplyUser selectFamilyApplyUser(int userid, int apply_id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id,fm_apply_id,userid,apply_time,is_apply from fm_apply_user where fm_apply_id="
						+ apply_id + " and userid=" + userid + " order by id desc");
		try {
			while (rs.next()) {
				FmApplyUser bean = new FmApplyUser();
				bean.setId(rs.getInt(1));
				bean.setFm_apply_id(rs.getInt(2));
				bean.setUserid(rs.getInt(3));
				bean.setApply_time(rs.getTimestamp(4).getTime());
				bean.setIs_apply(rs.getInt(5));
				return bean;
			}
			return null;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 根据用户id得到邀请参加
	 * 
	 * @param userid
	 * @return
	 */
	public FmApplyUser selectFamilyApplyUser(int userid) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id,fm_apply_id,userid,apply_time,is_apply from fm_apply_user where userid="
						+ userid + " order by id desc");
		try {
			while (rs.next()) {
				FmApplyUser bean = new FmApplyUser();
				bean.setId(rs.getInt(1));
				bean.setFm_apply_id(rs.getInt(2));
				bean.setUserid(rs.getInt(3));
				bean.setApply_time(rs.getTimestamp(4).getTime());
				bean.setIs_apply(rs.getInt(5));
				return bean;
			}
			return null;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 根据用户id得到邀请参加
	 * 
	 * @param userid
	 * @return
	 */
	public FmApplyUser selectFamilyApplyUser(int userid, int is_apply, boolean bool) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id,fm_apply_id,userid,apply_time,is_apply from fm_apply_user where is_apply="
						+ is_apply + " and userid=" + userid + " order by id desc");
		try {
			while (rs.next()) {
				FmApplyUser bean = new FmApplyUser();
				bean.setId(rs.getInt(1));
				bean.setFm_apply_id(rs.getInt(2));
				bean.setUserid(rs.getInt(3));
				bean.setApply_time(rs.getTimestamp(4).getTime());
				bean.setIs_apply(rs.getInt(5));
				return bean;
			}
			return null;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 根据id家族邀请
	 * 
	 * @param id
	 * @return
	 */
	public FmApply selectFmApplybyId(int id) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select fm_name,send_total,is_ok from fm_apply where id=" + id);
		try {
			while (rs.next()) {
				FmApply bean = new FmApply();
				bean.setId(id);
				bean.setFm_name(rs.getString(1));
				bean.setSend_total(rs.getInt(2));
				bean.setIsok(rs.getInt(3));
				return bean;
			}
			return null;
		} catch (SQLException e) {
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 根据id家族邀请
	 * 
	 * @param id
	 * @return
	 */
	public boolean selectcheckedNumber(int applyId) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select distinct userid from fm_apply_user where is_apply=1 and fm_apply_id="
				+ applyId);
		try {
			int count = 0;
			while (rs.next()) {
				int userid = rs.getInt(1);
				UserStatusBean userStatus = UserInfoUtil.getUserStatus(userid);
				if (userStatus != null && userStatus.getTong() == 0) {
					count++;
				}
			}
			if (count > 4) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 添加新邀请，邀请数加一
	 * 
	 * @param applyid
	 * @param userid
	 * @return
	 */
	public boolean insertFamilyApplyUser(int applyid, int userid) {
		DbOperation db = new DbOperation(5);
		String query = "update fm_apply set send_total=send_total+1 where id=" + applyid;
		try {
			boolean result = db.executeUpdate(query);
			if (!result) {
				return false;
			}
			query = "insert into fm_apply_user(fm_apply_id,userid,apply_time)values(" + applyid + "," + userid
					+ ",now())";
			result = db.executeUpdate(query);
			if (!result) {// 创建用户失败
				return false;
			}
			return true;
		} finally {
			db.release();
		}
	}

	/**
	 * 更新邀请
	 * 
	 * @param applyid
	 * @param userid
	 * @return
	 */
	public boolean updateFamilyApplyUser(int apply_id, int userid, int is_apply) {
		DbOperation db = new DbOperation(5);
		try {
			String query = "update fm_apply_user set is_apply=" + is_apply + " where fm_apply_id=" + apply_id
					+ " and userid=" + userid;
			return db.executeUpdate(query);
		} finally {
			db.release();
		}
	}

	/**
	 * 帮会转化家族
	 * 
	 * @param fm_name
	 *            家族名
	 * @param fm_member_num
	 *            总人数
	 * @param fm_level
	 *            家族等级
	 * @param create_id
	 *            创始人
	 * @param nickName
	 *            创始人 名字
	 * @param money
	 *            资金
	 * @param forumId
	 *            论坛id
	 */
	public int insertchangTong(String fm_name, int fm_member_num, int fm_level, int create_id, String nickName,
			long money, int forumId) {
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_home(fm_name,fm_member_num,fm_level,fm_time,create_id,leader_id,leader_name,money,forumId)values(?,"
				+ fm_member_num
				+ ","
				+ fm_level
				+ ",now(),"
				+ create_id
				+ ","
				+ create_id
				+ ",'',"
				+ money
				+ ","
				+ forumId + ")";
		// 准备
		if (!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		// 传递参数
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setString(1, fm_name);
			boolean result = db.executePstmt();
			if (!result) {// 创建家族失败
				return 0;
			}

			int fmid = db.getLastInsertId();// 得到最后的id;

			query = "insert into fm_user(id,fm_id,fm_name,fm_flags,create_time)values(" + create_id + "," + fmid
					+ ",'族长'," + FamilyUserBean.allflages() + ",now()) on duplicate key update fm_id=" + fmid
					+ ",fm_name='族长',create_time=now(),fm_flags=" + FamilyUserBean.allflages();
			result = db.executeUpdate(query);// 更新用户信息，如果不存在则插入
			if (!result) {// 创建用户失败
				return 0;
			}
			// 动态
			query = "insert into fm_movement(userid,username,fm_id,fm_name,movement_time,fm_state)values(" + create_id
					+ ",?," + fmid + ",?,now(),0)";
			// 准备
			if (!db.prepareStatement(query)) {
				return 0;
			}
			// 传递参数
			pstmt = db.getPStmt();
			pstmt.setString(1, nickName);
			pstmt.setString(2, fm_name);
			result = db.executePstmt();
			if (!result) {// 添加动态信息
				return 0;
			}
			// 历史
			String content = nickName + "创建本家族:" + fm_name;
			query = "insert into fm_history (fm_id, event, event_time, fm_state)values(" + fmid + ",?,now(),1)";// 插入家族历史表
			if (!db.prepareStatement(query)) {
				return 0;
			}
			// 传递参数
			pstmt = db.getPStmt();
			pstmt.setString(1, content);
			result = db.executePstmt();
			if (!result) {// 创建家族失败
				return 0;
			}
			return fmid;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			db.release();
		}
	}

	/**
	 * 添加user
	 * 
	 * @param dbs
	 * @param userId
	 * @param fmid
	 * @return
	 */
	public boolean insertuserchange(int userId, int fmid) {
		DbOperation db = new DbOperation(5);
		try {
			String query = "insert into fm_user(id,fm_id,create_time)values(" + userId + "," + fmid
					+ ",now()) on duplicate key update create_time=now(),fm_id=" + fmid;
			return db.executeUpdate(query);// 更新用户信息，如果不存在则插入
		} finally {
			db.release();
		}

	}

	/**
	 * 添加历史记录
	 * 
	 * @param content
	 * @param fmid
	 * @param type
	 * @return
	 */
	public boolean insertHistory(String content, int fmid, int type) {
		DbOperation db = new DbOperation(5);
		try {
			String query = "insert into fm_history (fm_id, event, event_time, fm_state)values(" + fmid + ",?,now(),"
					+ type + ")";// 插入家族历史表
			if (!db.prepareStatement(query)) {
				return false;
			}
			// 传递参数
			PreparedStatement pstmt = db.getPStmt();
			pstmt.setString(1, content);
			return db.executePstmt();
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 添加动态
	 * 
	 * @param userid
	 * @param username
	 * @param fmid
	 * @param fmName
	 * @param movement
	 * @param fmUrl
	 * @param type
	 * @return
	 */
	public boolean insertMovement(int userid, String username, int fmid, String fmName, String movement, String fmUrl,
			int type) {
		DbOperation db = new DbOperation(5);
		try {
			// 动态
			String query = "insert into fm_movement(userid,username,fm_id,fm_name,movement,movement_time,fm_url,fm_state)values("
					+ userid + ",?," + fmid + ",?,?,now(),?," + type + ")";
			// 准备
			if (!db.prepareStatement(query)) {
				return false;
			}
			// 传递参数
			PreparedStatement pstmt = db.getPStmt();
			pstmt.setString(1, username == null ? "" : username);
			pstmt.setString(2, fmName == null ? "" : fmName);
			pstmt.setString(3, movement == null ? "" : movement);
			pstmt.setString(4, fmUrl == null ? "" : fmUrl);
			return db.executePstmt();
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	public boolean insertFmFundHistory(int userid, String username, int fmid, String fmName, String movement,
			String fmUrl, int type) {
		DbOperation db = new DbOperation(5);
		try {

			String query = "insert into fm_fund_history(fm_id, userid, username, event, event_time,fm_state)values(?,?,?,?,now(),?)";
			if (!db.prepareStatement(query)) {
				return false;
			}
			PreparedStatement pstmt = db.getPStmt();
			pstmt.setInt(1, fmid);
			pstmt.setInt(2, userid);
			pstmt.setString(3, username);
			pstmt.setString(4, "家族易帜扣除" + Constants.FM_RENAME);
			pstmt.setInt(5, 9);// 9改名易帜
			return db.executePstmt();
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 添加基金明细
	 * 
	 * @param fmid
	 * @param sum
	 * @param type
	 * @param balance
	 * @return
	 */
	public boolean insertFmFundDetail(int fmid, long sum, int type, long balance) {
		DbOperation db = new DbOperation(5);
		try {

			String query = "insert into fm_fund_detail(fm_id, c_time, fm_sum, fm_type, balance)values(?,now(),?,?,?)";
			if (!db.prepareStatement(query)) {
				return false;
			}
			PreparedStatement pstmt = db.getPStmt();
			pstmt.setInt(1, fmid);
			pstmt.setLong(2, sum);
			pstmt.setInt(3, type);
			pstmt.setLong(4, balance);
			return db.executePstmt();
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 添加基金明细
	 * 
	 * @param fmid
	 * @param sum
	 * @param type
	 * @param balance
	 * @return
	 */
	public List selectFmFundDetail(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_fund_detail where " + cond);
		try {
			while (rs.next()) {
				list.add(getFmFundDetail(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 添加用户log
	 * 
	 * @param fmid
	 * @param sum
	 * @param type
	 * @param balance
	 * @return
	 */
	public boolean insertFmUserLog(int fmid, String event, int type) {
		DbOperation db = new DbOperation(5);
		try {

			String query = "insert into fm_user_log(fm_id, event, event_time, fm_state)values(?,?,now(),?)";
			if (!db.prepareStatement(query)) {
				return false;
			}
			PreparedStatement pstmt = db.getPStmt();
			pstmt.setInt(1, fmid);
			pstmt.setString(2, event);
			pstmt.setInt(3, type);
			return db.executePstmt();
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 查询log
	 * 
	 * @param fmid
	 * @param sum
	 * @param type
	 * @param balance
	 * @return
	 */
	public List selectFmUserLogList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_user_log where " + cond);
		try {
			while (rs.next()) {
				list.add(geFmLog(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private FamilyHistoryBean geFmLog(ResultSet rs) throws SQLException {
		FamilyHistoryBean bean = new FamilyHistoryBean();
		bean.setId(rs.getInt("id"));
		bean.setFm_id(rs.getInt("fm_id"));
		bean.setEvent(rs.getString("event"));
		bean.setEvent_time(rs.getTimestamp("event_time"));
		bean.setFm_state(rs.getInt("fm_state"));
		return bean;
	}

	private FundDetail getFmFundDetail(ResultSet rs) throws SQLException {
		FundDetail bean = new FundDetail();
		bean.setId(rs.getInt("id"));
		bean.setFmId(rs.getInt("fm_id"));
		bean.setcTime(rs.getTimestamp("c_time").getTime());
		bean.setSum(rs.getLong("fm_sum"));
		bean.setType(rs.getInt("fm_type"));
		bean.setBalance(rs.getLong("balance"));
		return bean;
	}

	private FmScore getFmScore(ResultSet rs) throws SQLException {
		FmScore bean = new FmScore();
		bean.setId(rs.getInt("id"));
		bean.setAskScore(rs.getInt("ask_score"));
		bean.setBoatScore(rs.getInt("boat_score"));
		bean.setSnowScore(rs.getInt("snow_score"));
		return bean;
	}

	private FmScore getFmVsScore(ResultSet rs) throws SQLException {
		FmScore bean = new FmScore();
		bean.setId(rs.getInt("id"));
		bean.setSnowScore(rs.getInt("score"));
		return bean;
	}

	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}
	
	public boolean executeUpdate(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}
	
	// ---------奖牌相关
	public List getMedalList(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from fm_medal where " + cond);
		try {
			List list = new ArrayList();
			while (rs.next()) {
				list.add(getMedal(rs));
			}
			return list;
		} catch (SQLException e) {
			return new ArrayList(0);
		} finally {
			db.release();
		}
	}
	private FamilyMedalBean getMedal(ResultSet rs) throws SQLException {
		FamilyMedalBean bean = new FamilyMedalBean();
		bean.setId(rs.getInt("id"));
		bean.setFmId(rs.getInt("fm_id"));
		bean.setSeq(rs.getInt("seq"));
		bean.setName(rs.getString("name"));
		bean.setImg(rs.getString("img"));
		bean.setInfo(rs.getString("info"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setEndTime(rs.getTimestamp("end_time").getTime());
		return bean;
	}
}
