<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.spec.shop.*" %><%@ page import="net.joycool.wap.framework.*,java.util.*"%><%
response.setHeader("Cache-Control", "no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String mobile=loginUser.getMobile();
int status = SqlUtil.getIntResult("select status from test.shop_gift where mobile='"+StringUtil.toSql(mobile)+"' and type=2 limit 1");	// type==1表示mmb老用户推广
if(status==-1){
response.sendRedirect("/bottom.jsp");
return;
}
if(status==0){
	SqlUtil.executeUpdate("update test.shop_gift set status=1,create_time=now() where mobile='"+StringUtil.toSql(mobile)+"' and type=2");
	
	// 给予对应的奖励
	int[] items = {5253};
	int i = RandomUtil.nextInt(items.length);
	int itemId = items[i];
	net.joycool.wap.cache.util.UserBagCacheUtil.addUserBagCache(loginUser.getId(), itemId, 1, 0);
}

//UserInfoBean userInfo = ShopAction.shopService.getUserInfo(loginUser.getId());

%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="领取奖励"><p>
<%if(status==0) {%>
恭喜您成功领取了元旦礼品
<%} else {%>
您已经领取过元旦礼品了
<%} %>
,请到<a href="/user/userBag.jsp">行囊</a>查收<br/>
<br/>
<a href="/Column.do?columnId=5507">返回乐酷欢迎页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>