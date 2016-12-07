<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
boolean result=action.createFmHomeapply();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="申请建立家族"><p align="left"><%
if(result){
Integer fhid=(Integer)request.getAttribute("applyId");
String fhname=(String)request.getAttribute("fhname");
%>恭喜您创建了家族<%=StringUtil.toWml(fhname)%>,但是一个人的家族有什么意思,邀请几位亲朋好友一起来玩吧,至少要成功邀请4位朋友一起建立家族才能真正成功哦!<br/>
<a href="friendlist.jsp?applyId=<%=fhid%>">邀请好友共同申请</a><br/>
您能免费邀请10个有效的好友,超过10位,邀请一位有效的需要1千万乐币!<br/><%
}else{
%><%=action.getTip()%><br/>
请输入家族名称(不得超过6个字)<br/>
<input name="fname" maxlength="6" /><br/>
<anchor title="确定">确定
  <go href="tobuild.jsp" method="post">
    <postfield name="fname" value="$(fname)" />
  </go>
</anchor><br/><%
}%><a href="index.jsp">返回家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>