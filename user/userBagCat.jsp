<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.user.UserBagAction" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.UserBagBean" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%
response.setHeader("Cache-Control","no-cache");
%><%@include file="../bank/checkpw.jsp"%><%
UserBagAction action = new UserBagAction(request);
action.userBagCat();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
HashMap userBagMap = UserBagCacheUtil.getUserBagItemMap(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title=" 行囊分类">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(userBagMap.size()>0){%>
共有<%=userBagMap.size()%>类物品<br/>
<%
int i = 0;
Iterator iter = userBagMap.entrySet().iterator();
while(iter.hasNext()){
    Map.Entry e = (Map.Entry)iter.next();
    Integer item = (Integer)e.getKey();
    int[] count = (int[])e.getValue();
    DummyProductBean dummyProduct=action.getDummyProduct(item.intValue());%>
<%=++i%>.<a href="userBagUse.jsp?item=<%=item.intValue()%>"><%=dummyProduct.getName()%></a>x<%=count[0]%><br/>
<%}}else{%>您的行囊中没有物品!<br/><%}%><br/>
<a href="userBag.jsp">返回我的行囊</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>