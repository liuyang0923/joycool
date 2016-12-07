<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.BaseAction,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
CoolUser cu = CoolShowAction.getCoolUser(ub);
int Iid = action.getParameterInt("Iid");
int back = action.buy(cu,Iid);
Pocket po = CoolShowAction.service.getDate1("del=0 and user_id="+ub.getId()+" and item_id="+Iid);
String ss = po==null?"购买":"续费";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="酷秀<%=ss%>">
<p><%=BaseAction.getTop(request, response)%><%
if(back == 1){
Commodity com = CoolShowAction.getCommodity(Iid);
%>【<%=com.getName()%>】购买成功!您可以在<a href="myGoods.jsp">我的物品</a>中找到它<br/><a href="room.jsp">&lt;回试衣间</a><br/><%
}else if(back == 2){
Commodity com = CoolShowAction.getCommodity(Iid);
%>【<%=com.getName()%>】续费成功!您可以在<a href="myGoods.jsp">我的物品</a>中查看!<br/><%
}else if(back == 3){
%><%=ss%>失败!您的酷币不足!点击【<a href="/pay/pay.jsp">充值</a>】<br/><a href="myGoods.jsp">&lt;我的物品</a><br/><%
}else if(back == 4){
%>已下架商品不能购买!<br/><%
}else if(back == 5){
%>无法重复操作!<br/><a href="myGoods.jsp">&lt;我的物品</a><br/><%
}else if(back == 6){
%>永久物品,无需续费!<br/><a href="consult.jsp?from=1&amp;Iid=<%=Iid%>">返回</a><br/><%
}else{
%>不存在的商品,购买失败!<br/><%
}
%><a href="index.jsp">&gt;我的酷秀</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>