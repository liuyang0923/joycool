<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%!
	static CastleService castleService = CastleService.getInstance();
%><%
	CastleAction action = new CastleAction(request);
	boolean isTip = action.isResult("tip");
	String tip = null;
	int grade = 0;
	if(!isTip){
		CastleUserBean user = action.getCastleUser();
		grade = action.getUserResBean().getBuildingGrade(ResNeed.WONDER_BUILD);
		if(grade==0){
			response.sendRedirect("ad.jsp");
			return;
		}
		String name = action.getParameterNoEnter("name");
		if(name!=null){
			if(StringUtil.getCLength(name)>20||name.length()==0){
				tip="名称最少一个字最多十个字";
			}else{
				SqlUtil.executeUpdate("update castle_ww set name='"+StringUtil.toSql(name)+"' where cid="+action.getCastle().getId(),5);
				tip="世界奇迹改名成功";
			}
		}
	}
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="世界奇迹"><p>
<%if(isTip){%><%=action.getTip()%><br/><%}else{

	WWBean ww = castleService.getWW("cid="+action.getCastle().getId());
%><%if(tip!=null){%><%=tip%><br/><%}
%>【<%=StringUtil.toWml(ww.getName())%>】等级<%=grade%><br/>
世界奇迹改名为:<input name="wwn" value=""/><br/>
<anchor>确认修改<go href="wwR.jsp" method="post">
<postfield name="name" value="$wwn"/></go></anchor><br/>
<br/>
<%}%>
<a href="fun.jsp?t=38">返回世界奇迹</a><br/><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>