<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="java.util.List"%><%response.setHeader("Cache-Control","no-cache");
TongAction action=new TongAction(request);
action.nominateAssistant(request);
TongBean tong=(TongBean)request.getAttribute("tong");
List tongUserList=(List)request.getAttribute("tongUserList");
String url=("/tong/tongList.jsp");
//TongUserBean userB=(TongUserBean)request.getAttribute("userB");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="任命助手">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tong!=null){%>
副帮主将会成为你的得力助手。副帮主只能任命2名，等级大于10。他们的权限小于你，只有：管理会员、取走帮会基金、设定帮会通知、论坛管理、聊天室管理、设定税收和取商店道具功能。<br/>

你现在的副帮主是：<br/>
<%if(tong.getUserIdA()>-1){
TongUserBean userA=TongCacheUtil.getTongUser(tong.getId(),tong.getUserIdA());
UserBean user=(UserBean)UserInfoUtil.getUser(tong.getUserIdA());
if(user!=null&& userA!=null){%>
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a> 贡献<%=userA.getDonation()%> <a href="/tong/nominateResult.jsp?tongId=<%=tong.getId()%>&amp;del=<%=userA.getUserId()%>">废除 </a><br/>
<%}
}if(tong.getUserIdB()>-1){
TongUserBean userB=TongCacheUtil.getTongUser(tong.getId(),tong.getUserIdB());
UserBean user=(UserBean)UserInfoUtil.getUser(tong.getUserIdB());
if(user!=null&&userB!=null){%>
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a> 贡献<%=userB.getDonation()%> <a href="/tong/nominateResult.jsp?tongId=<%=tong.getId()%>&amp;del=<%=userB.getUserId()%>">废除 </a><br/>
<%}
}%>
<a href="/tong/assistantList.jsp?tongId=<%=tong.getId()%>">任命副帮主 </a><br/>
帮会高层:现有<%=tong.getCadreCount()%>人(最多10人)<br/>
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
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a> 
<%=StringUtil.toWml(action.getTongTitle(tongUser))%>
<a href="/tong/tongCadreDelete.jsp?tongId=<%=tong.getId()%>&amp;userId=<%=user.getId()%>">废除</a><br/>
<%}%>
<a href="/tong/tongCadre.jsp?tongId=<%=tong.getId()%>">任命帮会高层 </a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理帮会 </a> <a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市 </a><br/>
<%
}else{%>
您不能任命助手！<br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>