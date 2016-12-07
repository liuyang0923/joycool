<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.text.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean" %><%@ page import="net.joycool.wap.bean.home.HomePhotoBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.home.HomeUserBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.service.infc.IHomeService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.home.HomeNeighborBean" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.framework.OnlineUtil" %><%@ page import="net.joycool.wap.bean.home.HomeTypeBean" %><%@ page import="net.joycool.wap.bean.home.HomeBean" %><%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.text.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean" %><%@ page import="net.joycool.wap.bean.home.HomePhotoBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.home.HomeUserBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.service.infc.IHomeService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.home.HomeNeighborBean" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.framework.OnlineUtil" %><%@ page import="net.joycool.wap.cache.util.HomeCacheUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%!
static IHomeService homeService =ServiceFactory.createHomeService();%><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
FriendAction faction = new FriendAction(request);
action.home(request);
String result=(String) request.getAttribute("result");
if("failure".equals(result)){
response.sendRedirect("viewAllHome.jsp");
return;
}
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理 
if("failure".equals(result)){
%>
<card title="乐酷家园">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<%int userId=action.getAttributeInt("userId");
if(userId==loginUser.getId()){
%>
<a href="inputRegisterInfo.jsp">（隆重）开通我的个人家园，让朋友们来做客！</a><br/> 
<%}%>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
}else if("info".equals(result)){
String url = ("home.jsp");
%>
<card title="乐酷家园" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后返回家园首页)<br/>
<a href="home.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if("addOk".equals(result)){
String url = ("home.jsp");
%>
<card title="乐酷家园" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后返回家园首页)<br/>
<a href="home.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if("delOk".equals(result)){
String url = ("home.jsp");
%>
<card title="乐酷家园" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(3秒后返回家园首页)<br/>
<a href="home.jsp">返回我的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if("success".equals(result)){
HomeUserBean homeUser=(HomeUserBean)request.getAttribute("homeUser");
//Vector userImageList=(Vector)request.getAttribute("userImageList");
HomeTypeBean homeType=(HomeTypeBean)request.getAttribute("homeType");
Vector homeVec=(Vector)request.getAttribute("homeVec");
int homeId=StringUtil.toInt((String)request.getAttribute("homeId"));
if(homeUser.getUserId()==loginUser.getId()){
%>
<card title="我的家">
<p align="left">
<%=BaseAction.getTop(request, response)%>
我的家<br/>
<%if(homeType==null){
HomeTypeBean homeTypes=(HomeTypeBean)request.getAttribute("homeTypes");
if(homeTypes!=null){
String[] roomIds=homeTypes.getRoomIds().split("_");
if(homeId<0 && roomIds.length>1)
{homeId=2;}
if(homeId<0&& roomIds.length==1)
{homeId=1;}
String userImageUrl = action.getImageUrl(Constants.HOME_IMAGE_SMALL_TYPE,loginUser.getId(),homeId);
HomeBean homeBean=null;
if(homeId<roomIds.length)
 homeBean=(HomeBean)homeVec.get(homeId-1);%>
<a href="seeBigImage.jsp?type=1&amp;homeId=<%=homeId%>">
<img src="<%=(userImageUrl)%>" alt="家的图片"/><br/>
</a>
<%
if(roomIds.length>1)
{
for(int i=2;i>0;i--){
 homeBean=(HomeBean)homeVec.get(i-1);
if(homeBean!=null){
if(homeBean.getId()!=homeId){%>
<a href="house.jsp?userId=<%=homeUser.getUserId()%>&amp;in=1&amp;homeId=<%=homeBean.getId()%>"><%=homeBean.getName()%></a> 
<%}else{%>
<%=homeBean.getName()%>
<%}}}

for(int i=2;i<roomIds.length;i++){
 homeBean=(HomeBean)homeVec.get(i);
if(homeBean!=null){
if(homeBean.getId()!=homeId){%>
<a href="house.jsp?userId=<%=homeUser.getUserId()%>&amp;in=1&amp;homeId=<%=homeBean.getId()%>"><%=homeBean.getName()%></a> 
<%}else{%>
<%=homeBean.getName()%>
<%}}}
}
else
{
homeBean=(HomeBean)homeVec.get(0);
if(homeBean!=null){%>
<%=homeBean.getName()%>
<%}
}
}%><br/>
<a href="editHome.jsp">档案管理</a>|<a href="homeManage.jsp">管理家园</a><br/>
<a href="/mycart.jsp">我的收藏</a>|<a href="/friendadver/friendAdverIndex.jsp">交友广告</a><br/>
<%
UserStatusBean status=(UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
if(status.getMark()==2 &&faction.isMarriage(loginUser.getId())!=0){%>
<a href="/friend/review.jsp?marriageId=<%=faction.isMarriage(loginUser.getId())%>">婚礼录像</a>|<br/>
<%}%><a href="/user/userBag.jsp">我的行囊</a>|<a href="homeNeighbor.jsp">我的邻居</a><br/>
<%int roomId=action.hasChatRoom(loginUser.getId()); if(roomId!=-1){%><a href="/chat/hall.jsp?roomId=<%=roomId%>">我的聊天室</a><br/><%}%>
<%}%>
<%if(homeType!=null){%>
<a href="house.jsp?userId=<%=homeUser.getUserId()%>&amp;in=1"><img src="/img/home/house/<%=homeUser.getTypeId()%>.gif" alt="家的图片"/></a><br/>

<a href="editHome.jsp">档案管理</a>|<a href="homeManage.jsp">管理家园</a><br/>
<a href="/mycart.jsp">我的收藏</a>|<a href="/user/userBag.jsp">我的行囊</a><br/>
<%
UserStatusBean status=(UserStatusBean)UserInfoUtil.getUserStatus(loginUser.getId());
if(status.getMark()==2 &&faction.isMarriage(loginUser.getId())!=0){%>
<a href="/friend/review.jsp?marriageId=<%=faction.isMarriage(loginUser.getId())%>">婚礼录像</a>|<br/>
<%}%><a href="/friendadver/friendAdverIndex.jsp">交友广告</a><br/><%int roomId=action.hasChatRoom(loginUser.getId()); if(roomId!=-1){%><a href="/chat/hall.jsp?roomId=<%=roomId%>">我的聊天室</a><br/><%}%>
<%}%>
<a href="home.jsp">返回我的家园</a>|<a href="home2.jsp?userId=<%=loginUser.getId()%>">自我预览</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
//设置添加好友后，自动返回页面
session.setAttribute("pagebeforeclick",PageUtil.getCurrentPageURL(request));
UserBean user=(UserBean)request.getAttribute("user");
%>
<card title="<%=StringUtil.toWml(user.getNickName())%>的家">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(user.getNickName())%>的家<br/>
<%if(homeType==null){
HomeTypeBean homeTypes=(HomeTypeBean)request.getAttribute("homeTypes");
if(homeTypes!=null){
String[] roomIds=homeTypes.getRoomIds().split("_");
if(homeId<0 && roomIds.length>1)
{homeId=2;}
if(homeId<0&& roomIds.length==1)
{homeId=1;}
String userImageUrl = action.getImageUrl(Constants.HOME_IMAGE_SMALL_TYPE,user.getId(),homeId);
HomeBean homeBean=null;
if(homeId<roomIds.length)
 homeBean=(HomeBean)homeVec.get(homeId-1);%>
<a href="<%=("seeBigImage.jsp?userId="+user.getId()+"&amp;homeId="+homeId+"&amp;unique=11652125596877")%>">
<img src="<%=(userImageUrl)%>" alt="家的图片"/><br/>
</a>
<%
if(roomIds.length>1)
{
for(int i=2;i>0;i--){
 homeBean=(HomeBean)homeVec.get(i-1);
if(homeBean!=null){
if(homeBean.getId()!=homeId){%>
<a href="house.jsp?userId=<%=homeUser.getUserId()%>&amp;in=1&amp;homeId=<%=homeBean.getId()%>"><%=homeBean.getName()%></a> 
<%}else{%>
<%=homeBean.getName()%>
<%}}}
for(int i=2;i<roomIds.length;i++){
 homeBean=(HomeBean)homeVec.get(i);
if(homeBean!=null){
if(homeBean.getId()!=homeId){%>
<a href="house.jsp?userId=<%=homeUser.getUserId()%>&amp;in=1&amp;homeId=<%=homeBean.getId()%>"><%=homeBean.getName()%></a> 
<%}else{%>
<%=homeBean.getName()%>
<%}}}
}
else
{
homeBean=(HomeBean)homeVec.get(0);
if(homeBean!=null){%>
<%=homeBean.getName()%>
<%}
}
}%><br/>
<% boolean isOnline=OnlineUtil.isOnline(user.getId()+""); %>
<a href="viewHomeUser.jsp?userId=<%=homeUser.getUserId()%>">查<%=user.getGender() == 1? "他" : "她"%>档案</a>
<%UserStatusBean status=(UserStatusBean)UserInfoUtil.getUserStatus(user.getId());if(status.getMark()==2&&faction.isMarriage(user.getId())!=0){%>
<a href="/friend/review.jsp?marriageId=<%=faction.isMarriage(user.getId())%>"><%=user.getGender() == 1? "他" : "她"%>的婚礼录像</a><%}%><br/>

<a href="/chat/post.jsp?toUserId=<%=user.getId()%>"><%if(isOnline){%>与<%}else{%>给<%}%><%=user.getGender() == 1? "他" : "她"%><%if(isOnline){%>聊天<%}else{%>留言<%}%></a>

<a href="/user/sendMessage.jsp?toUserId=<%=user.getId()%>">给<%=user.getGender() == 1? "他" : "她"%>写信</a><br/>
<%}%>

<%if(homeType!=null){%>
<a href="home.jsp?userId=<%=homeUser.getUserId()%>&amp;in=1"><img src="/img/home/house/<%=homeUser.getTypeId()%>.gif" alt="家的图片"/></a><br/>
<% boolean isOnline=OnlineUtil.isOnline(user.getId()+""); %>
<a href="viewHomeUser.jsp?userId=<%=homeUser.getUserId()%>">查<%=user.getGender() == 1? "他" : "她"%>档案</a>
<%UserStatusBean status=(UserStatusBean)UserInfoUtil.getUserStatus(user.getId());if(status.getMark()==2&&faction.isMarriage(user.getId())!=0){%>
<a href="/friend/review.jsp?marriageId=<%=faction.isMarriage(user.getId())%>"><%=user.getGender() == 1? "他" : "她"%>的婚礼录像</a><%}%><br/>
<a href="/chat/post.jsp?toUserId=<%=user.getId()%>"><%if(isOnline){%>与<%}else{%>给<%}%><%=user.getGender() == 1? "他" : "她"%><%if(isOnline){%>聊天<%}else{%>留言<%}%></a>
<a href="/user/sendMessage.jsp?toUserId=<%=user.getId()%>">给<%=user.getGender() == 1? "他" : "她"%>写信</a><br/>
<%}%>
<a href="home2.jsp?userId=<%=user.getId()%>">返回<%=StringUtil.toWml(user.getNickName())%>的家园</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}}%>
</wml>
