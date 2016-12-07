<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsGame.getState() != vsGame.gameStart) {
	// 跳转
	response.sendRedirect("view.jsp");
	return;
}
List zombieList = vsUser.getZombieList();
int index = action.getParameterInt("x");
Object[][] view = vsGame.getGameMap();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (zombieList != null) {
	if(index >= 0 && index < zombieList.size()){
		ZombieProtoBean zombie = (ZombieProtoBean) zombieList.get(index);
		if (zombie != null) {
			if (action.hasParam("j")) {
				int j = action.getParameterInt("j");
				int currI = view.length - 1;
				if (j >= 0 && j < view[currI].length) {
					if (vsUser.isZombie()) {
						PVZBean pvzBean = (PVZBean) view[currI][j];
						if (pvzBean.getState() == 0) {
							long now = System.currentTimeMillis();
							if(zombie.getBuildTime()<now){
								if(vsGame.getZombieLineNum()[j] - vsGame.getLestLineNum() < 3) {
									int remainSun  = vsGame.getZombieSun() - zombie.getPrice();
									if (remainSun >= 0) {
										vsGame.getZombieLineNum()[j] += 1;
										vsGame.setZombieSun(remainSun);
										ZombieBean zombieBean = new ZombieBean();
										zombieBean.setProto(zombie);
										zombieBean.setHp(zombie.getMaxHp());
										zombieBean.setUid(vsUser.getUserId());
										zombieBean.setActionTime(System.currentTimeMillis() + zombie.getMoveCd());
										zombieBean.setCurrI(currI);
										zombieBean.setCurrJ(j);
										pvzBean.getZombieList().add(zombieBean);
										vsGame.setZombieNum(vsGame.getZombieNum() + 1);
										vsUser.setOperNum(vsUser.getOperNum() + 1);
										StringBuilder info = new StringBuilder();
										info.append("[僵尸]在第");
										info.append(j+1);
										info.append("列放了");
										info.append(zombie.getName());
										info.append(".");
										vsGame.getFightInformationList().addFirst(info.toString());
										zombie.setBuildTime(System.currentTimeMillis() + zombie.getBuildCd());
										%>僵尸设置成功!<br/><%
									} else {
										%>阳光不足！无法放置<br/><%
									}
								} else {
									%>设置失败,此列僵尸数量不能超过任意一列3个!<br/><%						
								}
							}else{
								%>还要等一会才能放置该僵尸!<br/><%
							}
						} else {
							%>已被攻陷,无须再放僵尸!<br/><%
						}
					} else {
						%>您无权操作!<br/><%
					}
					%><a href="view.jsp">返回战场</a><br/><%
				} else {
					response.sendRedirect("setzombie2.jsp?x="+index);
					return;
				}
			} else {
				%>请点击放置僵尸位置:<br/><%
				if (index >= 0 && index < zombieList.size()) {
					ZombieProtoBean bean = (ZombieProtoBean) zombieList.get(index);
					for (int j = 0; j < view[0].length; j++){
						PVZBean pvzBean = (PVZBean) view[0][j];
						if (pvzBean.getState() == 1) {
							%>大门.<%
						} else {
							%>一一.<%
						}	
					}
					%><br/><%
					for (int i = 1; i < view.length - 1; i++){
						for (int j=0;j<view[i].length;j++) {
							PVZBean pvzBean = (PVZBean) view[i][j];
							if (pvzBean.getState() == 0) {
								PlantBean tempPlant = pvzBean.getPlantBean();
								if (tempPlant != null) {
								%><%=tempPlant.getProto().getShortName()%>.<%
								} else {
								%>空地.<%
								}
							} else {
								%>一一.<%
							}
						}
						%><br/><%
					}
					for (int j = 0;j < view[view.length - 1].length;j++) {
						PVZBean pvzBean = (PVZBean) view[view.length - 1][j];
						if (pvzBean.getState() == 0) {
							%><a href="setzombie2.jsp?j=<%=j%>&amp;x=<%=index%>">放置.</a><%
						} else {
							%>一一.<%
						}
					}
					%><br/><a href="setzombie.jsp">返回</a><br/><%
				}
			}
		} else {
			%>僵尸数据错误,请通知管理员修改!<br/><%
		}
	} else {
		response.sendRedirect("setzombie.jsp");
		return;
	}
	

} else {
	%>无可选择僵尸,请通知管理员添加!<br/><%
} 
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>