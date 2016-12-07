<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.*"%><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族基金"><p align="left"><%=BaseAction.getTop(request, response)%>
家族目前资金<%=action.getFmFund(fmLoginUser.getFm_id())%>乐币<br/>
其中您取走的累计:<%=fmLoginUser.getFm_money_used()%><br/>
提取基金:<br/>
<input name="getfund" format="*N" maxlength="10"/><br/>
<anchor title="提取">
确定<go href="refund.jsp" method="post">
        <postfield name="getfund" value="$(getfund)" />
      </go>
    </anchor><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>