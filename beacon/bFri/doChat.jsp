<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.bean.chat.*,net.joycool.wap.action.user.*"%><%
	response.setHeader("Cache-Control","no-cache");
	int toUserId = StringUtil.toId(request.getParameter("uid"));
	
	if(toUserId == 0){
		response.sendRedirect("/beacon/bFri/myInfo.jsp");
	}
	
	String content = StringUtil.removeCtrlAsc(StringUtil.trim(request.getParameter("content")));
	// 输入项目检查
	if (content == null) {
		request.setAttribute("result", "failure");
		request.setAttribute("tip", "请填写内容。");
		return;
	}
	content = content.trim();
	if (content.length() == 0 || content.length() > 100) {
		request.setAttribute("result", "failure");
		request.setAttribute("tip", "每条信息不能超过100字。至少一个字");
		return;
	}
	UserBean toUser = UserInfoUtil.getUser(toUserId);
	UserBean fromUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
	
	JCRoomContentBean jcRoomContent = new JCRoomContentBean();
	jcRoomContent.setContent(content);
	jcRoomContent.setFromId(fromUser.getId());
	jcRoomContent.setFromNickName(fromUser.getNickName());
	jcRoomContent.setToId(toUser.getId());
	jcRoomContent.setToNickName(toUser.getNickName());
	jcRoomContent.setIsPrivate(1);
	jcRoomContent.setAttach("");
	jcRoomContent.setRoomId(0);
	jcRoomContent.setSecRoomId(0);
	jcRoomContent.setMark(0);
	IChatService chatService = ServiceFactory.createChatService();
	chatService.addContent(jcRoomContent);
	// macq_2007-5-22_添加最近联系人名单_start
	RoomCacheUtil.addLinkManList(fromUser.getId(), toUser.getId());
	// macq_2007-5-22_添加最近联系人名单_end
	// 加入消息系统
	NoticeBean notice = new NoticeBean();
	notice.setTitle(NoticeUtil.getChatNoticeTitle(fromUser.getNickName(), content));
	notice.setType(NoticeBean.GENERAL_NOTICE);
	notice.setUserId(toUser.getId());
	notice.setHideUrl("/chat/hall.jsp");
	notice.setLink("/chat/pmSpace.jsp?toUserId=" + fromUser.getId()+ "&amp;roomId=" + 0);
	// macq_2007-5-16_增加聊天消息类型_start
	notice.setMark(NoticeBean.CHAT);
	// macq_2007-5-16_增加聊天消息类型_end
	NoticeUtil.addNotice(notice);
	// mcq_1_增加用户经验值 时间:2006-6-11
	// 增加用户经验值
	RankAction.addPoint(fromUser, Constants.RANK_GENERAL);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="和主人对话" ontimer="<%=response.encodeURL("myInfo.jsp")%>"><timer value="30"/>
<p><br/>发送成功！<br/>
3秒后自动返回<br/><a href="<%=("myInfo.jsp")%>">返回朋友买卖首页</a>
<br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>
