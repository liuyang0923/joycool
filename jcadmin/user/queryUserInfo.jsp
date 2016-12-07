<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.bean.*,  net.joycool.wap.bean.tong.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*, net.joycool.wap.cache.util.*,
                 net.joycool.wap.util.db.*"%><%!
static IUserService service = ServiceFactory.createUserService();
static String[] fs = {"chat","mail","forum","home","tong","team","info","game","fan","sell"};
static String[] fs3 = {"聊天","信件","论坛","家园","帮会","圈子","资料","游戏","沉迷","交易"};
static String[] ad = {"chata","maila","foruma","homea","tonga","teama","infoa","gamea","newsa","admin","op"};
static String[] ad2 = {"聊天监察","信件监察","论坛监察","家园监察","帮会监察","圈子监察","资料监察","游戏监察","新闻监察","管理员","Opera允许"};
static String quidsCookie = "quids";
%><%
if(!group.isFlag(7))
	return;
%>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../js/jquery.js"></script>
<script>
var fromDiv;
function sendPassword(from,uid){
	if(!confirm('确认要下发密码给该用户?'))
		return;
	fromDiv=from.parentNode;
	fromDiv.innerHTML="<font color=red>下发中…</font>"

	$.get('sendPassword.jsp?id='+uid, function(data) {
  		fromDiv.innerHTML="<font color=green>下发成功</font>"
	});
	
	
	return false;
}
var fromDiv2;
function unforbid(from,uid,type,typename){
	if(!confirm('确认要解除该用户的'+typename+'封禁?'))
		return;
	fromDiv2=from.parentNode;
	fromDiv2.innerHTML="<font color=red>"+typename+"</font>(解除中)"

	$.get('unforbid.jsp?id='+uid+'&type='+type, function(data) {
  		fromDiv2.style.display="none";
	});
	
	
	return false;
}
var fromDiv3;
function kickout(from,uid){
	if(!confirm('确认要踢下线该用户?'))
		return;
	fromDiv3=from.parentNode;
	fromDiv3.innerHTML="<font color=red>处理中</font>"

	$.get('kickout.jsp?id='+uid, function(data) {
  		fromDiv3.innerHTML="<font color=green>已处理</font>"
	});
	
	
	return false;
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<p>查询用户名&nbsp;&nbsp;<a href="index.jsp">返回</a>&nbsp;&nbsp;<a href="addMoney.jsp">给用户加银行存款</a></p>
<p align=center>
<%
String mobile = request.getParameter("mobile");
String ids = request.getParameter("id");
String userName = request.getParameter("nickname");


CookieUtil cu = new CookieUtil(request,response);
String quids = cu.getCookieValue(quidsCookie);
if(quids==null)
	quids = ",";
else
	quids = java.net.URLDecoder.decode(quids);

UserBean user = null;
Vector userIdList = null;
if(mobile!=null && !mobile.equals("")){	
	if(mobile.startsWith("86")){
		mobile = mobile.substring(2);
	}
    String condition = "mobile='" + StringUtil.toSql(mobile) + "'";
    userIdList=service.getUserList(condition);
    if(userIdList.size()==0 && group.isFlag(1)){
    	response.sendRedirect("queryRegister.jsp?mobile="+mobile);
    	return;
    }
}
String dids = request.getParameter("dids");
if(dids!=null){
	if(dids.equals("all"))
		quids=",";
	else
		quids = quids.replace(","+dids+",",",");
	cu.setCookie(quidsCookie,java.net.URLEncoder.encode(quids),90000000);
	if(quids.length()>1)
		response.sendRedirect("queryUserInfo.jsp?id="+quids.substring(1,quids.length()-1));
	else
		response.sendRedirect("queryUserInfo.jsp");
	return;
}
if(ids!=null&&ids.length()!=0){	
	ids=ids.trim();
	if(CheckUtil.isIntList(ids)) {
	
		if(request.getParameter("addId")!=null){
			quids = quids +ids+ ",";
			ids = quids.substring(1,quids.length()-1);
			cu.setCookie(quidsCookie,java.net.URLEncoder.encode(quids),90000000);
			response.sendRedirect("queryUserInfo.jsp?id="+ids);
		}
	
	    String condition = "id in(" + StringUtil.toSql(ids)+")";
	    userIdList=service.getUserList(condition);
    }
}
if(userName!=null && !userName.equals("")){	
    String condition = "nickname like '" + StringUtil.toSql(userName) + "%' limit 50";
    userIdList=service.getUserList(condition);
}
StringBuilder idssb = null;
%>



<% if(userIdList != null){

    %>
    
    <table width="100%" align="center" cellpadding="2">
    	<tr>
    		<td>id</td>
    		<td>手机</td>
    		<td>昵称</td>
    		<td>密码</td>
			<td>家族/帮会</td>
			<td>现金</td>
			<td>存款</td>
			<td>股市</td>
			<td>级</td>
			<td>最后登陆</td>
			<td>登陆次<br/>在线时</td>
			<td></td>
    	</tr>
    	<% idssb = new StringBuilder(userIdList.size()*10);
    	   for(int i = 0;i<userIdList.size();i++){
     user = (UserBean)userIdList.get(i);
     if(i!=0)idssb.append(',');
     idssb.append(user.getId());
UserStatusBean userStatus = UserInfoUtil.getUserStatus(user.getId());
UserSettingBean userSetting = service.getUserSetting("user_id="+user.getId());
net.joycool.wap.bean.stock2.StockAccountBean stock = net.joycool.wap.action.stock2.StockWorld.getStockAccount(user.getId());
int forbidLog = SqlUtil.getIntResult("select id from forbid_log where user_id="+user.getId()+" limit 1");
int userLog = SqlUtil.getIntResult("select id from user_log where user_id="+user.getId()+" limit 1");
    TongBean tong = null;
    jc.family.FamilyHomeBean family = null;
    if(userStatus.getTong()>0&&userStatus.getTong()<20000)
    	tong = TongCacheUtil.getTong(userStatus.getTong());
    else if(userStatus.getTong()>=20000)
    	family=jc.family.FamilyAction.getFmByID(userStatus.getTong());
    	
%>
		<tr>
			<td width="80">
				<%= user.getId() %><%if(quids.contains(","+user.getId())){%>-<a href="queryUserInfo.jsp?dids=<%=user.getId()%>">删</a><%}%><br/>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)<%if(OnlineUtil.isOnline(String.valueOf(user.getId()))){%><br/><div><a href="#" onclick="return kickout(this,'<%= user.getId() %>');">踢下线</a></div><%}%>
			</td>
			<td width="80">
				<%= user.getMobile() %><%if(mobile==null&&user.getMobile().length()>0&&!user.getMobile().equals("fttodeath")){%><br/><a href="queryUserInfo.jsp?mobile=<%= user.getMobile() %>">查询</a><%}%>
				<br/><%=DateUtil.formatDate(user.getCreateDatetime())%>
			</td>
			<td>
				<%= user.getNickName() %><%
if(userLog!=-1){%><br/><a href="userLog.jsp?uid=<%=user.getId()%>"><font color=red>历史>></font></a><%}
				%><br/><%= StringUtil.limitString(StringUtil.noEnter(user.getSelfIntroduction()),20) %>-<a href="../alterUserInfo.jsp?inputUserId=<%=user.getId()%>">修改</a>
			</td>
			<td width="100"><%if(group.isFlag(1)){%>[<%= Encoder.decrypt(user.getPassword()) %>]
				<%if(userSetting!=null&&userSetting.getBankPw().length()>0){%>
				<br/>银[<%=userSetting.getBankPw()%>]
				<%}%><br/><div style="float:left;"><a href="updateUserPassword.jsp?defaultId=<%=user.getId()%>">修改</a>-</div><%}
				%><div style="float:left;"><a href="#" onclick="return sendPassword(this,<%=user.getId()%>)">发送密码</a></div>
			</td>
			<td width="100"><%if(tong!=null){%>
<a href="../tong/search.jsp?tongId=<%= tong.getId()%>"><%= StringUtil.toWml(tong.getTitle())%></a><br/>(<%=tong.getId()%>)<%}%>
<%if(family!=null){%>
<a href="../fm/familyhome.jsp?id=<%= family.getId()%>"><%= StringUtil.toWml(family.getFm_name())%></a><br/>(<%=family.getId()%>)<%}%>
			</td>
			<td width="60"><%=StringUtil.bigNumberFormat(userStatus.getGamePoint())%></td>
			<td width="60"><a href="addMoney.jsp?defaultId=<%=user.getId()%>"><%=StringUtil.bigNumberFormat(BankCacheUtil.getStoreMoney(user.getId()))%></a>
			<%if(group.isFlag(0)){%><br/><a href="moneyLog4.jsp?inputUserId=<%= user.getId()%>">转账记录</a><%}%></td>
			<td width="60"><%if(stock!=null&&stock.getMoney()+stock.getMoneyF()>0){%><a href="../stock2/userStock.jsp?id=<%=user.getId()%>"><%=StringUtil.bigNumberFormat(stock.getMoney()+stock.getMoneyF())%></a><%}else{%>无<%}%></td>
	<td width="20"><%=userStatus.getRank()%></td>
			<td width="100"><%=userStatus.getLastLoginTime()%><br/><% 
	long now = System.currentTimeMillis();
	for(int i2=0;i2<fs.length;i2++){
			
		if(ForbidUtil.isForbid(fs[i2],user.getId())){%><div><a href="#" onclick="return unforbid(this,<%=user.getId()%>,<%=i2%>,'<%=fs3[i2]%>')"><font color=red><%=fs3[i2]%></font></a><%=DateUtil.formatTimeInterval((ForbidUtil.getForbid(fs[i2],user.getId()).getEndTime()-now) )%></div><%}
	}
	for(int i2=0;i2<ad.length;i2++){
			
		if(ForbidUtil.isForbid(ad[i2],user.getId())){%><a href="../adminuser.jsp?group=<%=ad[i2]%>"><font color=green><%=ad2[i2]%></font></a><br/><%}
	}
if(SecurityUtil.checkForbidUserId(user.getId())){%><div><a href="#" onclick="return unforbid(this,<%=user.getId()%>,100,'全站')"><font color=red>全站</font></a></div><%}

if(forbidLog!=-1){%><a href="forbidLog.jsp?uid=<%=user.getId()%>"><font color=red>案底>></font></a><%}

%></td>
			<td width="80"><%=userStatus.getLoginCount()%>
			/<%if(group.isFlag(0)){%><a href="../onlineUser.jsp?userId=<%=user.getId()%>"><%=userStatus.getOnlineTime()%></a><%}else{%><a href="#" title="<%UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(String.valueOf(user.getId()));if(onlineUser!=null){%><%=onlineUser.getIpAddress()%><%}%>"><%=userStatus.getOnlineTime()%><a/><%}%></td>
<td width="150"><a href="info.jsp?id=<%= user.getId()%>">资料</a>-<a href="friend.jsp?userId=<%= user.getId()%>">好友</a>-<a href="userBag.jsp?userId=<%= user.getId()%>">行囊</a>-<a href="userBagPresent.jsp?userId=<%= user.getId()%>">记录</a><br/>
<a href="../searchUserPost.jsp?userId=<%= user.getId()%>">聊天</a>-<a href="../searchUserMessage.jsp?userId=<%= user.getId()%>">信件</a>-<a href="moneyLog2.jsp?userId=<%= user.getId()%>">赠送</a>-<a href="moneyLog.jsp?inputUserId=<%= user.getId()%>">转帐</a><br/>
<a href="teams.jsp?id=<%= user.getId()%>">圈子</a>-<a href="home.jsp?id=<%= user.getId()%>">家园</a>-<a href="game.jsp?id=<%= user.getId()%>">游戏</a>-<a href="forums.jsp?id=<%= user.getId()%>">帖子</a>
</td>
		</tr>
		<%}%>
	</table>
<%}%>


<%if(group.isFlag(0)){%>
	<%if(idssb!=null){%><a href="#" onclick="window.clipboardData.setData('Text', '<%=idssb.toString()%>');return false;">复制ids</a><%}else{%>复制ids<%}%>-
	<%if(quids.length()>1){%><a href="queryUserInfo.jsp?id=<%=quids.substring(1,quids.length()-1)%>">读取cookie</a>-<a href="queryUserInfo.jsp?dids=all">清空cookie</a><%}else{%>读取cookie-清空cookie<%}%>
	<br/>
<%}%>

</p>
<table width="90%" align="center" border="1">
        <form method="get" action="queryUserInfo.jsp">
		<tr>
			<td width="30%">
				用户手机号
			</td>
			<td width="70%">
				<input type=text name="mobile">
				<input type=submit value="提交">
			</td>
		</tr>
		</form>
		<form method="get" action="queryUserInfo.jsp">
		<tr>
			<td width="30%">
				用户ID（数字）
			</td>
			<td width="70%">
				<input type=text name="id">
				<input type=submit value="查询"><%if(group.isFlag(0)){%>-<input type=submit name="addId" value="添加"><%}%>
			</td>
		</tr>
		</form>
		<form method="get" action="queryUserInfo.jsp">
		<tr>
			<td width="30%">
				用户昵称
			</td>
			<td width="70%">
				<input type=text name="nickname">
				<input type=submit value="提交">
			</td>
		</tr>
		</form>
</table>	