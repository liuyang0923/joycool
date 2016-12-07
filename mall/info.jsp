<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.framework.*,net.wxsj.bean.mall.*,net.wxsj.action.mall.*,java.util.*,net.wxsj.util.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.bean.UserBean,net.joycool.wap.util.PageUtil"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);

MallAction action = new MallAction();
action.info(request, response);

ArrayList replyList = (ArrayList) request.getAttribute("replyList");
InfoBean info = (InfoBean) request.getAttribute("info");
InfoBean next = (InfoBean) request.getAttribute("next");
InfoBean prev = (InfoBean) request.getAttribute("prev");
PagingBean paging = (PagingBean) request.getAttribute("paging");

int i, count;
ReplyBean reply = null;

BackBean mallInfoBack = (BackBean) session.getAttribute("mallInfoBack");
%>
<wml>
<card title="乐乐卖场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="index.jsp">卖场</a><%if(mallInfoBack != null){%>><a href="<%=(mallInfoBack.getUrl().replace("&", "&amp;"))%>"><%=mallInfoBack.getTitle()%></a><%}%>>信息详情<br/>
[<%=info.getInfoTypeStr1()%>]<%if(info.getIsTop() == 1){%>[顶]<%}%><%if(info.getIsJinghua() == 1){%>[精]<%}%><%=StringUtil.toWml(info.getName())%><br/>
--------<br/>
信息编号：<%=info.getId()%><br/>
发布者：<a href="/chat/post.jsp?toUserId=<%=info.getUserId()%>"><%=StringUtil.toWml(info.getUserNick())%></a>|<%=info.getCreateDatetime().substring(5, 16)%><br/>
期望价格：<%=StringUtil.toWml(info.getPrice())%><br/>
详细描述：<%=StringUtil.toWml(info.getIntro())%><br/>
交易方式：<%=StringUtil.toWml(info.getBuyMode())%><br/>
联系电话：<%=StringUtil.toWml(info.getTelephone())%>(<%if(info.getValidated() == 0){%>未验证<%if(loginUser != null && loginUser.getId() == info.getUserId()){%> <a href="validate.jsp?id=<%=info.getId()%>">验证</a><%}}else{%>已验证<%}%> <a href="validate.jsp">说明</a>)<br/>
所在地区：<%=StringUtil.toWml(info.getAddress())%><br/>
标签：<%
ArrayList tagList = info.getTagList();
TagBean tag = null;
count = tagList.size();
for(i = 0; i < count; i ++){
	tag = (TagBean) tagList.get(i);
	if(i > 0){
		out.println(" ");
	}
%><a href="infoList.jsp?tagId=<%=tag.getId()%>"><%=tag.getName()%></a><%
}

ArrayList areaTagList = info.getAreaTagList();
AreaTagBean areaTag = null;
count = areaTagList.size();
for(i = 0; i < count; i ++){
	areaTag = (AreaTagBean) areaTagList.get(i);	
	out.println(" ");
%><a href="infoList.jsp?areaTagId=<%=areaTag.getId()%>"><%=areaTag.getName()%></a><%
}
%><br/>
浏览次数：<%=info.getHits()%><br/>
+点评信息+<br/>
<a href="postReply.jsp?parentId=<%=info.getId()%>">我要点评</a><br/>
<%
count = replyList.size();
for(i = 0; i < count; i ++){
	reply = (ReplyBean) replyList.get(i);
	
%>
<%=(i + 1 + paging.getCurrentPageIndex() * 8)%>.<a href="/chat/post.jsp?toUserId=<%=reply.getUserId()%>"><%=StringUtil.toWml(reply.getUserNick())%></a>:<%=StringUtil.toWml(reply.getContent())%><br/>
<%
}

String fenye = PageUtil.shuzifenye(paging, paging.getPrefixUrl(), true, "|", response);
if(fenye != null && !"".equals(fenye)){
%>
<%=fenye%>
<%
}

if(next != null){
%>
下一条:<a href="info.jsp?id=<%=next.getId()%>">[<%=next.getInfoTypeStr1()%>]<%if(next.getIsTop() == 1){%>[置顶]<%}%><%if(next.getIsJinghua() == 1){%>[精华]<%}%><%=StringUtil.toWml(next.getName())%></a><br/>
<%
}
if(prev != null){
%>
上一条:<a href="info.jsp?id=<%=prev.getId()%>">[<%=prev.getInfoTypeStr1()%>]<%if(prev.getIsTop() == 1){%>[置顶]<%}%><%if(prev.getIsJinghua() == 1){%>[精华]<%}%><%=StringUtil.toWml(prev.getName())%></a><br/>
<%
}
%>
<a href="index.jsp">返回卖场首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>