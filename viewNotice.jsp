<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.NoticeAction"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.HashMap"%><%@ page import="java.net.URLEncoder"%><%
response.setHeader("Cache-Control","no-cache");

NoticeAction.viewNotice(request);

// zhangyi add noticeBack control 2006/06/20 start
String backTo = (String) request.getAttribute("backTo");
//liuyi 2007-01-19 通知系统bug start
if(backTo!=null){
	int endIndex = backTo.indexOf("backTo");
	if(endIndex!=-1){
		backTo = backTo.substring(0, endIndex);
	}
}
//liuyi 2007-01-19 通知系统bug end
HashMap noticeBackLinkMap = new HashMap();
noticeBackLinkMap.put("backTo",backTo);
noticeBackLinkMap.put("displayTimes",new Integer("0"));
session.setAttribute("noticeBackLinkMap",noticeBackLinkMap);
// zhangyi add noticeBack control 2006/06/20 end

NoticeBean notice = (NoticeBean) request.getAttribute("notice");
if(notice == null){
	//response.sendRedirect(( "/"));
	BaseAction.sendRedirect(null, response);
	return;
}
if(notice.getLink() != null && !notice.getLink().equals("")){
	String link = notice.getLink();
	//liuyi 2006-10-30 结义消息处理 start
	if(notice.getType()==NoticeBean.KEEP_NOT_READ_NOTICE){
		if(link!=null){
			if(link.indexOf("?") == -1){
				link += "?noticeId=" + notice.getId();
			} else {
				link += "&noticeId=" + notice.getId();
			}
		}
	}
	//liuyi 2006-10-30 结义消息处理 end
	if(backTo!=null &&link.indexOf("backTo") == -1){
		if(link.indexOf("?") == -1){
			link += "?backTo=" + URLEncoder.encode(backTo);
		} else {
			link += "&backTo=" + URLEncoder.encode(backTo);
		}
	}
	response.sendRedirect((link));
	return;
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=StringUtil.toWml(notice.getTitle())%>">
<p align="left">
<%=StringUtil.toWml(notice.getContent())%><br/>
<br/>
<%=BaseAction.getTop(request, response)%>
<%
if(backTo == null){
%>
<anchor><prev/>返回上一页</anchor><br/>
<%
} else {
%>
<%
// add by zhangyi
session.setAttribute("backTo",backTo);
}
%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>