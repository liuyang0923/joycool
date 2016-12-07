<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.SqlUtil"%><%@ page import="java.text.SimpleDateFormat"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.action.LinkAction" %><%!
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.M.d");
	static int PAGE_ROW = 20;	// 一页多少行
%><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
UserBean loginUser = action.getLoginUser();
ForumUserBean forumUser = action.getForumUser();
int catId = action.getParameterInt("cat");
PrimeCatBean catBean = ForumAction.getForumService().getPrimeCat("id="+catId);
if(catBean==null){
	response.sendRedirect("index.jsp");
	return;
}
ForumBean forum = ForumCacheUtil.getForumCache(catBean.getForumId());

Object[] adminObj = (Object[])session.getAttribute("primeAdmin"+forum.getId());
HashSet adminSelect = null;
if(adminObj!=null) {	// 管理精华区
	adminSelect = (HashSet)adminObj[0];
	int act = action.getParameterInt("a");
	switch(act){
	case 1: {	// 选中目录
		int select = action.getParameterInt("s");
		adminSelect.add(new Integer(-select));
	} break;
	case 2: {	// 取消选中目录
		int select = action.getParameterInt("s");
		adminSelect.remove(new Integer(-select));
	} break;
	case 3: {	// 选中帖子
		int select = action.getParameterInt("s");
		adminSelect.add(new Integer(select));
	} break;
	case 4: {	// 取消选中帖子
		int select = action.getParameterInt("s");
		adminSelect.remove(new Integer(select));
	} break;
	case 5: {	// 取消所有选中
		adminSelect.clear();
	} break;
	case 6: {	// 移动所有选中
		ForumAction.moveSelect(new ArrayList(adminSelect), catBean, forum);
		adminSelect.clear();
	} break;
	case 7: {	// 删除目录
		// 目录下没有其他目录和内容才允许删除
		if(catBean.getParentId()!=0 && ( !SqlUtil.exist("select id from jc_forum_prime where cat_id="+catId+" limit 1",2)
			&&	!SqlUtil.exist("select id from jc_forum_prime_cat where parent_id="+catId+" limit 1",2))) {
			SqlUtil.executeUpdate("delete from jc_forum_prime_cat where id="+catId,2);
			SqlUtil.executeUpdate("update jc_forum_prime_cat set cat_count=cat_count-1 where id=" + catBean.getParentId(),2);
			response.sendRedirect("prime.jsp?cat="+catBean.getParentId());
			return;
		}
	} break;
	case 8: {	// 修改目录名称
		String name=action.getParameterNoEnter("name");
		if(name!=null){
			catBean.setName(name);
			SqlUtil.executeUpdate("update jc_forum_prime_cat set name='"+StringUtil.toSql(name)+"' where id="+catId,2);
			response.sendRedirect("prime.jsp?cat="+catBean.getParentId());
			return;
		}
	} break;
	case 10: {	// 结束管理
		session.removeAttribute("primeAdmin"+forum.getId());
		adminSelect=null;
	} break;
	}
	if(act>0){
		response.sendRedirect("prime.jsp?cat="+catId);
		return;
	}
}
PagingBean paging = new PagingBean(action,catBean.getCatCount()+catBean.getThreadCount(),PAGE_ROW,"p");
int start = paging.getStartIndex();
List primeCatList;
if(start<=catBean.getCatCount()){
	primeCatList = ForumAction.getForumService().getPrimeCatList("parent_id="+catId+" order by id limit "+start+","+PAGE_ROW);
}else{
	primeCatList=new ArrayList(0);
}
List contentList;
int catMore = catBean.getCatCount()-start;// 本页一共输出多少目录
if(catMore<PAGE_ROW){	// 不到20条（可能是负数），剩下的输出帖子
	int limit = PAGE_ROW;
	int queryStart;
	if(catMore>0){
		queryStart=0;
		limit-=catMore;
	}else{
		queryStart=-catMore;
	}
	contentList = SqlUtil.getIntList("select id from jc_forum_prime where cat_id="+catId+" order by id limit "+queryStart+","+limit,2);
}else{
	contentList=new ArrayList(0);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="forum.jsp?forumId=<%=forum.getId()%>"><%=StringUtil.toWml(forum.getTitle())%></a>&gt;<%if(catBean.getParentId()==0){%>分类精华<%}else{%><a href="prime.jsp?cat=<%=forum.getPrimeCat()%>">分类精华</a>&gt;<%=StringUtil.toWml(catBean.getName())%><%}%><br/>
<%if(adminSelect!=null&&adminSelect.size()>0){%>
操作选中的内容:<a href="prime.jsp?cat=<%=catBean.getId()%>&amp;a=5">清空选择</a>|<a href="prime.jsp?cat=<%=catBean.getId()%>&amp;a=6">移动到本目录</a><br/>
<%}%>
<%
int sum=start;
for(int i = 0;i<primeCatList.size();i++){
PrimeCatBean bean = (PrimeCatBean)primeCatList.get(i);
%><%if(adminSelect!=null){if(!adminSelect.contains(new Integer(-bean.getId()))){%><a href="prime.jsp?cat=<%=catBean.getId()%>&amp;a=1&amp;s=<%=bean.getId()%>">选</a><%}else{%><a href="prime.jsp?cat=<%=catBean.getId()%>&amp;a=2&amp;s=<%=bean.getId()%>">消</a><%}}%>
<%=++sum%>.+<a href="prime.jsp?cat=<%=bean.getId()%>"><%=StringUtil.toWml(bean.getName())%></a>(<%=bean.getCatCount()%>+<%=bean.getThreadCount()%>)<%if(adminSelect!=null){%>-<a href="primec.jsp?cat=<%=bean.getId()%>">修改</a><%}%><br/><%
}%>

<%				
for (int i = 0; i < contentList.size(); i++) {
	int content = ((Integer) contentList.get(i)).intValue();
	ForumContentBean cons = ForumCacheUtil.getForumContent(content);
	if (cons != null) {
		%><%if(adminSelect!=null){if(!adminSelect.contains(new Integer(cons.getId()))){%><a href="prime.jsp?cat=<%=catBean.getId()%>&amp;a=3&amp;s=<%=cons.getId()%>">选</a><%}else{%><a href="prime.jsp?cat=<%=catBean.getId()%>&amp;a=4&amp;s=<%=cons.getId()%>">消</a><%}}%>
<%=++sum%>.<a href="primev.jsp?id=<%=content%>"><%=StringUtil.toWml(StringUtil.limitString(cons.getTitle(),30))%></a>(<%=sdf.format(cons.getCreateTime())%>)<br/><%
	}else{
		%>(<%=content%>)<br/><%
	}
}
%>
<%=paging.shuzifenye("prime.jsp?cat="+catId,true,"|",response)%><br/>
<%if(catBean.getParentId()!=0){%><a href="prime.jsp?cat=<%=catBean.getParentId()%>">返回上一级</a><br/><%}%>
<%if(adminSelect!=null){%><a href="primeAdd.jsp?cat=<%=catBean.getId()%>">创建分类</a><br/><a href="primel.jsp?forumId=<%=forum.getId()%>">查看未分类精华区</a><br/>
<a href="prime.jsp?a=10&amp;cat=<%=catId%>">结束管理分类精华</a><br/><%}else{ 

if(loginUser != null && forum.getUserIdSet().contains(new Integer(loginUser.getId()))){%>
<a href="primeAdmin.jsp?forumId=<%=forum.getId()%>">管理分类精华区</a><br/>
<%}}%>
<a href="forum.jsp?forumId=<%=forum.getId()%>">返回论坛</a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>