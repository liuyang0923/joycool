<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%
FamilyAction fmAction=new FamilyAction(request,response);
int id=fmAction.getParameterInt("id");
if(id==0){
response.sendRedirect("/fm/index.jsp");return;
}
String cmd=request.getParameter("cmd");
FamilyUserBean fmUser=null;
if(cmd!=null&&cmd.equals("s")){
fmUser=fmAction.getUserBan();
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="添加封禁成员"><p align="left"><%=BaseAction.getTop(request, response)%>
请输入要封禁在成员ID<br/>
<input name="uid" format="*N" maxlength="9"/>
<anchor title="搜索">
搜索
      <go href="gamemgt.jsp?id=<%=id%>&#38;cmd=s" method="post">
        <postfield name="uid" value="$(uid)" />
      </go>
    </anchor><br/>
<%if(fmUser!=null){%>
<%=fmUser.getNickNameWml()%>-<%=net.joycool.wap.util.StringUtil.toWml(fmUser.getFm_name())%><br/>
请选择要封禁的游戏:<br/>
<a href="gamemgt.jsp?id=<%=id%>&#38;uid=<%=fmUser.getId()%>&#38;game=ask&#38;cmd=s">家族问答</a>&#160;(<%=fmUser.isBlocked_ask(fmUser.getFm_state())?"解封":"封禁" %>)<br/>
<a href="gamemgt.jsp?id=<%=id%>&#38;uid=<%=fmUser.getId()%>&#38;game=boat&#38;cmd=s">赛龙舟</a>&#160;(<%=fmUser.isBlocked_boat(fmUser.getFm_state())?"解封":"封禁" %>)<br/>
<a href="gamemgt.jsp?id=<%=id%>&#38;uid=<%=fmUser.getId()%>&#38;game=snow&#38;cmd=s">打雪仗</a>&#160;(<%=fmUser.isBlocked_snow(fmUser.getFm_state())?"解封":"封禁" %>)<br/>
<%}else{
if(cmd!=null&&cmd.equals("s")){%>
<%=fmAction.getTip()%><br/>
<%}}%>
<a href="game.jsp?id=<%=id%>">返回游戏管理</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>