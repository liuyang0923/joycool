<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="net.joycool.wap.spec.garden.*,java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.action.user.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.buyfriends.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.bean.home.*,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.*,net.joycool.wap.action.home.HomeAction,net.joycool.wap.service.impl.HomeServiceImpl"%><%@ page import="net.joycool.wap.spec.friend.*"%><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
HomeServiceImpl service = new HomeServiceImpl();
String tip = "程序将要遍历jc_home_user表，将目前没有分类的日记、相册都过渡到默认分类下面(速度慢)。";
int type = action.getParameterInt("t");
HomeUserBean homeUser = null;
HomeDiaryCat diaryCat = null;
HomePhotoCat photoCat = null;
if (type == 1){
	List homeUserList = service.getHomeUserList(" 1");
	for (int i = 0 ; i < homeUserList.size() ; i++){
		homeUser = (HomeUserBean)homeUserList.get(i);
		if (homeUser != null){
			// 看是否有默认“日记”分类,没有就先创建再导入
			if (service.getHomeDiaryCat(" def=1 and uid=" + homeUser.getUserId()) == null){
				diaryCat = new HomeDiaryCat();
				diaryCat.setCatName("默认分类");
				diaryCat.setDef(1);
				diaryCat.setUid(homeUser.getUserId());
				diaryCat.setCount(0);
				service.addHomeDiaryCat(diaryCat);
			}
			SqlUtil.executeUpdate("update jc_home_diary set cat_id=(select id from jc_home_diary_cat where uid=" + homeUser.getUserId() + " and def=1) where user_id=" + homeUser.getUserId() + " and cat_id=0",0);
			SqlUtil.executeUpdate("update jc_home_diary_cat set `count`=(select count(id) from jc_home_diary where del=0 and user_id=" + homeUser.getUserId() + ") where def=1 and uid=" + homeUser.getUserId(),0);
			//SqlUtil.executeUpdate("update jc_home_user set diary_count=(select `count` from jc_home_diary_cat where def=1 and uid=" + homeUser.getUserId() + ") where user_id=" + homeUser.getUserId(),0);
			// 看是否有默认“相册”分类,没有就先创建再导入
			if (service.getHomePhotoCat(" def=1 and uid=" + homeUser.getUserId()) == null){
				photoCat = new HomePhotoCat();
				photoCat.setCatName("默认分类");
				photoCat.setDef(1);
				photoCat.setUid(homeUser.getUserId());
				photoCat.setCount(0);
				service.addHomePhotoCat(photoCat);
			}
			SqlUtil.executeUpdate("update jc_home_photo set cat_id=(select id from jc_home_photo_cat where uid=" + homeUser.getUserId() + " and def=1) where user_id=" + homeUser.getUserId() + " and cat_id=0",0);
			SqlUtil.executeUpdate("update jc_home_photo_cat set `count`=(select count(id) from jc_home_photo where user_id=" + homeUser.getUserId() + ") where def=1 and uid=" + homeUser.getUserId(),0);
			SqlUtil.executeUpdate("update jc_home_user set photo_count=(select `count` from jc_home_photo_cat where def=1 and uid=" + homeUser.getUserId() + "),diary_count=(select `count` from jc_home_diary_cat where def=1 and uid=" + homeUser.getUserId() + "),diary_cat_count=1,photo_cat_count=1 where user_id=" + homeUser.getUserId(),0);
		}
	}
	tip = "完毕";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="将无分类的日记与相册导入默认分类中">
<p>
<%=tip%><br/>
<a href="cat.jsp?t=1"><%=type==1?"再试一次":"开始" %></a>|<a href="../lswjs/index.jsp">回导航</a><br/>
</p>
</card>
</wml>