<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.tongDissolve(request);
int result=StringUtil.toInt((String)request.getAttribute("result"));
TongBean tong=(TongBean)request.getAttribute("isuser");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="解散帮会">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%> 
<%if(result==1){%> 
<%=request.getAttribute("tip")%><br/><br/>
<a href="/tong/tongDissolve.jsp?tongId=<%=tong.getId()%>&amp;time=1&amp;del=<%=tong.getId()%>">解散帮会（慎重！） </a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%}else if(result==2){%> 
<%=request.getAttribute("tip")%><br/><br/>
<a href="/tong/tongDissolve.jsp?tongId=<%=tong.getId()%>&amp;time=2&amp;del=<%=tong.getId()%>">解散帮会（慎重！） </a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%}else if(result==3){%> 
<%=request.getAttribute("tip")%><br/><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<a href="/tong/tongList.jsp">返回所有帮会页面 </a><br/>
<%}else if(result==4){%> 
<%=request.getAttribute("tip")%><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%}else {%> 
<%=request.getAttribute("tip")%><br/><br/>
<%}
}
else {%> 
<%=request.getAttribute("tip")%><br/>
<%}%> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>