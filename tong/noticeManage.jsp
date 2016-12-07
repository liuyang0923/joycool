<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.noticeManage(request);
TongBean tong=(TongBean)request.getAttribute("tong");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="帮会通知">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
帮会通知会发给你所有的帮会会员。每人每条收100乐币，每小时只能发一次。请慎重使用。<br/>
帮会通知<br/>
<input name="notice"  maxlength="18" value="" title="名称"/><br/>
<anchor title="确定">确定发送
    <go href="/tong/noticeResult.jsp" method="post">
    <postfield name="notice" value="$notice"/>
    <postfield name="tongId" value="<%=tong.getId()%>"/>
    </go>
</anchor><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a> <a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<%}else{%>
您无权管理该帮会通知！<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>