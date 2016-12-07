<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%!
static CastleService service = CastleService.getInstance();%><%
		
	CustomAction action = new CustomAction(request);
	List list = service.getWWList("1 order by lvl desc,id asc");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p>
<%=BaseAction.getTop(request, response)%>
==世界奇迹(<%=list.size()%>)==<br/>
<%
if(list.size()==0){%>(暂时未开放)<br/><%}
for(int i=0;i<list.size();i++){
WWBean ww = (WWBean)list.get(i);
CastleBean castle = CastleUtil.getCastleById(ww.getCid());
CastleUserBean user = CastleUtil.getCastleUser(castle.getUid());
String tongName =null;
if(user.getTong()!=0)
	tongName = StringUtil.toWml(CastleUtil.getTong(user.getTong()).getName());
%><%=i+1%>.<a href="../search.jsp?cid=<%=ww.getCid()%>"><%=ww.getName()%></a><%if(ww.getLevel()>=5){%><img src="/cast/img/battle.gif" alt="X"/><%}%><%=ww.getLevel()%>级(<%=user.getNameWml()%>)<%if(user.getTong()!=0){%><a href="../tong/tong.jsp?id=<%=user.getTong()%>"><%=tongName%></a><%}%><br/>
<%}%>
<a href="../s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>