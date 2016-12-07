<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,jc.family.game.yard.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%!
	static int[] randomCount = {3,2,2,2,1,1,1,1,1,1};
%><% response.setHeader("Cache-Control","no-cache");
YardAction action=new YardAction(request);
ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("game",action.getLoginUser().getId());
if(forbid != null) {
	response.sendRedirect("index.jsp");
	return;
}
int id = action.getParameterInt("id");
int fmId = action.getFmId();
if(fmId<=0){
	response.sendRedirect("index.jsp?id="+id);
	return;
}
if(id == 0){
	if(fmId > 0){
		id = fmId;
	}else{
		response.sendRedirect("/fm/index.jsp");
		return;
	}
}
YardBean yard = action.getYardByID(id);
int prodId = action.getParameterInt("pid");
YardItemProtoBean seed = YardAction.getItmeProto(prodId);
if (seed==null||seed.getRank()>yard.getRank()){
	response.sendRedirect("index.jsp");
	return;
}
String tip = "";
int count = 0;
TinyGame8 tinyGame = YardAction.getTinyGame8();
TinyAction tiny = new TinyAction(request, response).checkGame(tinyGame);
if(tiny == null) {
	// 失败
	tip ="生产失败.";
} else {
	if(tiny.getGameResult() != -1) {
		// 成功
		int rand = RandomUtil.nextInt(10);
		count = randomCount[rand];
		if(rand < yard.getCoopRate()){
			count++;
		}
		yard.checkCoopRate();
		StringBuilder sb = new StringBuilder();
		sb.append("恭喜你生产了");
		sb.append(count);
		sb.append("个");
		sb.append(seed.getName());
		sb.append("!!");
		if(yard.getCoopRate()!=0) {
			
			sb.append("<br/>协作度+10%");
			UserBean coopUser = UserInfoUtil.getUser(yard.getLastProduceId());
			if(coopUser!=null){
				sb.append("<br/>协作人:<a href=\"/chat/post.jsp?toUserId=");
				sb.append(coopUser.getId());
				sb.append("\">");
				sb.append(coopUser.getNickNameWml());
				sb.append("</a>");
			}
			
		}
		tip = sb.toString();
		yard.addMaterial(prodId,count);
		// 写记录
		action.addSeedLog(id,count);
		yard.setLastProduceId(action.getLoginUser().getId());
		yard.getProductLog().add(action.getLoginUser().getNickNameWml() + "生产了" +  seed.getName() +"x" + count);
	} else {
		// 失败
		tip ="生产失败.";
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="生产基地">
<p align="left">
<%if ("".equals(tip)){
%>完成关卡即可获得奖励!<br/><%
} else {
%><%=tip%><br/>当前协作度:<%=yard.getCoopRate()*10%>%<br/><a href="prod2.jsp?id=<%=id%>&amp;pid=<%=prodId%>">继续生产</a><br/><%
}%>
<a href="prod.jsp?id=<%=id%>">返回生产基地</a>
</p>
</card>
</wml>