<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.service.infc.*"%><%@ page import="net.wxsj.service.factory.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/zippo/end/index.jsp"));
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
IZippoService zippoService = ServiceFactory.createZippoService(IBaseService.CONN_IN_SERVICE, null);

ArrayList pairList = zippoService.getZippoPairList("user_id = " + loginUser.getId(), 0, -1, "id");
ZippoMobileBean oldMobile = zippoService.getZippoMobile("user_id = " + loginUser.getId());
int i, count;
ZippoPairBean pair = null;
count = pairList.size();
ZippoStarBean star = null;
ZippoBean zippo = null;
%>
<card title="猜名人专用zippo活动">
<p align="left">
您的答案<br/>
--------------------<br/>
<%
if(oldMobile == null){
%>
您没有参与搭配游戏或没有提交您的搭配结果！<br/>
<%
}
else {
	boolean notAdd = false;
	int correctCount = 0;
	for(i = 0; i < count; i ++){
		pair = (ZippoPairBean) pairList.get(i);
		star = ZippoFrk.getStarById(pair.getStarId());
		zippo = ZippoFrk.getZippoById(pair.getZippoId());
		if(pair.getResult() == 1){
			correctCount ++;
			notAdd = true;
		}
		else if(pair.getResult() == 0){
			notAdd = true;
		}
%>
<%=(i + 1)%>.<%=StringUtil.toWml(star.getName())%>:<%=StringUtil.toWml(zippo.getName())%><%if(pair.getResult() == 1 || pair.getResult() == 3){%>(正确)<%}%><br/>
<%
	}
    if(notAdd){
		//加上乐币
	    int [] sts = new int[2];
	    sts[0] = JoycoolInfc.GAME_POINT;
	    sts[1] = JoycoolInfc.POINT;
	    int [] svs = {200000 * correctCount, 200 * correctCount};
	    JoycoolInfc.updateUserStatus(loginUser.getId(), sts, svs, request);

		//更新结果
		zippoService.getDbOp().executeUpdate("update wxsj_zippo_pair set result = (result + 2) where user_id = " + loginUser.getId());
%>
您猜对了<%=correctCount%>个搭配，一共得到<%=(200000 * correctCount)%>乐币，<%=(200 * correctCount)%>点经验！<br/>
<%
	}
}
%>
<%
zippoService.releaseAll();
%>
<a href="/wxsj/zippo/end/index.jsp">返回颁奖页面</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>