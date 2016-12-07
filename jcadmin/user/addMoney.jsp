<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*, java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.bean.bank.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction" %><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<p>给用户加银行存款&nbsp;&nbsp;<a href="index.jsp">返回</a></p>
<p align=center>
<%
String defaultId = request.getParameter("defaultId");
if(defaultId==null) defaultId="";
String action = request.getParameter("action");
if("add".equalsIgnoreCase(action)){
int id = StringUtil.toInt(request.getParameter("id"));

String data = request.getParameter("money");
try{
	Long.parseLong(data);
}catch(Exception e){
	out.print("乐币数据错误");
	return;
}
long money = StringUtil.toLong(request.getParameter("money"));

IUserService userService = ServiceFactory.createUserService();
IBankService bankService = ServiceFactory.createBankService();
UserBean user = UserInfoUtil.getUser(id);
if(user==null){
	out.print("该用户不存在！");
	return;
}
else{
if(money>0){
	long quota = SqlUtil.getLongResult("select money from admin_quota where id="+adminUser.getId(),"mcool");
	if(quota<money){
		out.print("乐币发放限额不足"); 
	}else{
		SqlUtil.executeUpdate("update admin_quota set money=money-"+money+" where id="+adminUser.getId());
		StoreBean store = bankService.getStore("user_id=" + id);
		if(store==null){
			store = new StoreBean();
			store.setMoney(money);
			store.setUserId(id);
			bankService.addStore(store);
			MoneyLogBean moneyLog = new MoneyLogBean();
			moneyLog.setUserId(id);
			moneyLog.setRUserId(0);
			moneyLog.setMoney(money);
			moneyLog.setType(Constants.BANK_ADMIN_OPERATE_TYPE);
			bankService.addMoneyLog(moneyLog);
		    String sql = "insert into jc_add_money_log(user_id,money,time) values(" + id + "," + money + ", now())";
		    SqlUtil.executeUpdate(sql, Constants.DBShortName);
		    UserInfoUtil.getMoneyStat()[18] += money;
		}
		else{
			bankService.updateStore("money=money+" + money, "user_id=" + id);
			String sql = "insert into jc_add_money_log(user_id,money,time) values(" + id + "," + money + ", now())";
		    SqlUtil.executeUpdate(sql, Constants.DBShortName);
			MoneyLogBean moneyLog = new MoneyLogBean();
			moneyLog.setUserId(id);
			moneyLog.setRUserId(0);
			moneyLog.setMoney(money);
			moneyLog.setType(Constants.BANK_ADMIN_OPERATE_TYPE);
			bankService.addMoneyLog(moneyLog);
			UserInfoUtil.getMoneyStat()[18] += money;
		}
		BankCacheUtil.flushBankStoreById(id);
		store = bankService.getStore("user_id=" + id);
		out.print("给用户" + id + "的银行存款加上了" + money + "乐币，他现在的银行存款是：" + store.getMoney()); 
		LogUtil.logAdmin(adminUser.getName()+"给用户"+id+"加存款"+money + "乐币");
	}
}else{
	money = Math.abs(money);
	UserStatusBean userStatus = UserInfoUtil.getUserStatus(id);
	if(userStatus==null){
		out.print("该用户不存在！");
		return;
	}
	else if(userStatus.getGamePoint()<money){//乐币不够
		StoreBean store = bankService.getStore("user_id=" + id);
		if(store==null){ //无存款
			UserInfoUtil.updateUserStatus("game_point=0", "user_id=" + id,id,UserCashAction.OTHERS,"管理员扣除用户身上乐币为零");
			money = userStatus.getGamePoint();
			String sql = "insert into jc_add_money_log(user_id,money,time) values(" + id + "," + (-1)*money + ", now())";
		    SqlUtil.executeUpdate(sql, Constants.DBShortName);
			MoneyLogBean moneyLog = new MoneyLogBean();
			moneyLog.setUserId(id);
			moneyLog.setRUserId(0);
			moneyLog.setMoney(0);
			moneyLog.setType(Constants.BANK_ADMIN_OPERATE_TYPE);
			bankService.addMoneyLog(moneyLog);
			out.print("该用户的乐币和存款已经清0！");
		}
		else if((userStatus.getGamePoint() + store.getMoney())<money){ //乐币加存款不够
			UserInfoUtil.updateUserStatus("game_point=0", "user_id=" + id,id,UserCashAction.OTHERS,"管理员扣除用户身上乐币为零");
			bankService.updateStore("money=0", "user_id=" + id);
			String sql = "insert into jc_add_money_log(user_id,money,time) values(" + id + "," + (-1)*(userStatus.getGamePoint() + store.getMoney()) + ", now())";
		    SqlUtil.executeUpdate(sql, Constants.DBShortName);
			MoneyLogBean moneyLog = new MoneyLogBean();
			moneyLog.setUserId(id);
			moneyLog.setRUserId(0);
			moneyLog.setMoney(0);
			moneyLog.setType(Constants.BANK_ADMIN_OPERATE_TYPE);
			bankService.addMoneyLog(moneyLog);
			out.print("该用户的乐币和存款已经清0！");
		}
		else{ //存款足够
			bankService.updateStore("money=money-" + money, "user_id=" + id);
			String sql = "insert into jc_add_money_log(user_id,money,time) values(" + id + "," + (-1)*money + ", now())";
		    SqlUtil.executeUpdate(sql, Constants.DBShortName);
		    MoneyLogBean moneyLog = new MoneyLogBean();
			moneyLog.setUserId(id);
			moneyLog.setRUserId(0);
			moneyLog.setMoney(-money);
			moneyLog.setType(Constants.BANK_ADMIN_OPERATE_TYPE);
			bankService.addMoneyLog(moneyLog);
		    out.print("该用户的存款扣除" + money);
		}
		UserInfoUtil.flushUserStatus(id);
		BankCacheUtil.flushBankStoreById(id);
	}
	else{
		UserInfoUtil.updateUserStatus("game_point=game_point-" + money, "user_id=" + id,id,UserCashAction.OTHERS,"管理员扣除用户身上乐币为零");
		String sql = "insert into jc_add_money_log(user_id,money,time) values(" + id + "," + (-1)*money + ", now())";
	    SqlUtil.executeUpdate(sql, Constants.DBShortName);
		out.print("该用户的乐币已经扣除" + money);
	}
	//userService.updateUserStatus("")
}
}
}
else if("query".equalsIgnoreCase(action)){
	int id = StringUtil.toInt(request.getParameter("id"));

	IUserService userService = ServiceFactory.createUserService();
	IBankService bankService = ServiceFactory.createBankService();
	UserBean user = UserInfoUtil.getUser(id);
	if(user==null){
		out.print("该用户不存在！");
		return;
	}
	
	UserStatusBean userStatus = userService.getUserStatus("user_id=" + id);
	StoreBean store = bankService.getStore("user_id=" + id);
%>
用户的乐币：<%= userStatus.getGamePoint() %>&nbsp;&nbsp;用户的存款：<%= (store==null)?0:store.getMoney() %>
<table width="90%" border="1">       
		<tr>
			<td width="30%">用户ID</td>
			<td width="30%">乐币数</td>
			<td width="30%">时间</td>
		</tr>		
<%	
	String sql = "select * from jc_add_money_log where user_id=" + id; 
    //数据库操作类
	DbOperation dbOp = new DbOperation();
	dbOp.init();	

	// 查询
	ResultSet rs = dbOp.executeQuery(sql);

	try {
		// 结果不为空
		while (rs.next()) {
			int userId = rs.getInt("user_id");
			long money = rs.getLong("money");
			String time = rs.getString("time");
			%>
		<tr>
			<td width="30%"><%= userId %></td>
			<td width="30%"><%= money %></td>
			<td width="30%"><%= time %></td>
		</tr>
			<%
		}
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		// 释放资源
		dbOp.release();
	}
%>
</table>
<%
}
%>
</p>
<table width="90%" border="1">       
		<form method="post">
		<input type=hidden name="action" value="query">
		<tr>
			<td colspan=2 >查询添加记录</td>
		</tr>
		<tr>
			<td width="30%">用户ID（数字）</td>
			<td width="70%">
			  <input type=text name="id">
			</td>
		</tr>		
		<tr>
			<td colspan=2>
				<input type=submit value="查询">
			</td>
		</tr>
		</form>
</table>	
<br>
<table width="90%" border="1">       
		<form method="post">
		<input type=hidden name="action" value="add">
		<tr>
			<td colspan=2 >添加乐币</td>
		</tr>
		<tr>
			<td width="30%">用户ID（数字）</td>
			<td width="70%">
			  <input type=text name="id" value="<%=defaultId%>">
			</td>
		</tr>
		<tr>
			<td width="30%">增加乐币（负数表示扣乐币）</td>
			<td width="70%">
			  <input type=text name="money">
			</td>
		</tr>
		<tr>
			<td colspan=2>
				<input type=submit value="添加">
			</td>
		</tr>
		</form>
</table>	