<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.mall.*,net.wxsj.action.mall.*,java.util.*,net.wxsj.util.*,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.PageUtil"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%
response.setHeader("Cache-Control","no-cache");

MallAction action = new MallAction();
action.infoList(request, response);

ArrayList hotTagList = (ArrayList) request.getAttribute("hotTagList");
ArrayList hotAreaTagList = (ArrayList) request.getAttribute("hotAreaTagList");
PagingBean paging = (PagingBean) request.getAttribute("paging");
ArrayList list = (ArrayList) request.getAttribute("list");

String name = (String) request.getAttribute("name");
int infoType = StringUtil.toInt((String) request.getAttribute("infoType"));
int id = StringUtil.toInt((String) request.getAttribute("id"));
int userId = StringUtil.toInt((String) request.getAttribute("userId"));
int tagId = StringUtil.toInt((String) request.getAttribute("tagId"));
int areaTagId = StringUtil.toInt((String) request.getAttribute("areaTagId"));
TagBean cTag = (TagBean) request.getAttribute("tag");
AreaTagBean cAreaTag = (AreaTagBean) request.getAttribute("areaTag");

int i, count;
TagBean tag = null;
AreaTagBean aTag = null;
InfoBean info = null;

String title = null;
%>
<wml>
<card title="乐乐卖场">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="post.jsp">发新帖子</a>|<a href="myPost.jsp">我的帖子</a><br/>
<%
//搜索
if(name != null || id > 0 || userId > 0){
%>
<a href="index.jsp">卖场</a>>搜索结果<br/>
<%
}
//分类标签
else if(tagId > 0){
	title = "其他";
	if(cTag != null){
		title = cTag.getName();
	}
%>
<a href="index.jsp">卖场</a>><%=title%><br/>
<%
	if(infoType == InfoBean.BUY){
%>
<a href="infoList.jsp?tagId=<%=tagId%>">全部</a>|求购类|<a href="infoList.jsp?tagId=<%=tagId %>&amp;infoType=<%=InfoBean.SELL%>">出售类</a><br/>
<%
    }
    else if(infoType == InfoBean.SELL){
%>
<a href="infoList.jsp?tagId=<%=tagId%>">全部</a>|<a href="infoList.jsp?tagId=<%=tagId %>&amp;infoType=<%=InfoBean.BUY%>">求购类</a>|出售类<br/>
<%
    }
    else {
%>
全部|<a href="infoList.jsp?tagId=<%=tagId %>&amp;infoType=<%=InfoBean.BUY%>">求购类</a>|<a href="infoList.jsp?tagId=<%=tagId %>&amp;infoType=<%=InfoBean.SELL%>">出售类</a><br/>
<%
    }
}
//地区标签
else if(areaTagId > 0){
	title = "其他";
	if(cAreaTag != null){
		title = cAreaTag.getName();
	}
%>
<a href="index.jsp">卖场</a>><%=title%><br/>
<%
	if(infoType == InfoBean.BUY){
%>
<a href="infoList.jsp?areaTagId=<%=areaTagId%>">全部</a>|求购类|<a href="infoList.jsp?areaTagId=<%=areaTagId %>&amp;infoType=<%=InfoBean.SELL%>">出售类</a><br/>
<%
    }
    else if(infoType == InfoBean.SELL){
%>
<a href="infoList.jsp?areaTagId=<%=areaTagId%>">全部</a>|<a href="infoList.jsp?areaTagId=<%=areaTagId %>&amp;infoType=<%=InfoBean.BUY%>">求购类</a>|出售类<br/>
<%
    }
    else {
%>
全部|<a href="infoList.jsp?areaTagId=<%=areaTagId %>&amp;infoType=<%=InfoBean.BUY%>">求购类</a>|<a href="infoList.jsp?areaTagId=<%=areaTagId %>&amp;infoType=<%=InfoBean.SELL%>">出售类</a><br/>
<%
    }
}
//按买/卖
else if(infoType >= 0){
	if(infoType == InfoBean.BUY){
%>
<a href="index.jsp">卖场</a>>求购类<br/>
<a href="infoList.jsp">全部</a>|求购类|<a href="infoList.jsp?infoType=<%=InfoBean.SELL%>">出售类</a><br/>
<%
    }
    else if(infoType == InfoBean.SELL){
%>
<a href="index.jsp">卖场</a>>出售类<br/>
<a href="infoList.jsp">全部</a>|<a href="infoList.jsp?infoType=<%=InfoBean.BUY%>">求购类</a>|出售类<br/>
<%
    }
}
//全部
else {
%>
<a href="index.jsp">卖场</a>>全部信息<br/>
<a href="infoList.jsp?infoType=<%=InfoBean.BUY%>">求购类</a>|<a href="infoList.jsp?infoType=<%=InfoBean.SELL%>">出售类</a><br/>
<%
}

//信息列表
count = list.size();
for(i = 0; i < count; i ++){
	info = (InfoBean) list.get(i);	
%><%=(i + 1 + paging.getCurrentPageIndex() * 8)%>.<%if(info.getIsTop() == 1){%>[顶]<%} else 	if(info.getIsJinghua() == 1){%>[精]<%}%><a href="info.jsp?id=<%=info.getId()%>">[<%=info.getInfoTypeStr1()%>]<%=StringUtil.toWml(info.getName())%></a><br/><%
}

if(count == 0){
%>
没有找到你要找的东西，换个别的试试吧。<br/>
<%
}
boolean addAnd = true;
if(paging.getPrefixUrl().indexOf("?") == -1){
	addAnd = false;
}

String fenye = PageUtil.shuzifenye(paging, paging.getPrefixUrl(), addAnd, "|", response);
if(fenye != null && !"".equals(fenye)){
%>
<%=fenye%>
<%
}
%>
热点：<%
count = hotTagList.size();
for(i = 0; i < count; i ++){
	tag = (TagBean) hotTagList.get(i);
	if(i > 0){
		out.println(" ");
	}
%><a href="infoList.jsp?tagId=<%=tag.getId()%>"><%=tag.getName()%></a><%
}
%> <a href="tagList.jsp">更多</a><br/>
地区：<%
count = hotAreaTagList.size();
for(i = 0; i < count; i ++){
	aTag = (AreaTagBean) hotAreaTagList.get(i);
	if(i > 0){
		out.println(" ");
	}
%><a href="infoList.jsp?areaTagId=<%=aTag.getId()%>"><%=aTag.getName()%></a><%
}
%> <a href="infoList.jsp?areaTagId=999999">其他</a><br/>
<a href="index.jsp">返回卖场首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>