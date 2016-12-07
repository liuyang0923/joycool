<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.chat.JCRoomContentCountBean"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.util.SqlUtil" %><%@ page import="java.util.HashMap" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%@ page import="java.util.Iterator" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction,net.joycool.wap.bean.jcforum.ForumRcmdBean,net.joycool.wap.bean.jcforum.ForumRcmdBean2,net.joycool.wap.service.factory.ServiceFactory,net.joycool.wap.bean.jcforum.ForumContentBean"%><%!
%><%response.setHeader("Cache-Control", "no-cache");
ForumAction action = new ForumAction(request);
UserBean loginUser = action.getLoginUser();
ForumBean forumBean = null;
ForumRcmdBean rcmdBean = null;
ForumRcmdBean2 rcmdBean2 = null;
ForumContentBean contentBean = null;
List list1 = ForumAction.getRcmdContent(5);
List list2 = ForumAction.getRcmdForum(3);
List tmpList = null;
int tmpId = 0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷论坛">
<p align="left"><%=BaseAction.getTop(request, response)%>
<img src="img/logo2.gif" alt="o" /><br/>
<a href="index.jsp">论坛快捷通道</a>|<a href="userTopic.jsp?u=<%=loginUser!=null?loginUser.getId():0%>">我的帖子</a><br/>
==☆乐酷精帖☆==<br/>
<%if (list1 != null && list1.size() > 0){
	for (int i = 0 ; i < list1.size() ; i++){
		rcmdBean = (ForumRcmdBean)list1.get(i);
		if (rcmdBean != null){
			contentBean = ForumCacheUtil.getForumContent(rcmdBean.getContentId());
			if (contentBean != null){
				%><a href="/jcforum/viewContent.jsp?contentId=<%=contentBean.getId()%>"><%=StringUtil.toWml(contentBean.getTitle())%></a>(<%=contentBean.getCount()%>)<br/><%
			}
		}
	}
}%>
==☆论坛推荐☆==<br/>
<%if (list2 != null && list2.size() > 0){
	for (int i = 0 ; i < list2.size() ; i++){
		if (list2.get(i) != null){
			rcmdBean2 = (ForumRcmdBean2)list2.get(i);
			if (rcmdBean2 != null){
				forumBean = ForumCacheUtil.getForumCache(rcmdBean2.getForumId());
				if (forumBean != null){
					%><%=i+1%>.<a href="/jcforum/forum.jsp?forumId=<%=forumBean.getId()%>"><%=StringUtil.toWml(forumBean.getTitle())%></a>-<%=rcmdBean2.getContentWml()%><br/><%
					tmpList = action.getPrime(forumBean.getId(),3);	
					if (tmpList != null && tmpList.size() > 0){
						for (int j = 0 ; j < tmpList.size() ; j++){
							tmpId = ((Integer)tmpList.get(j)).intValue();
							contentBean = ForumCacheUtil.getForumContent(tmpId);
							if (contentBean != null){
								%><a href="/jcforum/viewContent.jsp?contentId=<%=contentBean.getId()%>"><%=StringUtil.toWml(contentBean.getTitle())%></a>(<%=contentBean.getCount()%>)<br/><%
							}
						}
					}
				}
			}
		}
	}
}%>
==☆坛子导航区☆==<br/>
+主流版块<br/>
<a href="forum.jsp?forumId=1">特快</a>.<a href="forum.jsp?forumId=10">水</a>|<a href="forum.jsp?forumId=4">灌水</a>|<a href="/news/index.jsp">新闻</a>|<a href="forum.jsp?forumId=1548">围炉</a><br/>
<a href="forum.jsp?forumId=787">潮汕</a>|<a href="forum.jsp?forumId=1450">客家</a>|<a href="forum.jsp?forumId=1802">新人</a>|<a href="forum.jsp?forumId=1985">交易</a>|<a href="forum.jsp?forumId=2878">桃源</a><br/>
<a href="forum.jsp?forumId=3506">手机</a>|<a href="forum.jsp?forumId=626">杂谈</a>|<a href="forum.jsp?forumId=3532">帮会</a>|<a href="forum.jsp?forumId=788">游戏</a><br/>
+情感文学<br/>
<a href="forum.jsp?forumId=5702">求职</a>|<a href="forum.jsp?forumId=5703">鹊桥</a>|<a href="forum.jsp?forumId=3">情感</a>|<a href="forum.jsp?forumId=951">灵异</a><br/>
<a href="forum.jsp?forumId=906">书虫</a>|<a href="forum.jsp?forumId=895">文墨</a>|<a href="forum.jsp?forumId=1512">情缘</a>|<a href="forum.jsp?forumId=1548">围炉</a><br/>
+娱乐杂谈<br/>
<a href="forum.jsp?forumId=671">贴图</a>|<a href="forum.jsp?forumId=1180">娱乐</a>|<a href="forum.jsp?forumId=1510">时尚</a>|<a href="forum.jsp?forumId=1181">星座</a>|<a href="forum.jsp?forumId=1113">美食</a><br/>
<a href="forum.jsp?forumId=5">祝福</a>|<a href="forum.jsp?forumId=2">幽默</a>|<a href="forum.jsp?forumId=1114">军人</a>|<a href="forum.jsp?forumId=1092">校园</a>|<a href="forum.jsp?forumId=1182">打工</a><br/>
+游戏贸易<br/>
<a href="forum.jsp?forumId=3533">股市</a>|<a href="forum.jsp?forumId=710">贸易</a>|<a href="forum.jsp?forumId=10515">彩坛</a>|<a href="forum.jsp?forumId=788">游戏</a><br/>
+各地风情<br/>
<a href="forum.jsp?forumId=1267">巴蜀</a>|<a href="forum.jsp?forumId=1481">闽南</a>|<a href="forum.jsp?forumId=1692">江苏</a>|<a href="forum.jsp?forumId=1819">东北</a>|<a href="forum.jsp?forumId=1959">广东</a><br/>
==版务管理==<br/>
<a href="forum.jsp?forumId=4687">投诉</a>|<a href="forum.jsp?forumId=8">建议</a>|<a href="forum.jsp?forumId=789">版务</a>|<a href="/jcforum/forumRule.jsp">须知</a>|<a href="/user/forumSet.jsp">设置</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p> 
</card>
</wml>
