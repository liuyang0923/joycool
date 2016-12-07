<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%><%! 
	static String[] errors = {"","你还没有在花之境中购买花的养殖地,点击【<a href=\"buyField.jsp\">购买养殖地</a>】去购买.<br/><a href=\"index.jsp\">返回花之境首页</a><br/>",
						  "不可重复购买.<br/><a href=\"fgarden.jsp\">返回我的花园</a><br/>",
						  "该页面无法访问.<br/><a href=\"fgarden.jsp\">返回我的花园</a><br/>",
						  "你的花房里没有花,无法进入实验室哦.<br/><a href=\"fgarden.jsp\">返回我的花园</a><br/>",
						  "你没有开通农场,或者钱数低于20000,请先去农场攒钱吧.<br/><a href=\"../myGarden.jsp\">返回我的农场</a><br/>",
						  "每位玩家只可拥有一块土地.<br/><a href=\"fgarden.jsp\">返回我的花园</a><br/>",
						  "你没有进行合成试验或合成时间未到.<br/><a href=\"lab.jsp\">返回我的实验室</a><br/>",
						  "你的金币或成就值不够.<br/><a href=\"fgarden.jsp\">返回我的花园</a><br/>",
						  "你已经被对方加入黑名单,无法进入对方的花园.<br/><a href=\"friend.jsp\">其他好友的花园</a><br/><a href=\"fgarden.jsp\">返回我的花园</a><br/>",
						  "你的培养皿中没有花.<br/><a href=\"lab.jsp\">返回</a><br/>",
						  "该用户不存在.<br/><a href=\"friend.jsp\">返回</a><br/>",
						  "只能种在自己的土地中.<br/><a href=\"fgarden.jsp\">返回</a><br/>",
						  "该土地已是河畔,无需升级!.<br/><a href=\"fgarden.jsp\">返回</a><br/>",
						  "已达最大土地拥有量,无法扩充！<br/><a href=\"fgarden.jsp\">返回</a><br/>",
						  "合成试验进行中,无法取消.<br/><a href=\"lab.jsp\">返回</a><br/>"};
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%	response.setHeader("Cache-Control","no-cache");
	FlowerAction action = new FlowerAction(request);
	int errorNum = action.getParameterInt("e");;
	if (errorNum <= 0 || errorNum > errors.length-1){
		errorNum = 3;
	}%><wml><card title="花之境"><p><%=BaseAction.getTop(request, response)%>
<%=errors[errorNum]%>
<%=BaseAction.getBottomShort(request, response)%></p>
</card></wml>