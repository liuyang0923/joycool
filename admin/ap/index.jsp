<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.admin.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*" %><%!
%><%response.setHeader("Cache-Control","no-cache");
ApplyAction action = new ApplyAction(request);
net.joycool.wap.bean.UserBean loginUser = action.getLoginUser();
int userId = (loginUser==null?0:loginUser.getId());
UserApplyBean latest = null;
if(userId!=0)
	latest = action.getLatestUserApply(userId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="申诉站">
<p align="left">
<%if(latest!=null){%>
你正在申诉的内容为:<br/>
【<%=latest.getTypeName()%>】<br/>
<%=StringUtil.toWml(latest.getContent())%><br/>
请耐心等待管理员处理.<br/>
<%}else{%>
是否遇到了无法解决的问题?在这里可以发起申诉,管理员会尽快帮你解决.<br/>
请根据你的情况,选择相应的申诉类型<br/>
1.<a href="a1.jsp">找回登陆密码</a><br/>
2.<a href="a2.jsp">找回银行密码</a><br/>
3.<a href="a3.jsp">被盗号要求取回</a><br/>
4.<a href="a4.jsp">举报骗子/小偷</a><br/>
5.<a href="a5.jsp">举报挂机行为/游戏漏洞</a><br/>
6.<a href="a6.jsp">投诉版主/监察/管理</a><br/>
7.<a href="a7.jsp">举报色情/违法等不良信息</a><br/>
<%}%>
<a href="/jcforum/forum.jsp?forumId=4687">&gt;&gt;我要申诉其他问题</a><br/>
<a href="/user/onlineManager.jsp?forumId=355">返回乐酷警察局</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>