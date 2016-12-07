<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.tong.TongAction"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongBean"%><%@ page import="net.joycool.wap.util.Constants"%><%
response.setHeader("Cache-Control","no-cache");
TongAction action = new TongAction(request);

String result = null;
String tip = null;
	TongBean tong = null;
	int tongId = action.getParameterInt("tongId");
	String orderBy = request.getParameter("orderBy");// 判断参数
	String prefixUrl = null;
	List tongUserList = null;
	PagingBean paging = null;
	if (tongId <= 0) {
		result = "failure";
		tip = "参数错误!";
	} else {
		tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
		} else {
			if ((orderBy == null) || (orderBy.equals(""))) {
				orderBy = "donation";
			}

			if (orderBy.equals("donation")) {
				tongUserList = TongCacheUtil.getTongUserListById(tongId);
				prefixUrl = "tongUserList.jsp?tongId=" + tong.getId()
						+ "&amp;orderBy=" + orderBy;
			} else {
				tongUserList = action.getOnlineTongUserList(tong.getId());
				prefixUrl = "tongUserList.jsp?tongId=" + tong.getId()
						+ "&amp;orderBy=" + orderBy;
			}
			paging = new PagingBean(action,tongUserList.size(),10,"p","go");
			tongUserList = tongUserList.subList(paging.getStartIndex(), paging.getEndIndex());
			
			result = "success";
		}
	}
	
		
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
String url=("/tong/tongList.jsp");
%>
<card title="城帮列表" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
%>
<card title="<%=StringUtil.toWml(tong.getTitle())%>帮">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(orderBy.equals("donation")){%>
<a href="/tong/tongUserList.jsp?tongId=<%=tong.getId()%>&amp;orderBy=online">按在线</a>|按贡献<br/>
<%}else{%>
按在线|<a href="/tong/tongUserList.jsp?tongId=<%=tong.getId()%>&amp;orderBy=donation">按贡献</a><br/>
<%}
if(tongUserList.size()>0){
Integer userId=null;
TongUserBean tongUser=null;
UserBean user = null ;
for(int i=0;i<tongUserList.size();i++){
userId=(Integer)tongUserList.get(i);
tongUser=action.getTongUser(tong.getId(),userId.intValue());
if(tongUser==null){continue;}
user= UserInfoUtil.getUser(tongUser.getUserId());
if(user==null){continue;}
if(tong.getUserId()==user.getId()){%>
<%=i+1 %>.<%if(user.getId()!=loginUser.getId()){%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><%}else{%><%=StringUtil.toWml(user.getNickName())%><%}%>
帮主(<%=tongUser.getDonation()%>点)<br/>
<%}else if(tong.getUserIdA()==user.getId() || tong.getUserIdB()==user.getId()){%>
<%=i+1 %>.<%if(user.getId()!=loginUser.getId()){%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><%}else{%><%=StringUtil.toWml(user.getNickName())%><%}%>
副帮主(<%=tongUser.getDonation()%>点)<br/>
<%}else{%>
<%=i+1 %>.<%if(user.getId()!=loginUser.getId()){%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a><%}else{%><%=StringUtil.toWml(user.getNickName())%><%}%>
<%=StringUtil.toWml(action.getTongTitle(tongUser))%>(<%=tongUser.getDonation()%>点)
<%if(tong.getUserIdA()==loginUser.getId() || tong.getUserIdB()==loginUser.getId()|| tong.getUserId()==loginUser.getId()){%>
<a href="/tong/tongExpelC.jsp?tongId=<%=tong.getId()%>&amp;userId=<%=user.getId()%>">开除</a>
<%}%>
<br/>
<%}}%><%=paging.shuzifenye(prefixUrl,true,"|",response)%><%}else{%>没有查询到结果记录<br/><%}%>
<%if(paging.getTotalPageCount()>5){%>
跳到<input name="index"  maxlength="5" format="*N" value="1"/>页
<anchor title="确定">GO
  <go href="<%=(prefixUrl)%>" method="post">
    <postfield name="go" value="$index"/>
  </go>
</anchor><br/><%}%>
<%if(tong.getUserIdA()==loginUser.getId() || tong.getUserIdB()==loginUser.getId()|| tong.getUserId()==loginUser.getId()){%>
ID:<input name="expelUser" format="*N"/>
<anchor title="确定">开除
  <go href="/tong/tongExpelC.jsp?tongId=<%=tong.getId()%>" method="post">
    <postfield name="userId" value="$expelUser"/>
  </go>
</anchor><br/><%}%>
<a href="/tong/tongChat.jsp?tongId=<%=tong.getId()%>">返回帮会聊天室</a><br/>
<a href="/tong/tongNotice.jsp?tongId=<%=tong.getId()%>">返回公告</a><br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>