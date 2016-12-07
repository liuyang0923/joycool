package jc.family.game.yard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class YardService {

	public int getIntResult(String sql) {
		DbOperation db = new DbOperation(5);
		try {
			return db.getIntResult(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return 0;
	}

	public List getIntListResult(String sql) {
		DbOperation db = new DbOperation(5);
		try {
			return db.getIntList(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public int upd(String sql) {
		DbOperation db = new DbOperation(5);
		try {
			db.executeUpdate(sql);
			return db.getLastInsertId();
		} finally {
			db.release();
		}
	}

	public YardItemProtoBean getYardItemProtoBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_item_proto where " + cond);
		try {
			if (rs.next()) {
				return getYardItemProtoBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardItemProtoBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_item_proto where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardItemProtoBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private YardItemProtoBean getYardItemProtoBean(ResultSet rs) throws SQLException {
		YardItemProtoBean bean = new YardItemProtoBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setPrice(rs.getInt("price"));
		bean.setFlags(rs.getInt("flags"));
		bean.setType(rs.getInt("t_type"));
		bean.setProduct(rs.getString("product"));
		bean.setBuyCount(rs.getInt("buy_count"));
		bean.setRank(rs.getInt("rank"));
		bean.setBasePrice(rs.getInt("base_price"));
		bean.setRank(rs.getInt("rank"));
		bean.setCapacity(rs.getInt("capacity"));
		bean.setTime(rs.getInt("r_time"));
		return bean;
	}
	
	public YardRecipeProtoBean getYardRecipeProtoBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_recipe_proto where " + cond);
		try {
			if (rs.next()) {
				return getYardRecipeProtoBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardRecipeProtoBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_recipe_proto where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardRecipeProtoBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private YardRecipeProtoBean getYardRecipeProtoBean(ResultSet rs) throws SQLException {
		YardRecipeProtoBean bean = new YardRecipeProtoBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setPrice(rs.getInt("price"));
		bean.setStuff(rs.getString("stuff"));
		bean.setDescribe(rs.getString("d_describe"));
		bean.setTime(rs.getInt("r_time"));
		bean.setRank(rs.getInt("rank"));
		bean.setMaterial(rs.getString("material"));
		bean.setProduct(rs.getString("product"));
		bean.setType(rs.getInt("type"));
		return bean;
	}

	
	public YardPlantProtoBean getYardPlantProtoBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_plant_proto where " + cond);
		try {
			if (rs.next()) {
				return getYardPlantProtoBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardPlantProtoBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_plant_proto where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardPlantProtoBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private YardPlantProtoBean getYardPlantProtoBean(ResultSet rs) throws SQLException {
		YardPlantProtoBean bean = new YardPlantProtoBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setPrice(rs.getInt("price"));
		bean.setDescribe(rs.getString("describe"));
		bean.setCanPlants(rs.getString("can_plants"));
		bean.setType(rs.getInt("type"));
		return bean;
	}
	
	public YardBean getYardBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_info where " + cond);
		try {
			if (rs.next()) {
				return getYardBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_info where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private YardBean getYardBean(ResultSet rs) throws SQLException {
		YardBean bean = new YardBean();
		bean.setFmid(rs.getInt("fmid"));
		bean.setMoney(rs.getInt("money"));
		bean.setCreateTime(rs.getTimestamp("createtime").getTime());
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setRank(rs.getInt("rank"));
		bean.setExp(rs.getInt("exp"));
		bean.setLandRank(rs.getInt("land_rank"));
		bean.setKitchenRank(rs.getInt("kitchen_rank"));
		bean.setKitchenRank2(rs.getInt("kitchen_rank2"));
		bean.setMateRank(rs.getInt("mate_rank"));
		bean.setKitchenRank3(rs.getInt("kitchen_rank3"));
		bean.setFactoryRank(rs.getInt("factory_rank"));
		return bean;
	}

	public YardItemBean getYardItemBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_fm_item where " + cond);
		try {
			if (rs.next()) {
				return getYardItemBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardItemBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_fm_item where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardItemBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private YardItemBean getYardItemBean(ResultSet rs) throws SQLException {
		YardItemBean bean = new YardItemBean();
		bean.setId(rs.getInt("id"));
		bean.setFmid(rs.getInt("fmid"));
		bean.setItemId(rs.getInt("itemid"));
		bean.setNumber(rs.getInt("number"));
		return bean;
	}

	public CookBean getCookBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_cook where " + cond);
		try {
			if (rs.next()) {
				return getCookBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getCookBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_cook where " + cond);
		try {
			while (rs.next()) {
				list.add(getCookBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private CookBean getCookBean(ResultSet rs) throws SQLException {
		CookBean bean = new CookBean();
		bean.setId(rs.getInt("id"));
		bean.setFmid(rs.getInt("fmid"));
		bean.setCreateTime(rs.getTimestamp("createTime").getTime());
		bean.setRecipeId(rs.getInt("recipeid"));
		return bean;
	}

	public FmItemLogBean getFmItmeLogBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_item_log where " + cond);
		try {
			if (rs.next()) {
				return getFmItmeLogBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getFmItmeLogBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_item_log where " + cond);
		try {
			while (rs.next()) {
				list.add(getFmItmeLogBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private FmItemLogBean getFmItmeLogBean(ResultSet rs) throws SQLException {
		FmItemLogBean bean = new FmItemLogBean();
		bean.setId(rs.getInt("id"));
		bean.setFmid(rs.getInt("fmid"));
		bean.setCreateTime(rs.getTimestamp("createTime").getTime());
		bean.setUserid(rs.getInt("userid"));
		bean.setItemId(rs.getInt("itemid"));
		bean.setType(rs.getInt("t_type"));
		return bean;
	}

	public YardUserBean getYardUserBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_user_log where " + cond);
		try {
			if (rs.next()) {
				return getYardUserBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardUserBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_user_log where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardUserBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public YardUserBean getYardUserBean2(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_user_log2 where " + cond);
		try {
			if (rs.next()) {
				return getYardUserBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardUserBeanList2(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_user_log2 where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardUserBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private YardUserBean getYardUserBean(ResultSet rs) throws SQLException {
		YardUserBean bean = new YardUserBean();
		bean.setId(rs.getInt("id"));
		bean.setUserid(rs.getInt("userid"));
		bean.setFmid(rs.getInt("fmid"));
		bean.setSeedCount(rs.getInt("seed_count"));
		return bean;
	}

	public YardCookBean getYardCookBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_cook where " + cond);
		try {
			if (rs.next()) {
				return getYardCookBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardCookBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_cook where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardCookBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private YardCookBean getYardCookBean(ResultSet rs) throws SQLException {
		YardCookBean bean = new YardCookBean();
		bean.setId(rs.getInt("id"));
		bean.setFmid(rs.getInt("fmid"));
		bean.setCreateTime(rs.getTimestamp("createTime").getTime());
		bean.setRecipeid(rs.getInt("recipeid"));
		return bean;
	}

	public boolean updateYardItemProtoBean(YardItemProtoBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(5);
		String query = SqlUtil
				.modifySql(
						add,
						"fm_yard_item_proto set name=?,price=?,flags=?,t_type=?,product=?,info=?,buy_count=?,r_time=?,base_price=?,capacity=?,rank=?",
						bean.getId());

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.name);
			pstmt.setInt(2, bean.getPrice());
			pstmt.setInt(3, bean.getFlags());
			pstmt.setInt(4, bean.getType());
			pstmt.setString(5, bean.getProduct());
			pstmt.setString(6, "");
			pstmt.setInt(7, bean.getBuyCount());
			pstmt.setInt(8, bean.getTime());
			pstmt.setInt(9, bean.getBasePrice());
			pstmt.setInt(10, bean.getCapacity());
			pstmt.setInt(11, bean.getRank());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		if (add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}

	public boolean updateYardRecipeProtoBean(YardRecipeProtoBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(5);
		String query = SqlUtil.modifySql(add,
				"fm_yard_recipe_proto set name=?,price=?,stuff=?,d_describe=?,r_time=?,material=?,product=?,rank=?,`type`=?",
				bean.getId());

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.name);
			pstmt.setInt(2, bean.getPrice());
			pstmt.setString(3, bean.getStuff());
			pstmt.setString(4, bean.getDescribe());
			pstmt.setInt(5, bean.getTime());
			pstmt.setString(6, bean.getMaterial());
			pstmt.setString(7, bean.getProduct());
			pstmt.setInt(8, bean.getRank());
			pstmt.setInt(9, bean.getType());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		if (add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	public YardLandBean getYardLandBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_fm_land where " + cond);
		try {
			if (rs.next()) {
				return getYardLandBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardLandBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_fm_land where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardLandBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	/**
	 * 给N块地
	 * @param count
	 * @param fmid
	 */
	public void addLand(int count, YardBean yard){
		DbOperation db = new DbOperation(5);
		if (count > 0){
			for (int i = 0 ; i < count ; i++){
				YardLandBean land = new YardLandBean();
				land.setFmid(yard.getFmid());
				db.executeUpdate("insert into fm_yard_fm_land (fmid,max_count,rank) values (" + yard.getFmid() +",0,0)");
				land.setId(db.getLastInsertId());
				yard.getLandList().add(land);
			}
		}
		db.release();
	}

	private YardLandBean getYardLandBean(ResultSet rs) throws SQLException {
		YardLandBean bean = new YardLandBean();
		bean.setId(rs.getInt("id"));
		bean.setFmid(rs.getInt("fmid"));
		bean.setPlantTime(rs.getTimestamp("plant_time").getTime());
		bean.setCount(rs.getInt("count"));
		bean.setRank(rs.getInt("rank"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setSeedId(rs.getInt("seed_id"));
		bean.setType(rs.getInt("type"));
		return bean;
	}

	public YardPlantBean getYardPlantBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_fm_plant where " + cond);
		try {
			if (rs.next()) {
				return getYardPlantBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardPlantBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_fm_plant where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardPlantBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	/**
	 * 给N间车间
	 * @param count
	 * @param fmid
	 */
	public void addPlant(int count, YardBean yard,int recipeId){
		DbOperation db = new DbOperation(5);
		if (count > 0){
			for (int i = 0 ; i < count ; i++){
				YardPlantBean plant = new YardPlantBean();
				plant.setFmid(yard.getFmid());
				db.executeUpdate("insert into fm_yard_fm_plant (fmid,max_count,rank,recipe_id) values (" + yard.getFmid() +",0,0," + recipeId + ")");
				plant.setId(db.getLastInsertId());
				plant.setRecipeId(recipeId);
				yard.getPlantList().add(plant);
			}
		}
		db.release();
	}

	private YardPlantBean getYardPlantBean(ResultSet rs) throws SQLException {
		YardPlantBean bean = new YardPlantBean();
		bean.setId(rs.getInt("id"));
		bean.setFmid(rs.getInt("fmid"));
		bean.setPlantTime(rs.getTimestamp("plant_time").getTime());
		bean.setCount(rs.getInt("count"));
		bean.setRank(rs.getInt("rank"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setRecipeId(rs.getInt("recipe_id"));
		bean.setType(rs.getInt("type"));
		bean.setPlantNow(rs.getInt("plant_now"));
		return bean;
	}
	
	public YardDeployBean getYardDeployBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_deploy where " + cond);
		try {
			if (rs.next()) {
				return getYardDeployBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardDeployBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_deploy where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardDeployBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	private YardDeployBean getYardDeployBean(ResultSet rs) throws SQLException {
		YardDeployBean bean = new YardDeployBean();
		bean.setId(rs.getInt("id"));
		bean.setFmid(rs.getInt("fmid"));
		bean.setProtoId(rs.getInt("proto_id"));
		bean.setMaterial(rs.getString("material"));
		bean.setStep(rs.getInt("step"));
		bean.setStartTime(rs.getTimestamp("start_time").getTime());
		bean.setIsCurrent(rs.getInt("is_current"));
		bean.setTotalPoint(rs.getInt("total_point"));
		bean.setInuse(rs.getInt("inuse"));
		return bean;
	}
	
	public CookBean2 getCookBean2(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_cook2 where " + cond);
		try {
			if (rs.next()) {
				return getCookBean2(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getCookBeanList2(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_cook2 where " + cond);
		try {
			while (rs.next()) {
				list.add(getCookBean2(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addCookBean2(CookBean2 bean){
		int lastInsertId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_yard_cook2 (recpie_id,content,need_time,step,material_id,count) values (?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getRecipeId());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getNeedTime());
			pstmt.setInt(4, bean.getStep());
			pstmt.setInt(5, bean.getMaterialId());
			pstmt.setInt(6, bean.getCount());
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
	
	private CookBean2 getCookBean2(ResultSet rs) throws SQLException {
		CookBean2 bean = new CookBean2();
		bean.setId(rs.getInt("id"));
		bean.setRecipeId(rs.getInt("recpie_id"));
		bean.setContent(rs.getString("content"));
		bean.setNeedTime(rs.getInt("need_time"));
		bean.setStep(rs.getInt("step"));
		bean.setMaterialId(rs.getInt("material_id"));
		bean.setCount(rs.getInt("count"));
		return bean;
	}
	
	/**
	 * 取得某家族已经配了哪些菜谱,配了多少份.<br/>
	 * map的key是菜谱ID,value是配的数量.
	 * @param fmId
	 * @return
	 */
	public HashMap getDeployCount(int fmId){
		HashMap map = new HashMap();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select count(id) c,proto_id p from fm_yard_deploy where fmid=" + fmId + " group by proto_id");
		try {
			while(rs.next()){
				map.put(new Integer(rs.getInt("p")), new Integer(rs.getInt("c")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return map;
	}
	
	public YardDeployingBean getYardDeployingBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_deploying where " + cond);
		try {
			if (rs.next()) {
				return getYardDeployingBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public List getYardDeployingBeanList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_yard_deploying where " + cond);
		try {
			while (rs.next()) {
				list.add(getYardDeployingBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	public int addDeploying(YardDeployingBean bean){
		int lastInsertId = 0;
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_yard_deploying (fmid,uid,deploy_id,recipe_id,create_time) values (?,?,?,?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastInsertId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getFmid());
			pstmt.setInt(2, bean.getUid());
			pstmt.setInt(3, bean.getDeployId());
			pstmt.setInt(4, bean.getRecipeId());
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
	
	private YardDeployingBean getYardDeployingBean(ResultSet rs) throws SQLException {
		YardDeployingBean bean = new YardDeployingBean();
		bean.setId(rs.getInt("id"));
		bean.setFmid(rs.getInt("fmid"));
		bean.setUid(rs.getInt("uid"));
		bean.setDeployId(rs.getInt("deploy_id"));
		bean.setRecipeId(rs.getInt("recipe_id"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}
	
	/**
	 * 返回某一家族有哪些菜正在做
	 * @param fmid
	 * @return
	 */
	public HashSet getFmDeploying(int fmid){
		HashSet hs = new HashSet();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select recipe_id from fm_yard_deploying where fmid=" + fmid);
		try {
			while (rs.next()) {
				hs.add(new Integer(rs.getInt("recipe_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return hs;
	}
}
