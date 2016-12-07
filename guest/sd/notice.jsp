<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="jc.guest.*,jc.guest.sd.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><% 
	response.setHeader("Cache-Control","no-cache");
	ShuDuAction action = new ShuDuAction(request,response);
	GuestUserInfo guestUser = action.getGuestUser();
	int type = action.getParameterInt("type");
	if(type < 1 || type > 4){
		type = 2;
	}
	int lvl = action.getParameterInt("lvl");
	if (lvl <= 0 || lvl > 4) {
		lvl = 1;
	}
	
 %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="数独"><p>
<%=BaseAction.getTop(request, response)%>
<% 
if (type == 1) {
%>确认重新开始数独游戏<%
	if (guestUser != null){
		int rants = ShuDuAction.rant[lvl];
		%>，需花费<%=rants%>游币<%
	}
%><br/><a href="shudu.jsp?ano=yes&amp;lvl=<%=lvl%>">确定</a>|<a href="shudu.jsp?lvl=<%=lvl%>">取消</a><br/><%
} else if (type == 2) {
	int result = action.getParameterInt("res");
	if (result == 1) {
	%>未登录,无法保存!<%
	} else {
	%>保存成功!<%
	}
%><br/><a href="shudu.jsp?lvl=<%=lvl%>">确定</a><br/><%
} else if (type == 3) {
	int result = action.getParameterInt("res");
	if (result == 1) {
	%>再看看哪里有错误吧!<br/><a href="shudu.jsp?lvl=<%=lvl%>">确定</a><br/><%
	} else {
		if (guestUser != null) {
			int rant = 0;
			int exp = 0;
			int remainExp = 0;
			String strLvl = "";
			if (lvl == 3) {
				rant = 2;
				exp = 2;
				strLvl = "困难";
			} else if (lvl == 4) {
				rant = 3;
				exp = 4;
				strLvl = "终极";
			} else if (lvl == 2) {
				rant = 1;
				exp = 1;
				strLvl = "中等";
			} else if (lvl == 1) {
				rant = 1;
				exp = 1;
				strLvl = "简单";
			}
			if(guestUser.getLevel()< GuestHallAction.point.length ){
				remainExp = GuestHallAction.point[guestUser.getLevel()]-guestUser.getPoint()-ShuDuAction.exp[lvl];
			}
			%>恭喜！[<%=strLvl%>]数独完成数加1。<% 
				if (!"".equals(remainExp)) {
				%>您获得<%=exp%>点经验，距离提升到下一等级，还差<%=remainExp%>点经验.每升1级，每天上线将会多领取50游币<%
				}
		} else {
			%>很抱歉，您还不是注册用户，我们无法为您保存战绩哦。想保存战绩？只需两步就可以成功注册，请点此<a href="/guest/nick.jsp">设置昵称</a><%
		}
	%><br/><a href="index.jsp">确定</a><br/><%
	}
} else if (type == 4) {
	%>很抱歉,您的游币不够了,无法进行游戏.*温馨提示:<a href="/register.jsp">注册</a>乐酷正式用户后会获得10000游币,每天上线可以多获得200游币哟~<br/><a href="index.jsp">确定</a><br/><%
}
 %>
<a href="index.jsp">返回数独</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>