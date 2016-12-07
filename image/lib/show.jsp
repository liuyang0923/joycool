<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.imglib.*,net.joycool.wap.util.db.DbOperation"%>
<% response.setHeader("Cache-Control","no-cache");
   ImgLibAction action = new ImgLibAction(request);
   if (action.getLoginUser() == null){
	   // 没登陆,返回登陆页面
	   response.sendRedirect("/user/login.jsp");
	   return;
   }
   String tip ="";
   Lib lib = null;
   UserBean user = null;
   boolean isAffirmDel = false;
   int id = action.getParameterInt("id");
   int userId = action.getParameterInt("uid");
   int del = action.getParameterInt("d");
   int modify = action.getParameterInt("m");
   int next = 0;
   int prev = 0;
   if (userId > 0){
	   user = UserInfoUtil.getUser(userId);
	   if (user == null){
		   tip = "用户不存在.";
	   }
   } else {
	   userId = action.getLoginUid();
   }
   if ("".equals(tip)){
	   LibUser libUser = ImgLibAction.service.getLibUser(" user_id=" + userId);
	   if (libUser == null){
			tip = "该用户还没有创建图库.";   
	   } else {
		   lib = ImgLibAction.service.getLib(" id=" + id);
		   if (lib != null){
			   // 是否查看自己的图片？
			   if (lib.getUserId() == action.getLoginUid()){
				   if (modify == 2){
					   	// 修改标题页面
				   		String title = action.getParameterNoEnter("title");
				   		if ("".equals(title) || title.length() > 12){
				   			tip = "没有写标题或字数太长.";
				   		} else {
				   			SqlUtil.executeUpdate("update img_lib set title='" + StringUtil.toSql(title) + "',modify_time=now() where id=" + id,5);
//				   			// 写入记录
//				   			LibLog log = new LibLog();
//				   			log.setUserId(action.getLoginUid());
//				   			log.setTitle(title);
//				   			log.setFlag(0);
//				   			log.setImgId(lib.getId());
//				   			if (ImgLibAction.service.getLog(" img_id=" + log.getImgId()) == null){
//				   				ImgLibAction.service.addNewLog(log);
//				   			} else {
//				   				ImgLibAction.service.updateLog(log);
//				   			}
				   			response.sendRedirect("show.jsp?id=" + id);
				   			return;
				   		}
				   } else {
					   if ("".equals(tip)){
						   if (del == 1){
						   		tip = "您真的要删除图片:" + StringUtil.toWml(lib.getTitle()) + "吗？";
						   		isAffirmDel = true;
						   } else {
							   next = ImgLibAction.service.getIntValue("select id from img_lib where id<" + id + " and user_id=" + userId + " order by id desc limit 1");
							   prev = ImgLibAction.service.getIntValue("select id from img_lib where id>" + id + " and user_id=" + userId + " order by id limit 1");
						   }
					   }
				  }
			   // 查看别人的图片
			   } else {
				   if (action.canView(libUser,action.getLoginUid())){
					   if (lib.getFlag() != 1){
						   tip = "图片不存在.";
					   } else {
						   // 正常浏览图片
						   int copy = action.getParameterInt("c");
						   if (copy == 1){
							   if (action.copyToMyLib(lib,ImgLibAction.service.getLibUser(" user_id=" + action.getLoginUid()))){
								   response.sendRedirect("mess.jsp?mid=2&uid=" + userId + "&id2=" + id);
								   return;
							   } else {
								   tip = (String)request.getAttribute("tip");
							   }
						   }
						   next = ImgLibAction.service.getIntValue("select id from img_lib where id<" + id + " and flag=1 and user_id=" + userId + " order by id desc limit 1");
						   prev = ImgLibAction.service.getIntValue("select id from img_lib where id>" + id + " and flag=1 and user_id=" + userId + " order by id limit 1");
					   }
				   } else {
					   tip = (String)request.getAttribute("tip");	   
				   }
			   }
		   } else {
			   tip = "图片不存在.";
		   }
	   }
   }
int toUserId=action.getParameterInt("tuid");
int roomId=action.getParameterInt("rid");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="我的图库"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (modify==1){
%>修改标题<br/>
标题:(限12字)<br/>
<input name="title" value="" maxlength="12" /><br/>
<anchor>
确认
<go href="show.jsp?id=<%=id%>&amp;m=2" method="post">
	<postfield name="title" value="$title" />
</go>
</anchor><a href="show.jsp?id=<%=id%>">返回</a><br/>
<%
} else {
%><%=StringUtil.toWml(lib.getTitle())%><br/>
<img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%><%=lib.getImg().startsWith("/")?lib.getImg():"/box/"+lib.getImg()%>" alt="loading..." /><br/>
<%if (prev>0){%><a href="show.jsp?id=<%=prev%><%if(userId != action.getLoginUid()){%>&amp;uid=<%=userId%><%}%>">上一张</a><%}else{%>上一张<%
}%>|<%if(next>0){%><a href="show.jsp?id=<%=next%><%if(userId != action.getLoginUid()){%>&amp;uid=<%=userId%><%}%>">下一张</a><%}else{%>下一张<%}%><br/>
<%if(userId == action.getLoginUid()){
%><a href="show.jsp?id=<%=lib.getId()%>&amp;d=1">删除</a>|<a href="show.jsp?id=<%=id%>&amp;m=1">修改标题</a><br/>
<a href="lib.jsp">返回我的图库</a><br/><%
if (toUserId > 0){
%><a href="/chat/postPic.jsp?rid=<%=roomId%>&amp;tuid=<%=toUserId%>">继续选择图片</a><br/><%
}
} else {
%>
<a href="lib.jsp?uid=<%=userId%>">返回TA的图库</a><br/><%	
}	
}	
} else {
%><%=tip%><br/><%=isAffirmDel==true?"<a href=\"lib.jsp?d=" + lib.getId() + "&amp;s=1\">删除</a>|":"" %><a href="<%=lib != null?"show.jsp?id=" + lib.getId():"lib.jsp?uid=" + userId %>">返回</a><br/><%	
}
%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>