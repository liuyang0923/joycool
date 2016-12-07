<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.cache.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="java.util.List"%><%
	CastleBean bean = action.getCastle();
	UserResBean res = action.getUserResBean();
%>
当前建造所需时间<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()).getValue()%>%<br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后建造所需时间<%=ResNeed.getBuildingT(building.getBuildType(),building.getGrade()+1).getValue()%>%<br/><%}%>
<%if(building.getGrade()>=10){%>[<a href="dupgrade.jsp">拆毁建筑</a>]如果你有哪个建筑不需要的,可以给你的建筑师发令让他将你不需要的建筑慢慢拆毁<br/><%}%>
==城堡信息==<br/>
坐标:<%=bean.getX()%>|<%=bean.getY()%><br/>
城堡名:<%=StringUtil.toWml(bean.getCastleName())%><a href="<%=("u.jsp") %>">修改</a><br/>
种族:<%=bean.getRaceName()%><br/>
人口:<%=res.getPeople()%>/<%=res.getGrainSpeed2()%><br/>