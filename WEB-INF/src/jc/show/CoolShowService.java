package jc.show;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.joycool.wap.util.db.DbOperation;

public class CoolShowService {

	public void upd(String sql) {
		DbOperation db = new DbOperation(5);
		try {
			db.executeUpdate(sql);
		} finally {
			db.release();
		}
	}

	/**
	 * 插入物品数据
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertGoods(Goods bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "INSERT INTO show_goods(create_time,name,bak,big_img,type,due,flag) VALUES(now(),?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getBak());
			pstmt.setString(3, bean.getBigImg());
			pstmt.setInt(4, bean.getType());
			pstmt.setInt(5, bean.getDue());
			pstmt.setInt(6, bean.getFlag());
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

	/**
	 * 更改物品数据
	 * 
	 * @param bean
	 * @return
	 */
	public boolean alterGoods(Goods bean, Goods old) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "update show_goods set name=?,bak=?,big_img=?,type=?,due=?,flag=? where id="
				+ bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			if (bean.getName() != null && bean.getName().length() > 0) {
				pstmt.setString(1, bean.getName());
			} else {
				pstmt.setString(1, old.getName());
			}
			if (bean.getBak() != null && bean.getBak().length() > 0) {
				pstmt.setString(2, bean.getBak());
			} else {
				pstmt.setString(2, old.getBak());
			}
			if (bean.getBigImg() != null && bean.getBigImg().length() > 0) {
				pstmt.setString(3, bean.getBigImg());
			} else {
				pstmt.setString(3, old.getBigImg());
			}
			if (bean.getType() > 0) {
				pstmt.setInt(4, bean.getType());
			} else {
				pstmt.setInt(4, old.getType());
			}
			pstmt.setInt(5, bean.getDue());
			if (bean.getFlag() >= 0) {
				pstmt.setInt(6, bean.getFlag());
			} else {
				pstmt.setInt(6, old.getFlag());
			}
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

	/**
	 * 插入商品
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertComm(Commodity bean) {

		DbOperation dbOp = new DbOperation(5);
		String query = "INSERT INTO show_commodity(create_time,name,bak,big_img,type,due,gender,item_id,price,del,catalog,goods_img,part_other,next) VALUES(now(),?,?,?,?,?,?,?,?,?,?,?,?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getBak());
			pstmt.setString(3, bean.getBigImg());
			pstmt.setInt(4, bean.getType());
			pstmt.setInt(5, bean.getDue());
			pstmt.setInt(6, bean.getGender());
			pstmt.setInt(7, bean.getIid());
			pstmt.setFloat(8, bean.getPrice());
			pstmt.setFloat(9, 1);
			pstmt.setInt(10, bean.getCatalog());
			pstmt.setString(11, bean.getGoodsImg());
			pstmt.setString(12, bean.getPartOther());
			pstmt.setInt(13, bean.getNext());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.executePstmt();
		bean.setId(dbOp.getLastInsertId());

		dbOp.release();
		return true;
	}

	/**
	 * 更改商品数据
	 * 
	 * @param bean
	 * @return
	 */
	public boolean alterComm(Commodity bean, Commodity old) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "update show_commodity set name=?,bak=?,big_img=?,type=?,due=?,gender=?,item_id=?,price=?,goods_img=?,catalog=?,part_other=?,next=? where id="
				+ bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			if (bean.getName() != null && bean.getName().length() > 0) {
				pstmt.setString(1, bean.getName());
			} else {
				pstmt.setString(1, old.getName());
			}
			if (bean.getBak() != null && bean.getBak().length() > 0) {
				pstmt.setString(2, bean.getBak());
			} else {
				pstmt.setString(2, old.getBak());
			}
			if (bean.getBigImg() != null && bean.getBigImg().length() > 0) {
				pstmt.setString(3, bean.getBigImg());
			} else {
				pstmt.setString(3, old.getBigImg());
			}
			if (bean.getType() > 0 && bean.getType() < Integer.MAX_VALUE) {
				pstmt.setInt(4, bean.getType());
			} else {
				pstmt.setInt(4, old.getType());
			}
			if (bean.getDue() >= 0) {
				pstmt.setInt(5, bean.getDue());
			} else {
				pstmt.setInt(5, old.getDue());
			}
			if (bean.getGender() >= 0) {
				pstmt.setInt(6, bean.getGender());
			} else {
				pstmt.setInt(6, old.getGender());
			}
			if (bean.getIid() > 0) {
				pstmt.setInt(7, bean.getIid());
			} else {
				pstmt.setInt(7, old.getIid());
			}
			if (bean.getPrice() >= 0) {
				pstmt.setFloat(8, bean.getPrice());
			} else {
				pstmt.setFloat(8, old.getPrice());
			}
			if (bean.getGoodsImg() != null && bean.getGoodsImg().length() > 0) {
				pstmt.setString(9, bean.getGoodsImg());
			} else {
				pstmt.setString(9, old.getGoodsImg());
			}
			if (bean.getCatalog() > 0) {
				pstmt.setInt(10, bean.getCatalog());
			} else {
				pstmt.setInt(10, old.getCatalog());
			}
			if (bean.getPartOther() != null) {
				pstmt.setString(11, bean.getPartOther());
			} else {
				pstmt.setString(11, old.getPartOther());
			}
			pstmt.setInt(12, bean.getNext());
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

	/**
	 * 增加新部位
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertPart(PartBean bean) {
		DbOperation dbOp = new DbOperation(5);
		String query = "INSERT INTO show_part(id,level_layer,level_show,name,bak) VALUES(?,?,?,?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getId());
			pstmt.setInt(2, bean.getLvlLayer());
			pstmt.setInt(3, bean.getLvlShow());
			pstmt.setString(4, bean.getName());
			pstmt.setString(5, bean.getBak());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.executePstmt();
		dbOp.release();
		return true;
	}
	// 增加商品分类
	public boolean insertCatalog(CatalogBean bean) {
		DbOperation dbOp = new DbOperation(5);
		String query = "INSERT INTO show_catalog(id,seq,name,bak,hide) VALUES(?,?,?,?,?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getId());
			pstmt.setInt(2, bean.getSeq());
			pstmt.setString(3, bean.getName());
			pstmt.setString(4, bean.getBak());
			pstmt.setInt(5, bean.getHide());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.executePstmt();
		dbOp.release();
		return true;
	}

	/**
	 * 修改部位
	 * @param bean
	 * @return
	 */
	public boolean alterPart(PartBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "update show_part set level_layer=?,level_show=?,name=?,bak=? where id="
				+ bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getLvlLayer());
			pstmt.setInt(2, bean.getLvlShow());
			pstmt.setString(3, bean.getName());
			pstmt.setString(4, bean.getBak());
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
	// 修改商品
	public boolean alterCatalog(CatalogBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "update show_catalog set seq=?,name=?,bak=?,hide=?,gender=? where id="
				+ bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getSeq());
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getBak());
			pstmt.setInt(4, bean.getHide());
			pstmt.setInt(5, bean.getGender());
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

	/**
	 * 物品表
	 * 
	 * @param cond
	 * @return
	 */
	public List getGoodsList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from show_goods where " + cond);
		try {
			while (rs.next()) {
				list.add(getGoods(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 我的物品表
	 * 
	 * @return
	 */
	public List getMyGoods(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_pocket where "
				+ cond);
		try {
			while (rs.next()) {
				list.add(getPocket(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * user表
	 * 
	 * @param cond
	 * @return
	 */
	public List getCoolUsers(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from show_users where " + cond);
		try {
			while (rs.next()) {
				list.add(getCU(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;

	}

	/**
	 * user表
	 * 
	 * @param cond
	 * @return
	 */
	public CoolUser getCoolUser(String cond) {
		CoolUser bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from show_users where " + cond);
		try {
			if (rs.next()) {
				bean = getCU(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 商品表
	 * 
	 * @param cond
	 * @return
	 */
	public List getCommodityList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_commodity where "
				+ cond);
		try {
			while (rs.next()) {
				list.add(getCommodity(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 我的收藏表
	 * 
	 * @param cond
	 * @return
	 */
	public List getCollection(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_collection where "
				+ cond);
		try {
			while (rs.next()) {
				list.add(getCol(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 购买记录表
	 * 
	 * @param cond
	 * @return
	 */
	public List getHistory(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_history where "
				+ cond);
		try {
			while (rs.next()) {
				list.add(getHis(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 商品推荐
	 * 
	 * @param cond
	 * @return
	 */
	public List getAdv(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_adv where " + cond);
		try {
			while (rs.next()) {
				list.add(getBeanAdv(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 部位表
	 * 
	 * @param cond
	 * @return
	 */
	public List getPartList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_part where " + cond);
		try {
			while (rs.next()) {
				list.add(getPartBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public PartBean getPartBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_part where " + cond);
		try {
			if (rs.next()) {
				return getPartBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}
	// catalog 
	public List getCatalogList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_catalog where " + cond);
		try {
			while (rs.next()) {
				list.add(getCatalogBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public CatalogBean getCatalogBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_catalog where " + cond);
		try {
			if (rs.next()) {
				return getCatalogBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}
	CatalogBean getCatalogBean(ResultSet rs) throws SQLException {
		CatalogBean bean = new CatalogBean();
		bean.setId(rs.getInt("id"));
		bean.setSeq(rs.getInt("seq"));
		bean.setHide(rs.getInt("hide"));
		bean.setName(rs.getString("name"));
		bean.setBak(rs.getString("bak"));
		bean.setGender(rs.getInt("gender"));
		return bean;
	}
	
	public BeanAdv getAdvBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_adv where " + cond);
		try {
			if (rs.next()) {
				return getBeanAdv(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	/**
	 * 收藏
	 * 
	 * @param cond
	 * @return
	 */
	public Collection getCol(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_collection where "
				+ cond);
		try {
			if (rs.next()) {
				return getCol(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public Pocket getDate1(String cond) {
		Pocket bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_pocket where "
				+ cond);
		try {
			if (rs.next()) {
				bean = getPocket(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	public Commodity getCommodity(String cond) {
		Commodity bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_commodity where "
				+ cond);
		try {
			if (rs.next()) {
				bean = getCommodity(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	public Goods getGoods(String cond) {
		Goods bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from show_goods where " + cond);
		try {
			if (rs.next()) {
				return getGoods(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	public GroupBean getGroup(String cond) {
		GroupBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from show_group where " + cond);
		try {
			if (rs.next()) {
				return getGroupBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	public List getGroupList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from show_group where " + cond);
		try {
			while (rs.next()) {
				list.add(getGroupBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	Pocket getPocket(ResultSet rs) throws SQLException {
		Pocket bean = new Pocket();
		bean.setId(rs.getInt("id"));
		bean.setUid(rs.getInt("user_id"));
		bean.setIid(rs.getInt("item_id"));
		bean.setDel(rs.getInt("del"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setEndTime(rs.getTimestamp("end_time").getTime());
		bean.setState(rs.getInt("state"));
		return bean;
	}

	Commodity getCommodity(ResultSet rs) throws SQLException {
		Commodity bean = new Commodity();
		bean.setId(rs.getInt("id"));
		bean.setDue(rs.getInt("due"));
		bean.setIid(rs.getInt("item_id"));
		bean.setGender(rs.getInt("gender"));
		bean.setType(rs.getInt("type"));
		bean.setCatalog(rs.getInt("catalog"));
		bean.setCount(rs.getInt("count"));
		bean.setState(rs.getInt("state"));
		bean.setDel(rs.getInt("del"));
		bean.setPrice(rs.getFloat("price"));
		bean.setBak(rs.getString("Bak"));
		bean.setName(rs.getString("name"));
		bean.setBigImg(rs.getString("big_img"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		
		bean.setGoodsImg(rs.getString("goods_img"));
		bean.setNext(rs.getInt("next"));
		bean.setPartOther(rs.getString("part_other"));
		return bean;
	}

	Goods getGoods(ResultSet rs) throws SQLException {
		Goods bean = new Goods();
		bean.setId(rs.getInt("id"));
		bean.setType(rs.getInt("type"));
		bean.setFlag(rs.getInt("flag"));
		bean.setDue(rs.getInt("due"));
		bean.setDel(rs.getInt("del"));
		bean.setIssale(rs.getInt("issale"));
		bean.setName(rs.getString("name"));
		bean.setBak(rs.getString("Bak"));
		bean.setBigImg(rs.getString("big_img"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}

	Collection getCol(ResultSet rs) throws SQLException {
		Collection coll = new Collection();
		coll.setId(rs.getInt("id"));
		coll.setUid(rs.getInt("user_id"));
		coll.setIid(rs.getInt("item_id"));
		coll.setCreateTime(rs.getTimestamp("create_time").getTime());
		return coll;
	}

	CoolUser getCU(ResultSet rs) throws SQLException {
		CoolUser cooluser = new CoolUser();
		cooluser.setId(rs.getInt("id"));
		cooluser.setUid(rs.getInt("user_id"));
		cooluser.setSumMyGoods(rs.getInt("sum_mygoods"));
		cooluser.setGenderUseing(rs.getInt("gender_useing"));
		cooluser.setImgurl(rs.getString("img_url"));
		cooluser.setImgurlF(rs.getString("img_female_url"));
		cooluser.setImgurlM(rs.getString("img_male_url"));
		cooluser.setCurItem(rs.getString("cur_item"));
		cooluser.setEndTime(rs.getTimestamp("end_time").getTime());
		return cooluser;
	}

	History getHis(ResultSet rs) throws SQLException {
		History history = new History();
		history.setId(rs.getInt("id"));
		history.setUid(rs.getInt("user_id_1"));
		history.setTouid(rs.getInt("user_id_2"));
		history.setIid(rs.getInt("item_id"));
		history.setCreatetime(rs.getTimestamp("create_time").getTime());
		return history;
	}

	BeanAdv getBeanAdv(ResultSet rs) throws SQLException {
		BeanAdv bean = new BeanAdv();
		bean.setId(rs.getInt("id"));
		bean.setDel(rs.getInt("del"));
		bean.setFlag(rs.getInt("flag"));
		bean.setPlace(rs.getInt("place"));
		bean.setCommid(rs.getInt("comm_id"));
		bean.setCreatetime(rs.getTimestamp("create_time").getTime());
		return bean;
	}

	PartBean getPartBean(ResultSet rs) throws SQLException {
		PartBean part = new PartBean();
		part.setId(rs.getInt("id"));
		part.setLvlLayer(rs.getInt("level_layer"));
		part.setLvlShow(rs.getInt("level_show"));
		part.setName(rs.getString("name"));
		part.setBak(rs.getString("bak"));
		return part;
	}
	
	GroupBean getGroupBean(ResultSet rs) throws SQLException {
		GroupBean bean = new GroupBean();
		bean.setId(rs.getInt("id"));
		bean.setItemIds(rs.getString("item_ids"));
		bean.setName(rs.getString("name"));
		bean.setUid(rs.getInt("uid"));
		bean.setDue(rs.getInt("due"));
		return bean;
	}
}
