<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end

String contentv=null;
//判断添加脏话
String contentaddd=(String)request.getParameter("content");
String contentadd=null;
if(contentaddd!=null){
	contentadd=contentaddd.trim();
   if(!(contentadd.equals(""))){
   Vector contentList = ContentList.getContentList();
   synchronized (contentList) {
   int count =contentList.size();
   for(int i=0;i<count;i++){
       contentv=(String)contentList.get(i);
		if(contentadd.equals(contentv)){
	   //response.sendRedirect("forcontent.jsp");
	   BaseAction.sendRedirect("/jcadmin/forcontent.jsp", response);
	   return;
	   }
   }
   contentList.add(contentadd);
   //response.sendRedirect("forcontent.jsp");
   BaseAction.sendRedirect("/jcadmin/forcontent.jsp", response);
	   return;
   }
   }
}
//判断删除脏话
if(request.getParameter("delete") != null){
	Vector contentList = ContentList.getContentList();
	int deleteId = Integer.parseInt(request.getParameter("delete"));
     deleteId=deleteId-1;
	contentList.remove(deleteId);
	//response.sendRedirect("forcontent.jsp");
	BaseAction.sendRedirect("/jcadmin/forcontent.jsp", response);
	return;
}
//判断重新设定
if(request.getParameter("clear") != null){
	Vector contentList = ContentList.getContentList();
	if(contentList != null){
		contentList.removeAllElements();		
	}
	contentList.add("我靠");
	contentList.add("我操");
	contentList.add("fuck");
	contentList.add("FUCK");
	contentList.add("bitch");
	contentList.add("他妈的");
	contentList.add("性爱");
	contentList.add("鸡巴");
	contentList.add("法轮功");
	contentList.add("法轮大法");
	contentList.add("falungong");
	contentList.add("falundafa");
	contentList.add("falun");
	contentList.add("江泽民");
	contentList.add("共产党");
	contentList.add("操你妈");
	contentList.add("操你妈逼");
	contentList.add("你妈逼");
	contentList.add("三级片");
	contentList.add("月赚千元");
	contentList.add("http://www.");
	contentList.add("操你妈");
	contentList.add("傻逼");
	contentList.add("操你大爷");
	contentList.add("你妈的");
	contentList.add("滚蛋");
	contentList.add("www.");
	contentList.add("wap.");
	contentList.add(".com");
	contentList.add(".cn");
	//response.sendRedirect("forcontent.jsp");
	BaseAction.sendRedirect("/jcadmin/forcontent.jsp", response);
	return;
}
%>
<html>
<head>
 <script language="javascript" >
function checkform(){
			if(document.bcontent.content.value == ''){
				alert("内容不能为空！");
				return false;
			}else{
				  return true;
				}
		}
</script>
</head>
<body>
<p><a href="forcontent.jsp?clear=1">重新设定</a></p>
<p>脏话列表</p>
<table width="100%" border="2">
<%
//显示脏话列表
Vector contentList = ContentList.getContentList();
if(contentList != null){
	Iterator itr = contentList.iterator();
	int i = 1;
	while(itr.hasNext()){
%>
<tr>
<td width="10%"><%=i%></td>
<td width="80%"><%=((String)itr.next())%></td>
<td width="10%"><a href="forcontent.jsp?delete=<%=i%>">删除</a></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<form name="bcontent" action="forcontent.jsp" method="post" onsubmit="return checkform()">
内容:<input name="content" type="text"  size="38" >
<input type="submit" value="提交">
<input type="reset" value="重置">
 </form>
<body>
</html>