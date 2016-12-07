<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.List,net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction fmAction=new FamilyAction(request,response);
int id=fmAction.getParameterInt("id");
if(id==0){
response.sendRedirect("index.jsp");return;
}
FamilyHomeBean fmhome=fmAction.getFmByID(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游戏经验值"><p align="left"><%=BaseAction.getTop(request, response)%>
当前游戏经验:<%=fmhome.getGame_num()%><br/>
参加一场游戏且满足一定的条件才能获得一点经验加成,所以成绩不好的队伍只要尽力了也是能拿到经验点的哦,所以加油吧!<br/>
<a href="myfamily.jsp?id=<%=id %>">返回<%=fmhome.getFm_nameWml()%>家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>