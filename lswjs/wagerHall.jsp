<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.cache.OsCacheUtil,net.joycool.wap.action.chat.RoomRateAction,net.joycool.wap.framework.JoycoolSpecialUtil"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.wgame.WGameAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 start
String reURL=request.getRequestURL().toString();
String queryStr=request.getQueryString();
session.setAttribute("loginReturnURL",queryStr==null?reURL:reURL+"?"+queryStr);
//zhul 2006-09-07 定义点好友或邮箱进入登陆后返回页 end
//用户乐币数
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean us=null;
int friendCount=0;

//liuyi 2006-12-27 通吃岛首页修改 start
int totalPk = 0;
int winPk = 0;
if(loginUser!=null){
us=UserInfoUtil.getUserStatus(loginUser.getId());

String key = "" + loginUser;
String totalPkGroup = "totalPkGroup";
String winPkGroup = "winPkGroup";

String query = "SELECT sum(win_count+win_total) FROM wgame_history0 where user_id=" + loginUser.getId();
Integer count = (Integer)OsCacheUtil.get(key, winPkGroup, 5*60);
if(count==null){
	count = new Integer(SqlUtil.getIntResult(query, Constants.DBShortName));
	OsCacheUtil.put(key, count, winPkGroup);
}
winPk = count.intValue();

query = "SELECT sum(lose_count+win_count+lose_total+win_total) FROM wgame_history0 where user_id=" + loginUser.getId();
count = (Integer)OsCacheUtil.get(key, totalPkGroup, 5*60);
if(count==null){
	count = new Integer(SqlUtil.getIntResult(query, Constants.DBShortName));
	OsCacheUtil.put(key, count, totalPkGroup);
}
totalPk = count.intValue();
//WUCX2006-10-13 判断是否好友 start
friendCount=UserInfoUtil.getUserOnlineFriendsCount(loginUser.getId());
//WUCX2006-10-13 判断是否好友 end
}

String sql = "select user_id from jc_online_user where position_id=2 limit 50";
List idList = (List)OsCacheUtil.get(sql, OsCacheUtil.ONLINE_USER_IDS_GROUP, 5*60);
if(idList==null){
	idList = SqlUtil.getIntList(sql, Constants.DBShortName);
	if(idList==null){
		idList = new ArrayList();
	}
	OsCacheUtil.put(sql, idList, OsCacheUtil.ONLINE_USER_IDS_GROUP);
}
int totalCount = idList.size();
int numberPerPage = 5;
int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if (pageIndex == -1) {
    pageIndex = 0;
}

int totalPageCount = totalCount / numberPerPage;
if (totalCount % numberPerPage != 0) {
    totalPageCount++;
}
if (totalPageCount == 0) {
    pageIndex = 0;
} else if (totalPageCount != 0 && pageIndex >= totalPageCount) {
    pageIndex = totalPageCount - 1;
}
String prefixUrl = "wagerHall.jsp?a=1";
int startIndex = Math.min(pageIndex * numberPerPage, idList.size());
int endIndex = Math.min(startIndex+numberPerPage, idList.size());
List subIdList = idList.subList(startIndex, endIndex);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="通吃岛">
<p align="left"><%
if(us!=null){ 
%><%=BaseAction.getTop(request, response)%>
小二：客官，您现有<%=us.getGamePoint()%>乐币，累计结果<%= totalPk %>场，<%= winPk %>胜，<%= totalPk - winPk %>败，胜率<%= (int)(((float)winPk)/totalPk*100) %>%.<br/>
<%
    HashMap rankMap=LoadResource.getRankMap();
    String name=null;
    RankBean rank = (RankBean) rankMap.get(new Integer(us.getRank()));
    if(loginUser.getGender()==1){name=rank.getMaleName();}
    else{name=rank.getFemaleName();}
}else{
%>小二：客官，您好，来试试手气啊~<br/>
<%}%>
<%--
<a href="/user/ViewFriends.do">好友<%=friendCount>0?String.valueOf(friendCount):""%></a>|<a href="/user/userInfo.jsp">资料</a>|<a href="/mycart.jsp">收藏</a>|<a href="/user/messageIndex.jsp">信箱</a>|<a href="/user/userBag.jsp">行囊</a><br/>
<a href="/chat/hall.jsp">聊天</a>|<a href="/friend/friendCenter.jsp">交友</a>|<a href="/home/home.jsp">家园</a>|<a href="/tong/tongList.jsp">帮会</a>|<a href="/jcforum/index.jsp">论坛</a><br/>
--%>
<a href="/wgamepk/dice/index.jsp">骰子</a>|<a href="/wgame/texas/index.jsp">德州扑克</a>|<a href="/wgamepk/football/index.jsp">射门</a><br/>
*<a href="/wgame/ssc/index.jsp">虚拟六时彩</a>*<a href="/bank/bank.jsp">银行</a>*<br/>
＝单回合PK看运气＝<br/>
<a href="/wgamepk/lgj/index.jsp">老虎杠子鸡</a>|<a href="/wgamepk/basketball/index.jsp">篮球</a>|<a href="/wgamepk/jsb/index.jsp">剪刀石头布</a><br/>
<a href="/wgame/g21/index.jsp">21点</a>|<a href="/wgamepkbig/ylhyk/index.jsp">大富豪</a>|<a href="/wgamefight/index.jsp">街霸PK场</a><br/>

<%--=BaseAction.getAdver(3,response)--%>

＝多回合PK靠实力＝<br/>
<a href="/wgamehall/gobang/index.jsp">五子棋</a>|<a href="/wgamehall/othello/index.jsp">黑白棋</a>|<a href="/wgamehall/football/index.jsp">点球</a><br/><a href="/job/handbookinger/index.jsp">乐酷马场</a>|<a href="/job/wheel/StartWheel.jsp">俄罗斯轮盘</a><br/>
!!游戏是虚拟的，反对一切利用游戏进行现金赌博的行为<br/>
!!请玩家们远离现金赌博，不要利用游戏内的胜负关系来参与一些人组织的私下交易。<br/>
<a href="/Column.do?columnId=12623">乐酷用户协议和游戏规则总则</a><br/>
＝没币了?教你一招＝<br/>
<a href="/lswjs/gameIndex.jsp">赚钱游艺</a>&nbsp;<a href="/chat/hall.jsp?roomId=<%=RoomRateAction.getRandomRoomId()%>">求助网友</a><br/>
＝在线玩家列表＝<br/>
<%
if(subIdList.size()>0){ 
	int index = 0;
    for(int i=0;i<subIdList.size();i++){
	    Integer userId = (Integer)subIdList.get(i);
	    if(userId==null)continue;
	    
	    int id = userId.intValue();
	    if(loginUser!=null && loginUser.getId()==id)continue;
	    UserBean user = UserInfoUtil.getUser(id);
	    if(user==null)continue;
	    index++;
	    
	    UserStatusBean status = UserInfoUtil.getUserStatus(id);
	    %>
	    <%= index %>.<a href="/chat/post.jsp?toUserId=<%=user.getId()%>"><%= StringUtil.toWml(user.getNickName()) %></a>(<%= (status!=null)?status.getPk():0 %>
	    <%if(user.getCityname() != null){%><%=user.getCityname()%><%}else{%>未知<%}%>)<br/> 
	    <%
    }
    %>
    <%=PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "|", response)%><br/>
    <%
}
%>
<a href="/top/index.jsp">PK王者排行榜</a><br/>
<%--=BaseAction.getAdver(4,response)--%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
<%//liuyi 2006-12-27 赌场首页修改 end%>