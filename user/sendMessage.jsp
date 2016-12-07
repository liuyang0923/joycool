<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.user.RankAction"%><%@ page import="net.joycool.wap.service.impl.MessageServiceImpl"%><%@ page import="net.joycool.wap.service.impl.UserServiceImpl"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%!
	static MessageServiceImpl messageService = new MessageServiceImpl();
	static UserServiceImpl service = new UserServiceImpl();
%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
UserBean loginUser = action.getLoginUser();
if (loginUser == null) {
	response.sendRedirect("/user/login.jsp");
    return;
}
String tip = null;
if(action.hasParam("content")){
    boolean flag = true;

    String content = action.getParameterNoEnter("content");
    String backTo = request.getParameter("backTo");        
    if(backTo == null){
        backTo = BaseAction.INDEX_URL;
    }
    int toUserId = action.getParameterInt("toUserId");
    UserBean toUser = null;
	if(toUserId!=0)
		toUser = UserInfoUtil.getUser(toUserId);
    String info = null;
    if (content == null || content.length() == 0) {
        tip = "消息内容不能为空！";
        flag = false;
    } else if (content.length() > 200) {
        tip = "消息内容过长！";
        flag = false;
    } else if (toUserId == 100 || toUserId == 0 || toUser == null) {
        tip = "无效的收件人！";
        flag = false;
    } else if(service.isUserBadGuy(toUserId, loginUser.getId())){
        tip = "你在对方的黑名单里，不能给他发送消息！";
        flag = false;
    } else {
        //macq_2007-6-19_放置重复发言_start
        String infos = (String) session.getAttribute("sendMessageCheck");
		info = loginUser.getId()+content +toUserId;
		if (info.equals(infos)) {
			tip = "不能发送内容重复的信件！";
            flag = false;
		} else if(!action.isCooldown("chat", 5000)) {
	        tip = "发送太快了休息一会吧！";
	        flag = false;
	    } else {
			ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("mail",loginUser.getId());
			if(forbid != null) {
				tip = "已经被禁止发送信件 - " + forbid.getBak();
	            flag = false;
			}
		}
    }
    //macq_2007-6-19_放置重复发言_end
    //填写信息不正确
    if (flag == false) {
        request.setAttribute("result", "failure");
        request.setAttribute("tip", tip);
        request.setAttribute("backTo", backTo);
    }
    //填写信息正确
    else {
    	session.setAttribute("sendMessageCheck", info);
    	
        int[] count = CountMaps.countMap1.getCount(loginUser.getId());
		count[0]++;
		if(count[0] < 500) {
		
	        MessageBean message = new MessageBean();
	        message.setFromUserId(loginUser.getId());
	        message.setToUserId(toUserId);
	        message.setContent(content);
	        message.setMark(0);
	        
	        messageService.addMessage(message);
	        toUser.notice[1]++;
	        //mcq_1_增加用户经验值  时间:2006-6-11
	        //增加用户经验值
	        RankAction.addPoint(loginUser,Constants.RANK_GENERAL);
	        //mcq_end
	//    		wucx 用户有好度2006－10－19 start
	//service.updateFriend("level_value=level_value+1,update_datetime=now()","user_id="+loginUser.getId()+" and friend_id="+StringUtil.toInt(toUserId));
	        service.addOrupdateFriendLevel(loginUser.getId(),toUserId);
	        UserInfoUtil.updateUserStatus("social=social+1" , "user_id="+loginUser.getId(), loginUser.getId(), 0, "写信增加1个社交指数");
	
	        		//wucx 用户有好度2006－10－19 end
        }
        request.setAttribute("result", "success");
        request.setAttribute("backTo", backTo);
    }


}
String returnUrl = (String)session.getAttribute("pagebeforeclick"); 
if(returnUrl==null){
	returnUrl = "ViewMessages.do";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<logic:present name="result" scope="request">

    <logic:equal name="result" value="failure">
<card title="发送信件">
<p align="left">
<%=BaseAction.getTop(request, response)%>
发送信件<br/>
--------<br/>
    <%=tip%><br/>
	<anchor title="back"><prev/>返回发送页面</anchor><br/>
<%
//String backTo = (String) request.getAttribute("backTo");
//backTo = backTo.replace("&", "&amp;");
%>
    <a href="<%=(returnUrl.replace("&", "&amp;"))%>">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>

    <logic:equal name="result" value="success">
<%
String toUserId = (String) request.getParameter("toUserId");
%>
<card title="发送信件" ontimer="<%=response.encodeURL(returnUrl.replace("&", "&amp;"))%>">
    <timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
发送信件<br/>
------------------<br/>
发送成功！3秒后自动跳转。<br/>
	<a href="<%=(returnUrl.replace("&", "&amp;"))%>">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
    </logic:equal>
</logic:present>

<logic:notPresent name="result" scope="request">
<card title="发送信件">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
int toUserId = action.getParameterInt("toUserId");

%>
发送信件<br/>
------------------<br/>
信件内容：<br/><input name="content"  maxlength="200" value=""/>
<br/> 
<br/>
<anchor title="确定">发送
<go href="sendMessage.jsp?toUserId=<%=toUserId%>" method="post">
    <postfield name="content" value="$content"/>
</go>
</anchor>
<br/>
<br/>

<a href="ViewUserInfo.do?userId=<%=toUserId%>">返回用户信息</a><br/>
<a href="ViewMessages.do">返回我的信箱</a><br/>
<%
String chatRoomId = (String)session.getAttribute("chatroomid");
if(chatRoomId==null || chatRoomId.equals("")){
	chatRoomId = "0";
}
%>
<a href="/chat/hall.jsp?roomId=<%=chatRoomId%>">返回聊天室</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:notPresent>
</wml>