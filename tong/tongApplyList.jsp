<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongApplyBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action= new TongAction(request);
action.tongApplyList(request);
String result =(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="帮会列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
Vector tongApplyList=(Vector)request.getAttribute("tongApplyList");
String totalCount=(String)request.getAttribute("totalCount");
String tongUserCount=(String)request.getAttribute("tongUserCount");
TongBean tong=(TongBean)request.getAttribute("tong");
%>
<card title="帮会管理">
<p align="left">
<%=BaseAction.getTop(request, response)%>
有<%=totalCount%>个待处理申请，您帮会现有<%=tongUserCount%>人：<br/>
<%if(tongApplyList.size()>0){
TongApplyBean tongApply=null;
UserBean user = null;
UserStatusBean userStatus = null;
for(int i=0;i<tongApplyList.size();i++){
tongApply=(TongApplyBean)tongApplyList.get(i);
user=UserInfoUtil.getUser(tongApply.getUserId());
if(user==null){continue;}
userStatus=UserInfoUtil.getUserStatus(tongApply.getUserId());
if(userStatus==null){continue;}
%>
<%=i+1%>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>">
<%=StringUtil.toWml(user.getNickName())%></a>
<%=userStatus.getRank()%>级
<%=user.getGender()==0?"女":"男"%>
<%if(user.getCityname()==null || user.getCityname().replace(" ","").equals("")){}else{%>
<%=user.getCityname()%>
<%}%>
<a href="/tong/tongApplyInfo.jsp?applyId=<%=tongApply.getId()%>">申请书</a><br/>
<%}String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}}else{%>没有查询到结果记录<br/><%}%>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市</a><br/>
<a href="/tong/tongManage.jsp?tongId=<%=tong.getId()%>">返回管理</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>