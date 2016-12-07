<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.HashMap" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.job.HandbookingerAction" %><%@ page import="net.joycool.wap.util.LoadResource" %><%@ page import="net.joycool.wap.bean.job.HandbookingerBean" %><%@ page import="net.joycool.wap.util.RandomUtil" %><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("jump")==null)
{//response.sendRedirect("/job/handbookinger/index.jsp");
BaseAction.sendRedirect("/job/handbookinger/index.jsp", response);
 return;
 }
HandbookingerAction action=new HandbookingerAction(request);
action.result(request);%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="结果页面" ontimer="<%=response.encodeURL("/job/handbookinger/index.jsp")%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%int winId=StringUtil.toInt((String)request.getAttribute("winId"));//获胜
  int horseId=StringUtil.toInt((String)request.getAttribute("horseId"));//获胜;//本人买的马
  int money=StringUtil.toInt((String)request.getAttribute("money"));//获胜钱
  Vector handbookingerList=(Vector)session.getAttribute("compensateList");
  int id=StringUtil.toInt((String)request.getAttribute("id"));//
  if("1".equals((String)request.getAttribute("win"))){%>
  <%=id%>号赛马<%=LoadResource.getHorse().get(request.getParameter("horseId"))%>一路领先获得了冠军，恭喜您赢得了<%=money%>乐币！！(5秒后自动跳转跑马首页)<br/>
  <%}
  else{
    int arrayIndex=0;
    for(int ii=0;ii<8;ii++)
    {
    HandbookingerBean handbookinger=(HandbookingerBean)handbookingerList.get(ii);
    if(handbookinger!=null && handbookinger.getId()==winId)
      {arrayIndex=ii;break;}
    }
     int array[]=new int[8];
     array=(int[])session.getAttribute("array");
     if((array[arrayIndex]+"").equals((String)request.getParameter("horseId")))
    { arrayIndex=arrayIndex+1;
      if(arrayIndex==8)
          arrayIndex=0;
    }
     %>
    <%=arrayIndex+1%>号赛马<%=LoadResource.getHorse().get(array[arrayIndex]+"")%>后来居上获得了冠军，
    <%String desc=action.getRandomResult(RandomUtil.nextInt(7));
    desc=desc.replace("horse",(String)LoadResource.getHorse().get(request.getParameter("horseId")));
    %>
   <%=desc%>(5秒后自动跳转跑马首页)<br/>
    <%}%>
<a href="/job/handbookinger/index.jsp">再买一场</a><br/>
<a href="/lswjs/wagerHall.jsp">游戏首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>