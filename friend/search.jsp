<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.friend.FriendBean" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.framework.OnlineUtil" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
action.searchResult(request);
String result=(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("nullUser")){
out.clearBuffer();
response.sendRedirect("inputRegisterInfo.jsp");
return;
}else if(result.equals("failure")){
action.getFriendCity(request,"search");
PagingBean paging = (PagingBean) request.getAttribute("paging");
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
List genderList=(List)request.getAttribute("genderList");
String gender=(String)request.getAttribute("gender");
%>
<card title="搜索结果">
<p align="left">
很遗憾,没有符合您要求的对象,别灰心哦!
<a href="advSearch.jsp">修改要求再来一次</a><br/>
<%if(gender.equals("0")){%>
找女性朋友|<a href="search.jsp?gender=1">找男性朋友</a><br/>
<%}else{%>
<a href="search.jsp?gender=0">找女性朋友</a>|找男性朋友<br/>
<%}
FriendBean friend=null;
int j=0;
for(int i=0;i<genderList.size();i++){
friend=(FriendBean)genderList.get(i);
if(friend.getUserId()==loginUser.getId())
continue;
%>
<%=j+1%>.<a href="friendInfo.jsp?userId=<%=friend.getUserId()%>"><%=StringUtil.toWml(UserInfoUtil.getUser(friend.getUserId()).getNickName())%></a>(<%=friend.getAge()%>
<%=friend.getGender()==0?"女":"男"%>
<%-- liuyi 2006-12-20 在线用户状态显示修改 start --%>
(<%=LoadResource.getPostionNameByUserId(friend.getUserId())%>)<br/>
<%-- liuyi 2006-12-20 在线用户状态显示修改 end --%>
<%j++;}%>
<%=paging.shuzifenye(prefixUrl1, true, "|", response)%>
找同城:<a href="search.jsp?city=1&amp;gender=0">MM</a>|<a href="search.jsp?city=1&amp;gender=1">GG</a><br/>
找同星座:<a href="search.jsp?constellation=1&amp;gender=0">MM</a>|<a href="search.jsp?constellation=1&amp;gender=1">GG</a><br/>
找有照片的:<a href="search.jsp?attach=1&amp;gender=0">MM</a>|<a href="search.jsp?attach=1&amp;gender=1">GG</a><br/>
找交友目的：<select name="aim" value="0">
    <option value="0">恋人</option><option value="1">知己</option><option value="2">玩伴</option><option value="3">解闷</option><option value="4">其他</option>
</select>
<anchor title="确定">提交
<go href="search.jsp" method="post">
<postfield name="aim" value="$aim"/>
</go>
</anchor><br/>
<a href="advSearch.jsp">高级交友搜索</a><br/>
<a href="friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
List friendList =(List)request.getAttribute("friendList");
int count = friendList.size();
PagingBean paging = (PagingBean) request.getAttribute("paging");
String prefixUrl = (String) request.getAttribute("prefixUrl");
%>
<card title="搜索结果">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(count>0){
%>
恭喜您找到<%=paging.getTotalCount()%>位理想对象!<br/>
<%
FriendBean friend = null;
UserBean user= null;
int j=1;
for(int i=0;i<count;i++){
    friend =(FriendBean)friendList.get(i);
    if(loginUser.getId()==friend.getUserId())
    continue;
    user=UserInfoUtil.getUser(friend.getUserId());
%>
<%=j%>.<a href="friendInfo.jsp?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>
<%-- liuyi 2006-12-20 在线用户状态显示修改 start --%>
(<%=LoadResource.getPostionNameByUserId(user.getId())%>)<br/>
<%-- liuyi 2006-12-20 在线用户状态显示修改 end --%>
<%j++;}
%>
<%=paging.shuzifenye(prefixUrl, true, "|", response)%>

找同城:<a href="search.jsp?city=1&amp;gender=0">MM</a>|<a href="search.jsp?city=1&amp;gender=1">GG</a><br/>
找同星座:<a href="search.jsp?constellation=1&amp;gender=0">MM</a>|<a href="search.jsp?constellation=1&amp;gender=1">GG</a><br/>
找有照片的:<a href="search.jsp?attach=1&amp;gender=0">MM</a>|<a href="search.jsp?attach=1&amp;gender=1">GG</a><br/>
找交友目的：<select name="aim" value="0">
    <option value="0">恋人</option><option value="1">知己</option><option value="2">玩伴</option><option value="3">夜情</option><option value="4">其他</option>
</select>
<anchor title="确定">提交
<go href="search.jsp" method="post">
<postfield name="aim" value="$aim"/>
</go>
</anchor><br/>
<a href="advSearch.jsp">高级交友搜索</a><br/>
<%}
else{
action.getFriendCity(request,"search");
int totalPageCount1 = ((Integer) request.getAttribute("totalPageCount1")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
List genderList=(List)request.getAttribute("genderList");
String gender=(String)request.getAttribute("gender");
%>
很遗憾,没有符合您要求的对象,别灰心哦!
<a href="advSearch.jsp">修改要求再来一次</a><br/>
<%if(gender.equals("0")){%>
找女性朋友|<a href="search.jsp?gender=1">找男性朋友</a><br/>
<%}else{%>
<a href="search.jsp?gender=0">找女性朋友</a>|找男性朋友<br/>
<%}
FriendBean friend=null;
int j=0;
for(int i=0;i<genderList.size();i++){
friend=(FriendBean)genderList.get(i);
if(friend.getUserId()==loginUser.getId())
continue;
%>
<%=j+1%>.<a href="friendInfo.jsp?userId=<%=friend.getUserId()%>"><%=StringUtil.toWml(UserInfoUtil.getUser(friend.getUserId()).getNickName())%></a>(<%=friend.getAge()%>
<%=friend.getGender()==0?"女":"男"%>
<%-- liuyi 2006-12-20 在线用户状态显示修改 start --%>
(<%=LoadResource.getPostionNameByUserId(friend.getUserId())%>)<br/>
<%-- liuyi 2006-12-20 在线用户状态显示修改 end --%>
<%j++;}%>
<%=paging.shuzifenye(prefixUrl1, true, "|", response)%>
找同城:<a href="search.jsp?city=ture&amp;gender=0">MM</a>|<a href="search.jsp?city=ture&amp;gender=1">GG</a><br/>
找同星座:<a href="search.jsp?constellation=ture&amp;gender=0">MM</a>|<a href="search.jsp?constellation=ture&amp;gender=1">GG</a><br/>
找有照片的:<a href="search.jsp?attach=ture&amp;gender=0">MM</a>|<a href="search.jsp?attach=ture&amp;gender=1">GG</a><br/>
找交友目的：<select name="aim" value="0">
    <option value="0">恋人</option>
    <option value="1">知己</option>
    <option value="2">玩伴</option>
    <option value="3">夜情</option>
    <option value="4">其他</option>
	</select>
<anchor title="确定">提交
<go href="search.jsp" method="post">
<postfield name="aim" value="$aim"/>
</go>
</anchor><br/>
<a href="advSearch.jsp">高级交友搜索</a><br/>
<%}
%>
<a href="friendCenter.jsp">返回交友中心</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>