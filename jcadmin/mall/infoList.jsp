<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.bean.mall.*,net.wxsj.action.mall.*,java.util.*,net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%
MallAdminAction action = new MallAdminAction();
action.infoList(request, response);

ArrayList list = (ArrayList) request.getAttribute("list");
PagingBean paging = (PagingBean) request.getAttribute("paging");

int i, count;
InfoBean info = null;
%>
信息列表<br/>
<table width="100%" border="1">
    <tr><td><strong>序号</strong></td><td><strong>类型</strong></td><td><strong>名称</strong></td><td><strong>最后回复时间</strong></td><td><strong>分类标签</strong></td><td><strong>地区标签</strong></td><td><strong>操作</strong></td></tr>
<%
count = list.size();
for(i = 0; i < count; i ++){
	info = (InfoBean) list.get(i);
	
%>
    <tr><td><%=(i + 1)%></td><td><%=info.getInfoTypeStr()%></td><td><%if(info.getIsTop() == 1){%>【置顶】<%}%><%if(info.getIsJinghua() == 1){%>【精华】<%}%><a href="info.jsp?id=<%=info.getId()%>" target="_blank"><%=info.getName()%></a></td><td><%=info.getLastReplyTime()%></td><td><%=info.getTagListStr()%></td><td><%=info.getAreaTagListStr()%></td><td><a href="editInfo.jsp?id=<%=info.getId()%>" target="_blank">编辑</a> <a href="deleteInfo.jsp?id=<%=info.getId()%>" onclick="return confirm('确认删除？')">删除</a></td></tr>
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