<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.user.*"%>
<% response.setHeader("Cache-Control","no-cache");
UserAction2 action = new UserAction2(request);
int type = action.getParameterInt("t");
String backTo = action.getParameterNoCtrl("backTo");
if (backTo == null || "".equals(backTo)){backTo = BaseAction.INDEX_URL;}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="初级用户注册" ontimer="<%=response.encodeURL(backTo)%>"><timer value="30"/><p>
<%=BaseAction.getTop(request, response)%>
<% switch (type){
case (0):{
%>页面跳转错误!(3秒钟返回进入页)<a href="<%=backTo%>">直接跳转</a><br/><br/><%
break;
}
case (1):{
%>恭喜您修改成功!(3秒钟返回进入页)<a href="<%=backTo%>">直接跳转</a><br/>
<a href="/guest/user/findpw.jsp">我想升级成免费高级会员</a><br/><%
break;
}
case (2):{
%>恭喜您登陆成功!(3秒钟返回进入页)<a href="<%=backTo%>">直接跳转</a><br/>
<a href="/guest/user/findpw.jsp">免费升级正式用户更多精彩</a><br/><%
break;
}
}
%>
<%=BaseAction.getBottomShort(request, response)%>
</p></card></wml>