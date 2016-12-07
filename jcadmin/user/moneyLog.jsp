<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.impl.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.bank.BankAction" %><%@ page import="net.joycool.wap.bean.bank.MoneyLogBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%!
static String[] moneyType = {"",
"存款",
"取款",
"贷款",
"还贷",
"拍卖",
"股市",
"大富豪",
"六合彩",
"乐币溢出",
"道具抢劫卡",
"离婚手续费",
"管理员",
"踩楼游戏",
"转帐交易",
"大富翁",
"桃花源",
"时时彩",
};
	static UserServiceImpl userService = new UserServiceImpl();
	static BankServiceImpl bankService = new BankServiceImpl();
%><%
int totalCount = 0;
int totalPageCount = 0;
String prefixUrl =null ;
int numberPage = 50;
String fenye = null;
int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if (pageIndex == -1) {
pageIndex = 0;
}
String inputUserId=request.getParameter("inputUserId");
String userId = request.getParameter("userId");
String nickname=request.getParameter("nickname");
List bankLog =null;
UserBean user=null;
String tip=null;
MoneyLogBean moneyLog=null;
if(inputUserId!=null)
{
	user=UserInfoUtil.getUser(StringUtil.toInt(inputUserId));
	//bankLog = BankAction.getBankLogList(StringUtil.toInt(inputUserId));
	//设置分页参数
		prefixUrl = "moneyLog.jsp?inputUserId="+inputUserId;
		totalCount = 10000;//bankService.getMoneyLogCount("user_id = "+ inputUserId);
		totalPageCount = totalCount / numberPage;
		if (totalCount % numberPage != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		// 取得要显示的消息列表
		int start = pageIndex * numberPage;
		int end = numberPage;
		bankLog=bankService.getMoneyLogList("user_id = "+ inputUserId + " order by id desc limit "+start+","+end);
		if(bankLog.size()<end){
			if(bankLog.size()>0)
				totalPageCount = pageIndex+1;
			else
				totalPageCount = pageIndex;
		}
		if(bankLog==null) {
	    tip="此用户没有银行记录!";
	    }
}
if(nickname!=null)
{
	user=userService.getUser("nickname ='"+nickname+"'");
	if(user==null){
		tip="没有查到与此相匹配的用户。";
	}else{
	     //设置分页参数
		prefixUrl = "moneyLog.jsp?inputUserId="+user.getId();
		totalCount = bankService.getMoneyLogCount("user_id = "+ user.getId());
		totalPageCount = totalCount / numberPage;
		if (totalCount % numberPage != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		// 取得要显示的消息列表
		int start = pageIndex * numberPage;
		int end = numberPage;
		bankLog=bankService.getMoneyLogList("user_id = "+ user.getId() + " order by time desc limit "+start+","+end);
		if(bankLog==null) {
			tip="此用户没有银行记录!";
		}
	}
}
%>
<html>
<div align="center">
<H2 align="center">用户银行管理后台</H2>
<%if(user!=null){%>
<p><%=StringUtil.toWml(user.getNickName())%>(<%=user.getId()%>)</p>
<%}%>
<table border="1" align="center" >
<tr>
	<td>
		对方ID
	</td>
	<td align=center>
		转帐
	</td>
	<td align=center>
		当前
	</td>
	<td>
		时间
	</td>
	<td>
		事件
	</td>
</tr>
<%
if(bankLog!=null){
for(int i = 0; i < bankLog.size(); i ++){
%><tr><%
	moneyLog=(MoneyLogBean)bankLog.get(i);
	String thing = moneyType[moneyLog.getType()];
%>
<td><%if(moneyLog.getRUserId()!=0){%><a href="queryUserInfo.jsp?id=<%=moneyLog.getRUserId()%>"><%=moneyLog.getRUserId()%></a><%}%></td>
<td align=right><%=moneyLog.getMoney()/10000%>万</td>
<td align=right><%=moneyLog.getCurrent()/10000%>万</td>
<td><%=moneyLog.getTime()%></td>
<td><%=thing%></td>
</tr>
<%}%>
<%}%>
</table>
<%fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, " ", response);
if(fenye != null && !fenye.equals("")){%><%=fenye%><br/><%}%>
<font color="red"><%=tip!=null?tip:""%></font><br/>
根据ID查找银行记录<br/>
<form name="form1" method="post" action="moneyLog.jsp">
用户ID：<input type="text" name="inputUserId"/><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
<div align="center">
根据昵称查找银行记录<br/>
<form name="form1" method="post" action="moneyLog.jsp">
用户昵称：<input type="text" name="nickname" /><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
</html>