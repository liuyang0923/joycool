<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.util.*,java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.pvz.*,jc.family.*"%><%
PVZAction action=new PVZAction(request,response);
%><%@include file="inc.jsp"%><%
if (vsUser == null || vsGame.getState() != vsGame.gameStart) {
	// 跳转
	response.sendRedirect("view.jsp");
	return;
}
List plantProtoList = vsUser.getPlantList();
int index = action.getParameterInt("x");
Object[][] view = vsGame.getGameMap();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="庄园大战"><p align="left"><%=BaseAction.getTop(request, response)%><%
if (plantProtoList != null) {
	if(index >= 0 && index < plantProtoList.size()){
		PlantProtoBean plantProto = (PlantProtoBean) plantProtoList.get(index);
		if (plantProto != null) {
			if (action.hasParam("i") && action.hasParam("j")) {
				int currI = action.getParameterInt("i");
				int currJ = action.getParameterInt("j");
				if (currI > 0 && currI < view.length - 1 && currJ >= 0 && currJ < view[currI].length) {
					if (vsUser.isPlant()) {
						PVZBean pvzBean = (PVZBean) view[currI][currJ];
						if (pvzBean.getState() == 0) {
							long now = System.currentTimeMillis();
							if(plantProto.getBuildTime()<now){
								if (pvzBean.getPlantBean() == null) {
									int remainSun = vsGame.getPlantSun() - plantProto.getPrice();
									if (remainSun >= 0) {
										vsGame.setPlantSun(remainSun);
										PlantBean plantBean = new PlantBean();
										plantBean.setProto(plantProto);
										plantBean.setHp(plantProto.getMaxHp());
										plantBean.setUid(vsUser.getUserId());
										if(plantProto.getAttackCd()>1) {
											if (plantBean.getProto().getType() == 3) {
												plantBean.setActionTime(System.currentTimeMillis() + plantProto.getAttackCd());
											} else {
												plantBean.setActionTime(System.currentTimeMillis() + RandomUtil.nextInt(plantProto.getAttackCd()/2));
											}
										}
										pvzBean.setPlantBean(plantBean);
										vsGame.setPlantNum(vsGame.getPlantNum()+1);
										vsUser.setOperNum(vsUser.getOperNum() + 1);
										StringBuilder info = new StringBuilder();
										info.append("[植物]在第");
										info.append(currJ+1);
										info.append("列种了");
										info.append(plantProto.getName());
										info.append(".");
										vsGame.getFightInformationList().addFirst(info.toString());
										plantProto.setBuildTime(System.currentTimeMillis() + plantProto.getBuildCd());
										%>植物种植成功!<br/><%
									} else {
										%>阳光不足！无法放置<br/><%
									}
								} else {
									%>已有植物,请换个地方种植<br/><%
								}
							}else{
								%>还要等一会才能放置该植物!<br/><%
							}
						} else {
							%>已被攻陷,无法再种植物!<br/><%
						}
					} else {
						%>您无权操作!<br/><%
					}
					%><a href="view.jsp">返回战场</a><br/><%
				} else {
					response.sendRedirect("setplant2.jsp?x="+index);
					return;
				}
			} else {
				%>请点击放置植物位置:<br/><%
				if (index >= 0 && index < plantProtoList.size()) {
					PlantProtoBean bean = (PlantProtoBean) plantProtoList.get(index);
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
								PlantBean plantBean = (PlantBean) pvzBean.getPlantBean();
								if (plantBean != null) {
								%><%=plantBean.getProto().getShortName()%>.<%
								} else {
								%><a href="setplant2.jsp?i=<%=i%>&amp;j=<%=j%>&amp;x=<%=index%>">放置.</a><%
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
							%>墓地.<%
						} else {
							%>一一.<%
						}
					}
					%><br/><a href="setplant.jsp">返回</a><br/><%
				}
			}
		} else {
			%>植物数据错误,请通知管理员修改!<br/><%
		}
	} else {
		response.sendRedirect("setplant.jsp");
		return;
	}
	

} else {
	%>无可选择植物,请通知管理员添加!<br/><%
} 
%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>