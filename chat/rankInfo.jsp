<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请榜说明">
<p align="left">
<%=BaseAction.getTop(request, response)%>
邀请榜说明<br/>
-----------<br/>
1、使用<a href="inviteRoom.jsp">好友邀请</a>功能免费发送消息给自己的好友，即可参与排名。<br/>
2、只要您邀请的好友是第一次来乐酷，您将获得1点积分，积分的多少决定您在排行榜上的名次。<br/>
3、我们将每天统计前一天每个用户邀请的好友上线的成功率，进行排名。每天排名前七位的用户都可以获得乐酷王冠一顶。<br/>
4、王冠一共7顶，每顶各不相同，获得王冠者随机分配。<br/>
5、王冠使用期限为一周，如果在一周内排名仍在前7位，王冠将继续保留，按照新的周期计算使用期限。<br/>
<a href="inviteRoom.jsp">邀请好友抢王冠>></a><br/>
<a href="lastRank.jsp">返回邀请榜</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
