<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.tong.TongTransferBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.transferReceiveNotice(request);
String result=(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if("userError".equals(result)){%>
<card title="帮主转让" ontimer="<%=response.encodeURL("/tong/tongList.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=(String)request.getAttribute("tip")%>(3秒后跳转到帮会页面）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong= (TongBean)request.getAttribute("tong");
UserBean user = UserInfoUtil.getUser(tong.getUserId());
TongTransferBean tongTransfer= (TongTransferBean)request.getAttribute("tongTransfer");
%>
<card title="帮主转让">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(tong.getTitle())%>帮帮主<%=StringUtil.toWml(user.getNickName())%>决定将帮会转让给您。<br/>
<%
//转让本帮兄弟
if(tongTransfer.getMark()==TongTransferBean.TONG_USER){%>
<anchor title="确定">接受
  <go href="transferResult.jsp" method="post">
    <postfield name="tongId" value="<%=tongTransfer.getTongId()%>"/>
  </go>
</anchor>
<a href="/tong/tongList.jsp">拒绝</a><br/>
<%}
//转让其他帮主
else if(tongTransfer.getMark()==TongTransferBean.TONG_OTHER_USER){%>
<anchor title="确定">接受
  <go href="transferResult.jsp" method="post">
    <postfield name="tongId" value="<%=tongTransfer.getTongId()%>"/>
  </go>
</anchor>
<a href="/tong/tongList.jsp">拒绝</a><br/>
<%}
//错误
else{
}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>