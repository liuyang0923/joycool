<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.chat.JCRoomContentCountBean"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.util.SqlUtil" %><%@ page import="java.util.HashMap" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%@ page import="java.util.Iterator" %><%!
static byte[] forumLock= new byte[0];
public HashMap listToMap(List list){
HashMap hm = null;
if(list==null){
	hm = new HashMap();
	return hm;
}
String key = "specialForumIndex";
hm = (HashMap) OsCacheUtil.get(key,
		OsCacheUtil.FORUM_CACHE_GROUP,
				OsCacheUtil.FORUM_CACHE_FLUSH_PERIOD);
if (hm == null) {
	synchronized (forumLock) {
		hm = (HashMap) OsCacheUtil.get(key,
				OsCacheUtil.FORUM_CACHE_GROUP,
				OsCacheUtil.FORUM_CACHE_FLUSH_PERIOD);
		if (hm == null) {
			hm = new HashMap();
			Iterator it = list.iterator();
			while(it.hasNext()){
				ForumBean forum = (ForumBean)it.next();
				if(forum!=null){
				hm.put(new Integer(forum.getId()),forum);
				}
			}
			OsCacheUtil.put(key, hm,
					OsCacheUtil.FORUM_CACHE_GROUP);
		}
	}
}
return hm;
}
%><%response.setHeader("Cache-Control", "no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser!=null&&loginUser.getUserSetting()!=null&&loginUser.getUserSetting().isFlag(9)){
	request.getRequestDispatcher("indexX.jsp").forward(request, response);;
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left"><%=BaseAction.getTop(request, response)%><%--=loginUser.showImg("../img/jcforum/jcforum.gif")--%>
<%if (request.getParameter("city") == null) {%>
<a href="top10.jsp">今日十大热贴</a>|<a href="/news/index.jsp">新闻</a>|<a href="/news/index2.jsp">八卦</a><br/>
<a href="forum.jsp?forumId=1">特快</a>.<a href="forum.jsp?forumId=10">水</a>|
<a href="forum.jsp?forumId=4">灌水</a>|
<a href="forum.jsp?forumId=1548">围炉</a>|
<a href="forum.jsp?forumId=787">潮汕</a><br/>
<a href="forum.jsp?forumId=1450">客家</a>|
<a href="forum.jsp?forumId=1802">新人</a>|
<a href="forum.jsp?forumId=1985">交易</a>|
<a href="forum.jsp?forumId=2878">桃源</a><br/>
<a href="forum.jsp?forumId=3506">手机</a>|
<a href="forum.jsp?forumId=626">杂谈</a>|
<a href="forum.jsp?forumId=3532">帮会</a>|
<a href="forum.jsp?forumId=788">游戏</a><br/>
<% ForumBean forum = null;
List vec = ForumCacheUtil.getForumListCache();
HashMap map = listToMap(vec);%>
===情感文学===<br/>
<%forum =(ForumBean)map.get(new Integer(5702));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(5703));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
<%forum =(ForumBean)map.get(new Integer(3));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(951));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
<%--<a href="/home/newDiaryTop.jsp?">网友日记</a>|<%forum =(ForumBean)map.get(new Integer(6));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}--%>
<%forum =(ForumBean)map.get(new Integer(906));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(895));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%><%forum =(ForumBean)map.get(new Integer(1512));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(1548));if(forum!=null){%><a 
href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
===杂谈灌水===<br/>
<%forum =(ForumBean)map.get(new Integer(10437));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(10436));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
<%forum =(ForumBean)map.get(new Integer(10515));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>">福体双彩</a>|<%}%><%forum =(ForumBean)map.get(new Integer(1092));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
<%forum =(ForumBean)map.get(new Integer(671));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(2));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
<%forum =(ForumBean)map.get(new Integer(1182));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(5));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>">祝福许愿</a><br/><%}%>
<%forum =(ForumBean)map.get(new Integer(1114));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><a href="forum.jsp?forumId=1268">体坛风云</a><!-- |<a href="forum.jsp?forumId=2313">奥运</a> --><br/>
<%forum =(ForumBean)map.get(new Integer(1180));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(1510));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
<%forum =(ForumBean)map.get(new Integer(1113));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(1181));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
<%--<%forum =(ForumBean)map.get(new Integer(1048));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%> --%>
===游戏贸易===<br/>
<a href="forum.jsp?forumId=3533">谈股论金</a>|<%forum =(ForumBean)map.get(new Integer(710));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>">自由贸易</a><br/><%}%>
<%forum =(ForumBean)map.get(new Integer(741));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>">幸运彩坛</a>|<%}%><%forum =(ForumBean)map.get(new Integer(1985));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
===各地风情===<br/>
<%forum =(ForumBean)map.get(new Integer(787));
if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(1267));
if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%><%forum =(ForumBean)map.get(new Integer(1450));
if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(1481));
if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%><%forum =(ForumBean)map.get(new Integer(1692));
if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a>|<%}%><%forum =(ForumBean)map.get(new Integer(1819));
if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%><%forum =(ForumBean)map.get(new Integer(1959));
if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><br/><%}%>
<a href="index.jsp?city=1">+帮会论坛+</a><br/>
===版务管理===<br/>
<%forum =(ForumBean)map.get(new Integer(8));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><%}%>|<a href="forum.jsp?forumId=4687">投诉站</a>|<%forum =(ForumBean)map.get(new Integer(789));if(forum!=null){%><a href="forum.jsp?forumId=<%=forum.getId()%>"><%=forum.getTitle()%></a><%}%><br/>

<%--<%IChatService chatService = ServiceFactory.createChatService();
JCRoomContentCountBean contentCountBean = chatService.getJCRoomContentCount(" room_id="+Constants.TIETU_TOTAL_COUNT_ID);
IForumMessageService forumService = ServiceFactory.createForumMessageService();
int totalTietuCount1 =  forumService.getNewForumMessageCount("forum_id=14");
int totalTietuCount = contentCountBean.getCount();%>
<%=j + 1%>.<a href="/tietu/forumIndex.jsp?id=14">美图秀场</a>(<%=totalTietuCount%>贴<%=totalTietuCount1%>新贴)<br/>极品美图，尽在此处！<br/>--%>
<%}else{
List tongList = TongCacheUtil.getTongListById("develop");
//ForumCacheUtil.getTongForumListCache();
int NUMBER_PER_PAGE=8;
int totalCount = tongList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "index.jsp?city=1";
		int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List tongList1 = tongList.subList(startIndex, endIndex);
    // Vector vec = ForumCacheUtil.getTongForumListCache();
			if (tongList1 != null) {
			Integer id=null;
				for (int i = 0; i < tongList1.size(); i++) {
				id=(Integer)tongList1.get(i);
				if(id!=null){
					ForumBean forum = ForumCacheUtil.getForumCacheBean(id.intValue());
					if (forum != null) {%>
<%=i + 1%>.<a href="forum.jsp?forumId=<%=forum.getId()%>"><%=StringUtil.toWml(forum.getTitle())%></a>(<%=forum.getTotalCount()%>贴<%=forum.getTodayCount()%>新贴)<br/>
<%--<%=StringUtil.toWml(forum.getDescription())%><br/>--%>
<%                   }
                 }
               }
           }
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%}	%>
<a href="index.jsp">返回乐酷论坛</a><br/>	
     <%  }%>
<a href="forumRank.jsp">等级说明</a>|<a href="forumRule.jsp">论坛须知</a>|<a href="/user/forumSet.jsp">个人设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p> 
</card>
</wml>
