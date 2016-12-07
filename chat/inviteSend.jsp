<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
int inviteCount=0;
int TOTAL_INVITE_COUNT=20;
if(session.getAttribute("inviteCount")!=null){
	inviteCount=StringUtil.toInt((String)session.getAttribute("inviteCount"));
}
String info="您的邀请已经发送给您的好友！(3秒钟后自动返回邀请页)您今天还能发"+(TOTAL_INVITE_COUNT-inviteCount)+"条!";
int flag=0;
String url=("inviteRoom.jsp");
if(request.getParameter("flag")!=null){
	flag=Integer.parseInt(request.getParameter("flag"));
	if(flag==2){//0-8点发送
		info="您的邀请已经发送成功！为了不影响您的好友休息，消息将在8点发送到他的手机上(3秒钟后自动返回邀请页)您今天还能发"+(TOTAL_INVITE_COUNT-inviteCount)+"条!";
	}
	else if(flag==3){//发送给北京用户
		url="/chat/hall.jsp";
		info="对不起，您的好友所在范围，暂时发送不到。您可以让他短信发送zc到13718998855，就可以上来跟您玩啦!(可惜，这样上来的用户不参予王冠排名哦)";
	}
	else if(flag==4)//用户等级不够
		info="对不起，只有3级以上用户才可使用此功能！ ";
	else if(flag==5){
		url="/chat/hall.jsp";
		info="您可以让他短信发送zc到13718998855，就可以上来跟您玩啦!(可惜，这样上来的用户不参予王冠排名哦)";		
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请好友来乐酷" <%if(flag!=3&&flag!=4&&flag!=5){%>ontimer="<%=response.encodeURL(url)%>"<%}%> >
<%if(flag!=3&&flag!=4&&flag!=5){%><timer value="30"/><%}%>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=info%><br/>
<%if(flag!=4&&flag!=5){%>
<a href="<%=("inviteRoom.jsp") %>">继续邀请</a><br/>
<%}%>
<a href="<%=("lastRank.jsp") %>">返回邀请榜</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>