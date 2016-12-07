<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.question.QuestionAction"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.question.eum"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
//取得用户信息的方法
QuestionAction action = new QuestionAction(request);
//判断是不是需要判断对错
List list = action.getTotalList(request);

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="龙榜历史大排名">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您目前的排名是:<%=action.getOrderTotal("totalValue")%><br/>
名次   姓名     级别     闯关数<br/>
<%for(int i=0;i<10;i++) { %>
<%if(action.page*10+i < list.size()) {%>
第<%=action.page*10+i+1%>名
<%if(UserInfoUtil.getUser(((eum)list.get(action.page*10+i)).getId()) != null)  {%>
<a href="/user/ViewUserInfo.do?userId=<%=((eum)list.get(action.page*10+i)).getId()%>">
<%=StringUtil.toWml(UserInfoUtil.getUser(((eum)list.get(action.page*10+i)).getId()).getNickName())%>
</a>

    <%=action.getEum(  (eum)list.get(action.page*10+i),action.page*10+i+1)%>
    <%}%>
    <br/>
<%}%>
<%}%>

<%if((action.page > 0)&&(action.page <= list.size()/10)){%>
<a href="/question/historyTotal.jsp?topage=<%=action.getprepage()%>">上一页</a>
<%}%>
<%if((action.page < list.size()/10)&&(action.page < 99)){%>
<a href="/question/historyTotal.jsp?topage=<%=action.getnexpage()%>">下一页</a><br/>
<%}%>
<br/>
<a href="/question/index.jsp">问答接龙</a><br/>
<a href="/lswjs/gameIndex.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>