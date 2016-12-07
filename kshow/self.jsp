<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.shop.*,net.joycool.wap.framework.BaseAction,jc.show.*,net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
CoolShowAction action = new CoolShowAction(request);
UserBean ub = action.getLoginUser();
int uid = ub.getId();
UserInfoBean bean = ShopAction.shopService.getUserInfo(uid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="个人信息">
<p><%=BaseAction.getTop(request, response)%>您有<%=bean.getGoldString()
%><br/><a href="/pay/pay.jsp">酷币充值</a>|<a href="history.jsp">历史记录</a><br/><a href="mycol.jsp">我的收藏</a>|<a href="myGoods.jsp">我的物品</a><br/><a href="index.jsp">&gt;我的酷秀</a><br/><a href="downtown.jsp?gend=2">&gt;&gt;商城男装区</a><br/><a href="downtown.jsp?gend=1">&gt;&gt;商城女装区</a><br/><%=BaseAction.getBottom(request, response)%></p>
</card>
</wml>