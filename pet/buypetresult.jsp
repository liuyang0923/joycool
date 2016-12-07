<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.pet.*;" %><%
response.setHeader("Cache-Control","no-cache");
PetAction action = new PetAction(request);
action.buyPetResult();
String result = (String)request.getAttribute("result");
String tip = (String)request.getAttribute("tip");
String index=("/pet/index.jsp");
String url=("/pet/rename.jsp");
String back=("/pet/info.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if("failure".equals(result)) {%>
<card title="宠物认养" ontimer="<%=response.encodeURL(back)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%>(3秒后跳转重命名)<br/>
<a href="<%=back%>">返回宠物大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refrush")){
out.clearBuffer();
response.sendRedirect("info.jsp");
return;
}else{%>
<card title="宠物认养" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
认养成功(3秒后跳转重命名)<br/>
<a href="<%=url%>">给宠物命名</a><br/>
<a href="<%=index%>">返回宠物资料</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>