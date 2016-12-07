package jc.match;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class MatchService {

	public MatchRank getMatchRank(String cond){
		MatchRank bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_rank where " + cond);
		try {
			if (rs.next()){
				bean = getRankBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getRankList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_rank where " + cond);
		try {
			while (rs.next()){
				list.add(getRankBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	
	// = =||估计用不到这个方法
	public boolean addRank(MatchRank bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_rank(user_id,vote_count) values (?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getVoteCount());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchRank getRankBean(ResultSet rs) throws SQLException{
		MatchRank bean = new MatchRank();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setVoteCount(rs.getInt("vote_count"));
		return bean;
	}
	
	public MatchFans getMatchFans(String cond){
		MatchFans bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_fans where " + cond);
		try {
			if (rs.next()){
				bean = getFansBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getFansList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_fans where " + cond);
		try {
			while (rs.next()){
				list.add(getFansBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	/**
	 * 注意返回的Map是以用户所持有的物品的ID做为Key，而不是ID
	 * @param cond
	 * @return
	 */
//	public HashMap getFansMap(String cond){
//		MatchFans bean = null;
//		HashMap map = new HashMap();
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select * from match_fans where " + cond);
//		try {
//			while (rs.next()){
//				bean = getFansBean(rs);
//				// 注意这里，以用户持有的物品ID为Key，方便查找
//				map.put(new Integer(bean.getGoodsId()), bean);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return map;
//	}
	
	public boolean addFans(MatchFans bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_fans(user_id,good0,good1,good2,good3,good4,good5,good6,good7,prices) values (?,?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			int good[] = bean.getGood();
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, good[0]);
			pstmt.setInt(3, good[1]);
			pstmt.setInt(4, good[2]);
			pstmt.setInt(5, good[3]);
			pstmt.setInt(6, good[4]);
			pstmt.setInt(7, good[5]);
			pstmt.setInt(8, good[6]);
			pstmt.setInt(9, good[7]);
			pstmt.setInt(10, bean.getPrices());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchFans getFansBean(ResultSet rs) throws SQLException{
		MatchFans bean = new MatchFans();
		bean.setUserId(rs.getInt("user_id"));
		int good[] = new int[8];
		good[0] = rs.getInt("good0");
		good[1] = rs.getInt("good1");
		good[2] = rs.getInt("good2");
		good[3] = rs.getInt("good3");
		good[4] = rs.getInt("good4");
		good[5] = rs.getInt("good5");
		good[6] = rs.getInt("good6");
		good[7] = rs.getInt("good7");
		bean.setGood(good);
		bean.setPrices(rs.getInt("prices"));
		return bean;
	}
	

	public MatchRes getMatchRes(String cond){
		MatchRes bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_res where " + cond);
		try {
			if (rs.next()){
				bean = getResBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getResList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_res where " + cond);
		try {
			while (rs.next()){
				list.add(getResBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public HashMap getResMap(String cond){
		HashMap map = new HashMap();
		MatchRes res = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_res where " + cond);
		try {
			while (rs.next()){
				res = getResBean(rs);
				map.put(new Integer(res.getId()), res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return map;
	}
	
	public boolean addRes(MatchRes bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_res(res_name,prices,point,cur,photo) values (?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getResName());
			pstmt.setInt(2, bean.getPrices());
			pstmt.setInt(3, bean.getPoint());
			pstmt.setInt(4, bean.getCur());
			pstmt.setString(5, bean.getPhoto());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchRes getResBean(ResultSet rs) throws SQLException{
		MatchRes bean = new MatchRes();
		bean.setId(rs.getInt("id"));
		bean.setResName(rs.getString("res_name"));
		bean.setPrices(rs.getInt("prices"));
		bean.setPoint(rs.getInt("point"));
		bean.setCur(rs.getInt("cur"));
		bean.setPhoto(rs.getString("photo"));
		return bean;
	}
	
	
	public MatchTrends getMatchTrends(String cond){
		MatchTrends bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_trends where " + cond);
		try {
			if (rs.next()){
				bean = getTrendsBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getTrendsList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_trends where " + cond);
		try {
			while (rs.next()){
				list.add(getTrendsBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addTrends(MatchTrends bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_trends (left_uid,right_uid,content,link,readed,create_time,flag) values (?,?,?,?,?,now(),?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getLeftUid());
			pstmt.setInt(2, bean.getRightUid());
			pstmt.setString(3, bean.getContent());
			pstmt.setString(4, bean.getLink());
			pstmt.setInt(5, bean.getReaded());
			pstmt.setInt(6, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchTrends getTrendsBean(ResultSet rs) throws SQLException{
		MatchTrends bean = new MatchTrends();
		bean.setId(rs.getInt("id"));
		bean.setLeftUid(rs.getInt("left_uid"));
		bean.setRightUid(rs.getInt("right_uid"));
		bean.setContent(rs.getString("content"));
		bean.setLink(rs.getString("link"));
		bean.setReaded(rs.getInt("readed"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public MatchUser getMatchUser(String cond){
		MatchUser bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_user where " + cond);
		try {
			if (rs.next()){
				bean = getUserBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getMatchUserList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_user where " + cond);
		try {
			while (rs.next()){
				list.add(getUserBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	/**
	 * 添加用户.如果用户不存在就插入新记录,若存在就更新一下参赛照片.
	 * 如果失败，返回0.成功更新记录，返回1，成功插入记录，返回2.
	 */
	public int addUser(MatchUser bean){
		DbOperation db = new DbOperation(5);
		// 先检查该记录是否存在,不存在再insert
		MatchUser bean2 = getMatchUser(" user_id=" + bean.getUserId());
		if (bean2 != null){
			db.executeUpdate("update match_user set photo='" + StringUtil.toSql(bean.getPhoto()) + "',photo2='" + bean.getPhoto2() + "',checked=0,photo_from=" + bean.getPhotoFrom() + " where user_id=" + bean.getUserId());
			// 如果所在地被重置为0了，就更新所在地
			if (bean2.getPlaceId() == 0){
				db.executeUpdate("update match_user set place_id=" + bean.getPlaceId() + " where user_id=" + bean.getUserId());
			}
			db.release();
			return 1;
		}
		String query = "insert into match_user (user_id,vote_count,consume,photo,photo2,del,checked,create_time,enounce,photo_from,cons_count,good0,good1,good2,good3,good4,good5,good6,good7,total_vote,total_consume,place_id,area_id) values (?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return 0;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getVoteCount());
			pstmt.setInt(3, bean.getConsume());
			pstmt.setString(4, bean.getPhoto());
			pstmt.setString(5, bean.getPhoto2());
			pstmt.setInt(6, bean.getDel());
			pstmt.setInt(7, 0);	//未通过
			pstmt.setString(8, bean.getEncounce());
			pstmt.setInt(9, bean.getPhotoFrom());
			pstmt.setInt(10, bean.getConsCount());
			int good[] = bean.getGood();
			pstmt.setInt(11, good[0]);
			pstmt.setInt(12, good[1]);
			pstmt.setInt(13, good[2]);
			pstmt.setInt(14, good[3]);
			pstmt.setInt(15, good[4]);
			pstmt.setInt(16, good[5]);
			pstmt.setInt(17, good[6]);
			pstmt.setInt(18, good[7]);
			pstmt.setInt(19, bean.getTotalVote());
			pstmt.setInt(20, bean.getTotalConsume());
			pstmt.setInt(21, bean.getPlaceId());
			pstmt.setInt(22, bean.getAreaId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			db.release();
		}
		return 2;
	}
	
	MatchUser getUserBean(ResultSet rs) throws SQLException{
		MatchUser bean = new MatchUser();
		bean.setUserId(rs.getInt("user_id"));
		bean.setVoteCount(rs.getInt("vote_count"));
		bean.setConsume(rs.getInt("consume"));
		bean.setEncounce(rs.getString("enounce"));
		bean.setPhoto(rs.getString("photo"));
		bean.setPhoto2(rs.getString("photo2"));
		bean.setDel(rs.getInt("del"));
		bean.setChecked(rs.getInt("checked"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setPhotoFrom(rs.getInt("photo_from"));
		bean.setConsCount(rs.getInt("cons_count"));
		int good[] = new int[8];
		good[0] = rs.getInt("good0");
		good[1] = rs.getInt("good1");
		good[2] = rs.getInt("good2");
		good[3] = rs.getInt("good3");
		good[4] = rs.getInt("good4");
		good[5] = rs.getInt("good5");
		good[6] = rs.getInt("good6");
		good[7] = rs.getInt("good7");
		bean.setGood(good);
		bean.setTotalVote(rs.getInt("total_vote"));
		bean.setTotalConsume(rs.getInt("total_consume"));
		bean.setBlogCount(rs.getInt("blog_count"));
		bean.setPlaceId(rs.getInt("place_id"));
		bean.setAreaId(rs.getInt("area_id"));
		return bean;
	}
	

	public MatchVoted getMatchVoted(String cond){
		MatchVoted bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_voted where " + cond);
		try {
			if (rs.next()){
				bean = getVotedBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getVotedList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_voted where " + cond);
		try {
			while (rs.next()){
				list.add(getVotedBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addVoted(MatchVoted bean,String cond){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_voted (user_id,good0,good1,good2,good3,good4,good5,good6,good7,prices,vote_count) values (?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE " + cond;
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			int good[] = bean.getGood();
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, good[0]);
			pstmt.setInt(3, good[1]);
			pstmt.setInt(4, good[2]);
			pstmt.setInt(5, good[3]);
			pstmt.setInt(6, good[4]);
			pstmt.setInt(7, good[5]);
			pstmt.setInt(8, good[6]);
			pstmt.setInt(9, good[7]);
			pstmt.setInt(10, bean.getPrices());
			pstmt.setInt(11, bean.getVoteCount());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchVoted getVotedBean(ResultSet rs) throws SQLException{
		MatchVoted bean = new MatchVoted();
		bean.setUserId(rs.getInt("user_id"));
		int good[] = new int[8];
		good[0] = rs.getInt("good0");
		good[1] = rs.getInt("good1");
		good[2] = rs.getInt("good2");
		good[3] = rs.getInt("good3");
		good[4] = rs.getInt("good4");
		good[5] = rs.getInt("good5");
		good[6] = rs.getInt("good6");
		good[7] = rs.getInt("good7");
		bean.setGood(good);
		bean.setPrices(rs.getInt("prices"));
		return bean;
	}
	
//	public MatchTodayVote getMatchTodayVote(String cond){
//		MatchTodayVote bean = null;
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select * from match_today_vote where " + cond);
//		try {
//			if (rs.next()){
//				bean = getTodayVote(rs);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return bean;
//	}
//	
//	public List getTodayVotedList(String cond){
//		List list = new ArrayList();
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select * from match_today_vote where " + cond);
//		try {
//			while (rs.next()){
//				list.add(getTodayVote(rs));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return list;
//	}
//	
//	public boolean addTodayVote(MatchTodayVote bean){
//		DbOperation db = new DbOperation(5);
//		String query = "insert into match_today_vote (user_id,vote_count,vote_time) values (?,?,now())";
//		if(!db.prepareStatement(query)) {
//			db.release();
//			return false;
//		}
//		PreparedStatement pstmt = db.getPStmt();
//		try{
//			pstmt.setInt(1, bean.getUserId());
//			pstmt.setInt(2, bean.getVoteCount());
//			pstmt.execute();
//		}catch(SQLException e) {
//			e.printStackTrace();
//			return false;
//		}finally{
//			db.release();
//		}
//		return true;
//	}
//	
//	MatchTodayVote getTodayVote(ResultSet rs) throws SQLException{
//		MatchTodayVote bean = new MatchTodayVote();
//		bean.setId(rs.getInt("id"));
//		bean.setUserId(rs.getInt("user_id"));
//		bean.setVoteCount(rs.getInt("vote_count"));
//		bean.setVoteTime(rs.getTimestamp("vote_time").getTime());
//		return bean;
//	}
	
	public MatchPhotoHistory getMatchPhotoHistory(String cond){
		MatchPhotoHistory bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_photo_history where " + cond);
		try {
			if (rs.next()){
				bean = getMatchPhotoHistory(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getPhotoHistoryList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_photo_history where " + cond);
		try {
			while (rs.next()){
				list.add(getMatchPhotoHistory(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addPhotoHistory(MatchPhotoHistory bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_photo_history (user_id,photo,create_time,photo_from,photo2) values (?,?,now(),?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getPhoto());
			pstmt.setInt(3, bean.getPhotoFrom());
			pstmt.setString(4, bean.getPhoto2());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchPhotoHistory getMatchPhotoHistory(ResultSet rs) throws SQLException{
		MatchPhotoHistory bean = new MatchPhotoHistory();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setPhoto(rs.getString("photo"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setPhotoFrom(rs.getInt("photo_from"));
		bean.setPhoto2(rs.getString("photo2"));
		return bean;
	}
	
	public MatchEnouHistory getMatchEnouHistory(String cond){
		MatchEnouHistory bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_enou_history where " + cond);
		try {
			if (rs.next()){
				bean = getMatchEnouHistory(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getEnouHistoryList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_enou_history where " + cond);
		try {
			while (rs.next()){
				list.add(getMatchEnouHistory(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addEnouHistory(MatchEnouHistory bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_enou_history (user_id,enounce,create_time) values (?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getEnounce());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchEnouHistory getMatchEnouHistory(ResultSet rs) throws SQLException{
		MatchEnouHistory bean = new MatchEnouHistory();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setEnounce(rs.getString("enounce"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
	
	public MatchFansRank getMatchFansRank(String cond){
		MatchFansRank bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_fans_rank where " + cond);
		try {
			if (rs.next()){
				bean = getFansRankBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getFansRankList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_fans_rank where " + cond);
		try {
			while (rs.next()){
				list.add(getFansRankBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	
	// = =||估计用不到这个方法
	public boolean addFansRank(MatchFansRank bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_fans_rank(user_id,vote_count,prices) values (?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getVoteCount());
			pstmt.setInt(3, bean.getPrices());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchFansRank getFansRankBean(ResultSet rs) throws SQLException{
		MatchFansRank bean = new MatchFansRank();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setVoteCount(rs.getInt("vote_count"));
		bean.setPrices(rs.getInt("prices"));
		return bean;
	}
	
	public MatchFansAb getMatchFansAb(String cond){
		MatchFansAb bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_fans_ab where " + cond);
		try {
			if (rs.next()){
				bean = getMatchFansAb(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getFansAbList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_fans_ab where " + cond);
		try {
			while (rs.next()){
				list.add(getMatchFansAb(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addFansAb(MatchFansAb bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_fans_ab (left_uid,right_uid,good0,good1,good2,good3,good4,good5,good6,good7,prices) values (?,?,?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			int good[] = bean.getGood();
			pstmt.setInt(1, bean.getLeftUid());
			pstmt.setInt(2, bean.getRightUid());
			pstmt.setInt(3, good[0]);
			pstmt.setInt(4, good[1]);
			pstmt.setInt(5, good[2]);
			pstmt.setInt(6, good[3]);
			pstmt.setInt(7, good[4]);
			pstmt.setInt(8, good[5]);
			pstmt.setInt(9, good[6]);
			pstmt.setInt(10, good[7]);
			pstmt.setInt(11, bean.getPrices());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchFansAb getMatchFansAb(ResultSet rs) throws SQLException{
		MatchFansAb bean = new MatchFansAb();
		bean.setId(rs.getInt("id"));
		bean.setLeftUid(rs.getInt("left_uid"));
		bean.setRightUid(rs.getInt("right_uid"));
		int good[] = new int[8];
		good[0] = rs.getInt("good0");
		good[1] = rs.getInt("good1");
		good[2] = rs.getInt("good2");
		good[3] = rs.getInt("good3");
		good[4] = rs.getInt("good4");
		good[5] = rs.getInt("good5");
		good[6] = rs.getInt("good6");
		good[7] = rs.getInt("good7");
		bean.setGood(good);
		bean.setPrices(rs.getInt("prices"));
		return bean;
	}
	
	public MatchFansAb2 getMatchFansAb2(String cond){
		MatchFansAb2 bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_fans_ab2 where " + cond);
		try {
			if (rs.next()){
				bean = getMatchFansAb2(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getFansAbList2(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_fans_ab2 where " + cond);
		try {
			while (rs.next()){
				list.add(getMatchFansAb(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addFansAb2(MatchFansAb2 bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_fans_ab2 (left_uid,right_uid,good0,good1,good2,good3,good4,good5,good6,good7,prices) values (?,?,?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			int good[] = bean.getGood();
			pstmt.setInt(1, bean.getLeftUid());
			pstmt.setInt(2, bean.getRightUid());
			pstmt.setInt(3, good[0]);
			pstmt.setInt(4, good[1]);
			pstmt.setInt(5, good[2]);
			pstmt.setInt(6, good[3]);
			pstmt.setInt(7, good[4]);
			pstmt.setInt(8, good[5]);
			pstmt.setInt(9, good[6]);
			pstmt.setInt(10, good[7]);
			pstmt.setInt(11, bean.getPrices());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchFansAb2 getMatchFansAb2(ResultSet rs) throws SQLException{
		MatchFansAb2 bean = new MatchFansAb2();
		bean.setId(rs.getInt("id"));
		bean.setLeftUid(rs.getInt("left_uid"));
		bean.setRightUid(rs.getInt("right_uid"));
		int good[] = new int[8];
		good[0] = rs.getInt("good0");
		good[1] = rs.getInt("good1");
		good[2] = rs.getInt("good2");
		good[3] = rs.getInt("good3");
		good[4] = rs.getInt("good4");
		good[5] = rs.getInt("good5");
		good[6] = rs.getInt("good6");
		good[7] = rs.getInt("good7");
		bean.setGood(good);
		bean.setPrices(rs.getInt("prices"));
		return bean;
	}
	
	public MatchTopTrends getTopTrends(String cond){
		MatchTopTrends bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_top_trends where " + cond);
		try {
			if (rs.next()){
				bean = getTopTrends(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public LinkedHashMap getTopTrendsMap(String cond){
		MatchTopTrends topTrendsBean = null;
		LinkedHashMap map = new LinkedHashMap();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_top_trends where " + cond);
		try {
			while (rs.next()){
				topTrendsBean = getTopTrends(rs);
				map.put(new Integer(topTrendsBean.getId()), topTrendsBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return map;
	}
	
	public int addTopTrends(MatchTopTrends bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into match_top_trends(content,links) values (?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getContent());
			pstmt.setString(2, bean.getLinks());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	MatchTopTrends getTopTrends(ResultSet rs) throws SQLException{
		MatchTopTrends bean = new MatchTopTrends();
		bean.setId(rs.getInt("id"));
		bean.setContent(rs.getString("content"));
		bean.setLinks(rs.getString("links"));
		return bean;
	}
	
	public MatchReco getReco(String cond){
		MatchReco bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_reco where " + cond);
		try {
			if (rs.next()){
				bean = getReco(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getRecoList(String cond){
		MatchReco bean = null;
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_reco where " + cond);
		try {
			while (rs.next()){
				bean = getReco(rs);
				list.add(new Integer(bean.getUserId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addReco(MatchReco bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into match_reco(user_id) values (?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	MatchReco getReco(ResultSet rs) throws SQLException{
		MatchReco bean = new MatchReco();
		bean.setUserId(rs.getInt("user_id"));
		return bean;
	}
	
	public MatchBuylog getBuyLog(String cond){
		MatchBuylog bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_buylog where " + cond);
		try {
			if (rs.next()){
				bean = getBuylog(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getBuyLogList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_buylog where " + cond);
		try {
			while (rs.next()){
				list.add(getBuylog(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addBuyLog(MatchBuylog bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_buylog(user_id,res_id,res_name,count,prices,cur,buy_time) values (?,?,?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getResId());
			pstmt.setString(3, bean.getResName());
			pstmt.setInt(4, bean.getCount());
			pstmt.setInt(5, bean.getPrices());
			pstmt.setInt(6, bean.getCur());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchBuylog getBuylog(ResultSet rs) throws SQLException{
		MatchBuylog bean = new MatchBuylog();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setResId(rs.getInt("res_id"));
		bean.setResName(rs.getString("res_name"));
		bean.setCount(rs.getInt("count"));
		bean.setPrices(rs.getInt("prices"));
		bean.setCur(rs.getInt("cur"));
		bean.setBuyTime(rs.getTimestamp("buy_time").getTime());
		return bean;
	}
	
	public MatchGood getGood(String cond){
		MatchGood bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_good where " + cond);
		try {
			if (rs.next()){
				bean = getGood(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getGoodList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_good where " + cond);
		try {
			while (rs.next()){
				list.add(getGood(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public HashMap getGoodMap(String cond){
		MatchGood matchGood = null;
		HashMap map = new HashMap();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_good where " + cond);
		try {
			while (rs.next()){
				matchGood = getGood(rs);
				map.put(new Integer(matchGood.getId()), matchGood);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return map;
	}
	
	public int addGood(MatchGood bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into match_good(good_name,price,photo,count,`describe`,flag,price2,hide,prio,count_now,buy_count) values (?,?,?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getGoodName());
			pstmt.setInt(2, bean.getPrice());
			pstmt.setString(3, bean.getPhoto());
			pstmt.setInt(4, bean.getCount());
			pstmt.setString(5, bean.getDescribe());
			pstmt.setInt(6, bean.getFlag());
			pstmt.setInt(7, bean.getPrice2());
			pstmt.setInt(8, bean.getHide());
			pstmt.setInt(9, bean.getPrio());
			pstmt.setInt(10, bean.getCountNow());
			pstmt.setInt(11, bean.getBuyCount());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	public boolean modifyGood(MatchGood bean){
		DbOperation db = new DbOperation(5);
		String query = "update match_good set good_name=?,price=?,photo=?,count=?,count_now=?,`describe`=?,flag=?,price2=?,prio=? where id=" + bean.getId();
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getGoodName());
			pstmt.setInt(2, bean.getPrice());
			pstmt.setString(3, bean.getPhoto());
			pstmt.setInt(4, bean.getCount());
			pstmt.setInt(5, bean.getCountNow());
			pstmt.setString(6, bean.getDescribe());
			pstmt.setInt(7, bean.getFlag());
			pstmt.setInt(8, bean.getPrice2());
			pstmt.setInt(9, bean.getPrio());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchGood getGood(ResultSet rs) throws SQLException{
		MatchGood bean = new MatchGood();
		bean.setId(rs.getInt("id"));
		bean.setGoodName(rs.getString("good_name"));
		bean.setPrice(rs.getInt("price"));
		bean.setPrice2(rs.getInt("price2"));
		bean.setPhoto(rs.getString("photo"));
		bean.setCount(rs.getInt("count"));
		bean.setDescribe(rs.getString("describe"));
		bean.setFlag(rs.getInt("flag"));
		bean.setHide(rs.getInt("hide"));
		bean.setPrio(rs.getInt("prio"));
		bean.setCountNow(rs.getInt("count_now"));
		bean.setBuyCount(rs.getInt("buy_count"));
		return bean;
	}
	
	public MatchExch getExch(String cond){
		MatchExch bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_exch where " + cond);
		try {
			if (rs.next()){
				bean = getExch(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getExchList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_exch where " + cond);
		try {
			while (rs.next()){
				list.add(getExch(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean addExch(MatchExch bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into match_exch(user_id,good_id,buy_time,good_name,vote,order_id) values (?,?,now(),?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getGoodId());
			pstmt.setString(3, bean.getGoodName());
			pstmt.setInt(4, bean.getVote());
			pstmt.setInt(5, bean.getOrderId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	MatchExch getExch(ResultSet rs) throws SQLException{
		MatchExch bean = new MatchExch();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setGoodId(rs.getInt("good_id"));
		bean.setBuyTime(rs.getTimestamp("buy_time").getTime());
		bean.setGoodName(rs.getString("good_name"));
		bean.setVote(rs.getInt("vote"));
		bean.setOrderId(rs.getInt("order_id"));
		return bean;
	}
	
	public MatchOrder getOrder(String cond){
		MatchOrder bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_order where " + cond);
		try {
			if (rs.next()){
				bean = getOrder(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getOrderList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_order where " + cond);
		try {
			while (rs.next()){
				list.add(getOrder(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addOrder(MatchOrder bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into match_order(user_id,phone,user_name,address,good_name,good_id,buy_time,send_time,price,actual_price) values (?,?,?,?,?,?,now(),?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getPhone());
			pstmt.setString(3, bean.getUserName());
			pstmt.setString(4, bean.getAddress());
			pstmt.setString(5, bean.getGoodName());
			pstmt.setInt(6, bean.getGoodId());
			pstmt.setString(7, DateUtil.formatSqlDatetime(bean.getSendTime()));
			pstmt.setInt(8, bean.getPrice());
			pstmt.setInt(9, bean.getActualPrice());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	MatchOrder getOrder(ResultSet rs) throws SQLException{
		MatchOrder bean = new MatchOrder();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setPhone(rs.getString("phone"));
		bean.setUserName(rs.getString("user_name"));
		bean.setAddress(rs.getString("address"));
		bean.setGoodName(rs.getString("good_name"));
		bean.setGoodId(rs.getInt("good_id"));
		bean.setBuyTime(rs.getTimestamp("buy_time").getTime());
		bean.setSendTime(rs.getTimestamp("send_time").getTime());
		bean.setPrice(rs.getInt("price"));
		bean.setActualPrice(rs.getInt("actual_price"));
		return bean;
	}
	
	public MatchInfo getInfo(String cond){
		MatchInfo bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_info where " + cond);
		try {
			if (rs.next()){
				bean = getInfo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getInfoList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_info where " + cond);
		try {
			while (rs.next()){
				list.add(getInfo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addInfo(MatchInfo bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into match_info(title,point_count,game_point_count,vote_count,start_time,end_time,flag,user_count,fans_count) values (?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getTitle());
			pstmt.setInt(2, bean.getPointCount());
			pstmt.setInt(3, bean.getGamePointCount());
			pstmt.setInt(4, bean.getVoteCount());
			pstmt.setString(5, bean.getStartTimeStr());
			pstmt.setString(6, bean.getEndTimeStr());
			pstmt.setInt(7, bean.getFalg());
			pstmt.setInt(8, bean.getUserCount());
			pstmt.setInt(9, bean.getFansCount());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	MatchInfo getInfo(ResultSet rs) throws SQLException{
		MatchInfo bean = new MatchInfo();
		bean.setId(rs.getInt("id"));
		bean.setTitle(rs.getString("title"));
		bean.setPointCount(rs.getInt("point_count"));
		bean.setGamePointCount(rs.getInt("game_point_count"));
		bean.setVoteCount(rs.getInt("vote_count"));
		bean.setStartTime(rs.getTimestamp("start_time").getTime());
		bean.setEndTime(rs.getTimestamp("end_time").getTime());
		bean.setFalg(rs.getInt("flag"));
		bean.setUserCount(rs.getInt("user_count"));
		bean.setFansCount(rs.getInt("fans_count"));
		return bean;
	}
	
	public MatchHistory getHistory(String cond){
		MatchHistory bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_history where " + cond);
		try {
			if (rs.next()){
				bean = getHistory(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getHistoryList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_history where " + cond);
		try {
			while (rs.next()){
				list.add(getHistory(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addHistory(MatchHistory bean){
		int lastId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into match_history(match_id,user_id,vote_count,rank,photo,photo2,photo_from,area_id) values (?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getMatchId());
			pstmt.setInt(2, bean.getUserId());
			pstmt.setInt(3, bean.getVoteCount());
			pstmt.setInt(4, bean.getRank());
			pstmt.setString(5, bean.getPhoto());
			pstmt.setString(6, bean.getPhoto2());
			pstmt.setInt(7, bean.getPhotoFrom());
			pstmt.setInt(8, bean.getAreaId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	MatchHistory getHistory(ResultSet rs) throws SQLException{
		MatchHistory bean = new MatchHistory();
		bean.setId(rs.getInt("id"));
		bean.setMatchId(rs.getInt("match_id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setVoteCount(rs.getInt("vote_count"));
		bean.setRank(rs.getInt("rank"));
		bean.setPhoto(rs.getString("photo"));
		bean.setPhoto2(rs.getString("photo2"));
		bean.setPhotoFrom(rs.getInt("photo_from"));
		bean.setAreaId(rs.getInt("area_id"));
		return bean;
	}
	
	public MatchArea getArea(String cond){
		MatchArea bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_area where " + cond);
		try {
			if (rs.next()){
				bean = getArea(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getAreaList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_area where " + cond);
		try {
			while (rs.next()){
				list.add(getArea(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public HashMap getAreaMap(String cond){
		HashMap map = new HashMap();
		MatchArea matchArea = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_area where " + cond);
		try {
			while (rs.next()){
				matchArea = getArea(rs);
				map.put(new Integer(matchArea.getId()), matchArea);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return map;
	}
	
	public int addArea(MatchArea bean){
		int lastInsertId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into match_area(area_name,citys,`describe`,create_time,provinces,count) values (?,?,?,now(),?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getAreaName());
			pstmt.setString(2, bean.getCitys());
			pstmt.setString(3, bean.getDescribe());
			pstmt.setString(4, bean.getProvinces());
			pstmt.setInt(5, bean.getCount());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastInsertId;
		}finally{
			lastInsertId = db.getLastInsertId();
			db.release();
		}
		return lastInsertId;
	}
	
	MatchArea getArea(ResultSet rs) throws SQLException{
		MatchArea bean = new MatchArea();
		bean.setId(rs.getInt("id"));
		bean.setAreaName(rs.getString("area_name"));
		bean.setCitys(rs.getString("citys"));
		bean.setDescribe(rs.getString("describe"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setProvinces(rs.getString("provinces"));
		bean.setCount(rs.getInt("count"));
		return bean;
	}

	
	public MatchAreaHistory getAreaHistory(String cond){
		MatchAreaHistory bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_area_history where " + cond);
		try {
			if (rs.next()){
				bean = getAreaHistory(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getAreaHistoryList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_area_history where " + cond);
		try {
			while (rs.next()){
				list.add(getAreaHistory(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addAreaHistory(MatchAreaHistory bean){
		int lastInsertId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into match_area_history(match_id,area_name,citys,`describe`,create_time,provinces,count,area_id) values (?,?,?,now(),?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getMatchId());
			pstmt.setString(2, bean.getAreaName());
			pstmt.setString(3, bean.getCitys());
			pstmt.setString(4, bean.getDescribe());
			pstmt.setString(5, bean.getProvinces());
			pstmt.setInt(6, bean.getCount());
			pstmt.setInt(7, bean.getAreaId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastInsertId;
		}finally{
			lastInsertId = db.getLastInsertId();
			db.release();
		}
		return lastInsertId;
	}
	
	MatchAreaHistory getAreaHistory(ResultSet rs) throws SQLException{
		MatchAreaHistory bean = new MatchAreaHistory();
		bean.setId(rs.getInt("id"));
		bean.setMatchId(rs.getInt("match_id"));
		bean.setAreaName(rs.getString("area_name"));
		bean.setCitys(rs.getString("citys"));
		bean.setDescribe(rs.getString("describe"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setProvinces(rs.getString("provinces"));
		bean.setCount(rs.getInt("count"));
		bean.setAreaId(rs.getInt("area_id"));
		return bean;
	}
	
	public MatchFocus getFocus(String cond){
		MatchFocus bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_focus where " + cond);
		try {
			if (rs.next()){
				bean = getFocus(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getFocusList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from match_focus where " + cond);
		try {
			while (rs.next()){
				list.add(getFocus(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addFocus(MatchFocus bean){
		int lastInsertId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into match_focus(uid,uid2,focus_time) values (?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUid());
			pstmt.setInt(2, bean.getUid2());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastInsertId;
		}finally{
			lastInsertId = db.getLastInsertId();
			db.release();
		}
		return lastInsertId;
	}
	
	MatchFocus getFocus(ResultSet rs) throws SQLException{
		MatchFocus bean = new MatchFocus();
		bean.setId(rs.getInt("id"));
		bean.setUid(rs.getInt("uid"));
		bean.setUid2(rs.getInt("uid2"));
		bean.setFocusTime(rs.getTimestamp("Focus_time").getTime());
		return bean;
	}
}