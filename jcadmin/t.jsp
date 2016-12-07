<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,
                 java.io.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.util.db.*"%><% 
IUserService userService = ServiceFactory.createUserService();
Date yestoday = DateUtil.rollDate(new Date(), -2);

%>
<%= yestoday %><br/>
<table>
<tr>
  <td>mobile</td>
  <td>last logint time</td>
  <td>yestody</td>
</tr>
<% 
BufferedReader reader = new BufferedReader(new FileReader(new File("/home/joycool/100000.txt")));
String line = reader.readLine();
while(line!=null){
	String mobile = line.trim();
	UserBean user = userService.getUser("mobile='" + mobile + "'");
	if(user!=null){
		UserStatusBean status = UserInfoUtil.getUserStatus(user.getId());
		if(status!=null){
			String loginTime = status.getLastLoginTime();
			Date date = DateUtil.parseDate(loginTime, DateUtil.normalTimeFormat);
			boolean flag = date.after(yestoday);
%>
<tr>
  <td><%= mobile %></td>
  <td><%= loginTime %></td>
  <td><%= flag %></td>
</tr>
<%			
		}
	}
	
	line = reader.readLine();
}
%>
</table>