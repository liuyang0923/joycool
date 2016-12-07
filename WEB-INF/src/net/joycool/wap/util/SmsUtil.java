package net.joycool.wap.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.joycool.wap.bean.SmsBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.admin.AdminAction;
import net.joycool.wap.util.db.DbOperation;

public class SmsUtil {
	public static String CODE = "06";

	public static int TYPE_SMS = 0;

	public static int TYPE_PUSH = 1;

	public static Hashtable sendHash = new Hashtable();

	public static int MAX_COUNT_PER_MOBILE = 2;
	
	public static int fromId = 0;	// 防止重复发送同一个id
	
	public static String smsDB = "bea";
	
	public static boolean redirectSend = true;

	public static boolean send(String code, String message, String mobile,
			int type) {
		if(true) {
			return sendSms(message, mobile);
		}
		if (code == null || mobile == null || message == null)
			return false;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init("titan");

		// 构建查询语句
		String query = "Insert into send_queue (code,msg,phone,type,mphone) VALUES(?,?,?,?,'') ";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, code);
			pstmt.setString(2, message);
			pstmt.setString(3, mobile);
			pstmt.setInt(4, type);

			// 执行
			dbOp.executePstmt();

			// liuyi 2007-01-11 短信日志 start
			String sql = "insert into jc_sms_send_log(type_id, mobile, message, time) values("
					+ SmsUtil.TYPE_SMS
					+ ",'"
					+ mobile
					+ "','"
					+ message
					+ "', now() )";
			SqlUtil.executeUpdate(sql, Constants.DBShortName);
			// liuyi 2007-01-11 短信日志 end
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 释放资源
		dbOp.release();

		return true;

	}
	
	public static Pattern pwPattern = Pattern.compile("修改密码[^0-9]*([0-9]+)");
	public static Pattern bpwPattern = Pattern.compile("银行密码[^0-9]*([0-9]+)");
	public static Pattern zcPattern = Pattern.compile("zc[^0-9]*([0-9]+)");
	
	private static IUserService userService = ServiceFactory.createUserService();
	public static void recv() {

		DbOperation dbOp = new DbOperation(5);

		String query = "select * from  receive_message";
		List list = new ArrayList();
		
		try {
			ResultSet rs = dbOp.executeQuery("select * from  receive_message where id>" + fromId + " order by id limit 10");
			while(rs.next()) {
				SmsBean sms = new SmsBean();
				sms.setId(rs.getInt("id"));
				
				sms.setContent(rs.getString("content"));
				sms.setMobile(rs.getString("mobile"));
				sms.setLine(rs.getString("line"));
				list.add(sms);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			Iterator iter = list.iterator();

			while(iter.hasNext()) {
				SmsBean sms = (SmsBean)iter.next();
				fromId = sms.getId();
				String content = StringUtil.noEnter(sms.getContent()).trim().toLowerCase();

				Matcher m = pwPattern.matcher(content);
				if(m.find()) {
					changePassword(sms.getMobile(), m.group(1));
				}
				m = bpwPattern.matcher(content);
				if(m.find()) {
					content = m.group(1);
					int userId = SqlUtil.getIntResult("select a.id from user_info a,user_status b where a.mobile='" + StringUtil.toSql(sms.getMobile()) + "' and b.user_id=a.id order by b.last_login_time desc limit 1", Constants.DBShortName);
					if(userId > 0 && content.length() > 0) {
						userService.updateUserSetting("bank_pw='" + StringUtil.toSql(content)
								+ "',update_datetime=now()", "user_id=" + userId);
						CacheManage.userInfo.srm(userId);
						AdminAction.addUserLog(userId, 5, content);
					}
				}
				m = zcPattern.matcher(content);
				if(m.find()) {
					content = m.group(1);
					if(content.length() > 11)
						content = content.substring(0, 11);
					Util.msgRegister(sms.getMobile(), content);
				} else {
					if(content.indexOf("z") != -1 || content.indexOf("注册") != -1 || content.startsWith("a")) {

						Util.msgRegister(sms.getMobile());

					} else if(content.equals("测试流程")) {
						SmsUtil.send(SmsUtil.CODE, "欢迎来到乐酷http://m.joycool.net.您的初始密码为123456,用本手机号和此密码就能登陆.期待您的光临!", sms.getMobile(), SmsUtil.TYPE_SMS);
					} else {
						Util.msgRegister(sms.getMobile());
					}
				}
					
				dbOp.executeUpdate("delete from receive_message where id=" + fromId);
				dbOp.executeUpdate("insert into receive_message_history (content, mobile, line,addtime)values('" + StringUtil.toSql(sms.getContent()) + "','" + StringUtil.toSql(sms.getMobile())+ "','" + sms.getLine() + "',now())");
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dbOp.release();
	}
	
	public static void changePassword(String mobile, String content) {
		String content2 = net.joycool.wap.util.Encoder.encrypt(content.trim());
		int userId = SqlUtil.getIntResult("select a.id from user_info a,user_status b where a.mobile='" + StringUtil.toSql(mobile) + "' and b.user_id=a.id order by b.last_login_time desc limit 1", Constants.DBShortName);
		if(userId > 0 && content.length() > 0) {
			UserInfoUtil.updateUser("password='" + StringUtil.toSql(content2) + "'", "id=" + userId, String.valueOf(userId));
			AdminAction.addUserLog(userId, 4, content);
		}
	}

	 public static boolean sendSms(String message, String mobile){
	 if(mobile==null || message==null)return false;
			
	 //数据库操作类
	 DbOperation dbOp = new DbOperation();
	 dbOp.init(smsDB);
	
	 // 构建查询语句
	 String query = "Insert into shortmessage (mobile,content,addtime, type) VALUES(?,?,now(),0) ";
	
	 if (!dbOp.prepareStatement(query)) {
	 dbOp.release();
	 return false;
	 }
	 // 传递参数
	 PreparedStatement pstmt = dbOp.getPStmt();
	 try {
	 pstmt.setString(1, mobile);
	 pstmt.setString(2, message);
				
	 //执行
	 dbOp.executePstmt();
	 } catch (SQLException e) {
	 e.printStackTrace();
	 dbOp.release();
	 return false;
	 }
	 // 释放资源
	 dbOp.release();
			
	 return true;
	
	 }

	public static boolean sendPush(String message, String mobile, String url) {
		if (mobile == null || message == null || url == null)
			return false;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init("sms");

		// 构建查询语句
		String query = "Insert into shortmessage (mobile,content,url,type,addtime) VALUES(?,?,?,"
				+ SmsUtil.TYPE_PUSH + ",now()) ";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, mobile);
			pstmt.setString(2, message);
			pstmt.setString(3, url);

			// 执行
			dbOp.executePstmt();

			// liuyi 2006-01-11 push日志 start
			String sql = "update sms_log set send_count=send_count+1 where type="
					+ SmsUtil.TYPE_PUSH;
			SqlUtil.executeUpdate(sql, Constants.DBShortName);
			sql = "insert into jc_sms_send_log(type_id, mobile, message, time) values("
					+ SmsUtil.TYPE_PUSH
					+ ",'"
					+ mobile
					+ "','"
					+ message + "|" + url
					+ "', now() )";
			SqlUtil.executeUpdate(sql, Constants.DBShortName);
			// liuyi 2006-01-11 push日志 end
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 释放资源
		dbOp.release();

		return true;

	}
}
