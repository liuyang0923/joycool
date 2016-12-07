<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.util.db.*"%><%@ page import="net.wxsj.service.infc.*"%><%@ page import="net.wxsj.service.factory.*"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><strong>已做搭配的用户列表</strong><br>
<table border="1" width="100%">
<tr>
<td><strong>序号</strong></td>
<td><strong>用户ID</strong></td>
<td><strong>总搭配数</strong></td>
</tr>
<%
IZippoService zippoService = ServiceFactory.createZippoService(IBaseService.CONN_IN_SERVICE, null);

DbOperation dbOp = zippoService.getDbOp();
ResultSet rs = dbOp.executeQuery("select count(id) as c, user_id from wxsj_zippo_pair group by user_id order by c desc, id desc");
ZippoMobileBean mobile = null;
UserBean user = null;
int i = 0;
while(rs.next()){
	i ++;	
	user = UserInfoUtil.getUser(rs.getInt("user_id"));
%>
<tr>
<td><%=i%></td>
<td><%=user.getId()%>(<%=user.getNickName()%>)</td>
<td><%=(rs.getInt("c"))%>&nbsp;<a href="userPairList.jsp?user_id=<%=user.getId()%>" target="_blank">查看</a></td>
</tr>
<%
}
zippoService.releaseAll();
%>
</table>