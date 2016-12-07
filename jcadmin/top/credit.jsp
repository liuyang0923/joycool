<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*,net.joycool.wap.action.jcadmin.UserCashAction"%><%@ page import="net.joycool.wap.service.infc.ITopService"%><%@ page import="net.joycool.wap.bean.top.UserTopBean"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.CrownBean"%><%@ page import="java.text.SimpleDateFormat"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.bean.chat.RoomInviteRankBean"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%!/**
	 * 取得某日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public String getFirstDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());

	}

	/**
	 * fanys 2006-08-22 得到当前日期得上一周的第一天
	 * 
	 * @return
	 */
	public String getFirstDayOfLastWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, 1);
		return getFirstDayOfWeek(c.getTime());
	}

	public void updateUserCrown(int userId, int imageId,
			IUserService userService, IChatService chatService) {
		CrownBean crown = null;
		RoomInviteRankBean inviteRank = chatService
				.getRoomInviteRank("user_id=" + userId
						+ " and create_datetime='" + getFirstDayOfLastWeek()
						+ "'");
		
		if (null == inviteRank){
			crown = userService.getCrown("id=" + imageId);
			UserInfoUtil.updateUserStatus("image_path_id=-8", "user_id=" + userId, userId,UserCashAction.OTHERS,
					"更新用户信誉王冠");
			UserStatusBean.flushUserHat(userId);
		}
	}

	%>
<%//lbj_登录限制_start
			if (session.getAttribute("adminLogin") == null) {
				//response.sendRedirect("../login.jsp");
				BaseAction.sendRedirect("/jcadmin/login.jsp", response);
				return;
			}
			//lbj_登录限制_end

			//创建工厂
			ITopService topService = ServiceFactory.createTopService();
			IUserService userService = ServiceFactory.createUserService();
			IChatService chatService = ServiceFactory.createChatService();
			CrownBean crown = null;
			//String strImagePathWhere = " image_path_id=2 ";
			//删除记录
			if (request.getParameter("delete") != null) {
				String delete = request.getParameter("delete");
				int number = StringUtil.toInt(delete);
				int userId = StringUtil.toInt(request.getParameter("userId"));
				int imageId = StringUtil
						.toInt(request.getParameter("td_image"));
				RoomInviteRankBean inviteRank = chatService
						.getRoomInviteRank("user_id=" + userId
								+ " and create_datetime='"
								+ getFirstDayOfLastWeek() + "'");
				crown = userService.getCrown("id=" + imageId);
				if (null == inviteRank){
					UserStatusBean us=UserInfoUtil.getUserStatus(userId);
					if(us!=null && us.getImagePathId()==-8){
					UserInfoUtil.updateUserStatus("image_path_id=0",
							"user_id=" + userId, userId,UserCashAction.OTHERS, "删除用户信誉王冠");
					UserStatusBean.flushUserHat(userId);
					}
				}		
				if (number >= 0) {
					topService.delUserTop("id=" + number);
				}
				//response.sendRedirect("credit.jsp");
				BaseAction.sendRedirect("/jcadmin/top/credit.jsp", response);
				return;
			}

			//添加和更改操作
			if (request.getMethod().equalsIgnoreCase("post")) {
				String changePriority = request.getParameter("changePriority");
				//判断更改顺序操作
				if (changePriority != null) {
					int number = StringUtil.toInt(changePriority);
					if (number >= 0) {
						String id = request.getParameter("id");
						int imageId = StringUtil.toInt(request
								.getParameter("td_image"));
						int userId = StringUtil.toInt(request
								.getParameter("userId"));
						topService.updateUserTop("priority=" + number
								+ ",image_id=" + imageId, "id=" + id);
						updateUserCrown(userId, imageId, userService,
								chatService);

					%>
<script>
			alert("更新成功！");
			window.navigate("credit.jsp");
			</script>
<%} else {%>
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
				//判断通过用户ID添加
				if (id != null && priority != null) {
					int number1 = StringUtil.toInt(id);
					int number2 = StringUtil.toInt(priority);
					int imageId = StringUtil.toInt(request
							.getParameter("image1"));
					if (number1 >= 0 && number2 >= 0) {
						UserBean user = UserInfoUtil.getUser(number1);
						if (user != null) {
						topService.delUserTop("user_id=" + user.getId());
							UserTopBean userTop = topService
									.getUserTop("user_id=" + number1
											+ " and mark=2");
							if (userTop == null) {
								userTop = new UserTopBean();
								userTop.setMark(2);
								userTop.setImageId(imageId);
								userTop.setPriority(number2);
								userTop.setUserId(number1);
								topService.addUserTop(userTop);
								updateUserCrown(user.getId(), imageId,
										userService, chatService);

							%>
<script>
				alert("添加成功！");
				window.navigate("credit.jsp");
				</script>
<%} else {%>
<script>
					alert("输入用户ID已经存在");
					history.back(-1);
					</script>
<%}
						} else {%>
<script>
					alert("输入用户id不存在");
					history.back(-1);
					</script>
<%}
					} else {%>
<script>
		alert("请填写正确各项参数！");
		history.back(-1);
		</script>
<%}
					return;
					//判断通过用户昵称添加
				} else if (nickname != null && priority != null) {
					int number1 = StringUtil.toInt(priority);
					int imageId = StringUtil.toInt(request
							.getParameter("image2"));
					if (number1 >= 0) {
						UserBean user = userService.getUser("nickname='"
								+ nickname + "'");
						if (user != null) {
						topService.delUserTop("user_id=" + user.getId());
							UserTopBean userTop = topService
									.getUserTop("user_id=" + user.getId()
											+ " and mark=2");
							if (userTop == null) {
								userTop = new UserTopBean();
								userTop.setMark(2);
								userTop.setPriority(number1);
								userTop.setUserId(user.getId());
								userTop.setImageId(imageId);
								topService.addUserTop(userTop);
								updateUserCrown(user.getId(), imageId,
										userService, chatService);

							%>
<script>
				alert("添加成功！");
				window.navigate("credit.jsp");
				</script>
<%} else {%>
<script>
					alert("输入用户昵称已经存在");
					history.back(-1);
					</script>
<%}
						} else {%>
<script>
					alert("输入用户昵称不存在");
					history.back(-1);
					</script>
<%}
					} else {%>
<script>
		alert("请填写正确各项参数！");
		history.back(-1);
		</script>
<%}
					return;
				}
			}
			//获取所有荣誉市民记录
			Vector userList = topService
					.getUserTopList(" mark=2 order by priority asc");
			//Vector crownList = userService.getCrownList(" image_path_id=2");

			%>
			<html>
			<head></head>
			<body>
乐酷荣誉市民用户排名管理后台
<hr>
<table width="100%" border="2">
	<tr>
		<td align="center" width="10%">序号</td>
		<td align="center" width="10%">优先级</td>
		<td align="center" width="10%">用户ID</td>
		<td align="center" width="10%">用户名</td>
		<td align="center" width="10%">添加时间</td>
		<td align="center" width="10%">王冠</td>
		<td align="center" width="10%">修改王冠</td>
		<td align="center" width="5%">操作</td>
		<td align="center" width="5%">操作</td>
	</tr>
	<%if (userList != null && userList.size() > 0) {
				UserTopBean userTop = null;
				for (int i = 0; i < userList.size(); i++) {
					userTop = (UserTopBean) userList.get(i);
%>
	<tr>
		<form method="post" action="credit.jsp">
		<td width="10%"><%=(i + 1)%></td>
		<td width="10%"><input type="text" name="changePriority"
			value=<%=userTop.getPriority()%>></td>
		<td width="10%"><%=userTop.getUserId()%>
		<input type="hidden" name="userId" value="<%=userTop.getUserId()%>"></td>
		<%UserBean user = UserInfoUtil.getUser(userTop.getUserId());
					String nickname = user.getNickName();
					if (nickname.equals(""))
						nickname = "该用户无昵称";
%>
		<td align="center" width="10%"><%=nickname%></td>
		<td align="center" width="15%"><%=userTop.getCreateDatetime()%>
		<input type="hidden" name="id" value="<%=userTop.getId()%>">
		
		</td>
		
		<%CrownBean tempCrown = null;
					tempCrown = userService.getCrown(" id="+ userTop.getImageId());
%>
		<td align="center">
		<%if(tempCrown!=null){%>
		<img src="/rep/lx/t<%=userTop.getImageId()%>.gif">
		<%}%></td>
		<td align="center"><select id="crown" name="td_image">
		<%--	<%for (int j = 0; j < crownList.size(); j++) {
						crown = (CrownBean) crownList.get(j);%>
			<option value="<%=crown.getId()%>"--%>
			<option value="8.gif">8.gif
		<%--		<%if(tempCrown!=null){if(crown.getId()==tempCrown.getId()) {%> selected <%}} %>><%=crown.getImage()%>
			<%}%>--%>
		</select></td>
		<td align="center" width="5%"><input type="submit" value="修改"></td>
		<td align="center" width="5%"><a
			href="credit.jsp?delete=<%=userTop.getId()%>&userId=<%=userTop.getUserId() %>">删除</a></td>
		</form>
	</tr>
	<%}
			} else {%>
	暂无排名
	<%}%>
</table>
<br />
增加用户
<hr>
按用户ID添加:
<form method="post" action="credit.jsp">用户ID：<input type="text"
	name="id" size="30"><br>
优先级：<input type="text" name="priority" size="30"><br>

王冠:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/rep/lx/t8.gif"><select id="image1"
	name="image1">
<%--	<%for (int j = 0; j < crownList.size(); j++) {
				crown = (CrownBean) crownList.get(j);
%>
	<option value="<%=crown.getId()%>"><%=crown.getImage()%> <%}%>--%>
	<option value="8">8.gif
</select><br />
<input type="submit" value="增加"></form>
按用户昵称添加:
<form method="post" action="credit.jsp">用户昵称：<input type="text"
	name="nickname" size="30"><br>
优先级：<input type="text" name="priority" size="30"><br>
王冠:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/rep/lx/t8.gif"><select id="image2"
	name="image2">
<%--	<%for (int j = 0; j < crownList.size(); j++) {
				crown = (CrownBean) crownList.get(j);
%>
	<option value="<%=crown.getId()%>"><%=crown.getImage()%> <%}%>--%>
	<option value="8">8.gif
</select><br />
<input type="submit" value="增加"></form>
<font color="red">优先显示邀请好友的王冠,所以给某个用户加上荣誉王冠后,可能不会显示荣誉王冠</font><br/>
</body>
</html>
