<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%TongAction action=new TongAction(request);
action.reNameResult(request);
TongBean tong=(TongBean)request.getAttribute("tong");
String tip=(String)request.getAttribute("tip");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(tong!=null){%>
<card title="修改结果" ontimer="<%=response.encodeURL("/tong/tongManage.jsp?tongId="+tong.getId())%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tip!=null){%>
<%=tip%>
<%}%><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">直接跳转 </a> <br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a> <a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>