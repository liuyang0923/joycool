<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%!
static HashMap titles=new HashMap();
static{
titles.put(new Integer(0),"桃花源恶人榜");
titles.put(new Integer(49),"灵川初级擂台大排名");
titles.put(new Integer(48),"灵川中级擂台大排名");
titles.put(new Integer(47),"灵川高级擂台大排名");
titles.put(new Integer(149),"浔州顶级擂台大排名");
}
static String[] orders = {"honor_week","honor_last","honor"};
%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
int id = action.getParameterInt("id");
int option=action.getParameterInt("o");
if(id>0&&(id>49||id<47)&&id!=149) id=0;
if(option<0||option>2) option=0;
List list = SqlUtil.getIntList("select user_id from farm_user_honor where arena=" + id + " and "+orders[option]+">0 order by " + orders[option] + " desc limit 10", 4);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
%>
==<%=titles.get(Integer.valueOf(id))%>==<br/>
<%if(option==0){%>+本周<%}else{%><a href="arena.jsp?o=0&amp;id=<%=id%>">本周</a><%}%>
/<%if(option==1){%>+上周<%}else{%><a href="arena.jsp?o=1&amp;id=<%=id%>">上周</a><%}%>
/<%if(option==2){%>+历史<%}else{%><a href="arena.jsp?o=2&amp;id=<%=id%>">历史</a><%}%><br/>
<%if(list.size()>0){
for(int i=0;i<list.size();i++){
Integer iid = (Integer)list.get(i);
FarmUserBean user = FarmWorld.one.getFarmUserCache(iid);
if(user==null){
%>(未知)<%}else{%>
<a href="../user/info.jsp?id=<%=user.getUserId()%>"><%=user.getNameWml()%></a>/战斗<%=user.getProRank(9)%>级/<%=user.getClass1Name()%><%}%><br/>
<%}%>
<%}else{%>
(无)<br/>
<%}%>
<%}%>
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>