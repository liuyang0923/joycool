<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.action.job.HappyCardAction"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@page import="net.joycool.wap.bean.job.HappyCardSendBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
int id = StringUtil.toInt(request.getParameter("id"));
//session.setAttribute("oxcx","true");//测试用
//lbj_判断是否应该取手机号，条件是：还没取过手机号 && 还没取到手机号（参数cp为空） && 系统设置要取手机号
if(SecurityUtil.oxcxMobile(request)){
	//lbj_这是取手机号之后返回的地址，也就是当前页地址，取手机号后会多加两个参数cp（手机号）和ua（手机型号）
	String url = "http://wap.joycool.net/chat/inviteDeal.jsp?id=" + id;	
	LogUtil.totalRedirect ++;
	//lbj_跳转到取手机号页面，该页面取到用户手机号后，会往url中加几个参数（手机号、手机型号等），跳回url
	SecurityUtil.redirectGetMobile(response, url);
} else {	
	String enterUrl = PageUtil.getCurrentPageURL(request);
	//lbj_用这个特殊的linkId来标明该手机号是用户被邀请上来时取到的
	int linkId = 999999;	
	String mobile = SecurityUtil.getPhone(request);
	//mobile="13580400925";//测试用
	if(mobile != null){
		LogUtil.totalBack ++;
	}	
	else{
		mobile = "fttodeath";
	}
	//lbj_取到手机号了
	//if(mobile != null && !mobile.equals(""))
	{
		if(mobile.startsWith("86")){
			mobile = mobile.substring(2);
		}
		session.setAttribute("userMobile", mobile);
		//lbj_将取到的手机号、型号等记录到LOG，好进行统计
		LogUtil.logMobile(mobile + ":" + SecurityUtil.getUA(request) + ":" + request.getRemoteAddr() + ":" + linkId);
		//lbj_根据手机号自动注册或自动登录
		Util.updatePhoneNumber(request, mobile);
		//fanys--2006-7-13 给邀请者和被邀请者发消息
		//当前登录用户为被邀请者
		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		NoticeBean notice = null;
		HappyCardSendBean cardsend=null;
		IJobService jobService = ServiceFactory.createJobService();
		IUserService userService = ServiceFactory.createUserService();

		cardsend=jobService.getHappyCardSend(" id="+id+" and mark=0 ");
		if(cardsend!=null){
		
			//WUCX2006-10-13 判断是否好友 start
			//Vector friends=userService.getFriends("user_id="+loginUser.getId()+" and friend_id="+cardsend.getUserId());
			//if(friends==null||friends.size()==0)//如果邀请人不是被邀请人的好友，则加为好友
			ArrayList  friends=UserInfoUtil.getUserFriends(loginUser.getId());		
			if(!friends.contains(cardsend.getUserId()+""))
				userService.addFriend(loginUser.getId(),cardsend.getUserId());
			
			//	friends=userService.getFriends("user_id="+cardsend.getUserId()+" and friend_id="+loginUser.getId());
			//	if(friends==null||friends.size()==0)//如果被邀请人不是邀请人的好友，则加为好友
			friends=UserInfoUtil.getUserFriends(cardsend.getUserId());
			if(!friends.contains(loginUser.getId()+""))
			//WUCX2006-10-13 判断是否好友 end
				userService.addFriend(cardsend.getUserId(),loginUser.getId());
			
			//邀请者
			UserBean invitor=UserInfoUtil.getUser(cardsend.getUserId());
			//给被邀请者发送消息,当前登录用户为被邀请者
			notice=new NoticeBean();
			notice.setUserId(loginUser.getId());
			notice.setTitle(cardsend.getSender()+"在乐酷的昵称是"+invitor.getNickName());
			notice.setContent("");
		    notice.setHideUrl("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			//notice.setLink("/job/happycard/viewCard.jsp?id=" + id);
			notice.setLink("/chat/post.jsp?toUserId=" + cardsend.getUserId());
			NoticeUtil.getNoticeService().addNotice(notice);
			
			//给邀请者发消息
			notice=new NoticeBean();
			notice.setUserId(cardsend.getUserId());//邀请者的ID
			notice.setTitle("您的好友上线了!");
			notice.setContent("");
			notice.setHideUrl("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			//notice.setLink("/chat/post.jsp?toUserId=" + loginUser.getId());
			notice.setLink("/chat/post.jsp?toUserId=" + loginUser.getId());
			NoticeUtil.getNoticeService().addNotice(notice);
			jobService.updateHappyCardSend(" mark=1,view_datetime=now()","id="+id);
			}
	}
   //response.sendRedirect(("/job/happycard/viewCard.jsp?id=" + id));
   BaseAction.sendRedirect("/job/happycard/viewCard.jsp?id=" + id, response);
}
%>