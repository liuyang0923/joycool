<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.wxsj.bean.mall.*,net.wxsj.action.mall.*,java.util.*"%><%
MallAdminAction action = new MallAdminAction();
action.info(request, response);

ArrayList replyList = (ArrayList) request.getAttribute("replyList");
InfoBean info = (InfoBean) request.getAttribute("info");

ReplyBean reply = null;
%>
[<%=info.getInfoTypeStr()%>]<%=info.getName()%><br/>
作者：<%=info.getUserNick()%> ID：<%=info.getUserId()%><br/>
介绍：<%=info.getIntro()%><br/>
分类标签：<%=info.getTagListStr()%><br/>
地区标签：<%=info.getAreaTagListStr()%><br/>
验证信息：<%=(info.getValidated() == 0 ? "未验证" : "已验证")%><br/>
期望价格：<%=info.getPrice()%><br/>
交易方式：<%=info.getBuyMode()%><br/>
联系电话：<%=info.getTelephone()%><br/>
所在地区：<%=info.getAddress()%><br/>
最后回复时间：<%=info.getLastReplyTime()%><br/>
回复数：<%=info.getReplyCount()%><br/>
发表时间：<%=info.getCreateDatetime()%><br/>
置顶：<%=(info.getIsTop() == 0 ? "否" : "是")%><br/>
精华：<%=(info.getIsJinghua() == 0 ? "否" : "是")%><br/>
<a href="editInfo.jsp?id=<%=info.getId()%>">编辑</a> <a href="deleteInfo.jsp?id=<%=info.getId()%>" onclick="return confirm('确认删除？')">删除</a><br/>
<br/>
回复列表<br/>
<table width="100%" border="1">
    <tr><td><strong>序号</strong></td><td><strong>内容</strong></td><td><strong>时间</strong></td><td><strong>用户</strong></td><td><strong>操作</strong></td></tr>
<%
int i, count;
count = replyList.size();
for(i = 0; i < count; i ++){
	reply = (ReplyBean) replyList.get(i);
	
%>
    <tr><td><%=(i + 1)%></td><td><%=reply.getContent()%></td><td><%=reply.getCreateDatetime()%></td><td><%=reply.getUserNick()%> ID：<%=reply.getUserId()%></td><td><a href="editReply.jsp?id=<%=reply.getId()%>">编辑</a> <a href="deleteReply.jsp?id=<%=reply.getId()%>" onclick="return confirm('确认删除？')">删除</a></td></tr>
<%
}
%>
</table>