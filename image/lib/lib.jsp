<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.imglib.*"%>
<%! static int COUNT_PRE_PAGE = 15;static String[] privacy = {"","仅自己可见","仅我的好友可见","所有人可见"};%>
<% response.setHeader("Cache-Control","no-cache");
   ImgLibAction action = new ImgLibAction(request);
   if (action.getLoginUser() == null){
	   // 没登陆,返回登陆页面
	   response.sendRedirect("/user/login.jsp");
	   return;
   }
   LibUser libUser = null;
   UserBean user = null;
   List list = null;
   PagingBean paging = null;
   Lib lib = null;
   boolean isAffirmDel = false;
   String tip = "";
   int userId = action.getParameterInt("uid");
   int pid = action.getParameterInt("pid");
   int pid2 = action.getParameterInt("pid2");
   if (userId == 0){
	   userId = action.getLoginUid();
   } else {
	   user = UserInfoUtil.getUser(userId);
	   if (user == null){
		   tip = "用户不存在.";
	   }
   }
   int del = action.getParameterInt("d");
   lib = ImgLibAction.service.getLib(" id=" + del);
   if (del > 0){
	   if (action.getParameterInt("s")!=1){
		   tip = "您真的要删除图片:" + StringUtil.toWml(lib!=null?lib.getTitle():"") + "吗？";
		   isAffirmDel = true;
	   } else {
		   if (lib == null){
			   tip = "要删除的图片不存在.";
		   } else if(lib.getUserId() != action.getLoginUid()){
			   tip = "只能删除自己的图片.";
		   } else {
			   action.delPic(ImgLibAction.service.getLib(" id=" + del));
		   }
	   }
   }
   libUser = ImgLibAction.service.getLibUser(" user_id=" + userId);
   if (libUser == null && userId == action.getLoginUid()){
	   libUser = action.createNewUser();
   }
   if (pid2 > 0 ){
	   if (pid2 > privacy.length-1){
		   pid2 = 1;
	   }
	   SqlUtil.executeUpdate("update img_lib_user set privacy=" + pid2 + " where user_id=" + action.getLoginUid(),5);
   	   libUser.setPrivacy(pid2);
   }
   int id = action.getParameterInt("id");

   if (userId == action.getLoginUid()){
	   // 查看自己的图库
	   list = ImgLibAction.service.getLibList(" user_id=" + userId + " order by id desc");
	   paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
   } else {
	   // 查看别人的图库
	   if (action.canView(libUser,action.getLoginUid())){
		   list = ImgLibAction.service.getLibList(" user_id=" + userId + " order by id desc");
		   paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
	   } else {
		   tip = (String)request.getAttribute("tip");
	   }
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="图库"><p>
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
if (pid == 1){
%>隐私设置:您将设置,在别人访问您的相册时,是否能看到您的个人图库.<br/>
<a href="lib.jsp?pid2=1">仅自己可见</a><br/>
<a href="lib.jsp?pid2=2">仅我的好友可见</a><br/>
<a href="lib.jsp?pid2=3">所有人可见</a><br/>
<a href="lib.jsp">返回</a><br/><%
} else {
if (userId == action.getLoginUid()){
	%>==我的图库==<br/>图库容量:<%=libUser.getCount()%>/<%=ImgLibAction.MAX_COUNT%>张<br/><a href="flib.jsp">查看好友的图库</a><br/>隐私设置:<%=libUser.getPrivacy()>privacy.length || libUser.getPrivacy() < 1?"error":privacy[libUser.getPrivacy()] %><a href="lib.jsp?pid=1">修改</a><br/>
	<%	
} else {
	%>==<%=user != null?user.getNickNameWml():"乐客" + userId%>的图库==<br/><%	
}
if (list.size() > 0){
	if (userId == action.getLoginUid()){
		for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
			lib = (Lib)list.get(i);
				if (lib.getFlag()==0){
					%><%=i+1 %>.<%=StringUtil.toWml(lib.getTitle()) %>(审批中)&#160;<a href="lib.jsp?d=<%=lib.getId()%>">删</a><br/><%	
				} else {
					%><%=i+1 %>.<a href="show.jsp?id=<%=lib.getId()%>"><%=StringUtil.toWml(lib.getTitle()) %></a>&#160;<a href="lib.jsp?d=<%=lib.getId()%>">删</a><br/><%	
				}
		}%><%=paging.shuzifenye("lib.jsp",false,"|",response)%><%
	} else {
		for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
			lib = (Lib)list.get(i);
			if(lib != null){
				if (lib.getFlag() == 0){
					%><%=i+1 %>.<%=StringUtil.toWml(lib.getTitle()) %>(审核中)<br/><%
				} else if (lib.getFlag() == 1){
					%><%=i+1 %>.<a href="show.jsp?id=<%=lib.getId()%>&amp;uid=<%=userId %>"><%=StringUtil.toWml(lib.getTitle()) %></a><br/><%
				} else {
					%><%=i+1 %>.<%=StringUtil.toWml(lib.getTitle()) %>(未通过)<br/><%
				}
			}
		}%><%=paging.shuzifenye("lib.jsp?uid=" + userId,true,"|",response)%><%
	}
}
if (userId == action.getLoginUid()){
	%><a href="update.jsp">上传新图片</a>|<a href="help.jsp">图库功能说明</a><br/><a href="/home/home.jsp">返回我的家园</a><br/><%	
} else {
	%><a href="lib.jsp">返回我的图库</a><br/><%	
	}
}
} else {
%><%=tip%><br/><%=isAffirmDel==true?"<a href=\"lib.jsp?d=" + del + "&amp;s=1\">删除</a>|":"" %><a href="lib.jsp">返回</a><br/><%
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>