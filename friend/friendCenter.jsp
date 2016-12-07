<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.friend.FriendBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.friendadver.FriendAdverBean" %><%@ page import="net.joycool.wap.framework.OnlineUtil" %><%@ page import="net.joycool.wap.util.LoadResource" %><%@ page import="net.joycool.wap.spec.bottle.BottleService,net.joycool.wap.util.DateUtil" %><%
response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
action.friendCenter(request);
String result=(String) request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("failure".equals(result)){
out.clearBuffer();
response.sendRedirect("inputRegisterInfo.jsp");
return;
}else if("success".equals(result)){
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector friendAdverList=(Vector)request.getAttribute("friendAdverList");
int totalPageCount1 = ((Integer) request.getAttribute("totalPageCount1")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
List genderList=(List)request.getAttribute("genderList");
Integer genderI=(Integer)request.getAttribute("gender");
int gender = genderI==null?0:genderI.intValue();
%>
<card title="乐酷交友中心">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="/Column.do?columnId=10438&amp;jaLineId=6923">净化网络环境,拒绝不良信息</a><br/>
<img src="<%=("/img/friend/center.gif" )%>" alt="交友中心图片"/><br/>
<a href="/friend/marriage.jsp">结婚礼堂</a>|
<a href="/friendadver/postAdver.jsp">我要交友</a><br/>
<%
if(loginUser != null){
int uid=loginUser.getId();
  BottleService service=new BottleService();
  int myBottleCount=service.getMyBottleCount(uid);
%>
我共投放了<%=myBottleCount %>个<a href="/beacon/bo/index.jsp">漂流瓶</a><br/>
<%
	if ( myBottleCount !=0 ){
		%>最后一个漂流瓶被浏览于<%=DateUtil.sformatTime(service.getLastBottlePickupTime(uid)) %><br/><%
	}
}
%>!!<a href="match/index.jsp">靓女选拔赛火热进行中</a><br/>
+速配交友+ <br/>
<a href="/friend/seeIntroduction.jsp">随便看看有没合适的</a><br/>
<a href="/mentalpic/index.jsp">图片心理测试</a>|<a href="/mental/index.jsp">交友测试</a><br/>
<a href="/friend/seeYoungLing.jsp">在线新人列表</a><br/>
找同城:<a href="/friend/search.jsp?city=ture&amp;gender=0">MM</a>|<a href="/friend/search.jsp?city=ture&amp;gender=1">GG</a><br/>
找同星座:<a href="/friend/search.jsp?constellation=ture&amp;gender=0">MM</a>|<a href="/friend/search.jsp?constellation=ture&amp;gender=1">GG</a><br/>
<a href="/friend/advSearch.jsp">高级交友搜索</a><br/>
+<a href="/friendadver/friendAdverIndex.jsp">交友广告</a>(<%if(gender==0){%>
女|<a href="/friend/friendCenter.jsp?gender=1">男</a>
<%}else{%>
<a href="/friend/friendCenter.jsp?gender=0">女</a>|男
<%}%>)+<br/>
<%
FriendAdverBean friendAdver=null;
boolean hasAttach=false;
for(int i=0;i<friendAdverList.size();i++){
friendAdver=(FriendAdverBean)friendAdverList.get(i);
	hasAttach = false;
	if(friendAdver.getAttachment() != null && !friendAdver.getAttachment().equals("")){
		hasAttach = true;
	}
	//liuyi 2006-11-28 程序优化 start
	int famCount= 0;
	String key = "friend_adver_id="+friendAdver.getId();
	Integer count = (Integer)OsCacheUtil.get(key, OsCacheUtil.FRIEND_ADV_COUNT_GROUP, OsCacheUtil.FRIEND_ADV_COUNT_FLUSH_PERIOD);
	if(count==null){
		count = new Integer(action.getFriendAdverMessageService().getFriendAdverMessageCount("friend_adver_id="+friendAdver.getId()));
		OsCacheUtil.put(key, count, OsCacheUtil.FRIEND_ADV_COUNT_GROUP);
	}
	famCount = count.intValue();
	//liuyi 2006-11-28 程序优化 end
%>
<%=i+1%>.<a href="/friendadver/friendAdverMessage.jsp?id=<%=friendAdver.getId()%>"><%if(hasAttach){%>[图]<%}%><%=StringUtil.toWml(friendAdver.getTitle())%>(点击<%=friendAdver.getHits()%>|回复<%=famCount%>)</a><br/>
<%}
String fenye = action.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", "pageIndex" , response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
<a href="/friendadver/friendAdverIndex.jsp">查看更多交友广告</a><br/>
<a href="/home/newDiaryTop.jsp?orderBy=mark">网友日记</a>|<a href="/home/viewAllHome.jsp">优秀家园</a><br/>
<img src="/rep/home/myalbum/1187196824336.jpg" alt="网友照片"/><img src="/rep/home/myalbum/1167610600210.jpg" alt="网友照片"/><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>