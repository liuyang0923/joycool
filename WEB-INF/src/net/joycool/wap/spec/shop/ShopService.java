package net.joycool.wap.spec.shop;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class ShopService {

	private static ShopService shopService;
	
	private ShopService(){}
	
	public static ShopService getInstance(){
		if(shopService == null) {
			synchronized(ShopService.class) {
				if(shopService == null)
					shopService = new ShopService();
			}
		}
		
		return shopService;
	}
	
	/**
	 * 得到用户的金币
	 * @param uid
	 * @return
	 */
	public UserInfoBean getUserInfo(int uid) {
		DbOperation db = new DbOperation(5);
		
		String query = "select * from shop_user_info where uid = " + uid;
		
		ResultSet rs = db.executeQuery(query);
		UserInfoBean infoBean = null;
		try{
			if(rs.next()) {
				infoBean = new UserInfoBean();
				infoBean.setUid(rs.getInt("uid"));
				infoBean.setGold(rs.getFloat("gold"));
				infoBean.setFavoriteCount(rs.getInt("favorite_count"));
				infoBean.setConsumeCount(rs.getFloat("consume_count"));				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return infoBean;
		}finally{
			db.release();
		}
		
		return infoBean;
	}
	
	
	public UserInfoBean addUserInfo(int uid) {
		UserInfoBean userInfo = new UserInfoBean();
		
		DbOperation db = new DbOperation(5);
		
		String query = "insert into shop_user_info set uid = " + uid+", gold = 0" +",favorite_count = 0, consume_count = 0";
		db.executeUpdate(query);
		
		userInfo.setUid(uid);
		userInfo.setGold(0);
		userInfo.setFavoriteCount(0);
		userInfo.setConsumeCount(0);
		
		db.release();
		
		return userInfo;
	}
	
	/**
	 * 更新用户的金币数
	 * @param uid
	 * @param count
	 */
	public void updateUserGold(int uid, float count) {
		DbOperation db = new DbOperation(5);
		String query = "update shop_user_info set gold = " + count + " where uid = " + uid;
		db.executeUpdate(query);
		db.release();
	}
	
	/**
	 * 更新用户的金币数,和消费总数
	 * @param uid
	 * @param count
	 */
	public void updateUserGold(int uid, float count, float price) {
		DbOperation db = new DbOperation(5);
		String query = "update shop_user_info set gold = " + count + ", consume_count = consume_count +" + price + " where uid = " + uid;
		db.executeUpdate(query);
		db.release();
	}
	
	/**
	 * 更新用户的收藏夹数量
	 * @param uid
	 * @param count
	 */
	public void updateUserFavoriteCount(int uid, int count) {
		DbOperation db = new DbOperation(5);
		String query = "update shop_user_info set favorite_count = favorite_count + " + count + " where uid = " + uid;
		db.executeUpdate(query);
		
		db.release();
	}
	
	
	/**
	 * 增加金币发生变化记录
	 * @param bean
	 */
	public void addGoldRecord(GoldRecordBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "insert into shop_gold_record set uid = " + bean.getUid() + ", item_id = "+bean.getItemId()+", gold = " + bean.getGold() + ", time = now(), type = " + bean.getType() + ", user_id = " + bean.getUserId();
		db.executeUpdate(query);
		db.release();
		
	}
	
	
	/**
	 * 得到购买商品的记录
	 * @param uid
	 * @param condition
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getBuyRecord(int uid, String condition, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		
		String query = "select * from shop_gold_record where uid = " + uid;
		if(condition == null || condition.length() == 0) {
			query += " limit " + start + ", " + limit;
		} else {
			query += " " + StringUtil.toSql(condition) + " limit " + start + ", " + limit;
		}
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				GoldRecordBean bean = getGoldRecord(rs);
				
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
	public List getBuyRecord(String condition) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		
		String query = "select * from shop_gold_record where " + condition;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				GoldRecordBean bean = getGoldRecord(rs);
				
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
	
	public List getRecord(String condition, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		
		String query = "select * from shop_gold_record ";
		if(condition == null || condition.length() == 0) {
			query += " limit " + start + ", " + limit;
		} else {
			query += " where " + condition + " limit " + start + ", " + limit;
		}
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				GoldRecordBean bean = getGoldRecord(rs);
				
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
	
	
	private GoldRecordBean getGoldRecord(ResultSet rs) throws SQLException{
		GoldRecordBean bean = new GoldRecordBean();
		bean.setId(rs.getInt("id"));
		bean.setGold(rs.getFloat("gold"));
		bean.setItemId(rs.getInt("item_id"));
		bean.setType(rs.getInt("type"));
		bean.setUid(rs.getInt("uid"));
		bean.setTime(rs.getTimestamp("time"));
		bean.setUserId(rs.getInt("user_id"));
		
		return bean;
	}
	
	public int getCountBuyRecord(int uid, String condition) {
		DbOperation db = new DbOperation(5);
		
		String query = "select count(*) as count from shop_gold_record where uid = " + uid;
		if(condition == null || condition.length() == 0) {
		} else {
			query += " " + condition ;
		}
		int count = 0;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				count = rs.getInt("count");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		return count;
	}
	
	public int getCountRecord(String condition) {
		DbOperation db = new DbOperation(5);
		
		String query = "select count(*) as count from shop_gold_record ";
		if(condition == null || condition.length() == 0) {
		} else {
			query += " where " + condition;
		}
		int count = 0;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				count = rs.getInt("count");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		return count;
	}
	
	static HashMap shopItemCache = new HashMap(200);
	static HashMap shopItemIdCache = new HashMap(20);	
	
	private void clear(){
		synchronized(shopItemCache) {
			shopItemCache.clear();
		}
		synchronized(shopItemIdCache) {
			shopItemIdCache.clear();
		}
	}
	
	private void clearItemCache(int id){
		synchronized(shopItemCache) {
			shopItemCache.remove(new Integer(id));
		}
	}
	
	private void clearItemIdCache() {
		synchronized(shopItemIdCache) {
			shopItemIdCache.clear();
		}
	}
	
		
	/**
	 * 获取热点排行
	 * @param limit
	 * @return
	 */
	public List getTop(int limit) {
		DbOperation db = new DbOperation(5);
		String query = "select id from shop_item where hidden = 1 order by count desc limit " + limit;
		
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try{
			while(rs.next()) {
				list.add(new Integer(rs.getInt("id")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	/**
	 * 获得最近推荐
	 * @return
	 */
	public List getSugguest(int limit) {
		DbOperation db = new DbOperation(5);
		String query = "select id from shop_item where hidden = 1 order by sugguest desc, seq desc limit " + limit;
		
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try{
			while(rs.next()) {
				list.add(new Integer(rs.getInt("id")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	
	/**
	 * 获取最新上架商品
	 * @param limit
	 * @return
	 */
	public List getNew(int limit) {
		DbOperation db = new DbOperation(5);
		String query = "select id from shop_item where hidden = 1 order by id desc, seq desc limit " + limit;
		
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try{
			while(rs.next()) {
				list.add(new Integer(rs.getInt("id")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	
	public boolean addItem(ItemBean bean) {
		DbOperation db = new DbOperation(5);
		
		String query = "insert into shop_item(item_id, price, type, fun_type, fun_type2, sugguest, time, count, max, odd,hidden,`desc`,photo_url,seq,name,times,due) values(?,?,?,0,0,0,now(),0,?,?,0,?,?,?,?,?,?)";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getItemId());
			pstmt.setFloat(2, bean.getPrice());
			pstmt.setInt(3, bean.getType());
			pstmt.setInt(4, bean.getMax());
			pstmt.setInt(5, bean.getOdd());
			pstmt.setString(6, bean.getDesc());
			pstmt.setString(7, bean.getPhotoUrl());
			pstmt.setInt(8, bean.getSeq());
			pstmt.setString(9, bean.getName());
			pstmt.setInt(10, bean.getTimes());
			pstmt.setInt(11, bean.getDue());
			pstmt.executeUpdate();
			bean.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		synchronized(shopItemIdCache) {
			List list = (List)shopItemIdCache.get(new Integer(bean.getType()));
			if(list == null) {
				list = getItemListByType(bean.getType());//new ArrayList();
				shopItemIdCache.put(new Integer(bean.getType()) , list);
			}
			list.add(new Integer(bean.getId()));
			
			list = (List)shopItemIdCache.get(new Integer(0));
			if(list == null) {
				list = getItemListByType(0);
				shopItemIdCache.put(new Integer(0) , list);
			}
			list.add(new Integer(bean.getId()));
		}
		return true;
	}
	
	
	public void updateHiddenItem(int id) {
		DbOperation db = new DbOperation(5);
		String query = "update shop_item set hidden = (~hidden & 1) where id = " + id;
		
		db.executeUpdate(query);
		
		db.release();
		this.clearItemCache(id);
		this.clearItemIdCache();
	}
	
	
	/**
	 * 是否推荐
	 * @param id
	 */
	public void updateSugguestItem(int id) {
		DbOperation db = new DbOperation(5);
		String query = "update shop_item set sugguest = (~sugguest & 1) where id = " + id;
		
		db.executeUpdate(query);
		
		db.release();
		
		this.clearItemCache(id);
		
	}
	
	/**
	 * 删除一个物品
	 * @param id
	 * @return
	 */
	public boolean deleteItem(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from shop_item where id = " + id;
		
		db.executeUpdate(query);
		
		db.release();
		this.clear();
		return true;		
	}
	
	public boolean updateItem(ItemBean itemBean) {
		synchronized(shopItemCache){
			DbOperation db = new DbOperation(5);
			String query = "update shop_item set price = " + itemBean.getPrice() + ", type = " + itemBean.getType() + ", max =  " + itemBean.getMax() + ", odd = " + itemBean.getOdd() + ", `desc` = '" + StringUtil.toSql(itemBean.getDesc()) + "', seq = "
					+ itemBean.getSeq() + ", name = '" + itemBean.getName() + "', times = " + itemBean.getTimes() + ", due = " + itemBean.getDue() + ", item_id = " + itemBean.getItemId();
			if(itemBean.getPhotoUrl() == null || itemBean.getPhotoUrl().length() == 0) {
				query += " where id = " + itemBean.getId();
			} else {
				query += ", photo_url = '" + StringUtil.toSql(itemBean.getPhotoUrl()) + "' where id = " + itemBean.getId();
			}
			db.executeUpdate(query);
			db.release();
			shopItemCache.remove(new Integer(itemBean.getId()));
			
			this.clear();
		}
		return true;
	}
	
	
	/**
	 * 根据条件获取某一类商品
	 * @param condition
	 * @return
	 */
	public List getItemListByCondition(String condition) {
		DbOperation db = new DbOperation(5);
		String query = "select id from shop_item ";
		
		if(condition != null) {
			query += " where hidden = 1 and " + condition;
		}
		
		query += " order by seq desc";
		
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try{
			while(rs.next()) {
				list.add(new Integer(rs.getInt("id")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	
	/**
	 * 购买一次更新一次数量
	 * @param id
	 * @param count
	 */
	public void updateItemCount(int id, int count) {
		synchronized(shopItemCache){
			
			DbOperation db = new DbOperation(5);
			String query = "update shop_item set count = count + " + count + " where id = " + id;
			
			db.executeUpdate(query);
			db.release();
			
			ItemBean bean = this.getShopItemById(id);
			bean.setCount(bean.getCount() + count);
		}
		
		
	}
	
	public void updateItemCountAndOdd(int id, int count , int odd) {
		synchronized(shopItemCache){
			DbOperation db = new DbOperation(5);
			String query = "update shop_item set count = count + " + count + ", odd = odd + " + odd + " where id = " + id;
			
			db.executeUpdate(query);
			db.release();		
			
			ItemBean bean = this.getShopItemById(id);
			bean.setCount(bean.getCount() + count);
			bean.setOdd(bean.getOdd() + odd);
		}
		
	}
	
	
	/**
	 * 取得某一类的ID放入内存
	 * @param typeId
	 * @return
	 */
	public List getItemListByType(int typeId) {
		
		synchronized(shopItemIdCache) {
			List idList = (List)shopItemIdCache.get(new Integer(typeId));
			
			if(idList != null)
				return idList;
			
			idList = new ArrayList();
			
			DbOperation db = new DbOperation(5);
			String query = "select id from shop_item where hidden = 1 ";
			if(typeId > 0) {
				query += " and type = " + typeId;
			}
			query += " order by seq desc";
			ResultSet rs = db.executeQuery(query);
			
			try{
				while(rs.next()) {
					idList.add(new Integer(rs.getInt("id")));
				}
			}catch(SQLException e) {
				e.printStackTrace();
				return idList;
			}finally{
				db.release();
			}
			if(idList.size() > 0)
				shopItemIdCache.put(new Integer(typeId), idList);
			return idList;
		}
	}
	
	public List getItemListByTypeAdmin(int typeId) {
		
			List idList = new ArrayList();
			
			idList = new ArrayList();
			
			DbOperation db = new DbOperation(5);
			String query = "select id from shop_item ";
			if(typeId > 0) {
				query += "where  type = " + typeId;
			}
			
			query += " order by seq desc";
			
			ResultSet rs = db.executeQuery(query);
			
			try{
				while(rs.next()) {
					idList.add(new Integer(rs.getInt("id")));
				}
			}catch(SQLException e) {
				e.printStackTrace();
				return idList;
			}finally{
				db.release();
			}
			return idList;
	}
	
	
	/**
	 * 取得所有物品加入缓存
	 */
	public static void getAllShopItem() {
		DbOperation db = new DbOperation(5);
		shopItemCache = new HashMap(200);
		String query = "select * from shop_item";
			
		ResultSet rs = db.executeQuery(query);
			
		try{
			synchronized(shopItemCache) {
				while(rs.next()) {
					ItemBean bean = getItemBean(rs);
					shopItemCache.put(new Integer(bean.getId()), bean);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
	}
	
	private static ItemBean getItemBean(ResultSet rs) throws SQLException {
		ItemBean bean = new ItemBean();
		bean.setCount(rs.getInt("count"));
		bean.setFunType(rs.getInt("fun_type"));
		bean.setFunType2(rs.getInt("fun_type2"));
		bean.setId(rs.getInt("id"));
		bean.setItemId(rs.getInt("item_id"));
		bean.setPresent(rs.getInt("present"));
		bean.setPrice(rs.getFloat("price"));
		bean.setSugguest(rs.getInt("sugguest"));
		bean.setTime(rs.getTimestamp("time"));
		bean.setType(rs.getInt("type"));
		bean.setMax(rs.getInt("max"));
		bean.setOdd(rs.getInt("odd"));
		bean.setHidden(rs.getInt("hidden"));
		bean.setDesc(rs.getString("desc"));
		bean.setFlag(rs.getInt("flag"));
		bean.setPhotoUrl(rs.getString("photo_url"));
		bean.setSeq(rs.getInt("seq"));
		bean.setName(rs.getString("name"));
		bean.setTimes(rs.getInt("times"));
		bean.setDue(rs.getInt("due"));
		return bean;
	}
	
	public ItemBean getShopItemByItemId(int itemId) {
		DbOperation db = new DbOperation(5);
		String query = "select * from shop_item where item_id = " + itemId;
		ItemBean bean =  null;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				bean = getItemBean(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return bean;
	}
	/**
	 * 取得一个物品
	 * @param id
	 * @return
	 */
	public ItemBean getShopItemById(int id) {
		ItemBean bean;
		synchronized(shopItemCache){
			bean = (ItemBean)shopItemCache.get(new Integer(id));
			if(bean == null) {
				DbOperation db = new DbOperation(5);
				String query = "select * from shop_item where id = " + id;
				
				ResultSet rs = db.executeQuery(query);
				
				try{
					if(rs.next()) {
						bean = getItemBean(rs);
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}finally{
					db.release();
				}
				
				if(bean != null)
					shopItemCache.put(new Integer(id), bean);
			}
			
		}
		return bean;
	}
	
	/**
	 * 把一个商品增加到某个用户的收藏夹中
	 * @param favoriteBean
	 * @return
	 */
	public boolean addFavorite(FavoriteBean favoriteBean) {
		DbOperation db = new DbOperation(5);
		
		String query = "insert into shop_favorite(uid, item_id, time) values(?,?,now())";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		
		try{
			db.getPStmt().setInt(1, favoriteBean.getUid());
			db.getPStmt().setInt(2, favoriteBean.getItemId());
			
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
	 * 得到收藏夹的信息
	 * @param uid
	 * @param start
	 * @param limit
	 * @return
	 */
	public List getFavoriteList(int uid, int start, int limit) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		
		String query = "select * from shop_favorite where uid = " + uid + " limit " + start + ", " + limit;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				list.add(getFavoriteBean(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return list;
	}
	
	public int getCountFavorite(int uid) {
		DbOperation db = new DbOperation(5);
		
		String query = "select count(*) as count from shop_favorite where uid = " + uid;
		
		ResultSet rs = db.executeQuery(query);
		int count = 0;
		try{
			if(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		
		return count;
	}
	
	
	
	
	private FavoriteBean getFavoriteBean(ResultSet rs) throws SQLException {
		FavoriteBean bean = new FavoriteBean();
		bean.setId(rs.getInt("id"));
		bean.setItemId(rs.getInt("item_id"));
		bean.setTime(rs.getTimestamp("time"));
		bean.setUid(rs.getInt("uid"));
		
		return bean;
	}
	
	public boolean isContainFavorite(int itemId, int uid) {
		
		DbOperation db = new DbOperation(5);
		
		String query = "select * from shop_favorite where item_id = " + itemId + " and uid = " + uid;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return true;
		} finally {
			db.release();
		}
		
		return false;
	}
	
	public void deleteFavorite(int id, int uid) {
		DbOperation db = new DbOperation(5);
		String query = "delete from shop_favorite where id = " + id + " and uid = " + uid;
		db.executeUpdate(query);
		db.release();
	}
	
	
	
	//礼物操作
	public UserGiftBean getUserGift(int uid, int itemId) {
		DbOperation db = new DbOperation(5);
		String query = "delete from user_gift where uid = " + uid + " and item_id = " + itemId;
		ResultSet rs = db.executeQuery(query);
		UserGiftBean bean = null;
		try{
			if(rs.next()) {
				bean = new UserGiftBean();
				bean.setId(rs.getInt("id"));
				bean.setCount(rs.getInt("count"));
				bean.setItemId(rs.getInt("item_id"));
				bean.setTime(rs.getTimestamp("time"));
				bean.setUid(rs.getInt("uid"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		} finally {
			db.release();
		}
		return bean;
	}
	
	public boolean isContainGift(int itemId, int uid) {
		DbOperation db = new DbOperation(5);
		String query = "select * from user_gift where item_id = " + itemId + " and uid = " + uid;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return true;
		} finally {
			db.release();
		}
		
		return false;
	}
	
	public boolean deleteUserGift(int uid, int itemId) {
		DbOperation db = new DbOperation(5);
		String query = "delete from user_gift where uid = " + uid + " and item_id = " + itemId;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean addUserGift(UserGiftBean bean){
		DbOperation db = new DbOperation(5);
		
		String query = "insert into user_gift(item_id, uid, time, count) values(?,?,now(),1)";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getItemId());
			pstmt.setInt(2, bean.getUid());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public void updateUserGiftCount(int itemId, int uid, int count) {
		DbOperation db = new DbOperation(5);
		String query = "update user_gift set count = count + " + count + " where uid = " + uid + " and item_id = " + itemId;
		db.executeUpdate(query);
		db.release();
	}
	
	public boolean addUserGetGift(UserGetGiftBean bean) {
		DbOperation db = new DbOperation(5);
		
		String query = "insert into user_get_gift(item_id, from_uid, to_uid, time) values(?,?,?,now())";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getItemId());
			pstmt.setInt(2, bean.getFromUid());
			pstmt.setInt(3, bean.getToUid());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean deleteUserGetGift(int id, int toUid) {
		DbOperation db = new DbOperation(5);
		String query = "delete from user_get_gift where id = " + id + " and to_uid = " + toUid;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public ShopTypeBean getShopTypeById(int id) {
		DbOperation db = new DbOperation(5);
		String query = "select * from shop_type where id = " + id;
		
		ResultSet rs = db.executeQuery(query);
		ShopTypeBean bean = null;
		try{
			if(rs.next()) {
				bean = new ShopTypeBean();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		
		return bean;
	}
	
	public boolean addShopType(int id, String typeName) {
		DbOperation db = new DbOperation(5);
		String query = "insert into shop_type(name) values(?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			//pstmt.setInt(1, id);
			pstmt.setString(1, typeName);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		types = null;
		return true;
	}
	
	public boolean updateShopType(int id, String typeName) {
		DbOperation db = new DbOperation(5);
		String query = "update shop_type set name = '" 
			+ StringUtil.toSql(typeName) 
			+ "' where id = " + id;
		db.executeUpdate(query);
		db.release();
		
		types = null;
		return true;
	}
	
	public static int getMaxShopTypeId() {
		DbOperation db = new DbOperation(5);
		String query = "select * from shop_type order by id desc limit 1";
		
		ResultSet rs = db.executeQuery(query);
		
		int max = 10;
		try{
			if(rs.next()) {
				max = rs.getInt("id");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return max;
	}
	
	public static String[] types;
	
	public static String[] getShopTypes(){
		if(types == null) {
			synchronized(ShopService.class) {
				if(types == null) {
					DbOperation db = new DbOperation(5);
					String query = "select * from shop_type order by id asc";
					
					ResultSet rs = db.executeQuery(query);
					
					types = new String[getMaxShopTypeId() + 1];
					types[0] = "全部";
					try{
						while(rs.next()) {
							types[rs.getInt("id")] = rs.getString("name");
						}
					}catch(SQLException e) {
						e.printStackTrace();
					}finally{
						db.release();
					}
				}
			}
		}
		return types;
	}
	
	public boolean addAdsType(String name) {
		DbOperation db = new DbOperation(5);
		String query = "insert into shop_ad_type set name = '" + StringUtil.toSql(name) + "'";
		
		db.executeUpdate(query);
		
		db.release();
		
		return true;
	}
	
	public List getAllAdsTypes() {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		
		String query = "select * from shop_ad_type";
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				String[] str = new String[3];
				str[0] = rs.getString("id");
				str[1] = rs.getString("name");
				list.add(str);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return list;
	}
	
	
	public boolean deleteShopAds(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from shop_ad where id = " + id;
		
		db.executeUpdate(query);
		
		db.release();
		ads = null;
		synchronized(adsMap) {
			adsMap.clear();
		}
		return true;
		
	}
	
	public String[] getShopAds(int itemId) {
		DbOperation db = new DbOperation(5);
		String query = "select * from shop_ad where id = " + itemId;
		
		ResultSet rs = db.executeQuery(query);
		String[] str = new String[3];
		try{
			if(rs.next()) {
				str[0] = rs.getString("id");
				str[1] = rs.getString("link");
				str[2] = rs.getString("info");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return str;
		}finally{
			db.release();
		}
		
		return str;
	}
	
	
	public boolean saveAds(String link, String info, int type) {
			DbOperation db = new DbOperation(5);
			String query = "insert into shop_ad set link = '" + StringUtil.toSql(link) + "', info = '" + info + "', type = " + type;
			db.executeUpdate(query);
			db.release();
			ads = null;
			
			synchronized(adsMap) {
				adsMap.remove(new Integer(type));
			}
			return true;
	}
	
	public int getCountAllAds(){
		int count = 0;
		
		DbOperation db = new DbOperation(5);
		
		String query = "select count(*) as count from shop_ad";
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		
		return count;
	}
	
	
	public static List ads;
	
	public static List getAllAds() {
		if(ads == null) {
			synchronized(ShopService.class) {
				if(ads == null) {
				ads = new ArrayList();
					DbOperation db = new DbOperation(5);
					
					String query = "select * from shop_ad";
					
					ResultSet rs = db.executeQuery(query);
					
					try{
						while(rs.next()) {
							String[] str = new String[4];
							str[0] = rs.getString("id");
							str[1] = rs.getString("link");
							str[2] = rs.getString("info");
							str[3] = rs.getString("type");
							ads.add(str);
						}
					}catch(SQLException e) {
						e.printStackTrace();
					}finally{
						db.release();
					}
				}
			}
		}
		return ads;
	}
	
	public static HashMap adsMap = new HashMap();
	
	public static List getAdsByType(int typeId) {
		List list;
		synchronized(adsMap) {
			list = (List)adsMap.get(new Integer(typeId));
			
			if(list == null) {
				list = new ArrayList();
				DbOperation db = new DbOperation(5);
				String query = "select * from shop_ad where type = " + typeId;
				ResultSet rs = db.executeQuery(query);
				try{
					while(rs.next()) {
						String[] str = new String[4];
						str[0] = rs.getString("id");
						str[1] = rs.getString("link");
						str[2] = rs.getString("info");
						str[3] = rs.getString("type");
						list.add(str);
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}finally{
					db.release();
				}
				adsMap.put(new Integer(typeId), list);
			}
		}
		return list;
	}
	public static List getSugguestList(int limit){
		return shopService.getSugguest(limit);
	}
	
	public static List getTopSugguestList(int limit){
		return shopService.getTop(limit);
	}
	
	
	public List getItemListByCondition2(String condition) {
		DbOperation db = new DbOperation(5);
		String query = "select id from shop_item ";
		
		if(condition != null) {
			query += " where " + condition;
		}
		
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try{
			while(rs.next()) {
				list.add(new Integer(rs.getInt("id")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
}
