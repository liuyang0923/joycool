<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="java.sql.SQLException"%><html>

<%
//执行结果标题
	out.print("<b>对user_status表产生如下更新操作：</b><br><hr><br>");
	
	
// 同步user_infor 和 user_status 表

// 数据库操作出始化
	DbOperation dbOp = new DbOperation();
	dbOp.init();
	DbOperation dbOp2 = new DbOperation();
	dbOp2.init();
	DbOperation dbOp1 = new DbOperation();
	dbOp1.init();

	// 构建查询语句
	String query = "select id from user_info order by id";
	// 查询
	ResultSet rs = dbOp.executeQuery(query);
	ResultSet nrs = null;

	try {
		// 有结果，添加user_status表缺少的记录
		int id = 0;
		while (rs.next()) {
			    query = "select id from user_status where user_id = " + rs.getInt("id");
				nrs = dbOp1.executeQuery(query);
				if(!nrs.next()){
			        query = "insert into user_status set user_id=" + rs.getInt("id") + ", game_point=10000, last_login_time=now(), last_logout_time=now(), login_count=1";
				    dbOp2.executeUpdate(query);
				}
				/*
				UserStatusBean usb = new UserStatusBean();
            	usb.setUserId(rs.getInt("id"));
            	usb.setPoint(0);
            	usb.setRank(0);
            	usb.setGamePoint(10000);
            	usb.setNicknameChange(0);
            	usb.setLoginCount(1);
            	// 添加user_status表缺少的记录
            	userServic.addUserStatus(usb);
				*/
		}
	}catch (SQLException e) {
			e.printStackTrace();
		}
		
		
// 最多取100000条访问次数递减的手机号

	String mobile = "";
	String mobileUser = "";
	int rank = 0;
	int id = 0;
	int j = 1;
	boolean  result = false;
	
	// 构建查询语句
	query = "select count(id) as c, mobile from jc_log_mobile_ua group by mobile order by c desc limit 0, 30000";
	// 查询
	rs = dbOp.executeQuery(query);	
	try {
		// 有结果，对结果进行封装
		while (rs.next()) {
			rank = 0;
			int count = Integer.parseInt(rs.getString("c"));
			if (count>=1000){
				rank = 7;
			}
			else if ( (count>=800)&&(count<1000) ){
				rank = 6;
			}
			else if ( (count>=500)&&(count<800) ){
				rank = 5;
			}
			else if ( (count>=300)&&(count<500) ){
				rank = 4;
			}
			else if ( (count>=200)&&(count<300) ){
				rank = 3;
			}
			else if ( (count>=100)&&(count<200) ){
				rank = 2;
			}
			else if ( (count>=10)&&(count<100) ){
				rank = 1;
			}
			
			mobile = rs.getString("mobile");
			
			// 以user_info表数据为内循环，进行user_status表中rank修改
			
			id = -1;			
			ResultSet rs2 = dbOp2.executeQuery("SELECT id, mobile FROM user_info where mobile = '" + mobile + "'");
			if (rs2.next()) {
				id = rs2.getInt("id");
				mobileUser = rs2.getString("mobile");
			}						
			
			if (id != -1){
				if (mobileUser != null){
					if ((!mobileUser.equals(""))){
					// 对用user_infor表中非空手机号进行操作
						if (mobileUser.equals(mobile)){
							//if (rank != 0){
							// 如果表中rank有默认值0，加上上面if，只修改rank不为0记录							
							result = dbOp1.executeUpdate("UPDATE user_status SET rank = " + rank + " WHERE user_id = " + id);
							// 执行结果显示
							out.print(j);
							//out.print("&nbsp;&nbsp;&nbsp;用户id："+id);
							out.print("&nbsp;&nbsp;&nbsp;用户mobile："+mobileUser);
							out.print("&nbsp;&nbsp;&nbsp;将用户rank更改为：<b>"+rank+"</b>");
							if (result){
								out.print("&nbsp;&nbsp;&nbsp; success!");
							}else{
								out.print("&nbsp;&nbsp;&nbsp;<b>faile?</b>");
								// 回滚(没有关联操作，不必要)
							}
							out.print("<br>");
					
							j++;

							//}			
						}
					}
				}
				}
			
			
		}
	} catch (SQLException e) {
			e.printStackTrace();
		}

out.print("<br><br> update over!");

// 释放资源
    dbOp2.release();
	dbOp1.release();
	dbOp.release();

%>

</html>