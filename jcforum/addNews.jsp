<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.action.jcforum.*" %><%
response.setHeader("Cache-Control","no-cache");
ForumxAction action=new ForumxAction(request);
UserBean loginUser = action.getLoginUser();
if(loginUser==null||!ForbidUtil.isForbid("newsa",loginUser.getId())){
	response.sendRedirect("index.jsp");
	return;
}

int id = action.getParameterInt("id");
int parentId = action.getParameterInt("parentId");
int type = action.getParameterInt("type");		// 是否已经加为新闻
ForumContentBean con = ForumCacheUtil.getForumContent(id);
if(con == null) {
	response.sendRedirect("index.jsp");
	return;
}
boolean admin2=ForbidUtil.isForbid("newsa2", loginUser.getId());
List typeList = SqlUtil.getObjectsList("select b.id,b.name from forum_news a,forum_news_type b where a.id="+id + " and a.type=b.id",2);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="添加新闻">
<p align="left">
<%if(type==0){%>
<%=StringUtil.toWml(con.getTitle())%><br/><%=con.getCreateDatetime()%><br/>
本条新闻已属于<%for(int i=0;i<typeList.size();i++){Object[] objs = (Object[])typeList.get(i);%>《<a href="/news/list.jsp?id=<%=objs[0]%>"><%=objs[1]%></a>》<%if(admin2){%><a href="/news/dNews.jsp?id=<%=id%>&amp;tid=<%=objs[0]%>">删</a><%}%><%}%><br/>————————<br/><%
//else 
{

if(parentId==0){
%>新闻类型:<select name="type">
<%
List list = action.getNewsTypeList();
for(int i = 0;i < list.size();i++){
NewsTypeBean bean = (NewsTypeBean)list.get(i);
if (bean.getType()==0){
%><option  value="<%=bean.getId()%>"><%=bean.getName()%></option>
<%}
}%>
</select><br/>
<anchor>确认添加新闻
<go href="addNews.jsp?id=<%=con.getId()%>" method="post">
    <postfield name="type" value="$type"/>
</go></anchor><br/><%
}

if(parentId==0){
%>八卦类型:<select name="type2">
<%
List list = action.getNewsTypeList();
for(int i = 0;i < list.size();i++){
NewsTypeBean bean = (NewsTypeBean)list.get(i);
if (bean.getType() ==1){
%><option  value="<%=bean.getId()%>"><%=bean.getName()%></option>
<%}
}%>
</select><br/>
<anchor>确认添加八卦
<go href="addNews.jsp?id=<%=con.getId()%>" method="post">
    <postfield name="type" value="$type2"/>
</go></anchor><br/><%
}

{
if(admin2){
%>专题类型:<select name="type3">
<%
List list = action.getNewsTypeList(parentId);
for(int i = 0;i < list.size();i++){
NewsTypeBean bean = (NewsTypeBean)list.get(i);
if (bean.getType() ==2){
%><option  value="<%=bean.getId()%>"><%=bean.getName()%></option>
<%
	if(bean.getChild()!=0){
		%><option value="<%=bean.getId()%>" onpick="<%=response.encodeURL("addNews.jsp?id="+id+"&amp;parentId="+bean.getId())%>">+<%=bean.getName()%></option><%
	}
}
}%>
</select><br/>
<anchor>确认添加专题
<go href="addNews.jsp?id=<%=con.getId()%>" method="post">
    <postfield name="type" value="$type3"/>
</go></anchor><br/><%
}
}

}
}else{
action.addNews(con, type);
%>
新闻添加成功!<br/>
<%}%>
<a href="viewContent.jsp?contentId=<%=id%>">返回帖子</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>