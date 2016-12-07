<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8" import="jc.guest.*,jc.guest.farmer.*,net.joycool.wap.framework.*"%>
<%!int size=6; int stones=6; int bombs=3; String[] mapStr = {"■","○","●","★"}; int endIndex = size*size;%><%
FarmAction action=new FarmAction(request,response);
GuestUserInfo guestUser = action.getGuestUser();
int uid = 0;
if(guestUser != null) {
	uid = guestUser.getId();
}
FarmGameBean fgbean = (FarmGameBean) session.getAttribute("fgbean");
if(fgbean == null || action.getParameterInt("rep") == 1){
	if (uid > 0) {
		if (guestUser.getMoney() > 1) {
			action.updateMoney(uid, -1);
			fgbean = action.initiateFarmGame(size,stones,bombs,uid,endIndex);
			// 防止刷经验
			session.setAttribute("start","yes");
		} else {
			request.setAttribute("shortMoney","yes");
		}
	} else {
		fgbean = action.initiateFarmGame(size,stones,bombs,uid,endIndex);
	}
	session.setAttribute("fgbean",fgbean);
}
int aimStation = action.getParameterInt("i");
if (aimStation >= 0 && aimStation < endIndex) {
	action.walk(fgbean,aimStation);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="完美农夫"><p align="left"><%=BaseAction.getTop(request, response)%><%
if ("yes".equals(request.getAttribute("shortMoney"))) {
	%>很抱歉，您的游币不够了，无法进行游戏。<br/>*温馨提示：<a href="/register.jsp">注册</a>乐酷正式用户后会获得10000游币，每天上线还可以多获得200游币哟~<br/><%
} else if(fgbean != null){
	int steps = fgbean.getSteps();
	%>已种:<%=steps%>&#160;锤子:<%=fgbean.getBombs()%><br/>请选择前进方向<br/><%
	int [] farm = fgbean.getFarm();
	// 玩家所在地点
	int index = fgbean.getIndex();
	boolean canSee = false;
	boolean isOver = true;
	for(int i = 0; i < farm.length; i++) {
		boolean canClick = false;
		if (farm[i] == 1) {
			canSee = true;
		} else if (farm[i] == 0 && fgbean.getBombs() > 0) {
			canSee = true;
		}
		if(canSee) {
			int temp = index - i;
			if(i == 0){ // 左上角
				if (temp == 1 || temp == size) {
					isOver = false;
					canClick = true;
				}
			} else if(i == size - 1) { // 右上角
				if (temp == -1 || temp == size) {
					isOver = false;
					canClick = true;
				}
			} else if(i == endIndex - size) { // 左下角
				if (temp == 1 || temp == -size) {
					isOver = false;
					canClick = true;
				}
			} else if(i == endIndex - 1) { // 右下角
				if (temp == -1 || temp == -size) {
					isOver = false;
					canClick = true;
				}
			} else if(i < size - 1) { // 上排
				if (Math.abs(temp) == 1 || temp == size) {
					isOver = false;
					canClick = true;
				}
			} else if(i > endIndex - size) { // 下排
				if (Math.abs(temp) == 1 || temp == -size) {
					isOver = false;
					canClick = true;
				}
			} else if(i % size == 0) { // 左排
				if (temp == 1 || Math.abs(temp) == size) {
					isOver = false;
					canClick = true;
				}
			} else if((i + 1) % size == 0) { // 右排
				if (temp == -1 || Math.abs(temp) == size) {
					isOver = false;
					canClick = true;
				}
			} else { // 其他地点
				if (i == index - 1 || i == index + 1 || i == index - size || i == index + size) {
					isOver = false;
					canClick = true;
				}	
			}
			canSee = false;
		}
		if (canClick) {
		%><a href="farm.jsp?i=<%=i%>"><%=mapStr[farm[i]]%></a>&#160;<%
		} else {
		%><%=mapStr[farm[i]]%>&#160;<%
		}
		if(i!=0 && (i + 1)%size == 0) {
			%><br/><%
		}
	}
	%>注:空地○,石头■,麦田●,玩家★<br/><a href="farm.jsp?rep=1">重开一局</a><%if (guestUser != null) {
%>(需花费1游币)<%
}%><br/><%
	if (isOver) {
		response.sendRedirect("over.jsp");
	}
}else{
	%>没有游戏捏?<br/><%
}
%><a href="index.jsp">返回完美农夫首页</a><br/><%
FarmerBean notice = action.getNotice();
if (notice != null) {
	GuestUserInfo tempGuest = action.getGuestUser(notice.getUid());
	%>恭喜<%=tempGuest.getUserNameWml()%>获得了1次完美评分!<br/><%
}
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>