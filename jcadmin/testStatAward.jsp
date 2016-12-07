<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.util.db.*"%><%@page  import="java.sql.ResultSet"%>
<%@page  import="java.sql.SQLException"%>
<%@page  import="java.util.Vector"%>
<%@page  import="net.joycool.wap.util.*"%>
<%
Vector vec=new Vector();
DbOperation dbOp = new DbOperation();
dbOp.init();
String strsql="select   distinct user_id  from  jc_test_record  where user_id not in(select user_id from jc_test_record where question_id=1 and answer_id=1 and mark=1) and mark=1  order by id limit 0,100 ";
ResultSet rs = dbOp.executeQuery(strsql);
try {
	while (rs.next()) {
		vec.add(rs.getInt(1)+"");
	}
} catch (SQLException e) {
	e.printStackTrace();
}

%>
<html>
<head>
</head>
<body>
<table width="800" border="1">
<tr>
<td>用户id</td>
<td>回答Id</td>
<td>排名</td>
</tr>
<%
int userId=0;
int recordId=0;
 for( int i=0;i<vec.size();i++){
	 userId=StringUtil.toInt((String)vec.get(i));
	 strsql="select max(id) from jc_test_record where user_id="+userId;
	 rs=dbOp.executeQuery(strsql);
	 if(rs.next())
		 recordId=rs.getInt(1);
%>
<tr>
<td><%=userId%></td>
<td><%=recordId%></td>
<td><%=(i+1)%></td>
</tr>
<%} 
dbOp.release();%>
</table>
说明:1-20名,性爱光碟一套;
21-40名,情趣内衣一件;
41-60名,印度神油一盒;
61-100名,品牌安全套一盒.<br/>
<a href="<%=("testStat.jsp") %>">返回</a><br/>
</body>
</html>