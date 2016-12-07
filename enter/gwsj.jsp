<%@page import="net.joycool.wap.util.CookieUtil"%>
<%@page import="net.joycool.wap.framework.BaseAction"%>
<%@page import="net.joycool.wap.framework.JoycoolSessionListener"%>
<%@page import="net.joycool.wap.service.factory.ServiceFactory"%>
<%@page import="net.joycool.wap.bean.NoticeBean"%>
<%@page import="net.joycool.wap.cache.util.UserBagCacheUtil"%>
<%@page import="net.joycool.wap.util.UserInfoUtil"%>
<%@page import="net.joycool.wap.bean.UserStatusBean"%>
<%@page import="net.joycool.wap.util.LogUtil"%>
<%@page import="net.joycool.wap.util.SecurityUtil"%>
<%@page import="net.joycool.wap.util.SequenceUtil"%>
<%@page import="net.joycool.wap.bean.UserBean"%>
<%@page import="net.joycool.wap.util.RandomUtil"%>
<%@page import="net.joycool.wap.service.impl.UserServiceImpl"%>
<%@page import="net.joycool.wap.util.StringUtil"%>
<%@page import="net.joycool.wap.util.SqlUtil"%>
<%@page import="jc.user.UserAction2"%>
<%@page import="net.joycool.wap.action.user.UserAction"%>
<%@page import="net.joycool.wap.util.Util"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ page import="other.util.*"%>
<%!
	static net.joycool.wap.service.impl.UserServiceImpl service = new net.joycool.wap.service.impl.UserServiceImpl();
%>
<%
     //国网手机请求这个地址，带过来两个参数一个是栏目id：froumId;另一个是token
     String forumId=request.getParameter("forumId");
     String token  =request.getParameter("token");
     if(null==token||"".equals(token)){//1如果token为空说明用户没有登录，则直接跳转到相应的论坛版块即可
    	 response.sendRedirect("/jcforum/forum.jsp?forumId="+forumId);
     }else{//2token不为空，此时需要到国网手机服务端验证token的真实性
    	 //目前测试地址ip为：http://124.207.34.148:8080；后面要改为正式的
    	 //String requestUrl="http://124.207.34.148:8080/rest/community/checkToken.json";
    	 String requestUrl="http://xg.innovationpower.cn:8888/rest/community/checkToken.json";
    	 HttpResponse httpResponse= Http.doRequest(requestUrl,"POST", "token="+token, "UTF-8");
         if (httpResponse == null) {//2.1如果委会结果为null，则直接跳转到论坛版块
        	 response.sendRedirect("/jcforum/forum.jsp?forumId="+forumId);
         } else {//返回内容2.2
             String responseStr=new String(httpResponse.getContent());
             if("".equals(responseStr)){//2.2.1如果返回的内容为空，则直接跳转到相应的论坛版块即可
            	 response.sendRedirect("/jcforum/forum.jsp?forumId="+forumId);
             }else{//2.2.2返回的值不为空，国网手机返回一个手机号的字符串如13655161319
            	 String[] userInfoArr =responseStr.split(",");//返回的是个数组
            	 
            	 String phone=userInfoArr[0];//手机号
            	 String password=userInfoArr[1];//密码
            	 String nickName=userInfoArr[2];//昵称
            	 int age=StringUtil.toInt(userInfoArr[3]);//年龄
            	 int sex=StringUtil.toInt(userInfoArr[4]);//性别

 				 int userId=0;
 				 synchronized(token){
 					userId = SqlUtil.getIntResult("select user_id from qqlogin where openid='" + StringUtil.toSql(phone)+"'");
 					if(userId<=0){	// 新注册用户
 						if(password!=null){
							 // 注册新用户，从RegisterAction拷贝
 						    nickName=nickName.replaceAll("[\\t\\n\\r ]","");
 							if (service.getUser("nickname = '" + StringUtil.toSql(nickName) + "'") != null) {	// 昵称有人用了，随机加3个数字
 								nickName += RandomUtil.nextInt(1000);
 							}
 							UserBean user = new UserBean();
 							
 							int userID = SequenceUtil.getSeq("userID");
 							user.setId(userID);//用户id
 							user.setMobile(phone);//手机号
 							user.setPassword(password);
 							if(SecurityUtil.checkNick(nickName)){//昵称
 								user.setNickName(nickName);
 							}else{
 								user.setNickName(String.valueOf(userID));				
 							}
 							user.setAge(age);//用户年龄
 							user.setGender(sex);//用户性别
 							user.setSelfIntroduction("");
 							service.addUser(user);
 			
 							String linkIds = (String) session.getAttribute("linkId");
 							StringBuilder logsb = new StringBuilder(128);
 							logsb.append(user.getMobile()+':'+user.getId()+":"+user.getNickName().replace(':', '_')+":"+request.getRemoteAddr()+
 									     ":"+"/user/RegisterOk.do"+":0:"+session.getId()+":"+StringUtil.toId(linkIds)+":0");
 							LogUtil.logPv(logsb.toString());
 			
 							UserStatusBean usb = UserInfoUtil.getUserStatus(user.getId());
 							if (usb == null) {//给新注册的用户添加用户附加信息
 								usb = new UserStatusBean();
 								usb.setUserId(user.getId());
 								usb.setPoint(0);
 								usb.setRank(3);
 								usb.setGamePoint(100000);
 								usb.setNicknameChange(0);
 								usb.setLoginCount(0);
 								service.addUserStatus(usb);
 								session.setAttribute("loginUser", user);//将用户信息放入缓存中
 								// 赠新人卡
 								UserBagCacheUtil.addUserBagCache(user.getId(), 33);
 								
 								NoticeBean notice = new NoticeBean();
 								notice.setUserId(user.getId());
 								notice.setTitle("登陆成功!赠新人卡一张!");
 								notice.setContent("");
 								notice.setType(NoticeBean.GENERAL_NOTICE);
 								notice.setHideUrl("");
 								notice.setLink("/user/registerSuccess.jsp");
 								ServiceFactory.createNoticeService().addNotice(notice);
 							}
 							//添加一条记录
 							SqlUtil.executeUpdate("insert into qqlogin set create_time=now(),user_id="+userID+",openid='"+StringUtil.toSql(phone)+"',ip='"+request.getRemoteAddr()+"'");
 			
 							UserBean loginUser = UserInfoUtil.getUser(userID);
 							loginUser.setIpAddress(request.getRemoteAddr());
 							loginUser.setUserAgent(request.getHeader("User-Agent"));
 							JoycoolSessionListener.updateOnlineUser(request, loginUser);
 							BaseAction.sendRedirect("/jcforum/forum.jsp?forumId="+forumId, response);
 						}
 					}
 				}
 				
 				if(userId>0){	// 注册成功或者登陆成功
 					if (SecurityUtil.checkForbidUserId(userId)) {//如果用户被拒绝访问则跳转到论坛页面
 					    BaseAction.sendRedirect("/jcforum/forum.jsp?forumId="+forumId, response);
 						return;
 					}
 					UserBean loginUser = UserInfoUtil.getUser(userId);
 					if(loginUser!=null){
 						loginUser.setIpAddress(request.getRemoteAddr());
 						loginUser.setUserAgent(request.getHeader("User-Agent"));
 						JoycoolSessionListener.updateOnlineUser(request, loginUser);//将user写入session
 						session.setAttribute("cd-fourmSTime", Long.valueOf(System.currentTimeMillis() + 20000));	// 20秒内不能发帖或者回帖
 						LogUtil.logLogin("gwsjlogin:" + loginUser.getId() + ":" + request.getRemoteAddr() + ":" + request.getHeader("User-Agent") + ":" + new CookieUtil(request).getCookieValue("jsid"));
 					}
 					BaseAction.sendRedirect("/jcforum/forum.jsp?forumId="+forumId, response);
 				}
                 
             }
         }
     }
%>
 