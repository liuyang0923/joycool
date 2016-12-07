<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.show.*,java.util.*,net.joycool.wap.bean.*,net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
if (ub == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
CoolUser cu = CoolShowAction.getCoolUser(ub);
List list = StringUtil.toInts(cu.getCurItem());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的酷秀">
<p><%=BaseAction.getTop(request, response)%>
==当前的形象==<br/><%if(cu.getImgurl().length()==0){%><img src="img/e<%=ub.getGender()%>.gif" alt="秀"/><%}else{%><img src="/rep/show/act/<%=cu.getImgurl()%>" alt="秀"/><%}%><br/><%
%>【酷秀搭配】<a href="room.jsp">+换装+</a><br/><%
	if(list!=null && list.size() > 0){
		for(int i=0;i<list.size();i++){
			Commodity com = CoolShowAction.getCommodity(((Integer)list.get(i)).intValue());
			if(com != null){
				%>[<%=com.getCatalogName()%>]<%=StringUtil.toWml(com.getName())%><br/><%
			}
		}
	}else{
		%>(暂无搭配)<br/><%
	}
%>==管理物品==<br/><a href="myGoods.jsp">我的物品</a>|<a href="mycol.jsp">我的收藏</a><br/><a href="room.jsp">我的试衣间</a>|<a href="history.jsp">历史记录</a><br/><a href="frdshowlist.jsp">好友的酷秀</a><br/>
商城<a href="downtown.jsp?gend=1">女装区</a>|<a href="downtown.jsp?gend=2">男装区</a><br/><%
%><%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>