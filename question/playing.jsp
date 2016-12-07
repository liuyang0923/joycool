<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.ForbidUtil"%><%@ page import="net.joycool.wap.bean.question.QuestionWareHouseBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.question.QuestionAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction" %><%
//禁止使用页面缓存
response.setHeader("Cache-Control","no-cache");
/*if(true){
response.sendRedirect("index.jsp");
return;
}*/
//取得用户信息的方法
QuestionAction action = new QuestionAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(ForbidUtil.isForbid("game",loginUser.getId())){
	response.sendRedirect("/bottom.jsp");
	return;
}
//判断是不是需要判断对错
int position = 0;
	int x = action.getParameterInt("id");
	int y  = 0 ;
{
	QuestionWareHouseBean questionWareHouse = (QuestionWareHouseBean)session.getAttribute("questionWareHouse");
	if(questionWareHouse != null) {
		y = questionWareHouse.getId();
	}

	if (y == -1)
		request.setAttribute("questionerror", "error");

	if ((request.getParameter("rs") != null) && action.isCooldown("chat", 2000)
			&& x - y>=0 && x - y < 5)// 防止用户刷新
	{
		int temp = 0;
		temp = action.result(request, response,y);
		action.setPage();
		session.removeAttribute("questionWareHouse");
		position = temp;
	} else {
		action.getLevle();
		action.setPage();
	}
}

//判断是否出错需要跳转
if(request.getAttribute("questionerror") != null)
{
    BaseAction.sendRedirect("/question/index.jsp",response);
	return;
}

//读取已经答对的题数
int winCount=0;
if(session.getAttribute("winCount") != null)
     winCount=Integer.parseInt(session.getAttribute("winCount").toString());
else
      session.setAttribute("winCount",new Integer(winCount));
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
//随机取得一道问答题目
if(session.getAttribute("questionWareHouse") == null)
	action.question(request);

//获取一条随机生成的题目
QuestionWareHouseBean wareHouse=(QuestionWareHouseBean)session.getAttribute("questionWareHouse");
if(wareHouse == null){
    BaseAction.sendRedirect("/question/index.jsp", response);
	return;
}
int qid = wareHouse.getId() + RandomUtil.nextInt(5);
//取得登陆用户信息 不能动啊
UserStatusBean status=UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="智力问答">
<p align="left">
<%=BaseAction.getTop(request, response)%><%--头信息--%>


<%//过关信息提示
if(position != 0)
{%>
<%=request.getAttribute("msg_1")%><br/>
<%=request.getAttribute("msg_2")%><br/>
<%}%>

<%if(request.getAttribute("msg_4") != null)
{%>
<%=request.getAttribute("msg_4")%><br/>
<%}%>

<%--当前关提示--%>
第<%=winCount+1%>关


<%--显示随机题目和答案选项--%>
<%=wareHouse.getName()%><br/>
<%
int rnd10=net.joycool.wap.util.RandomUtil.seqInt(10);
String addurl = "&amp;"+rnd10;
if(rnd10%3==0){
if(rnd10%2==0){
%>
A:<a href="playing.jsp?id=<%=qid%>&amp;rs=2<%=addurl%>"><%=wareHouse.getKey2()%></a><br/>
B:<a href="playing.jsp?id=<%=qid%>&amp;rs=1<%=addurl%>"><%=wareHouse.getKey1()%></a><br/>
<%}else{%>
A:<a href="playing.jsp?id=<%=qid%>&amp;rs=1<%=addurl%>"><%=wareHouse.getKey1()%></a><br/>
B:<a href="playing.jsp?id=<%=qid%>&amp;rs=2<%=addurl%>"><%=wareHouse.getKey2()%></a><br/>
<%}%>
<%if(!wareHouse.getKey3().equals("")){%>
C:<a href="playing.jsp?id=<%=qid%>&amp;rs=3<%=addurl%>"><%=wareHouse.getKey3()%></a><br/>
<%}%>
<%if(!wareHouse.getKey4().equals("")){%>
D:<a href="playing.jsp?id=<%=qid%>&amp;rs=4<%=addurl%>"><%=wareHouse.getKey4()%></a><br/>
<%}%>
<%}else{
	char ans = 'A';
%>


<%if(!wareHouse.getKey3().equals("")){%>
<%=ans++%>:<a href="playing.jsp?id=<%=qid%>&amp;rs=3<%=addurl%>"><%=wareHouse.getKey3()%></a><br/>
<%}%>
<%if(!wareHouse.getKey4().equals("")){%>
<%=ans++%>:<a href="playing.jsp?id=<%=qid%>&amp;rs=4<%=addurl%>"><%=wareHouse.getKey4()%></a><br/>
<%}%>
<%=ans++%>:<a href="playing.jsp?id=<%=qid%>&amp;rs=1<%=addurl%>"><%=wareHouse.getKey1()%></a><br/>
<%=ans++%>:<a href="playing.jsp?id=<%=qid%>&amp;rs=2<%=addurl%>"><%=wareHouse.getKey2()%></a><br/>

<%}%>
<br/>

<img src="../img/questions/<%=(String)request.getAttribute("msgPage")%>" alt="问答"/>
<%=(String)request.getAttribute("msg_3")%><br/>


你有<%=status.getGamePoint()%>乐币,经验<%=status.getPoint()%><br/>
<a href="/lswjs/gameIndex.jsp">返回娱乐城首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
