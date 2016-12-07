<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.job.HuntAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
Calendar cal=Calendar.getInstance();
LuckBean luck=(LuckBean)session.getAttribute("luckBean");
//如果luck为空，返回运势主页
if(luck==null)
{
	//response.sendRedirect(("index.jsp"));
	BaseAction.sendRedirect("/job/luck/index.jsp", response);
	return;	
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="看今天运势" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(loginUser.getNickName())%> <%=loginUser.getAge()%>岁 <%=loginUser.getGender()==0?"女":"男"%><br/>
<%=cal.get(Calendar.MONTH)+1%>月<%=cal.get(Calendar.DAY_OF_MONTH)%>日运势：<br/>
综合运势：<%=luck.getSynthesis()%><br/>
爱情运势：<%=luck.getLove()%><br/>
工作状况：<%=luck.getJob()%><br/>
理财投资：<%=luck.getFinace()%><br/>
健康指数：<%=luck.getHealth()%><br/>
幸运色：<%=luck.getColor()%><br/>
幸运数字：<%=luck.getNum()%><br/>
今日速配星座：<%=luck.getMate()%><br/>
总评：<%=luck.getAppraise()%><br/>

<br/>
<a href="index.jsp">重新测一下</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<a href="http://wap.joycool.net">乐酷免费门户首页</a><br/>
</p>
</card>
</wml>