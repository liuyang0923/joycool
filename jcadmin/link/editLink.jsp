<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*"%><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<p>更新用户密码&nbsp;&nbsp;<a href="index.jsp">返回</a></p>
<p align=center>
<%
ILinkService linkService = ServiceFactory.createLinkService();

int typeId = StringUtil.toInt(request.getParameter("typeId"));
if(typeId<1){
	typeId = LinkBean.TYPE_EBOOK;
}
int moduleId = StringUtil.toInt(request.getParameter("moduleId"));
if(moduleId<1){
	moduleId = LinkBean.RANDOM_MODULE;
}
int subModuleId = StringUtil.toInt(request.getParameter("subModuleId"));

int action = StringUtil.toInt(request.getParameter("action"));
if(action==1){ //更新或者添加
	int id = StringUtil.toInt(request.getParameter("id"));
    String url = request.getParameter("url");
    String desc = request.getParameter("desc");
    if(url!=null && desc!=null){   
    LinkBean bean = linkService.getLink("type_id=" + typeId + " and module_id=" + moduleId + " and sub_module_id=" + subModuleId);
    if(bean==null){ //添加
    	bean = new LinkBean();
    	bean.setTypeId(typeId);
    	bean.setModuleId(moduleId);
    	bean.setSubModuleId(subModuleId);
    	bean.setUrl(url);
    	bean.setDesc(desc);
    	linkService.addLink(bean);
    }
    else{  //更新
    	String set = " url='" + url + "', name='" + desc + "' ";
    	String condition = "id=" + id;
    	linkService.updateLink(set, condition);
    }
    OsCacheUtil.flushGroup(OsCacheUtil.LINK_GROUP);
    OsCacheUtil.flushGroup(OsCacheUtil.LINK_LIST_GROUP);
    }
} 
else if(action==2){ //删除
	int id = StringUtil.toInt(request.getParameter("id"));
	LinkBean bean = linkService.getLink("id=" + id);
	if(bean!=null){
		typeId = bean.getTypeId();
		moduleId = bean.getModuleId();
		subModuleId = bean.getSubModuleId();
		
		linkService.deleteLink("id=" + id);
		OsCacheUtil.flushGroup(OsCacheUtil.LINK_GROUP);
	    OsCacheUtil.flushGroup(OsCacheUtil.LINK_LIST_GROUP);
	}
}
else if(action==3){
	
	linkService.deleteLink("type_id=" + typeId + " and module_id=" + moduleId);	 
	OsCacheUtil.flushGroup(OsCacheUtil.LINK_GROUP);
	OsCacheUtil.flushGroup(OsCacheUtil.LINK_LIST_GROUP);

}

LinkBean bean = null;
%>
</p>
<a href="editLink.jsp?action=3&typeId=<%= typeId %>&moduleId=<%= moduleId %>">删除下面所有链接</a><br>
<table width="90%" align="left" border="1">
        <tr align="left">
		   <td width="10%">
				页面级别
			</td>
			<td width="40%">
				url
			</td>	
			<td width="30%">	
				名称
			</td>
			<td width="20%">	
				操作
			</td>
	    </tr>
        <form method="post">
        <%
            bean = linkService.getLink("type_id=" + typeId + " and module_id=" + moduleId + " and sub_module_id=1");
        %>
        <input type=hidden name="action" value="1">
        <input type=hidden name="id" value="<%= (bean==null)?"0":bean.getId()+ "" %>">
        <input type=hidden name="typeId" value="<%= typeId %>">
        <input type=hidden name="moduleId" value="<%= moduleId %>">
        <input type=hidden name="subModuleId" value="1">
		<tr align="left">
		    <td width="10%">
				第一级
			</td>
			<td width="40%">
				<input type=text name="url" value="<%= (bean==null)?"":bean.getUrl() %>" size=40>&nbsp;&nbsp;
			</td>	
			<td width="30%">	
				<input type=text name="desc" value="<%= (bean==null)?"":bean.getDesc() %>" size=20>
			</td>
			<td width="20%">	
				<input type=submit value="提交">&nbsp;
				<% if(bean!=null){ %>
				&nbsp;<a href="editLink.jsp?action=2&id=<%= bean.getId()%>">删除</a>
				<% } %>
			</td>
	    </tr>	    
		</form>
		<form method="post">
		<%
            bean = linkService.getLink("type_id=" + typeId + " and module_id=" + moduleId + " and sub_module_id=2");
        %>
        <input type=hidden name="action" value="1">
        <input type=hidden name="id" value="<%= (bean==null)?"0":bean.getId()+ "" %>">
        <input type=hidden name="typeId" value="<%= typeId %>">
        <input type=hidden name="moduleId" value="<%= moduleId %>">
        <input type=hidden name="subModuleId" value="2">
		<tr align="left">
		   <td width="10%">
				第二级
			</td>
			<td width="40%">
				<input type=text name="url" value="<%= (bean==null)?"":bean.getUrl() %>" size=40>&nbsp;&nbsp;
			</td>	
			<td width="30%">	
				<input type=text name="desc" value="<%= (bean==null)?"":bean.getDesc() %>" size=20>
			</td>
			<td width="20%">	
				<input type=submit value="提交">&nbsp;
				<% if(bean!=null){ %>
				&nbsp;<a href="editLink.jsp?action=2&id=<%= bean.getId()%>">删除</a>
				<% } %>
			</td>
	    </tr>	    
		</form>	
		<form method="post">
		<%
            bean = linkService.getLink("type_id=" + typeId + " and module_id=" + moduleId + " and sub_module_id=3");
        %>
        <input type=hidden name="action" value="1">
        <input type=hidden name="id" value="<%= (bean==null)?"0":bean.getId()+ "" %>">
        <input type=hidden name="typeId" value="<%= typeId %>">
        <input type=hidden name="moduleId" value="<%= moduleId %>">
        <input type=hidden name="subModuleId" value="3">
		<tr align="left">
		   <td width="10%">
				第三级
			</td>
			<td width="40%">
				<input type=text name="url" value="<%= (bean==null)?"":bean.getUrl() %>" size=40>&nbsp;&nbsp;
			</td>	
			<td width="30%">	
				<input type=text name="desc" value="<%= (bean==null)?"":bean.getDesc() %>" size=20>
			</td>
			<td width="20%">	
				<input type=submit value="提交">&nbsp;
				<% if(bean!=null){ %>
				&nbsp;<a href="editLink.jsp?action=2&id=<%= bean.getId()%>">删除</a>
				<% } %>
			</td>
	    </tr>	    
		</form>	
		<form method="post">
		<%
            bean = linkService.getLink("type_id=" + typeId + " and module_id=" + moduleId + " and sub_module_id=4");
        %>
        <input type=hidden name="action" value="1">
        <input type=hidden name="id" value="<%= (bean==null)?"0":bean.getId()+ "" %>">
        <input type=hidden name="typeId" value="<%= typeId %>">
        <input type=hidden name="moduleId" value="<%= moduleId %>">
        <input type=hidden name="subModuleId" value="4">
		<tr align="left">
		   <td width="10%">
				第四级
			</td>
			<td width="40%">
				<input type=text name="url" value="<%= (bean==null)?"":bean.getUrl() %>" size=40>&nbsp;&nbsp;
			</td>	
			<td width="30%">	
				<input type=text name="desc" value="<%= (bean==null)?"":bean.getDesc() %>" size=20>
			</td>
			<td width="20%">	
				<input type=submit value="提交">&nbsp;
				<% if(bean!=null){ %>
				&nbsp;<a href="editLink.jsp?action=2&id=<%= bean.getId()%>">删除</a>
				<% } %>
			</td>
	    </tr>	    
		</form>		
</table>	