<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="java.util.*,net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.List"%><%
	
	
	CastleAction action = new CastleAction(request);
	CastleUtil.stat4Defense();
	CastleUserBean castleUser = action.getCastleUser();
	int cur = action.getParameterIntS("p");
	int userCur = 0;
	if(cur == -1 && castleUser!=null){
		userCur = action.getCastleService().getDefenseCurArray(castleUser.getUid());
		//System.out.println(userCur);
		if(userCur > 0) {
			cur = (userCur % 10 == 0) ? (userCur / 10 - 1) : (userCur / 10);
			//System.out.println(cur);
		} else{
			cur = 0;
		}
	}
	if(cur<0) cur=0;
	int start = cur * 10;
	int limit = 11;
	
	//PagingBean paging = new PagingBean(action, total, 10, "p");
	List list = action.getCastleService().getDefenseArray(start, limit);
	int count = list.size() > 10 ? 10 : list.size();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="防御排行榜"><p>
<%if(userCur > 0) {%>
您当前的排名为:第<%=userCur %>名<br/>
<%} %>==城主/防御==<br/>
<%for(int i=0;i<count;i++){
	Object[] obj = (Object[])list.get(i);
%>
<%=obj[0]%>.<a href="../user.jsp?uid=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[2])%></a>/<%=obj[3]%>
<br/><%}%>
<%if(list.size() > 10) {%><a href="stat4.jsp?p=<%=cur+1%>">下一页</a><%}else{%>下一页<%}%>
<%if(cur > 0) {%><a href="stat4.jsp?p=<%=cur-1%>">上一页</a><%}else{%>上一页<%}%><br/>
跳转到:<input name="p" maxlength="3" format="*N"/><anchor>GO<go href="stat4.jsp"><postfield name="p" value="$p"/></go></anchor><br/>
<a href="stats.jsp">返回城堡排名页</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>