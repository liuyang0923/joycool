<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.io.File"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%!
static IUserService userService = ServiceFactory.createUserService();
Integer zero = new Integer(0);
static String[] typeName = {"","帖子图片","回复图片","聊天图片","用户头像","家园相册","图库","可信度","家族","家族相册"};
static String[] paths = {"","jcforum/","jcforum/","chat/","friend/attach/","home/myalbum/","","credit/","family/","family/photo/"};
%><%
CustomAction action = new CustomAction(request);
int redo = action.getParameterInt("re");
if(redo>0) {
    SqlUtil.executeUpdate("insert ignore into img_check select * from img_check_his where id="+redo);
    SqlUtil.executeUpdate("delete from img_check_his where id="+redo);
}
PagingBean paging = new PagingBean(action,500000,30,"p");
%>
<html>
<head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body onkeydown="if(event.keyCode==39){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()+1%>';return false;}else if(event.keyCode==37){window.location='<%=request.getRequestURI()%>?p=<%=paging.getCurrentPageIndex()-1%>';return false;}else return true;">
<font color=red>先审核再发历史</font>-<a href="img.jsp?p=<%=paging.getCurrentPageIndex()%>">刷新</a>
<table width="800" border="2">
<tr>
<td>id</td><td width="60">id2</td>
<td>用户</td>
<td>时间</td>
<td align=center>类型</td>
<td>结果</td>
</tr>
<%
//显示封禁列表
List logList = SqlUtil.getObjectsList("select id,id2,type,file,create_time,bak from img_check_his order by id desc limit " + paging.getStartIndex()+",30");
if(logList != null){
	Iterator itr = logList.iterator();
	int i = 1;
	while(itr.hasNext()){
	Object[] objs = (Object[])itr.next();
	int type=((Long)objs[2]).intValue();
	String imgfile = objs[3].toString();
%>
<tr>
<td width="60"><%=objs[0]%></td><td width="60"><%=objs[1]%></td>
<td width="50"></td>
<td width="120"><%=objs[4]%></td>
<td align=center width=100><%=typeName[type]%></td>
<td><img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%><%if(!imgfile.startsWith("/")){%>/<%=paths[type]%><%}%><%=imgfile%>" alt="x"/></td>
<td><a href="imghis.jsp?re=<%=objs[0]%>&p=<%=paging.getCurrentPageIndex()%>"><font color=red>重审</font></a></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<%=paging.shuzifenye("imghis.jsp",false,"|",response)%><br/>
<p align="center"><a href="../chatmanage.jsp">返回</a></p>
<body>
</html>