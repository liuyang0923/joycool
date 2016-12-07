<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.framework.*"%><meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<%
DbOperation dbOp = new DbOperation();
dbOp.init();
String query = "SELECT count(id) as c_id FROM user_info where TO_DAYS(now()) - TO_DAYS(create_datetime) < 1";
ResultSet rs = dbOp.executeQuery(query);
int regCount = 0;
if(rs.next()){
	regCount = rs.getInt("c_id");
}
dbOp.release();

int maxSessionCount = JoycoolSessionListener.maxSessionCount;
int totalCount = CountUtil.totalCount;
%>
<p align="center">
当天新增用户数:<%=regCount%><br/>
当天用户在线峰值:<%=maxSessionCount%><br/>
当天总的PV值:<%=totalCount%><br/>
总跳转数:<%=LogUtil.totalRedirect%><br/>
总跳回数:<%=LogUtil.totalBack%><br/>
</p>