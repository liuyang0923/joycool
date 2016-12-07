<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
int id=action.getParameterInt("id");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left"><%=BaseAction.getTop(request, response)%><%
List list=action.vsService.getVsExploitDetailList("b.id="+id+" order by a.id");

if(list==null||list.size()<2){
%>挑战信息不存在<br/><%
}else{
VsExploits bean = (VsExploits)list.get(0);	// 挑战方
VsExploits bean2 = (VsExploits)list.get(1);	// 迎战方
%><%=bean.toString2()%><br/>
<%=net.joycool.wap.util.DateUtil.formatDate2(bean.getcTime())%><br/>
挑战家族:<a href="/fm/myfamily.jsp?id=<%=bean.getFmA()%>"><%=bean.getFmANameWml()%></a><br/>
参与人数:<%=bean.getUserCount()%>人<br/>
应战家族:<a href="/fm/myfamily.jsp?id=<%=bean.getFmB()%>"><%=bean.getFmBNameWml()%></a><br/>
参与人数:<%=bean.getUserCount()%>人<br/>
<%if(bean.getScore()>0){%>积分变化:<%=bean.getScore()%><br/><%}
%><%
if(bean.getUserA()!=0){
%>发起者:<a href="/user/ViewUserInfo.do?userId=<%=bean.getUserA()%>"><%=bean.getUserANickNameWml()%></a><br/>
应战者:<a href="/user/ViewUserInfo.do?userId=<%=bean.getUserB()%>"><%=bean.getUserBNickNameWml()%></a><br/>
<%
}
VsGameBean vsbean=action.getVsGameById(bean.getId());
if(vsbean!=null){
%><a href="<%=vsbean.getGameUrl()%>?id=<%=vsbean.getId()%>">比赛详细信息</a><br/><%
}
}%><a href="nowvs.jsp?a=1">返回挑战赛列表</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>