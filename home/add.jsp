<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.home.*,net.joycool.wap.service.impl.*,net.joycool.wap.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean,net.joycool.wap.cache.util.HomeCacheUtil,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendMarriageBean" %>
<%! static HomeServiceImpl homeServiceImpl = new HomeServiceImpl(); %>
<%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
String tip = "";
int backFlag = 0;
List list = null;
int type = action.getParameterInt("t");	
int catId = action.getParameterInt("cid");
int submit = action.getParameterInt("s");
HomeDiaryCat cat = null;
if (catId == 0){
	cat = new HomeDiaryCat();
	cat.setCatName("默认分类");
	cat.setUid(action.getLoginUser().getId());
} else {
	cat = homeServiceImpl.getHomeDiaryCat("id=" + catId);	
}
if (cat == null || cat.getUid() != action.getLoginUser().getId()){
	tip = "分类不存在或不是你建立的分类.";	
} else {
	if (submit == 1){
		session.setAttribute("diaryList",new ArrayList());
		action.diaryList(request);
		if ("addSuccess".equals(request.getAttribute("result"))){
			tip = (String)request.getAttribute("tip");
		}
	}
	list = homeServiceImpl.getHomeDiaryCatList(action.getLoginUser().getId() , HomeDiaryCat.PRIVACY_SELF);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="添加"><p align="left">
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
%>标题(25字内):<br/><input name="title"  maxlength="25" value=""/><br/>
内容(1000字内):<br/><input name="content"  maxlength="1000" value=""/><br/>
分类:<br/><select name="cid" value="<%=catId%>">
<option value="0">默认分类</option>
<%for(int i = 0; i < list.size();i++) {
cat = (HomeDiaryCat)list.get(i);%>
<option value="<%=cat.getId() %>" ><%=StringUtil.toWml(cat.getCatName()) %></option>
<%} %></select><br/>
<anchor title="确定">添加日记
    <go href="add.jsp?s=1" method="post">
    <postfield name="cid" value="$cid"/>
    <postfield name="title" value="$title"/>
    <postfield name="userId" value="<%=action.getLoginUser().getId()%>"/>
    <postfield name="content" value="$content"/>
    <postfield name="reviewUserId" value="<%=action.getLoginUser().getId()%>"/>
    </go>
</anchor><br/>
<a href="homeDiaryList.jsp?cid=<%=catId==0?0:cat.getId()%>">返回<%=catId == 0?"默认分类":StringUtil.toWml(cat.getCatName())%></a><br/>
<%
} else {
%><%=tip%><br/><%
if (backFlag == 0){
// 一般性返回
%><a href="homeDiaryList.jsp?cid=<%=catId%>">返回</a><br/><%
}
}
%>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>