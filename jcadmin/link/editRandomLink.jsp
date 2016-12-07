<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*"%><script language="javascript">
    function confirmIt(message, url){
        if(confirm(message)){
            window.location=url;
        }
    }
</script>                 
随机刷新链接&nbsp;&nbsp;<a href="index.jsp">返回</a><br>
<%
ILinkService linkService = ServiceFactory.createLinkService();

int typeId = StringUtil.toInt(request.getParameter("typeId"));
int moduleId = LinkBean.RANDOM_MODULE;
int subModuleId = StringUtil.toInt(request.getParameter("subModuleId"));
if(subModuleId<0){
	subModuleId=0;
}

int action = StringUtil.toInt(request.getParameter("action"));
if(action==1){ //添加   
	String url = request.getParameter("url");   
    String desc = request.getParameter("desc");   
    if(url!=null && desc!=null){
    
//    LinkBean bean = linkService.getLink("type_id=" + typeId + " and module_id=" + moduleId + " and sub_module_id=" + subModuleId + " and url='" + url + "'");
//    if(bean==null){  
        LinkBean bean = new LinkBean();
        bean.setTypeId(typeId);
        bean.setModuleId(moduleId);
        bean.setSubModuleId(subModuleId);
        bean.setUrl(url);
        bean.setDesc(desc);
        
        linkService.addLink(bean);
    
        OsCacheUtil.flushGroup(OsCacheUtil.LINK_GROUP);
        OsCacheUtil.flushGroup(OsCacheUtil.LINK_LIST_GROUP);
//    }  
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
if(action==3){ //修改
	int id = StringUtil.toInt(request.getParameter("id"));
    String url = request.getParameter("url");   
    String desc = request.getParameter("desc");
    if(url!=null && desc!=null){ 
    	LinkBean bean = linkService.getLink("id=" + id);
    	if(bean!=null){
    		typeId = bean.getTypeId();
    		moduleId = bean.getModuleId();
    		subModuleId = bean.getSubModuleId();
    		
    	    String set = " url='" + url + "', name='" + desc + "' ";
    	    String condition = "id=" + id;
    	    linkService.updateLink(set, condition); 
    	    
    	    OsCacheUtil.flushGroup(OsCacheUtil.LINK_GROUP);
            OsCacheUtil.flushGroup(OsCacheUtil.LINK_LIST_GROUP);
    	}    
    }  
}

%>
<table width=780 border="1">  
        <tr align="left">
		   <td width="10%">
				序号
			</td>
			<td width="40%">
				URL
			</td>
			<td width="30%">
				名称
			</td>
			<td width="10%">
				修改
			</td>
			<td width="10%">
				删除
			</td>
	    </tr>    
		<%
            List beanList = linkService.getLinkList("type_id=" + typeId + " and module_id=" + LinkBean.RANDOM_MODULE + " and sub_module_id=" + subModuleId);
		    //if(beanList!=null)
		    {
		    	for(int i=0;beanList!=null && i<beanList.size();i++){
		    		LinkBean bean = (LinkBean)beanList.get(i);
		    		if(bean==null)continue;	    		
		    		%>
		<form method="post"> 
		<input type=hidden name="action" value="3">	
		<input type=hidden name="id" value="<%= bean.getId() %>"> 		
		<tr align="left">
		   <td width="10%">
				<%= i+1 %>
			</td>
			<td width="40%">
				<input type=text name="url" value="<%= bean.getUrl() %>" size=50>
			</td>
			<td width="30%">
				<input type=text name="desc" value="<%= bean.getDesc() %>" size=30>
			</td>
			<td width="10%">
			    <input type=submit value="修改">&nbsp;&nbsp;
			    </form>
			</td>
			<td width="10%">    
			    <form method="post">
			      <input type=hidden name="action" value="2">
			      <input type=hidden name="id" value="<%= bean.getId() %>"> 
			      <input type=submit value="删除">&nbsp;&nbsp;
			    </form>  
				<!-- <a href="editRandomLink.jsp?action=2&id=<%= bean.getId()%>">删除</a>  -->
			</td>
	    </tr>
		    		<% 
		    	}
		    }
        %>
</table>

<form method="post">
<table width=780 border="1">  
        <input type=hidden name="action" value="1">		
        <input type=hidden name="typeId" value="<%= typeId %>">
        <input type=hidden name="moduleId" value="<%= LinkBean.RANDOM_MODULE %>">
        <input type=hidden name="subModuleId" value="<%= subModuleId %>">
		<tr align="left">		   
			<td width="10%">
				url				
			</td>
			<td width="80%">
				<input type=text name="url" value="" size=70>&nbsp;&nbsp;				
			</td>
	    </tr>
	    <tr align="left">
	        <td width="10%">
				名称			
			</td>		   
			<td width="70%">
				<input type=text name="desc" value="" size=40> &nbsp;
				<input type=submit value="添加">
			</td>
	    </tr>	    	    	
</table>	
</form>	