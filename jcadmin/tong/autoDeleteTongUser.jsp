<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.util.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*,net.joycool.wap.action.jcadmin.*"%><%@ page import="net.joycool.wap.bean.tong.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%
//如果3天不登录，而且等级小于2，就自动从帮会里踢出
String sql = "select a.user_id from jc_tong_user a left "
	+ " join user_status b " 
	+ " on a.user_id=b.user_id "
	+ " where a.mark=0 "
	+ " and to_days(now())-to_days(b.last_login_time)>5 "
	+ " and b.rank<2";
List userIdList = SqlUtil.getIntList(sql, Constants.DBShortName);
if(userIdList!=null){
	ITongService tongService = ServiceFactory.createTongService(); 
	
	for(int i=0;i<userIdList.size();i++){
		Integer id = (Integer)userIdList.get(i);
		if(id==null)continue;
		
		int userId = id.intValue();
		UserStatusBean status = UserInfoUtil.getUserStatus(userId);
		if(status==null)continue;
		
		int tongId = status.getTong();
		TongCacheUtil.updateTong("user_count=user_count-1", "id="
				+ tongId, tongId);
	    TongCacheUtil.deleteTongUser("user_id=" + userId, userId, tongId);// 删除帮会记录
	    tongService.updateTongApply("mark=4",
			    "mark=1 and user_id=" + userId);// 帮会申请表更改状态标志
	    UserInfoUtil.updateUserStatus("tong=0,game_point=game_point-100000",
			    "user_id=" + userId, userId,
			    UserCashAction.OTHERS, "马甲退出帮会,扣除100000乐币");
	}
}
%>