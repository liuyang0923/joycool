<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session="false"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction,net.joycool.wap.bean.NoticeBean,net.joycool.wap.cache.*, net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = null;
HttpSession session = request.getSession(false);
if(session!=null)
	loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(loginUser == null){
/*	String ip = request.getRemoteAddr();
	if(ip.startsWith("59.151.46.")) {
		BaseAction.sendRedirect("/wapIndex.jsp", response);
		return;
	}*/
	String code = request.getParameter("code");
	if(code == null){
        //liuyi 2007-01-25 登录问题修改 start
		//response.sendRedirect((BaseAction.INDEX_URL));
        BaseAction.sendRedirect("/user/login.jsp", response);
	    //liuyi 2007-01-25 登录问题修改 end
		return;
	}
	int qm = code.indexOf('?');
	if(qm >= 0)
		code = code.substring(0, qm);
	String [] ss = code.split("AAAA");
	if(ss == null || ss.length != 2){
        //liuyi 2007-01-25 登录问题修改 start
		//response.sendRedirect((BaseAction.INDEX_URL));
		BaseAction.sendRedirect("/user/login.jsp", response);
	    //liuyi 2007-01-25 登录问题修改 end
		return;
	}
	String idStr = ss[0];
	String password = ss[1];
	int id = StringUtil.toInt(Encoder.decrypt(idStr));
	if(id == -1){
        //liuyi 2007-01-25 登录问题修改 start
		//response.sendRedirect((BaseAction.INDEX_URL));
		BaseAction.sendRedirect("/user/login.jsp", response);
	    //liuyi 2007-01-25 登录问题修改 end
		return;
	}else if(SecurityUtil.checkForbidUserId(id)){
		BaseAction.sendRedirect("/user/login.jsp", response);
		return;
	}
	loginUser = UserInfoUtil.getUser(id);
	if(loginUser == null || !loginUser.getPassword().equals(password)){
        //liuyi 2007-01-25 登录问题修改 start
		//response.sendRedirect((BaseAction.INDEX_URL));
		BaseAction.sendRedirect("/user/login.jsp", response);
	    //liuyi 2007-01-25 登录问题修改 end
		return;
	}
	session = SecurityUtil.newSession(request);
	loginUser.setIpAddress(request.getRemoteAddr());
	loginUser.setUserAgent(request.getHeader("User-Agent"));
	JoycoolSessionListener.updateOnlineUser(request, loginUser);

	//liuyi 2006-12-28 登录统计修改 start
	LogUtil.logLogin("bookmark:" + loginUser.getId() + ":" + request.getRemoteAddr());
    //liuyi 2006-12-28 登录统计修改 end
	
	//liuyi 2006-12-26 书签奖励 start
//	String flag = (String)OsCacheUtil.get(loginUser.getId()+"", "enterGroup", 3600*24); //每天最多一次
//	if(flag==null){
//		String sql = "select reward_count from jc_bookmark_reward where user_id=" + loginUser.getId();
//		int rewardCount = SqlUtil.getIntResult(sql, Constants.DBShortName);
//		if(rewardCount<5){  //最多5次
//		    UserInfoUtil.updateUserStatus("game_point=game_point+1000,point=point+100",
//				    "user_id=" + loginUser.getId(), loginUser.getId(),
//				    UserCashAction.PRESENT, "书签登录获1000乐币和100经验值");
//		    OsCacheUtil.put(loginUser.getId()+"", "true", "enterGroup");
//		    
//		    if(rewardCount<0){
//		    	sql = "insert into jc_bookmark_reward(user_id, reward_count) values(" + loginUser.getId() + ",1)";
//		    }
//		    else{
//		    	sql = "update jc_bookmark_reward set reward_count=reward_count+1 where user_id=" + loginUser.getId();
//		    }
//		    SqlUtil.executeUpdate(sql, Constants.DBShortName);
//		
//		    NoticeBean notice=new NoticeBean();
//		    notice.setUserId(loginUser.getId());
//		    notice.setTitle("书签登录获赠1000乐币和100经验值!");
//		    notice.setContent("");
//		    notice.setHideUrl("");
//		    notice.setType(NoticeBean.GENERAL_NOTICE);
//		    notice.setLink("/lswjs/index.jsp");
//		    NoticeUtil.getNoticeService().addNotice(notice);
//		}
//	}
	//liuyi 2006-12-26 书签奖励 end
	
	//liuyi 2006-12-20 登录注册修改 start
	String mobile = (String) session.getAttribute("userMobile");
 
	//liuyi 2006-12-20 登录注册修改 end
	//liuyi 2007-01-25 登录问题修改 start
	//response.sendRedirect((BaseAction.INDEX_URL));
	BaseAction.sendRedirect(null, response);
    //liuyi 2007-01-25 登录问题修改 end
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card id="card1" title="乐酷游戏社区">
<p align="left">
保存书签<br/>
---------<br/>
尊敬的乐酷用户,您把“本页”加为手机书签后,以后从这个书签就可以自动登录进入乐酷啦，不需要输入用户ID和密码啦！（注意，每次修改密码之后必须重新保存本页面！）<br/>
这个页面书签代表的用户是ID<%= loginUser.getId() %>的帐号哦。<br/>
如果您还不知道怎么把本页加为书签,请看<a href="#card2">帮助</a>,但必须添加“本页”为书签才算成功，不能加首页或者其他页面哦！<br/>
<a href="<%=BaseAction.INDEX_URL%>">返回首页</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

<card id="card2" title="乐酷游戏社区">
<p align="left">
保存书签的方法<br/>
---------<br/>
诺基亚:依次选"操作"-"增加书签"<br/>
摩托罗拉:依次选"菜单键"-"书签"-"标记站点"-"保存"<br/>
索爱:依次选"移动梦网"-"更多"-"书签"-"添加书签"-"确定"<br/>
三星:依次选"功能表"-"娱乐功能"-"WAP浏览器" -"收藏夹"-选择一个空的收藏夹地址-确认url地址-输入"乐酷门户"<br/>
松下:选择页面左上角的"菜单"-"书签"-"标记站点"-"保存"<br/>
西门子:依次选"上网键"-"收藏夹"-"储存"<br/>
CECT:依次选"菜单"-"保存书签"-"保存"<br/>
LG:依次选"菜单"-"书签"-"标记站点"-"保存"<br/>
三菱:按左功能键-"书签"-"添加新书签"-"保存"<br/>
海尔:网页浏览状态下长按"*"键-"书签"-"新建"-"编辑"-输入"乐酷门户"-"保存"<br/>
夏新:访问网站时选中页面左上角-"书签"-"保存"- "新建" -输入""乐酷门户"-"保存"<br/>
联想:依次选"浏览器"-"空书签"-输入"乐酷门户"-"保存"<br/>
东信:依次选"选项"-"保存书签"<br/>
NEC:依次选"菜单"-"访问WEB浏览器"-"选择网站"-"书签"-"标记站点"-"保存"<br/>
<a href="#card1">返回保存书签</a><br/>
<a href="<%=BaseAction.INDEX_URL%>">返回首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>