<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="jc.imglib.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.bean.auction.*,net.joycool.wap.framework.*"%>
<%
ImgLibAction action = new ImgLibAction(request);
PagingBean paging = null;
List list = null;
int pageNow = 0;
int userId = action.getParameterInt("uid");
if (userId > 0){
	LibUser libUser = ImgLibAction.service.getLibUser(" user_id=" + userId);
	if(libUser!=null){
		paging = new PagingBean(action, libUser.getCount(), 20, "p");
		list = ImgLibAction.service.getLibList(" user_id=" + userId+" limit "+paging.getStartIndex()+",20");
	}
}%>
<html>
<head><link href="../farm/common.css" rel="stylesheet" type="text/css">
</head>
<body>
<table border=1 width=500 align=center>
	<tr bgcolor=#C6EAF5>
		<td align=center>
			<font color=#1A4578>id</font>
		</td>
		<td align=center>
			<font color=#1A4578>名称</font>
		</td>
		<td align=center>
			<font color=#1A4578></font>
		</td>
		<td align=center>
			<font color=#1A4578></font>
		</td>
	</tr>
	<% if (list != null && list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				Lib lib = (Lib)list.get(i);
				if (lib != null){
					%><tr>
						<td><%=lib.getId()%></td>
						<td><%=lib.getTitle()%></td>
						<td><img src="/rep<%=lib.getImg()%>"/></td>
						<td><%=DateUtil.formatDate2(lib.getCreateTime())%></td>
					  </tr><%
				}
			}
	   }%>
</table>
<%=paging != null?paging.shuzifenye("imglib.jsp?uid=" + userId, true, "|", response):""%>
<body>
</html>