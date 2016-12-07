<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="java.util.*,java.io.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.action.chat.*,net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.ServiceFactory"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page errorPage=""%>
<%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("./login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
IChatService service = ServiceFactory.createChatService();
String fun=request.getParameter("fun");
if(fun==null) fun="1";
pageContext.setAttribute("fun",fun);
%>
<html >
<head>
<script language="javascript">
function fun(selItem)
{
var form=selItem;
if(form.elements['name'].value==""||form.elements['name'].value==null)
{
	alert("聊天室名不能为空!");
	return false;
}
return true;
}
</script>
</head>
<body>
<p align="center">
<a href="randomRoomId.jsp?fun=1">显示</a>&nbsp;&nbsp;
<a href="randomRoomId.jsp?fun=2">增加</a>&nbsp;&nbsp;
</p>
<hr/>
<logic:equal name="fun" value="1" >
<%
//删除记录
String del=request.getParameter("delete");
String tip=null;
if(del!=null)
{
String id=request.getParameter("id");
service.deleteRoomRate("id="+id);
RoomRateAction.clearRoomMap();
tip="删除成功!";
}
//修改记录
String alter=request.getParameter("alter");
if(alter!=null)
{
String id =request.getParameter("id");
String rate = request.getParameter("rate");
if(StringUtil.toInt(rate)<0) { tip="数据输入有误!"; }else{
service.updateRoomRate("room_rate="+rate,"id="+id);
RoomRateAction.clearRoomMap();
tip="修改成功!";
}
}
//显示记录
ArrayList roomList=service.getRoomRateList(null);
TreeMap roomMap = RoomRateAction.getRoomMap();
int base=Integer.parseInt((String)roomMap.get(new Integer(Constants.RANDOM_BASE)));
%>
<%if(tip!=null){%><%=tip%><br/><%}%>
<div align="center">
<table border="1">
<caption>随机聊天室管理</caption>
<tr><th>ROOMID</th><th>聊天室名</th><th>机率</th><th>概率</th><th>修改</th><th>删除</th></tr>
<%
int size=roomList.size();
for(int i=0;i<size;i++){
	RoomRateBean room=(RoomRateBean)roomList.get(i);	
%>
<form name="form2" method="get" action="randomRoomId.jsp">
<tr><td><input type="hidden" name="id" value="<%=room.getId()%>"/><%=room.getRoomId()%></td><td><%=room.getName()%></td>
<td><input type="text" name="rate" size="10" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;" value="<%=room.getRate()%>"/></td>
<td><%=(room.getRate()*100/base)+"%"%></td>
<td><input type="submit" name="alter" value="修改"/></td>
<td><input type="submit" name="delete" value="删除" onClick="return confirm('你确定删除此条记录吗？');"/></td></tr>
</form>
<%}%>
</table>
</div>
</logic:equal>

<logic:equal name="fun" value="2">
<%
String submit=request.getParameter("submit1");
String tip1=null;
if(submit!=null)
{
String id=request.getParameter("id");
String name=request.getParameter("name");
String rate=request.getParameter("rate");
if(id.equals("")||name.equals("")||rate.equals(""))
{
	tip1="内容不能为空!";
}else if(StringUtil.toInt(id)<0||StringUtil.toInt(rate)<0){
	tip1="数据输入有误!";
}else{
RoomRateBean room=new RoomRateBean();
room.setRoomId(Integer.parseInt(id));
room.setName(name);
room.setRate(Integer.parseInt(rate));
service.addRoomRate(room);
RoomRateAction.clearRoomMap();
tip1 ="聊天室:"+name+"添加成功!";
}
}
%>
<%if(tip1!=null){%><%=tip1%><br/><%}%>
<form id="form1" method="post" action="randomRoomId.jsp" onSubmit=" return fun(this)">
<table border="1" align="center">
<tr><th>ROOMID</th><td><input type="text" name="id" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><th>聊天室名</th><td><input type="text" name="name"  /><input type="hidden" name="fun" value="2"/></td></tr>
<tr><th>机率</th><td><input type="text" name="rate" onKeyPress="if(event.keyCode<48 || event.keyCode>57) event.returnValue=false;"/></td></tr>
<tr><td colspan="2" align="center"><input type="submit" name="submit1" value="提交" /><input type="reset" name="reset" value="重置"></td></tr>
</table>
</form>
</logic:equal>

</body>
</html>