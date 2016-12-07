<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%

	CustomAction action = new CustomAction(request);
	int area=action.getParameterInt("i");
	List list = SqlUtil.getObjectsList("select uid,name,id from outs where area="+area+" order by id", 5);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="名人堂"><p>
【名人堂】<br/>
这里都是有突出表现的城堡玩家!(排名不分先后)<br/>
<%for(int i=0;i<list.size();i++){
	Object[] obj = (Object[])list.get(i);
%>【<a href="/user/ViewUserInfo.do?userId=<%=obj[0]%>"><%=StringUtil.toWml(obj[1].toString())%></a>】<a href="out2.jsp?id=<%=obj[2]%>">详细</a>
<br/><%}%>

<a href="/cast/index.jsp">返回城堡大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>