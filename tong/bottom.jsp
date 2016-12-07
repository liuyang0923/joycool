<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><% ForumBean forum=ForumCacheUtil.getForumCacheBean(tong.getId());
if(forum!=null){%><a href="/jcforum/forum.jsp?forumId=<%=forum.getId()%>">帮会论坛</a><%}%><%if(us!=null && us.getTong()==tong.getId()){%>|<a href="/tong/tongChat.jsp?tongId=<%=tong.getId() %>">帮会聊天室</a><%}%><br/> 
<a href="/tong/shop.jsp?tongId=<%=tong.getId()%>">商店</a>|<a href="/tong/hockshop.jsp?tongId=<%=tong.getId()%>">当铺</a>|<a href="/tong/tongFund.jsp?tongId=<%=tong.getId()%>">基金</a>|仓库<br/>
<a href="/tong/tong.jsp?tongId=<%=tong.getId()%>">返回<%=StringUtil.toWml(tong.getTitle())%></a><br/>
<%=BaseAction.getBottom(request, response)%>
