<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.*" %><%@ page import="net.joycool.wap.service.factory.*" %><%@ page import="net.joycool.wap.cache.util.UserBagCacheUtil" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.service.impl.DummyServiceImpl" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.service.impl.UserbagItemServiceImpl" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.item.UserItemLogBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%!
static String typeNames[] = {"赠送","交易","抢劫","礼品","获得","使用","丢弃","拍卖","","","","",""};
static String typeColors[] = {"blue","blue","black","black","red","black","gray","green","","","","",""};
static UserbagItemServiceImpl service = new UserbagItemServiceImpl();
%><%
int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if(pageIndex<0)
pageIndex=0;
int userId = StringUtil.toInt(request.getParameter("userId"));
String nickname=request.getParameter("nickname");
IUserService userService = ServiceFactory.createUserService();
List userBagItem =null;
UserBean user=null;
String tip=null;
int NUMBER_PER_PAGE = 20;
int totalCount = 0;
int totalPageCount = 0;
String prefixUrl = "userBagPresent.jsp?userId="+userId;
String fenye = null;
UserItemLogBean userItem=null;
DummyServiceImpl dummyImpl = new DummyServiceImpl();
DummyProductBean bean =null;

if(nickname!=null)
{
	user=userService.getUser("nickname ='"+nickname+"'");
	if(user==null){
		tip="没有查到与此相匹配的用户。";
	}else
	{
	String condition = "user_id="+user.getId() + " order by time desc " + " limit " + pageIndex
				* NUMBER_PER_PAGE + ", " + NUMBER_PER_PAGE;
	userBagItem = service.getUserBagItemList(condition);
	if(userBagItem==null)
	{tip="没有查到与此相匹配的用户。";}
	prefixUrl = "userBagPresent.jsp?userId="+user.getId();
	totalCount = SqlUtil.getIntResult("select count(id) from user_item_log where user_id="+user.getId());
	}
}

if(userId>0){
user=UserInfoUtil.getUser(userId);
String condition = "user_id="+userId + " order by time desc " + " limit " + pageIndex
				* NUMBER_PER_PAGE + ", " + NUMBER_PER_PAGE;
	     userBagItem = service.getUserBagItemList(condition);
	     totalCount = SqlUtil.getIntResult("select count(id) from user_item_log where user_id="+userId);
		if(userBagItem==null) {
			tip="此用户没有赠送记录!";
		}
}
/*totalPageCount = totalCount / NUMBER_PER_PAGE;
	if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}*/
totalPageCount = 1000;
if(user!=null){
if(userBagItem.size()<NUMBER_PER_PAGE){
	if(userBagItem.size()==0)
		totalPageCount=pageIndex;
	else
		totalPageCount=pageIndex+1;
}}
%>
<html>
<div align="center">
<H2 align="center">用户赠送道具管理后台</H2>
<a href="/jcadmin/user/toUserBagPresent.jsp?userId=<%=userId%>">根据接收者查询</a>
<%if(user!=null){%>
<p><%=StringUtil.toWml(user.getNickName())%>(<%=user.getId()%>)</p>
<%}%>
<table border="1" align="center" >
<tr>
	<td colspan=2>
		接收方
	</td>
	<td>
		道具名称
	</td>
	<td>
		数量
	</td>
	<td>
		物品id
	</td>
	<td>
		时间
	</td>
</tr>
<%
if(userBagItem!=null){
for(int i = 0; i < userBagItem.size(); i ++){
%><tr><%
	userItem=(UserItemLogBean)userBagItem.get(i);
	UserBean user2=UserInfoUtil.getUser(userItem.getToUserId());
	int itemId = userItem.getUserBagId();
	if(itemId == 0||itemId > userItem.getItemId())
		itemId = userItem.getItemId();
	bean = dummyImpl.getDummyProducts(itemId);
%>
<td><%if(userItem.getToUserId()!=0){%><a href="queryUserInfo.jsp?id=<%=userItem.getToUserId()%>"><%=userItem.getToUserId()%></a><%}%></td>
<td><%if(user2!=null){%><%=StringUtil.toWml(user2.getNickName())%><%}%></td>
<td><%=bean.getName()%></td>
<td><%=userItem.getStack()%></td>
<td><a href="userBag2.jsp?id=<%=userItem.getUserBagId()%>"><%=userItem.getUserBagId()%></a></td>
<td><%=userItem.getTime().substring(0,19)%></td>
<td width=50 align=center><font color="<%=typeColors[userItem.getType()]%>"><%=typeNames[userItem.getType()]%></font></td>
</tr>
<%}%>
<%}%>
</table>
<%fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, " ", response);
if(fenye != null && !fenye.equals("")){%><%=fenye%><br/><%}%>
<font color="red"><%=tip!=null?tip:""%></font><br/>
根据ID查找行囊赠送记录<br/>
<form name="form1" method="get" action="userBagPresent.jsp">
用户ID：<input type="text" name="userId"/><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
</html>