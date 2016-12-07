package jc.credit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.joycool.wap.util.db.DbOperation;

public class CreditService {

	public Property getProperty(String cond){
		Property bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_property where " + cond);
		try {
			if (rs.next()){
				bean = getProperty(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getPropertyList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_property where " + cond);
		try {
			while (rs.next()){
				list.add(getProperty(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public HashMap getPropertyMap(String cond){
		HashMap map = new HashMap();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_property where " + cond);
		try {
			while (rs.next()){
				map.put(Integer.valueOf(getProperty(rs).getId()),getProperty(rs).getContent());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return map;
	}
	
	Property getProperty(ResultSet rs) throws SQLException{
		Property bean = new Property();
		bean.setId(rs.getInt("id"));
		bean.setContent(rs.getString("content"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public UserInfo getUserInfo(String cond){
		UserInfo bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_info where " + cond);
		try {
			if (rs.next()){
				bean = getUserInfo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public boolean addNewInfo(UserInfo bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into credit_user_info (user_id,mobile,point,create_time,modify_time,users,flag) values (?,?,?,now(),now(),?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getMobile());
			pstmt.setInt(3, bean.getPoint());
			pstmt.setString(4, bean.getUsers());
			pstmt.setInt(5, bean.getFlag());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}

	public List getUserInfoList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_info where " + cond);
		try {
			while (rs.next()){
				list.add(getUserInfo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	UserInfo getUserInfo(ResultSet rs) throws SQLException{
		UserInfo bean = new UserInfo();
		bean.setUserId(rs.getInt("user_id"));
		bean.setMobile(rs.getString("mobile"));
		bean.setPoint(rs.getInt("point"));
		bean.setIntactPoint(rs.getInt("intact_point"));
		bean.setPhonePoint(rs.getInt("phone_point"));
		bean.setIdCardPoint(rs.getInt("id_card_point"));
		bean.setNetPoint(rs.getInt("net_point"));
		bean.setConPoint(rs.getInt("con_point"));
		bean.setPlayerPoint(rs.getInt("player_point"));
		bean.setPlayerCount(rs.getInt("player_count"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setModifyTime(rs.getTimestamp("modify_time").getTime());
		bean.setUsers(rs.getString("users"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public UserBase getUserBase(String cond){
		UserBase bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_base where " + cond);
		try {
			if (rs.next()){
				bean = getUserBase(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	public List getUserBaseList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_base where " + cond);
		try {
			while (rs.next()){
				list.add(getUserBase(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	/**
	 * 注意,此方法只返回UserBase的user_id
	 * @param cond
	 * @return user_id的List。注意UserId是字符串类型的,方便以后转换成int.
	 */
	public List searchUserBase(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select user_id from credit_user_base where " + cond);
		try {
			while (rs.next()){
				list.add(rs.getInt("user_id") + "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public boolean createUserBase(UserBase userBase){
		DbOperation db = new DbOperation(5);
		String query = "insert into credit_user_base (user_id,bir_year,bir_month,bir_day,bir_hour,stature,animals,astro,aim,gender) values (?,?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, userBase.getUserId());
			pstmt.setInt(2, userBase.getBirYear());
			pstmt.setInt(3, userBase.getBirMonth());
			pstmt.setInt(4, userBase.getBirDay());
			pstmt.setInt(5, userBase.getBirHour());
			pstmt.setInt(6, userBase.getStature());
			pstmt.setString(7, userBase.getAnimals());
			pstmt.setInt(8, userBase.getAstro());
			pstmt.setInt(9, userBase.getAim());
			pstmt.setInt(10, userBase.getGender());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	UserBase getUserBase(ResultSet rs) throws SQLException{
		UserBase bean = new UserBase();
		bean.setUserId(rs.getInt("user_id"));
		bean.setBirYear(rs.getInt("bir_year"));
		bean.setBirMonth(rs.getInt("bir_month"));
		bean.setBirDay(rs.getInt("bir_day"));
		bean.setProvince(rs.getInt("province"));
		bean.setCity(rs.getInt("city"));
		bean.setEducation(rs.getInt("education"));
		bean.setPersonality(rs.getInt("personality"));
		bean.setBlood(rs.getInt("blood"));
		bean.setPhoto(rs.getString("photo"));
		bean.setStature(rs.getInt("stature"));
		bean.setBirHour(rs.getInt("bir_hour"));
		bean.setAnimals(rs.getString("animals"));
		bean.setAstro(rs.getInt("astro"));
		bean.setPoint(rs.getInt("point"));
		bean.setAim(rs.getInt("aim"));
		bean.setGender(rs.getInt("gender"));
		return bean;
	}	
	
	public UserWork getUserWork(String cond){
		UserWork bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_work where " + cond);
		try {
			if (rs.next()){
				bean = getUserWork(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	UserWork getUserWork(ResultSet rs) throws SQLException{
		UserWork bean = new UserWork();
		bean.setUserId(rs.getInt("user_id"));
		bean.setIdentity(rs.getInt("identity"));
		bean.setTrade(rs.getInt("trade"));
		bean.setEarning(rs.getInt("earning"));
		bean.setDream(rs.getInt("dream"));
		bean.setPoint(rs.getInt("point"));
		return bean;
	}
	
	public UserLive getUserLive(String cond){
		UserLive bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_live where " + cond);
		try {
			if (rs.next()){
				bean = getUserLive(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	UserLive getUserLive(ResultSet rs) throws SQLException{
		UserLive bean = new UserLive();
		bean.setUserId(rs.getInt("user_id"));
		bean.setLoveTime(rs.getInt("love_time"));
		bean.setBill(rs.getInt("bill"));
		bean.setSmoke(rs.getInt("smoke"));
		bean.setFilmStart(rs.getString("film_start"));
		bean.setSinger(rs.getString("singer"));
		bean.setMarrage(rs.getInt("marrage"));
		bean.setChild(rs.getInt("child"));
		bean.setFood(rs.getString("food"));
		bean.setSport(rs.getString("sport"));
		bean.setFocusOn(rs.getString("focus_on"));
		bean.setGoodAt(rs.getString("good_at"));
		bean.setPoint(rs.getInt("point"));
		return bean;
	}
	
	public UserLooks getUserLooks(String cond){
		UserLooks bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_looks where " + cond);
		try {
			if (rs.next()){
				bean = getUserLooks(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	UserLooks getUserLooks(ResultSet rs) throws SQLException{
		UserLooks bean = new UserLooks();
		bean.setUserId(rs.getInt("user_id"));
		bean.setBodyType(rs.getInt("body_type"));
		bean.setLooks(rs.getInt("looks"));
		bean.setCharm(rs.getInt("charm"));
		bean.setPoint(rs.getInt("point"));
		return bean;
	}
	
	public UserContacts getUserContacts(String cond){
		UserContacts bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_contacts where " + cond);
		try {
			if (rs.next()){
				bean = getUserContacts(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	UserContacts getUserContacts(ResultSet rs) throws SQLException{
		UserContacts bean = new UserContacts();
		bean.setUserId(rs.getInt("user_id"));
		bean.setContacts(rs.getString("contacts"));
		bean.setIdCard(rs.getString("id_card"));
		bean.setAddress(rs.getString("address"));
		bean.setTrueName(rs.getString("true_name"));
		bean.setPrivateLevel(rs.getInt("private"));
		bean.setPoint(rs.getInt("point"));
		return bean;
	}
	
	public UserGrade getUserGrade(String cond){
		UserGrade bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_grade where " + cond);
		try {
			if (rs.next()){
				bean = getUserGrade(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getUserGradeList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_user_grade where " + cond);
		try {
			while (rs.next()){
				list.add(getUserGrade(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	UserGrade getUserGrade(ResultSet rs) throws SQLException{
		UserGrade bean = new UserGrade();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setUsers(rs.getString("users"));
		return bean;
	}
	
	public CreditProvince getProvince(String cond){
		CreditProvince bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_province where " + cond);
		try {
			if (rs.next()){
				bean = getProvince(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getProvinceList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_province where " + cond);
		try {
			while (rs.next()){
				list.add(getProvince(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	CreditProvince getProvince(ResultSet rs) throws SQLException{
		CreditProvince bean = new CreditProvince();
		bean.setId(rs.getInt("id"));
		bean.setProvince(rs.getString("province"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	public CreditCity getCity(String cond){
		CreditCity bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_city where " + cond);
		try {
			if (rs.next()){
				bean = getCity(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getCityList(String cond){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from credit_city where " + cond);
		try {
			while (rs.next()){
				list.add(getCity(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	CreditCity getCity(ResultSet rs) throws SQLException{
		CreditCity bean = new CreditCity();
		bean.setId(rs.getInt("id"));
		bean.setCity(rs.getString("city"));
		bean.setHypo(rs.getInt("hypo"));
		bean.setFlag(rs.getInt("flag"));
		return bean;
	}
	
	// 取得所在地字符串
//	public String getPlace(UserBase userBase){
//		String tmp = "";
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select concat(cp.province,',',cc.city) place from credit_province cp ,credit_city cc where cp.id=" + userBase.getProvince() + " and cc.id=" + userBase.getCity());
//		try {
//			while (rs.next()){
//				tmp = rs.getString("place");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return tmp;
//	}
	
//	// 取得省字符串
//	public String getProvinceString(String cond){
//		String tmp = "";
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select province from credit_province where " + cond);
//		try {
//			if (rs.next()){
//				tmp = rs.getString("province");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return tmp;
//	}
//	
//	// 取得城市字符串
//	public String getCityString(String cond){
//		String tmp = "";
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select city from credit_city where " + cond);
//		try {
//			if (rs.next()){
//				tmp = rs.getString("city");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return tmp;
//	}
}
