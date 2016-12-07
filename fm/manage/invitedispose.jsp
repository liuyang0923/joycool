<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.*"%><%@ page import="jc.family.*"%><%
FamilyAction fmAction=new FamilyAction(request,response);
FamilyNewManBean bean=fmAction.getfamilyOneInvite();
String backTo=fmAction.getParameterString("backTo");
if(backTo==null||"".equals(backTo)){
backTo="/fm/index.jsp";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族邀请"><p align="left"><%
if(bean==null){
out.println(fmAction.getTip()+"<br/>");
}else{
int fmid=fmAction.getParameterInt("fmid");
FamilyHomeBean fm = FamilyAction.getFmByID(fmid);
%><%=StringUtil.toWml(bean.getInvitename())%>邀请您加入家族【<%=StringUtil.toWml(fm.getFm_name())%>】,您是否接受?<br/>
<a href="invitedisposeresult.jsp?cmd=y&#38;fmid=<%=fmid%>">接受</a> <a href="invitedisposeresult.jsp?cmd=n&#38;fmid=<%=fmid%>&#38;backTo=<%=backTo.replace("&", "&amp;")%>">拒绝</a><br/>
===家族详情===<br/>
【<a href="/fm/myfamily.jsp?id=<%=fmid%>"><%=StringUtil.toWml(fm.getFm_name())%></a>】<br/>
家族简介:<%=StringUtil.toWml(fm.getInfo())%><br/>
成立于<%=DateUtil.formatDate1(fm.getFm_time())%>,现有成员<%=fm.getFm_member_num()%>人<br/>
<%}%>
<a href="<%=backTo.replace("&", "&amp;")%>">返回上一页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>