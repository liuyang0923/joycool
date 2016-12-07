<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.cache.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><table>
<%
Hashtable h = OnlineUtil.getOnlineHash();
Enumeration e = h.keys();
while(e.hasMoreElements()){
	String key = (String)e.nextElement();
	UserBean user = (UserBean)OnlineUtil.getOnlineBean(key);
	if(user==null)continue;
	
	String lastPage = user.getLastVisitPage();
	long last = OnlineUtil.getLastVisitTime(key);
	boolean flag = OnlineUtil.isActive(key);
	%>
	<tr>
	  <td><%= key %></td>
	  <td><%= lastPage %></td>
	  <td><%= (System.currentTimeMillis()-last)/1000 %></td>
	  <td><%= flag %></td>
	  <td><%= LoadResource.getPostionById(key, 1).getUrl() %></td>
	</tr>
	<%
}
%>
</table>