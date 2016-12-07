<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.BaseAction,jc.show.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
int uid = ub.getId();
int Iid = action.getParameterInt("Iid");
int touid = action.getParameterInt("touid");
int back = action.give(Iid,uid,touid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="酷秀赠送">
<p><%=BaseAction.getTop(request, response)%><%
if(back == 1){
Commodity com = CoolShowAction.getCommodity(Iid);
%>酷秀【<%=com.getName()%>】赠送成功!<br/><%
}else if(back == 2){
%>酷币不足,赠送失败!点击【<a href="/pay/pay.jsp">充值</a>】<br/><%
}else if(back == 3){
%>不存在的用户,赠送失败!<br/><%
}else if(back == 4){
%>无法重复操作!<br/><%
}else{
%>不存在的商品,购买失败!<br/><%
}
%><a href="consult.jsp?from=1&amp;Iid=<%=Iid%>">返回</a><br/><a href="myGoods.jsp">&lt;我的物品</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p>
</card>
</wml>