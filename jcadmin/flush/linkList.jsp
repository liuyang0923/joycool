<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.bean.flush.*,net.wxsj.action.flush.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.bean.PagingBean"%><%
FlushAdminAction action = new FlushAdminAction();
action.linkList(request, response);

ArrayList list = (ArrayList) request.getAttribute("list");
PagingBean paging = (PagingBean) request.getAttribute("paging");

int i, count;
LinkBean bean = null;
%>
友链列表<br/>
<table width="100%" border="1">
    <tr>
	<td><strong>序号</strong></td>
	<td><strong>标题</strong></td>
	<td><strong>地址</strong></td>
	<td><strong>最大点</strong></td>
	<td><strong>最大号</strong></td>
	<td><strong>当前点</strong></td>
	<td><strong>当前号</strong></td>
	<td><strong>脚本</strong></td>
	<td><strong>操作</strong></td>
    </tr>
<%
count = list.size();
for(i = 0; i < count; i ++){
	bean = (LinkBean) list.get(i);	
%>
    <tr>
	<td><%=(i + 1)%></td>
	<td><%=bean.getName()%></td>
	<td><%=bean.getLink()%></td>
	<td><%=bean.getMaxHits()%></td>
	<td><%=bean.getMaxMobile()%></td>
	<td><%=bean.getCurrentHits()%></td>
	<td><%=bean.getCurrentMobile()%></td>
	<td><%=bean.getScript()%></td>	
	<td><a href="editLink.jsp?id=<%=bean.getId()%>">编辑</a> <a href="deleteLink.jsp?id=<%=bean.getId()%>" onclick="return confirm('确认删除？')">删除</a> <a href="history.jsp?id=<%=bean.getId()%>" target="_blank">刷量记录</a></td>
	</tr>
<%
}
%>
</table>

<%
String fenye = PageUtil.shuzifenye(paging, paging.getPrefixUrl(), false, " ", response);
if(fenye != null && !"".equals(fenye)){
%>
<%=fenye%>
<%
}
%>

<form action="addLink.jsp" method="post">
<table width="100%" border="0">
  <tr>
    <td width="100">标题：</td>
	<td><input type="text" name="name" size="50" value=""/></td>
  </tr>
  <tr>
    <td>地址：</td>
	<td><input type="text" name="link" size="50" value="http://"/></td>
  </tr>
  <tr>
    <td>备注：</td>
	<td><textarea name="remark" cols="50" rows="5"></textarea></td>
  </tr>
  <tr>
    <td>最大点击数：</td>
	<td><input type="text" name="maxHits" size="5" value="100"/></td>
  </tr>
  <tr>
    <td>最大手机号：</td>
	<td><input type="text" name="maxMobile" size="5" value="100"/></td>
  </tr>
  <tr>
    <td>脚本：</td>
	<td><textarea name="script" cols="50" rows="5"></textarea></td>
  </tr>  
  <tr>
    <td></td>
	<td><input type="submit" name="B" value="添加友链"/></td>
  </tr>
</table>
</form>