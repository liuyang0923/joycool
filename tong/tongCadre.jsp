<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.PageUtil"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);
action.tongCadre(request);
String result =(String)request.getAttribute("result");
String url=("/tong/tongList.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="帮会列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/tong/tongList.jsp">帮会列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("error")){
TongBean tong=(TongBean)request.getAttribute("tong");
url=("/tong/nominateAssistant.jsp?tongId="+tong.getId());
%>
<card title="帮会任命" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/tong/nominateAssistant.jsp?tongId=<%=tong.getId()%>">帮会任命</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
TongBean tong=(TongBean)request.getAttribute("tong");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
List tongUserList=(List)request.getAttribute("tongUserList");
%>
<card title="任命帮会高层">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请任命帮会高层,现有<%=tong.getCadreCount()%>人(最多10人):<br/>
<%
Integer userId=null;
UserBean user=null;
TongUserBean tongUser=null;
for(int i=0;i<tongUserList.size();i++){
userId=(Integer)tongUserList.get(i);
user=(UserBean)UserInfoUtil.getUser(userId.intValue());
if(user==null)continue;
tongUser=TongCacheUtil.getTongUser(tong.getId(),userId.intValue());
if(tongUser==null)continue;%>
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a> 贡献<%=tongUser.getDonation()%> <a href="/tong/tongCadreJump.jsp?tongId=<%=tong.getId()%>&amp;userId=<%=user.getId()%>">任命</a><br/>
<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}%>
ID:<input name="assistUser" format="*N"/>
<anchor title="确定">任命
  <go href="/tong/tongCadreJump.jsp?tongId=<%=tong.getId()%>" method="post">
    <postfield name="userId" value="$assistUser"/>
  </go>
</anchor><br/>
<a href="/tong/nominateAssistant.jsp?tongId=<%=tong.getId()%>">帮会任命</a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理帮会 </a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>