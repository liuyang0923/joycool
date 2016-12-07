package jc.guest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jc.guest.battle.GamepageAction;
import jc.guest.battle.Topbean;

import net.joycool.wap.action.jcforum.ForumxAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.cache.StaticCacheMap;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.CookieUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.encoder.Base64x;

public class GuestHallAction extends CustomAction{
	
	public static String GUEST_KEY = "guest";
	
	// 最大游币
	public static int MAX_MONEY = 2100000000;
	
	public static GuestHallService service = new GuestHallService();
	
	public static ICacheMap onlineGuestCache = CacheManage.addCache(new StaticCacheMap(4000), "guestCache");
	
	// 每级所需要的经验值
	public static int point[] = {0,21,56,141,336,725,1416,2541,4256,6741,10200,14861,20976,28821,38696,50925,65856,83861,105336,130701,160400};
	
	// 称号名称
	public static String title[] = {"","呱呱落地","安全第一","常胜将军","社交达人","小财主","财神爷","金榜题名","国士无双","天下第一","富甲天下","乐酷居民","一贫如洗"};
	
	// 称号描述
	public static String titleDesc[] = {"",
		"设置昵称就可以在游客大厅聊天,并获得称号\"呱呱落地\"!",
		"设置密码就可以确保再次登录的成功,并获得称号\"安全第一\"!",
		"一天内,如果你的昵称在姓名大作战中战胜10次,就可以获得称号\"常胜将军\"!",
		"如果你的关注列表中人数超过50,就可以获得称号\"社交达人\".",
		"游币超过8888,就考虑下册封你为\"小财主\"!",
		"游币超过888888,就可以获得称号\"财神爷\"!",
		"在任意排行榜进入前30,就可以获得称号\"金榜题名\".",
		"进入经验和财富榜前30的可以获得称号\"国士无双\".",
		"经验榜第一可以获得称号\"天下第一\".",
		"冲上财富榜第一,还可以获得称号\"富甲天下\"噢!",
		"如果你注册乐酷正式用户,就可以获得称号\"乐酷居民\"!",
		"游币没有了,也会获得称号哦！就叫做\"一贫如洗\"."};
	
	public GuestHallAction(){
		
	}
	
	public GuestHallAction(HttpServletRequest request){
		super(request);
	}
	
	public GuestHallAction(HttpServletRequest request,HttpServletResponse response){
		super(request,response);
	}
	
	/**
	 * 从session中取得游客账户.
	 * @return
	 */
	public static GuestUserInfo getGuestUserSe(HttpServletRequest request){
		HttpSession session = request.getSession();
		GuestUserInfo user = (GuestUserInfo)session.getAttribute(GUEST_KEY);
		return user;
	}
	
	public static ICacheMap guestUserCache = CacheManage.addCache(new LinkedCacheMap(2000, true), "guestUser");
	
	/**
	 * 按UID从缓存中取得一位游客用户.
	 * @param uid
	 * @return
	 */
	public static GuestUserInfo getGuestUser(int uid){
		GuestUserInfo guestUser = null;
		synchronized(guestUserCache) {
			guestUser = (GuestUserInfo)guestUserCache.get(new Integer(uid));
			if(guestUser == null) {
				guestUser = service.getUserInfo(" id=" + uid);
				if(guestUser != null) {
					guestUserCache.put(new Integer(uid), guestUser);
				}
			}
		}
		return guestUser;
	}
	
	/**
	 * 按昵称来取得一位游客用户(可以通过昵称或手机号来登陆).
	 * @param nick
	 * @return
	 */
	public GuestUserInfo getGuestUser(String login,String pw){
		GuestUserInfo user = null;
		if (login == null || pw == null)
			return null;
		// 先按昵称查找
		user = service.getUserInfo(" user_name='" + StringUtil.toSql(login) + "' and `password`='" + StringUtil.toSql(pw) + "'");
		// 再按手机号查找
		if (user == null)
			user = service.getUserInfo(" mobile='" + StringUtil.toSql(login) + "' and `password`='" + StringUtil.toSql(pw) + "'");
		return user;
	}

	/**
	 * 从cookie中取得一位游客用户.
	 * @return
	 */
	public GuestUserInfo getGuestUserCk(){
		CookieUtil ck = new CookieUtil(request, response);
		int uid = 0;
		GuestUserInfo autoUser = null;
		String jcgu = ck.getCookieValue("jcgu");
		if (jcgu != null && jcgu.length() == 16) {	// 长度不对就不要了
			String duid = jcgu.substring(5, 10);
			uid = Base64x.decodeInt(duid);
			autoUser = getGuestUser(uid);
			if (autoUser != null) {
				String pwdcheck = jcgu.substring(0, 5) + jcgu.substring(10, 16);
				String pwdcheck2 = Base64x.encodeMd5(autoUser.getPassword());
				if (pwdcheck2.substring(0, 11).equals(pwdcheck)) { // 密码校验成功
					return autoUser;
				}
			}
		}
		return null;
	}
	
	/**
	 * 此方法先在session中寻找游客用户,再从缓存中查找.
	 * @return
	 */
	public GuestUserInfo getGuestUser(){
		GuestUserInfo user = getGuestUserSe(request);
		if (user != null){
			user = getGuestUser(user.getId());
		}
		return user;
	}
	
	/**
	 * 只根据昵称,在数据库里创建user.密码用随机的5位数,同时flag=0.正确返回0.错误返回编号,用于back.jsp.
	 * @param nickName
	 * @return
	 */
	public int addUser(String nickName){
		GuestUserInfo user = null;
		if (nickName == null || nickName.length() < 2 || nickName.length() > 10){
			return 1;	// 昵称输入错误
		}
		user = service.getUserInfo(" user_name='" + StringUtil.toSql(nickName) + "'");
		if (user != null){
			return 2;	// 昵称已被占用
		}
		// 分配随机密码，创建用户。
		String password = "" + RandomUtil.nextInt(10000,99999);
		user = new GuestUserInfo();
		user.setUserName(nickName);
		user.setPassword(password);
		user.setAge(0);
		user.setGender(2);
		user.setMobile("");
		user.setFlag(0);
		user.setPoint(0);
		user.setMoney(0);
		user.setMyTitle("");
		UserBean loginUser = this.getLoginUser();
		if (loginUser == null){
			user.setBuid(0);
			user.setFocus(0);	// 未登陆用户，默认为允许随便添加关注
		} else {
			user.setFocus(2);	// 已登陆用户，默认为需要身份验证
			user.setBuid(loginUser.getId());
		}
		user.setLevel(1);	// 从一级开始~
		int lastInsertId = service.addUser(user);
		user.setId(lastInsertId);

		// 把用户bean放入session中
		session.setAttribute(GUEST_KEY, user);
		// 再放入cookie中
		saveToCookie(user);
		return 0;
	}
	
	/**
	 * 把游客信息写入到cookie中.
	 * @param user
	 * @return
	 */
	public boolean saveToCookie(GuestUserInfo user){
		if (user == null){
			return false;
		}
		if (request.isRequestedSessionIdFromCookie()){
			String password = Base64x.encodeMd5(user.getPassword());
			String id = Base64x.encodeInt(user.getId());
			String data = password.substring(0, 5) + id + password.substring(5, 11);
			Cookie cookie = new Cookie("jcgu", data);	// 加cookie
			cookie.setMaxAge(90000000);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return true;
	}
	
	/**
	 * 删除cookie.
	 * @param name
	 */
	public void removeCookie(String name){
		if (name == null){
			return;
		}
		CookieUtil cookie = CookieUtil.getInstance(request, response);
		if (cookie != null){
			cookie.removeCookie(name);
		}
	}
	
	/**
	 * 添加关注.<br/>
	 * 说明:<br/>
	 * guestUser要将focusUid添加关注.<br/>
	 * 如果被添加方需要验证,会直接发出验证信息.
	 * @param guestUser
	 * @param focusUid
	 * @return
	 */
	public int addFocus(GuestUserInfo guestUser,int focusUid){
		if (guestUser == null || focusUid <= 0){
			// 输入错误
			return 8;
		}
		if (focusUid == guestUser.getId()){
			// 不可添加自己
			return 12;
		}
		GuestUserInfo focusUser = getGuestUser(focusUid);
		if (focusUser == null){
			// 游客不存在
			return 8;
		} else if (service.getUserFocus("left_uid=" + guestUser.getId() + " and right_uid=" + focusUid) != null){
			// 添加过此用户了
			return 11;
		} else if (focusUser.getFocus() == 1){
			// 设置为了拒绝任何人关注
			return 10;
		} else if (focusUser.getFocus() == 2){
			// 需要身份验证
			sendMsg(guestUser.getId(),focusUid,guestUser.getUserName() + "申请将您加为关注.");
			return 9;
		}
		UserFocus focus = new UserFocus(guestUser.getId(),focusUser.getId());
		service.addFocus(focus);
		return 7;
	}
	
	/**
	 * 修改密码.
	 * @param guestUser
	 * @param pw1
	 * @param pw2
	 * @return
	 */
	public int changePw(GuestUserInfo guestUser,String pw1,String pw2){
		if (guestUser == null || pw1 == null || pw2 == null){
			return 15;
		}
		if (!guestUser.getPassword().equals(pw1)){
			return 14;
		}
		if (pw2.length() < 4 || pw2.length() > 10){
			return 15;
		}
	    Pattern pattern = Pattern.compile("[0-9a-zA-Z]*");	// 只匹配字符和数字
	    Matcher match = pattern.matcher(pw2);
	    if (!match.matches()){
	    	return 15;
	    }
	    SqlUtil.executeUpdate("update guest_user_info set `password`='" + pw2 + "' where id=" + guestUser.getId(), 6);
		// 重写cookie和session中的bean
	    guestUser.setPassword(pw2);
		// 重写cookie
	    saveToCookie(guestUser);
		// 重放入session
		session.setAttribute(GuestHallAction.GUEST_KEY,guestUser);
	    return 13;
	}
	
	/**
	 * 查看有没有发给uid的关注验证信息.
	 * @param uid
	 * @return
	 */
	public String getMsg(int uid){
		if (uid <= 0){
			return "";
		}
		FocusMsg msg = service.getFocusMsg(" right_uid=" + uid + " and readed=0 order by id asc limit 1");
		if (msg == null){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"conf.jsp\">");
		sb.append(msg.getContentWml());
		sb.append("</a><br/>");
		return sb.toString();
	}
	
	/**
	 * 发送加关注的验证消息.leftUid发送给rightUid.
	 * @param leftUid
	 * @param rightUid
	 * @param content
	 * @return
	 */
	public void sendMsg(int leftUid,int rightUid,String content){
		// 2个uid错误或2个uid相等，不发验证消息
		if (leftUid <= 0 || rightUid <= 0 || leftUid == rightUid || content == null){
			return;
		}
		// 已经加了关注，不发验证消息
		if (service.getUserFocus(" left_uid=" + leftUid + " and right_uid=" + rightUid) != null){
			return;
		}
		// 已经发过消息的，不能再发
		if (service.getFocusMsg(" left_Uid=" + leftUid + " and right_uid=" + rightUid) != null){
			return;
		}
		FocusMsg msg = new FocusMsg(leftUid,rightUid,content);
		service.addFocusMsg(msg);
	}
	
	/**
	 * 返回在线列表和离线列表.<br/>
	 * 请从request中取得"online"和"offline"这2个集合.
	 * @param guestUid
	 */
	public void getOnlineAndOffLineList(int guestUid){
		if (guestUid <= 0){
			return;
		}
		List list = GuestHallAction.service.getUserFocusList("left_uid=" + guestUid);
		if (list == null || list.size() <= 0){
			return;
		}
		List online = new ArrayList();
		List offline = new ArrayList();
		UserFocus userFocus = null;
		OnlineGuest onlineGuest = null;
		for (int i = 0 ; i < list.size() ; i++){
			userFocus = (UserFocus)list.get(i);
			if (userFocus != null){
				onlineGuest = (OnlineGuest)onlineGuestCache.sgt(new Integer(userFocus.getRightUid()));
				if (onlineGuest == null){
					// 离线
					offline.add(userFocus);
				} else {
					if (onlineGuest.isOnline()){
						// 在线
						online.add(userFocus);
					} else {
						// 虽然在在线列表中，可超过了5分钟没有任何操作，强制设为离线
						onlineGuestCache.srm(userFocus.getRightUid());
						offline.add(userFocus);
					}
				}
			}
		}
		request.setAttribute("online", online);
		request.setAttribute("offline", offline);
	}
	
	/**
	 * 操作用户游币.<br/>
	 * 说明:<br/>
	 * money为负,表示扣除.21亿封顶.<br/>
	 * @param uid
	 * @param money
	 * @return
	 */
	public static boolean updateMoney(int uid,int money){
		if (uid <= 0 || money == 0){
			return false;
		}
		GuestUserInfo guestUser = getGuestUser(uid);
		if (guestUser != null){
			// 改缓存
			if (money > 0){
				// 如果要加上money后超过21亿了，则只加到21亿
				if ((MAX_MONEY - guestUser.getMoney()) < money){
					guestUser.setMoney(MAX_MONEY);
				} else {
					guestUser.setMoney(guestUser.getMoney() + money);
				}
			} else {
				money = Math.abs(money);
				if (guestUser.getMoney() - money < 0){
					guestUser.setMoney(0);
				} else {
					guestUser.setMoney(guestUser.getMoney() -  money);
				}
			}
			// 改数据库
			SqlUtil.executeUpdate("update guest_user_info set money=" + guestUser.getMoney() + " where id=" + uid, 6);
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据经验值,计算某用户的实际等级.
	 * @param guestUser
	 * @return
	 */
	public static int calcLevel(GuestUserInfo guestUser){
		if (guestUser == null){
			return 0;
		}
		for (int i = point.length - 1 ; i >= 0 ; i--){
			if (guestUser.getPoint() >= point[i]){
				return i + 1;
			}
		}
		return 0;
	}
	
	/**
	 * 取得奖励.<br/>
	 * 说明:<br/>
	 * 未注册乐酷正式用户以100为基数加上等级附加的游币,乐库正式用户以300为基数加上等级附加的游币.
	 * @param guestUser
	 * @return
	 */
	public int getAward(GuestUserInfo guestUser){
		if (guestUser == null){
			return 0;
		}
		if (guestUser.getBuid() == 0){
//			if (guestUser.getLevel() == 0){
//				return 100;
//			} else {
				return (guestUser.getLevel() - 1) * 50 + 100;
//			}
		} else {
//			if (guestUser.getLevel() == 0){
//				return 300;
//			} else {
				return (guestUser.getLevel() - 1) * 50 + 300;
//			}
		}
	}
	
	/**
	 * 将今天的日期变成数字.比如,08月26日,就变成数字:826,09月01日变成数字901.据此来判断今天有没有领过奖励.
	 * @return
	 */
	public int getTodayInt(){
		java.util.Calendar c = java.util.Calendar.getInstance();
		return (c.get(java.util.Calendar.MONTH) + 1) * 100 + c.get(java.util.Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 取得今天的奖励.即使多次使用此方法,一天之内也<b>只能获得一次</b>奖励,<b>不会</b>重复获得的.
	 * @param guestUser
	 */
	public void getAwardToday(GuestUserInfo guestUser){
		if (guestUser == null){
			return;
		}
		int today = getTodayInt();
		if (guestUser.getAward() != today){
			updateMoney(guestUser.getId(),getAward(guestUser));
			guestUser.setAward(today);
			SqlUtil.executeUpdate("update guest_user_info set award=" + today + " where id=" + guestUser.getId(), 6);
		}
	}
	
	/**
	 * 看用户是否已经含有了某个称号.
	 * @param guestUser
	 * @param titleId
	 * @return
	 */
	public boolean isHaveTitle(GuestUserInfo guestUser,int titleId){
		if (guestUser == null || titleId < 1 || titleId >= title.length ){
			return false;
		}
		String tmp[] = guestUser.getMyTitle().split(",");
		// 查找一下用户是不是已经获得过这个称号.由于一共才有12个称号,这里就用最简单但效率最低的循环来判断.
		for (int i = 0 ; i < tmp.length ; i++){
			if (tmp[i].equals(String.valueOf(titleId))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 添加称号.
	 * @param guestUser
	 * @param titleId
	 */
	public void addTitle(GuestUserInfo guestUser,int titleId){
		if (!isHaveTitle(guestUser,titleId)){
			guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
			SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
		}
	}
	
	/**
	 * 设置新的称号.并且判断是否达到了称号的要求.
	 * @param guestUser
	 * @param titleId
	 * @return
	 */
	public boolean addTitle2(GuestUserInfo guestUser,int titleId){
		if (guestUser == null){
			return false;
		}
		List list = null;
		GuestUserInfo tmpUser = null;
		switch (titleId){
		case 1:		// 呱呱落地
					// 设置昵称就可以在游客大厅聊天,并获得称号\"呱呱落地\"!
			if (guestUser.getUserName() != null && !"".equals(guestUser.getUserName())){
				guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
				SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
				return true;
			}
			break;
		case 2:		// 安全第一
					// 设置密码就可以确保再次登录的成功,并获得称号\"安全第一\"!
			if (guestUser.getFlag() > 0 && guestUser.getPassword() != null){
				guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
				SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
				return true;
			}
			break;
		case 3:		// 常胜将军
					// 一天内,如果你的昵称在姓名大作战中战胜10次,就可以获得称号\"常胜将军\"!
			Entry entry = null;
			Topbean topb = null;
			HashMap map = GamepageAction.top;
			Iterator iter = map.entrySet().iterator(); 
			while (iter.hasNext()){
				entry = (Entry) iter.next();
				topb = (Topbean)entry.getValue();
				// topb.getWinname()取得的是一个list,此list的第一个元素是赢的人的名子.因为输了的人和赢的人用了同一个bean,所以这里topb.getWinname()一定要存成list.
				if (topb != null && topb.getWinname().get(0).equals(guestUser.getUserName()) && topb.getTimes() >= 10){
					guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
					SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
					return true;
				}
			}
			break;
		case 4:		// 社交达人
					// 如果你的关注列表中人数超过50,就可以获得称号\"社交达人\".
			list = GuestHallAction.service.getUserFocusList("left_uid=" + guestUser.getId());
			if (list != null && list.size() > 50){
				guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
				SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
				return true;
			}
			break;
		case 5:		// 小财主
					// 游币超过8888,就考虑下册封你为\"小财主\"!
			if (guestUser.getMoney() > 8888){
				guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
				SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
				return true;
			}
			break;
		case 6:		// 财神爷
					// 游币超过888888,就可以获得称号\"财神爷\"!
			if (guestUser.getMoney() > 888888){
				guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
				SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
				return true;
			}
			break;
		case 7:		// 金榜题名
					// 在任意排行榜进入前30,就可以获得称号\"金榜题名\".
					// 经验排行榜前30
					list = service.getUserList(" 1 order by `point` desc,id desc limit 30");
					for(int i = 0 ; i < list.size() ; i++){
						tmpUser = (GuestUserInfo)list.get(i);
						if (tmpUser != null && guestUser.getId() == tmpUser.getId()){
							guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
							SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
							return true;
						}
					}
					// 财富排行榜前30
					list = service.getUserList(" 1 order by money desc,id desc limit 30");
					for(int i = 0 ; i < list.size() ; i++){
						tmpUser = (GuestUserInfo)list.get(i);
						if (tmpUser != null && guestUser.getId() == tmpUser.getId()){
							guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
							SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
							return true;
						}
					}
			break;
		case 8:		// 国士无双
					// 进入经验和财富榜前30的可以获得称号\"国士无双\".
					// 经验排行榜前30
					boolean flag = false;
					list = service.getUserList(" 1 order by `point` desc,id desc limit 30");
					for(int i = 0 ; i < list.size() ; i++){
						tmpUser = (GuestUserInfo)list.get(i);
						if (tmpUser != null && guestUser.getId() == tmpUser.getId()){
							flag = true;
							break;
						}
					}
					// 财富排行榜前30
					if (flag){
						// 如果经验排名都没进前30的话,财富榜就不要查了
						list = service.getUserList(" 1 order by money desc,id desc limit 30");
						for(int i = 0 ; i < list.size() ; i++){
							tmpUser = (GuestUserInfo)list.get(i);
							if (tmpUser != null && guestUser.getId() == tmpUser.getId()){
								guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
								SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
								return true;
							}
						}
					}
			break;
		case 9:		// 天下第一
					// 经验榜第一可以获得称号\"天下第一\".
			list = service.getUserList(" 1 order by `point` desc,id desc limit 1");
			if (list != null){
				tmpUser = (GuestUserInfo)list.get(0);
				if (tmpUser != null && tmpUser.getId() == guestUser.getId()){
					guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
					SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
					return true;
				}
			}
			break;
		case 10:	// 富甲天下
					// 冲上财富榜第一,还可以获得称号\"富甲天下\"噢!
			list = service.getUserList(" 1 order by money desc,id desc limit 1");
			if (list != null){
				tmpUser = (GuestUserInfo)list.get(0);
				if (tmpUser != null && tmpUser.getId() == guestUser.getId()){
					guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
					SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
					return true;
				}
			}
			break;
		case 11:	// 乐酷居民
					// 如果你注册乐酷正式用户,就可以获得称号\"乐酷居民\"!
			if (guestUser.getBuid() > 0){
				guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
				SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
				return true;
			}
			break;
		case 12:	// 一贫如洗
					// 游币没有了,也会获得称号哦！就叫做\"一贫如洗\".
			if (guestUser.getMoney() < 30){
				guestUser.setMyTitle(guestUser.getMyTitle() + titleId + ",");
				SqlUtil.executeUpdate("update guest_user_info set my_title='" + guestUser.getMyTitle() + "' where id=" + guestUser.getId(), 6);
				return true;
			}
			break;
		}
		return false;
	}
	
	/**
	 * 返回我的称号.
	 * @param guestUser
	 * @param link    单击某一称号后要跳转到的地址.
	 * @param isMyself
	 * @return
	 */
	public String getTitleStr(GuestUserInfo guestUser,String link,boolean isMyself){
		if (guestUser == null || link == null || "".equals(link)){
			return "暂无称号";
		}
		int tmpId = 0;
		StringBuilder sb = new StringBuilder();
		String tmp[] = guestUser.getMyTitle().split(",");
		if (isMyself){
			// 自己查看
			for (int i = 0 ; i < tmp.length ; i++){
				tmpId = StringUtil.toInt(tmp[i]);
				
				if (tmpId > 0 && tmpId < title.length){
					if (tmpId != guestUser.getTitleNow()){
						sb.append("<a href=\"");
						sb.append(link);
						sb.append("?t=3&amp;ch=");
						sb.append(tmpId);
						sb.append("\">");
						sb.append(title[tmpId]);
						sb.append("</a>,");
					} else {
						sb.append(title[tmpId]);
						sb.append(",");
					}
				}
			}
		} else {
			// 别人查看
			for (int i = 0 ; i < tmp.length ; i++){
				tmpId = StringUtil.toInt(tmp[i]);
				if (tmpId > 0 && tmpId < title.length){
					sb.append(title[tmpId]);
					sb.append(",");
				}
			}
		}
		if (sb.length() > 0){
			return sb.substring(0, sb.length() - 1);
		} else {
			return "暂无称号";
		}
	}
	
	/**
	 * 给用户添加经验:<br/>
	 * 最大经验值为最高等级的经验值.<br/>
	 * 如果用户的原经验值+要加入的经验值>最高经验值,则把该用户的经验设为最高经验值.<br/>
	 * @param guestUser
	 * @param point
	 * @return
	 */
	public static boolean addPoint(GuestUserInfo guestUser,int add){
		if (guestUser == null || add <= 0){
			return false;
		}
		// 目前可以加入的最大经验值
		int allowPoint = point[point.length-1] - guestUser.getPoint();
		if (allowPoint >= add){
			guestUser.setPoint(guestUser.getPoint() + add);
		} else {
			guestUser.setPoint(point[point.length-1]);
		}
		guestUser.setLevel(calcLevel(guestUser));	// 计算正确的等级
		SqlUtil.executeUpdate("update guest_user_info set `point`=" + guestUser.getPoint() + ",level=" + guestUser.getLevel() + " where id=" + guestUser.getId(), 6);
		return true;
	}
	
	/**
	 * 返回某一用户还没有得到的称号的ID数组.<br/>
	 * 如果失败,将所有称号全部返回.如果获得了所有称号,返回元素个数为0的int数组.<br/><br/>
	 * 返回的数组中,元素为1,表示已经得到了此称号,0表示还没得到.注意遍历时要从1开始.
	 * @param guestUser
	 * @return
	 */
	public int[] getMyTitle(GuestUserInfo guestUser){
		int[] tmp = new int[title.length];		// 所有的称号
		if (guestUser == null){
			return tmp;
		}
		int titleId = 0;
		String[] tmp2 = guestUser.getMyTitle().split(",");	// 我所获得的称号
		if (tmp2.length == tmp.length -1){
			// 获得了所有的称号
			return new int[0];
		}
		for (int i = 0 ; i < tmp2.length ; i++){
			titleId = StringUtil.toInt(tmp2[i]);
			if (titleId > 0){
				tmp[titleId] = 1;
			}
		}
		return tmp;
	}
	
	/**
	 * 取得count条type类型的新闻.
	 * @param type
	 * @param count
	 * @return
	 */
	public String getNews(int type,int count){
		StringBuilder sb = new StringBuilder();
		List list = ForumxAction.getRandomLatestNews(type,count);
		ForumContentBean con = null;
		for (int i = 0 ; i < list.size() ; i++){
			con = ForumCacheUtil.getForumContent(((Integer)list.get(i)).intValue());
			if (con != null){
				sb.append("<a href=\"/news/news.jsp?id=");
				sb.append(con.getId());
				sb.append("&amp;tid=");
				sb.append(type);
				sb.append("\">");
				sb.append(StringUtil.toWml(StringUtil.limitString(con.getTitle(),24)));
				sb.append("</a><br/>");
			}
		}
		return sb.toString();
	}
}
