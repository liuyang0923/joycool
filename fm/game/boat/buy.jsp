<%@ page language="java" import="net.joycool.wap.spec.shop.*,net.joycool.wap.bean.*,jc.family.game.*,net.joycool.wap.util.*,jc.family.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
BoatAction action=new BoatAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
int mid = action.getParameterInt("mid");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买龙舟"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean != null){
	if(FamilyUserBean.isflag_game(fmbean.getFm_flags())){
		MatchBean matchBean = (MatchBean) GameAction.matchCache.get(new Integer(mid));
		if(matchBean != null && matchBean.getType() == 1){
			if(matchBean.getState() == 0){
				int bid = action.getParameterInt("bid");
				BoatBean bean = BoatAction.service.getBoatBean(" id="+bid);
				if(bean != null){
					UserBean userBean = action.getLoginUser();
					int uid = userBean.getId();
					if(bean.getRentType() == 0){
						UserStatusBean usb = UserInfoUtil.getUserStatus(uid);
						if(usb != null){
							long gamePoint = usb.getGamePoint();
							long rent = (long) bean.getRent() * 100000000;
							if(gamePoint > rent){
								UserInfoUtil.updateUserCash(uid, 0 - rent, 20, "购买了龙舟"+StringUtil.toSql(bean.getName()));
								BoatAction.service.upd("update fm_game_fmboat set is_use=1 where fid="+fmbean.getFm_id());
								BoatAction.service.upd("insert into fm_game_fmboat (fid,bid,use_time)values("+fmbean.getFm_id()+","+bean.getId()+","+bean.getUseTime()+")");
								response.sendRedirect("buy.jsp?a=1&mid="+mid);
								return;
							} else {
							%>乐币不足,无法购买!<br/><%
							}
						} else {
						%>乐币不足,无法购买!<br/><%
						}
					} else if(bean.getRentType() == 1) {
						UserInfoBean uib = ShopAction.shopService.getUserInfo(uid);
						if(uib != null){
							float gold = uib.getGold();
							float rent = bean.getRent();
							if(gold > rent){
								ShopUtil.updateUserGold(uid, rent, 7);
								BoatAction.service.upd("update fm_game_fmboat set is_use=1 where fid="+fmbean.getFm_id());
								BoatAction.service.upd("insert into fm_game_fmboat (fid,bid,use_time)values("+fmbean.getFm_id()+","+bean.getId()+","+bean.getUseTime()+")");
								response.sendRedirect("buy.jsp?a=1&mid="+mid);
								return;
							} else {
							%>酷币不足,无法购买!<br/><%
							}
						} else {
						%>酷币不足,无法购买!<br/><%
						}
					}
				} else {
					if(1==action.getParameterInt("a")){
					%>购买成功!<br/><%
					} else {
					%>无此龙舟<br/><%
					}
				}
			}else{
			%>非报名阶段,无法购买龙舟!<br/><%
			}
		} else {
		%>不存在龙舟赛事,不能购买龙舟!<br/><%
		}
	} else {
	%>您不是游戏管理员,不能购买龙舟!<br/><%
	}
}
%><a href="shop.jsp?mid=<%=mid%>">返回龙舟商店</a><br/><a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>