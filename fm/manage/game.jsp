<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.*"%><%@ page import="jc.family.*"%><%
FamilyAction fmAction=new FamilyAction(request,response);
int id=fmAction.getParameterInt("id");
if(id==0){
response.sendRedirect("/fm/index.jsp");return;
}
List gamelist=fmAction.getFmGameBanUserList();
int p =fmAction.getParameterInt("p");
String game=request.getParameter("game");
if(game==null)game="boat";
String paging=(String)request.getAttribute("pages");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家游戏管理"><p align="left"><%=BaseAction.getTop(request, response)%>
<a href="gamemgt.jsp?id=<%=id%>">添加封禁成员</a><br/>
封禁列表:<br/><%
if(game.equals("boat")){%>龙舟|<a href="game.jsp?id=<%=id%>&#38;game=ask">问答</a>|<a href="game.jsp?id=<%=id%>&#38;game=snow">打雪仗</a><br/><%}
if(game.equals("ask")){%><a href="game.jsp?id=<%=id%>&#38;game=boat">龙舟</a>|问答|<a href="game.jsp?id=<%=id%>&#38;game=snow">打雪仗</a><br/><%}
if(game.equals("snow")){%><a href="game.jsp?id=<%=id%>&#38;game=boat">龙舟</a>|<a href="game.jsp?id=<%=id%>&#38;game=ask">问答</a>|打雪仗<br/><%}
for(int i=0;i<gamelist.size();i++){
FamilyUserBean fmUser=(FamilyUserBean)gamelist.get(i);
%><a href="/user/ViewUserInfo.do?userId=<%=fmUser.getId()%>"><%=1+i+p*10%>.<%=fmUser.getNickNameWml()%></a>&#160;&#160;<a href="game.jsp?id=<%=id%>&#38;game=<%=game%>&#38;p=<%=p%>&#38;userid=<%=fmUser.getId()%>">解封</a><br/>
<%}%>
<%if(paging!=null&&!"".equals(paging)){%>
<%=paging%><%}%>
<a href="management.jsp?id=<%=id%>">返回家族管理</a><br/>
<a href="/fm/myfamily.jsp?id=<%=id%>">返回我的家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>