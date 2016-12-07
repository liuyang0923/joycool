<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.ITopService" %><%@ page import="net.joycool.wap.bean.AdverBean" %><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean, net.joycool.wap.cache.*" %><%@ page import="net.joycool.wap.service.infc.IAdverServic" %><script>
function check(){
  if (isNaN(groupNew.value))
  {alert("非法字符！");
  groupNew.value="";}
}
</script>

<%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
String groupid = request.getParameter("groupid");
if(groupid == null)
groupid = "1";
IAdverServic topService = ServiceFactory.createAdverService();
//删除操作
if(request.getParameter("delete") != null){
    String delete=request.getParameter("delete");
    int number=StringUtil.toInt(delete);
    if(number>=0){
          topService.deleteAdver("id="+number);
	    }
	CacheManage.jspAdver.clear();
	BaseAction.sendRedirect("/jcadmin/advice/adviceManager.jsp?groupid="+groupid, response);
	return;
}
//更新和添加操作
if(request.getParameter("nameNew") == null){

    String id = request.getParameter("id");
	String name = request.getParameter("name");
	String title = request.getParameter("title");
	String url = request.getParameter("url");
	//更新位置操作
	if(name!=null){
	    if(title != null){
	       topService.updateAdver(" name ='" + name +"', title = '"+title+"', url ='" +url+"'"," id="+id);
	       CacheManage.jspAdver.clear();
	       %>
	   		<script>
			alert("更新成功！");
			window.navigate("adviceManager.jsp?groupid=<%=groupid%>");
			</script>
	       <%
	    }else{%>
	    <script>
		alert("请填写正确各项参数！");
		history.back(-1);
		</script>
	    <%}
	    return;
	}
	}else{
	String name = request.getParameter("nameNew");
	String title = request.getParameter("titleNew");
	String url = request.getParameter("urlNew");
	String group = request.getParameter("groupNew");
	//判断通用户id添加
	
	if(name !=null && title !=null && url !=null && group !=null && !name.equals("") && !title.equals("") && !url.equals("") && !group.equals("")){
		         topService.addAdvice("'"+name+"',"+"'"+title+"',"+"'"+url+"','"+group+"'");    
		         CacheManage.jspAdver.clear();
		         %>
		         <script>
				alert("添加成功！");
				window.navigate("adviceManager.jsp?groupid=<%=groupid%>");
				</script>
	   <%}else{%>
	    <script>
		alert("请填写正确各项参数！");
		history.back(-1);
		</script>
	    <%}
	    return;
	}
	
//获取所有通缉令记录

Vector userList=topService.getAdverList("groupid = "+groupid);
%>
乐酷JSP广告管理后台
<hr>
<table width="50%" border="2">
<tr>
<td  align="center">序号</td>
<td  align="center" size=5>前缀</td>
<td  align="center" size=5>标题</td>
<td  align="center" size=5>URL地址</td>
<td  align="center" size=5>组别</td>
<td  align="center" size=5>操作</td>
<td  align="center" size=5>操作</td>
</tr>
<%
if(userList!=null || userList.size()>0){
AdverBean userTop=null;
for(int i = 0; i < userList.size(); i ++){
	userTop = (AdverBean) userList.get(i);
%>
<tr>
<form method="post" action="adviceManager.jsp?groupid=<%=groupid%>">
<td align="center"  name="id" size=10><%=userTop.getId()%></td>
<td ><input type="text" size=8 name="name" value=<%=userTop.getName()%>></td>
<td ><input type="text" size=22 name="title" value=<%=userTop.getTitle()%>></td>
<td ><input type="text" size=80 name="url" value=<%=userTop.getUrl()%>></td>
<td align="center" ><%=userTop.getGroup()%></td>
<input type="hidden" name="id" value=<%=userTop.getId()%>></td>
<td  align="center"><input type="submit" value="修改"></td>
</form>
<td align="center" ><a href="adviceManager.jsp?delete=<%=userTop.getId()%>&groupid=<%=groupid%>" onclick='return confirm("确定要删除吗?")'>删除</a></td>

</tr>
<%
}}else{%>
暂无排名
<%}%>
</table><br/>
增加广告
<hr>
<form method="post" action="adviceManager.jsp?groupid=<%=groupid%>">
前缀文字：<input type="text" size=30  name="nameNew" size="30"><br>
广告标题：<input type="text" size=30   name="titleNew" size="30"><br>
URL 地址：<input type="text" size=100   name="urlNew" size="30"><br>
所属组别：<input type="text" size=10   name="groupNew" size="30" value=<%=groupid%>  onkeypress="return event.keyCode>=48&&event.keyCode<=57" onpaste="return !clipboardData.getData(’text’).match(/\D/)" style="ime-mode:disabled" ondragenter="return false"><br>
<p align="center"><input type="submit" value="增加一条广告"></p>
</form>

<%
userList=topService.getAdverList("1=1 group by groupid");
%>
<table width="75%" border="2">
<%
if(userList!=null || userList.size()>0){
AdverBean userTop=null;
for(int i = 0; i <= userList.size()/10; i ++){
	
%>
<tr>
<%for(int j=0;j<10;j++) {
if(i*10+j >= userList.size())
break;
userTop = (AdverBean) userList.get(i*10+j);%>
<td align="center" width="5%"><a href="adviceManager.jsp?groupid=<%=userTop.getGroup()%>"><%=userTop.getGroup()%></a></td>
<%}%>

</tr>
<%
}}else{%>
暂无排名
<%}%>
</table><br/>