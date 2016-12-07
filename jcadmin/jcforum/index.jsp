<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl" %><%response.setHeader("Cache-Control", "no-cache");
			if (session.getAttribute("adminLogin") == null) {
				//response.sendRedirect("../login.jsp");
				BaseAction.sendRedirect("/jcadmin/login.jsp", response);
				return;
			}%>
<%IJcForumService service = ServiceFactory.createJcForumService();
//增加版块
if (null != request.getParameter("add")) {
	String name = request.getParameter("name").trim();
	String des = request.getParameter("des").trim();
	String ban = request.getParameter("ban").trim();
	String userId =  request.getParameter("userId").trim();
	if (name != null && des != null && !name.equals("")
			&& !des.equals("")) {
		ForumBean forum = new ForumBean();
		forum.setDescription(des);
		forum.setTitle(name);
		forum.setUserId(ban);
		service.addForum(forum);
		ForumCacheUtil.deleteForumList();
	} else {%>
<script>
alert("请填写各个内容！");
</script>
<%}
}
//删除版块
if (null != request.getParameter("deleteban")) {
	int id = StringUtil.toInt(request.getParameter("deleteban"));
DbOperation	dbOp = new DbOperation(2);
	dbOp.init();
	dbOp.executeUpdate("delete from jc_forum where id=" + id);
	dbOp.release();
	ForumCacheUtil.deleteForumList();
}//删除版主
if (null != request.getParameter("deletebanzhu")) {
	int id = StringUtil.toInt(request.getParameter("deletebanzhu"));
	service.updateForum("user_id=''","id="+id);
	ForumCacheUtil.deleteForum(id); 
	ForumCacheUtil.deleteForumList();
}
//删除一个版主
if (null != request.getParameter("userId")) {
    StringBuffer stringb = new StringBuffer();
	String userId = request.getParameter("userId");
	int id = StringUtil.toInt(request.getParameter("sid"));
    JcForumServiceImpl impl =new JcForumServiceImpl();
    ForumBean forum = impl.getForum("id=" + id);
    String userName = forum.getUserId();
	String userStr[] = userName.split("_");
	for (int j = 0; j < userStr.length; j++) {
	String userIds = userStr[j];
	if(userIds==null)continue;
	if(userIds.equals(userId)){continue;}
	else{
	stringb.append(userIds);
	stringb.append("_");
	}
	}
	stringb.deleteCharAt(stringb.length()-1);
	String newUserId = stringb.toString();
	service.updateForum("user_id=" +"'"+ newUserId+"'","id="+id);
	ForumCacheUtil.deleteForum(id); 
	ForumCacheUtil.deleteForumList();
}
//增加版主	
if (null != request.getParameter("addmyban")) {
	String name = request.getParameter("ban").trim();
	int userId = StringUtil.toInt(name);
	if(userId<=0){%>
		<script>
		alert("用户id输入有误！");
		window.navigate("index.jsp");
		</script>
	<%return;}
	UserBean userBean = UserInfoUtil.getUser(userId);
	if(userBean==null){%>
		<script>
		alert("该用户不存在！");
		window.navigate("index.jsp");
		</script>
	<%return;}
	String id = request.getParameter("id").trim();
	ForumBean forum=ForumCacheUtil.getForumCache(StringUtil.toInt(id));
	if(forum!=null){
		String user[]=forum.getUserId().split("_");
		if(user.length>4){%>
			<script>
			alert("已有5个版主不能添加了！");
			</script>
		<%}else{
			String myid=null;
			if(forum.getUserId().equals("")){
			 myid=name;
			 } else{
			myid=forum.getUserId()+"_"+name;
			}
			service.updateForum("user_id='"+myid+"'","id="+id);
			ForumCacheUtil.deleteForum(StringUtil.toInt(id));
			ForumCacheUtil.deleteForumList();
			%>
			<script>
			alert("添加成功!");
			window.navigate("index.jsp");
			</script>
			<%return;
		}
	}
}
%>
<html>
	<head>
	 <script language="javascript" >
function operate2(){
     if (confirm('将删除该板块所有版主,你确定要删除吗？')) {
       return true;
       } else {
        return false;
       }
      }
</script>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<H1 align="center">论坛版主后台</H1>
		<hr>
		<a href="editUser.jsp">版主链接</a><br/><br/>
		<form action="index.jsp" method="get"><input type=text name=tong value=""/><input type=submit value="查询帮会论坛"/></form> 
		<br/>
	<%--	
		<form method="post" action="index.jsp?add=1">
			论坛名称：
			<input id="name" name="name">
			<BR>
			论坛描述：
			<input id="des" name="des">
			<BR>
			论坛版主Id：
			<input id="ban" name="ban">
			<BR>
			<input type="submit" id="add" name="add" value="增加论坛">
			<br />

		</form>--%>
		<%if (null != request.getParameter("addban")){%>
		<form method="post" action="index.jsp?addmyban=1">
			论坛版主Id：
			<input id="ban" name="ban">
			<input type="hidden" id="id" name="id" value="<%=request.getParameter("addban")%>">
			<BR>
			<input type="submit" id="addmyban" name=" addmyban" value="增加版主">
			<br />
		</form>
		<a href="index.jsp">返回论坛后台首页</a><br/>
		<%}else if (null == request.getParameter("search")) {%>
		<table width="95%" border="1" align="center">
			<th >ID</th>
			<th>名称</th>
			<th>版主id</th>
			<th>版主名称</th>
			<th>操作</th>
			<th>操作</th>
			<th>全部帖</th>
			<th>已删除</th>
			<%List vec = null;
			int tongId = StringUtil.toInt(request.getParameter("tong"));
			int id = StringUtil.toInt(request.getParameter("id"));
			
			if(id>0) {
				vec = new ArrayList(1);
				vec.add(ForumCacheUtil.getForumCache(id));
			} else if(tongId<=0) {
				vec = ForumCacheUtil.getForumListCache();
			} else {
				vec = new ArrayList(1);
				vec.add(ForumCacheUtil.getForumCacheBean(tongId));
			}
				for (int i = 0; i < vec.size(); i++) {
					ForumBean notice = (ForumBean) vec.get(i);%>
			<tr>
					<td>
						<%=i + 1%>
					</td>
					<td>
						<%=notice.getTitle()%>
						<input type="hidden" id="id" name="id" value="<%=notice.getId() %>">
					</td>
					<td>
					<%=notice.getUserId()%>
						
					</td>
					<td>
						<table border="1">
						<tr>			
						<%
						String users[] = notice.getUserId().split("_");
						for (int j = 0; j < users.length; j++) {
							int userId = StringUtil.toInt(users[j]);
							if(userId==-1)continue;
							UserBean user = UserInfoUtil.getUser(userId);
							if(user==null)continue;%>
						<td><a href="index.jsp?userId=<%=user.getId()%>&amp;sid=<%=notice.getId()%>" onclick="return confirm('确认要删除这名版主?')"><%=user.getNickName()%></a></td>
						<%}%>
						</tr>
						</table>
					</td>
					<td>
						<a href="index.jsp?deletebanzhu=<%=notice.getId()%>" onClick="return operate2()">删除版主</a>
					</td>
					<td>
						<a href="index.jsp?addban=<%=notice.getId()%>">添加版主</a>
					</td>
					<td>
						<a href="../user/forums.jsp?fid=<%=notice.getId()%>">查看</a>
					</td>
					<td>
						<a href="content.jsp?forumId=<%=notice.getId()%>">查看</a>
					</td>
			</tr>
				<%}%>
			<tr>
		</table>
		<%}else {%>
		<table width="800" border="1">
			<tr>
				<td>编号</td>
				<td>名称</td>
				<td>操作</td>
			</tr>
			<%List vec = ForumCacheUtil.getForumListCache();
				for (int i = 0; i < vec.size(); i++) {
					ForumBean notice = (ForumBean) vec.get(i);%>
			<tr>
				<td>
					<%=i + 1%>
				</td>
				<td>
					<%=notice.getTitle()%>
					<input type="hidden" id="id" name="id" value="<%=notice.getId() %>">
				</td>
				<td>
					<%--<a href="index.jsp?deleteban=<%=notice.getId()%>">删除</a>&nbsp;&nbsp; --%><a href="index.jsp?search=<%=notice.getId()%>">察看</a>&nbsp;&nbsp;
				</td>
			</tr>
			<%}%>
			<tr>
		</table>
		<%}%>
		<a href="addForum.jsp">添加论坛</a><br/>
		<p align="center">
		<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a>
		</p>
		<br />
	</body>
</html>
