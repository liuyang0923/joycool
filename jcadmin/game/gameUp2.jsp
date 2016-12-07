<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.service.impl.CatalogServiceImpl,java.io.File,com.jspsmart.upload.*,jc.download.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! static int COUNT_PRE_PAGE = 10;
    static CatalogServiceImpl service = new CatalogServiceImpl();
%>
<%SmartUpload smUpload = new SmartUpload();
GameAction action = new GameAction(request);
String tip = "";
GameBean bean = null;
List list = null;
List list2 = null;
CatalogBean cataBean = null;
PagingBean paging = null;
int type = 0;	// 0:列表,1:修改
int mid = action.getParameterInt("mid");
if (mid > 0){
	bean = GameAction.service.getGame(" id=" + mid);
	if (bean == null){
		tip ="要查找的游戏不存在.";
	} else {
		type = 1;
		list2 = service.getCatalogList(" type='wapgame' and visible=1");
		if (action.getParameterInt("s")==1){
			// 提交修改
			smUpload.initialize(pageContext);
			smUpload.upload();
			if (action.modifyGame(smUpload,bean)){
				// 清缓存
				net.joycool.wap.cache.OsCacheUtil.flushGroup("game");
				type = 0;
			} else {
				tip = (String)request.getAttribute("tip");
			}
		}
	}
}
if (type == 0){
paging = new PagingBean(action,1000,COUNT_PRE_PAGE,"p");
list = GameAction.service.getGamesList(" 1 order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
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
%>修改游戏:<br/>
		<form action="gameUp2.jsp?mid=<%=bean.getId()%>&s=1" method="post" enctype="multipart/form-data">
			游戏名(100字内):<input type="text" name="gname" value="<%=bean.getName()%>" /><br/>
			描述:<br/><textarea name="description" cols="80" rows="10"><%=bean.getDescription()%></textarea><br/>
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
			图片:<input type="file" name="image" /><br/>原图片:<br/>
			<img src="<%=action.getGameUrlPath() + "pic/" + bean.getPicUrl()%>?<%=RandomUtil.nextInt(1000000) %>" alt="无法显示" /><br/>
			游戏:<input type="file" name="game" /><br/>
			<input type="submit" value="修改">
			<input id="cmd" type="button" value="回列表" onClick="javascript:window.location.href='gameUp2.jsp'">
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
			<font color=#1A4578>名称</font>
		</td>
		<td align=center>
			<font color=#1A4578>描述</font>
		</td>
		<td align=center>
			<font color=#1A4578>大小</font>
		</td>
		<td align=center>
			<font color=#1A4578>游戏地址</font>
		</td>
		<td align=center>
			<font color=#1A4578>图片地址</font>
		</td>
		<td align=center>
			<font color=#1A4578>点击</font>
		</td>
		<td align=center>
			<font color=#1A4578>类别</font>
		</td>
		<td align=center>
			<font color=#1A4578>创建者</font>
		</td>
		<td align=center>
			<font color=#1A4578>创建时间</font>
		</td>
		<td align=center>
			<font color=#1A4578>更新者</font>
		</td>
		<td align=center>
			<font color=#1A4578>更新时间</font>
		</td>
		<td align=center>
			<font color=#1A4578>provider_id</font>
		</td>
		<td align=center>
			<font color=#1A4578>mark</font>
		</td>
		<td align=center>
			<font color=#1A4578>操作</font>
		</td>
	</tr>
	<% if (list != null && list.size() > 0){
		for (int i = 0 ; i < list.size() ; i++){
			bean = (GameBean)list.get(i);
			if (bean != null){
				%><tr>
					<td><%=bean.getId() %></td>
					<td><%=StringUtil.toWml(bean.getName())%></td>
					<td><%=StringUtil.toWml(bean.getDescription())%></td>
					<td><%=bean.getKb()%></td>
					<td><%=bean.getFileUrl()%></td>
					<td><%=bean.getPicUrl()%></td>
					<td><%=bean.getHits()%></td>
					<td><%=bean.getCatalogId()%></td>
					<td><%=bean.getCreateUserId()%></td>
					<td><%=DateUtil.formatSqlDatetime(bean.getCreateDatetime())%></td>
					<td><%=bean.getUpdateUserId()%></td>
					<td><%=DateUtil.formatSqlDatetime(bean.getUpdateDatetime())%></td>
					<td><%=bean.getProviderId()%></td>
					<td><%=bean.getMark()%></td>
					<td><a href="gameUp2.jsp?mid=<%=bean.getId()%>">修改</a></td>
				</tr><%
			}
		}
	}%>
</table>
<%=paging!=null?paging.shuzifenye("gameUp2.jsp",false,"|",response):""%><%
}
%>
	</body>
</html>