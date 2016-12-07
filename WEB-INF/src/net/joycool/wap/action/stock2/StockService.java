package net.joycool.wap.action.stock2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.stock2.StockAccountBean;
import net.joycool.wap.bean.stock2.StockBean;
import net.joycool.wap.bean.stock2.StockCCBean;
import net.joycool.wap.bean.stock2.StockCJBean;
import net.joycool.wap.bean.stock2.StockNoticeBean;
import net.joycool.wap.bean.stock2.StockWTBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author macq
 * @explain：新版股票数据库操作方法
 * @datetime:2007-4-25 14:59:30
 */
public class StockService {
	
	static String STOCK_CACHE_GROUP = "stock2";

	public StockBean getStock(String condition) {
		StockBean stock = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			if (rs.next()) {
				stock = this.getStock(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stock;
	}

	public Vector getStockList(String condition) {
		Vector stockList = new Vector();
		StockBean stock = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			while (rs.next()) {
				stock = this.getStock(rs);
				stockList.add(stock);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockList;
	}

	public boolean addStock(StockBean bean) {

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO stock(name,code,price,end_price,count,desc,create_datetime) VALUES(?,?,?,?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getCode());
			pstmt.setFloat(3, bean.getPrice());
			pstmt.setFloat(4, bean.getEndPrice());
			pstmt.setString(5, bean.getDesc());
			pstmt.setTimestamp(6, bean.getCreateDatetime());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		dbOp.release();
		return true;
	}

	public boolean deleteStock(String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "DELETE FROM stock WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public boolean updateStock(String set, String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "UPDATE stock SET " + set + " WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public int getStockCount(String condition) {
		int count = 0;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT count(id) as c_id FROM stock WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return count;
	}
	
	public StockAccountBean getStockAccount(String condition) {
		StockAccountBean stockAccount = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_account";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			if (rs.next()) {
				stockAccount = this.getStockAccount(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockAccount;
	}

	public Vector getStockAccountList(String condition) {
		Vector stockAccountList = new Vector();
		StockAccountBean stockAccount = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_account";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			while (rs.next()) {
				stockAccount = this.getStockAccount(rs);
				stockAccountList.add(stockAccount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockAccountList;
	}

	public boolean addStockAccount(StockAccountBean bean) {

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO stock_account(user_id,money,money_f,create_datetime) VALUES(?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setLong(2, bean.getMoney());
			pstmt.setLong(3, bean.getMoneyF());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		//获取插入数据记录id
		bean.setId(dbOp.getLastInsertId());
		

		dbOp.release();
		return true;
	}

	public boolean deleteStockAccount(String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "DELETE FROM stock_account WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public boolean updateStockAccount(String set, String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "UPDATE stock_account SET " + set + " WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public int getStockAccountCount(String condition) {
		int count = 0;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT count(id) as c_id FROM stock_account WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return count;
	}
	
	public StockCCBean getStockCC(String condition) {
		StockCCBean stockCC= null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_cc";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			if (rs.next()) {
				stockCC = this.getStockCC(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockCC;
	}

	public Vector getStockCCList(String condition) {
		Vector stockCCList = new Vector();
		StockCCBean stockCC = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_cc";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			while (rs.next()) {
				stockCC = this.getStockCC(rs);
				stockCCList.add(stockCC);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockCCList;
	}

	public boolean addStockCC(StockCCBean bean) {

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO stock_cc(user_id,stock_id,count,count_f,cost,create_datetime) VALUES(?,?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getStockId());
			pstmt.setLong(3, bean.getCount());
			pstmt.setLong(4, bean.getCountF());
			pstmt.setLong(5, bean.getCost());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		dbOp.release();
		return true;
	}

	public boolean deleteStockCC(String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "DELETE FROM stock_cc WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public boolean updateStockCC(String set, String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "UPDATE stock_cc SET " + set + " WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public int getStockCCCount(String condition) {
		int count = 0;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT count(id) as c_id FROM stock_cc WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return count;
	}
	
	public StockWTBean getStockWT(String condition) {
		StockWTBean stockWT = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_wt";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			if (rs.next()) {
				stockWT = this.getStockWT(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockWT;
	}

	public Vector getStockWTList(String condition) {
		Vector stockWTList = new Vector();
		StockWTBean stockWT = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_wt";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			while (rs.next()) {
				stockWT = this.getStockWT(rs);
				stockWTList.add(stockWT);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockWTList;
	}
	
	/**
	 *  
	 * @author macq
	 * @explain： 获取买卖五手信息列表(带缓存)
	 * @datetime:2007-4-25 16:45:55
	 * @param condition
	 * @return
	 * @return Vector
	 */
	public Vector getStockWTTop5List(String condition) {

		String query = condition;
		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector stockWTList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.STOCK2_TOP_COUNT_GROUP, OsCacheUtil.STOCK2_TOP_FLUSH_PERIOD);
			if (stockWTList != null) {
				return stockWTList;
			}
		}
		
		Vector stockWTList = new Vector();
		StockWTBean stockWT = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();


		ResultSet rs = dbOp.executeQuery(query);
		try {

			while (rs.next()) {
				stockWT = new StockWTBean();
				stockWT.setPrice(rs.getFloat("price"));
				stockWT.setCount(rs.getLong("wt_count"));
				stockWTList.add(stockWT);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if (stockWTList != null) {
				OsCacheUtil.put(key, stockWTList, OsCacheUtil.STOCK2_TOP_COUNT_GROUP);
			}
		}
		return stockWTList;
	}

	public boolean addStockWT(StockWTBean bean) {

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO stock_wt(user_id,stock_id,price,count,cj_count,mark,create_datetime) VALUES(?,?,?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getStockId());
			pstmt.setFloat(3, bean.getPrice());
			pstmt.setLong(4, bean.getCount());
			pstmt.setLong(5, bean.getCjCount());
			pstmt.setInt(6, bean.getMark());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		dbOp.release();
		return true;
	}

	public boolean deleteStockWT(String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "DELETE FROM stock_wt WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public boolean updateStockWT(String set, String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "UPDATE stock_wt SET " + set + " WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public int getStockWTCount(String condition) {
		int count = 0;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT count(id) as c_id FROM stock_wt WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return count;
	}
	
	public StockCJBean getStockCJ(String condition) {
		StockCJBean stockCJ= null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_cj";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			if (rs.next()) {
				stockCJ = this.getStockCJ(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockCJ;
	}

	public Vector getStockCJList(String condition) {
		Vector stockCJList = new Vector();
		StockCJBean stockCJ = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_cj";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			while (rs.next()) {
				stockCJ= this.getStockCJ(rs);
				stockCJList.add(stockCJ);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockCJList;
	}

	public boolean addStockCJ(StockCJBean bean) {

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO stock_cj(user_id,stock_id,wt_id,price,cj_count,mark,`count`,charge,create_datetime) VALUES(?,?,?,?,?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getStockId());
			pstmt.setInt(3, bean.getWtId());
			pstmt.setFloat(4, bean.getPrice());
			pstmt.setLong(5, bean.getCjCount());
			pstmt.setInt(6, bean.getMark());
			pstmt.setLong(7, bean.getCount());
			pstmt.setLong(8, bean.getCharge());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		dbOp.release();
		return true;
	}

	public boolean deleteStockCJ(String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "DELETE FROM stock_cj WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public boolean updateStockCJ(String set, String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "UPDATE stock_cj SET " + set + " WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public int getStockCJCount(String condition) {
		int count = 0;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT count(id) as c_id FROM stock_cj WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return count;
	}
	
	
	public StockNoticeBean getStockNotice(String condition) {
		StockNoticeBean stockNotice= null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_notice";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			if (rs.next()) {
				stockNotice = this.getStockNotice(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockNotice;
	}

	public Vector getStockNoticeList(String condition) {
		Vector stockNoticeList = new Vector();
		StockNoticeBean stockNotice = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT * from stock_notice";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {

			while (rs.next()) {
				stockNotice= this.getStockNotice(rs);
				stockNoticeList.add(stockNotice);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return stockNoticeList;
	}

	public boolean addStockNotice(StockNoticeBean bean) {

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO stock_notice(title,content,create_datetime) VALUES(?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		dbOp.release();
		return true;
	}

	public boolean deleteStockNotice(String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "DELETE FROM stock_notice WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public boolean updateStockNotice(String set, String condition) {
		boolean result;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "UPDATE stock_notice SET " + set + " WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public int getStockNoticeCount(String condition) {
		int count = 0;

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "SELECT count(id) as c_id FROM stock_notice WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return count;
	}
	
	private StockBean getStock(ResultSet rs) throws SQLException {
		StockBean stock = new StockBean();
		stock.setId(rs.getInt("id"));
		stock.setName(rs.getString("name"));
		stock.setCode(rs.getString("code"));
		stock.setPrice(rs.getFloat("price"));
		stock.setEndPrice(rs.getFloat("end_price"));
		stock.setStartPrice(rs.getFloat("start_price"));
		stock.setCount(rs.getLong("count"));
		stock.setTotalCount(rs.getLong("total_count"));
		stock.setDesc(rs.getString("desc"));
		stock.setUrl(rs.getString("url"));
		stock.setStatus(rs.getInt("status"));
		stock.setType(rs.getInt("type"));
		stock.setCreateDatetime(rs.getTimestamp("create_datetime"));
		stock.setWithdraw(rs.getLong("withdraw"));
		return stock;
	}

	private StockAccountBean getStockAccount(ResultSet rs) throws SQLException {
		StockAccountBean stockAccount = new StockAccountBean();
		stockAccount.setId(rs.getInt("id"));
		stockAccount.setUserId(rs.getInt("user_id"));
		stockAccount.setMoney(rs.getLong("money"));
		stockAccount.setMoneyF(rs.getLong("money_f"));
		stockAccount.setAsset(rs.getLong("asset"));
		stockAccount.setCreateDatetime(rs.getTimestamp("create_datetime"));
		return stockAccount;
	}

	private StockCCBean getStockCC(ResultSet rs) throws SQLException {
		StockCCBean stockCC = new StockCCBean();
		stockCC.setId(rs.getInt("id"));
		stockCC.setUserId(rs.getInt("user_id"));
		stockCC.setStockId(rs.getInt("stock_id"));
		stockCC.setCount(rs.getLong("count"));
		stockCC.setCountF(rs.getLong("count_f"));
		stockCC.setCost(rs.getLong("cost"));
		stockCC.setCreatedatetime(rs.getString("create_datetime"));
		return stockCC;
	}

	private StockWTBean getStockWT(ResultSet rs) throws SQLException {
		StockWTBean stockWT = new StockWTBean();
		stockWT.setId(rs.getInt("id"));
		stockWT.setUserId(rs.getInt("user_id"));
		stockWT.setStockId(rs.getInt("stock_id"));
		stockWT.setPrice(rs.getFloat("price"));
		stockWT.setCount(rs.getLong("count"));
		stockWT.setCjCount(rs.getLong("cj_count"));
		stockWT.setMark(rs.getInt("mark"));
		stockWT.setCreatedatetime(rs.getString("create_datetime"));
		return stockWT;
	}
	
	private StockNoticeBean getStockNotice(ResultSet rs) throws SQLException {
		StockNoticeBean stockNotice = new StockNoticeBean();
		stockNotice.setId(rs.getInt("id"));
		stockNotice.setTitle(rs.getString("title"));
		stockNotice.setContent(rs.getString("content"));
		stockNotice.setCreateDatetime(rs.getString("create_datetime"));
		return stockNotice;
	}

	private StockCJBean getStockCJ(ResultSet rs) throws SQLException {
		StockCJBean stockCJ = new StockCJBean();
		stockCJ.setId(rs.getInt("id"));
		stockCJ.setUserId(rs.getInt("user_id"));
		stockCJ.setStockId(rs.getInt("stock_id"));
		stockCJ.setWtId(rs.getInt("wt_id"));
		stockCJ.setPrice(rs.getFloat("price"));
		stockCJ.setCjCount(rs.getLong("cj_count"));
		stockCJ.setCount(rs.getLong("count"));
		stockCJ.setCharge(rs.getLong("charge"));
		stockCJ.setMark(rs.getInt("mark"));
		stockCJ.setCreatedatetime(rs.getTimestamp("create_datetime"));
		return stockCJ;
	}
	/**
	 * 该实现启用缓存请注意，更新时间为12小时
	 */
	public List getUserList(String condition) {
		String query = "SELECT * from stock_account";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			List list = (List) OsCacheUtil.get(key, STOCK_CACHE_GROUP, 600);
			if (list != null) {
				return list;
			}
		}
		List userList = new ArrayList();

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		ResultSet rs = dbOp.executeQuery(query);
		try {

			while (rs.next()) {
				userList.add(getStockAccount(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();

		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, userList, STOCK_CACHE_GROUP);
		}
		return userList;
	}
	
	public void calcStockAccount(StockAccountBean account) {
		long res = SqlUtil.getLongResult("select sum(a.count*b.price) from stock_cc a,stock b where a.user_id=" + account.getUserId() + " and a.stock_id=b.id", 
				Constants.DBShortName);
		account.setStockPrice(res);
		account.calcAsset();
	}
	
	public void calcAllStockAccount() {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "update stock_account c set asset=(select sum(a.count*b.price) from stock_cc a,stock b where a.user_id=c.user_id and a.stock_id=b.id)+c.money+c.money_f";
		dbOp.executeUpdate(query);
		dbOp.release();
	}
}
