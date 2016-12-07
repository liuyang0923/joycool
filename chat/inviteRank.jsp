<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.CrownBean"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%
response.setHeader("Cache-Control","no-cache");

JCRoomChatAction action=new JCRoomChatAction(request);
action.getCurInviteRank(request);
Vector resource=(Vector)request.getAttribute("resource");
Vector rankList=(Vector)request.getAttribute("rankList");
int rank = StringUtil.toInt((String)request.getAttribute("rank"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="今日邀请排名">
<p align="left">
<%=BaseAction.getTop(request, response)%>
今日邀请排名(最终排名前七将在第二天得到帽子)<br/>
----------<br/>
<%if(rankList.size()==0){%>您目前还没有排名<%}else if(rank==1){%>您目前排名第1<%}else{%>您目前排名第<%=rank%><%}%><br/>
<%
RoomInviteRankBean rankBean=null;
for(int i=0;i<rankList.size();i++){
	rankBean=(RoomInviteRankBean)rankList.get(i);
	UserStatusBean us=UserInfoUtil.getUserStatus(rankBean.getUserId());
%>
<%=(i+1)%>.<a href="/user/ViewUserInfo.do?userId=<%=rankBean.getUserId()%>"><%if(us!=null){%><%=us.getHatShow()%><%}%><%=StringUtil.toWml(rankBean.getNickName())%></a>(邀请<%=rankBean.getCount()%>位)<br/>
<%}%>----------<br/>
<%
for(int i=1;i<8;i++){
%><img src="/rep/lx/t<%=i%>.gif" alt=""/><%}%>
这7顶独一无二的王冠,只有每天总排名前７位的精英才能获得,还不赶快行动?<a href="<%=("inviteRoom.jsp") %>">马上抢夺王冠</a><br/>
<a href="lastRank.jsp">返回邀请榜</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
