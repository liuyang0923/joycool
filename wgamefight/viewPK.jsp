<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.wgamefight.FightAction" %><%@ page import="net.joycool.wap.bean.wgamefight.WGameFightBean" %><%
response.setHeader("Cache-Control","no-cache");
FightAction action = new FightAction(request);
action.viewWGamePK(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
WGameFightBean fightBean = (WGameFightBean) request.getAttribute("fightBean");
int toUserId=0;
String chatUrl = null;
String viewUrl = null;
if(fightBean != null){    
	if(fightBean.getLeftUserId() == loginUser.getId()){
		toUserId = fightBean.getRightUserId();
	} else {
		toUserId = fightBean.getLeftUserId();
	}
	chatUrl = ("/chat/post.jsp?toUserId=" + toUserId );
	viewUrl = ("/user/ViewUserInfo.do?userId=" + toUserId );
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="赌局">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//已经被取消
if(fightBean == null){
%>
这局已被取消!<br/>
<%
} 
//坐庄中
else if(fightBean.getMark() == WGameFightBean.PK_MARK_BKING){
	UserStatusBean usTemp=null;
    usTemp=UserInfoUtil.getUserStatus(fightBean.getLeftUserId());
%>
庄家:<%if(usTemp!=null){%><%=usTemp.getHatShow()%><%}%><%=StringUtil.toWml(fightBean.getLeftNickname())%><br/>
状态:等待挑战<br/>
赌注:<%=fightBean.getWager()%>乐币<br/>
<%
}
//结束
else if(fightBean.getMark() == WGameFightBean.PK_MARK_END){
String leftStr[]=fightBean.getLeftViewed().split(",");
String rightStr[]=fightBean.getRightViewed().split(",");
if(fightBean.getLeftUserId()==fightBean.getWinUserId()){%>
恭喜，您坐庄赢了！
<%}
else if(fightBean.getWinUserId()==0){
%>
您和挑战者打平!
<%}else{%>
很遗憾，您坐庄输了！
<%}%><br/>
赌注金额：<%=fightBean.getWager()%><br/>
庄家选择：<%for(int i=0;i<leftStr.length;i++){%><%=action.getStringName(StringUtil.toInt(leftStr[i]))%><%}%><br/>
庄家：您自己<br/>
挑战者：<a href="<%=viewUrl%>"><%=StringUtil.toWml(fightBean.getRightNickname())%></a><br/>
<%}%>
<br/>
<%= PositionUtil.getLastModuleUrl(request, response)%><br/>
<a href="/wgamefight/index.jsp">返回上一级</a><br/>
<a href="/lswjs/wagerHall.jsp">返回通吃岛赌坊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>