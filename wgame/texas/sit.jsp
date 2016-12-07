<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.util.ForbidUtil"%><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%
response.setHeader("Cache-Control","no-cache");

TexasGame game = (TexasGame)session.getAttribute("texas");
if(game == null) {
	response.sendRedirect("index.jsp");
	return;
}

TexasAction action = new TexasAction(request);
if(ForbidUtil.isForbid("game",action.getLoginUser().getId())){
	response.sendRedirect("/enter/not.jsp");
	return;
}
action.sit();
if(action.isResult("ok")){
	response.sendRedirect("play.jsp");
	return;
}
int sit = action.getParameterInt("sit");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="德州扑克">
<p align="left">
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
<%=game.getBoardId()+1%>号<%=game.getGameTypeName()%>桌<%=sit+1%>座<br/>
大盲注<%=game.getBaseWager()%>,小盲注<%=game.getBaseWager()/2%><br/>
现有<%=game.getUserCount()%>人,最多<%=game.getMaxUser()%>人<br/>
<%if(game.getGameType()==1){
TexasUserBean tub =	TexasGame.getUserBean(action.getLoginUser().getId());
%>现有积分<%=tub.getMoney()%><br/><%}%>
请输入带入游戏的筹码:(最多可以携带<%=game.getMaxMoney()%>,至少<%=game.getMinMoney()%>)<br/>
<input name="moneyte" format="*N" maxlength="4" value="<%=game.getMaxMoney()%>"/><br/>
<anchor title="确定">确定坐下
  <go href="sit.jsp?sit=<%=sit%>" method="post">
    <postfield name="money" value="$moneyte"/>
  </go>
</anchor><br/>

<br/>
<a href="play.jsp">返回游戏</a><br/>
<a href="index.jsp">返回德州扑克大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>

</card>
</wml>