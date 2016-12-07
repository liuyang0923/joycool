<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.spec.shop.*" %><%@ page import="net.joycool.wap.framework.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String mobile=loginUser.getMobile();
int status = SqlUtil.getIntResult("select status from test.shop_gift where mobile='"+StringUtil.toSql(mobile)+"' and type=1 limit 1");	// type==1表示mmb老用户推广
if(status==-1){
response.sendRedirect("/bottom.jsp");
return;
}
if(status==0){
	SqlUtil.executeUpdate("update test.shop_gift set status=1,create_time=now() where mobile='"+StringUtil.toSql(mobile)+"' and type=1");
	ShopUtil.charge(loginUser.getId(),10.0f);
}
UserInfoBean userInfo = ShopAction.shopService.getUserInfo(loginUser.getId());

%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="领取酷币"><p>
<%if(status==0) {%>
恭喜您成功领取了10酷币
<%} else {%>
您已经领取过酷币了
<%} %>
,您现在共拥有<%=userInfo.getGoldString()%><br/>
<a href="/Column.do?columnId=5511">&gt;先了解游戏币如何使用?</a><br/>
<br/>
<a href="/Column.do?columnId=5507">返回乐酷欢迎页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>