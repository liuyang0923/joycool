<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="jc.imglib.*,net.joycool.wap.framework.BaseAction"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.action.user.UserBagAction"%><%!
static IUserService userService = ServiceFactory.createUserService();
static int COUNT_PRE_PAGE = 10;%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
UserBean loginUser = action.getLoginUser();
int toUserId=action.getParameterInt("tuid");
String tip = null;
UserSettingBean set = loginUser.getUserSetting();
String un = String.valueOf(loginUser.getId());
int roomId=0;
//禁言
ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("chat",loginUser.getId());
if (forbid != null){
	tip = "您已被禁止发言.";
}
int send = action.getParameterInt("s");
int imgId = action.getParameterInt("iid");
Lib lib = null;
List imgList = null;
PagingBean paging = null;
UserBean toUser = UserInfoUtil.getUser(toUserId);
LibUser libUser = null;
if(tip==null){
	libUser = ImgLibAction.service.getLibUser(" user_id=" + loginUser.getId());
	if (toUser == null){
		tip = "对方用户不存在.";
	} else {
		if (send == 1 && toUserId > 0 && imgId > 0){
			// 发图
			lib = ImgLibAction.service.getLib(" id=" + imgId);
			if (lib == null || lib.getUserId() != loginUser.getId()){
				tip = "要发送的图片不存在,或不是你的图片.";
			} else if (lib.getImg().length() < 6) {
				// o.gif,x.gif
				tip  = "图片正在审核中,或没有通过审核.";
			} else if(!action.isCooldown("chat", 5000)){
					tip="你的发言太快了！请先休息一会再继续。";
				}else{
				JCRoomContentBean roomContent = new JCRoomContentBean();
				roomContent.setContent("");
				roomContent.setFromId(loginUser.getId());
				if (loginUser.getNickName() == null
						|| loginUser.getNickName().equals("v")
						|| loginUser.getNickName().replace(" ", "").equals("")) {
					roomContent.setFromNickName("乐客" + loginUser.getId());
				} else {
					roomContent.setFromNickName(loginUser.getNickName());
				}
				roomContent.setToId(toUserId);
				if (toUser.getNickName() == null
						|| toUser.getNickName().equals("v")
						|| toUser.getNickName().replace(" ", "").equals("")) {
					roomContent.setToNickName("乐客" + toUser.getId());
				} else {
					roomContent.setToNickName(toUser.getNickName());
				}
				roomContent.setIsPrivate(1);
				// 上传文件
	//			String filePath = ImgLibAction.ATTACH_URL_ROOT;
				String fileURL = lib.getImg();
				roomContent.setAttach(fileURL);
				roomContent.setRoomId(roomId);
				action.addContent(roomContent);
				response.sendRedirect("hall.jsp");
				return;
			}
		}
		if (libUser == null){
			tip = "您还没有创建图库.";
		} else {
			paging = new PagingBean(action, libUser.getCount(), COUNT_PRE_PAGE, "p");
			imgList = ImgLibAction.service.getLibList(" user_id=" + loginUser.getId()+" limit "+paging.getStartIndex()+",10");
			
		}
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="贴图">
<p align="left">
<%=BaseAction.getTop(request,response)%>
<%if (tip==null){
%>请从图库中选择图片:<br/><%
if(libUser.getCount() > 0){
	int startIndex = paging.getStartIndex();
	for (int i = 0 ; i < imgList.size() ; i++){
		lib = (Lib)imgList.get(i);
		if (lib != null){
			%><%=i+startIndex+1%>.<a href="postPic.jsp?pic=<%=lib.getId()%>&amp;tuid=<%=toUserId%>&amp;s=1&amp;iid=<%=lib.getId()%>"><%=StringUtil.toWml(lib.getTitle()) %></a><br/><%
		}
	}%><%=paging.shuzifenye("postPic.jsp?rid=" + roomId + "&amp;tuid=" + toUserId, true, "|", response)%><%
}else{%>(暂无)<br/><%}
} else {
%><%=tip%><br/><%	
}%>
<a href="post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=toUserId%>">返回发言</a><br/>
<a href="/image/lib/lib.jsp">&gt;&gt;去整理我的图库</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p>
</card>
</wml>