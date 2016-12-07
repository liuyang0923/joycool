<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.tong.TongAssistantBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.PageUtil" %><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.assistantList(request);
TongBean tong=(TongBean)request.getAttribute("tong");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(tong!=null){
String result=(String)request.getAttribute("result");
if(result.equals("failure")){%>
<card title="任命副帮主" ontimer="<%=response.encodeURL("/tong/nominateAssistant.jsp?tongId="+tong.getId())%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector tongList=(Vector)request.getAttribute("tongList");%>
<card title="任命副帮主">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请任命你的副帮主：<br/>
<%if(tongList!=null){
for(int i=0;i<tongList.size();i++){
TongAssistantBean bean=(TongAssistantBean)tongList.get(i);
TongUserBean tongUser=action.getTongUser(tong.getId(),bean.getUserId());
if(tongUser==null)continue;
if(bean!=null){
UserBean user=(UserBean)UserInfoUtil.getUser(bean.getUserId());
if(user!=null){
%>
<%=StringUtil.toWml(user.getNickName())%> <%=bean.getRank()%>级 贡献<%=bean.getDonation()%> <%=StringUtil.toWml(action.getTongTitle(tongUser))%> <a href="/tong/nominateResult.jsp?tongId=<%=tong.getId()%>&amp;add=<%=bean.getUserId()%>">任命</a><br/>
<%  }
   }
 }
}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
ID:<input name="assistUser" format="*N"/>
<anchor title="确定">任命
  <go href="/tong/nominateResult.jsp?tongId=<%=tong.getId()%>" method="post">
    <postfield name="add" value="$assistUser"/>
  </go>
</anchor><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a> <a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理 </a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
<%}else{%>
<card title="任命副帮主">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您不能任命助手！<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>