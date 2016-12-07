<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.service.impl.CatalogServiceImpl,java.io.File,com.jspsmart.upload.*,jc.download.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static int COUNT_PRE_PAGE = 10;
	static CatalogServiceImpl service = new CatalogServiceImpl();
%>
<%SmartUpload smUpload = new SmartUpload();
RingAction action = new RingAction(request);
String tip = "";
PringBean bean = null;
List list = null;
List list2 = null;
CatalogBean cataBean = null;
PagingBean paging = null;
int type = 0;	// 0:列表,1:修改
int rid = action.getParameterInt("rid");
if (rid > 0){
	bean = RingAction.service.getPring(" id=" + rid);
	if (bean == null){
		tip ="要查找的铃声不存在.";
	} else {
		type = 1;
		list2 = service.getCatalogList(" type='ring' and visible=1");
		if (action.getParameterInt("s")==1){
			// 提交修改
			smUpload.initialize(pageContext);
			smUpload.upload();
			if (action.modifyRing(smUpload,bean)){
				// 清缓存
				net.joycool.wap.cache.OsCacheUtil.flushGroup("ring");
				type = 0;
			} else {
				tip = (String)request.getAttribute("tip");
			}
		}
	}
}
if (type == 0){
paging = new PagingBean(action,1000,COUNT_PRE_PAGE,"p");
list = RingAction.service.getPringsList(" 1 order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
}
%>
<html>
	<head>
		<title>修改游戏</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
	<%=tip%><br/>
<%if (type == 1){
%>修改铃声:<br/>
		<form action="ringUp2.jsp?rid=<%=bean.getId()%>&s=1" method="post" enctype="multipart/form-data">
			歌名(50字内):<input type="text" name="sname" value="<%=StringUtil.toWml(bean.getName())%>"><br/>
			演唱(50字内):<input type="text" name="singer" value="<%=StringUtil.toWml(bean.getSinger())%>"><br/>
			<%if (list2 != null && list2.size() > 0){
				%>类型:<select name="cata">
						<%for (int i = 0 ; i < list2.size() ; i++){
							cataBean = (CatalogBean)list2.get(i);
							if (cataBean != null){
								%><option value="<%=cataBean.getId()%>"><%=StringUtil.toWml(cataBean.getName())%></option><%
							}
						}
						%>
				  </select><br/><script language="javascript">document.forms[0].cata.value="<%=bean.getCatalogId()%>";</script><%	
			}%>
			铃声:<input type="file" name="ring"><br/>
			<input type="submit" value="修改">
			<input id="cmd" type="button" value="回列表" onClick="javascript:window.location.href='ringUp2.jsp'">
		</form>
<%
} else {
%><input id="cmd" type="button" value="首页" onClick="javascript:window.location.href='index.jsp'"><br/>
<table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align=center>
			<font color=#1A4578>ID</font>
		</td>
		<td align=center>
			<font color=#1A4578>类别</font>
		</td>
		<td align=center>
			<font color=#1A4578>歌曲</font>
		</td>
		<td align=center>
			<font color=#1A4578>演唱</font>
		</td>
		<td align=center>
			<font color=#1A4578>type_id</font>
		</td>
		<td align=center>
			<font color=#1A4578>文件</font>
		</td>
		<td align=center>
			<font color=#1A4578>user_id</font>
		</td>
		<td align=center>
			<font color=#1A4578>创建时间</font>
		</td>
		<td align=center>
			<font color=#1A4578>下载量</font>
		</td>
		<td align=center>
			<font color=#1A4578>操作</font>
		</td>
	</tr>
	<% if (list != null && list.size() > 0){
		for (int i = 0 ; i < list.size() ; i++){
			bean = (PringBean)list.get(i);
			if (bean != null){
				%><tr>
					<td><%=bean.getId() %></td>
					<td><%=bean.getCatalogId()%></td>
					<td><%=StringUtil.toWml(bean.getName())%></td>
					<td><%=StringUtil.toWml(bean.getSinger())%></td>
					<td><%=bean.getTypeId()%></td>
					<td><%=StringUtil.toWml(bean.getFile())%></td>
					<td><%=bean.getUserId()%></td>
					<td><%=DateUtil.formatSqlDatetime(bean.getCreateDatetime())%></td>
					<td><%=bean.getDownloadSum()%></td>
					<td><a href="ringUp2.jsp?rid=<%=bean.getId()%>">修改</a></td>
				</tr><%
			}
		}
	}%>
</table>
<%=paging!=null?paging.shuzifenye("ringUp2.jsp",false,"|",response):""%><%
}
%>
	</body>
</html>