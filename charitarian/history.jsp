<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.Constants,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.charitarian.CharitarianAction" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.framework.OnlineUtil" %><%@ page import="net.joycool.wap.util.LoadResource" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.service.impl.UserServiceImpl" %><%
response.setHeader("Cache-Control","no-cache");
CharitarianAction action = new CharitarianAction(request);
action.history(request);
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List userIdList=(List)request.getAttribute("userIdList");
int totalPageCount1 = ((Integer) request.getAttribute("totalPageCount1")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
List userIdList1=(List)request.getAttribute("userIdList1");
String money=(String)request.getAttribute("money");
String count=(String)request.getAttribute("count");
String count1=(String)request.getAttribute("count1");
String order=(String)request.getAttribute("order");
String order1=(String)request.getAttribute("order1");
if(order==null){
order="online";
}
if(order1==null){
order1="online";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="慈善基金">
<p align="left">
<%=BaseAction.getTop(request, response)%>
捐献历史：大善人，您共捐献过<%=money%>乐币，救济过<%=count%>个新用户。他们是：<br/>
<%if(order.equals("online")){%>
按在线|<a href="/charitarian/history.jsp?order=rank">按当前级别</a><br/>
<%}else{%>
<a href="/charitarian/history.jsp?order=online">按在线</a>|按当前级别<br/>
<%}%>
<%
Integer userId=null;
int j=0;
for(int i=0;i<userIdList.size();i++){
userId=(Integer)userIdList.get(i);
UserBean user =UserInfoUtil.getUser(userId.intValue());
if(user==null) continue;
UserStatusBean userStatus=UserInfoUtil.getUserStatus(userId.intValue());
%>
<%=j+1%>.<a href="/user/ViewUserInfo.do?userId=<%=userId.toString()%>"><%=StringUtil.toWml(user.getNickName())%></a>
<%=user.getAge()%>
<%=user.getGender()==0?"女":"男"%>
<%
String cityName = user.getCityname();
if(cityName==null || cityName.equals(""))
cityName="未知";
%>
<%=cityName%>
<%=userStatus.getRank()%>级
<%-- liuyi 2006-12-20 在线用户状态显示修改 start --%>
(<%=LoadResource.getPostionNameByUserId(userId.intValue())%>)<br/>
<%-- liuyi 2006-12-20 在线用户状态显示修改 end --%>
<%j++;}
String fenye = action.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", "pageIndex" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
被捐献历史：在您是新手时，共收到<%=StringUtil.toInt(count1)* Constants.CHARITARIAN_USER_MONEY%>乐币的捐赠，来自<%=count1%>位慈善家是：<br/>
<%if(order1.equals("online")){%>
按在线|<a href="/charitarian/history.jsp?order1=rank">按当前级别</a><br/>
<%}else{%>
<a href="/charitarian/history.jsp?order1=online">按在线</a>|按当前级别<br/>
<%}%>
<%
int k=0;
for(int i=0;i<userIdList1.size();i++){
userId=(Integer)userIdList1.get(i);
UserBean user =UserInfoUtil.getUser(userId.intValue());
if(user==null) continue;
UserStatusBean userStatus=UserInfoUtil.getUserStatus(userId.intValue());
%>
<%=k+1%>.<a href="/user/ViewUserInfo.do?userId=<%=userId.toString()%>"><%=StringUtil.toWml(user.getNickName())%></a>
<%=user.getAge()%>
<%=user.getGender()==0?"女":"男"%>
<%
String cityName = user.getCityname();
if(cityName==null || cityName.equals(""))
cityName="未知";
%>
<%=cityName%>
<%=userStatus.getRank()%>级
<%-- liuyi 2006-12-20 在线用户状态显示修改 start --%>
(<%=LoadResource.getPostionNameByUserId(userId.intValue())%>)<br/>
<%-- liuyi 2006-12-20 在线用户状态显示修改 end --%>
<%k++;}
String fenye1 = action.shuzifenye(totalPageCount1, pageIndex1, prefixUrl1, true, "|", "pageIndex1" , response);
if(!"".equals(fenye1)){%><%=fenye1%><br/><%}%>
继续捐献?
<input name="count"  maxlength="11" value="1"/>份
<anchor title="确定">确定
<go href="result.jsp" method="post">
<postfield name="count" value="$count"/>
</go>
</anchor><br/>
(注:一份<%= Constants.CHARITARIAN_USER_MONEY %>乐币,一次最少捐助1份,最多捐助20万份)<br/>
<a href="/charitarian/index.jsp">慈善基金首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>