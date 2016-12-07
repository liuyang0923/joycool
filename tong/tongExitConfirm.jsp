<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.cache.util.*,net.joycool.wap.bean.UserBean,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><jsp:directive.page import="net.joycool.wap.bean.tong.*"/>
<jsp:directive.page import="net.joycool.wap.util.StringUtil"/>
<%
response.setHeader("Cache-Control","no-cache");

int tongId = StringUtil.toInt(request.getParameter("tongId"));
TongBean tong = TongCacheUtil.getTong(tongId);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
TongUserBean tongUser = TongCacheUtil.getTongUser(tong.getId(), loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="退出帮会" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%//if(tongUser==null){%>
<%--你不是该帮会成员！--%>
<%
//}else{ 
%>
确定要放弃一切，退出帮会吗？损失1万乐币哦。<br/>
<a href="/tong/tongExit.jsp?tongId=<%=tong.getId()%>">确定退出</a> 
<%//} %> 
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回帮会首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>