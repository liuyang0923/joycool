<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><jsp:directive.page import="net.joycool.wap.action.user.UserBagAction"/><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
UserBean loginUser = action.getLoginUser();
String un = loginUser.getUserName();
int roomId=action.getParameterInt("roomId");
int toUserId=action.getParameterInt("toUserId");

//封禁
int count=0;
String user=null;
Vector postList = ContentList.postList;
if(postList != null){
	count=postList.size();
	for(int i=0;i<count;i++){
		user=(String)postList.get(i);
	    if(un.equals(user)){
		//response.sendRedirect(("hall.jsp?roomId="+roomId));
		BaseAction.sendRedirect("/chat/hall.jsp?roomId="+roomId, response);
		return;
		}
	}
}
//macq_2006-12-15_判断用户是否被别人使用臭鸡蛋,不能发言_start
if(UserBagAction.stopSpeakMap.containsKey(new Integer(loginUser.getId()))){
		//response.sendRedirect(("hall.jsp?roomId="+roomId));
		BaseAction.sendRedirect("/chat/hall.jsp?roomId="+roomId, response);
		return;
}
//macq_2006-12-15_判断用户是否被别人使用臭鸡蛋,不能发言_end
if(un.equals("vqwertyuio") || un.equals("SM") || un.equals("v678905")){
	return;
}
IUserService service = ServiceFactory.createUserService();
action.pmSpace(request);
String enterMode = (String)request.getAttribute("enterMode");
String result = (String)request.getAttribute("result");
String returnUrl = StringUtil.convertNull((String) session.getAttribute(Constants.LAST_MODULE_URL)); 
if(returnUrl==null){
	returnUrl = BaseAction.INDEX_URL;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if(!"get".equals(enterMode) && "success".equals(result)){
String url="/chat/pmSpace.jsp?toUserId="+toUserId;
%>
<card title="私聊空间" ontimer="<%=response.encodeURL(url)%>">
<timer value="10"/>
<%
} else {
%>
<card title="私聊空间">
<%
}
%>
<!--<p align="center">发言</p>-->
<p align="left">
<%--<%=BaseAction.getTop(request, response)%>--%>
<%
//表单
if("get".equals(enterMode)){	
	String target = (String)request.getAttribute("target");
	//有接收者
	if("notNull".equals(target)){
	    UserBean toUser = (UserBean) request.getAttribute("toUser");
		if(toUser == null){
%>
对方已下线。<br/>
<%
		}
        else {
        	//liuyi 2006-09-07 start 修改用户在线状态判断
            UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(""+toUser.getId());
        	//liuyi 2006-09-07 end
%>
<%-- liuyi 2006-12-20 在线用户状态显示修改 start --%>
<%if(toUser.getUs2()!=null){%><%=toUser.getUs2().getHatShow()%><%}%><%=((toUser.getNickName() == null || toUser.getNickName().equals("v") || toUser.getNickName().replace(" ", "").equals("")) ? toUser.getUserName() : StringUtil.toWml(toUser.getNickName()))%><% if(onlineUser!=null){%>(<%=LoadResource.getPostionById(onlineUser.getId() + "", onlineUser.getPositionId()).getPositionName()%>)<%}else{%>(离线)<%}%><br/>
<%-- liuyi 2006-12-20 在线用户状态显示修改 end --%>
<input type="text" name="content" value="你好啊"/><br/>
<anchor title="post">发送私聊信息
  <go href="pmSpace.jsp?roomId=<%=roomId%>&amp;toUserId=<%=toUserId%>" method="post">
    <postfield name="to" value="<%=toUser.getUserName()%>"/>
    <postfield name="content" value="$(content)"/>
	<postfield name="isPrivate" value="1"/>
  </go>
</anchor><br/>
<%--<a href="../user/sendAction.jsp?roomId=<%=roomId%>&amp;toUserId=<%=toUser.getId()%>" title="进入">对<%=toUser.getGender() == 1? "他" : "她"%>动作</a>|--%><a href="/chat/moreFunction.jsp?toUserId=<%=toUser.getId()%>">更多互动功能</a><br/>
<%=PositionUtil.getLastModuleUrl(request, response)%><br/><%action.pmSpaceViewMessage(request,response,"pmSpace.jsp");
Vector vecMessages=(Vector)request.getAttribute("messages");
String pagination=(String)request.getAttribute("pagination");
JCRoomContentBean content=null;
for(int i = 0; i < vecMessages.size(); i ++){
	content = (JCRoomContentBean)vecMessages.get(i);%>
<%=(i + 1)%>.<%=action.getPrivateMessageSpaceDisplay(content, request, response)%><br/>
<%}%>
<%if(pagination!=null && !pagination.equals("")){%><%=pagination%><br/><%}%>
<%}
LinkedList linkManList = (LinkedList) request.getAttribute("linkManList");
if(linkManList.size()>0){%>
<a href="/chat/linkMan.jsp?roomId=<%=roomId%>">最近联系人</a>：
<%
for(int i = 0; i < linkManList.size(); i ++){
	if(i>3)break;
	int userId = ((Integer)linkManList.get(i)).intValue();
	UserBean linkManUser=UserInfoUtil.getUser(userId);
	if(linkManUser==null)continue;%>
	<a href="/chat/pmSpace.jsp?roomId=<%=roomId %>&amp;toUserId=<%=linkManUser.getId()%>"><%=StringUtil.toWml(linkManUser.getShortNickName())%></a>&nbsp;		
<%}%><br/><%}%>
==与其他人的私聊信息==<br/>
<%
action.pmViewMessage(request,response,"pmSpace.jsp",5);
Vector vecPMMessages=(Vector)request.getAttribute("pmMessages");
String pmPagination=(String)request.getAttribute("pmPagination");
for(int i = 0; i < vecPMMessages.size(); i ++){
	JCRoomContentBean content = (JCRoomContentBean)vecPMMessages.get(i);
%>
<%=(i + 1)%>.<%=action.getPrivateMessageSpaceDisplay(content, request, response)%><br/>
<%}%>
<%if(pmPagination!=null||pmPagination.equals("")){%><%=pmPagination%><br/><%}%>
<%//没有接收者
}else {
LinkedList linkManList = (LinkedList) request.getAttribute("linkManList");
if(linkManList.size()>0){%>
<a href="/chat/linkMan.jsp?roomId=<%=roomId%>">最近联系人</a>：
<%
for(int i = 0; i < linkManList.size(); i ++){
	if(i>3)break;
	int userId = ((Integer)linkManList.get(i)).intValue();
	UserBean linkManUser=UserInfoUtil.getUser(userId);
	if(linkManUser==null)continue;%>
	<a href="/chat/pmSpace.jsp?roomId=<%=roomId %>&amp;toUserId=<%=linkManUser.getId()%>"><%=StringUtil.toWml(linkManUser.getShortNickName())%></a>&nbsp;		
<%}%><br/><%}%>
==您的私聊信息==<br/>
<%
action.noUserPMViewMessage(request,response,"pmSpace.jsp",8);
Vector vecPMMessages=(Vector)request.getAttribute("pmMessages");
String pmPagination=(String)request.getAttribute("pmPagination");
for(int i = 0; i < vecPMMessages.size(); i ++){
	JCRoomContentBean content = (JCRoomContentBean)vecPMMessages.get(i);
%>
<%=(i + 1)%>.<%=action.getPrivateMessageSpaceDisplay(content, request, response)%><br/>
<%}%>
<%if(pmPagination!=null && !pmPagination.equals("")){%><%=pmPagination%><br/><%}%>
<%=PositionUtil.getLastModuleUrl(request, response)%><br/><%}
%><%=BaseAction.getBottom(request, response)%><%
}
//留言结果
else {	
	String tip = (String)request.getAttribute("tip");
	//失败
	if("failure".equals(result)){
%>
<%=tip%><br/>
<anchor title="back"><prev/>返回发言</anchor><br/>
<%
	} else {
UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(toUserId);
//if(onlineUser!=null){%>
发言成功！<br/>
<%
//}
//else if(onlineUser==null){%>
<%--!发言成功！<br/>--%>
<%//}
String isPrivate=request.getParameter("isPrivate");
String privateNotice=(String)request.getAttribute("privateNotice"); if(isPrivate==null&&privateNotice!=null){%><%=privateNotice%><br/><%}
if(onlineUser!=null){%>
<%-- liuyi 2006-12-20 在线用户状态显示修改 start --%>
<% String positionName = LoadResource.getPostionById(onlineUser.getId() + "", onlineUser.getPositionId()).getPositionName(); %>
对方在<%= positionName %>,<%= ("发呆".equals(positionName))?"可能已经离开了,因此无法回复":"请耐心等待回复" %>(1秒跳转)<br/>
<%-- liuyi 2006-12-20 在线用户状态显示修改 end --%>
<%}%>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<%
 }
}
%>
<%--
<br/><a href="/user/ViewFriends.do">返回好友列表</a><br/>
<%
String chatRoomId = (String)session.getAttribute("currentroomid");
if(chatRoomId==null || chatRoomId.equals("")){
	chatRoomId = "0";
}
%>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>            --%>
</p>
</card>
</wml>