<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.BaseAction,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
int Iid = action.getParameterInt("Iid");
int back = action.collection(ub.getId(),Iid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="酷秀收藏">
<p><%=BaseAction.getTop(request, response)%><%
if(back == 1){
Commodity com = CoolShowAction.getCommodity(Iid);
%>您已经将【<%=com.getName()%>】添加到收藏夹中<br/><%
}else if(back == 2){
Commodity com = CoolShowAction.getCommodity(Iid);
%>您的收藏夹中已经添加了【<%=com.getName()%>】,无法重复添加同一个商品<br/><%
}else{
%>商品信息不存在,收藏失败!<br/><%
}
%><a href="consult.jsp?from=1&amp;Iid=<%=Iid%>">&lt;返回</a><br/><a href="mycol.jsp">&lt;我的收藏</a><br/><a href="myGoods.jsp">&lt;我的物品</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>