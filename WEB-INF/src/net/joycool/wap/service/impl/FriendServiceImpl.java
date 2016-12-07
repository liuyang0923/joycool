package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.bean.friend.FriendActionBean;
import net.joycool.wap.bean.friend.FriendBadUserBean;
import net.joycool.wap.bean.friend.FriendBagBean;
import net.joycool.wap.bean.friend.FriendBean;
import net.joycool.wap.bean.friend.FriendCartoonBean;
import net.joycool.wap.bean.friend.FriendDrinkBean;
import net.joycool.wap.bean.friend.FriendGuestBean;
import net.joycool.wap.bean.friend.FriendMarriageBean;
import net.joycool.wap.bean.friend.FriendProposalBean;
import net.joycool.wap.bean.friend.FriendReviewBean;
import net.joycool.wap.bean.friend.FriendRingBean;
import net.joycool.wap.bean.friend.FriendUserBean;
import net.joycool.wap.bean.friend.FriendVoteBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class FriendServiceImpl implements IFriendService {

	static ICacheMap friendInfoCache = CacheManage.friendInfo;
	static ICacheMap friendSearchCache = CacheManage.friendSearch;
	/*
	 * （非 Javadoc） macq_2006-11-21_增加用户照片排名方法
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#addFriendVote(net.joycool.wap.bean.friend.FriendVoteBean)
	 */
	public boolean addFriendVote(FriendVoteBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_vote(user_id,count) VALUES(?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setLong(2, bean.getCount());
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

	/*
	 * （非 Javadoc） macq_2006-11-21_增加用户照片排名方法
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#delFriendVote(java.lang.String)
	 */
	public boolean delFriendVote(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_vote WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc） macq_2006-11-21_增加用户照片排名方法
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVote(java.lang.String)
	 */
	public FriendVoteBean getFriendVote(String condition) {
		FriendVoteBean friendVote = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_vote";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendVote = this.getFriendVote(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendVote;
	}

	/*
	 * （非 Javadoc） macq_2006-11-21_增加用户照片排名方法
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVoteCount(java.lang.String)
	 */
	public int getFriendVoteCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_vote";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	/*
	 * （非 Javadoc） macq_2006-11-21_增加用户照片排名方法
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVoteList(java.lang.String)
	 */
	public Vector getFriendVoteList(String condition) {
		Vector friendVoteList = new Vector();
		FriendVoteBean friendVote = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_vote";
		// 构建更新语句
		if (condition != null) {
			if (condition.toLowerCase().indexOf("left join") != -1) {
				query = condition;
			} else {
				query = query + " WHERE " + condition;
			}
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendVote = this.getFriendVote(rs);
				friendVoteList.add(friendVote);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendVoteList;
	}
	public List getFriendVoteList2(String query) {
		List friendVoteList = new ArrayList();
		DbOperation dbOp = new DbOperation(true);

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				friendVoteList.add(getFriendVote(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return friendVoteList;
	}

	/*
	 * （非 Javadoc） macq_2006-11-21_增加用户照片排名方法
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#updateFriendVote(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateFriendVote(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_vote SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private FriendVoteBean getFriendVote(ResultSet rs) throws SQLException {
		FriendVoteBean friendVote = new FriendVoteBean();
		friendVote.setId(rs.getInt("id"));
		friendVote.setUserId(rs.getInt("user_id"));
		friendVote.setCount(rs.getLong("count"));
		return friendVote;
	}

	public void flushFriend(int id) {
		friendInfoCache.srm(id);
	}
	public FriendBean getFriend(Integer iid) {
		synchronized(friendInfoCache) {
			FriendBean friendBean = (FriendBean) friendInfoCache.get(iid);
			if (friendBean == null) {
	
				DbOperation dbOp = new DbOperation(true);
	
				ResultSet rs = dbOp.executeQuery("SELECT * from jc_friend where user_id=" + iid);
				try {
					if (rs.next()) {
						friendBean = this.getFriend(rs);
						friendInfoCache.put(iid, friendBean);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dbOp.release();
			}
			return friendBean;
		}
	}
	public FriendBean getFriend(int id) {
		return getFriend(Integer.valueOf(id));
	}

	public Vector getFriendList(String condition) {
		Vector FriendList = new Vector();
		FriendBean friend = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friend = this.getFriend(rs);
				FriendList.add(friend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return FriendList;
	}

	public boolean addFriend(FriendBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// liuyi 2007-01-22 交友信息修改 start
		String query = "INSERT INTO jc_friend(user_id,name,mobile,city,constellation,"
				+ "height,height_type,weight,weight_type,work,personality,marriage,aim,friend_condition,"
				+ "attach,gender,age,age_type,attach_type,create_datetime,birthday) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getMobile());
			pstmt.setString(4, bean.getCity());
			pstmt.setInt(5, bean.getConstellation());
			pstmt.setInt(6, bean.getHeight());
			pstmt.setInt(7, bean.getHeightType());
			pstmt.setInt(8, bean.getWeight());
			pstmt.setInt(9, bean.getWeightType());
			pstmt.setString(10, bean.getWork());
			pstmt.setInt(11, bean.getPersonality());
			pstmt.setInt(12, bean.getMarriage());
			pstmt.setInt(13, bean.getAim());
			pstmt.setString(14, bean.getFriendCondition());
			pstmt.setString(15, bean.getAttach());
			pstmt.setInt(16, bean.getGender());
			pstmt.setInt(17, bean.getAge());
			pstmt.setInt(18, bean.getAgeType());
			pstmt.setInt(19, bean.getAttachType());
			String birthday = bean.getBirthday();
			pstmt.setString(20, (birthday != null) ? birthday
					: "1986-01-01");
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// liuyi 2007-01-22 交友信息修改 end
		// 执行
		dbOp.executePstmt();
		// 释放资源
		dbOp.release();
		flushFriend(bean.getUserId());
		return true;
	}

	public boolean delFriend(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriend(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendBean getFriend(ResultSet rs) throws SQLException {
		FriendBean friend = new FriendBean();
		friend.setUserId(rs.getInt("user_id"));
		friend.setName(rs.getString("name"));
		friend.setMobile(rs.getString("mobile"));
		friend.setCity(rs.getString("city"));
		friend.setConstellation(rs.getInt("constellation"));
		friend.setHeight(rs.getInt("height"));
		friend.setHeightType(rs.getInt("height_type"));
		friend.setWeight(rs.getInt("weight"));
		friend.setWeightType(rs.getInt("weight_type"));
		friend.setWork(rs.getString("work"));
		friend.setPersonality(rs.getInt("personality"));
		friend.setMarriage(rs.getInt("marriage"));
		friend.setAim(rs.getInt("aim"));
		friend.setFriendCondition(rs.getString("friend_condition"));
		friend.setAttach(rs.getString("attach"));
		friend.setAttachType(rs.getInt("attach_type"));
		friend.setGender(rs.getInt("gender"));
		friend.setAge(rs.getInt("age"));
		friend.setAgeType(rs.getInt("age_type"));
		friend.setCreateDatetime(rs.getString("create_datetime"));
		// liuyi 2007-01-22 生日 start
		friend.setBirthday(StringUtil.date2String(rs, "birthday"));
		// liuyi 2007-01-22 生日 end
		return friend;
	}

	public FriendProposalBean getFriendProposal(String condition) {
		FriendProposalBean friendProposal = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_proposal";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendProposal = this.getFriendProposal(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendProposal;
	}

	public Vector getFriendProposalList(String condition) {
		Vector friendProposalList = new Vector();
		FriendProposalBean friendProposal = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_proposal";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendProposal = this.getFriendProposal(rs);
				friendProposalList.add(friendProposal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendProposalList;
	}

	public boolean addFriendProposal(FriendProposalBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_proposal(from_id,to_id,finger_ring_id,mark,create_datetime) VALUES(?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFromId());
			pstmt.setInt(2, bean.getToId());
			pstmt.setInt(3, bean.getFingerRingId());
			pstmt.setInt(4, bean.getMark());
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

	public boolean delFriendProposal(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_proposal WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriendProposal(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_proposal SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendProposalCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_proposal WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendProposalBean getFriendProposal(ResultSet rs)
			throws SQLException {
		FriendProposalBean friendProposal = new FriendProposalBean();
		friendProposal.setId(rs.getInt("id"));
		friendProposal.setFromId(rs.getInt("from_id"));
		friendProposal.setToId(rs.getInt("to_id"));
		friendProposal.setFingerRingId(rs.getInt("finger_ring_id"));
		friendProposal.setMark(rs.getInt("mark"));
		friendProposal.setCreateDatetime(rs.getString("create_datetime"));
		return friendProposal;
	}

	public FriendMarriageBean getFriendMarriage(String condition) {
		FriendMarriageBean friendMarriage = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_marriage";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendMarriage = this.getFriendMarriage(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendMarriage;
	}

	public Vector getFriendMarriageList(String condition) {
		Vector friendMarriageList = new Vector();
		FriendMarriageBean friendMarriage = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_marriage";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendMarriage = this.getFriendMarriage(rs);
				friendMarriageList.add(friendMarriage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendMarriageList;
	}

	public boolean addFriendMarriage(FriendMarriageBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_marriage(from_id,to_id,finger_ring_id,mark,create_datetime,candy_price,candy_num,candy_remain,pledge,guest_num,money,redbag_num,marriage_form) VALUES(?,?,?,?,now(),?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFromId());
			pstmt.setInt(2, bean.getToId());
			pstmt.setInt(3, bean.getFingerRingId());
			pstmt.setInt(4, bean.getMark());
			pstmt.setInt(5, bean.getCandyPrice());
			pstmt.setInt(6, bean.getCandyNum());
			pstmt.setInt(7, bean.getCandyRemain());
			pstmt.setString(8, bean.getPledge());
			pstmt.setInt(9, bean.getGuestNum());
			pstmt.setLong(10, bean.getMoney());
			pstmt.setInt(11, bean.getRedbagNum());
			pstmt.setInt(12, bean.getMarriageForm());
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

	public boolean delFriendMarriage(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_marriage WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriendMarriage(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_marriage SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendMarriageCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_marriage WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendMarriageBean getFriendMarriage(ResultSet rs)
			throws SQLException {
		FriendMarriageBean friendMarriage = new FriendMarriageBean();
		friendMarriage.setId(rs.getInt("id"));
		friendMarriage.setFromId(rs.getInt("from_id"));
		friendMarriage.setToId(rs.getInt("to_id"));
		friendMarriage.setFingerRingId(rs.getInt("finger_ring_id"));
		friendMarriage.setMark(rs.getInt("mark"));
		friendMarriage.setCreateDatetime(rs.getString("create_datetime"));
		friendMarriage.setCandyPrice(rs.getInt("candy_price"));
		friendMarriage.setCandyNum(rs.getInt("candy_num"));
		friendMarriage.setCandyRemain(rs.getInt("candy_remain"));
		friendMarriage.setPledge(rs.getString("pledge"));
		friendMarriage.setGuestNum(rs.getInt("guest_num"));
		friendMarriage.setMoney(rs.getLong("money"));
		friendMarriage.setRedbagNum(rs.getInt("redbag_num"));
		friendMarriage.setMarriageForm(rs.getInt("marriage_form"));
		friendMarriage.setMarriageDatetime(rs.getString("marriage_datetime"));
		return friendMarriage;
	}

	public FriendRingBean getFriendRing(String condition) {
		FriendRingBean fgriendRing = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_ring";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				fgriendRing = this.getFriendRing(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return fgriendRing;
	}

	public Vector getFriendRingList(String condition) {
		Vector friendRingList = new Vector();
		FriendRingBean friendRing = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_ring";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendRing = this.getFriendRing(rs);
				friendRingList.add(friendRing);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendRingList;
	}

	public boolean addFriendRing(FriendRingBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_ring(name,price,picture) VALUES(?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getPrice());
			pstmt.setString(3, bean.getPicture());
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

	public boolean delFriendRing(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_ring WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriendRing(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_ring SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendRingCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_ring WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendRingBean getFriendRing(ResultSet rs) throws SQLException {
		FriendRingBean friendRing = new FriendRingBean();
		friendRing.setId(rs.getInt("id"));
		friendRing.setName(rs.getString("name"));
		friendRing.setPrice(rs.getInt("price"));
		friendRing.setPicture(rs.getString("picture"));
		return friendRing;
	}

	public FriendReviewBean getFriendReview(String condition) {
		FriendReviewBean friendReview = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_review";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendReview = this.getFriendReview(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendReview;
	}

	public Vector getFriendReviewList(String condition) {
		Vector friendReviewBeanList = new Vector();
		FriendReviewBean friendReview = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_review";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendReview = this.getFriendReview(rs);
				friendReviewBeanList.add(friendReview);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendReviewBeanList;
	}

	public boolean addFriendReview(FriendReviewBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_review(marriage_id,review_user_id,review,file,create_datetime) VALUES(?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getMarriageId());
			pstmt.setInt(2, bean.getReviewUserId());
			pstmt.setString(3, bean.getReview());
			pstmt.setInt(4, bean.getFile());
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

	public boolean delFriendReview(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_review WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriendReview(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_review SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendReviewCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_review WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendReviewBean getFriendReview(ResultSet rs) throws SQLException {
		FriendReviewBean friendReview = new FriendReviewBean();
		friendReview.setId(rs.getInt("id"));
		friendReview.setMarriageId(rs.getInt("marriage_id"));
		friendReview.setReviewUserId(rs.getInt("review_user_id"));
		friendReview.setFile(rs.getInt("file"));
		friendReview.setReview(rs.getString("review"));
		friendReview.setCreateDatetime(rs.getString("create_datetime"));
		return friendReview;
	}

	public FriendBagBean getFriendBag(String condition) {
		FriendBagBean friendBag = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_bag";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendBag = this.getFriendBag(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendBag;
	}

	public Vector getFriendBagList(String condition) {
		Vector friendBagList = new Vector();
		FriendBagBean friendBag = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_bag";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendBag = this.getFriendBag(rs);
				friendBagList.add(friendBag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendBagList;
	}

	public boolean addFriendBag(FriendBagBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_bag(marriage_id,user_id,price,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getMarriageId());
			pstmt.setInt(2, bean.getUserId());
			pstmt.setInt(3, bean.getPrice());
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

	public boolean delFriendBag(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_bag WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriendBag(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_bag SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendBagCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_bag WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendBagBean getFriendBag(ResultSet rs) throws SQLException {
		FriendBagBean friendBag = new FriendBagBean();
		friendBag.setId(rs.getInt("id"));
		friendBag.setMarriageId(rs.getInt("marriage_id"));
		friendBag.setUserId(rs.getInt("user_id"));
		friendBag.setPrice(rs.getInt("price"));
		friendBag.setCreateDatetime(rs.getString("create_datetime"));
		return friendBag;
	}

	public FriendGuestBean getFriendGuest(String condition) {
		FriendGuestBean friendGuest = null;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {

			friendGuest = (FriendGuestBean) OsCacheUtil.get(condition,
					OsCacheUtil.FRIEND_GUEST_BEAN_GROUP,
					OsCacheUtil.FRIEND_GUEST_FLUSH_PERIOD);
			if (friendGuest != null)
				return friendGuest;

		}
		// lbj_2006-08-05_缓存_end
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_guest";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendGuest = this.getFriendGuest(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		OsCacheUtil.put(condition, friendGuest,
				OsCacheUtil.FRIEND_GUEST_BEAN_GROUP);
		return friendGuest;
	}

	public Vector getFriendGuestList(String condition) {
		Vector friendGuestList = new Vector();
		FriendGuestBean friendGuest = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_guest";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendGuest = this.getFriendGuest(rs);
				friendGuestList.add(friendGuest);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendGuestList;
	}

	public boolean addFriendGuest(FriendGuestBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_guest(marriage_id,user_id,mark,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getMarriageId());
			pstmt.setInt(2, bean.getUserId());
			pstmt.setInt(3, bean.getMark());
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

	public boolean delFriendGuest(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_guest WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriendGuest(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_guest SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendGuestCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_guest WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendGuestBean getFriendGuest(ResultSet rs) throws SQLException {
		FriendGuestBean friendGuest = new FriendGuestBean();
		friendGuest.setId(rs.getInt("id"));
		friendGuest.setMarriageId(rs.getInt("marriage_id"));
		friendGuest.setUserId(rs.getInt("user_id"));
		friendGuest.setMark(rs.getInt("mark"));
		friendGuest.setAction1(rs.getInt("action1"));
		friendGuest.setAction2(rs.getInt("action2"));
		friendGuest.setAction3(rs.getInt("action3"));
		friendGuest.setCreateDatetime(rs.getString("create_datetime"));
		return friendGuest;
	}

	/**
	 * liuyi 2006-10-28 根据id获取结义酒
	 * 
	 * @param friendDrinkId
	 * @return
	 */
	public FriendDrinkBean getFriendDrink(int friendDrinkId) {
		FriendDrinkBean friendDrink = null;
		friendDrink = (FriendDrinkBean) OsCacheUtil.get("" + friendDrinkId,
				OsCacheUtil.FRIEND_DRINK_BEAN_GROUP,
				OsCacheUtil.FRIEND_DRINK_BEAN_FLUSH_PERIOD);
		if (friendDrink == null) {
			friendDrink = getFriendDrink(" id=" + friendDrinkId);
			if (friendDrink != null)
				OsCacheUtil.put("" + friendDrinkId, friendDrink,
						OsCacheUtil.FRIEND_DRINK_BEAN_GROUP);
		}
		return friendDrink;
	}

	public FriendDrinkBean getFriendDrink(String condition) {
		FriendDrinkBean friendDrink = null;
		// 构建查询语句
		String query = "SELECT * from jc_friend_drink";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendDrink = this.getFriendDrink(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendDrink;
	}

	public Vector getFriendDrinkList(String condition) {
		Vector friendDrinkList = new Vector();
		FriendDrinkBean friendDrink = null;

		// 构建查询语句
		String query = "SELECT * from jc_friend_drink";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// liuyi 2006-11-03 结义酒加缓存 start
		Vector drinkList = (Vector) OsCacheUtil.get(query,
				OsCacheUtil.FRIEND_DRINK_GROUP,
				OsCacheUtil.FRIEND_DRINK_FLUSH_PERIOD);
		if (drinkList != null) {
			return drinkList;
		}

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs != null) {
				while (rs.next()) {
					friendDrink = this.getFriendDrink(rs);
					friendDrinkList.add(friendDrink);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();

		OsCacheUtil.put(query, friendDrinkList, OsCacheUtil.FRIEND_DRINK_GROUP);
		// liuyi 2006-11-03 结义酒加缓存 end
		return friendDrinkList;
	}

	public boolean addFriendDrink(FriendDrinkBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_drink(name,price) VALUES(?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getPrice());
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

	public boolean delFriendDrink(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_drink WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriendDrink(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_drink SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendDrinkCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_drink WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendDrinkBean getFriendDrink(ResultSet rs) throws SQLException {
		FriendDrinkBean friendDrink = new FriendDrinkBean();
		friendDrink.setId(rs.getInt("id"));
		friendDrink.setName(rs.getString("name"));
		friendDrink.setPrice(rs.getInt("price"));
		return friendDrink;
	}

	public FriendUserBean getFriendUser(String condition) {
		FriendUserBean friendUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendUser = this.getFriendUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendUser;
	}

	public Vector getFriendUserList(String condition) {
		Vector friendUserList = new Vector();
		FriendUserBean friendUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendUser = this.getFriendUser(rs);
				friendUserList.add(friendUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendUserList;
	}

	public boolean addFriendUser(FriendUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_user(user_id,friend_id,mark,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getFriendId());
			pstmt.setInt(3, bean.getMark());
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

	public boolean delFriendUser(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriendUser(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_user SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_user WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendUserBean getFriendUser(ResultSet rs) throws SQLException {
		FriendUserBean friendUser = new FriendUserBean();
		friendUser.setId(rs.getInt("id"));
		friendUser.setUserId(rs.getInt("user_id"));
		friendUser.setFriendId(rs.getInt("friend_id"));
		friendUser.setMark(rs.getInt("mark"));
		friendUser.setCreateDatetime(rs.getString("create_datetime"));
		return friendUser;
	}

	public FriendBadUserBean getFriendBadUser(String condition) {
		FriendBadUserBean friendBadUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_bad_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendBadUser = this.getFriendBadUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendBadUser;
	}

	public Vector getFriendBadUserList(String condition) {
		Vector friendBadUserList = new Vector();
		FriendBadUserBean friendBadUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_bad_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendBadUser = this.getFriendBadUser(rs);
				friendBadUserList.add(friendBadUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendBadUserList;
	}

	public boolean addFriendBadUser(FriendBadUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_bad_user(user_id,friend_id,create_datetime) VALUES(?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getFriendId());
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

	public boolean delFriendBadUser(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_bad_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateFriendBadUser(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_bad_user SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getFriendBadUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_bad_user WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	private FriendBadUserBean getFriendBadUser(ResultSet rs)
			throws SQLException {
		FriendBadUserBean friendBadUser = new FriendBadUserBean();
		friendBadUser.setId(rs.getInt("id"));
		friendBadUser.setUserId(rs.getInt("user_id"));
		friendBadUser.setFriendId(rs.getInt("friend_id"));
		friendBadUser.setCreateDatetime(rs.getString("create_datetime"));
		return friendBadUser;
	}

	public long getFriendBagTotal(String condition) {
		long count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT sum(price) as c_id FROM jc_friend_bag WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getLong("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public int getFriendCacheCount(String condition) {

		String query = "SELECT count(id) as c_id FROM jc_friend WHERE " + condition;

		String key = "c " + condition;
		synchronized(friendSearchCache) {
			Integer c = (Integer) friendSearchCache.get(key);
			if (c != null) {
				return c.intValue();
			}
	
			int count = 0;
			DbOperation dbOp = new DbOperation(true);
	
			ResultSet rs = dbOp.executeQuery(query);
	
			try {
				if (rs.next()) {
					count = rs.getInt("c_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
	
			friendSearchCache.put(key, new Integer(count));
	
			return count;
		}
	}

	public Vector getFriendCacheList(String condition) {

		String sql = "SELECT * FROM jc_friend";
		if (condition != null) {
			sql = sql + " WHERE " + condition;
		}
		
		String key = condition;
		synchronized(friendSearchCache) {
			Vector friendCityList = (Vector) friendSearchCache.get(key);
			if (friendCityList != null) {
				return friendCityList;
			}
	
			friendCityList = new Vector();
	
			DbOperation dbOp = new DbOperation(true);
			ResultSet rs = dbOp.executeQuery(sql);
			if (rs == null) {
				dbOp.release();
				return null;
			}
	
			try {
				while (rs.next()) {
					friendCityList.add(getFriend(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();
	
			friendSearchCache.put(key, friendCityList);
			return friendCityList;
		}
	}

	public int getFriendMarriageId(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT id as c_id FROM jc_friend_marriage WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public boolean addFriendAction(FriendActionBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_action(type_id,content,file) VALUES(?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTypeId());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getFile());
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

	/*
	 */
	public boolean delFriendAction(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_action WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * 
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVote(java.lang.String)
	 */
	public FriendActionBean getFriendAction(String condition) {
		FriendActionBean friendAction = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_action";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendAction = this.getFriendAction(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendAction;
	}

	/*
	 * 
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVoteCount(java.lang.String)
	 */
	public int getFriendActionCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_action";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	/*
	 * 
	 * 
	 * 
	 */
	public Vector getFriendActionList(String condition) {
		Vector friendActionList = new Vector();

		FriendActionBean friendAction = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_action";
		// 构建更新语句
		if (condition != null) {
			if (condition.toLowerCase().indexOf("left join") != -1) {
				query = condition;
			} else {
				query = query + " WHERE " + condition;
			}
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendAction = this.getFriendAction(rs);
				friendActionList.add(friendAction);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendActionList;
	}

	/*
	 * 
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#updateFriendVote(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateFriendAction(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_action SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private FriendActionBean getFriendAction(ResultSet rs) throws SQLException {
		FriendActionBean friendAction = new FriendActionBean();
		friendAction.setId(rs.getInt("id"));
		friendAction.setContent(rs.getString("content"));
		friendAction.setFile(rs.getInt("file"));
		friendAction.setTypeId(rs.getInt("type_id"));
		return friendAction;
	}

	public boolean addFriendCartoon(FriendCartoonBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_friend_cartoon(type,pic) VALUES(?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getType());
			pstmt.setString(2, bean.getPic());

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

	/*
	 */
	public boolean delFriendCartoon(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_friend_cartoon WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * 
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVote(java.lang.String)
	 */
	public FriendCartoonBean getFriendCartoon(String condition) {
		FriendCartoonBean friendAction = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_cartoon";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendAction = this.getFriendCartoon(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendAction;
	}

	/*
	 * 
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVoteCount(java.lang.String)
	 */
	public int getFriendCartoonCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_friend_cartoon";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	/*
	 * 
	 * 
	 * 
	 */
	public Vector getFriendCartoonList(String condition) {
		Vector friendActionList = new Vector();

		FriendCartoonBean friendAction = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from jc_friend_cartoon";
		// 构建更新语句
		if (condition != null) {
			if (condition.toLowerCase().indexOf("left join") != -1) {
				query = condition;
			} else {
				query = query + " WHERE " + condition;
			}
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendAction = this.getFriendCartoon(rs);
				friendActionList.add(friendAction);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendActionList;
	}

	/*
	 * 
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#updateFriendVote(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateFriendCartoon(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_friend_cartoon SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private FriendCartoonBean getFriendCartoon(ResultSet rs)
			throws SQLException {
		FriendCartoonBean friend = new FriendCartoonBean();
		friend.setId(rs.getInt("id"));
		friend.setPic(rs.getString("pic"));
		friend.setType(rs.getInt("type"));

		return friend;
	}
}
