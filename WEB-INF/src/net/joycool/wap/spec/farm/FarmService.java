package net.joycool.wap.spec.farm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain： 采集
 * @datetime:1007-10-24
 */
public class FarmService {
	// 得到作物列表
	public List getCropList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_crop WHERE " + cond + " order by rank,id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getCrop(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private FarmCropBean getCrop(ResultSet rs) throws SQLException {
		FarmCropBean bean = new FarmCropBean();
		bean.setId(rs.getInt("id"));
		bean.setProId(rs.getInt("pro_id"));
		bean.setName(rs.getString("name"));
		bean.setGrowTime(rs.getInt("grow_time"));
		bean.setRotTime(rs.getInt("rot_time"));
		bean.setRank(rs.getInt("rank"));
		bean.setProduct(rs.getInt("product"));
		bean.setActInterval(rs.getInt("act_interval"));
		bean.setProductCount(rs.getInt("product_count"));
		return bean;
	}
	
	// 得到作物田列表
	public List getFieldList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_field WHERE " + cond + " order by rank";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getField(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	public FarmFieldBean getField(String cond) {
		FarmFieldBean bean = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_field WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getField(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	private FarmFieldBean getField(ResultSet rs) throws SQLException {
		FarmFieldBean bean = new FarmFieldBean();
		bean.setId(rs.getInt("id"));
		bean.setStartTime(rs.getTimestamp("start_time").getTime());
		bean.setLastActTime(rs.getTimestamp("last_act_time").getTime());
		bean.setUserId(rs.getInt("user_id"));
		bean.setCropId(rs.getInt("crop_id"));
		bean.setActCount(rs.getInt("act_count"));
		return bean;
	}
	// 得到套装列表
	public List getItemSetList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from item_set WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getItemSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	private ItemSetBean getItemSet(ResultSet rs) throws SQLException {
		ItemSetBean bean = new ItemSetBean();
		bean.setId(rs.getInt("id"));
		bean.setFlag(rs.getInt("flag"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setAttribute(rs.getString("attribute"));
		bean.setItems(rs.getString("items"));
		bean.setCount(rs.getString("count"));
		bean.init();
		return bean;
	}
	public boolean addField(FarmFieldBean bean) {
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO farm_field(user_id,crop_id,start_time) VALUES(?,0,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		bean.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
	// 得到畜牧业列表
	public List getFeedList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_feed WHERE " + cond + " order by rank";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getFeed(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	public FarmFeedBean getFeed(String cond) {
		FarmFeedBean bean = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_feed WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getFeed(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	private FarmFeedBean getFeed(ResultSet rs) throws SQLException {
		FarmFeedBean bean = new FarmFeedBean();
		bean.setId(rs.getInt("id"));
		bean.setStartTime(rs.getTimestamp("start_time").getTime());
		bean.setLastActTime(rs.getTimestamp("last_act_time").getTime());
		bean.setUserId(rs.getInt("user_id"));
		bean.setCropId(rs.getInt("crop_id"));
		bean.setActCount(rs.getInt("act_count"));
		return bean;
	}
	public boolean addFeed(FarmFeedBean bean) {
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO farm_feed(user_id,crop_id,start_time) VALUES(?,0,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		bean.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
	
	private FarmUserBean getFarmUser(ResultSet rs) throws SQLException {
		FarmUserBean bean = new FarmUserBean();
		bean.setUserId(rs.getInt("user_id"));
		bean.setMoney(rs.getInt("money"));
		bean.setProPoint(rs.getInt("pro_point"));
		bean.setExp(rs.getInt("exp"));
		bean.setRank(rs.getInt("rank"));
		bean.setMaxRank(rs.getInt("max_rank"));
		bean.setPos(rs.getInt("pos"));
		bean.setName(rs.getString("name"));
		
		bean.setClass1(rs.getInt("class1"));
		bean.setElement(rs.getInt("element"));
		
		bean.setAttr1(rs.getInt("attr1"));
		bean.setAttr2(rs.getInt("attr2"));
		bean.setAttr3(rs.getInt("attr3"));
		bean.setAttr4(rs.getInt("attr4"));
		bean.setAttr5(rs.getInt("attr5"));
		bean.setBattlePoint(rs.getInt("battle_point"));
		bean.setBindPos(rs.getInt("bind_pos"));
		bean.setQuestCreature(rs.getString("quest_creature"));
		return bean;
	}
	public FarmUserBean getFarmUser(String condition) {
		FarmUserBean user = null;

		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_user WHERE " + condition;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				user = this.getFarmUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return user;
	}
	
	// 获得用户的身上装备
	private FarmUserEquipBean getFarmUserEquip(ResultSet rs) throws SQLException {
		FarmUserEquipBean bean = new FarmUserEquipBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setUserbagId(rs.getInt("userbag_id"));
		bean.setPart(rs.getInt("part"));
		return bean;
	}
	public void getFarmUserEquip(FarmUserBean user) {
		FarmUserEquipBean[] equips = user.getEquip();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_user_equip WHERE user_id=" + user.getUserId();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				FarmUserEquipBean bean = getFarmUserEquip(rs);
				if(bean.getPart() >= 0 && bean.getPart() < FarmUserBean.TOTAL_PART)
					equips[bean.getPart()] = bean;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
	}
	
//	 获得用户的专业
	private FarmUserProBean getFarmUserPro(ResultSet rs) throws SQLException {
		FarmUserProBean bean = new FarmUserProBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setPro(rs.getInt("pro_id"));
		bean.setExp(rs.getInt("exp"));
		bean.setRank(rs.getInt("rank"));
		bean.setSkill(rs.getString("skill"));
		bean.setMaxRank(rs.getInt("max_rank"));
		return bean;
	}
	public void getFarmUserPro(FarmUserBean user) {
		FarmUserProBean[] pros = user.getPro();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_user_pro WHERE user_id=" + user.getUserId();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				int pro = rs.getInt("pro_id");
				if(pro >= 0 && pro < FarmUserBean.TOTAL_PRO)
					pros[pro] = getFarmUserPro(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
	}
	
	public void removeUserPro(int id) {
		SqlUtil.executeUpdate("delete from farm_user_pro where id=" + id, 4);
	}
	
	public void addUserPro(FarmUserProBean pro) {
		DbOperation dbOp = new DbOperation(4);
		dbOp.executeUpdate("insert into farm_user_pro set rank=1,exp=0,user_id=" + pro.getUserId() + ",pro_id=" + pro.getPro() + ",max_rank=" + pro.getMaxRank());
		pro.setId(dbOp.getLastInsertId());
		dbOp.release();
	}
	
	public boolean addFarmUser(FarmUserBean bean) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = "INSERT INTO farm_user(create_datetime,user_id,name,money,rank,exp,max_rank,pro_point) VALUES(now(),?,?,?,?,?,?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setString(2, bean.getName());
			pstmt.setInt(3, bean.getMoney());
			pstmt.setInt(4, bean.getRank());
			pstmt.setInt(5, bean.getExp());
			pstmt.setInt(6, bean.getMaxRank());
			pstmt.setInt(7, bean.getProPoint());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	
	public boolean updateFarmUser(String set, String cond) {
		DbOperation dbOp = new DbOperation(4);
		
		String sql = "update farm_user set " + set + " where " + cond;

		boolean res = dbOp.executeUpdate(sql);

		dbOp.release();
		return res;
	}
	
	// 得到专业技能列表
	public List getProList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_pro WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getPro(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private FarmProBean getPro(ResultSet rs) throws SQLException {
		FarmProBean bean = new FarmProBean();
		bean.setId(rs.getInt("id"));
		bean.setMaxRank(rs.getInt("max_rank"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setPoint(rs.getInt("point"));
		return bean;
	}
	
	private LandMapBean getLand(ResultSet rs) throws SQLException {
		LandMapBean bean = new LandMapBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setWidth(rs.getInt("width"));
		bean.setHeight(rs.getInt("height"));
		bean.setGrid(rs.getString("grid"));
		bean.setRank(rs.getInt("rank"));
		bean.setPos(rs.getInt("pos"));
		bean.setItem1(rs.getString("item1"));
		bean.setItem1Grid(rs.getString("item1_grid"));
		bean.setItem2(rs.getString("item2"));
		bean.setItem2Grid(rs.getString("item2_grid"));
		bean.init();
		return bean;
	}
	
	// 得到采集地图列表
	public List getLandList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_land WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getLand(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	// 得到专业技能
	public List getSkillList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_skill WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getSkill(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private FarmSkillBean getSkill(ResultSet rs) throws SQLException {
		FarmSkillBean bean = new FarmSkillBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setRank(rs.getInt("rank"));
		bean.setPrice(rs.getInt("price"));
		bean.setProId(rs.getInt("pro_id"));
		bean.setProNpc(rs.getInt("pro_npc"));
		bean.setType(rs.getInt("type"));
		bean.setMaterial(rs.getString("material"));
		bean.setProduct(rs.getString("product"));
		bean.setCost(rs.getString("cost"));
		bean.setEffect(rs.getString("effect"));
		bean.setCooldown(rs.getInt("cooldown"));
		bean.setCooldownId(rs.getInt("cooldown_id"));
		bean.setClass1(rs.getInt("class1"));
		bean.setElement(rs.getInt("element"));
		bean.setFlag(rs.getInt("flag"));
		bean.init();
		return bean;
	}
	
	// 得到采集的物品列表
	public List getLandItemList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_land_item WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getLandItem(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private LandItemBean getLandItem(ResultSet rs) throws SQLException {
		LandItemBean bean = new LandItemBean();
		bean.setId(rs.getInt("id"));
		bean.setRank(rs.getInt("rank"));
		bean.setProId(rs.getInt("pro_id"));
		bean.setMin(rs.getInt("min"));
		bean.setMax(rs.getInt("max"));
		bean.setItemId(rs.getInt("item_id"));
		bean.setName(rs.getString("name"));
		return bean;
	}
	
	// 得到地图列表
	public List getMapList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_map WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getMap(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private MapBean getMap(ResultSet rs) throws SQLException {
		MapBean bean = new MapBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setEntryNode(rs.getInt("entry_node"));
		bean.setX(rs.getInt("x"));
		bean.setY(rs.getInt("y"));
		bean.setExp(rs.getInt("exp"));
		bean.setFlag(rs.getInt("flag"));
		bean.setRank(rs.getInt("rank"));
		bean.setSp(rs.getInt("sp"));
		bean.setCooldown(rs.getInt("cooldown"));
		bean.setAttackCount(rs.getInt("attack_count"));
		bean.setParent(rs.getInt("parent"));
		bean.setMaxPlayer(rs.getInt("max_player"));
		bean.setCondition(rs.getString("condition"));
		bean.setCity(rs.getInt("city"));
		bean.init();
		return bean;
	}
	
	// 得到地图结点列表
	public List getMapNodeList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_map_node WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getMapNode(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private MapNodeBean getMapNode(ResultSet rs) throws SQLException {
		MapNodeBean bean = new MapNodeBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setMapId(rs.getInt("map_id"));
		bean.setExp(rs.getInt("exp"));
		bean.setLink(rs.getString("link"));
		bean.init();
		return bean;
	}
	
//	 得到地图路标列表
	public List getMapSignList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_map_sign WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getMapSign(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private MapSignBean getMapSign(ResultSet rs) throws SQLException {
		MapSignBean bean = new MapSignBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setPosId(rs.getInt("pos_id"));
		bean.setDistance(rs.getInt("dist2"));
		bean.setFlag(rs.getInt("flag"));
		bean.init();
		return bean;
	}

	// 得到picktbean
	public List getPickTList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_pick2 WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getPickT(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private PickTBean getPickT(ResultSet rs) throws SQLException {
		PickTBean bean = new PickTBean();
		bean.setId(rs.getInt("id"));
		bean.setPos(rs.getString("pos"));
		bean.setItems(rs.getString("items"));
		bean.setCooldown(rs.getInt("cooldown"));
		bean.init();
		return bean;
	}

	public boolean addUserEquip(FarmUserEquipBean equip) {
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO farm_user_equip(user_id,part,userbag_id) VALUES(?,?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, equip.getUserId());
			pstmt.setInt(2, equip.getPart());
			pstmt.setInt(3, equip.getUserbagId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		equip.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
	
	// 得到冷却map
	public HashMap getCooldownMap(String cond) {
		HashMap map = new HashMap();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_cooldown WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				CooldownBean bean = getCooldown(rs);
				map.put(Integer.valueOf(bean.getCooldownId()), bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return map;
	}
	
	private CooldownBean getCooldown(ResultSet rs) throws SQLException {
		CooldownBean bean = new CooldownBean();
		bean.setUserId(rs.getInt("user_id"));
		bean.setTime(rs.getTimestamp("time").getTime());
		bean.setCooldownId(rs.getInt("cooldown_id"));
		return bean;
	}

	public void getFarmCooldown(FarmUserBean user) {
		HashMap map = user.getCooldownMap();
		map.clear();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_cooldown WHERE user_id=" + user.getUserId();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				CooldownBean bean = getCooldown(rs);
				map.put(Integer.valueOf(bean.getCooldownId()), bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		user.setCooldownMap(map);
	}
	
	// 得到书本列表
	public List getBookList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_book WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getBook(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private FarmBookBean getBook(ResultSet rs) throws SQLException {
		FarmBookBean bean = new FarmBookBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setContent(rs.getString("content"));
		bean.setPos(rs.getInt("pos"));
		return bean;
	}
	
	// 得到收藏列表
	public List getCollectList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_collect WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getCollect(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private CollectBean getCollect(ResultSet rs) throws SQLException {
		CollectBean bean = new CollectBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setType(rs.getInt("type"));
		bean.setItems(rs.getString("items"));
		bean.setRank(rs.getInt("rank"));
		bean.setPrice(rs.getInt("price"));
		bean.init();
		return bean;
	}
	
	// 得到用户收藏列表
	public UserCollectBean getUserCollect(String cond) {
		UserCollectBean bean = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_user_collect WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getUserCollect(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	private UserCollectBean getUserCollect(ResultSet rs) throws SQLException {
		UserCollectBean bean = new UserCollectBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setCollected(rs.getInt("collected"));
		bean.setCount(rs.getInt("count"));
		bean.setCollectId(rs.getInt("collect_id"));
		bean.setStartTime(rs.getTimestamp("start_time").getTime());
		bean.setFinishTime(rs.getTimestamp("finish_time").getTime());
		return bean;
	}
	
	// 得到用户荣誉
	public UserHonorBean getUserHonor(String cond) {
		UserHonorBean bean = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_user_honor WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getUserHonor(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	private UserHonorBean getUserHonor(ResultSet rs) throws SQLException {
		UserHonorBean bean = new UserHonorBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setArena(rs.getInt("arena"));
		bean.setHonor(rs.getInt("honor"));
		bean.setHonorWeek(rs.getInt("honor_week"));
		bean.setHonorLast(rs.getInt("honor_last"));
		bean.setHonorHigh(rs.getInt("honor_high"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
	
	// 得到大地图采集物品
	public List getPickList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_pick WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getPick(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private MapPickBean getPick(ResultSet rs) throws SQLException {
		MapPickBean bean = new MapPickBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setPos(rs.getInt("pos"));
		bean.setQuestId(rs.getInt("quest_id"));
		bean.setItems(rs.getString("items"));
		bean.init();
		return bean;
	}
	
	// 得到触发器
	public List getTriggerList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_trigger WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getTrigger(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private TriggerBean getTrigger(ResultSet rs) throws SQLException {
		TriggerBean bean = new TriggerBean();
		bean.setId(rs.getInt("id"));
		bean.setFlag(rs.getInt("flag"));
		bean.setName(rs.getString("name"));
		bean.setBak(rs.getString("bak"));
		bean.setEvent(rs.getString("event"));
		bean.setCondition(rs.getString("condition"));
		bean.setAction(rs.getString("action"));
		bean.setInfo(rs.getString("info"));
		bean.setInterval(rs.getInt("interval"));
		bean.init();
		return bean;
	}
	
	// 添加或者更新地图，用于后台修改
	public boolean updateMapNode(MapNodeBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_map_node set map_id=?,name=?,info=?,link=?,exp=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getMapId());
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getInfo());
			pstmt.setString(4, bean.getLink());
			pstmt.setInt(5, bean.getExp());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	// 添加或者更新地图路标，用于后台修改
	public boolean updateMapSign(MapSignBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_map_sign set name=?,info=?,flag=?,pos_id=?,dist2=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setInt(3, bean.getFlag());
			pstmt.setInt(4, bean.getPosId());
			pstmt.setInt(5, bean.getDistance());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	// 添加或者更新skill，用于后台修改
	public boolean updateSkill(FarmSkillBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_skill set pro_id=?,name=?,info=?,rank=?,material=?,product=?,price=?,cooldown=?,cooldown_id=?,class1=?,element=?,flag=?,cost=?,effect=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getProId());
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getInfo());
			pstmt.setInt(4, bean.getRank());
			pstmt.setString(5, bean.getMaterial());
			pstmt.setString(6, bean.getProduct());
			pstmt.setInt(7, bean.getPrice());
			pstmt.setInt(8, bean.getCooldownMinute());
			pstmt.setInt(9, bean.getCooldownId());
			pstmt.setInt(10, bean.getClass1());
			pstmt.setInt(11, bean.getElement());
			pstmt.setInt(12, bean.getFlag());
			pstmt.setString(13, bean.getCost());
			pstmt.setString(14, bean.getEffect());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	// 添加或者更新pro，用于后台修改
	public boolean updatePro(FarmProBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_pro set max_rank=?,name=?,info=?,point=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getMaxRank());
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getInfo());
			pstmt.setInt(4, bean.getPoint());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	// 添加或者更新crop，用于后台修改
	public boolean updateCrop(FarmCropBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_crop set name=?,rank=?,grow_time=?,rot_time=?,product=?,act_interval=?,product_count=?,pro_id=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getRank());
			pstmt.setInt(3, bean.getGrowTime());
			pstmt.setInt(4, bean.getRotTime());
			pstmt.setInt(5, bean.getProduct());
			pstmt.setInt(6, bean.getActInterval());
			pstmt.setInt(7, bean.getProductCount());
			pstmt.setInt(8, bean.getProId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	// 添加或者更新land，用于后台修改
	public boolean updateLand(LandMapBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_land set name=?,width=?,height=?,grid=?,rank=?,item1=?,item1_grid=?,item2=?,item2_grid=?,pos=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getWidth());
			pstmt.setInt(3, bean.getHeight());
			pstmt.setString(4, bean.getGrid());
			pstmt.setInt(5, bean.getRank());
			pstmt.setString(6, bean.getItem1());
			pstmt.setString(7, bean.getItem1Grid());
			pstmt.setString(8, bean.getItem2());
			pstmt.setString(9, bean.getItem2Grid());
			pstmt.setInt(10, bean.getPos());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	// 添加或者更新land_item，用于后台修改
	public boolean updateLandItem(LandItemBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_land_item set name=?,item_id=?,pro_id=?,rank=?,min=?,max=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getItemId());
			pstmt.setInt(3, bean.getProId());
			pstmt.setInt(4, bean.getRank());
			pstmt.setInt(5, bean.getMin());
			pstmt.setInt(6, bean.getMax());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public boolean updateItem(DummyProductBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"item set name=?,introduction=?,mode=?,value=?,time=?" +
				",dummy_id=?,price=?,description=?,mark=?,start_time=now(),brush=?" +
				",buy_price=?,bind=?,due=?,rank=?,seq=?,stack=?,`unique`=?,cooldown=?," +
				"class1=?,class2=?,attribute=?,`usage`=?,grade=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getIntroduction());
			pstmt.setInt(3, bean.getMode());
			pstmt.setInt(4, bean.getValue());
			pstmt.setInt(5, bean.getTime());
			pstmt.setInt(6, bean.getDummyId());
			pstmt.setInt(7, bean.getPrice());
			pstmt.setString(8, bean.getDescription());
			pstmt.setInt(9, bean.getMark());
			pstmt.setInt(10, bean.getBrush());
			pstmt.setInt(11, bean.getBuyPrice());
			pstmt.setInt(12, bean.getBind());
			pstmt.setInt(13, bean.getDue());
			pstmt.setInt(14, bean.getRank());
			pstmt.setInt(15, bean.getSeq());
			pstmt.setInt(16, bean.getStack());
			pstmt.setInt(17, bean.getUnique());
			pstmt.setInt(18, bean.getCooldown());
			pstmt.setInt(19, bean.getClass1());
			pstmt.setInt(20, bean.getClass2());
			pstmt.setString(21, bean.getAttribute());
			pstmt.setString(22, bean.getUsage());
			pstmt.setInt(23, bean.getGrade());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public boolean updateBook(FarmBookBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_book set name=?,content=?,pos=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getPos());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public boolean updatePick(MapPickBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_pick set name=?,info=?,pos=?,items=?,quest_id=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setInt(3, bean.getPos());
			pstmt.setString(4, bean.getItems());
			pstmt.setInt(5, bean.getQuestId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public boolean updateMap(MapBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_map set name=?,info=?,entry_node=?,x=?,y=?,exp=?,flag=?,rank=?,sp=?,cooldown=?,attack_count=?,parent=?,max_player=?,`condition`=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setInt(3, bean.getEntryNode());
			pstmt.setInt(4, bean.getX());
			pstmt.setInt(5, bean.getY());
			pstmt.setInt(6, bean.getExp());
			pstmt.setInt(7, bean.getFlag());
			pstmt.setInt(8, bean.getRank());
			pstmt.setInt(9, bean.getSp());
			pstmt.setInt(10, bean.getCooldown());
			pstmt.setInt(11, bean.getAttackCount());
			pstmt.setInt(12, bean.getParent());
			pstmt.setInt(13, bean.getMaxPlayer());
			pstmt.setString(14, bean.getCondition());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public boolean updateItemSet(ItemSetBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"item_set set name=?,info=?,items=?,attribute=?,count=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setString(3, bean.getItems());
			pstmt.setString(4, bean.getAttribute());
			pstmt.setString(5, bean.getCount());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public boolean updatePickT(PickTBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_pick2 set pos=?,items=?,cooldown=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getPos());
			pstmt.setString(2, bean.getItems());
			pstmt.setInt(3, bean.getCooldown());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public boolean updateCollect(CollectBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_collect set name=?,info=?,items=?,type=?,rank=?,price=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setString(3, bean.getItems());
			pstmt.setInt(4, bean.getType());
			pstmt.setInt(5, bean.getRank());
			pstmt.setInt(6, bean.getPrice());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public boolean updateTrigger(TriggerBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_trigger set name=?,bak=?,flag=?,event=?,`condition`=?,action=?,info=?,`interval`=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getBak());
			pstmt.setInt(3, bean.getFlag());
			pstmt.setString(4, bean.getEvent());
			pstmt.setString(5, bean.getCondition());
			pstmt.setString(6, bean.getAction());
			pstmt.setString(7, bean.getInfo());
			pstmt.setInt(8, bean.getIntervalMinute());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}

	public boolean addUserCollect(UserCollectBean bean) {
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO farm_user_collect(user_id,collect_id,start_time,finish_time) VALUES(?,?,now(),now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getCollectId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		bean.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
	
	public boolean addUserHonor(UserHonorBean bean) {
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO farm_user_honor(user_id,arena,create_time,honor,honor_week,honor_last,honor_high) VALUES(?,?,now(),?,?,?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getArena());
			pstmt.setInt(3, bean.getHonor());
			pstmt.setInt(4, bean.getHonorWeek());
			pstmt.setInt(5, bean.getHonorLast());
			pstmt.setInt(6, bean.getHonorHigh());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		bean.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
	
	private UserTriggerBean getUserTrigger(ResultSet rs) throws SQLException {
		UserTriggerBean bean = new UserTriggerBean();
		bean.setId(rs.getInt("id"));
		bean.setTriggerTime(rs.getTimestamp("trigger_time").getTime());
		bean.setUserId(rs.getInt("user_id"));
		bean.setTriggerId(rs.getInt("trigger_id"));
		return bean;
	}
	
	// 得到用户的某个任务
	public UserTriggerBean getUserTrigger(String cond) {
		UserTriggerBean bean = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_user_trigger WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getUserTrigger(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	// 完成某个触发
	public boolean addUserTrigger(UserTriggerBean userTrigger) {
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO farm_user_trigger set user_id=?,trigger_id=?,trigger_time=now()";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, userTrigger.getUserId());
			pstmt.setInt(2, userTrigger.getTriggerId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		userTrigger.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
}
