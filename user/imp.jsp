<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.friend.FriendBean,net.joycool.wap.action.friend.*,net.joycool.wap.framework.*,net.joycool.wap.util.*,net.joycool.wap.bean.*,net.joycool.wap.bean.jcforum.*,net.joycool.wap.bean.tong.*,net.joycool.wap.bean.tong.TongBean,net.joycool.wap.action.tong.TongAction,net.joycool.wap.cache.util.*"%><%@ page import="jc.credit.*,net.joycool.wap.spec.friend.*,net.joycool.wap.action.friend.*,net.joycool.wap.bean.friend.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction,net.joycool.wap.cache.util.*,net.joycool.wap.cache.*"%>
<%! static int xingge[] = {14,12,19,16,17,18};
	static int mudi[] = {1,2,3,3,4};
	static int hunyin[] = {71,72,74,73};
	static HashMap cityMap = null;
%>
<% response.setHeader("Cache-Control","no-cache");
FriendAction action = new FriendAction(request);
CreditAction action2 = new CreditAction(request);
CreditCity city = null;
byte[] lock = new byte[0];
if (cityMap == null){
	synchronized (lock){
		if (cityMap == null){
			cityMap = new HashMap();
			List list = CreditAction.service.getCityList(" 1");
			if (list != null && list.size() > 0){
				for (int i = 0 ; i < list.size() ; i++){
					city = (CreditCity)list.get(i);
					if (city != null){
						cityMap.put(city.getCity(),city);
					}
				}
			}
		}
	}
}
String tip = "";
int userCity = 0;
int userProvince = 0;
Lunar lunar = null;
UserBean user = null;
FriendBean friend = null;
UserBase userBase = null;
UserInfo userInfo = null;
Map.Entry entry = null;
String key = null;
StringBuilder sql = new StringBuilder();
UserBean loginUser = action.getLoginUser();
if (loginUser == null){
	response.sendRedirect("/user/login.jsp");
	return;
}
int uid = loginUser.getId();
	user = UserInfoUtil.getUser(uid);
	if (user == null){
		tip = "用户不存在.";
	} else {
		// 只查找userInfo是不是存在
		userInfo = CreditAction.service.getUserInfo(" user_id=" + user.getId());
		// 如果可信度部分等于0或没有可信度，但却有交友资料的，可以把数据导过来。
		if (user.getFriend() == 1 && (userInfo == null || userInfo.getTotalPoint() == 0)){
			// 不存在userInfo就建立
			userInfo = action2.getUserInfo(user.getId());
			friend = FriendAction.getFriendService().getFriend(user.getId());
			if (friend != null){
				// 生日提前判断，因为交友信息里只有生日可以不输入。比较麻烦。
				String bir = friend.getBirthday();
				String tmp[] = bir.split("-");
				int year = 0,month = 0,day = 0;
				if (tmp.length == 3){
					year = StringUtil.toInt(tmp[0]);
					month = StringUtil.toInt(tmp[1]);
					day = StringUtil.toInt(tmp[2]);
					if (year < 1000 || year > 9999 ){
						tip = "交友资料中的生日信息填写错误.";
					}
				} else {
					tip = "交友资料中的生日信息填写错误.";
				}
				if ("".equals(tip)){
					userBase = CreditAction.getUserBaseBean(friend.getUserId());
					// 导入基本信息
					if (userBase == null){
						userBase = new UserBase();
						userBase.setUserId(friend.getUserId());
						userBase.setGender(UserInfoUtil.getUser(friend.getUserId()).getGender());
						userBase.setProvince(userProvince);
						userBase.setCity(userCity);
						userBase.setAnimals("");
						CreditAction.service.createUserBase(userBase);
					}
					// 导入生日
					sql.append(",bir_year=");
					sql.append(year);
					sql.append(",bir_month=");
					sql.append(month);
					sql.append(",bir_day=");
					sql.append(day);
					// 计算星座与属性
					lunar = new Lunar(year,month,day);
					sql.append(",animals='");
					sql.append(lunar.animalsYear());
					sql.append("'");
					sql.append(",astro=");
					sql.append(CreditAction.getAstroIdByDate(month,day));
					// 身高
					sql.append(",stature=");
					sql.append(friend.getHeight());
					// 性格
					sql.append(",personality=");
					sql.append(xingge[friend.getPersonality()]);
					// 交友目的
					sql.append(",aim=");
					sql.append(mudi[friend.getAim()]);
					SqlUtil.executeUpdate("update credit_user_base set " + sql.substring(1,sql.length()) + " where user_id=" + friend.getUserId(),5);
					// 清除缓存
					CreditAction.userBaseCache.clear();
					// 真实姓名
					if (!"".equals(friend.getName())){
						if (SqlUtil.getIntResult("select user_id from credit_user_contacts where user_id=" + friend.getUserId(),5) > 0){
							SqlUtil.executeUpdate("update credit_user_contacts set true_name='" + StringUtil.toSql(friend.getName()) + "',contacts='" + friend.getMarriage() + "' where user_id=" + friend.getUserId(),5);
						} else {
							SqlUtil.executeUpdate("insert into credit_user_contacts (user_id,true_name,contacts) values (" + friend.getUserId() + ",'" + StringUtil.toSql(friend.getName()) + "','" + friend.getMobile() + "')",5);
						}
					}
					// 职业
					if (SqlUtil.getIntResult("select user_id from credit_user_work where user_id=" + friend.getUserId(),5) > 0){
						SqlUtil.executeUpdate("update credit_user_work set trade=34 where user_id=" + friend.getUserId(),5);
					} else {
						SqlUtil.executeUpdate("insert into credit_user_work (user_id,trade) values (" + friend.getUserId() + ",34)",5);
					}
					// 婚姻
					if (SqlUtil.getIntResult("select user_id from credit_user_live where user_id=" + friend.getUserId(),5) > 0){
						SqlUtil.executeUpdate("update credit_user_live set marrage=" + hunyin[friend.getMarriage()] + " where user_id=" + friend.getUserId(),5);
					} else {
						SqlUtil.executeUpdate("insert into credit_user_live (user_id,marrage) values (" + friend.getUserId() + "," + hunyin[friend.getMarriage()] + ")",5);
					}
					// 插入地区
					if (cityMap != null){
						Iterator iter = cityMap.entrySet().iterator();
						while (iter.hasNext()){
							entry = (Map.Entry) iter.next(); 
							key = (String)entry.getKey();
							city = (CreditCity)cityMap.get(key);
							if (city != null && city.getCity().equals(friend.getCity())){
								userCity = city.getId();
								userProvince = city.getHypo();
								break;
							}
						}
					}
					if (userCity > 0 && userProvince > 0){
						SqlUtil.executeUpdate("update credit_user_base set city=" + userCity + ",province=" + userProvince + " where user_id=" + user.getId(),5);
					}
					tip = "导入完成.";
				}
			} else {
				tip  = "没有交友信息.";
			}
		} else if (user.getFriend() == 0){
			tip = "您没有填写交友信息.";
		} else if (userInfo != null && userInfo.getTotalPoint() > 0){
			tip = "您已经填写可信度了.";
		}
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="资料导入">
<p>
<%=BaseAction.getTop(request, response)%>
<%=tip%><br/>
<a href="/user/ViewUserInfo.do?userId=<%=uid%>">返回个人资料</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>