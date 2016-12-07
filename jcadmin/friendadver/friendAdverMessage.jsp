<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.friendadver.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
		//登录限制
		if(session.getAttribute("adminLogin") == null){
			//response.sendRedirect("../login.jsp");
			BaseAction.sendRedirect("/jcadmin/login.jsp", response);
			return;
		}

        response.setHeader("Cache-Control","no-cache");       
        String condition = null;
		int FRIENDADVER_MESSAGE_PER_PAGE = 8;
        IFriendAdverService faService = ServiceFactory.createFriendAdverService();
		IUserService userService = ServiceFactory.createUserService();
        IFriendAdverMessageService famService = ServiceFactory.createFriendAdverMessageService();

        //取得参数
        //主题id
        int faId = StringUtil.toInt(request.getParameter("id"));
        //页码
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        if (pageIndex == -1) {
            pageIndex = 0;
        }

        /**
         * 取得主题
         */
        condition = "id = " + faId;
        FriendAdverBean friendAdver = faService.getFriendAdver(condition);
        if (friendAdver == null) {
            return;
        }

		UserBean user = UserInfoUtil.getUser( friendAdver.getUserId());

        /**
         * 分页相关
         */
        condition = "friend_adver_id = " + faId;
        int totalCount = famService.getFriendAdverMessageCount(condition);

        int totalPageCount = totalCount
                / FRIENDADVER_MESSAGE_PER_PAGE;
        if (totalCount %FRIENDADVER_MESSAGE_PER_PAGE != 0) {
            totalPageCount++;
        }
        if (totalPageCount == 0) {
            pageIndex = 0;
        } else if (totalPageCount != 0 && pageIndex >= totalPageCount) {
            pageIndex = totalPageCount - 1;
        }
        String prefixUrl = "friendAdverMessage.jsp?id=" + faId;

        /**
         * 取得主题列表
         */
        condition += " ORDER BY id DESC LIMIT " + pageIndex
                * FRIENDADVER_MESSAGE_PER_PAGE + ", "
                + FRIENDADVER_MESSAGE_PER_PAGE;
        Vector friendAdverMessageList = famService.getFriendAdverMessageList(condition);
                
        /**
         * 更新点击率
         */
        if (pageIndex == 0) {
            condition = "id = " + faId;
            String set = "hits = (hits + 1)";
            faService.updateFriendAdver(set, condition);
        } 
		
		int i, count;
		FriendAdverMessageBean fam;
		String age=(String)LoadResource.getAgeMap().get(new Integer(friendAdver.getAge()));
%>
<p align="left">
<%=StringUtil.toWml(friendAdver.getTitle())%><br/>
<%if(friendAdver.getAttachment()==null){%>
择友要求:<%=StringUtil.toWml((friendAdver.getArea()==0?"":user.getCityname()+",")+age+","+(friendAdver.getSex()==0?"女":"男"))%><br/>
<%}else if(friendAdver.getAttachment().equals("")){}else{%>
<a href="<%=friendAdver.getAttachmentURL()%>"><img src="<%=friendAdver.getAttachmentURL()%>" alt="loading..."/></a><br/>
<%}%>
备注:<%=StringUtil.toWml(friendAdver.getRemark())%><br/>
自我描述:<%=StringUtil.toWml("我是"+user.getNickName()+","+user.getAge()+(user.getCityname()==null?"":","+user.getCityname())+","+user.getSelfIntroduction())%><br/>
发表评论:<a href="postWord.jsp?id=<%=friendAdver.getId()%>&amp;backTo=<%=StringUtil.getBackTo(request)%>">发文</a>|<a href="postMap.jsp?id=<%=friendAdver.getId()%>&amp;backTo=<%=StringUtil.getBackTo(request)%>">贴图</a><br/>
网友评论:<br/>
<%
count = friendAdverMessageList.size();
for(i = 0; i < count; i ++){
	fam = (FriendAdverMessageBean) friendAdverMessageList.get(i);
%>
<%=(pageIndex * FRIENDADVER_MESSAGE_PER_PAGE + i + 1)%>.<a href="/user/ViewUserInfo.do?userId=<%=fam.getUserId() %>&amp;backTo=<%=PageUtil.getBackTo(request)%>"><%=StringUtil.toWml(fam.getUserNickname())%></a>:<%=StringUtil.toWml(fam.getContent())%>(<%=fam.getCreateDatetime()%>)|<a href="deleteAdverMessage.jsp?id=<%=fam.getId()%>&amp;faId=<%=faId%>">删除</a><br/>
<%
	if(fam.getAttachment() != null && !fam.getAttachment().equals("")){
%>
<a href="<%=fam.getAttachmentURL()%>"><img src="<%=fam.getAttachmentURL()%>" alt="loading..."/></a><br/>
<%
    }
}
%>
<%=PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "|", response)%><br/>
<%if(totalPageCount>1){%>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>
<%}%>
我也要发布<a href="postAdver.jsp?backTo=<%=StringUtil.getBackTo(request)%>">发文</a>|<a href="postAdverAttach.jsp">贴图</a><br/>
<a href="friendAdverIndex.jsp">返回交友中心</a>
</p>