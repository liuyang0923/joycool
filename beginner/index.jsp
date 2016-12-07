<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.beginner.BeginnerQuestionAction" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.Constants" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser==null){
	response.sendRedirect("/user/login.jsp");
	return;
}
BeginnerQuestionAction action = new BeginnerQuestionAction(request);
action.index(request); 
//取得登陆用户信息
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List beginnerHelpOnlineList=(List)request.getAttribute("beginnerHelpOnlineList");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷">
<p align="left">
<img src="../img/beginner.gif" alt="新手区"/><br/>
<%=BaseAction.getTop(request, response)%>
乐酷向导冲你眨眼道：欢迎光临，新手区会帮助你迅速爱上乐酷的。<br/>
你可以向“在线热心用户”请教问题，也可以看“常见问题解惑”，还可以玩专门给新手提供的简单游戏赚钱。怎么样，开始吧？<br/>
＝在线热心用户＝<br/>
<%
String userId=null;
UserBean user=null;
for(int i=0;i<beginnerHelpOnlineList.size();i++){
userId=(String)beginnerHelpOnlineList.get(i);
user=UserInfoUtil.getUser(StringUtil.toInt(userId));
if(user==null)continue;
if(user.getId()==loginUser.getId()){%>
<%=i+1%>.您自己<br/>
<%}else{%>
<%=i+1%>.<a href="/chat/post.jsp?toUserId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><br/>
<%}
}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
＝新手游戏＝<br/>
<a href="/beginner/question.jsp">新手趣味问答发大财</a><br/>
＝常见问题＝<br/>
<a href="/Column.do?columnId=5667">乐酷站是什么？</a><br/>
<a href="/Column.do?columnId=6121">乐酷收费吗？</a><br/>
<a href="/Column.do?columnId=6126">如何修改个人资料</a><br/>
<a href="/Column.do?columnId=5671">怎样升级？</a><br/>
<a href="/Column.do?columnId=6269">新手怎样赚钱？</a><br/>
<a href="/Column.do?columnId=5596">》更多答疑</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>