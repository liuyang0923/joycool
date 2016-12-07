<%@ page pageEncoding="utf-8"%><%

FamilyUserBean fmUser = action.getFmUser();
int id=action.getParameterInt("id");
VsGameBean vsGame=action.getVsGameById(id);
if(vsGame == null){
	if(id>0){
		// TODO 服务器重启后数据丢失，需要查挑战表退钱
	}
	response.sendRedirect("/fm/game/vs/vsgame.jsp?id="+fmUser.getFm_id());
	return;
}
action.addVsGame(vsGame);
if(action.hasParam("c")){
	if(net.joycool.wap.util.ForbidUtil.isForbid("game",fmUser.getId())){
		response.sendRedirect("/enter/not.jsp");
		return;
	}
	String tip=null;
	if(action.isInFm(fmUser.getFm_id())){
		if(FamilyUserBean.isVsGame(fmUser.getFm_state())){
			if(vsGame.notStopEnter()){
				if(!vsGame.addUser(fmUser)){
					tip="本家族参与该场挑战人数已达上限("+vsGame.getMaxPlayer()+"人)无法进入";
				}
			}else if(action.getVsUser(vsGame.getGameType())==null){
				tip="游戏已开始"+vsGame.getEnterTime()+"分钟,无法参赛";
			}
		}else{
			tip="您不是家族精英,无法参赛";
		}
	}else{
		tip="非本家族比赛,无法参赛";
	}
	if(tip != null){
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族挑战"><p align="left">
<%=tip%><br/>
<a href="<%=vsGame.getGameUrl()%>?id=<%=vsGame.getId()%>">&gt;&gt;观看游戏</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml><%
		return;
	}
}
%>