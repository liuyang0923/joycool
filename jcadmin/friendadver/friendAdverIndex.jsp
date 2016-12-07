<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.forum.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.friendadver.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
		//登录限制
		if(session.getAttribute("adminLogin") == null){
			//response.sendRedirect("../login.jsp");
			BaseAction.sendRedirect("/jcadmin/login.jsp", response);
			return;
		}

        response.setHeader("Cache-Control","no-cache");
		String backTo=request.getParameter("backTo");
		if(backTo==null||backTo.equals("")) backTo="/lswjs/index.jsp";
        
        int FRIEND_ADVER_PER_PAGE = 10;
        IFriendAdverService service = ServiceFactory
                .createFriendAdverService();
        String condition = null;
        //页码
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        if (pageIndex == -1) {
            pageIndex = 0;
        }
        //order by
        String ob = request.getParameter("ob");
        if (ob == null || ob.equals("")) {
            ob = "id";
        }
		//取得性别参数和地区参数
		String sex=request.getParameter("sex");
		if(sex==null||sex.equals(""))
		{
			sex="0";
		}
		condition="sex="+Integer.parseInt(sex);
		String area=request.getParameter("area");
	
        /**
         * 分页相关
         */
 
        int totalCount = service.getFriendAdverCount(condition);

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
        condition += " ORDER BY " + ob + " DESC LIMIT " + pageIndex
                * FRIEND_ADVER_PER_PAGE + ", " + FRIEND_ADVER_PER_PAGE;
        Vector friendAdverList = service.getFriendAdverList(condition);
%>
<p align="left"><%=BaseAction.getTop(request, response)%>
交友中心<br/>
----------<br/>
我要找 <a href="friendAdverIndex.jsp?sex=0">女友</a>　<a href="friendAdverIndex.jsp?sex=1">男友</a>　<a href="friendAdverIndex.jsp?area=1">本地区</a><br/>
<%
int count = friendAdverList.size();
boolean hasAttach;
for(int i = 0; i < count; i ++){
	FriendAdverBean fa = (FriendAdverBean) friendAdverList.get(i);
	hasAttach = false;
	if(fa.getAttachment() != null && !fa.getAttachment().equals("")){
		hasAttach = true;
	}
%>
<a href="friendAdverMessage.jsp?id=<%=fa.getId()%>"><%=(pageIndex * FRIEND_ADVER_PER_PAGE + i + 1)%>.<%if(hasAttach){%>[图]<%}%><%=StringUtil.toWml(fa.getTitle())%>(点击<%=fa.getHits()%>)</a>|<a href="deleteAdver.jsp?id=<%=fa.getId()%>">删除记录</a>
<%if(hasAttach){ %>|<a href="<%=("deleteAdver.jsp?id=" + fa.getId())+"&amp;deleteImage=0"%>">删除图片</a><%}%><br/>
<%
}
%>
<%=PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "|", response)%><br/>
<%if(totalPageCount>1){%>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>
<%}%>
我也要发布<a href="postAdver.jsp?backTo=<%=StringUtil.getBackTo(request)%>">发文</a>|<a href="postAdverAttach.jsp">贴图</a><br/>
<a href="<%=(backTo.replace("&","&amp;"))%>">返回上一级</a><br/>
<a href="<%=("/chat/hall.jsp")%>">进入聊天大厅</a><br/>
<a href="http://wap.joycool.net/wapIndex.jsp">返回乐酷首页</a><br/>
</p>