<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page errorPage=""%><%@ page import="java.util.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.chat.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.friendadver.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
        response.setHeader("Cache-Control","no-cache");
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 start
String reURL=request.getRequestURL().toString();
String queryStr=request.getQueryString();
session.setAttribute("loginReturnURL",queryStr==null?reURL:reURL+"?"+queryStr);
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 end
        UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
		String backTo=request.getParameter("backTo");
		if(backTo==null||backTo.equals("")) backTo="/lswjs/index.jsp";
        //求发布量
        IChatService chatService = ServiceFactory.createChatService();
        JCRoomContentCountBean rcc=chatService.getJCRoomContentCount("room_id=10000000");		
        
        int FRIEND_ADVER_PER_PAGE = 10;
        IFriendAdverService service = ServiceFactory
                .createFriendAdverService();
        IFriendAdverMessageService fmService = ServiceFactory.createFriendAdverMessageService();

        String condition = null;
        //页码
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        if (pageIndex == -1) {
            pageIndex = 0;
        }
        //order by
        String ob = request.getParameter("ob");
//        if (ob == null || ob.equals("")) {
            ob = "id";
//        }
		//取得性别参数和地区参数
		String sex=request.getParameter("sex");
		if(sex==null||sex.equals(""))
		{
			if(loginUser!=null&&loginUser.getGender()==0)
			{
				sex="1";
			}else
			{
				sex="0";
			}
		}
		condition=" gender="+Integer.parseInt(sex);
//		condition="sex="+sex;
		String area=request.getParameter("area");
		if(area!=null && area.equals("1"))
		{
			if(loginUser!=null && loginUser.getCityno()!=0) condition+=" and cityno="+loginUser.getCityno();
			//if(loginUser.getCityno()!=0) condition+=" and area="+loginUser.getCityno();
		}
        /**
         * 分页相关
         */
 
        int totalCount = 0;
        Integer listCount = (Integer)OsCacheUtil.get(condition, "friendAdvListCount", 30*60);
        if(listCount==null){
        	listCount = new Integer(service.getFriendAdverCount(condition));
        	OsCacheUtil.put(condition, listCount, "friendAdvListCount");
        }
        totalCount = listCount.intValue();

        int totalPageCount = totalCount / FRIEND_ADVER_PER_PAGE;
        if (totalCount % FRIEND_ADVER_PER_PAGE != 0) {
            totalPageCount++;
        }
        if (totalPageCount == 0) {
            pageIndex = 0;
        } else if (totalPageCount != 0 && pageIndex >= totalPageCount) {
            pageIndex = totalPageCount - 1;
        }
        String prefixUrl = "friendAdverIndex.jsp?sex=" + sex + "&area=" + area;

        /**
         * 取得主题列表
         */
        condition += " ORDER BY jc_friend_adver." + ob + " DESC LIMIT " + pageIndex
                * FRIEND_ADVER_PER_PAGE + ", " + FRIEND_ADVER_PER_PAGE;
        //liuyi 2006-11-26 服务器变慢原因修改 start
        Vector friendAdverList = service.getFriendAdverList(condition);      	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="交友广告">
<p align="left"><%=BaseAction.getTop(request, response)%>
<img src="img/friend.gif" alt="交友首页"/><br/>
交友广告(<%=rcc!=null?rcc.getCount()+"":"0"%>贴)<br/>
---------<br/>
我要找 <%if(sex.equals("0")){%>女友<%}else{%><a href="friendAdverIndex.jsp?sex=0">女友</a><%}%>.<%if(sex.equals("1")){%>男友<%}else{%><a href="friendAdverIndex.jsp?sex=1">男友</a><%}%>.<%if("1".equals(area)){%>本地区<%}else{%><a href="friendAdverIndex.jsp?area=1&amp;sex=<%=sex%>&amp;ob=<%=ob%>">本地区</a><%}%><br/>
<%--if(ob.equals("hits")){%>按人气<%}else{%><a href="friendAdverIndex.jsp?ob=hits&amp;sex=<%=sex%>">按人气</a><%}%> <%if(ob.equals("id")){%>按时间<%}else{%><a href="friendAdverIndex.jsp?ob=id&amp;sex=<%=sex%>">按时间</a><%}%><br/--%>
<%
int count = friendAdverList.size();
boolean hasAttach;
for(int i = 0; i < count; i ++){
	FriendAdverBean fa = (FriendAdverBean) friendAdverList.get(i);
	hasAttach = false;
	if(fa.getAttachment() != null && !fa.getAttachment().equals("")){
		hasAttach = true;
	}
	int famCount=0;
	String key = "friend_adver_id="+fa.getId();
	Integer replyCount = (Integer)OsCacheUtil.get(key, "friendAdverReplyCount", 15*60);
	if(replyCount==null){
		replyCount = new Integer(fmService.getFriendAdverMessageCount("friend_adver_id="+fa.getId()));
		OsCacheUtil.put(key, replyCount, "friendAdverReplyCount");
	}
	famCount = replyCount.intValue();
	//liuyi 2006-11-26 服务器变慢原因修改 end
%>
<a href="friendAdverMessage.jsp?id=<%=fa.getId()%>"><%=(pageIndex * FRIEND_ADVER_PER_PAGE + i + 1)%>.<%if(hasAttach){%>[图]<%}%><%=StringUtil.toWml(fa.getTitle())%>(点击<%=fa.getHits()%>|回复<%=famCount%>)</a><br/>
<%
}
%>
<%=PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "|", response)%><br/>
<%if(totalPageCount>1){%>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>
<%}%>
我要<a href="postAdver.jsp?backTo=<%=StringUtil.getBackTo(request)%>">发文征友</a><%--|<a href="postAdverAttach.jsp">贴图</a>--%><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>