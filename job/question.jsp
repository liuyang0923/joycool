<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.JobWareHouseBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.JobAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction" %><%
response.setHeader("Cache-Control","no-cache");
session.setAttribute("question","ok");
int winCount=0;
//读取连赢次数
if(session.getAttribute("winCount") != null){
     String count=session.getAttribute("winCount").toString();
     winCount=Integer.parseInt(count);
} 
JobAction action = new JobAction(request);
action.question(request);
//取得登陆用户信息
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean status;
if(loginUser!=null)
	status=UserInfoUtil.getUserStatus(loginUser.getId());
else
	status = new UserStatusBean();

//获取一条随机生成的题目
JobWareHouseBean wareHouse=(JobWareHouseBean)request.getAttribute("jobWareHouse");
if(wareHouse == null){
	//response.sendRedirect(("http://wap.joycool.net"));
	BaseAction.sendRedirect(null, response);
	return;
}
String url="/job/question.jsp";
int questionCount=StringUtil.toInt((String)session.getAttribute("questionCount"));
if(questionCount<0)
questionCount=0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="智力问答" onenterbackward="<%=response.encodeURL(url)%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//判断如果连赢次数大于1,显示连赢次数
if(winCount>1){%>
您已经连赢<%=winCount%>次,继续加油!<br/>
<%}
if(session.getAttribute("win") != null){
    
UserInfoUtil.updateUserStatus("game_point=game_point+"+winCount*1000, "user_id="
						+ loginUser.getId(), loginUser.getId(), UserCashAction.WAGER,
						"打工赚钱答对"+winCount+"道题,送乐币"+winCount/10+"万");%>
您已经连赢<%=winCount%>次,获乐币<%=winCount/10%>万!<br/>
<%session.removeAttribute("win");
}
%>
<%//显示随机题目和答案选项%>
<%=wareHouse.getName()%><br/>
A:<a href="/job/result.jsp?id=<%=wareHouse.getId()%>&amp;rs=1&amp;questionCount=<%=questionCount%>"><%=wareHouse.getKey1()%></a><br/>
B:<a href="/job/result.jsp?id=<%=wareHouse.getId()%>&amp;rs=2&amp;questionCount=<%=questionCount%>"><%=wareHouse.getKey2()%></a><br/>
<%if(!wareHouse.getKey3().equals("")){%>
C:<a href="/job/result.jsp?id=<%=wareHouse.getId()%>&amp;rs=3&amp;questionCount=<%=questionCount%>"><%=wareHouse.getKey3()%></a><br/>
<%}%>
<%if(!wareHouse.getKey4().equals("")){%>
D:<a href="/job/result.jsp?id=<%=wareHouse.getId()%>&amp;rs=4&amp;questionCount=<%=questionCount%>"><%=wareHouse.getKey4()%></a><br/>
<%}%>
<%if(!wareHouse.getKey5().equals("")){%>
E:<a href="/job/result.jsp?id=<%=wareHouse.getId()%>&amp;rs=5&amp;questionCount=<%=questionCount %>"><%=wareHouse.getKey5()%></a><br/>
<%}%>
<br/>
你现有乐币<%=status.getGamePoint()%><br/>
<a href="/job/mindex.jsp">听歌猜名</a><br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>
