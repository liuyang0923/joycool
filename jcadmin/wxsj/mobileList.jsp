<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.util.db.*"%><%@ page import="net.wxsj.service.infc.*"%><%@ page import="net.wxsj.service.factory.*"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><strong>已提交的用户列表</strong><br>
<table border="1" width="100%">
<tr>
<td><strong>序号</strong></td>
<td><strong>用户ID</strong></td>
<td><strong>手机号</strong></td>
<td><strong>总搭配数</strong></td>
</tr>
<%
IZippoService zippoService = ServiceFactory.createZippoService(IBaseService.CONN_IN_SERVICE, null);

DbOperation dbOp = zippoService.getDbOp();
ResultSet rs = dbOp.executeQuery("select count(wxsj_zippo_pair.id) as c, wxsj_zippo_mobile.* from wxsj_zippo_mobile join wxsj_zippo_pair on wxsj_zippo_mobile.user_id=wxsj_zippo_pair.user_id group by wxsj_zippo_mobile.user_id order by c desc");
ZippoMobileBean mobile = null;
UserBean user = null;
int i = 0;
while(rs.next()){
	i ++;
	mobile = new ZippoMobileBean();
	mobile.setId(rs.getInt("id"));
	mobile.setUserId(rs.getInt("user_id"));
	mobile.setMobile(rs.getString("mobile"));
	user = UserInfoUtil.getUser(mobile.getUserId());
%>
<tr>
<td><%=i%></td>
<td><%=mobile.getUserId()%>(<%=user.getNickName()%>)</td>
<td><%=mobile.getMobile()%></td>
<td><%=(rs.getInt("c"))%>&nbsp;<a href="userPairList.jsp?user_id=<%=mobile.getUserId()%>" target="_blank">查看</a></td>
</tr>
<%
}
zippoService.releaseAll();
%>
</table>