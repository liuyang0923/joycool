<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*, java.util.*, net.joycool.wap.bean.*"%><%
//重新读入返回进入页模块配置
PositionUtil.loadModuleConf();
%>
<table width="90%" align="center" border="1">
<% 
Hashtable moduleHash = PositionUtil.moduleHash;
Enumeration enu = moduleHash.keys();
while (enu.hasMoreElements()) {
	String moduleUrl = (String) enu.nextElement();

	ModuleBean module = (ModuleBean) moduleHash.get(moduleUrl);
	if (module != null) {
%>
        <tr>
			<td>
				<%= module.getName() %>
			</td>
			<td>
				<%= module.getImage() %>
			</td>
			<td>
				<%= module.getPriority() %>
			</td>
		</tr>
<%
	}
}
%>
</table>
