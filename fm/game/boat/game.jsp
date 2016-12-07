<%@ page language="java" import="jc.util.*,net.joycool.wap.bean.*,java.util.*,jc.family.game.*,net.joycool.wap.util.*,java.text.*,jc.family.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%!
static int NUMBER_OF_PAGE = 3;%><%
BoatAction action=new BoatAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
String ct = request.getParameter("ct");
int ifImg = action.getParameterInt("ifImg");
if("a".equals(ct)||"f".equals(ct)){session.setAttribute("ct",ct);}
if(session.getAttribute("ct")!=null){ct=(String)session.getAttribute("ct");}
SimpleChatLog2 scl =null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="龙舟比赛" ontimer="<%=response.encodeURL("game.jsp?ct="+ct+"&amp;ifImg="+ifImg)%>"><timer value="100"/><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean == null){
	out.println("你没有加入家族");
}else{
	MatchBean matchBean = null;
	UserBean userBean = action.getLoginUser();
	int uid = userBean.getId();
	int fid = fmbean.getFm_id();
	int mid = action.getParameterInt("mid");
	if(mid > 0){
		matchBean = (MatchBean)BoatAction.matchCache.get(new Integer(mid));
		if(matchBean != null)
			session.setAttribute("mid",new Integer(mid));
	}
	if(mid == 0){
		Integer inmid = (Integer)session.getAttribute("mid");
		mid = inmid.intValue();
		matchBean = (MatchBean)BoatAction.matchCache.get(new Integer(mid));
	}
	boolean canSee = action.canSee(matchBean,mid,fid,uid);
	if(canSee){
		HashMap gameMap = matchBean.getGameMap();
		BoatGameBean bean = (BoatGameBean)gameMap.get(new Integer(fid));
		MemberBean memberBean = (MemberBean) bean.getMemberMap().get(new Integer(uid));
		if(bean.getIsover() != 2){
			int station = 0;
			action.sit(bean,uid);
			int mySeat = memberBean.getSeat();
			if(mySeat >= 0 && mySeat < 11){
				if(mySeat%2 == 0){
					station = 1;//左排
				}else{
					station = 2;//右排
				}
				if(memberBean.getSeat()==10){
					station = 3;//掌舵人
				}
			}
			%><a href="game.jsp?ifImg=<%=ifImg%>">刷新</a>|<%if(ifImg == 0){%><a href="game.jsp?ifImg=1">关图片</a><%} else {%><a href="game.jsp?ifImg=0">开图片</a><%}%><br/><%
			if(bean.getAccident().length() > 0){
				%><%if(ifImg == 0){%><img src="/rep/family/boat/<%=bean.getAccidentImg()%>" alt="x"/><br/><%}%><%=StringUtil.toWml(bean.getAccident())%><br/><%
			}
			if(station==0){
			%>你还没有坐下,请先选择你的座位!<br/><a href="seat.jsp">龙舟座位</a><br/><%
			}else{
				int hit = action.getParameterInt("hit");
				BoatBean boat = bean.getBoat();
				if(hit==1 || hit==2){
					if(memberBean.getHit()==0){
						memberBean.setHit(hit);
					}
				}else if(hit == 3 && station == 3 && boat.getSpeAngleReset() == 1 && System.currentTimeMillis() - boat.getSpeEffectTime() > 240000){
					boat.setSpeEffectTime(System.currentTimeMillis());
					bean.setAngle(0);
					bean = (BoatGameBean)gameMap.get(new Integer(fid));
				}
				if(station==1){
					%><a href="game.jsp?hit=1&amp;ifImg=<%=ifImg%>">↑</a>&#160;<a href="game.jsp?hit=2&amp;ifImg=<%=ifImg%>">→</a><%
				}else if(station==2){
					%><a href="game.jsp?hit=2&amp;ifImg=<%=ifImg%>">←</a>&#160;<a href="game.jsp?hit=1&amp;ifImg=<%=ifImg%>">↑</a><%
				}else if(station==3){
					%><a href="say.jsp">发言</a><br/><%
					if(boat.getSpeAngleReset() == 1){
						long coolTime = 240000 + boat.getSpeEffectTime() - System.currentTimeMillis();
						if((coolTime) < 0){
						%><a href="game.jsp?hit=3&amp;ifImg=<%=ifImg%>">复位</a><br/><%
						}else{
						%>复位(<%=GameAction.getFormatDifferTime(coolTime)%>)<br/><%
						}
					}
					%><a href="game.jsp?hit=1&amp;ifImg=<%=ifImg%>">←</a>&#160;<a href="game.jsp?hit=2&amp;ifImg=<%=ifImg%>">→</a><%
				}
				%><br/><%
			}
			DecimalFormat numFormat = new DecimalFormat("#.#");
			%>有效速度:<%=numFormat.format(bean.getSpeed())%>米/分<br/>
已完成:<%=numFormat.format(bean.getDistance()/(bean.getAllDistance()/100))%>%&#160;<%if(bean.getAngle()==0){if(ifImg == 0){%><img src="/rep/family/boat/ahead.gif" alt="x"/><%}else{%>直行<%}}else if(bean.getAngle()>0){if(ifImg == 0){%><img src="/rep/family/boat/right.gif" alt="x"/><%}else{%>右偏<%}%><%=Math.abs(bean.getAngle())%>度<%}else{if(ifImg == 0){%><img src="/rep/family/boat/left.gif" alt="x"/><%}else{%>左偏<%}%><%=Math.abs(bean.getAngle())%>度<%}%><br/>
----游戏交流------<a href="chatmore.jsp">更多</a><br/><%
			if("a".equals(ct)){
				scl = SimpleChatLog2.getChatLog(mid,"match");
				%><a href="game.jsp?ct=f&amp;mid=<%=mid%>&amp;ifImg=<%=ifImg%>">家族</a>|所有<br/><%
			}else{
				StringBuilder fmChatId = new StringBuilder();
				fmChatId.append(mid);
				fmChatId.append(fid);
				scl = SimpleChatLog2.getChatLog(Integer.parseInt(fmChatId.toString()),"match_fm");
				%>家族|<a href="game.jsp?ct=a&amp;mid=<%=mid%>&amp;ifImg=<%=ifImg%>">所有</a><br/><%
			}
			PagingBean paging = new PagingBean(action, scl.size(),NUMBER_OF_PAGE,"p");
			String content = action.getParameterNoEnter("content");
			if(content != null&&!"".equals(content)) {		// 发言
				if(content.length() > 100){
					content = content.substring(0,100);
				}
				scl.add(uid,userBean.getNickNameWml(),StringUtil.toWml(content));
			}
%><%=scl.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<a href="say.jsp">发言</a><br/><a href="gamelive.jsp?mid=<%=mid%>">比赛实况</a><br/><a href="seat.jsp">龙舟座位</a><br/><%
		} else {
			DecimalFormat numFormat = new DecimalFormat("#.#");
			if(bean.getDistance() >= bean.getAllDistance()){
%>恭喜!!本家族的龙舟到达终点!!<br/>名次:第<%=bean.getRank()%>名<br/>里程:<%=bean.getAllDistance()%>米<br/>用时:<%=GameAction.getFormatDifferTime(bean.getSpendTime())%><br/>当前速度:<%=numFormat.format(bean.getSpeed())%>米/分<br/>最大速度:<%=numFormat.format(bean.getMaxSpeed())%>米/分<br/><%
			} else {
%>比赛结束,本家族的龙舟未到达终点!<br/>名次:第<%=bean.getRank()%>名<br/>里程:<%=numFormat.format(bean.getDistance())%>米<br/>用时:<%=GameAction.getFormatDifferTime(bean.getSpendTime())%><br/>当前速度:<%=numFormat.format(bean.getSpeed())%>米/分<br/>最大速度:<%=numFormat.format(bean.getMaxSpeed())%>米/分<br/><%
			}
		}
	}
	if(request.getAttribute("tip") != null){
		%><%=request.getAttribute("tip")%><br/><%
	}
	%><a href="/fm/game/fmgame.jsp">返回家族活动</a><br/><%
}%><%=BaseAction.getBottomShort(request,response)%></p></card></wml>