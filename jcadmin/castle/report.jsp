<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.cache.CacheManage"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
static CastleMessage getMessage2(ResultSet rs) throws Exception {
	CastleMessage bean = new CastleMessage(rs.getString("content"), rs.getInt("id"), rs.getTimestamp("time"), rs.getInt("uid"));
	bean.setDetail(rs.getString("detail"));
	bean.setType(rs.getInt("type"));
	bean.setTongId(rs.getInt("tong_id"));
	return bean;
}
static List getCastleMessageByUid(int uid, int start, int limit) {
	List list = new ArrayList();
	DbOperation db = new DbOperation(5);
	String query = "SELECT * FROM castle_message WHERE uid = "+uid+" order by id desc limit "+start+","+limit;
	
	try {
		ResultSet rs = db.executeQuery(query);
		while(rs.next()) {
			list.add(getMessage2(rs));
		}
	} catch (Exception e) {
		e.printStackTrace();
		return list;
	}finally{
		db.release();
	}
	
	
	return list;
}
%><%
	CustomAction action = new CustomAction(request);
	
	int id = action.getParameterInt("id");
	if(id==0)
		id = action.getParameterInt("uid");
	CastleUserBean user = CastleUtil.getCastleUser(id);
	if(user==null){
		response.sendRedirect("index.jsp");
		return;
	}
	TongBean tongBean = null;
	List list = service.getCastleList(id);
	if(user.getTong() > 0) {
		tongBean = CastleUtil.getTong(user.getTong());
	}
	int p = action.getParameterInt("p");
	int number=20;
	List msgList = getCastleMessageByUid(id, p*number, number);
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	城主信息<br/><br/>
【<a href="user.jsp?id=<%=id%>"><%=StringUtil.toWml(user.getName())%></a>】<br/>
<a href="report.jsp?id=<%=id%>&p=<%=p+1%>">下一页</a>&nbsp;<%if(p>0){%><a href="report.jsp?id=<%=id%>&p=<%=p-1%>">上一页</a><%}%><br/>
<table class="tbg" cellpadding="2" cellspacing="1">
<%
	for(int i = 0; i < msgList.size(); i++) {
		CastleMessage message = (CastleMessage)msgList.get(i);
%><tr><td width="120"><%=DateUtil.formatSqlDatetime(message.getTime()).substring(5)%></td><td width="200" ><%=message.getContent()%></td><td style="WORD-BREAK: break-all;"><%=message.getDetail()%></td></tr>
<%}%>
</table><br/>
<%@include file="bottom.jsp"%>
</html>