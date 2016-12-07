<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.BaseAction,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
int del = action.getParameterInt("del");
int back = action.delcol(ub.getId(),del);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="收藏酷秀">
<p><%=BaseAction.getTop(request, response)%><%
if(back == 1){
	%>收藏删除成功<br/><%
}else{
	%>此收藏已经删除<br/><%
}
%><a href="mycol.jsp">&lt;我的收藏</a><br/><a href="myGoods.jsp">&lt;我的物品</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>