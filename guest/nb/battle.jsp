<%@page contentType="text/vnd.wap.wml;charset=utf-8"%><%response.setHeader("Cache-Control","no-cache");%>
<%@page import="jc.guest.*,net.joycool.wap.bean.*,net.joycool.wap.util.StringUtil,jc.guest.battle.*"%><%@page import="net.joycool.wap.framework.BaseAction"%>
<%GamepageAction action=new GamepageAction(request);
GuestHallAction action2 = new GuestHallAction(request,response);
GuestUserInfo guestUser = action2.getGuestUser();
Stored stored = (Stored)session.getAttribute("stored");
int k = StringUtil.toInt(request.getParameter("k"));
if (stored == null){
	if(StringUtil.toInt(request.getParameter("challenge"))==1){
		stored=new Stored();
		if(!action.check(request.getParameter("username1"),request.getParameter("username2"),stored)){
			session.setAttribute("stored",stored);
			response.sendRedirect("challenge.jsp?name="+StringUtil.toInt(request.getParameter("name"))+"&winlose="+StringUtil.toInt(request.getParameter("winlose")));
		return;
		}
		session.removeAttribute("stored");
	}
	if (action.getParameterNoEnter("username1")==null || action.getParameterNoEnter("username2")==null){
		if (k < 0){
			response.sendRedirect("game.jsp");
			return;
		}
	}
}
int result = action.fight(request,response,guestUser);
if (result == 1){
	// 没钱玩，转到提示页面
	response.sendRedirect("error.jsp");
	return;
}
NameProperty np1=null;NameProperty np2=null;
int firstdead=2;
stored=(Stored)session.getAttribute("stored");
   np1 =stored.getNp1(); 
   np2 =stored.getNp2();	
   firstdead=stored.getFirstdead();
if(!stored.isError()){
%><jsp:forward page="game.jsp"/>
<%}java.util.ArrayList  depict=stored.getDepict();
int time = 50;
if(firstdead==1){
	time = 10000000;
}
%>
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="名字大作战" ontimer="<%=response.encodeURL("battle.jsp") %>"><timer value="<%=time %>"/>
<p><%=BaseAction.getTop(request,response)%>
<a href="battle.jsp">刷新</a><br/>
[<%=StringUtil.toWml(np1.getName())%>]血:<%=np1.getHp()%><a href="property.jsp?index=1">查看属性</a><br/>
[<%=StringUtil.toWml(np2.getName())%>]血:<%=np2.getHp()%><a href="property.jsp?index=2">查看属性</a><br/>	
<%int x=0;for(int i=depict.size()-1;i>=0;i--){%>
<%=i+1%>.<%=StringUtil.toWml(depict.get(i).toString())%><br/>
  <%if(x>5)
	break;
	x++;}%>
<a href="show.jsp">查看全部</a><br/>
<a href="game.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p>
</card>
</wml>