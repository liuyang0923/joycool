<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.ITopService" %><%@ page import="net.joycool.wap.bean.top.UserTopBean" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean, net.joycool.wap.cache.*" %><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
//创建工厂
ITopService topService = ServiceFactory.createTopService();
IUserService userService = ServiceFactory.createUserService();
//删除操作
if(request.getParameter("delete") != null){
    String delete=request.getParameter("delete");
    int number=StringUtil.toInt(delete);
    UserTopBean userTop=topService.getUserTop("id="+number);
    if(userTop!=null){
          topService.delUserTop("id="+number);
          UserStatusBean us=UserInfoUtil.getUserStatus(userTop.getUserId());
		  if(us!=null && us.getImagePathId()==-9){
          	 UserInfoUtil.updateUserStatus("image_path_id=0", "user_id=" + userTop.getUserId(), userTop.getUserId(),UserCashAction.OTHERS,"更新用户黑名单王冠");
		 	 UserStatusBean.flushUserHat(userTop.getUserId());
		  }
		  OsCacheUtil.flushGroup(OsCacheUtil.TOP_GROUP, "blacklist");
	  }
	
	BaseAction.sendRedirect("/jcadmin/top/blackList.jsp", response);
	return;
}
//更新和添加操作
if(request.getMethod().equalsIgnoreCase("post")){
	String changePriority = request.getParameter("changePriority");
	//更新位置操作
	if(changePriority!=null){
	   int number=StringUtil.toInt(changePriority);
	    if(number>=0){
	       String  id= request.getParameter("id");
	       topService.updateUserTop("priority="+number,"id="+id);
	       OsCacheUtil.flushGroup(OsCacheUtil.TOP_GROUP, "blacklist");
	       %>
	   		<script>
			alert("更新成功！");
			window.navigate("blackList.jsp");
			</script>
	       <%
	    }else{%>
	    <script>
		alert("请填写正确各项参数！");
		history.back(-1);
		</script>
	    <%}
	    return;
	}
	String priority = request.getParameter("priority");
	String nickname = request.getParameter("nickname");
	String id = request.getParameter("id");
	//判断通过用户id添加
	if(id!=null && priority!=null){
	int number1=StringUtil.toInt(id);
	int number2=StringUtil.toInt(priority);
	    if(number1>=0 && number2>=0){
	    UserBean user=UserInfoUtil.getUser(number1);
	    if(user!=null){
	    UserTopBean userTop=topService.getUserTop("user_id="+number1+" and mark=1");
		    if(userTop==null){
		         userTop=new UserTopBean();
		         userTop.setMark(1);
		         userTop.setPriority(number2);
		         userTop.setUserId(number1);
		         userTop.setImageId(9);
		         topService.addUserTop(userTop);
		         UserInfoUtil.updateUserStatus("image_path_id=-9", "user_id=" + number1, number1,UserCashAction.OTHERS,
					"更新用户黑名单王冠");
				UserStatusBean.flushUserHat(number1);
		         OsCacheUtil.flushGroup(OsCacheUtil.TOP_GROUP, "blacklist");
		         %>
		         <script>
				alert("添加成功！");
				window.navigate("blackList.jsp");
				</script>
		       <%}else{%>
		       	    <script>
					alert("输入用户ID已经存在");
					history.back(-1);
					</script>
		       <%}
		    }else{%>
		       	    <script>
					alert("输入用户id不存在");
					history.back(-1);
					</script>
		       <%}
	    }else{%>
	    <script>
		alert("请填写正确各项参数！");
		history.back(-1);
		</script>
	    <%}
	    return;
	    //判断通过用户昵称添加
	}else if(nickname!=null && priority!=null){
	int number1=StringUtil.toInt(priority);
		if(number1>=0){
        UserBean user=userService.getUser("nickname='"+nickname+"'");
	        if(user!=null){
		      UserTopBean userTop=topService.getUserTop("user_id="+user.getId()+" and mark=1");
			    if(userTop==null){
			         userTop=new UserTopBean();
			         userTop.setMark(1);
			         userTop.setPriority(number1);
			         userTop.setImageId(9);
			         userTop.setUserId(user.getId());
			         topService.addUserTop(userTop);
			         UserInfoUtil.updateUserStatus("image_path_id=-9", "user_id=" + user.getId(), user.getId(),UserCashAction.OTHERS,
					 "更新用户黑名单王冠");
					 UserStatusBean.flushUserHat(user.getId());
			         OsCacheUtil.flushGroup(OsCacheUtil.TOP_GROUP, "blacklist");
			         %>
		         <script>
				alert("添加成功！");
				window.navigate("blackList.jsp");
				</script>
		       <%}else{%>
		       	    <script>
					alert("输入用户昵称已经存在");
					history.back(-1);
					</script>
		       <%}
		    }else{%>
		       	    <script>
					alert("输入用户昵称不存在");
					history.back(-1);
					</script>
		       <%}
	    }else{%>
	    <script>
		alert("请填写正确各项参数！");
		history.back(-1);
		</script>
	    <%}
     return;
	}
}
//获取所有通缉令记录
Vector userList=topService.getUserTopList("mark=1 order by priority asc");
%>
乐酷通缉令排名管理后台
<hr>
<table width="100%" border="2">
<tr>
<td  align="center" width="10%">序号</td>
<td  align="center" width="10%">优先级</td>
<td  align="center" width="10%">用户ID</td>
<td  align="center" width="10%">用户名</td>
<td  align="center" width="10%">添加时间</td>
<td  align="center" width="5%">操作</td>
<td  align="center" width="5%">操作</td>
</tr>
<%
if(userList!=null || userList.size()>0){
UserTopBean userTop=null;
for(int i = 0; i < userList.size(); i ++){
	userTop = (UserTopBean) userList.get(i);
%>
<tr>
<form method="post" action="blackList.jsp">
<td width="10%"><%=(i + 1)%></td>
<td width="10%"><input type="text" name="changePriority" value=<%=userTop.getPriority()%>></td>
<td width="10%"><%=userTop.getUserId()%></td>
<%
UserBean user=UserInfoUtil.getUser(userTop.getUserId());
String nickname=user.getNickName();
if(nickname.equals(""))
nickname="该用户无昵称";
%>
<td align="center" width="10%"><%=nickname%></td>
<td align="center" width="10%"><%=userTop.getCreateDatetime()%></td>
<input type="hidden" name="id" value=<%=userTop.getId()%>>
<td  align="center" width="5%"><input type="submit" value="修改"></td>
</form>
<td align="center" width="5%"><a href="blackList.jsp?delete=<%=userTop.getId()%>">删除</a></td>

</tr>
<%
}}else{%>
暂无排名
<%}%>
</table><br/>
增加用户
<hr>
按用户ID添加:
<form method="post" action="blackList.jsp">
用户ID：<input type="text" name="id" size="30"><br>
优先级：<input type="text" name="priority" size="30"><br>
<input type="submit" value="增加">
</form>
按用户昵称添加:
<form method="post" action="blackList.jsp">
用户昵称：<input type="text" name="nickname" size="30"><br>
优先级：<input type="text" name="priority" size="30"><br>
<input type="submit" value="增加">
</form>