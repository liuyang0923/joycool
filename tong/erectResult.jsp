<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.erectResult(request);
String result=(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="申请结果">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(result!=null&&result.equals("success")){
String tongName=(String)request.getAttribute("tongName");
String tongId=(String)request.getAttribute("tongId");
if(tongName!=null){%>
您的"<%=StringUtil.toWml(tongName)%>"已经成立！！扣除100万乐币！<br/>
恭喜您成为<%=StringUtil.toWml(tongName)%>首任帮主！以后维护乐酷和平，就靠你啦！<br/>
<a href="/tong/tongManage.jsp?tongId=<%=tongId%>">管理帮会 </a><br/>
<a href="/tong/tong.jsp?tongId=<%=tongId%>">去看看<%=StringUtil.toWml(tongName)%></a><br/>
<%}
}else if(result!=null&&result.equals("userError")){%>
<%if(request.getAttribute("tip")!=null){%>
<%=request.getAttribute("tip")%><br/>
<%}else{%>
您没有资格建立帮会！<br/>
<%}}
else {%>
参数错误！<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>