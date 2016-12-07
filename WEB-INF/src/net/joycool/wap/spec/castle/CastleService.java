/**
 * 2008-10-15
 * @author lhy
 */
package net.joycool.wap.spec.castle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class CastleService {

	private static CastleService castleService;
	
	public static synchronized CastleService getInstance(){
		if(castleService != null)
			return castleService;
		
		synchronized(CastleService.class) {
			if(castleService == null) {
				castleService = new CastleService();
			}
		}
		return castleService;
	}
	
	private CastleService(){}
	
	
	public static List loadAllBuildingResourceNeed(){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "select * from building_resource_need";
		try{
			ResultSet rs = db.executeQuery(query);
			
			while(rs.next()) {
				BuildingTBean tBean = new BuildingTBean();
				tBean.setBuildType(rs.getInt("build_type"));
				tBean.setFe(rs.getInt("fe"));
				tBean.setGrade(rs.getInt("grade"));
				tBean.setGrain(rs.getInt("grain"));
				tBean.setPeople(rs.getInt("people"));
				tBean.setTime(rs.getInt("build_time"));
				tBean.setWood(rs.getInt("wood"));
				tBean.setStone(rs.getInt("stone"));
				tBean.setValue(rs.getInt("value"));
				tBean.setCivil(rs.getInt("civil"));
				list.add(tBean);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	
	public static void main(String[] args) {
		BuildingTBean tBean = new BuildingTBean();
		for(int i = 1; i < 10;i++){
			tBean.setBuildType(i);
			for(int j = 0; j <= 9; j ++) {
				tBean.setGrade(j);
				//ResNeed.resourceNeed(tBean);
				addBuildingResourceNeed(tBean);
			}
		}
	}
	
	
	
	private static boolean addBuildingResourceNeed(BuildingTBean tBean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO building_resource_need(build_type, grade, people,wood,fe,grain,build_time,stone) values(?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, tBean.getBuildType());
			pstmt.setInt(2, tBean.getGrade());
			pstmt.setInt(3, tBean.getPeople());
			pstmt.setInt(4, tBean.getWood());
			pstmt.setInt(5, tBean.getFe());
			pstmt.setInt(6, tBean.getGrain());
			pstmt.setInt(7, tBean.getTime());
			pstmt.setInt(8, tBean.getStone());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	/**
	 * 取得建筑build_pos数组
	 * @param cid
	 * @return
	 */
	public int[] getBuildPos(int cid) {
		int[] buildPos = new int[ResNeed.castlePosCount + 1];
		DbOperation db = new DbOperation(5);
		String query = "SELECT build_type,build_pos FROM castle_building WHERE cid = " + cid;
		ResultSet rs = db.executeQuery(query);
		
		try {
			BuildingBean bean = null;
			while(rs.next()) {
				buildPos[rs.getInt("build_pos")] = rs.getInt("build_type");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return buildPos;
		}finally{
			db.release();
		}
		
		return buildPos;
	}
	// 取得所有建筑建造的数量
	public int[] getBuildingCountByType(String cond) {
		int[] count = new int[ResNeed.castlePosCount + 1];
		DbOperation db = new DbOperation(5);
		String query = "SELECT build_type,count(id) FROM castle_building WHERE " + cond
					+ " group by build_type";
		ResultSet rs = db.executeQuery(query);
		
		try {
			BuildingBean bean = null;
			while(rs.next()) {
				count[rs.getInt(1)] = rs.getInt(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		
		return count;
	}
	
	
	public boolean updateCityName(int cid, String castleName) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle SET castle_name = ? WHERE id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, castleName);
			pstmt.setInt(2, cid);
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	
	/**
	 * 增加一条消息，比如你的伐木场升级完成
	 * @param msg
	 * @return
	 */
	public boolean addCastleMessage(CastleMessage msg) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle_message(uid, content,type,tong_id,detail,pos, time) values(?,?,?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, msg.getUid());
			pstmt.setString(2, msg.getContent());
			pstmt.setInt(3, msg.getType());
			pstmt.setInt(4, msg.getTongId());
			pstmt.setString(5, msg.getDetail());
			pstmt.setInt(6, msg.getPos());
			pstmt.execute();
			msg.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	/**
	 * 得到某个用户的城堡信息
	 * @param uid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getCastleMessageByUid(int uid, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_message WHERE uid = ? order by id desc limit ?,?";
		if (!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		try {
			db.getPStmt().setInt(1, uid);
			db.getPStmt().setInt(2, start);
			db.getPStmt().setInt(3, limit);
			ResultSet rs = db.getPStmt().executeQuery();
			while(rs.next()) {
				list.add(getMessage(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		
		return list;
	}
	
	public LinkedList getTongReports(int tongId, int start, int limit) {
		LinkedList list = new LinkedList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_message WHERE tong_id =" + tongId + " and type=2 order by id desc limit " + start + "," + limit;

		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getMessage(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		
		return list;
	}
	public List getCastleMessageList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_message WHERE " + cond;

		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getMessage(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		
		return list;
	}
	public List getCastleMessageListIndexPos(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_message use index(posidx) WHERE " + cond;

		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getMessage(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		
		return list;
	}
	
	public CastleMessage getCastleMessage(String cond) {
		CastleMessage bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_message WHERE " + cond;

		try {
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				bean = getMessage2(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		
		
		return bean;
	}
	
	private static CastleMessage getMessage(ResultSet rs) throws SQLException {
		CastleMessage bean = new CastleMessage(rs.getString("content"), rs.getInt("id"), rs.getTimestamp("time"), rs.getInt("uid"));
		bean.setType(rs.getInt("type"));
		bean.setTongId(rs.getInt("tong_id"));
		return bean;
	}
	private static CastleMessage getMessage2(ResultSet rs) throws SQLException {
		CastleMessage bean = new CastleMessage(rs.getString("content"), rs.getInt("id"), rs.getTimestamp("time"), rs.getInt("uid"));
		bean.setDetail(rs.getString("detail"));
		bean.setType(rs.getInt("type"));
		bean.setTongId(rs.getInt("tong_id"));
		return bean;
	}
	
	public int getCountMessageByUid(int uid) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "SELECT count(*) as count FROM castle_message WHERE uid = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return count;
		}
		try {
			db.getPStmt().setInt(1, uid);
			ResultSet rs = db.getPStmt().executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		
		return count;
	}
	
	
	public List getAllCacheCastle() {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "SELECT id, x, y FROM castle";
		
		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getXYCastleBean(rs));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	/**
	 * 根据x,y坐标获取城堡
	 * @param x
	 * @param y
	 * @return
	 */
	public CastleBean getCastleByXY(int x, int y) {
		CastleBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle where x = " + x + " and y = " + y;
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				bean = getCastleBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	/**
	 * 获得x,y坐标的周围玩家
	 * @param x
	 * @param y
	 * @return
	 */
	public List getAroundUser(int x, int y) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM `castle` c where (x between ? - 1 and ? + 1) and (y between ? - 1 and ? + 1)";
		if (!db.prepareStatement(query)) {
			db.release();
			return list;
		}
		try {
			db.getPStmt().setInt(1, x);
			db.getPStmt().setInt(2, x);
			db.getPStmt().setInt(3, y);
			db.getPStmt().setInt(4, y);
			ResultSet rs = db.getPStmt().executeQuery();
			
			while(rs.next()) {
				CastleBean bean = new CastleBean();
				bean.setCastleName(rs.getString("castle_name"));
				bean.setMap(rs.getString("map"));
				bean.setUid(rs.getInt("uid"));
				bean.setX(rs.getInt("x"));
				bean.setY(rs.getInt("y"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}

	
	/**
	 * 得到城市信息
	 * @param uid
	 * @return
	 */
	public CastleBean getCastle(String cond){
		CastleBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getCastleBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		
		return bean;
	}
	
	
	public List getCastleList(int uid) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle where uid = " + uid;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getCastleBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	private CastleBean getXYCastleBean(ResultSet rs)throws SQLException{
		CastleBean bean = new CastleBean();
		bean.setId(rs.getInt("id"));
		bean.setX(rs.getInt("x"));
		bean.setY(rs.getInt("y"));
		return bean;
	}
	
	private CastleBean getCastleBean(ResultSet rs) throws SQLException{
		CastleBean bean = new CastleBean();
		bean.setId(rs.getInt("id"));
		bean.setUid(rs.getInt("uid"));
		bean.setCastleName(rs.getString("castle_name"));
		bean.setX(rs.getInt("x"));
		bean.setY(rs.getInt("y"));
		bean.setMap(rs.getString("map"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setExpand(rs.getInt("expand"));
		bean.setExpand2(rs.getInt("expand2"));
		bean.setRace(rs.getInt("race"));
		bean.setType(rs.getInt("type"));
		return bean;
	}
	
	public CastleUserBean getCastleUser(int uid){
		CastleUserBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_user where uid = " + uid;
		ResultSet rs = db.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getCastleUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		return bean;
	}

	private CastleUserBean getCastleUser(ResultSet rs) throws SQLException{
		CastleUserBean bean = new CastleUserBean();
		bean.setUid(rs.getInt("uid"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setMain(rs.getInt("main"));
		bean.setCur(rs.getInt("cur"));
		
		bean.setCivil(rs.getInt("civil"));
		bean.setCivilTime(rs.getLong("civil_time"));
		bean.setCivilSpeed(rs.getInt("civil_speed"));
		bean.setFlag(rs.getInt("flag"));
		bean.setCastleCount(rs.getInt("castle_count"));
		bean.setProtectTime(rs.getTimestamp("protect_time").getTime());
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setSpTime(rs.getTimestamp("sp_time").getTime());
		bean.setLockTime(rs.getTimestamp("lock_time").getTime());

		bean.setTong(rs.getInt("tong"));
		bean.setQuest(rs.getInt("quest"));
		bean.setQuestStatus(rs.getInt("quest_status"));
		
		bean.setGold(rs.getInt("gold"));
		bean.setPeople(rs.getInt("people"));
		bean.setRace(rs.getInt("race"));
		
		bean.setAttackTotal(rs.getLong("attack_total"));
		bean.setDefenseTotal(rs.getLong("defense_total"));
		bean.setRobTotal(rs.getLong("rob_total"));
		
		bean.setAttackWeek(rs.getInt("attack_week"));
		bean.setDefenseWeek(rs.getInt("defense_week"));
		bean.setRobWeek(rs.getInt("rob_week"));
		return bean;
	}
	
	/**
	 * 加入一个新城市
	 * @param castleBean
	 * @return
	 */
	public boolean addCastle(CastleBean castleBean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle(uid, castle_name, x, y, race,type,map, protected_time,create_time) values(?,?,?,?,?,?,?,date_add(now(),interval 3 day),now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, castleBean.getUid());
			pstmt.setString(2, castleBean.getCastleName());
			pstmt.setInt(3, castleBean.getX());
			pstmt.setInt(4, castleBean.getY());
			pstmt.setInt(5, castleBean.getRace());
			pstmt.setInt(6, castleBean.getType());
			pstmt.setString(7, castleBean.getMap());
			pstmt.execute();
			castleBean.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	public boolean updateCastle(CastleBean castleBean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle set type=?,race=?,castle_name=?,x=?,y=?,expand=?,expand2=? where id=" + castleBean.getId();
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, castleBean.getType());
			pstmt.setInt(2, castleBean.getRace());
			pstmt.setString(3, castleBean.getCastleName());
			pstmt.setInt(4, castleBean.getX());
			pstmt.setInt(5, castleBean.getY());
			pstmt.setInt(6, castleBean.getExpand());
			pstmt.setInt(7, castleBean.getExpand2());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	/**
	 * 根据用户id取得该用户的资源
	 * @param uid
	 * @return
	 */
	public UserResBean getUserRes(int id) {
		UserResBean bean = null;
		DbOperation db = new DbOperation(5);		
		String query = "SELECT * FROM castle_user_resource where id = " + id;
		ResultSet rs = db.executeQuery(query);		
		try {
			if (rs.next()) {
				bean = getUserRes(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		return bean;
	}
	
	private UserResBean getUserRes(ResultSet rs) throws SQLException{
		UserResBean bean = new UserResBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("uid"));
		bean.setTime(rs.getLong("time"));
		bean.setWood(rs.getInt("wood"));
		bean.setWoodSpeed(rs.getInt("wood_speed"));
		bean.setOtherWoodSpeed(rs.getInt("other_wood_speed"));
		bean.setFe(rs.getInt("fe"));
		bean.setFeSpeed(rs.getInt("fe_speed"));
		bean.setOtherFeSpeed(rs.getInt("other_fe_speed"));
		bean.setGrain(rs.getInt("grain"));
		bean.setGrainSpeed(rs.getInt("grain_speed"));
		bean.setOtherGrainSpeed(rs.getInt("other_grain_speed"));
		bean.setStone(rs.getInt("stone"));
		bean.setStoneSpeed(rs.getInt("stone_speed"));
		bean.setOtherStoneSpeed(rs.getInt("other_stone_speed"));
		bean.setCivil(rs.getInt("civil"));
		bean.setMaxRes(rs.getInt("max_res"));
		bean.setMaxGrain(rs.getInt("max_grain"));
		bean.setMaxPeople(rs.getInt("people"));
		bean.setCave(rs.getInt("cave"));
		bean.setPeople2(rs.getInt("people2"));
		bean.setPeople(rs.getInt("people"));
		bean.setWall(rs.getInt("wall"));
		bean.setMerchant(rs.getShort("merchant"));
		bean.setFlag(rs.getInt("flag"));
		bean.setTrap(rs.getInt("trap"));
		
		bean.setLoyalTime(rs.getLong("loyal_time"));
		bean.setLoyal(rs.getInt("loyal"));
		bean.setLoyalSpeed(rs.getInt("loyal_speed"));
		return bean;
	}
	/**
	 * 新增用户资源
	 * @param bean
	 * @return
	 */
	public boolean addUserRes(UserResBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle_user_resource(uid, time, wood, wood_speed, other_wood_speed, fe, fe_speed, other_fe_speed, grain, grain_speed, other_grain_speed,stone,stone_speed,other_stone_speed,id,people,civil) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setLong(2, bean.getTime());
			pstmt.setInt(3, bean.getWood());
			pstmt.setInt(4, bean.getWoodSpeed());
			pstmt.setInt(5, bean.getOtherWoodSpeed());
			pstmt.setInt(6, bean.getFe());
			pstmt.setInt(7, bean.getFeSpeed());
			pstmt.setInt(8, bean.getOtherFeSpeed());
			pstmt.setInt(9, bean.getGrain());
			pstmt.setInt(10, bean.getGrainSpeed());
			pstmt.setInt(11, bean.getOtherGrainSpeed());
			pstmt.setInt(12, bean.getStone());
			pstmt.setInt(13, bean.getStoneSpeed());
			pstmt.setInt(14, bean.getOtherStoneSpeed());
			pstmt.setInt(15, bean.getId());
			pstmt.setInt(16, bean.getPeople());
			pstmt.setInt(17, bean.getCivil());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean addCastleUser(CastleUserBean bean) {
		DbOperation db = new DbOperation(5);
		// 注意，这里保存了14天的新手保护，在CastleUserBean中的protectInterval也应该同时修改
		String query = "INSERT INTO castle_user(uid, name, people,castle_count,race,protect_time, create_time,lock_time,civil_time) values(?,?,?,?,?,date_add(now(),interval " + CastleUtil.PROTECT_DAY + " day), now(),date_add(now(),interval 7 day),?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUid());
			pstmt.setString(2, bean.getName());
			pstmt.setInt(3, bean.getPeople());
			pstmt.setInt(4, bean.getCastleCount());
			pstmt.setInt(5, bean.getRace());
			pstmt.setLong(6, bean.getCivilTime());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	public boolean addOasis(OasisBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle_oasis(id,type,create_time,update_time) values(?,?,now(),now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getId());
			pstmt.setInt(2, bean.getType());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean updateUserResFlag(UserResBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_user_resource SET time = ?, wood = ?, fe = ?, grain = ?, stone = ?,flag=? WHERE id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setLong(1, bean.getTime());
			pstmt.setInt(2, bean.getWood());
			pstmt.setInt(3, bean.getFe());
			pstmt.setInt(4, bean.getGrain());
			pstmt.setInt(5, bean.getStone());
			pstmt.setInt(6, bean.getFlag());
			pstmt.setInt(7, bean.getId());
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean updateUserRes(UserResBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_user_resource SET time = ?, wood = ?, wood_speed = ?, fe = ?, fe_speed = ?, grain = ?, grain_speed = ?,stone = ?,stone_speed = ? WHERE id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setLong(1, bean.getTime());
			pstmt.setInt(2, bean.getWood());
			pstmt.setInt(3, bean.getWoodSpeed());
			pstmt.setInt(4, bean.getFe());
			pstmt.setInt(5, bean.getFeSpeed());
			pstmt.setInt(6, bean.getGrain());
			pstmt.setInt(7, bean.getGrainSpeed());
			pstmt.setInt(8, bean.getStone());
			pstmt.setInt(9, bean.getStoneSpeed());
			pstmt.setInt(10, bean.getId());
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	// 更新绿洲资源
	public boolean updateOasisRes(OasisBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_oasis SET time = ?, wood = ?, fe = ?,grain = ?, stone = ? WHERE id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setLong(1, bean.getTime());
			pstmt.setInt(2, bean.getWood());
			pstmt.setInt(3, bean.getFe());
			pstmt.setInt(4, bean.getGrain());
			pstmt.setInt(5, bean.getStone());
			pstmt.setInt(6, bean.getId());
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	/**
	 * 更新用户资源，主要是资源的增长速度更新
	 * @param bean
	 * @return
	 */
	public boolean updateUserResAll(UserResBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_user_resource SET time = ?, wood = ?, wood_speed = ?, fe = ?, fe_speed = ?, grain = ?, grain_speed = ?,stone = ?,stone_speed = ?,civil=?,max_res=?,max_grain=?,max_people=?,people=?,cave=?,people2=?,wall=?,other_wood_speed=?,other_fe_speed=?,other_grain_speed=?,other_stone_speed=?,flag=? WHERE id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setLong(1, bean.getTime());
			pstmt.setInt(2, bean.getWood());
			pstmt.setInt(3, bean.getWoodSpeed());
			pstmt.setInt(4, bean.getFe());
			pstmt.setInt(5, bean.getFeSpeed());
			pstmt.setInt(6, bean.getGrain());
			pstmt.setInt(7, bean.getGrainSpeed());
			pstmt.setInt(8, bean.getStone());
			pstmt.setInt(9, bean.getStoneSpeed());
			pstmt.setInt(10, bean.getCivil());
			pstmt.setInt(11, bean.getMaxRes());
			pstmt.setInt(12, bean.getMaxGrain());
			pstmt.setInt(13, bean.getMaxPeople());
			pstmt.setInt(14, bean.getPeople());
			pstmt.setInt(15, bean.getCave());
			pstmt.setInt(16, bean.getPeople2());
			pstmt.setInt(17, bean.getWall());
			pstmt.setInt(18, bean.getOtherWoodSpeed());
			pstmt.setInt(19, bean.getOtherFeSpeed());
			pstmt.setInt(20, bean.getOtherGrainSpeed());
			pstmt.setInt(21, bean.getOtherStoneSpeed());
			pstmt.setInt(22, bean.getFlag());
			pstmt.setInt(23, bean.getId());
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean updateUserCivil(CastleUserBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_user SET civil_time = ?, civil = ?, civil_speed = ?,people=? WHERE uid = " + bean.getUid();
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setLong(1, bean.getCivilTime());
			pstmt.setInt(2, bean.getCivil());
			pstmt.setInt(3, bean.getCivilSpeed());
			pstmt.setInt(4, bean.getPeople());
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	/**
	 * 新增加一个建筑
	 * @param bean
	 * @return
	 */
	public boolean addBuilding(BuildingBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle_building(build_type, grade, cid, people, build_pos) VALUES(?,?,?,?,?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, bean.getBuildType());
			pstmt.setInt(2, bean.getGrade());
			pstmt.setInt(3, bean.getCid());
			pstmt.setInt(4, bean.getPeople());
			pstmt.setInt(5, bean.getBuildPos());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	
	/**
	 * 建筑升级
	 * @param bean
	 * @return
	 */
	public boolean updateBuilding(BuildingBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_building SET grade = ?, people = ? WHERE cid = ? and build_pos = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, bean.getGrade());
			pstmt.setInt(2, bean.getPeople());
			pstmt.setInt(3, bean.getCid());
			pstmt.setInt(4, bean.getBuildPos());
			db.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			db.release();
		}
		return true;
	}
	
	public void deleteBuilding(BuildingBean bean) {
		DbOperation db = new DbOperation(5);
		db.executeUpdate("delete from castle_building where id=" + bean.getId());
		db.release();
	}
	public void deleteBuilding(BuildingThreadBean bean) {
		DbOperation db = new DbOperation(5);
		db.executeUpdate("delete from castle_building where cid=" + bean.getCid() + " and build_pos=" + bean.getBuildPos());
		db.release();
	}
	
	/**
	 * 获取某个用户的基本建资源筑
	 * @param cid
	 * @return
	 */
	public int[] getBaseBuilding(int cid) {
		int[] buildPos = new int[19];
		DbOperation db = new DbOperation(5);
		String query = "SELECT build_pos, grade FROM castle_building WHERE cid = " + cid + " AND build_pos <= 18";
		ResultSet rs = db.executeQuery(query);
		try {
			while(rs.next()) {
				buildPos[rs.getInt("build_pos")] = rs.getInt("grade");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return buildPos;
		}finally{
			db.release();
		}
		
		return buildPos;
	}
	
	public int[] getAdvanceBuild(int cid) {
		int[] buildPos = new int[ResNeed.castlePosCount + 1];
		DbOperation db = new DbOperation(5);
		String query = "SELECT build_pos, grade FROM castle_building WHERE cid = " + cid + " AND build_pos > 18";
		ResultSet rs = db.executeQuery(query);
		try {
			while(rs.next()) {
				buildPos[rs.getInt("build_pos")] = rs.getInt("grade");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return buildPos;
		}finally{
			db.release();
		}
		
		return buildPos;
	}
	
	public List getHasBuild(int cid) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT build_type FROM castle_building WHERE cid = " + cid + " AND build_pos > 18";
		ResultSet rs = db.executeQuery(query);
		try {
			while(rs.next()) {
				list.add(new Integer(rs.getInt("build_type")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	public List getAllBuilding(int cid) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_building WHERE cid = " + cid + " ORDER BY build_type ASC";
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try {
			BuildingBean bean = null;
			while(rs.next()) {
				bean = new BuildingBean();
				bean.setId(rs.getInt("id"));
				bean.setBuildType(rs.getInt("build_type"));
				bean.setGrade(rs.getInt("grade"));
				bean.setPeople(rs.getInt("people"));
				bean.setBuildPos(rs.getInt("build_pos"));
				bean.setCid(cid);
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	/**
	 * 根据pos来取得建筑
	 * @param cid
	 * @param pos
	 * @return
	 */
	public BuildingBean getBuildingBeanByPos(int cid, int pos) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_building WHERE cid = ? and build_pos = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return null;
		}
		BuildingBean bean = null;
		try {
			db.getPStmt().setInt(1, cid);
			db.getPStmt().setInt(2, pos);
			ResultSet rs = db.getPStmt().executeQuery();
			
			while(rs.next()) {
				bean = new BuildingBean();
				bean.setId(rs.getInt("id"));
				bean.setBuildType(rs.getInt("build_type"));
				bean.setGrade(rs.getInt("grade"));
				bean.setPeople(rs.getInt("people"));
				bean.setCid(rs.getInt("cid"));
				bean.setBuildPos(rs.getInt("build_pos"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		
		return bean;
	}
	/**
	 * 根据id来取得建筑
	 * @param id
	 * @return
	 */
	public BuildingBean getBuildingBeanById(int id) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_building WHERE id = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return null;
		}
		BuildingBean bean = null;
		try {
			db.getPStmt().setInt(1, id);
			
			ResultSet rs = db.getPStmt().executeQuery();
			
			while(rs.next()) {
				bean = new BuildingBean();
				bean.setId(rs.getInt("id"));
				bean.setBuildType(rs.getInt("build_type"));
				bean.setGrade(rs.getInt("grade"));
				bean.setPeople(rs.getInt("people"));
				bean.setCid(rs.getInt("cid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		
		return bean;
	}
	
	
	public BuildingBean getBuildingBean(int type, int cid) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_building WHERE cid = ? and build_type = ? limit 1";
		if (!db.prepareStatement(query)) {
			db.release();
			return null;
		}
		BuildingBean bean = null;
		try {
			db.getPStmt().setInt(1, cid);
			db.getPStmt().setInt(2, type);
			
			ResultSet rs = db.getPStmt().executeQuery();
			
			if(rs.next()) {
				bean = new BuildingBean();
				bean.setId(rs.getInt("id"));
				bean.setBuildType(rs.getInt("build_type"));
				bean.setBuildPos(rs.getInt("build_pos"));
				bean.setGrade(rs.getInt("grade"));
				bean.setPeople(rs.getInt("people"));
				bean.setCid(cid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		
		return bean;
	}
	
	public List getBuildingBeanList(String cond) {
		List list =new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_building WHERE " + cond;

		try {
			ResultSet rs = db.executeQuery(query);
			
			while(rs.next()) {
				list.add(getBuildingBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	public BuildingBean getBuildingBean(ResultSet rs) throws SQLException {
		BuildingBean bean = new BuildingBean();
		bean.setId(rs.getInt("id"));
		bean.setBuildType(rs.getInt("build_type"));
		bean.setBuildPos(rs.getInt("build_pos"));
		bean.setGrade(rs.getInt("grade"));
		bean.setPeople(rs.getInt("people"));
		bean.setCid(rs.getInt("cid"));
		return bean;
	}
	
	/**
	 * 得到用户建筑占用的人口
	 * @param cid
	 * @return
	 */
	public int getBuildingPeople(int cid) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "SELECT SUM(people) as count FROM castle_building WHERE cid = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return count;
		}
		try {
			db.getPStmt().setInt(1, cid);
			ResultSet rs = db.getPStmt().executeQuery();
			while(rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		return count;
	}
	
	/**
	 * 获得藏兵洞的士兵数量
	 */
	public int getHiddenSoldierCount(int cid) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "SELECT count1+count2+count3+count4+count5+count6+count7+count8+count9+count10 as count FROM castle_hidden_soldier WHERE cid = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return count;
		}		
		try {
			db.getPStmt().setInt(1, cid);
			ResultSet rs = db.getPStmt().executeQuery();
			while(rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		
		
		return count;
	}

	/**
	 * 每建造一个兵，相应的兵种数量就自动加1
	 * @param uid
	 * @param type
	 * @return
	 */
	public boolean updateSoldierCount(CastleArmyBean army, int type) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_soldier SET count" + type + " = count" + type + " + 1 WHERE id = " + army.getId();
		db.executeUpdate(query);
		db.release();
		return true;
	}
	public boolean updateArmyHero(CastleArmyBean army) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_soldier SET hero=" + army.getHero() + " WHERE id = " + army.getId();
		db.executeUpdate(query);
		db.release();
		return true;
	}
	// 更新所有士兵数量
	public boolean updateSoldierCount(CastleArmyBean army) {
		DbOperation db = new DbOperation(5);
		int[] count = army.getCount();
		StringBuilder query = new StringBuilder(64);
		query.append("UPDATE castle_soldier SET ");
		for(int i = 1;i < count.length;i++) {
			if(i != 1)
				query.append(',');
			query.append("count");
			query.append(i);
			query.append('=');
			query.append(count[i]);
		}
		query.append(",hero=");
		query.append(army.getHero());
		query.append(" WHERE id=");
		query.append(army.getId());
		db.executeUpdate(query.toString());
		db.release();
		return true;
	}
	
	// 更新所有士兵数量
	public boolean deleteCastleArmyById(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from castle_soldier where id=" + id;
		boolean flag = db.executeUpdate(query);
		db.release();
		return flag;
	}
	
	/**
	 * 获得藏兵的藏兵
	 * @param cid
	 * @return
	 */
	public CastleArmyBean getCastleHiddenArmy(int cid) {
		CastleArmyBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_hidden_soldier where cid=" + cid + " limit 1";
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				bean = getCastleHiddenArmy(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	
	private CastleArmyBean getCastleHiddenArmy(ResultSet rs) throws SQLException {
		CastleArmyBean army = new CastleArmyBean();
		army.setId(rs.getInt("id"));
		army.setCid(rs.getInt("cid"));
		
		for(int i = 1;i <= ResNeed.soldierTypeCount;i++)
			army.setCount(i, rs.getInt("count" + i));
		return army;
	}
	
	/**
	 * 初始化藏兵洞藏兵的士兵
	 * @param cid
	 * @return 
	 */
	public boolean addHiddenSoldier(CastleArmyBean army) {
		DbOperation db = new DbOperation(5);
		
		String query = "INSERT INTO castle_hidden_soldier(cid) values(?)";
		
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, army.getCid());
			pstmt.execute();
			army.setId(db.getLastInsertId());
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	/**
	 * 更新藏兵洞的藏兵数量
	 */
	public boolean updateHiddenSoldierCount(int id, int[] count) {
		DbOperation db = new DbOperation(5);
		//int[] count = army.getCount();
		StringBuilder query = new StringBuilder(64);
		query.append("UPDATE castle_hidden_soldier SET ");
		for(int i = 1;i < count.length;i++) {
			if(i != 1)
				query.append(',');
			query.append("count");
			query.append(i);
			query.append('=');
			query.append("count");
			query.append(i);
			query.append('+');
			query.append(count[i]);
		}
		query.append(" WHERE id=");
		query.append(id);
		db.executeUpdate(query.toString());
		db.release();
		return true;
	}
	
	/**
	 * 更新藏兵洞的藏兵数量
	 */
	public boolean updateHiddenSoldierCount(CastleArmyBean army) {
		DbOperation db = new DbOperation(5);
		int[] count = army.getCount();
		StringBuilder query = new StringBuilder(64);
		query.append("UPDATE castle_hidden_soldier SET ");
		for(int i = 1;i < count.length;i++) {
			if(i != 1)
				query.append(',');
			query.append("count");
			query.append(i);
			query.append('=');
			query.append(count[i]);
		}
		query.append(" WHERE id=");
		query.append(army.getId());
		db.executeUpdate(query.toString());
		db.release();
		return true;
	}

	/**
	 * 初始化用户的防御和攻击等级
	 * @param bean
	 * @return
	 */
	public boolean addSoldierSmithy(SoldierSmithyBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle_soldier_smithy(cid, soldier_type, attack, defence) values(?,?,?,?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, bean.getCid());
			pstmt.setInt(2, bean.getSoldierType());
			pstmt.setInt(3, bean.getAttack());
			pstmt.setInt(4, bean.getDefence());
			pstmt.execute();
			bean.setId(db.getLastInsertId());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	/**
	 * 用户的士兵防御等级+1
	 * @param cid
	 * @return
	 */
	public boolean updateSoldierDefence(int cid, int soldierType) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_soldier_smithy SET defence = defence + 1 WHERE cid = ? and soldier_type = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, cid);
			pstmt.setInt(2, soldierType);
			db.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	/**
	 * 用户的士兵攻击等级+1
	 * @param cid
	 * @return
	 */
	public boolean updateSoldierAttack(int cid, int soldierType) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_soldier_smithy SET attack = attack + 1 WHERE cid = ? and soldier_type = ?";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, cid);
			pstmt.setInt(2, soldierType);
			db.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public SoldierSmithyBean getSoldierSmithy(int cid, int soldierType) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier_smithy WHERE cid = " + cid + " and soldier_type = " + soldierType;
		try {
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				return getSoldierSmithyBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return null;
	}
	
	/**
	 * 获取某个用户士兵的攻击和防守
	 * @param cid
	 * @return
	 */
	public List getSoldierSmithy(int cid) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier_smithy WHERE cid = " + cid;
		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getSoldierSmithyBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	
	private SoldierSmithyBean getSoldierSmithyBean(ResultSet rs) throws SQLException {
		SoldierSmithyBean bean = new SoldierSmithyBean();
		bean.setCid(rs.getInt("cid"));
		bean.setId(rs.getInt("id"));
		bean.setSoldierType(rs.getInt("soldier_type"));
		bean.setAttack(rs.getInt("attack"));
		bean.setDefence(rs.getInt("defence"));
		
		return bean;
	}
	
	// 获取任务列表
	public static List getQuestList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_quest WHERE " + cond;
		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getQuest(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	
	private static CastleQuestBean getQuest(ResultSet rs) throws SQLException {
		CastleQuestBean bean = new CastleQuestBean();
		bean.setId(rs.getInt("id"));
		bean.setTitle(rs.getString("title"));
		bean.setStartMsg(rs.getString("start_msg"));
		bean.setEndMsg(rs.getString("end_msg"));
		bean.setRewardMsg(rs.getString("reward_msg"));
		bean.setMission(rs.getString("mission"));
		bean.setWood(rs.getInt("wood"));
		bean.setStone(rs.getInt("stone"));
		bean.setFe(rs.getInt("fe"));
		bean.setGrain(rs.getInt("grain"));
		bean.setGold(rs.getInt("gold"));
		bean.setSp(rs.getInt("sp"));
		bean.setType(rs.getInt("type"));
		bean.setReward(rs.getInt("reward"));
		bean.setValue(rs.getInt("value"));
		bean.setValue2(rs.getInt("value2"));
		bean.setValue3(rs.getInt("value3"));
		return bean;
	}
	
	public CastleArmyBean getCastleArmyById(int id) {
		CastleArmyBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier where id=" + id;
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				bean = getCastleArmy(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	// 获得城堡留在本地的军队
	public CastleArmyBean getCastleArmy(int cid) {
		CastleArmyBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier where cid=" + cid + " and at=cid limit 1";
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				bean = getCastleArmy(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	public CastleArmyBean getCastleArmy(int cid, int at) {
		CastleArmyBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier where cid=" + cid + " and at=" + at + " limit 1";
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				bean = getCastleArmy(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	// 获得城堡内所有军队
	public List getCastleArmyAtList(int at) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier where at=" + at;
		try {
			ResultSet rs = db.executeQuery(query);
			while (rs.next()) {
				list.add(getCastleArmy(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return list;
	}
	// 获得cid拥有的所有军队，不包括在自己城堡里的
	public List getCastleArmyList(int cid) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier where cid=" + cid + " and at!=cid";
		try {
			ResultSet rs = db.executeQuery(query);
			while (rs.next()) {
				list.add(getCastleArmy(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return list;
	}

	private CastleArmyBean getCastleArmy(ResultSet rs) throws SQLException {
		CastleArmyBean army = new CastleArmyBean();
		army.setId(rs.getInt("id"));
		army.setCid(rs.getInt("cid"));
		army.setAt(rs.getInt("at"));
		for(int i = 1;i <= ResNeed.soldierTypeCount;i++)
			army.setCount(i, rs.getInt("count" + i));
		army.setCount(0, rs.getInt("hero"));
		return army;
	}
	
	public boolean addCastleArmy(CastleArmyBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "insert into castle_soldier set cid=" + bean.getCid() + ",at=" + bean.getAt();
		boolean flag = db.executeUpdate(query);
		bean.setId(db.getLastInsertId());
		db.release();
		return flag;
	}
	//================绿洲============
	public OasisBean getOasis(String cond){
		OasisBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_oasis where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getOasisBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		
		return bean;
	}
	public List getOasisList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_oasis where " + cond;
		try {
			ResultSet rs = db.executeQuery(query);
			while (rs.next()) {
				list.add(getOasisBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return list;
	}
	private OasisBean getOasisBean(ResultSet rs) throws SQLException{
		OasisBean bean = new OasisBean();
		bean.setId(rs.getInt("id"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setUpdateTime(rs.getTimestamp("update_time").getTime());
		bean.setRace(rs.getInt("race"));
		bean.setType(rs.getInt("type"));
		bean.setWood(rs.getInt("wood"));
		bean.setStone(rs.getInt("stone"));
		bean.setFe(rs.getInt("fe"));
		bean.setGrain(rs.getInt("grain"));
		bean.setCid(rs.getInt("cid"));
		bean.setUid(rs.getInt("uid"));
		bean.setRobTime(rs.getLong("rob_time"));
		return bean;
	}
	//=================绿洲的军队=======================
	// 更新所有士兵数量
	public boolean updateOasisSoldierCount(CastleArmyBean army) {
		DbOperation db = new DbOperation(5);
		int[] count = army.getCount();
		StringBuilder query = new StringBuilder(64);
		query.append("UPDATE castle_soldier2 SET ");
		for(int i = 1;i < count.length;i++) {
			if(i != 1)
				query.append(',');
			query.append("count");
			query.append(i);
			query.append('=');
			query.append(count[i]);
		}
		query.append(",hero=");
		query.append(army.getHero());
		query.append(" WHERE id=");
		query.append(army.getId());
		db.executeUpdate(query.toString());
		db.release();
		return true;
	}
	
	// 更新所有士兵数量
	public boolean deleteOasisArmyById(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from castle_soldier2 where id=" + id;
		boolean flag = db.executeUpdate(query);
		db.release();
		return flag;
	}
	public CastleArmyBean getOasisArmyById(int id) {
		CastleArmyBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier2 where id=" + id;
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				bean = getCastleArmy(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	public CastleArmyBean getOasisArmy(int cid, int at) {
		CastleArmyBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier2 where cid=" + cid + " and at=" + at + " limit 1";
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				bean = getCastleArmy(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	public CastleArmyBean getOasisArmyAt(int at) {
		CastleArmyBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier2 where at=" + at + " limit 1";
		try {
			ResultSet rs = db.executeQuery(query);
			if (rs.next()) {
				bean = getCastleArmy(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	// 获得绿洲内所有军队
	public List getOasisArmyAtList(int at) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier2 where at=" + at;
		try {
			ResultSet rs = db.executeQuery(query);
			while (rs.next()) {
				list.add(getCastleArmy(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return list;
	}
	// 获得一个城堡的所有绿洲的所有军队
	public List getOasisArmyAtCidList(int cid) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier2 where at_cid=" + cid;
		try {
			ResultSet rs = db.executeQuery(query);
			while (rs.next()) {
				list.add(getCastleArmy(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return list;
	}
	public List getOasisArmyList(int cid) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_soldier2 where cid=" + cid;
		try {
			ResultSet rs = db.executeQuery(query);
			while (rs.next()) {
				list.add(getCastleArmy(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return list;
	}
	public boolean addOasisArmy(CastleArmyBean bean, int atCid) {
		DbOperation db = new DbOperation(5);
		String query = "insert into castle_soldier2 set cid=" + bean.getCid() + ",at=" + bean.getAt() + ",at_cid=" + atCid;
		boolean flag = db.executeUpdate(query);
		bean.setId(db.getLastInsertId());
		db.release();
		return flag;
	}
	public boolean addOasisArmyFull(CastleArmyBean army) {
		DbOperation db = new DbOperation(5);
		int[] count = army.getCount();
		StringBuilder query = new StringBuilder(64);
		query.append("insert into castle_soldier2 SET cid=");
		query.append(army.getCid());
		query.append(",at=");
		query.append(army.getAt());
		for(int i = 1;i < count.length;i++) {
			query.append(',');
			query.append("count");
			query.append(i);
			query.append('=');
			query.append(count[i]);
		}
		boolean flag = db.executeUpdate(query.toString());
		army.setId(db.getLastInsertId());
		db.release();
		return flag;
	}
	//==================商人相关操作======================
	public boolean updateUserMerchant(int cid, int count) {
		DbOperation db = new DbOperation(5);
		String query = "update castle_user_resource set merchant = " + count + " where id = " + cid;
		boolean flag = db.executeUpdate(query);
		db.release();
		return flag;
	}
	
	//==================联盟相关操作======================
	public boolean addTong(TongBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle_tong(uid, name, create_time, count) values(?,?,now(),?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, bean.getUid());
			pstmt.setString(2, bean.getName());
			pstmt.setInt(3, bean.getCount());
			pstmt.execute();
			
			bean.setId(db.getLastInsertId());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	// 更新联盟成员数量
	public boolean updateTongCount(int id, int count) {
		DbOperation db = new DbOperation(5);
		String query = "update castle_tong set count = count + " + count + " where id = " + id;
		boolean flag = db.executeUpdate(query);
		db.release();
		return flag;
	}
	
	public boolean updateUserTong(int uid, int tongId) {
		DbOperation db = new DbOperation(5);
		String query = "update castle_user set tong = " + tongId + " where uid = " + uid;
		boolean flag = db.executeUpdate(query);
		db.release();
		return flag;
	}
	
	public List getTongUser(int tongId, int start, int limit) {
		DbOperation db = new DbOperation(5);
		String query = "select uid from castle_user where tong=" + tongId + " order by people desc limit " + start + ", " + limit;
		
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try{
			while(rs.next()) {
				list.add(new Integer(rs.getInt("uid")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	public List getTongUser(int tongId) {
		DbOperation db = new DbOperation(5);
		String query = "select uid from castle_user where tong = " + tongId;
		
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try{
			while(rs.next()) {
				list.add(new Integer(rs.getInt("uid")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	public boolean deleteTong(int tongId) {
		DbOperation db = new DbOperation(5);
		db.executeUpdate("update castle_user set tong = 0 where tong = " + tongId);
		db.executeUpdate("delete from castle_tong where id = " + tongId);
		db.executeUpdate("delete from castle_tong_invite where tong_id = " + tongId);
		db.executeUpdate("delete from castle_tong_power where tong_id = " + tongId);
		db.executeUpdate("delete from castle_agree where tong_id = " + tongId);
		db.executeUpdate("delete from castle_agree where tong_id2 = " + tongId);
		db.executeUpdate("delete from castle_agree_apply where tong_id = " + tongId);
		db.executeUpdate("delete from castle_agree_apply where tong_id2 = " + tongId);
		db.release();
		return true;
	}
	
	public TongBean getTong(int tongId) {
		if(tongId == 0)
			return null;
		DbOperation db = new DbOperation(5);
		String query = "select * from castle_tong where id = " + tongId;
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				TongBean tongBean = new TongBean();
				tongBean.setId(rs.getInt("id"));
				tongBean.setCount(rs.getInt("count"));
				tongBean.setCreateTime(rs.getTimestamp("create_time"));
				tongBean.setName(rs.getString("name"));
				tongBean.setInfo(rs.getString("info"));
				tongBean.setUid(rs.getInt("uid"));
				tongBean.setPeople(rs.getInt("people"));
				return tongBean;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return null;
	}
	
	//邀请加入联盟
	public boolean addTongInvite(TongInviteBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "insert into castle_tong_invite set from_uid=" + bean.getFromUid() + ",to_uid=" + bean.getToUid() + ", tong_id = " + bean.getTongId();
		boolean flag = db.executeUpdate(query);
		bean.setId(db.getLastInsertId());
		db.release();
		return flag;
	}
	
	public List getTongInvite(int uid,int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "select * from castle_tong_invite where to_uid = " + uid + " limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				TongInviteBean bean = new TongInviteBean();
				bean.setId(rs.getInt("id"));
				bean.setFromUid(rs.getInt("from_uid"));
				bean.setToUid(rs.getInt("to_uid"));
				bean.setTongId(rs.getInt("tong_id"));
				
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	public int getTongInviteCount(int uid) {
		DbOperation db = new DbOperation(5);
		String query = "select count(id) as count from castle_tong_invite where to_uid = " + uid ;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				return rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			db.release();
		}
		
		return 0;
	}
	
	public List getTongInviteFromTong(int tongId,int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "select * from castle_tong_invite where tong_id = " + tongId + " limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				TongInviteBean bean = new TongInviteBean();
				bean.setId(rs.getInt("id"));
				bean.setFromUid(rs.getInt("from_uid"));
				bean.setToUid(rs.getInt("to_uid"));
				bean.setTongId(rs.getInt("tong_id"));
				
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	public int getTongInviteFromTongCount(int tongId) {
		DbOperation db = new DbOperation(5);
		String query = "select count(id) as count from castle_tong_invite where tong_id = " + tongId ;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				return rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			db.release();
		}
		
		return 0;
	}
	
	public boolean getTongInvite(int fromUid, int toUid) {
		DbOperation db = new DbOperation(5);
		
		String query = "select id from castle_tong_invite where from_uid = " + fromUid + " and to_uid = " + toUid;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return false;
	}
	
	public boolean getTongInviteToUid(int toUid, int tongId) {
		DbOperation db = new DbOperation(5);
		
		String query = "select id from castle_tong_invite where to_uid = " + toUid + " and tong_id = " + tongId;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return false;
	}
	
	public boolean deleteTongInvite(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from castle_tong_invite where id = " + id;
		boolean flag = db.executeUpdate(query);
		db.release();
		return flag;
	}
	
	public TongInviteBean getTongInviteBean(int id) {
		TongInviteBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "select * from castle_tong_invite where id = " + id;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				bean = new TongInviteBean();
				bean.setId(rs.getInt("id"));
				bean.setFromUid(rs.getInt("from_uid"));
				bean.setToUid(rs.getInt("to_uid"));
				bean.setTongId(rs.getInt("tong_id"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		
		return bean;
	}
	
	// 返回接近的军队，远离的军队数量
	// 数组 2 远离中的攻击军队 0 接近中的攻击军队 1 接近中的支援军队
	public Object[] getCacheAttackCount(int cid) {
		int[] count = new int[4];
		long[] time = new long[4];
		Object[] attack = {count, time};
		DbOperation db = new DbOperation(5);
		try {
			ResultSet rs = db.executeQuery("select type,end_time from cache_attack where to_cid=" + cid + " and to_cid!=from_cid order by end_time");
			while(rs.next()) {
				int type = rs.getInt(1);
				if(type == 5 || type == 10) {
					count[1]++;
					if(time[1] == 0)
						time[1] = rs.getLong(2);
				} else if(type == 1 || type == 0 || type == 7 || type == 8){
					count[0]++;
					if(time[0] == 0)
						time[0] = rs.getLong(2);
				}
			}
			rs = db.executeQuery("select type,end_time from cache_attack where from_cid=" + cid + " and from_cid=cid order by end_time");
			while(rs.next()) {
				int type = rs.getInt(1);
				if(type == 5 || type == 10) {
					count[3]++;
					if(time[3] == 0)
						time[3] = rs.getLong(2);
				} else if(type != 4){
					count[2]++;
					if(time[2] == 0)
						time[2] = rs.getLong(2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.release();
		return attack;
	}
	
	
		
	//城市人口排行
	public List getCastleArray(int start, int limit) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from castle_stat order by id limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				Object[] obj = new Object[5];
				obj[0] = new Integer(rs.getInt("id"));
				obj[1] = new Integer(rs.getInt("uid"));
				obj[2] = new Integer(rs.getInt("people"));
				obj[3] = rs.getString("name");
				obj[4] = new Integer(rs.getInt("castle_count"));
				
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	//联盟人口排行
	public List getTongArray(int start, int limit) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from castle_stat2 order by id limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				Object[] obj = new Object[5];
				obj[0] = new Integer(rs.getInt("id"));
				obj[1] = new Integer(rs.getInt("tong_id"));
				obj[2] = new Integer(rs.getInt("people"));
				obj[3] = rs.getString("name");
				obj[4] = new Integer(rs.getInt("count"));
				
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	
	//攻击排行
	public List getAttackArray(int start, int limit) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from castle_stat3 order by id limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				Object[] obj = new Object[5];
				obj[0] = new Integer(rs.getInt("id"));
				obj[1] = new Integer(rs.getInt("uid"));
				obj[2] = rs.getString("name");
				obj[3] = new Integer(rs.getInt("attack_total"));
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	
	//防御排行
	public List getDefenseArray(int start, int limit) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from castle_stat4 order by id limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				Object[] obj = new Object[5];
				obj[0] = new Integer(rs.getInt("id"));
				obj[1] = new Integer(rs.getInt("uid"));
				obj[2] = rs.getString("name");
				obj[3] = new Integer(rs.getInt("defense_total"));
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	
	//抢夺排行
	public List getRobArray(int start, int limit) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from castle_stat5 order by id limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				Object[] obj = new Object[5];
				obj[0] = new Integer(rs.getInt("id"));
				obj[1] = new Integer(rs.getInt("uid"));
				obj[2] = rs.getString("name");
				obj[3] = new Integer(rs.getInt("rob_total"));
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	// 英雄排行榜
	public List getHeroArray(int start, int limit) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from castle_stat6 order by id limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				Object[] obj = new Object[7];
				obj[0] = new Integer(rs.getInt("id"));
				int uid = rs.getInt("uid");
				obj[1] = new Integer(uid);
				obj[2] = rs.getString("name");
				int exp = rs.getInt("exp");
				obj[3] = new Integer(exp);
				obj[4] = new Integer(HeroBean.exp2Rank(exp));
				CastleUserBean user = CastleUtil.getCastleUser(uid);
				if(user == null) {
					obj[5] = "[?]";
					obj[6] = ResNeed.getSoldierType(1, 1);
				} else {
					obj[5] = user.getName();
					obj[6] = ResNeed.getSoldierRes(user.getRace(), rs.getInt("type"));
				}
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	
	/************每周**************/

	//攻击排行
	public List getAttackWArray(int start, int limit) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from castle_statw3 order by id limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				Object[] obj = new Object[5];
				obj[0] = new Integer(rs.getInt("id"));
				obj[1] = new Integer(rs.getInt("uid"));
				obj[2] = rs.getString("name");
				obj[3] = new Integer(rs.getInt("attack_total"));
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	
	//防御排行
	public List getDefenseWArray(int start, int limit) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from castle_statw4 order by id limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				Object[] obj = new Object[5];
				obj[0] = new Integer(rs.getInt("id"));
				obj[1] = new Integer(rs.getInt("uid"));
				obj[2] = rs.getString("name");
				obj[3] = new Integer(rs.getInt("defense_total"));
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	
	//抢夺排行
	public List getRobWArray(int start, int limit) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from castle_statw5 order by id limit " + start + "," + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				Object[] obj = new Object[5];
				obj[0] = new Integer(rs.getInt("id"));
				obj[1] = new Integer(rs.getInt("uid"));
				obj[2] = rs.getString("name");
				obj[3] = new Integer(rs.getInt("rob_total"));
				list.add(obj);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
			
		}
		return list;
	}
	
	
	//用户当前的排名
	public int getCastleCurArray(int uid) {
		
		String query = "select id from castle_stat where uid = " + uid;
		return SqlUtil.getIntResult(query, 5);
	}
	//联盟当前的排名
	public int getTongCurArray(int tongId) {
		
		String query = "select id from castle_stat2 where tong_id = " + tongId;
		return SqlUtil.getIntResult(query, 5);
	}
	//攻击当前的排名
	public int getAttackCurArray(int uid) {
		String query = "select id from castle_stat3 where uid = " + uid;
		return SqlUtil.getIntResult(query, 5);
	}
	
	//防御当前的排名
	public int getDefenseCurArray(int uid) {
		String query = "select id from castle_stat4 where uid = " + uid;
		return SqlUtil.getIntResult(query, 5);
	}
	//抢夺当前的排名
	public int getRobCurArray(int uid) {
		String query = "select id from castle_stat5 where uid = " + uid;
		return SqlUtil.getIntResult(query, 5);
	}
	// 英雄当前的排名
	public int getHeroCurArray(int uid) {
		String query = "select id from castle_stat6 where uid = " + uid;
		return SqlUtil.getIntResult(query, 5);
	}
	
	/***本周**********/
	//攻击当前的排名
	public int getAttackWCurArray(int uid) {
		String query = "select id from castle_statw3 where uid = " + uid;
		return SqlUtil.getIntResult(query, 5);
	}
	
	//防御当前的排名
	public int getDefenseWCurArray(int uid) {
		String query = "select id from castle_statw4 where uid = " + uid;
		return SqlUtil.getIntResult(query, 5);
	}
	//抢夺当前的排名
	public int getRobWCurArray(int uid) {
		String query = "select id from castle_statw5 where uid = " + uid;
		return SqlUtil.getIntResult(query, 5);
	}
	
	
	// 添加扩张记录
	public void addCastleExpand(CastleBean from, CastleBean to) {
		DbOperation db = new DbOperation(5);
		db.executeUpdate("update castle set expand=" + from.getExpand()
				+ " where id=" + from.getId());
		db.executeUpdate("insert into castle_expand set create_time=now(),from_cid=" + from.getId()
				+ ",to_cid=" + to.getId());
		db.release();
	}
	// 添加绿洲占领记录
	public void addCastleExpand2(CastleBean from, CastleBean to) {
		DbOperation db = new DbOperation(5);
		db.executeUpdate("update castle set expand2=" + from.getExpand2()
				+ " where id=" + from.getId());
		db.executeUpdate("insert into castle_expand2 set create_time=now(),from_cid=" + from.getId()
				+ ",to_cid=" + to.getId());
		db.release();
	}
	
	// 市场买物品和交易相关
	// 添加卖的记录
	public boolean addTrade(TradeBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle_trade(cid, supply,need,supply_res,need_res,supply_merchant,distance,x,y) values(?,?,?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getCid());
			pstmt.setInt(2, bean.getSupply());
			pstmt.setInt(3, bean.getNeed());
			pstmt.setInt(4, bean.getSupplyRes());
			pstmt.setInt(5, bean.getNeedRes());
			pstmt.setInt(6, bean.getSupplyMerchant());
			pstmt.setInt(7, bean.getDistance());
			pstmt.setInt(8, bean.getX());
			pstmt.setInt(9, bean.getY());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	// 查询卖的记录
	public List getTradeList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_trade where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getTradeBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	private TradeBean getTradeBean(ResultSet rs)throws SQLException{
		TradeBean bean = new TradeBean();
		bean.setId(rs.getInt("id"));
		bean.setCid(rs.getInt("cid"));
		bean.setSupply(rs.getInt("supply"));
		bean.setNeed(rs.getInt("need"));
		bean.setSupplyRes(rs.getInt("supply_res"));
		bean.setNeedRes(rs.getInt("need_res"));
		bean.setSupplyMerchant(rs.getInt("supply_merchant"));
		bean.setDistance(rs.getInt("distance"));
		bean.setX(rs.getInt("x"));
		bean.setY(rs.getInt("y"));
		return bean;
	}
	
	public TradeBean getTradeBean(String cond) {
		TradeBean trade = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_trade where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			if (rs.next()) {
				trade = getTradeBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return trade;
	}
	
	
	public void deleteTongPower(int uid) {
		DbOperation db = new DbOperation(5);
		String query = "delete from castle_tong_power where uid = " + uid;
		db.executeUpdate(query);
		
		db.release();
	}
	
	public void addTongPower(TongPowerBean powerBean){
		DbOperation db = new DbOperation(5);
		String query = "insert into castle_tong_power set uid = " + powerBean.getUid() + 
				", power_name = '" + StringUtil.trim(powerBean.getPowerName()) +
				"', power = " + powerBean.getPower() + ", tong_id = " + powerBean.getTongId();
		db.executeUpdate(query);
		db.release();
		
	}
	
	public void updateTongPower(int uid, int power) {
		DbOperation db = new DbOperation(5);
		String query = "update castle_tong_power set power = " + power + " where uid = " + uid;
		
		db.executeUpdate(query);
		
		db.release();
		
	}
	
	public void updateTongPowerName(int uid, String name) {
		DbOperation db = new DbOperation(5);
		String query = "update castle_tong_power set power_name = '" + StringUtil.toSql(name) + "' where uid = " + uid;
		
		db.executeUpdate(query);
		
		db.release();
	}
	
	
	public void updateTongInfo(int tongId, String info) {
		DbOperation db = new DbOperation(5);
		String query = "update castle_tong set info = '" + StringUtil.toSql(info) + "' where id = " + tongId;
		
		db.executeUpdate(query);
		
		db.release();
	}
	
	public void updateTongName(int tongId, String name) {
		DbOperation db = new DbOperation(5);
		String query = "update castle_tong set name = '" + StringUtil.toSql(name) + "' where id = " + tongId;
		
		db.executeUpdate(query);
		
		db.release();
	}
	
	
	public List getTongPower(int tongId) {
		DbOperation db = new DbOperation(5);
		
		String query = "select * from castle_tong_power where tong_id = " + tongId + " and power > 0 order by power desc";
		
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try{
			while(rs.next()) {
				TongPowerBean bean = new TongPowerBean();
				bean.setPower(rs.getInt("power"));
				bean.setPowerName(rs.getString("power_name"));
				bean.setTongId(rs.getInt("tong_id"));
				bean.setUid(rs.getInt("uid"));
				
				list.add(bean);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return list;
	}
	
	
	public TongPowerBean getTongPowerByUid(int uid) {
		DbOperation db = new DbOperation(5);
		TongPowerBean bean =null;
		String query = "select * from castle_tong_power where uid = " + uid;
	
		ResultSet rs = db.executeQuery(query);
	
		try{
			while(rs.next()) {
				bean = new TongPowerBean();
				bean.setPower(rs.getInt("power"));
				bean.setPowerName(rs.getString("power_name"));
				bean.setTongId(rs.getInt("tong_id"));
				bean.setUid(rs.getInt("uid"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
	
		return bean;
	}
	
	
	// 联盟外交相关
	public List getTongAgreeList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_agree where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getTongAgree(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	public TongAgreeBean getTongAgree(String cond) {
		TongAgreeBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_agree where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getTongAgree(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	public List getTongAgreeApplyList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_agree_apply where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getTongAgree(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	public TongAgreeBean getTongAgreeApply(String cond) {
		TongAgreeBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_agree_apply where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				bean = getTongAgree(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	public TongAgreeBean getTongAgree(ResultSet rs)throws SQLException{
		TongAgreeBean bean = new TongAgreeBean();
		bean.setId(rs.getInt("id"));
		bean.setTongId(rs.getInt("tong_id"));
		bean.setTongId2(rs.getInt("tong_id2"));
		bean.setType(rs.getInt("type"));
		return bean;
	}
	public boolean addTongAgree(TongAgreeBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle_agree(tong_id,tong_id2,type,create_time) values(?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getTongId());
			pstmt.setInt(2, bean.getTongId2());
			pstmt.setInt(3, bean.getType());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	public boolean addTongAgreeApply(TongAgreeBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO castle_agree_apply(tong_id,tong_id2,type,create_time) values(?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getTongId());
			pstmt.setInt(2, bean.getTongId2());
			pstmt.setInt(3, bean.getType());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}

	public void updateUserFlag(CastleUserBean castleUser) {
		SqlUtil.executeUpdate("update castle_user set flag=" + castleUser.getFlag() + " where uid=" + castleUser.getUid(), 5);
	}
	
	/// 奇迹和宝物
	public WWBean getWW(String cond) {
		WWBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from castle_ww where " + cond);
		
		try {
			if(rs.next()) {
				bean = getWW(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return bean;
	}
	public List getWWList(String cond) {
		List list = new ArrayList(16);
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from castle_ww where " + cond);
		
		try {
			while(rs.next()) {
				list.add(getWW(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return list;
	}
	public WWBean getWW(ResultSet rs)throws SQLException{
		WWBean bean = new WWBean();
		bean.setId(rs.getInt("id"));
		bean.setLevel(rs.getInt("lvl"));
		bean.setCid(rs.getInt("cid"));
		bean.setName(rs.getString("name"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
	
	public List getArtList(String cond) {
		List list = new ArrayList(16);
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from castle_art where " + cond);
		
		try {
			while(rs.next()) {
				list.add(getArt(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return list;
	}
	public ArtBean getArt(ResultSet rs)throws SQLException{
		ArtBean bean = new ArtBean();
		bean.setId(rs.getInt("id"));
		bean.setType(rs.getInt("type"));
		bean.setFlag(rs.getInt("flag"));
		bean.setStatus(rs.getInt("status"));
		bean.setCid(rs.getInt("cid"));
		bean.setUid(rs.getInt("uid"));
		bean.setX(rs.getInt("x"));
		bean.setY(rs.getInt("y"));
		bean.setEffect(rs.getInt("effect"));
		bean.setName(rs.getString("name"));
		bean.setCaptureTime(rs.getLong("capture_time"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
	// 宝物拥有者历史
	public List getArtHisList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from castle_art_his where " + cond);
		
		try {
			while(rs.next()) {
				list.add(getArtHis(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return list;
	}
	public ArtBean getArtHis(ResultSet rs)throws SQLException{
		ArtBean bean = new ArtBean();
		bean.setId(rs.getInt("id"));
		bean.setType(rs.getInt("art_id"));
		bean.setCid(rs.getInt("cid"));
		bean.setUid(rs.getInt("uid"));
		bean.setCaptureTime(rs.getTimestamp("time").getTime());
		return bean;
	}

	public boolean updateLoyal(UserResBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_user_resource SET loyal_time = ?, loyal = ?, loyal_speed = ? WHERE id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setLong(1, bean.getLoyalTime());
			pstmt.setInt(2, bean.getLoyal());
			pstmt.setInt(3, bean.getLoyalSpeed());
			pstmt.setInt(4, bean.getId());
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}

	public List getHeroList(String cond) {
		List list = new ArrayList(3);
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_hero where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getHero(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	public HeroBean getHero(String cond) {
		HeroBean bean = null;
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM castle_hero where " + cond;
		ResultSet rs = db.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getHero(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	public HeroBean getHero(ResultSet rs)throws SQLException{
		HeroBean bean = new HeroBean();
		bean.setId(rs.getInt("id"));
		bean.setRace(rs.getInt("race"));
		bean.setType(rs.getInt("type"));
		bean.setUid(rs.getInt("uid"));
		bean.setName(rs.getString("name"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setStat1(rs.getInt("stat1"));
		bean.setStat2(rs.getInt("stat2"));
		bean.setStat3(rs.getInt("stat3"));
		bean.setStat4(rs.getInt("stat4"));
		bean.setStat5(rs.getInt("stat5"));
		
		bean.setTime(rs.getLong("time"));
		bean.setHealth(rs.getFloat("health"));
		bean.setStatus(rs.getInt("status"));
		bean.setExp(rs.getInt("exp"));
		return bean;
	}

	public boolean updateHero(HeroBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_hero SET time=?,stat1=?,stat2=?,stat3=?,stat4=?,stat5=?,name=?,health=?,exp=?,type=?,status=? WHERE id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setLong(1, bean.getTime());
			pstmt.setInt(2, bean.getStat1());
			pstmt.setInt(3, bean.getStat2());
			pstmt.setInt(4, bean.getStat3());
			pstmt.setInt(5, bean.getStat4());
			pstmt.setInt(6, bean.getStat5());
			pstmt.setString(7, bean.getName());
			pstmt.setFloat(8, bean.getHealth());
			pstmt.setInt(9, bean.getExp());
			pstmt.setInt(10, bean.getType());
			pstmt.setInt(11, bean.getStatus());
			pstmt.setInt(12, bean.getId());
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	public boolean updateHeroSimple(HeroBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "UPDATE castle_hero SET time=?,health=?,exp=?,status=? WHERE id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setLong(1, bean.getTime());
			pstmt.setFloat(2, bean.getHealth());
			pstmt.setInt(3, bean.getExp());
			pstmt.setInt(4, bean.getStatus());
			pstmt.setInt(5, bean.getId());
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}

	public boolean addHero(HeroBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "insert into castle_hero SET name=?,health=?,type=?,status=?,create_time=now(),uid=?,race=?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getName());
			pstmt.setFloat(2, bean.getHealth());
			pstmt.setInt(3, bean.getType());
			pstmt.setInt(4, bean.getStatus());
			pstmt.setInt(5, bean.getUid());
			pstmt.setInt(6, bean.getRace());
			db.executePstmt();
			bean.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	// 添加、修改宝物
	public boolean addArt(ArtBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "insert into castle_art SET name=?,cid=?,type=?,create_time=now(),uid=?,capture_time=?,flag=?,effect=?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getCid());
			pstmt.setInt(3, bean.getType());
			pstmt.setInt(4, bean.getUid());
			pstmt.setLong(5, bean.getCaptureTime());
			pstmt.setInt(6, bean.getFlag());
			pstmt.setInt(7, bean.getEffect());
			db.executePstmt();
			bean.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	public boolean modifyArt(ArtBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "update castle_art SET name=?,type=?,flag=?,effect=?,status=?,cid=? where id=" + bean.getId();
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getType());
			pstmt.setInt(3, bean.getFlag());
			pstmt.setInt(4, bean.getEffect());
			pstmt.setInt(5, bean.getStatus());
			pstmt.setInt(6, bean.getCid());
			db.executePstmt();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
}
