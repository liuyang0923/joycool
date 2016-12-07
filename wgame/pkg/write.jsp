<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
action.write();
PkgBean pkg = (PkgBean)request.getAttribute("pkg");;
if(pkg==null){
	response.sendRedirect("my1.jsp");
	return;
}
PkgTypeBean type = (PkgTypeBean)action.getPkgType(pkg.getType());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=type.getName()%><br/>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{%>
请填写个性礼签:(收到礼包后就能看到,一般写祝贺词等内容,最多50字)<br/>
<input name="pkgt" maxlength="50" value=""/><br/>
请书写一封送给好友的书信(打开礼包后才能看到,最多1000字)<br/>
<input name="pkgc" maxlength="1000" value=""/><br/>
<anchor title="确定">确认提交
<go href="write.jsp?id=<%=pkg.getId()%>" method="post">
    <postfield name="title" value="$pkgt"/>
    <postfield name="content" value="$pkgc"/>
</go></anchor><br/>

<%}%><br/>
<%if(pkg!=null){%><a href="send.jsp?id=<%=pkg.getId()%>">返回</a><br/><%}%>
<a href="my1.jsp">返回我的礼包列表</a><br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>