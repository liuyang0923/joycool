package net.joycool.wap.spec.garden.flower;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.util.db.DbOperation;

public class FlowerService {

	// 根据UID来取得一块土地的所有信息
	public List getField(int uid) {
		List fieldList = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from flower_field where user_id=" + uid
						+ " order by id");
		try {
			while (rs.next()) {
				fieldList.add(getField(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return fieldList;
	}

	// 取得一块土地(by id)
	public FieldBean getFieldById(int id) {
		FieldBean fb = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_field where id = "
				+ id);
		try {
			if (rs.next()) {
				fb = getField(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return fb;
	}

	// 增加土地的数量
	public boolean addFieldCount(int uid){
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("update flower_user set field_count = field_count + 1 where user_id = " + uid);
		db.release();
		return result;
	}
	
	// 取得任务(by id)
	public FlowerTask getTaskById(int id) {
		FlowerTask ft = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_task where id = "
				+ id);
		try {
			if (rs.next()) {
				ft = getTask(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return ft;
	}
	
	// 取得任务列表
	public HashMap getTaskes(){
		int i = 0;
		FlowerTask ft = null;
		HashMap taskMap = new HashMap();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_task");
		try {
			while(rs.next()){
				i++;
				ft = getTask(rs);
				taskMap.put(new Integer(i), ft);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return taskMap;
	}
	
	// 取得一块地的完整信息
	FieldBean getField(ResultSet rs) throws SQLException {
		FieldBean fb = new FieldBean();
		fb.setId(rs.getInt("id"));
		fb.setUserId(rs.getInt("user_id"));
		fb.setField(rs.getInt("field"));
		fb.setCreateTime(rs.getTimestamp("create_time").getTime());
		fb.setType(rs.getInt("type"));
		return fb;
	}

	// 取得仓库信息
	FlowerStoreBean getStore(ResultSet rs) throws SQLException {
		FlowerStoreBean fsb = new FlowerStoreBean();
		fsb.setId(rs.getInt("id"));
		fsb.setUserId(rs.getInt("user_id"));
		fsb.setFlowerId(rs.getInt("flower_id"));
		fsb.setCount(rs.getInt("count"));
		fsb.setType(rs.getInt("type"));
		return fsb;
	}

	// 取得用户养殖地类型
	FlowerUserBean getFlowerUser(ResultSet rs) throws SQLException {
		FlowerUserBean fub = new FlowerUserBean();
		fub.setUserId(rs.getInt("user_Id"));
		fub.setExp(rs.getInt("exp"));
		fub.setUsedExp(rs.getInt("used_exp"));
		fub.setFieldCount(rs.getInt("field_count"));
		fub.setTaskNum(rs.getInt("task_num"));
		return fub;
	}

	// 取得培养皿信息
	DishBean getDish(ResultSet rs) throws SQLException {
		DishBean dish = new DishBean();
		dish.setId(rs.getInt("id"));
		dish.setUserId(rs.getInt("user_id"));
		dish.setCultureDish1(rs.getInt("culture_dish1"));
		dish.setCultureDish2(rs.getInt("culture_dish2"));
		dish.setCultureDish3(rs.getInt("culture_dish3"));
		dish.setCultureDish4(rs.getInt("culture_dish4"));
		dish.setState(rs.getInt("state"));
		dish.setTime(rs.getTimestamp("time").getTime());
		return dish;
	}

	// 取得属性信息
	FlowerBean getFlower(ResultSet rs) throws SQLException {
		FlowerBean f = new FlowerBean();
		f.setId(rs.getInt("id"));
		f.setName(rs.getString("name"));
		f.setType(rs.getInt("type"));
		f.setState(rs.getInt("state"));
		f.setPrice(rs.getInt("price"));
		f.setTime(rs.getInt("time"));
		f.setCompose(rs.getString("compose"));
		f.setCompTime(rs.getInt("comp_time"));
		f.setSuccessExp(rs.getInt("success_exp"));
		f.setFailExp(rs.getInt("fail_exp"));
		f.setGrowExp(rs.getInt("grow_exp"));
		return f;
	}

	// 取得排行榜
	FlowerRankBean getFlowerRank(ResultSet rs) throws SQLException {
		FlowerRankBean frb = new FlowerRankBean();
		frb.setId(rs.getInt("id"));
		frb.setUserId(rs.getInt("user_Id"));
		frb.setExp(rs.getInt("exp"));
		return frb;
	}

	// 取得合成公式
	FlowerComposeBean getComposeBean(ResultSet rs) throws SQLException {
		FlowerComposeBean fcb = new FlowerComposeBean();
		fcb.setId(rs.getInt("id"));
		fcb.setUserId(rs.getInt("user_id"));
		fcb.setFlowerId(rs.getInt("flower_Id"));
		fcb.setCreateTime(rs.getTimestamp("create_time").getTime());
		return fcb;
	}

	// 取得日志信息
	FlowerMessageBean getMessage(ResultSet rs) throws SQLException {
		FlowerMessageBean fm = new FlowerMessageBean();
		fm.setId(rs.getInt("id"));
		fm.setUserId(rs.getInt("user_id"));
		fm.setContent(rs.getString("content"));
		fm.setFromId(rs.getInt("from_uid"));
		fm.setReaded(rs.getInt("readed"));
		fm.setCreateTime(rs.getTimestamp("create_time").getTime());
		fm.setType(rs.getInt("type"));
		return fm;
	}

	// 取得任务
	FlowerTask getTask(ResultSet rs) throws SQLException{
		FlowerTask ft = new FlowerTask();
		ft.setId(rs.getInt("id"));
		ft.setTitle(rs.getString("title"));
		ft.setStartMsg(rs.getString("start_msg"));
		ft.setMission(rs.getString("mission"));
		ft.setSuccess(rs.getString("success"));
		ft.setExp(rs.getInt("exp"));
		ft.setFlowerId(rs.getInt("flower_id"));
		ft.setType(rs.getInt("type"));
		return ft;
	}
	
	// 花市
	FlowerMarketBean getMarketBean(ResultSet rs) throws SQLException{
		FlowerMarketBean fmb = new FlowerMarketBean();
		fmb.setId(rs.getInt("id"));
		fmb.setFlowerId(rs.getInt("flower_id"));
		fmb.setType(rs.getInt("type"));
		fmb.setPrice(rs.getInt("price"));
		fmb.setState(rs.getInt("state"));
		fmb.setSellerUid(rs.getInt("seller_uid"));
		fmb.setBuyerUid(rs.getInt("buyer_uid"));
		fmb.setReleaseTime(rs.getTimestamp("release_time").getTime());
		fmb.setBuyTime(rs.getTimestamp("buy_time").getTime());
		return fmb;
	}
	
	// 特殊物品
	EspecialBean getEspecial(ResultSet rs) throws SQLException{
		EspecialBean espBean = new EspecialBean();
		espBean.setId(rs.getInt("id"));
		espBean.setName(rs.getString("name"));
		espBean.setMoney(rs.getInt("money"));
		espBean.setExp(rs.getInt("exp"));
		espBean.setFlowerId(rs.getInt("flower_id"));
		espBean.setTime(rs.getInt("time"));
		espBean.setType(rs.getInt("type"));
		espBean.setState(rs.getInt("state"));
		espBean.setMessage(rs.getString("message"));
		return espBean;
	}
	
	// 取得特殊物品Map
	public HashMap getEspecialMap(String cond){
		HashMap getEspecialMap = new HashMap();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_especial where " + cond);
		EspecialBean espBean = null;
		try {
			while(rs.next()){
				espBean = getEspecial(rs);
				getEspecialMap.put(new Integer(espBean.getId()),espBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return getEspecialMap;		
	}
	
	// 取得某一特殊物品
	public EspecialBean getEspecial(String cond){
		EspecialBean espBean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_especial where " + cond);
		try {
			if (rs.next()){
				espBean = getEspecial(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return espBean;
	}
	
	// 取得花市List
	public List getMarketList(String cond){
		List marketList = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_market where " + cond);
		try {
			while(rs.next()){
				marketList.add(getMarketBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return marketList;
	}
	
	// 增加flower_market数据
	public boolean sellFlower(String cond){
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("insert into flower_market " + cond);
		db.release();
		return result;
	}
	
	// 修改updateFlowerUser表
	public boolean updateFlowerUser(String cond) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("update flower_user " + cond);
		db.release();
		return result;
	}

	// 写入日志
	public boolean insertMessage(String cond) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("insert into flower_message " + cond);
		db.release();
		return result;
	}

	// 取得一个用户的全部日志
	public List getMessageList(String cond) {
		List logList = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_message where " + cond);
		try {
			while (rs.next()) {
				logList.add(getMessage(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return logList;
	}

	// 创建一块养殖地
	public boolean createField(int uid) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db
				.executeUpdate("insert into flower_user (user_id,exp,field_count) values ("
						+ uid + ",0,1)");
		result = addField(uid, 1);
		db.release();
		return result;
	}

	// 增加一块养殖地
	public boolean addField(int uid, int type) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db
				.executeUpdate("insert into flower_field (user_id,type) values ("
						+ uid + "," + type + ")");
		db.release();
		return result;
	}

	// 取得用户[分页]
	public List getFlowerUser(int pageNow,int pageInfoCount){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_user order by user_id desc limit " + (pageNow * pageInfoCount) + "," + pageInfoCount);
		try{
			while(rs.next()) {
				list.add(getFlowerUser(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	
	// 取得用户养殖地类型
	public FlowerUserBean getFieldType(int uid) {
		FlowerUserBean fub = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from flower_user where user_id =" + uid);
		try {
			if (rs.next()) {
				fub = this.getFlowerUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return fub;
	}

	// 改更用户养殖地类型
	public boolean changeFieldType(int id, int type) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("update flower_field set type=" + type
				+ " where id=" + id);
		db.release();
		return result;
	}

	// 开始种地
	public boolean planting(int fieldId, int seedId) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_field where id = "
				+ fieldId);
		try {
			if (rs.next()) {
				result = db.executeUpdate("update flower_field set field = "
						+ seedId
						+ ",create_time=now() where id="
						+ fieldId);
			}// else {
			// result = db
			// .executeUpdate("insert into flower_field
			// (user_id,field,create,steal_date) values ("
			// + uid + "," + seedId + ",now(),now())");
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}

	// 根据用户id，取得其库存的鲜花/种子
	public List getUserStore(String cond) {
		List storeList = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_store where "
				+ cond);
		try {
			while (rs.next()) {
				storeList.add(getStore(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return storeList;
	}

	// 根据用户id，取得其持有的种子数量
	public List getSeeds(String cond) {
		FlowerStoreBean fsb = null;
		List seedList = new ArrayList();
		DbOperation db = new DbOperation(5);
		// 种子type=1
		ResultSet rs = db
				.executeQuery("select flower_id,count,type from flower_store where "
						+ cond);
		try {
			while (rs.next()) {
				// 这个Bean中只有花种子的信息，没有其它
				fsb = new FlowerStoreBean();
				fsb.setFlowerId(rs.getInt("flower_id"));
				fsb.setCount(rs.getInt("count"));
				fsb.setType(rs.getInt("type"));
				seedList.add(fsb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return seedList;
	}

	// 种子数量-N
	public boolean descSeed(int uid, int seedId, int count) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("update flower_store set count = count - "
				+ count + " where user_id = " + uid + " and flower_id = "
				+ seedId + " and type = 1 and count > 0");
		db.release();
		return result;
	}

	// 采摘鲜花
	public boolean pickFlower(int id) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		// 更改用户养殖地信息
		result = db
				.executeUpdate("update flower_field set field = 0,create_time = '1970-01-01 00:00:00' where id = "
						+ id);
		db.release();
		return result;
	}

	// 取得所有花的属性
	public List getFlowerList(String cond) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		ResultSet rs = db.executeQuery("select * from flower_property where "
				+ cond);
		try {
			while (rs.next()) {
				list.add(getFlower(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	// 买N个种子
	public boolean buyFlowerSeed(int uid, int flowerId, int count) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		// 如果用户购买过此花则数量+1即可，否则添加
		ResultSet rs = db
				.executeQuery("select id from flower_store where flower_id="
						+ flowerId + " and user_id= " + uid + " and type=1");
		try {
			if (rs.next()) {
				result = db
						.executeUpdate("update flower_store set count=count+"
								+ count + " where id=" + rs.getInt("id"));
			} else {
				result = db
						.executeUpdate("insert into flower_store (user_id,flower_id,count,type) values ("
								+ uid + "," + flowerId + "," + count + ",1)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}

	// 朵花-N
	public boolean descFlower(int uid, int flowerId, int num) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		int id = 0;
		int count = 0;
		ResultSet rs = db
				.executeQuery("select id,count from flower_store where user_id = "
						+ uid
						+ " and flower_id = "
						+ flowerId
						+ " and type = 2");
		try {
			if (rs.next()) {
				id = rs.getInt("id");
				count = rs.getInt("count");
				if (count != 0) {
					result = db
							.executeUpdate("update flower_store set count = count - "
									+ num + " where id=" + id);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}

	// 花朵+1 (只有从培养皿里删除花时才会用到这个方法。因为它不会判断这朵花是否买来的)
	public boolean addFlowerFromDish(int uid, int flowerId) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id from flower_store where user_id = "
						+ uid + " and flower_id = " + flowerId
						+ " and type = 2");
		try {
			if (rs.next()) {
				result = db
						.executeUpdate("update flower_store set count=count+1 where id ="
								+ rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}

	// 花朵+N
	public boolean addFlower(int uid, int flowerId, int count) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		// 先查看用户是否有这种花
		ResultSet rs = db
				.executeQuery("select id from flower_store where user_id = "
						+ uid + " and flower_id = " + flowerId
						+ " and type = 2");
		try {
			if (rs.next()) {
				result = db
						.executeUpdate("update flower_store set count=count+"
								+ count + " where id =" + rs.getInt("id"));
			} else {
				result = db
						.executeUpdate("insert into flower_store (user_id,flower_id,count,type) values ("
								+ uid + "," + flowerId + "," + count + ",2)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}
	
	// 增加特殊物品
	public boolean addSpecial(int uid,int esp,int count){
		boolean result = false;
		DbOperation db = new DbOperation(5);
		// 先查看用户是否有这物品
		ResultSet rs = db
				.executeQuery("select id from flower_store where user_id = "
						+ uid + " and flower_id = " + esp
						+ " and type = 3");
		try {
			if (rs.next()) {
				result = db
						.executeUpdate("update flower_store set count=count+"
								+ count + " where id =" + rs.getInt("id"));
			} else {
				result = db
						.executeUpdate("insert into flower_store (user_id,flower_id,count,type) values ("
								+ uid + "," + esp + "," + count + ",3)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;		
	}
	
	// 物品-N
	public boolean descSpecial(int uid, int esp, int num) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		int id = 0;
		int count = 0;
//		System.out.println("数据库中查找的是 " + FlowerUtil.getEspecial(esp).getName());
		ResultSet rs = db
				.executeQuery("select id,count from flower_store where user_id = "
						+ uid
						+ " and flower_id = "
						+ esp
						+ " and type = 3");
		try {
			if (rs.next()) {
				id = rs.getInt("id");
				count = rs.getInt("count");
				if (count != 0) {
					result = db
							.executeUpdate("update flower_store set count = count - "
									+ num + " where id=" + id);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}

	// 取得培养皿信息
	public DishBean getCultureDish(int uid) {
		DishBean dish = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from flower_culture_dish where user_id = "
						+ uid);
		try {
			if (rs.next()) {
				dish = getDish(rs);
//				System.out.println(uid + " 哦哦，他读了，看到了吗？他读数据库了哦~！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return dish;
	}

	// 创建一个培养皿
	public DishBean createDish(int uid) {
		DishBean dish = null;
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db
				.executeUpdate("insert into flower_culture_dish (user_id) values ("
						+ uid + ")");
		if (result) {
			dish = getCultureDish(uid);
		}
		db.release();
		return dish;
	}

	// 更新培养皿
	public boolean updateDish(int dishNum, int flowerId, int uid) {
		String tmp = "culture_dish" + dishNum;
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("update flower_culture_dish set " + tmp
				+ " = " + flowerId + " where user_id = " + uid);
//		System.out.println("update flower_culture_dish set " + tmp + " = " + flowerId + " where user_id = " + uid);
		db.release();
		return result;
	}

	// 从培养皿中删除一朵花
	public boolean deleteFromDishDB(int dishNum, int uid) {
//		System.out.println("用户ID:" + uid + " | 删除了一朵花.  | " + new Date());
		String tmp = "culture_dish" + dishNum;
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("update flower_culture_dish set " + tmp
				+ " = 0" + " where user_id = " + uid);
		db.release();
		return result;
	}

	// 清空培养皿
	public boolean clearDish(int uid) {
		boolean result = false;
//		System.out.println("用户ID:" + uid + " | 清空了培养皿.  | " + new Date());
		DbOperation db = new DbOperation(5);
		result = db
				.executeUpdate("update flower_culture_dish set culture_dish1 = 0,culture_dish2 = 0,culture_dish3 = 0,culture_dish4 = 0,state = 0,time = '1970-01-01 00:00:00' where user_id =" + uid);
		db.release();
		return result;
	}

	public boolean updateDish(String cond){
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("update flower_culture_dish " + cond);
		db.release();
		return result;
	}
	
	// 用户公式表中是否包含某公式
	public boolean isContainCompose(int uid, int flowerId) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select id from flower_compose where user_id ="
						+ uid + " and flower_id = " + flowerId);
		try {
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return result;
	}
	
	// 向公式表中插入数据
	public boolean addCompose(String cond) {
		boolean result = false;
		DbOperation db = new DbOperation(5);
		result = db.executeUpdate("insert into flower_compose " + cond);
		db.release();
		return result;
	}

	// 查询公式表
	public List getUserCompose(String cond) {
		List userComposeList = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_compose where "
				+ cond);
		try {
			while (rs.next()) {
				userComposeList.add(getComposeBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return userComposeList;
	}

	// 取得排行榜
	public List getRank(String cond) {
		List rankList = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from flower_rank where "
				+ cond);
		try {
			while (rs.next()) {
				rankList.add(getFlowerRank(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return rankList;
	}
	
//	//****Flower_Log表只供管理员查看****
//	
//	public FlowerLog getOneLog(String cond){
//		FlowerLog bean = null;
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select * from flower_log where " + cond);
//		try {
//			if (rs.next()){
//				bean = getLogBean(rs);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return bean;
//	}
//	
//	public List getLogList(String cond){
//		List list = new ArrayList();
//		DbOperation db = new DbOperation(5);
//		ResultSet rs = db.executeQuery("select * from flower_log where " + cond);
//		try {
//			while (rs.next()){
//				list.add(getLogBean(rs));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			db.release();
//		}
//		return list;
//	}
//	
//	public boolean addNewLog(FlowerLog bean){
//		DbOperation db = new DbOperation(5);
//		String query = "insert into flower_log (content,flag,user_id,create_time) values (?,?,?,now())";
//		if(!db.prepareStatement(query)) {
//			db.release();
//			return false;
//		}
//		PreparedStatement pstmt = db.getPStmt();
//		try{
//			pstmt.setString(1, bean.getContent());
//			pstmt.setInt(2, bean.getFlag());
//			pstmt.setInt(3, bean.getUserId());
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
//	FlowerLog getLogBean(ResultSet rs) throws SQLException{
//		FlowerLog bean = new FlowerLog();
//		bean.setId(rs.getInt("id"));
//		bean.setUserId(rs.getInt("user_id"));
//		bean.setContent(rs.getString("content"));
//		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
//		bean.setFlag(rs.getInt("flag"));
//		return bean;
//	}
}