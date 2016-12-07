<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.service.infc.*"%><%@ page import="net.wxsj.service.factory.*"%><%
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

int starId = StringUtil.toInt(request.getParameter("id"));
int zippoId = StringUtil.toInt(request.getParameter("zippoId"));

ZippoPairBean starPair = zippoService.getZippoPair("user_id = " + loginUser.getId() + " and star_id = " + starId);
ZippoPairBean zippoPair = zippoService.getZippoPair("user_id = " + loginUser.getId() + " and zippo_id = " + zippoId);

ZippoMobileBean zippoMobile = zippoService.getZippoMobile("user_id = " + loginUser.getId());
%>
<card title="猜名人专用火机">
<p align="left">
猜测结果<br/>
----------------<br/>
<%
if(zippoMobile != null){
%>
您已经提交您的所有猜测结果！不能再继续游戏！<br/>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<%
}
else if(zippoPair != null){
%>
此次搭配无效！你已经给该zippo（芝宝）选了一个明星了！<br/>
<a href="/wxsj/zippo/game/zippo.jsp?id=<%=zippoId%>">返回</a><br/>
<%
}
else if(starPair != null){
%>
此次搭配无效！你已经给该明星配备过一款zippo（芝宝）了！<br/>
<a href="/wxsj/zippo/game/zippo.jsp?id=<%=zippoId%>">返回</a><br/>
<%
}
else {
	if(request.getParameter("confirm") != null){
		ZippoBean zippo = ZippoFrk.getZippoById(zippoId);
		//开始事务
		zippoService.getDbOp().init();
		zippoService.getDbOp().startTransaction();

		int pairId = zippoService.getNumber("id", "wxsj_zippo_pair", "max", "id > 0") + 1;
		ZippoPairBean pair = new ZippoPairBean();
		pair.setId(pairId);
		pair.setUserId(loginUser.getId());
		pair.setStarId(starId);
		pair.setZippoId(zippoId);
		pair.setTypeId(zippo.getTypeId());
		pair.setCreateDatetime(DateFrk.getNow());

		ZippoStarBean star = ZippoFrk.getStarById(starId);
		if(star.getTypeId() == zippo.getId()){
			pair.setResult(1);
		}

		zippoService.addZippoPair(pair);
		zippoService.getDbOp().commitTransaction();
%>
此次搭配已完成！<br/>
<br/>
<a href="/wxsj/zippo/game/index.jsp">我还要猜其他明星的zippo</a><br/>
<a href="/lswjs/index.jsp">先答到这里回头再来继续猜</a><br/>
<a href="/wxsj/zippo/game/confirm.jsp">就猜那么多了提交最终答案，等着拿奖了</a><br/>
<%
	}
    else {
%>
确认这次搭配么？猜测结果不能更改的哦~~<br/>
<a href="<%=("/wxsj/zippo/game/chooseStar.jsp?id=" + starId + "&amp;zippoId=" + zippoId + "&amp;confirm=true")%>">确认</a><br/>
<a href="/wxsj/zippo/game/zippo.jsp?id=<%=zippoId%>">再看看</a><br/>
<%
	}
}

zippoService.releaseAll();
%>
<a href="/wxsj/zippo/rule.jsp">具体奖励和答题规则</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>