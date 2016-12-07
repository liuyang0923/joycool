<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.service.infc.*"%><%@ page import="net.wxsj.service.factory.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/zippo/game/index.jsp"));
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
IZippoService zippoService = ServiceFactory.createZippoService(IBaseService.CONN_IN_SERVICE, null);

ArrayList pairList = zippoService.getZippoPairList("user_id = " + loginUser.getId(), 0, 1, "id");

ZippoMobileBean oldMobile = zippoService.getZippoMobile("user_id = " + loginUser.getId());

boolean ended = true;
%>
<card title="猜名人专用火机">
<p align="left">
提交结果<br/>
--------------------<br/>
<%
if(ended){
%>
本次活动已经结束，不能再提交结果，谢谢您的关注！<br/>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<%
}
else if(oldMobile != null){
%>
您已经提交您的所有搭配结果！不能再继续游戏！<br/>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<%
}
//还没有一次完成的搭配
else if(pairList.size() == 0){
%>
您还没有完成一次搭配，不能提交抽奖请求！<br/>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<%
}
else {
	//进入页面
	if("get".equalsIgnoreCase(request.getMethod())){
%>
请输入您的领奖手机号，我们将通过这个号码与您联系。<br/>
<input type="text" name="mobile" maxlength="12" format="*N"/><br/>
我们将在2006年12月20号中午公布游戏结果及获奖名单，记得来看看哦~~~~<br/>
<anchor>确认提交
  <go href="/wxsj/zippo/game/confirm.jsp" method="post">
    <postfield name="mobile" value="$mobile"/>
  </go>
</anchor><br/>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<%
	}
    //处理页面
    else if("post".equalsIgnoreCase(request.getMethod())){
		String mobile = request.getParameter("mobile");
		boolean result = true;
		String tip = null;
		if(mobile == null || mobile.equals("")){
			result = false;
			tip = "手机号吗不能为空！";
		}
		else if(zippoService.getZippoMobile("mobile = '" + mobile + "'") != null){
			result = false;
			tip = "这个手机号码已被别的id提交为领奖联系号码！";
		}
		if(!result){
%>
游戏结果提交失败！<br/>
<%=tip%><br/>
<anchor>返回填写手机号<prev/></anchor><br/>
<%
		}
        else {
		    //开始事务
		    zippoService.getDbOp().init();
		    zippoService.getDbOp().startTransaction();

		    int zippoMobileId = zippoService.getNumber("id", "wxsj_zippo_mobile", "max", "id > 0") + 1;
            ZippoMobileBean zippoMobile = new ZippoMobileBean();
		    zippoMobile.setId(zippoMobileId);
		    zippoMobile.setUserId(loginUser.getId());
		    zippoMobile.setMobile(mobile);

		    zippoService.addZippoMobile(zippoMobile);
		    zippoService.getDbOp().commitTransaction();

			//加上乐币
	        int [] sts = new int[2];
	        sts[0] = JoycoolInfc.GAME_POINT;
	        sts[1] = JoycoolInfc.POINT;
	        int [] svs = {100000, 300};
	        JoycoolInfc.updateUserStatus(loginUser.getId(), sts, svs, request);
%>
游戏结果提交成功！<br/>
你的兑奖号码为<%=zippoMobileId%>。<br/>
记住哦~~手机号跟兑奖号码是领奖的唯一凭证。<br/>
您得到100000乐币，300点经验！<br/>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<%
		}
	}
}

zippoService.releaseAll();
%>
<a href="/wxsj/zippo/rule.jsp">具体奖励和答题规则</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>