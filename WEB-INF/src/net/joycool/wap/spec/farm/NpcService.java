package net.joycool.wap.spec.farm;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain： 买卖物品等
 * @datetime:1007-10-24
 */
public class NpcService {
	// npc购买和出售的商品列表
	public FarmShopBean getShop(String cond) {
		FarmShopBean bean = new FarmShopBean();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_npc_sell WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getShop(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	public List getShopList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_shop WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getShop(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private FarmShopBean getShop(ResultSet rs) throws SQLException {
		FarmShopBean bean = new FarmShopBean();
		bean.setId(rs.getInt("id"));
		bean.setNpcId(rs.getInt("npc_id"));
		bean.setItemId(rs.getInt("item_id"));
		bean.setBuyPrice(rs.getInt("buy_price"));
		bean.setSellPrice(rs.getInt("sell_price"));
		bean.setBrush(rs.getInt("brush"));
		bean.setStack(rs.getInt("stack"));
		bean.setMaxStack(rs.getInt("max_stack"));
		bean.setFlag(rs.getInt("flag"));
		bean.setStartTime(rs.getTimestamp("start_time").getTime());
		bean.setDefBuyPrice(rs.getInt("def_buy_price"));
		bean.setDefSellPrice(rs.getInt("def_sell_price"));
		bean.setDefStack(rs.getInt("def_stack"));
		return bean;
	}
	// 驿站
	public List getCarList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_car WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getCar(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private FarmCarBean getCar(ResultSet rs) throws SQLException {
		FarmCarBean bean = new FarmCarBean();
		bean.setId(rs.getInt("id"));
		bean.setQuestId(rs.getInt("quest_id"));
		bean.setFlag(rs.getInt("flag"));
		bean.setCooldown(rs.getInt("cooldown"));
		bean.setMoney(rs.getInt("money"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setLine(rs.getString("line"));
		bean.init();
		return bean;
	}
	public List getNpcList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_npc WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getNpc(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	public FarmNpcBean getNpc(String cond) {
		FarmNpcBean bean = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_npc WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getNpc(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	private FarmNpcBean getNpc(ResultSet rs) throws SQLException {
		FarmNpcBean bean = new FarmNpcBean();
		bean.setId(rs.getInt("id"));
		bean.setDefaultType(rs.getInt("default_type"));
		bean.setName(rs.getString("name"));
		bean.setIntro(rs.getString("intro"));
		bean.setLearnSkill(rs.getString("learn_skill"));
		bean.setTalk(rs.getString("talk"));
		bean.setFlag(rs.getInt("flag"));
		bean.setQuestBegin(rs.getString("quest_begin"));
		bean.setQuestEnd(rs.getString("quest_end"));
		bean.setPos(rs.getInt("pos"));
		bean.setCars(rs.getString("cars"));
		bean.init();
		return bean;
	}
	
	private FarmUserBean getFarmUser(ResultSet rs) throws SQLException {
		FarmUserBean bean = new FarmUserBean();
		bean.setUserId(rs.getInt("user_id"));
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
	
	private FarmUserProBean getFarmUserPro(ResultSet rs) throws SQLException {
		FarmUserProBean bean = new FarmUserProBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setPro(rs.getInt("pro"));
		bean.setExp(rs.getInt("exp"));
		bean.setRank(rs.getInt("rank"));
		bean.setSkill(rs.getString("skill"));
		return bean;
	}
	public void getFarmUserPro(FarmUserBean user) {
		FarmUserProBean[] pros = user.getPro();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_user_pro WHERE user_id=" + user.getUserId();

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				int pro = rs.getInt("pro");
				if(pro >= 0 && pro < FarmUserBean.TOTAL_PRO)
					pros[pro] = getFarmUserPro(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
	}
	
	public boolean addFarmUser(FarmUserBean bean) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = "INSERT INTO farm_user(user_id) VALUES(?)";

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

		dbOp.release();
		return true;
	}
	
	
	private FactoryBean getFactory(ResultSet rs) throws SQLException {
		FactoryBean bean = new FactoryBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setRank(rs.getInt("rank"));
		bean.setInterval(rs.getInt("interval"));
		bean.setPos(rs.getInt("pos"));
		return bean;
	}
	
	// 得到工厂列表
	public List getFactoryList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_factory WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getFactory(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private FactoryProcBean getFactoryProc(ResultSet rs) throws SQLException {
		FactoryProcBean bean = new FactoryProcBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setFactoryId(rs.getInt("factory_id"));
		bean.setComposeId(rs.getInt("compose_id"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setDoneTime(rs.getTimestamp("done_time").getTime());
		bean.setStatus(rs.getInt("status"));
		return bean;
	}
	
	// 得到工厂列表
	public List getFactoryProcList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_factory_proc WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getFactoryProc(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	// 得到工厂列表
	public FactoryProcBean getFactoryProc(String cond) {
		FactoryProcBean proc = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_factory_proc WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				proc = getFactoryProc(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return proc;
	}
	
	private FactoryComposeBean getFactoryCompose(ResultSet rs) throws SQLException {
		FactoryComposeBean bean = new FactoryComposeBean();
		bean.setId(rs.getInt("id"));
		bean.setFactoryId(rs.getInt("factory_id"));
		bean.setRank(rs.getInt("rank"));
		bean.setTime(rs.getInt("time"));
		bean.setPrice(rs.getInt("price"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setMaterial(rs.getString("material"));
		bean.setProduct(rs.getString("product"));
		bean.init();
		return bean;
	}
	
	// 得到工厂公式列表
	public List getFactoryComposeList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_factory_compose WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getFactoryCompose(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}

	private FarmQuestBean getQuest(ResultSet rs) throws SQLException {
		FarmQuestBean bean = new FarmQuestBean();
		bean.setId(rs.getInt("id"));
		bean.setRank(rs.getInt("rank"));
		bean.setPrice(rs.getInt("price"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setEndInfo(rs.getString("end_info"));
		bean.setGive(rs.getString("give"));
		bean.setMaterial(rs.getString("material"));
		bean.setProduct(rs.getString("product"));
		bean.setPrize(rs.getString("prize"));
		bean.setPreQuestId(rs.getString("pre_quest_id"));
		bean.setFlag(rs.getInt("flag"));
		bean.setInterval(rs.getInt("interval"));
		bean.setCreature(rs.getString("creature"));
		bean.setTalk(rs.getString("talk"));
		bean.setSearch(rs.getString("search"));
		bean.setObjective(rs.getString("objective"));
		bean.setRequestInfo(rs.getString("request_info"));
		bean.setNext(rs.getInt("next"));
		bean.setMutex(rs.getString("mutex"));
		bean.setCondition(rs.getString("condition"));
		bean.setPreCondition(rs.getString("pre_condition"));
		bean.init();
		return bean;
	}
	
	// 得到任务列表
	public List getQuestList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_quest WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getQuest(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private FarmUserQuestBean getUserQuest(ResultSet rs) throws SQLException {
		FarmUserQuestBean bean = new FarmUserQuestBean();
		bean.setId(rs.getInt("id"));
		bean.setStatus(rs.getInt("status"));
		bean.setStartTime(rs.getTimestamp("create_time").getTime());
		bean.setDoneTime(rs.getTimestamp("done_time").getTime());
		bean.setUserId(rs.getInt("user_id"));
		bean.setQuestId(rs.getInt("quest_id"));
		bean.setNpcId(rs.getInt("npc_id"));
		return bean;
	}
	
	// 得到用户的某个任务
	public FarmUserQuestBean getUserQuest(String cond) {
		FarmUserQuestBean bean = null;
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_user_quest WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getUserQuest(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	public List getUserQuestList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_user_quest WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getUserQuest(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	public boolean addFactoryProc(FactoryProcBean proc) {
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO farm_factory_proc set user_id=?,factory_id=?,compose_id=?,status=0,create_time=now()";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, proc.getUserId());
			pstmt.setInt(2, proc.getFactoryId());
			pstmt.setInt(3, proc.getComposeId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		proc.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
	
	public boolean addUserQuest(FarmUserQuestBean userQuest) {
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO farm_user_quest set user_id=?,quest_id=?,npc_id=?,create_time=now(),status=0";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, userQuest.getUserId());
			pstmt.setInt(2, userQuest.getQuestId());
			pstmt.setInt(3, userQuest.getNpcId());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		userQuest.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}
	
	// 获取farm talk数据
	private FarmTalkBean getTalk(ResultSet rs) throws SQLException {
		FarmTalkBean bean = new FarmTalkBean();
		bean.setId(rs.getInt("id"));
		bean.setTitle(rs.getString("title"));
		bean.setContent(rs.getString("content"));
		bean.setLink(rs.getString("link"));
		bean.setQuestBegin(rs.getInt("quest_begin"));
		bean.setQuestEnd(rs.getInt("quest_end"));
		bean.setQuest(rs.getInt("quest"));
		bean.setPreQuest(rs.getInt("pre_quest"));
		bean.setCondition(rs.getString("condition"));
		bean.init();
		return bean;
	}
	
	public List getTalkList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_talk WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getTalk(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
//	 获取sone数据
	private FarmStoneBean getStone(ResultSet rs) throws SQLException {
		FarmStoneBean bean = new FarmStoneBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setValue(rs.getString("value"));
		bean.setType(rs.getInt("type"));
		bean.setPos(rs.getInt("pos"));
		bean.init();
		return bean;
	}
	
	public List getStoneList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_stone WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getStone(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private CreatureTBean getCreatureT(ResultSet rs) throws SQLException {
		CreatureTBean bean = new CreatureTBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setAttack(rs.getInt("attack"));
		bean.setDefense(rs.getInt("defense"));
		bean.setHp(rs.getInt("hp"));
		bean.setMp(rs.getInt("mp"));
		bean.setLevel(rs.getInt("level"));
		bean.setLevelRange(rs.getInt("level_range"));
		bean.setType(rs.getInt("type"));
		bean.setFlag(rs.getInt("flag"));
		bean.setDrops(rs.getString("drops"));
		bean.setCooldown(rs.getInt("cooldown"));
		bean.init();
		return bean;
	}
	
	// 得到怪物模板列表
	public List getCreatureTList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_creature WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getCreatureT(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	private CreatureSpawnBean getCreatureSpawn(ResultSet rs) throws SQLException {
		CreatureSpawnBean bean = new CreatureSpawnBean();
		bean.setId(rs.getInt("id"));
		bean.setCount(rs.getInt("count"));
		bean.setTemplateId(rs.getInt("template_id"));
		bean.setPos(rs.getString("pos"));
		bean.setCooldown(rs.getInt("cooldown"));
		bean.setFlag(rs.getInt("flag"));
		bean.init();
		return bean;
	}
	
	// 得到怪物生长列表
	public List getCreatureSpawnList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from farm_creature_spawn WHERE " + cond + " order by id";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getCreatureSpawn(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	// 添加或者更新npc
	public boolean updateNpc(FarmNpcBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_npc set name=?,intro=?,quest_begin=?,quest_end=?,talk=?,pos=?,flag=?,learn_skill=?,cars=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getIntro());
			pstmt.setString(3, bean.getQuestBegin());
			pstmt.setString(4, bean.getQuestEnd());
			pstmt.setString(5, bean.getTalk());
			pstmt.setInt(6, bean.getPos());
			pstmt.setInt(7, bean.getFlag());
			pstmt.setString(8, bean.getLearnSkill());
			pstmt.setString(9, bean.getCars());
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
	
	// 添加或者更新npc talk
	public boolean updateTalk(FarmTalkBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		String query = SqlUtil.modifySql(add, 
				"farm_talk set title=?,content=?,link=?,quest_begin=?,quest_end=?,quest=?,pre_quest=?,`condition`=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
			pstmt.setString(3, bean.getLink());
			pstmt.setInt(4, bean.getQuestBegin());
			pstmt.setInt(5, bean.getQuestEnd());
			pstmt.setInt(6, bean.getQuest());
			pstmt.setInt(7, bean.getPreQuest());
			pstmt.setString(8, bean.getCondition());
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
	
	// 添加或者更新npc quest
	public boolean updateQuest(FarmQuestBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = SqlUtil.modifySql(add, 
				"farm_quest set name=?,info=?,material=?,product=?,prize=?,end_info=?,pre_quest_id=?,rank=?,price=?,flag=?,creature=?,talk=?,search=?,give=?,objective=?,request_info=?,next=?,mutex=?,`interval`=?,`condition`=?,pre_condition=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setString(3, bean.getMaterial());
			pstmt.setString(4, bean.getProduct());
			pstmt.setString(5, bean.getPrize());
			pstmt.setString(6, bean.getEndInfo());
			pstmt.setString(7, bean.getPreQuestId());
			pstmt.setInt(8, bean.getRank());
			pstmt.setInt(9, bean.getPrice());
			pstmt.setInt(10, bean.getFlag());
			pstmt.setString(11, bean.getCreature());
			pstmt.setString(12, bean.getTalk());
			pstmt.setString(13, bean.getSearch());
			pstmt.setString(14, bean.getGive());
			pstmt.setString(15, bean.getObjective());
			pstmt.setString(16, bean.getRequestInfo());
			pstmt.setInt(17, bean.getNext());
			pstmt.setString(18, bean.getMutex());
			pstmt.setInt(19, bean.getIntervalMinute());
			pstmt.setString(20, bean.getCondition());
			pstmt.setString(21, bean.getPreCondition());
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
	
	// 添加或者更新factory
	public boolean updateFactory(FactoryBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = SqlUtil.modifySql(add, 
				"farm_factory set name=?,info=?,rank=?,`interval`=?,pos=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setInt(3, bean.getRank());
			pstmt.setInt(4, bean.getInterval());
			pstmt.setInt(5, bean.getPos());
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
	
	// 添加或者更新npc quest
	public boolean updateFactoryCompose(FactoryComposeBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = SqlUtil.modifySql(add, 
				"farm_factory_compose set factory_id=?,name=?,info=?,material=?,product=?,rank=?,price=?,time=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFactoryId());
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getInfo());
			pstmt.setString(4, bean.getMaterial());
			pstmt.setString(5, bean.getProduct());
			pstmt.setInt(6, bean.getRank());
			pstmt.setInt(7, bean.getPrice());
			pstmt.setInt(8, bean.getTime());
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
	
	// 添加或者更新npc shop买卖的物品
	public boolean updateShop(FarmShopBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = SqlUtil.modifySql(add, 
				"farm_shop set npc_id=?,item_id=?,brush=?,buy_price=?,sell_price=?,stack=?,max_stack=?,flag=?,def_buy_price=?,def_sell_price=?,def_stack=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getNpcId());
			pstmt.setInt(2, bean.getItemId());
			pstmt.setInt(3, bean.getBrush());
			pstmt.setInt(4, bean.getBuyPrice());
			pstmt.setInt(5, bean.getSellPrice());
			pstmt.setInt(6, bean.getStack());
			pstmt.setInt(7, bean.getMaxStack());
			pstmt.setInt(8, bean.getFlag());
			pstmt.setInt(9, bean.getDefBuyPrice());
			pstmt.setInt(10, bean.getDefSellPrice());
			pstmt.setInt(11, bean.getDefStack());
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
	
	// 添加或者更新creature template买卖的物品
	public boolean updateCreatureT(CreatureTBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = SqlUtil.modifySql(add, 
				"farm_creature set name=?,info=?,hp=?,mp=?,attack=?,level=?,level_range=?,defense=?,type=?,drops=?,flag=?,cooldown=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setInt(3, bean.getHp());
			pstmt.setInt(4, bean.getMp());
			pstmt.setInt(5, bean.getAttack());
			pstmt.setInt(6, bean.getLevel());
			pstmt.setInt(7, bean.getLevelRange());
			pstmt.setInt(8, bean.getDefense());
			pstmt.setInt(9, bean.getType());
			pstmt.setString(10, bean.getDrops());
			pstmt.setInt(11, bean.getFlag());
			pstmt.setInt(12, bean.getCooldown());
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
	
	// 添加或者更新creature spawn
	public boolean updateCreatureSpawn(CreatureSpawnBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = SqlUtil.modifySql(add, 
				"farm_creature_spawn set template_id=?,pos=?,count=?,cooldown=?,flag=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getTemplateId());
			pstmt.setString(2, bean.getPos());
			pstmt.setInt(3, bean.getCount());
			pstmt.setInt(4, bean.getCooldown());
			pstmt.setInt(5, bean.getFlag());
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
	
	// 添加或者更新stone信息
	public boolean updateStone(FarmStoneBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = SqlUtil.modifySql(add, 
				"farm_stone set name=?,info=?,pos=?,type=?,value=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setInt(3, bean.getPos());
			pstmt.setInt(4, bean.getType());
			pstmt.setString(5, bean.getValue());
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
	
	// 添加或者更新驿站线路信息
	public boolean updateCar(FarmCarBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(4);
		
		String query = SqlUtil.modifySql(add, 
				"farm_car set name=?,info=?,line=?,flag=?,money=?,quest_id=?,cooldown=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setString(3, bean.getLine());
			pstmt.setInt(4, bean.getFlag());
			pstmt.setInt(5, bean.getMoney());
			pstmt.setInt(6, bean.getQuestId());
			pstmt.setInt(7, bean.getCooldown());
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
}
