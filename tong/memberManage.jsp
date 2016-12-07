<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%TongAction action=new TongAction(request);
action.memberManage(request);
TongBean tong=(TongBean)request.getAttribute("tong");
String count=(String)request.getAttribute("count");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="会员管理">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
会员管理，是保证帮会兴盛的必要途径！<br/>
<a href="/tong/tongApplyList.jsp?tongId=<%=tong.getId()%>">入帮申请 </a> 
<%if(count!=null){%>
<%=count%>
<%}%>个未处理<br/>
<a href="/tong/tongUserList.jsp?tongId=<%=tong.getId()%>">帮会会员 </a> 
<%=tong.getUserCount()%>个<br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市</a> 
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理</a><br/>
<%}else{%>
您无权对该帮会进行会员管理！<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>