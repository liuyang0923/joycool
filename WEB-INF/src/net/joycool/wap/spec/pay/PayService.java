package net.joycool.wap.spec.pay;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class PayService {
	public List getOrders(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from pay_order where " + cond);
		try{
			while(rs.next()) {
				list.add(getOrder(rs));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return list;
	}
	
	public PayOrderBean getOrder(String cond) {
		PayOrderBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("SELECT * from pay_order where " + cond);
		try{
			if(rs.next())
				bean = getOrder(rs);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		db.release();
		return bean;
	}
	
	public PayOrderBean getOrder(ResultSet rs) throws SQLException {
		PayOrderBean bean = new PayOrderBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setType(rs.getInt("type"));
		bean.setMoney(rs.getInt("money"));
		bean.setCode(rs.getString("code"));
		bean.setBak(rs.getString("bak"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setEndTime(rs.getTimestamp("end_time").getTime());
		bean.setCardId(rs.getString("card_id"));
		bean.setCardPwd(rs.getString("card_password"));
		bean.setResult(rs.getString("result"));
		bean.setResultstr(rs.getString("resultstr"));
		bean.setResult2(rs.getString("result2"));
		bean.setChannelId(rs.getInt("channel_id"));
		
		return bean;
	}
	public boolean addOrder(PayOrderBean bean) {
		DbOperation db = new DbOperation(5);
		
		String query = "insert into pay_order(user_id,type,money,code,bak,create_time,end_time,card_id,card_password,channel_id) values(?,?,?,?,?,now(),now(),?,?,?)";
		
		if(!db.prepareStatement(query)) {
			
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		
		try{
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getType());
			pstmt.setInt(3, bean.getMoney());
			pstmt.setString(4, bean.getCode());
			pstmt.setString(5, bean.getBak());
			pstmt.setString(6, bean.getCardId());
			pstmt.setString(7, bean.getCardPwd());
			pstmt.setInt(8, bean.getChannelId());
			pstmt.execute();
			bean.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		db.release();
		return true;
	}
	
	public boolean updateOrderType(int id, int type, String result, String resultStr) {
		DbOperation db = new DbOperation(5);
		String query = "update pay_order set type = " + type + ", result = '" + StringUtil.toSql(result) + "', resultstr = '" + StringUtil.toSql(resultStr) + "' where id = " + id;	
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean updateOrderResult2(int id, int type, String result2) {
		DbOperation db = new DbOperation(5);
		String query = "update pay_order set type = " + type + ", result2 = '" + result2 + "',end_time=now() where id = " + id;	
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	
	public boolean addPayBean(PayBean bean) {
		DbOperation db = new DbOperation(5);
		
		String query = "insert into pay_channel(name,merchant_id,currency,pm_id,pc_id,merchant_key,notify_url,version_id,search_url,submit_url) values(?,?,?,?,?,?,?,?,?,?)";
		
		if(!db.prepareStatement(query)) {
			
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		
		try{
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getMerchantId());
			pstmt.setString(3, bean.getCurrency());
			pstmt.setString(4, bean.getPmId());
			pstmt.setString(5, bean.getPcId());
			pstmt.setString(6, bean.getMerchantKey());
			pstmt.setString(7, bean.getNotifyURL());
			pstmt.setString(8, bean.getVersionId());
			pstmt.setString(9, bean.getSearchURL());
			pstmt.setString(10, bean.getSubmitURL());
			pstmt.execute();
			bean.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		db.release();
		return true;
	}
	
	public boolean updatePayBean(PayBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "update pay_channel set name = '" 
			+ StringUtil.toSql(bean.getName()) + "', merchant_id = '" 
			+ StringUtil.toSql(bean.getMerchantId()) + "',currency = '" 
			+ StringUtil.toSql(bean.getCurrency()) + "', pm_id = '" 
			+ StringUtil.toSql(bean.getPmId()) + "', pc_id = '" 
			+ StringUtil.toSql(bean.getPcId()) + "', merchant_key = '" 
			+ StringUtil.toSql(bean.getMerchantKey()) + "', notify_url = '" 
			+ StringUtil.toSql(bean.getNotifyURL()) + "', search_url = '"
			+ StringUtil.toSql(bean.getSearchURL()) + "', submit_url = '"
			+ StringUtil.toSql(bean.getSubmitURL()) + "' where id = " + bean.getId();
		
		db.executeUpdate(query);
		
		db.release();
		
		return true;
		
	}
	
	public PayBean getPayBeanById(int id) {
		DbOperation db = new DbOperation(5);
		String query = "select * from pay_channel where id = " + id;
		
		ResultSet rs = db.executeQuery(query);
		PayBean payBean = null;
		try{
			if(rs.next()) {
				payBean = getPayBean(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return payBean;
		
	}
	
	public List getPayBeans(String condition) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "select * from pay_channel";
		
		if(condition != null) {
			query += " where " + condition;
		}
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				PayBean payBean = getPayBean(rs);
				
				list.add(payBean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	private PayBean getPayBean(ResultSet rs) throws SQLException{
		PayBean payBean = new PayBean();
		payBean.setId(rs.getInt("id"));
		payBean.setCurrency(rs.getString("currency"));
		payBean.setMerchantId(rs.getString("merchant_id"));
		payBean.setMerchantKey(rs.getString("merchant_key"));
		payBean.setName(rs.getString("name"));
		payBean.setNotifyURL(rs.getString("notify_url"));
		payBean.setPcId(rs.getString("pc_id"));
		payBean.setPmId(rs.getString("pm_id"));
		payBean.setVersionId(rs.getString("version_id"));
		payBean.setSearchURL(rs.getString("search_url"));
		payBean.setSubmitURL(rs.getString("submit_url"));
		
		return payBean;		
	}
}
