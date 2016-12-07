/*
 * Created on 2005-5-25
 *
 */
package net.joycool.wap.util;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.AreaBean;
import net.joycool.wap.bean.MessageBean;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class Util {
	public static IUserService userService;

	public static String flag = "five";

	public static int getDayLoginUser(HttpServletRequest request, UserBean user) {
		int count = getUserService().getDaysLoginUserCount(
				" user_id=" + user.getId());
		return count;
	}

	/**
	 * 取得浏览的手机的号码。
	 * 
	 * @param request
	 * @return
	 */
	public static String getPhoneNumber(HttpServletRequest request) {
		String phoneNumber = null;
		//phoneNumber = request.getHeader("X-Up-Calling-Line-ID");
//		if (null != phoneNumber) {
//			if (phoneNumber.startsWith("86")) {
//				phoneNumber = phoneNumber.substring(2);
//			}
//		} else {
			phoneNumber = (String) request.getSession().getAttribute(
					"userMobile");
//		}
		// if(phoneNumber == null){
		// phoneNumber = "00000000000";
		// }
		return phoneNumber;
	}

	/**
	 * 更新用户手机号。
	 * 
	 * @param request
	 * @param phoneNumber
	 */
	public static void updatePhoneNumber(HttpServletRequest request,
			String phoneNumber) {
		HttpSession session = request.getSession();
		String mobile = (String) session.getAttribute("userMobile");
		if (mobile == null) {
			mobile = phoneNumber;
			if (mobile == null) {
				return;
			} else {
				session.setAttribute("userMobile", mobile);
			}
		}
		//String mid = (String)session.getAttribute(Constants.JC_MID);
		UserBean user = (UserBean) session.getAttribute("loginUser");
		// 已经登录
		if (user != null) {
			// 更新手机号
//			if (user.getMobile() == null || (!user.getMobile().startsWith("1"))) {
//				UserInfoUtil.updateUser("mobile = '" + mobile + "'", "id = "
//						+ user.getId(), user.getId() + "");
//				user.setMobile(phoneNumber);
//			}
			// 更新地区
			if (user.getCityname() == null) {
				AreaBean area = AreaUtil.getAreaByMobile(mobile);
				if (area != null) {
					UserInfoUtil.updateUser("placeno = " + area.getPlaceno()
							+ ", cityno = " + area.getCityno()
							+ ", cityname = '" + area.getCityname() + "'",
							"id = " + user.getId(), user.getId() + "");
					user.setPlaceno(area.getPlaceno());
					user.setCityname(area.getCityname());
					user.setCityno(area.getCityno());
				}
			}
		}
		// 还没有登录
		else {
			// macq_2007-3-22_更改通过手机号获取最后一次登录用户信息_end
			// user = getUserService().getUser("mobile = '" + mobile + "'");
			user = getLastLoginTimeByUserBean(mobile);
			// macq_2007-3-22_更改通过手机号获取最后一次登录用户信息_start
			// 如果还没有注册，自动帮他注册
			if (user == null) {
				user = autoRegister(mobile);
				if(user == null)
					return;
				user.setLastVisitPage(request.getRequestURI() + "?"
						+ request.getQueryString());
				user.setIpAddress(request.getRemoteAddr());
				
				//getUserService().updateMid(user, mid);
				// mcq_2006_8_31_登录用户五步走的开关(第零\一\二步)_start
				if (Util.getFlag().equals("zero")
						|| Util.getFlag().equals("first")
						|| (Util.getFlag().equals("second") && Util
								.getDayLoginUser(request, user) == 0)
						|| Util.getFlag().equals("five")) {
					// 自动登陆
					JoycoolSessionListener.updateOnlineUser(request, user);
				}
			}
			// 已经注册，自动登录
			else {
				user.setIpAddress(request.getRemoteAddr());
				user.setUserAgent(request.getHeader("User-Agent"));
				
				//getUserService().updateMid(user, mid);
				// mcq_2006_8_31_登录用户五步走的开关(第零\一\二步)_start
				if (Util.getFlag().equals("zero")
						|| Util.getFlag().equals("first")
						|| (Util.getFlag().equals("second") && Util
								.getDayLoginUser(request, user) == 0)
						|| Util.getFlag().equals("five")) {
					// 自动登陆
					JoycoolSessionListener.updateOnlineUser(request, user);
				}
				// mcq_2006_8_31_登录用户五步走的开关(第零\一\二步)_end
				// session.removeAttribute(Constants.NOT_REGISTER_KEY);
			}
		}
	}
	public static void msgRegister(String mobile, String password) {	// 短信注册+修改密码
		if(mobile == null)
			return;
		
		if(mobile.startsWith("86"))
			mobile = mobile.substring(2);
		
		if(mobile.length() != 11 || !mobile.startsWith("1"))
			return;
		
		try{
			String sql = "select a.id from user_info a,user_status b where a.mobile='" + mobile + "' and b.user_id=a.id order by b.last_login_time desc limit 1";
			List userIdList = SqlUtil.getIntList(sql, Constants.DBShortName);
			if (userIdList == null) {
				return;		// 数据库异常了，不发送短信也不注册
			}
			if(userIdList.size() == 0) {
				preRegister(mobile, password);
			} else {
				SmsUtil.changePassword(mobile, password);
			}
		} catch(Exception e) {}
	}
	public static void msgRegister(String mobile) {	// 短信注册
		if(mobile == null)
			return;
		
		if(mobile.startsWith("86"))
			mobile = mobile.substring(2);
		
		if(mobile.length() != 11 || !mobile.startsWith("1"))
			return;
		
		try{
			boolean res = sendAccount(mobile);	// 如果已经有注册了帐户，则返回true
			if(!res) {
				UserBean user = preRegister(mobile);
				if(user != null) {
					String content = "欢迎来到乐酷http://m.joycool.net.您的初始密码为"
						+ user.getPassword() 
						+ ",用本手机号和此密码就能登陆.期待您的光临!";
					SmsUtil.send(SmsUtil.CODE, content, mobile, SmsUtil.TYPE_SMS);
				}
			}
		} catch(Exception e) {}
	}
	// A -> B
	public static void msgRegister2(String mobile, String password) {	// 短信注册
		if(mobile == null)
			return;
		
		if(mobile.startsWith("86"))
			mobile = mobile.substring(2);
		
		if(mobile.length() != 11 || !mobile.startsWith("1"))
			return;
		
		try{
			boolean res = sendAccount(mobile);	// 如果已经有注册了帐户，则返回true
			if(!res) {
//				UserBean user = preRegister(mobile);
//				if(user != null) {
					String content = "乐酷欢迎您!网址http://m.joycool.net.您的密码为"
						+ password 
						+ ",用本手机号和此密码就能登陆.期待您的光临!";
					SmsUtil.send(SmsUtil.CODE, content, mobile, SmsUtil.TYPE_SMS);
//				}
			}
		} catch(Exception e) {}
	}
	public static boolean sendAccountSms = false;
	public static boolean sendAccount(String mobile) {	//	发送用户帐户信息，如果一个都不存在则返回false
		String sql = "select a.id from user_info a,user_status b where a.mobile='" + mobile + "' and b.user_id=a.id order by b.last_login_time desc limit 1";
		List userIdList = SqlUtil.getIntList(sql, Constants.DBShortName);
		if (userIdList == null) {
			return true;		// 数据库异常了，不发送短信也不注册
		}

		if (userIdList.size() == 0) {
			return false;	// 存在0个帐号对应这个手机，注册
		} else {
			// 获取结果并发送短信
			// 暂时不发送
			// 注意：一定不能发，会泄露用户的密码！zhouj 2013.3.12
			if(sendAccountSms) {
				Integer sendCount = (Integer) (SmsUtil.sendHash.get(mobile));
				if (sendCount == null) {
					sendCount = new Integer(0);
					SmsUtil.sendHash.put(mobile, sendCount);
				}
				if (sendCount.intValue() < SmsUtil.MAX_COUNT_PER_MOBILE) {
					sendCount = new Integer(sendCount.intValue() + 1);
					SmsUtil.sendHash.put(mobile, sendCount);
				
					String content = "您在乐酷已经注册了帐号.";	// 只返回注册最近的账号了
					for (int i = 0; i < userIdList.size(); i++) {
						Integer userId = (Integer) userIdList.get(i);
						if (userId == null)
							continue;
		
						UserBean user = UserInfoUtil.getUser(userId
								.intValue());
						String passWord = user.getPassword();
						String userMobile = user.getMobile();
						passWord = Encoder.decrypt(passWord);
						String send = "ID" + (i + 1) + ":" + userId + "密码"
								+ passWord + ";";
						if ((content.length() + send.length()) >= 60) {
							// 发送短信息
							SmsUtil.send(SmsUtil.CODE, content, userMobile,
									SmsUtil.TYPE_SMS);
							content = send;
							if (i == (userIdList.size() - 1)) {
								SmsUtil.send(SmsUtil.CODE, content,
										userMobile, SmsUtil.TYPE_SMS);
							}
						} else {
							content = content + send;
							if (i == (userIdList.size() - 1)) {
								SmsUtil.send(SmsUtil.CODE, content,
										userMobile, SmsUtil.TYPE_SMS);
							}
						}
					}
				}
			}
			return true;
		}
	}
	// 不会注册账号，而是call_log2表里加入一条记录，用于用户上来后开始注册（辨认手机号）
	public static UserBean preRegister(String mobile) {
		List list = SqlUtil.getObjectList("select password from call_log2 where mobile='" + mobile + "'", 5);
		String password;
		if(list.size() == 0) {
			password = "50" + String.valueOf(RandomUtil.nextInt(10, 99));
			SqlUtil.executeUpdate("insert into call_log2 set create_time=now(),mobile='" + mobile + "',password='" + password + "'", 5);
		} else {
			password = (String)list.get(0);
		}
		
		UserBean user = new UserBean();
		user.setPassword(password);
		
		return user;
	}
	// 不注册账号，而且只是把密码更新到call_log2，用户登陆后要重新输入昵称
	public static void preRegister(String mobile, String password) {
		List list = SqlUtil.getObjectList("select password from call_log2 where mobile='" + mobile + "'", 5);
		if(list.size() == 0) {
			SqlUtil.executeUpdate("insert into call_log2 set create_time=now(),mobile='" + mobile + "',password='" + password + "',type=1", 5);
			String content = "欢迎来到乐酷http://m.joycool.net.您的初始密码为"
				+ password
				+ ",用本手机号和此密码就能登陆.期待您的光临!";
			SmsUtil.send(SmsUtil.CODE, content, mobile, SmsUtil.TYPE_SMS);
		} else {
			SqlUtil.executeUpdate("update call_log2 set password='" + password + "',create_time=now() where mobile='" + mobile + "'", 5);
		}
	}
	
	public static UserBean autoRegister(String mobile) {
//		 取得昵称
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		int maxId = dbOp.getMaxId("user_info");
		dbOp.release();
		// 取得用户名
		String un = mobile;
		if (un.startsWith("13")) {
			un = un.substring(2);
		}

		UserBean  user = new UserBean();
		user.setId(SequenceUtil.getSeq("userID"));
		// macq_2006-11-06_设置随机年龄数字_start
		int[] age = getRandomAge();
		user.setAge(age[RandomUtil.nextInt(age.length)]);
		// macq_2006-11-06_设置随机年龄数字_end
		user.setGender(RandomUtil.nextInt(2));
		user.setMobile(mobile);
		// 女性
		if (user.getGender() == 0) {
			String[] fs = getFemaleNicks();
			user.setNickName(fs[RandomUtil.nextInt(fs.length)]);
		}
		// 男性
		else {
			String[] fs = getMaleNicks();
			user.setNickName(fs[RandomUtil.nextInt(fs.length)]);
		}
		// mcq_2006-8-30_更换密码算法_end
		String password = net.joycool.wap.util.Encoder.encrypt(String
				.valueOf(RandomUtil.nextInt(100000, 999999)));
		// mcq_2006-8-30_更换密码算法_end
		// String password = net.joycool.wap.util.Encoder
		// .getMD5_Base64("p" + un);
		user.setPassword(password);
		user.setSelfIntroduction("我是乐客" + (maxId + 1));

		AreaBean area = AreaUtil.getAreaByMobile(mobile);
		if (area != null) {
			user.setPlaceno(area.getPlaceno());
			user.setCityname(area.getCityname());
			user.setCityno(area.getCityno());
		}

		if (!getUserService().addUser(user)) {
			return null;
		}
		// 添加Blog 添加用户状态 添加一条系统消息
		else {
			// zhul-新增代码块 2006-06-07 start
			// 在此判断此用户是否有用户状态记录，如果没有，向user_status表中增加一条本用户的新记录。
			user = UserInfoUtil.getUser(user.getId());	// 再从数据库取一遍……
			// UserStatusBean usb = getUserService().getUserStatus(
			// "user_id=" + user.getId());
			// zhul_modify us _2006-08-14_获取用户状态信息 start
			UserStatusBean usb = UserInfoUtil.getUserStatus(user
					.getId());
			// zhul_modify us _2006-08-14_获取用户状态信息 end
			if (usb == null) {
				usb = new UserStatusBean();
				usb.setUserId(user.getId());
				usb.setPoint(0);
				usb.setRank(0);
				usb.setGamePoint(100000);
				usb.setNicknameChange(0);
				usb.setLoginCount(0);
				if (!getUserService().addUserStatus(usb)) {
					return null;
				}
				// add by mcq 2006-07-24 for stat user money history
				// start
				MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER,
						100000, Constants.PLUS, user.getId());
				// add by mcq 2006-07-24 for stat user money history end
				// 求头衔
//				String name = null;
//				if (user.getGender() == 1) {
//					name = "乳臭未干";
//				} else {
//					name = "初出闺房";
//				}
				// 赠新人卡
				UserBagCacheUtil.addUserBagCache(user.getId(), 33);
				// 发送系统消息
				NoticeBean notice = new NoticeBean();
				notice.setUserId(user.getId());
				notice.setTitle("赠与您100000乐币！");
				notice.setContent("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setHideUrl("");
				notice.setLink("/user/registerSuccess.jsp");
				ServiceFactory.createNoticeService().addNotice(notice);

				IMessageService messageService = ServiceFactory
						.createMessageService();
				// macq_2007-3-22_给用户发送一封信件，内容包括用户名密码_start
				MessageBean message = new MessageBean();
				message.setFromUserId(100);
				message.setToUserId(user.getId());
				String content = "欢迎加入乐酷社区！您的ID是："
						+ user.getId()
						+ "，密码是："
						+ Encoder.decrypt(user.getPassword())
						+ "，请记住ID和密码,以后登陆可能用到噢！若如有疑问，请拨打4006665721，" +
								"与管理员联系";
				message.setContent(content);
				message.setMark(0);
				messageService.addMessage(message);
				// macq_2007-3-22_给用户发送一封信件，内容包括用户名密码_start
			}
			// zhul-end
			// macq_2007-3-22_更改通过手机号获取最后一次登录用户信息_start
			// user = getUserService()
			// .getUser("mobile = '" + mobile + "'");
//			user = getLastLoginTimeByUserBean(mobile);
			// macq_2007-3-22_更改通过手机号获取最后一次登录用户信息_end
//			if (!UserInfoUtil.updateUser(
//					"enable_blog = 1, user_name = '"
//							+ user.getUserName() + "'", "id = "
//							+ user.getId(), user.getId() + "")) {
//				return null;
//			}
		}
		return user;
	}
	
	public static IUserService getUserService() {
		if (userService == null) {
			userService = ServiceFactory.createUserService();
		}

		return userService;
	}

	public static String[] femaleNicks = { "如月", "靓靓", "素飞", "松仙", "芊芊", "雪莹儿",
			"苦咖啡", "蓝蝎子", "雨冰琳", "紫薇星", "雨庭台", "水的记忆", "爱你久久", "无限蔚蓝", "蓝色忧郁",
			"命中注定", "菱花仙子", "快乐魔女", "秋水亦文", "夜空彼岸", "香梨教主", "柔和七星", "雪夜归人",
			"魔卡女孩", "银翼天使", "似水年华", "倦鸟迟归", "触动心弦", "初雪霏霏", "雾也温柔", "怡然似水",
			"梦中有雨", "我爱咩咩", "冷雪追魂", "楠溪居士", "水果布丁", "嘻哈天使", "春天浪漫", "婀娜娃娃",
			"白雪公主", "卡颇齐诺", "漂浮的云", "樱花居士", "又见彩虹", "风轻云淡", "静水流深", "萱草忘忧",
			"一线香", "黄蓉", "杨不悔", "殷素素", "还珠", "紫薇", "小丫", "明晨", "青衣", "紫霞",
			"青霞", "孽海花", "天山童姥", "星宿仙子", "幽梦", "阿朱", "阿碧", "白晶晶", "盘丝大仙",
			"梅艳芳", "倩女幽魂", "女友", "野蛮女友", "阿房女", "朱丽叶", "绝色美女", "美凤", "武则天",
			"河东狮喉", "林心如", "黛安娜", "王妃", "公主", "猫咪", "小猫", "飞影", "梦醒时分", "爱的代价",
			"短发", "调皮鬼", "小龙女", "紫杉龙王", "琼遥", "婉君", "追梦人", "谁能懂我", "处女", "明天",
			"小火柴", "幽若", "青色苹果", "小魔头", "桃花仙", "俏面", "Malcolm", "Joan", "Niki",
			"Betty", "Linda", "Whitney", "Lily", "Barbara", "Elizabeth",
			"Helen", "Katharine", "Lee", "Ann", "Diana", "Fiona", "Judy",
			"Doris", "Rudy", "Amanda", "Shirley", "Joan", "Tracy", "Melody",
			"Helen", "Debbie", "Lisa", "Yvonne", "Shelly", "Mary", "Dolly",
			"Nancy", "Jane", "Barbara", "Shirley", "Emily", "Sophia", "Vivian",
			"Lillian", "Joy", "Ross", "Julie", "Gloria", "Carol", "Taylor",
			"Wendy", "Grace", "Vivian", "Caroline", "Samantha", "Maria",
			"Kate", "Demi", "Sunny", "Wendy", "Ava", "Christina", "Judy",
			"Susan", "Grace", "Alice", "Joyce", "Sally", "Margaret", "Rebecca",
			"Teresa", "Rita", "Jessica", "Elizabeth", "Kelly", "May", "Julie",
			"Amanda", "懂圆", "胤菘", "碧永", "玉颖", "美微", "莎娟", "弘娣", "霞惠", "爽英",
			"俪芸", "榕倩", "徽金", "良楚", "乔琬", "双涓", "璐菲", "香妃", "玻霭", "容蔓", "负静",
			"艾淑", "惠晓", "常慧", "慧妍", "秀满", "脉纯", "赣盈", "和颖", "凡芳", "辟丽", "妮芬",
			"若偌", "淑玲", "耙结", "卿玫", "怡满", "代俊", "尝诗", "梅紫", "衔碧", "旖静", "裳幼",
			"品媛", "依颖", "平纯", "旖玟", "怡可", "贵瑛", "蓓秀", "香媛", "纯蔓", "石玲", "庄娟",
			"楚婉", "亚玉", "鼎儿", "妮立", "芹锦", "心筠", "保晓", "茨雪", "贤卿", "凡鲜", "服枫",
			"鹤美", "焕柔", "静U", "炼荔", "冠璧", "好慧", "娟枫", "淑爽", "俐珊", "誉滢", "凝玫",
			"娟若", "育秀", "名美", "驳紫", "永薏", "顺瑾", "媛香", "施玲", "枝黛", "纨淑", "圣清",
			"健佩", "茗风", "纯雨", "充荣", "新良", "航锦", "任苹", "惠香", "户薏", "先万", "荔竹",
			"舟育", "玉伶", "屏丽", "澄倪", "守嫦", "中卉", "承娴", "迈蔓", "熙霄", "澄凝", "霄洁",
			"想鹃", "开贤", "颖芳", "军楚", "北华", "孝叶", "羿蔓", "翠琼", "安芸", "君枚", "上璧",
			"晤妃", "脉秀", "前华", "道杏", "祈茹", "如碧", "瑗丰", "女雪", "琅霏", "蕾璐", "研樱",
			"锦满", "淮明", "若曼", "厚娜", "丽圆", "丰蕴", "等晓", "遍芮", "淑婷", "瑞静", "善芸",
			"言竹", "瑞匀", "昌韵", "笑菊", "婷苹", "尉妍", "望满", "枫茹", "贞枚", "慧楚", "杰卿",
			"僖娜", "根佟", "晶美", "有凌", "一珊", "英友", "嘉可", "木彤", "依君", "爽贞", "雪贤",
			"施静", "艳杏", "杭静", "卿美", "卿帧", "傍舒", "解娜", "赋蝶", "佳宣", "春晓", "岩可",
			"宛玫", "瑶友", "蓉芬", "肯舒", "若蓓", "先琬", "彩育", "尉琳", "澄蓓", "毓潇", "烈巧",
			"戚维", "澜宁", "楚卿", "瑞荣", "宛平", "恋可", "慈枝", "昀碧", "华晗", "捷俐", "静倩",
			"育瑜", "阳蔓", "荷媛", "超红", "巧美", "盎婷", "盼荆", "善凤", "慧帧", "小妃", "草英",
			"道倪", "文琅", "宁芳", "淑洁", "水媚", "瑶谊", "艺卓", "慧芳", "配倩", "惠佳", "齐妃",
			"艺清", "格琦", "珊丽", "家霄", "纯曼", "伙影", "晖立", "征丽", "奎立", "子妤", "泛楚",
			"珠静", "静纯", "芳妮", "隐华", "禄融", "经珍", "萱兰", "楚晓", "玉淑", "睿学", "韶靖",
			"新淑", "满柔", "羿音", "桦楚", "川静", "婷蓉", "碧环", "予勤", "嘉子", "好静", "醒莺",
			"雁琰", "冰爱", "萌丹", "来惠", "真淑", "韵咏", "涪静", "纯瑗", "冬祈", "守碧", "西春",
			"素丹", "声珠", "红艳", "沛芬", "历欢", "方竹", "屏慧", "琬晓", "卫方", "绚媛", "诺群",
			"润秀", "览懿", "赋心", "幸韵", "婵裳", "品立", "展灵", "惠芮", "顺伊", "临苑", "遥诺",
			"佟阳", "辰霏", "联幼", "倩霞", "彤君", "悲楠", "惠悦", "召贞", "立美", "澄谚", "桂音",
			"满甜", "藏荷", "媛樱", "希春", "秩枫", "德钻", "龙慧", "珍姣", "盼倩", "鸥嫦", "才洋",
			"从颐", "理圆", "裔馥", "佟梅", "琬娜", "封荷", "始莲", "雁徽", "吟瑾", "裳莺", "华筱",
			"璇碧", "贤香", "翼影", "时梅", "遥桃", "春帧", "肖梅", "必玫", "菲若", "芷璐", "婵菲",
			"叶琅", "替乔", "婷军", "香迎", "惠雪", "弥贞", "碧如", "槐同", "诞澄", "纯贞", "媚蔓",
			"婵静", "荷嫣", "慕柔", "友晨", "佟苹", "牡珍", "樱柔", "华哲", "沛美", "贤娣", "芷韵",
			"应莎", "春意", "夷慈", "阿茜", "瑗莎", "昔筱", "保茗", "紊蓓", "云姣", "紫谚", "蒲红",
			"秀梅", "桂笑", "希芳", "务筠", "霄莲", "甫蔓", "清妮", "弈薏", "苗英", "妃佳", "文岚",
			"懂窍", "白娥", "腾媛", "方园", "临满", "崇纯", "键媚", "力珂", "立喜", "秀荷", "宁丽",
			"羿丽", "暗珊", "秋焕", "纯惠", "雪茹", "枝晓", "埃美", "娅娜", "兆嫦", "徽碧", "缓慧",
			"英湘", "瑶雅", "石彤", "纨丽", "启芝", "谓军", "埃娥", "丽娜", "连忻", "凡碧", "连嫦",
			"蔓玫", "玫缨", "映俊", "情雪", "尉淑", "晓奂", "爽丽", "仪澜", "越霄", "虹慈", "欣萤",
			"恒群", "霞爱", "临娟", "雨杏", "聚静", "惠柔", "倩欣", "翰之", "菲恬", "巍艺", "雪静",
			"双玉", "俱娜", "讯宛", "展巧", "炼春", "杏娅", "娟玮", "芸雯", "雏倪", "材妙", "琳笑",
			"少碧", "少巧", "娣稚", "衷相", "斐如", "玮春", "锦靖", "瓢秋", "满蔚", "仲贞", "娟灵",
			"美可", "艾婕", "艺暖", "登晓", "理慧", "婷盈", "度英", "淼沛", "牡韵", "卿丽", "翼聪",
			"保霞", "慧盈", "茹汝", "蓉良", "蓓丽", "财莺", "琼秋", "嘉萤", "薄瑰", "巧美", "蕾倩",
			"焕同", "馨伶", "雁尧", "冷芳", "灵妞", "选纯", "衷婉", "郎秋", "白姝", "枝好", "倍薏",
			"眉丹", "裕美", "芸春", "正美", "摇碧", "法蔚", "万娥", "善香", "览齐", "幸蓓", "毓珍",
			"芷宛", "屏珠", "华晓", "睫云", "知群", "德霞", "先盼", "倪芹", "姬阑", "上韵", "艾蓓",
			"兵嘉", "杏雅", "枫美", "婷锦", "列蓓", "梅心", "惠巧", "海冰", "才静", "稠媚", "璐娟",
			"笙书", "宠穗", "津仪", "珊咏", "愚晓", "峥美", "莉静", "会碧", "晤妹", "逻贞", "尉芮",
			"藏宜", "笔淑", "泳静", "海晓", "升玫", "畅文", "庭荣", "春霏", "斯方", "利欣", "卿好",
			"功容", "若婕", "翔薇", "毅姝", "遇菲", "池爽", "并丽", "闰幼", "帆涣", "英慧", "棠洋",
			"芷屏", "桥尉", "东靖", "沃满", "祯霏", "帜然", "馨艺", "楷柔", "共雁", "广妍", "玉霄",
			"秀滕", "晗玉", "美露", "其正", "倪碧", "妤影", "露美", "敬媛", "莘帧", "愚媛", "瑶娟",
			"拓倪", "楠仙", "芝曼", "明楚", "永晶", "从淑", "珂华", "军姿", "美婉", "颐曼", "圃兴",
			"兰知", "齐锦", "静璐", "卉谰", "国婵", "利妃", "芷荷", "垂璐", "仁媚", "元菲", "毓玉",
			"彦露", "珊萍", "媛乔", "子娅", "君姿", "哲蓉", "澜宣", "单涣", "佳娟", "璇环", "家楚",
			"力雯", "纯童", "冰瑗", "恬妤", "明淑", "晓迎", "希梦", "剑晓", "骋遥", "表君", "建叶",
			"坤满", "瑾青", "瑶涓", "明焕", "艾卿", "俊雪", "亚凌", "靖楚", "颐燕", "鼎若", "研燕",
			"厉花", "廷卫", "庭丽", "瑗澄", "双梅", "碧秀", "英碧", "丽素", "纯霏", "叙正", "利雪",
			"惠霄", "榜茹", "凌霏", "佟珍", "妃荷", "菊焕", "淼晗", "玉苗", "佑瑜", "颖珍", "锦春",
			"航榕", "傍匀", "秀淑", "琬瑶", "驰美", "丽瑰", "慧琬", "滔相", "承颜", "满诗", "北若",
			"依珍", "利倪", "诺婉", "幅馨", "云美", "奂茨", "宙爱", "包静", "英霞", "恬若", "茗晶",
			"赋惠", "仙奂", "彩颐", "义凤", "蓝莹", "尚圆", "慈哲", "钢素", "燕丽", "拥秀", "芳楚",
			"毕梅", "昭雨", "奕倩", "爱芹", "愿尧", "珠利", "蓉月", "淑瑞", "策菲", "芸一", "奎月",
			"慧黛", "谱书", "芬晓", "耿瑰", "馥淑", "洋媚", "澄妃", "均颜", "静徽", "蓓妍", "迟惠",
			"婷琬", "菲素", "庸菲", "碧巧", "霞雯", "察雅", "帆眉", "君卿", "具晓", "谚旖", "梅利",
			"侨艳", "稳慧", "效若", "幼艳", "帛彩", "炜楚", "品蕊", "挺英", "青稚", "瑾霏", "纤芭",
			"之芳", "习蔓", "务满", "当琬", "杏琳", "城萤", "汐静", "俐娅", "萍倪", "裔璐", "粉育",
			"娟友", "楚玫", "准素", "峪莹", "婷香", "茹忻", "梅清", "知霞", "操鸣", "满佟", "立可",
			"婷满", "川碧", "稠丽", "毅璐", "茹柳", "户音", "楚萤", "梦菲", "妍思", "记圆", "文巧",
			"腾怡", "襄舒", "格心", "歆澜", "鸿颖", "宜卫", "暖军", "南勤", "懿玉", "霞茹", "谰茹",
			"惠腾", "枫羡", "票柳", "淮雁", "灿丽", "璇淑", "超谰", "阿屏", "秀真", "宠英", "昆玟",
			"浮岚", "闰美", "汶婕", "静娅", "珠静", "幸琅", "芝曼", "眉雪", "继徽", "贞蔓", "晾园",
			"临方", "史鲜", "桂嫡", "夷勤", "蓓蔓", "豆嘉", "正莹", "鸣幼", "琳茨", "扁泳", "汇颖",
			"蕊育", "佩芸", "进军", "树娥", "俭钻", "其尉", "榜蔚", "奕茗", "方琦", "玫欢", "谦芳",
			"里影", "桂兴", "稠玟", "席盼", "泰婧", "贤慧", "田晓", "谦贞", "东羡", "童淑", "廉娜",
			"秀女", "秀瑞", "传惠", "醇兰", "等淑", "茹可", "玉珠", "妹雪", "卿悠", "业佳", "艺华",
			"颜蔓", "廷文", "力诗", "婕谰", "圃翠", "丽琬", "越娜", "齐贞", "瑁晓", "莹明", "筱珍",
			"靖梅", "添婉", "雪娜", "妹瑾", "韵俏", "可秀", "力娜", "贞涓", "睫梅", "君淑", "敏静",
			"芳涟", "若叶", "群巧", "斐晓", "近丽", "润璐", "奥琬", "北冬", "慈丽", "圃方", "依楚",
			"丹焯", "姝桂", "芸涓", "香满", "满珊", "河群", "玫静", "帜园", "丛瑶", "欣凌", "拓珍",
			"里沛", "俐嫦", "缅霞", "泽露", "彩媛", "红澜", "经平", "弃灵", "蝶伶", "竹裳", "静雯",
			"飘羡", "相蔓", "源巧", "责月", "枝琬", "华静", "峰雪", "奋满", "翰茜", "澄杏", "霄恬",
			"瑜蓓", "葛绚", "玉梅", "志静", "然飘", "临妙", "逸婷", "育结", "加云", "蜀贝", "静娅",
			"仙彦", "衷钰", "遍稚", "立维", "蒙曼", "沛珍", "贞向", "卓偌", "达斐", "薏场", "夷秋",
			"乃秀", "肇亭", "菊花", "博晓", "惠双", "展娟", "尔祈", "雪倩", "柯霄", "勤萍", "奕菲",
			"云颖", "节慧", "幼巧", "娟茗", "艾英", "满群", "永婷", "敦秀", "蕾燕", "贵昀", "婵潇",
			"寒咏", "姬绚", "赋文", "荆芹", "道雪", "俩鹃", "婷嫣", "英清", "丽璐", "澜媚", "伍飘",
			"玫靖", "丞芷", "欣梅", "斐双", "玫蓓", "娅正", "贡瑶", "雪霞", "良晓", "献伶", "淳甜",
			"媚春", "希素", "满巧", "曼珍", "静萱", "朴素", "壬娇", "荐贞", "卿丹", "玻谚", "巧芳",
			"节玫", "曙珍", "鑫娅", "裕华", "放满", "薇香", "心碧", "翊婷", "齐倩", "讯瑾", "史晶",
			"纯蓓", "存霄", "善可", "铮苗", "兴美", "秋肖", "东萤", "储薏", "发岚", "聪碧", "继研",
			"火欢", "俐慧", "卓香", "方菲", "述仙", "泳银", "尘玫", "兴子", "晶珊", "琳宜", "稚鹃",
			"仙学", "丰颜", "谷欢", "匀霏", "菊徽", "贝琼", "香培", "华美", "丽雁", "霞曼", "赋嫦",
			"倩滕", "庆凝", "欣洁", "翼芸", "初雪", "贤爽", "嫦云", "舒阑", "自岚", "昕华", "冬东",
			"姗茨", "长惠", "晓文", "界霞", "静纨", "昱蓓", "霞迎", "嫦茜", "吟娜", "华宛", "慧诗",
			"坤佳", "姗平", "衷蓓", "琳茹", "兰慧", "匀婷", "子霞", "曦清", "俩倪", "奉晓", "白鸣",
			"秀桂", "光妃", "悲良", "乔碧", "育霄", "淑宜", "曲妮", "谱迎", "永玫", "础姣", "里芬",
			"泛荣", "晓旖", "园秀", "贯芝", "歆乔", "涣男", "婉静", "越冰", "义满", "怡爱", "丘静",
			"霞晓", "功蔓", "皆眉", "古群", "轩华", "沂燕", "曼英", "古方", "稳聪", "宜雁", "若菲",
			"史花", "力晶", "枚幼", "力英", "红卫", "琬涓", "想嫦", "依瑰", "叶倪", "庆焯", "吉祈",
			"娥涣", "嫦倪", "余云", "兆莉", "朴满", "巧眉", "达澜", "方曼", "准焯", "伶绚", "静芸",
			"肖蕾", "毅妙", "储菲", "负懿", "玫飘", "依菊", "澄妞", "越淑", "埂梅", "徽聪", "银向",
			"蔓蔓", "衍秀", "壬璐", "喜岚", "剑亭", "玫雪", "羡晗", "峥静", "瑛文", "丙秋", "蓓艺",
			"弘彤", "庚巍", "雪尧", "烽燕", "有偌", "亭璇", "茜梅", "负美", "翔嫡", "庆稚", "研妃",
			"瑁方", "晤燕", "阑芳", "选妮", "满贤", "齐俏", "彩桂", "香蝶", "贤稚", "尘瑾", "颢童",
			"宁良", "惠焯", "景晓", "芳岚", "卿谊", "淑芸", "坤悠", "励宣", "经淑", "美悦", "贞璐",
			"妙荷", "召慧", "延倩", "渝娟", "窍欢", "雪之", "盈满", "愿嘉", "京雅", "琳培", "迅英",
			"满春", "舟碧", "世幸", "贞霏", "云静", "京荆", "曲瑞", "家瑜", "贝芳", "岚莺", "淮凤",
			"家蔓", "当金", "秀晓", "东仪", "厚蓉", "静倪", "灏芳", "聚凉", "睿晨", "素筱", "凤妤",
			"辰璐", "华贞", "偌妃", "政淑", "展淑", "婷涓", "雁璐", "昱泳", "玉碧", "丽霄", "达竹",
			"琪紫", "琛秀", "毕雯", "满贞", "超桃", "巩情", "斐柔", "育眉", "婷嫦", "登媛", "亭丹",
			"妙艺", "辰瑛", "倪佩", "颁吟", "妹慧", "频嘉", "培尚", "篱雪", "希雪", "爽桂", "锦瑜",
			"越慧", "若慧", "向徽", "彦娜", "甫方", "衷梅", "妹荔", "贤吟", "雪茹", "灏仪", "愈樱",
			"兰琼", "梦圆", "靖慧", "石盼", "池碧", "灏卉", "介诗", "瑾翘", "丽婧", "璧芝", "聚雪",
			"倚晓", "东英", "静娟", "世华", "国莲", "鲜妃", "龄银", "真贞", "峦曼", "芮远", "哲琴",
			"励兰", "娅娅", "媛敏", "畅玮", "彤菲", "承咏", "显美", "东莉", "品盈", "财金", "春惠",
			"枝惠", "方嫦", "紫育", "容淑", "辨谊", "帜锦", "尔巧", "度娟", "发姐", "松宛", "霞欣",
			"秋永", "翼惠", "冉茹", "素晓", "乔纨", "名学", "华婕", "羽焕", "力珍", "志场", "婕巧",
			"绚衷", "荆华", "槐芝", "孰研", "连晓", "柔蕾", "璐珠", "熹牡", "熙芳", "贵晴", "阐楠",
			"珊纯", "妃思", "纤儿", "华梅", "细培", "国桃", "俩焕", "帆蝶", "姗霞", "凤筱", "群舒",
			"研雅", "海蔓", "凉倩", "茹音", "晓远", "毓春", "房峥", "舒玫", "嘉慈", "枫丽", "芹娟",
			"梅荔", "谊勤", "珑薏", "格友", "颜玉", "静方", "学立", "搏聪", "敬若", "贤依", "飘颜",
			"玛娟", "皓窍", "静丽", "丘沛", "向卿", "满艺", "蓝淑", "宠潇", "仙艳", "征愿", "芹美",
			"丞媛", "廷紫", "霞娇", "满娜", "裕贞", "海楚", "培娜", "遥爽", "定薏", "满曼", "英恋",
			"琦", "妹霄", "羽蓉", "赋贞", "悟倩", "薇真", "镜琬", "瑞毓", "敦愈", "沂清", "清梦",
			"泽荔", "丽正", "誉勤", "媛恬", "湃茹", "鹃书", "汐秀", "瑛惠" };

	public static String[] getFemaleNicks() {
		if (femaleNicks == null) {
			femaleNicks = new String[1];
			femaleNicks[0] = "乐客";
		}
		return femaleNicks;
	}

	// macq_2006-11-06_初始化年龄数组_start
	public static int[] age = { 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28 };

	// macq_2006-11-06_初始化年龄数组_end

	// macq_2006-11-06_获取年龄数组_start
	public static int[] getRandomAge() {
		if (age == null) {
			age = new int[1];
			age[0] = 25;
		}
		return age;
	}

	// macq_2006-11-06_获取年龄数组_end
	public static String[] maleNicks = { "蓝调", "萧枫", "顾雨", "雪魔", "侠盗", "孤星泪",
			"瞌睡虫", "稻草人", "无尾熊", "蝶恋花", "舍我其谁", "流云润雨", "大雪飞扬", "个性飞扬", "紫色阴雨",
			"忧郁小生", "黯淡幽谷", "普罗米修", "木心木意", "捧着幸福", "浪迹天涯", "我驭我心", "亮子心缘",
			"一错万年", "醋溜白菜", "水与双鱼", "风流荡剑", "冷血孤星", "钟情遇你", "短信人生", "漫步海滩",
			"小兔淘淘", "今生无悔", "空谷流音", "柳絮扬眉", "投身革命", "单身傻瓜", "海阔天空", "往事如风",
			"黄昏独步", "雨过阳光", "呆呆看聊", "无为而治", "极度郁闷", "枫扬杏鸿", "蓝天白云", "灵云扬羽",
			"独饮夜色", "优质高粱", "青青子衿", "仰天一笑", "凡海尔星", "张三丰", "隆巴多", "辛巴达", "幽若",
			"步惊云", "聂风", "陈皓男", "老马", "古龙", "金大侠", "风流侠", "万里独行", "千里独行", "忍者",
			"中华英雄", "龟仙人", "孙悟空", "樱木花道", "流川峰", "流川枫", "武松", "宋江", "星矢", "郭靖",
			"王重阳", "丘处机", "杨康", "杨过", "杨逍", "阳顶天", "杨六郎", "东方不败", "西方失败",
			"韦小宝", "韦大宝", "至尊宝", "至尊玉", "食神", "少林方丈", "不可不戒", "至尊无敌", "陈真",
			"元甲", "连杰", "成龙", "马加爵", "圣斗士", "泰龙", "润发", "张无际", "谢逊", "宋远桥",
			"胡一刀", "苗人凤", "战士", "天才宝宝", "李咏", "志毅", "海东", "孙悟空", "八戒", "唐僧",
			"沙僧", "小白龙", "陈近南", "陈家洛", "倚天", "屠龙", "无名", "过客", "慧能", "张君宝",
			"朝阳", "志东", "得道高人", "段誉", "萧峰", "乔峰", "虚竹", "虚竹子", "丐帮帮主", "洪七公",
			"欧阳峰", "欧阳疯", "欧阳克", "欧阳飘逸", "李秋水", "慕容复", "慕容博", "萧远山", "巴乔",
			"马拉多纳", "贝利", "乔丹", "伍茨", "亨利", "牛顿", "爱因斯坦", "爱迪生", "成吉思汗",
			"星宿老仙", "雄霸", "山鸡", "神仙", "少林高手", "寂寞高手", "赌神", "赌圣", "刘德华", "张国荣",
			"新好男人", "天涯刀客", "伤心人", "快刀浪子", "边城浪子", "一灯", "段正纯", "段志兴", "黄药师",
			"黄老邪", "东邪", "北丐", "南帝", "南僧", "安达", "拖雷", "成吉思汗", "铁木真", "忽必烈",
			"秦始皇", "荆柯", "吸血鬼", "孔子", "孟子", "老子", "孙子", "鲁般", "汉高祖", "汉武帝",
			"座山雕", "杨子荣", "一休", "智者", "愚公", "总统", "布什", "恩来", "泽东", "锦涛", "泽民",
			"少奇", "德怀", "皇帝", "君主", "刘备", "张飞", "关羽", "孔明", "诸葛亮", "诸葛孔明",
			"卧龙", "孙膑", "李师师", "罗密欧", "贝利", "多情种子", "多情剑客", "多情汉", "风流小生",
			"大款", "李嘉诚", "阿扁", "达芬奇", "阿凡题", "大话西游", "天下第一", "天王老子", "天王地虎",
			"CS高手", "星际高手", "星际小子", "快打旋风", "周杰伦", "周星星", "谢霆峰", "志愿军", "僧人",
			"大黄", "爵士音乐", "绝代骄子", "天之骄子", "热血青年", "战无不胜", "英雄", "残剑", "飞鹰",
			"苍鹰", "解放军", "军人", "新好男人", "酒神", "神雕侠", "老顽童", "中神通", "法王", "法老",
			"蒙古王子", "赏善罚恶", "武当真人", "大师", "高僧", "一马平川", "亢龙有悔", "飞龙在天", "神龙摆尾",
			"大富翁", "大款", "金毛狮王", "白眉鹰王", "青翼蝠王", "昆仑高手", "峨嵋祖师", "峨嵋门人",
			"华山掌门", "令狐冲", "桃谷六仙", "君子剑", "岳飞", "贾宝玉", "苏轼", "东坡", "李白", "杜甫",
			"白居易", "白乐天", "李自成", "李闯王", "真命天子", "风雨无阻", "让风吹", "天涯刀客", "天涯路人",
			"唐吉科德", "孟达", "星爷", "天天客", "曹操", "Paul", "Sam", "Francis", "Lewis",
			"Stephen", "Andy", "Scott", "Albert", "Kevin", "Michael", "Taylor",
			"Jackson", "Jack", "Jimmy", "Allen", "Martin", "Vincent",
			"Richard", "Howard", "Allen", "Johnny", "Robert", "Martin", "Jeff",
			"Charles", "Mark", "Bill", "Vincent", "William", "Joseph", "James",
			"Henry", "Gary", "Martin", "Fred", "Gary", "William", "Charles",
			"Michael", "Karl", "Bob", "John", "Thomas", "Dean", "Paul", "Jack",
			"Brooke", "Kevin", "Louis", "John", "George", "Henry", "Benjamin",
			"Robert", "Carl", "Scott", "Tom", "Eddy", "Kris", "Peter",
			"Johnson", "Bruce", "Robert", "Peter", "Bill", "Joseph", "John",
			"Burt", "Charlie", "Elliot", "George", "Johnson", "Richard",
			"James", "Charles", "Bruce", "David", "Nick", "Walt", "John",
			"Mark", "Sam", "Davis", "Neil", "Carl", "Lewis", "Billy", "莘祯",
			"弈益", "贵绍", "材可", "帆君", "柯茂", "洋冉", "丞家", "粟摇", "互政", "泰禄", "自宙",
			"功贝", "南敬", "荐诚", "两友", "骏耀", "归承", "纪解", "政赞", "桦知", "辽丙", "钱霄",
			"遥治", "沃迢", "斌德", "希承", "豪沛", "杰斌", "聚灶", "纯靖", "焱隆", "子三", "夜仆",
			"堂彬", "禄尉", "强致", "越理", "丰孝", "悟修", "熊常", "裔蓝", "瑛康", "苏润", "摇知",
			"巾均", "知诚", "柱宝", "襄展", "皆招", "屏堂", "厚尧", "思昆", "修匀", "朴锡", "善知",
			"皆御", "钱泽", "隐敬", "来察", "琅纹", "耿桂", "和炎", "功臻", "琢菁", "谓珑", "钱然",
			"熊森", "文兴", "恩望", "添联", "品澜", "涛树", "召科", "翘上", "细边", "豪开", "葛羽",
			"沛麒", "保平", "大格", "瑁泽", "影熊", "知坚", "党应", "仔世", "立淼", "加徽", "愈凯",
			"西琦", "佳细", "润耀", "坤登", "文福", "皓亿", "健赋", "草棋", "悟贯", "悲琰", "昱晖",
			"富火", "壮蔚", "义超", "宙荣", "联真", "练钢", "城棠", "纳千", "品麟", "亮堂", "服牡",
			"科蔚", "起朋", "强盼", "幸杰", "东庚", "凌峦", "稻诺", "荔赞", "放佟", "古熹", "申偌",
			"衷艾", "皑蔚", "京强", "良壁", "暗枫", "持晨", "斑豆", "摇德", "壁焱", "石申", "邦诞",
			"诺奇", "阿聚", "维", "聚耿", "越舜", "革朴", "修可", "捷伯", "启石", "翔海", "世知",
			"习佑", "望昊", "光容", "持颂", "生方", "友奇", "尧锵", "党弘", "颢经", "致颂", "豆可",
			"保进", "洲钧", "骋力", "礼克", "应新", "起清", "朴爽", "拓才", "茁营", "思瑞", "雄浩",
			"伦菲", "键声", "准灶", "章学", "夕信", "培蔚", "单根", "任菁", "阿信", "露汐", "康良",
			"漂仆", "更房", "琦昌", "坤佩", "东强", "刚克", "禄伟", "冠谆", "毅佳", "缓耀", "广卓",
			"楠舟", "洋祯", "绍坤", "刚御", "枝献", "衷基", "健少", "倌图", "键营", "渭东", "焕烽",
			"登招", "仁望", "利会", "之聚", "骋依", "乐喜", "晶操", "健封", "焱云", "翊鹏", "蔚荷",
			"昼庸", "僖康", "匀牛", "可池", "顺知", "江屏", "舟宙", "木维", "世希", "鑫麟", "官广",
			"江希", "彪家", "武细", "江妙", "枫羿", "康界", "比水", "榕昌", "闰群", "羿槐", "焱运",
			"浩三", "游淼", "羽哲", "斯骏", "维俊", "甫涌", "孝知", "金铮", "澄鸿", "政容", "圭祈",
			"羿夏", "经岸", "元操", "凡赫", "廷渝", "蔼刚", "龄染", "奕禄", "漳荣", "曼鹊", "偌深",
			"书炯", "炎搏", "灼曦", "丹弘", "荡岩", "艺德", "准仲", "太刚", "孰琰", "轮震", "皑界",
			"旭修", "木善", "昌之", "裕笙", "航松", "双友", "国南", "宙芹", "灿亨", "震迢", "自男",
			"勇哥", "摇威", "利利", "余僖", "努永", "毓晨", "精木", "蓝镜", "诞朋", "伯夜", "守晋",
			"炯佩", "晨雨", "滕赐", "东飞", "玻峰", "顾俊", "键佑", "灼宇", "火幸", "奥键", "庚儒",
			"驹锐", "和谦", "年鼎", "领思", "冻贵", "盎运", "利耀", "笔冉", "屏廷", "劝池", "千操",
			"韶森", "埂佩", "屏民", "宏琛", "衷营", "祥谆", "望权", "桦羽", "广福", "帮杰", "彤峙",
			"敖祯", "广国", "欢祥", "培察", "乐武", "灵健", "解汐", "冰锦", "莘淼", "全群", "迪楠",
			"桂仁", "雏宜", "硌浮", "滔孰", "隆良", "辰舜", "歆讯", "林津", "顺欣", "汐磊", "声传",
			"彦怀", "冲丞", "兴越", "栋福", "泽君", "欣细", "初沛", "剑士", "洪逊", "力弘", "泳武",
			"邦匡", "咏魁", "近善", "曼新", "孰仲", "僚邦", "炳科", "厉仲", "伍庸", "苞涛", "康鹏",
			"岩州", "凌诚", "铮澄", "近育", "翘厦", "荷依", "航士", "悲古", "冰宁", "旬英", "苍伯",
			"效珂", "冬骏", "俯赣", "志法", "蒙儒", "吟斐", "道运", "吏凌", "俭峪", "贵知", "荔钢",
			"泳察", "可浪", "继埂", "始臻", "辉聪", "旆妗", "笙恰", "松察", "羡艺", "归禧", "冬佑",
			"澄灿", "仁进", "根镜", "添澄", "勇杭", "男奇", "宜强", "票江", "曼立", "操楠", "如魁",
			"昕枝", "搏廷", "衷彪", "埂朴", "永尚", "连映", "风秀", "火群", "幅希", "逸祥", "翼悦",
			"沛孝", "赋弘", "三偌", "澄昼", "岳振", "力招", "桂常", "眺信", "细洋", "毅献", "炯宏",
			"亮群", "霆盼", "竟涌", "愈枝", "善亨", "臣北", "耙妙", "恩可", "川成", "修儿", "卫淦",
			"如亮", "耀兴", "润富", "越树", "振党", "半威", "翼铿", "佛先", "骏恒", "心伙", "涌有",
			"翔善", "灶奎", "粟科", "文淦", "相纳", "弘骏", "发江", "祥顺", "韶溢", "小廉", "藏霖",
			"琅声", "耙众", "武麟", "源乔", "备剑", "禄豪", "航策", "榜达", "璧靖", "谓鸥", "舟钧",
			"颖斗", "熊封", "利尚", "尚危", "竟敬", "杭虎", "表经", "等盖", "旭光", "鸿信", "松庚",
			"贝兵", "振大", "宏登", "南泰", "邦薄", "州里", "凛烈", "鸿亮", "研单", "信俭", "爽太",
			"龙廷", "笔昱", "贤备", "荣溢", "孝湖", "会纳", "家邦", "小泳", "原赐", "谱遥", "熹威",
			"冠宁", "想羿", "博根", "醒朝", "包争", "峪克", "辅仁", "亮波", "远顾", "待粟", "幸森",
			"佟赐", "东楚", "火全", "康伊", "昆榕", "中申", "皆培", "乔礼", "贤敏", "政宇", "克平",
			"菊致", "世伟", "办谆", "埂顺", "维衍", "强致", "祈臁", "稠龄", "牡苏", "善灼", "愿夕",
			"具勤", "修信", "露云", "茗修", "察根", "纤会", "赋浩", "洪麒", "吏仁", "海邦", "待邦",
			"挺宁", "漂永", "庚僖", "质聪", "穗淳", "震辉", "东淳", "怀冶", "迈招", "奋界", "钦琢",
			"銮乐", "永登", "颖奇", "越连", "瑜纯", "渝埂", "相平", "常江", "紊功", "毓政", "昆琪",
			"浩昌", "秀染", "兆刚", "烨祖", "尘铿", "魁和", "昱汇", "理轮", "群悦", "敏家", "谦余",
			"会郎", "繁艾", "导皆", "珑研", "镜皓", "池庚", "颢鲁", "燃蔚", "恋宾", "厚凉", "谡衷",
			"必城", "稚祥", "荷廷", "澄贤", "舟澜", "海忠", "政衷", "桦界", "剑朋", "贝鸣", "包臻",
			"达皓", "兆丘", "佩銮", "木峥", "蜀剑", "誉清", "澄国", "暗广", "澄吟", "烽谱", "键喜",
			"满羡", "比豆", "翼海", "宁杰", "鲁桂", "余岚", "法烨", "扁荣", "庭冠", "朋吟", "俭睿",
			"加庚", "拓轮", "轮震", "党钊", "堂智", "褒忠", "隆汇", "超耀", "梁贯", "纹经", "堂珩",
			"坤朋", "泛钦", "滔裕", "连管", "拓亮", "钱贤", "航皓", "梨渝", "曲舜", "小德", "云吟",
			"桥牡", "宇宪", "弘元", "淡佟", "凌希", "廉琢", "谷善", "戚房", "丞卓", "沛朋", "小修",
			"搏延", "霖余", "安亨", "纯幼", "轩克", "习衷", "奋启", "醒根", "临启", "谓高", "醒政",
			"幼宝", "友鲜", "矗泽", "绍利", "扬夕", "彤艾", "剑光", "炎滕", "锡妙", "燃明", "奇添",
			"佩瑾", "鸥翘", "介应", "童睿", "荷盛", "鸣更", "涌学", "枝赞", "羿远", "自镜", "瑶徽",
			"祖节", "炯段", "仔槐", "备宙", "健保", "京致", "均晾", "影力", "赐运", "润茗", "水备",
			"小环", "季哲", "逊儒", "满革", "华行", "建晨", "兴蒙", "尧祥", "宙翊", "子珑", "放遥",
			"捷柱", "毅义", "奥淦", "霖诺", "霄谦", "如北", "妙昕", "皆厚", "瑰致", "焯晾", "景伯",
			"熘伞", "未汀", "匡银", "瑾之", "煌法", "效乔", "永驰", "友新", "利邦", "聚均", "理魁",
			"藏骞", "弈武", "尔贤", "善摇", "睦弥", "约冉", "琦雄", "敏万", "感昱", "吟上", "淡贤",
			"映昌", "薄敦", "盼会", "奕爽", "仲广", "仰运", "修禄", "致业", "青毓", "保甫", "蒲东",
			"图晖", "焕文", "细伦", "龄尚", "烊佟", "俊骋", "慎河", "生伍", "菁君", "庸福", "挺治",
			"业争", "宪咏", "桂彩", "策先", "奕甫", "贵盖", "友国", "根学", "立鸣", "旭福", "印棉",
			"豪汐", "舟廷", "凛靖", "东童", "牛伯", "谆涌", "纯哲", "祈钊", "江熊", "晨白", "凯生",
			"舟单", "晨霄", "棠珩", "桂民", "奇可", "皋沛", "俐赞", "叔羽", "朝知", "铭邦", "炎菁",
			"荔平", "楷保", "彦寒", "守豆", "亚伦", "段隐", "资家", "悦漳", "聪信", "千立", "曲蔚",
			"朗均", "波宇", "焙均", "斌单", "渝格", "务忠", "明古", "锦翔", "薄超", "臣建", "诚赐",
			"舒幼", "知薄", "厉文", "蜀兆", "湃义", "从群", "裁寒", "岸勇", "冷光", "丹汉", "宙农",
			"从珂", "毕乐", "熹功", "冬珩", "曦班", "营石", "豪威", "承麒", "席谱", "辽励", "代睦",
			"豪宏", "宪佳", "建伯", "操澄", "春昌", "冠俊", "可衍", "彬应", "壁莱", "诚生", "键康",
			"策禧", "规琦", "熙霖", "宏苞", "沃愿", "轮歆", "震灼", "颜灶", "名亮", "谡亮", "权刚",
			"崇力", "毕曦", "辽文", "廉銮", "经霄", "杭蔚", "帮夕", "勇江", "骋琛", "悲映", "里灵",
			"湖睦", "西迟", "丙博", "聚星", "缓偌", "亮行", "海军", "稳辰", "醇舍", "祥解", "编民",
			"盛玄", "亮轮", "功嘉", "房堂", "深信", "羿鸿", "理武", "伊贵", "沛磊", "柏滨", "太愈",
			"禧儒", "磊石", "锦烨", "军鸿", "天尚", "赞伍", "舱革", "讯威", "繁瑜", "怀谦", "孝之",
			"境有", "踌麒", "启蜀", "洋波", "键天", "博沛", "庭行", "劝汐", "希廷", "联方", "帜运",
			"骋峥", "沧操", "孝煌", "飘帜", "变侨", "小钊", "珑营", "图奎", "朋牡", "运汝", "必攀",
			"全游", "双湖", "瑾霆", "根润", "喜锦", "资齐", "懂文", "庆火", "佳谦", "超薄", "键锵",
			"童祥", "原琪", "焕凌", "沛岳", "群腾", "勉均", "若维", "珑冰", "醇彬", "储釜", "慕修",
			"功龄", "办磊", "琰楚", "颢广", "扁可", "功勇", "慈上", "列准", "羚翘", "琰官", "仰汐",
			"境庚", "铿蒙", "荔敏", "伟金", "东裕", "羡谆", "戚献", "桦帜", "燃澄", "招众", "澄武",
			"巍龄", "逻欣", "翘辉", "竹裕", "辅理", "景冬", "咏冬", "潮宜", "贯贤", "钧桂", "涣波",
			"丛史", "兴颜", "生添", "良茁", "丛汉", "紊达", "自仁", "尉荷", "固榕", "帜夏", "才智",
			"翰兴", "磊裔", "沪宇", "屏君", "曼欢", "丞宪", "寒健", "逸舟", "代睿", "茗涛", "维学",
			"宪赐", "飘翘", "汐其", "季熊", "皆仆", "澜秀", "力鸣", "淮楷", "简江", "锦恒", "颂遥",
			"畅标", "依逸", "驹有", "纹北", "浩梦", "旭环", "羚裕", "剑封", "羡兴", "效山", "彪赋",
			"从任", "启榕", "泽悦", "旺麒", "开颖", "霖泰", "古瑞", "质时", "乐相", "汐侠", "儒杭",
			"管儿", "廉东", "余琛", "方争", "煜汀", "东廷", "务臣", "共茂", "波栋", "思水", "僚驰",
			"法权", "蔽滕", "舱钢", "党登", "亨渝", "熹坤", "弘登", "毕金", "开帅", "木锵", "稚向",
			"赋镜", "涣献", "瑞山", "笙升", "力勇", "华才", "励策", "博忠", "湃醒", "鼎搏", "斯曼",
			"青清", "万俊", "治跋", "冻宁", "亚樵", "帆昆", "河翔", "常叔", "欢洪", "霏应", "满强",
			"坚羿", "益量", "发良", "蜀博", "云钊", "爽琳", "辰迢", "大资", "凉云", "从纯", "起胜",
			"谡舟", "皓准", "东誉", "皆浩", "贡年", "结鹄", "笙夜", "有盼", "宙皋", "明夏", "昆琅",
			"羡成", "隆尉", "炯潮", "聚明", "西倌", "荔才", "遥江", "隆跋", "夕白", "木亭", "楠江",
			"焕泰", "冠泽", "明靖", "启建", "滕蔚", "萌栋", "先伊", "成旭", "史培", "达礼", "律之",
			"彦鸥", "昱幸", "京连", "舟晨", "铎博", "麦辉", "灼威", "春德", "康里", "贤稚", "替强",
			"堂资", "发延", "梦仔", "路池", "南焯", "灵东", "登义", "穗冰", "沪梦", "棠曙", "亮辰",
			"悦革", "进跋", "里吟", "义枫", "卫施", "广危", "环方", "励官", "发牡", "侠树", "冠甫",
			"登菁", "帜誉", "崇源", "慎毅", "潇夕", "章诞", "冰科", "斑汐", "从仲", "斯遥", "彦迅",
			"召晨", "乃维", "珑澄", "夕泉", "祥卓", "循睦", "柯霖", "柯庚", "鹄钢", "屏键", "善颖",
			"熙淮", "持立", "庚弘", "仲子", "骋淮", "书锐", "艾格", "范颖", "童华", "声应", "贵兴",
			"华征", "敞冶", "义晖", "佑偌", "露游", "选灼", "亨蒙", "混赫", "大锋", "望荔", "皆澄",
			"峰鹏", "露瑁", "华虎", "榕德", "舜尚", "奇宪", "促澄", "斑欢", "添涛", "键品", "鉴众",
			"任革", "渭丞", "焕郎", "毕桂", "福强", "祥哲", "凡臁", "俊濉", "楠吉", "豪麒", "游谆",
			"宏勤", "丁策", "轩察", "菘程", "津家", "翼霏", "朋彪", "努进", "鹤纯", "浪才", "峦佳",
			"毕希", "跋冲", "均耿", "经和", "感津", "浪驰", "感靖", "政进", "鹊育", "苞丹", "乔贞",
			"学珑", "孰颖", "菁孝", "冠菲", "斐汐", "朋环", "和家", "浩劲", "庸峰", "懂轮", "操嘉",
			"时岚", "南民", "树松", "列侨", "路栋", "埂爽", "隆厚", "瑞之", "政民", "富彬", "明愈",
			"世图", "遍超", "羽迢", "亚博", "朋迢", "汐澄", "熹齐", "羚拓", "湘培", "望言", "巩皓",
			"锡楷", "森冉", "力细", "翼匀", "禄贵", "致庚", "营南", "禄水", "召邦", "腾儿", "巍驹",
			"埂豪", "届斌", "坚静", "毅坤", "缓曙", "博伊", "悲荔", "肖克", "桂琳", "简欢", "栋修",
			"睫匀", "吟权", "盎震", "育世", "里兴", "程萌", "炳菁", "翰尉", "凛知", "篱时", "浩森",
			"晨发", "临腾", "述封", "顺努", "鑫维", "里博", "便庚", "辰洪", "剑霏", "菲醒", "力厦",
			"卉洪", "靠纯", "澄羿", "耙永", "混毕", "泽昌", "亚安", "昌早", "恒迟", "忠开", "拥炳",
			"涌富", "思锵", "友愈", "壮烈", "子珑", "晶会", "丘丁", "南如", "丛腾", "环棠", "和三",
			"枫榕", "廉博", "傲质", "票亨", "会贡", "友书", "谱更", "熊雄", "朗冉", "挺偌", "放丞",
			"坤睿", "添儒", "官臁", "任鹄", "怀埂", "辟熊", "欣庚", "莘冶", "修冬", "诺伟", "炎宝",
			"乃翔" };

	public static String[] getMaleNicks() {
		if (maleNicks == null) {
			maleNicks = new String[1];
			maleNicks[0] = "乐客";
		}
		return maleNicks;
	}

	/**
	 * @return 返回 flag。
	 */
	public static String getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            要设置的 flag。
	 */
	public static void setFlag(String flag) {
		Util.flag = flag;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取用户最后一次登录用户id
	 * @datetime:2007-3-20 11:23:15
	 * @param flag
	 * @return void
	 */
	public static int getLastLoginTimeByUserId(String mobile) {
		int userId = SqlUtil
				.getIntResult(
						"SELECT a.id FROM user_info a  left join user_status b on a.id=b.user_id where mobile='"
								+ mobile
								+ "' order by b.last_login_time desc limit 1",
						Constants.DBShortName);
		return userId;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取用户最后一次登录用户bean
	 * @datetime:2007-3-20 11:23:15
	 * @param flag
	 * @return void
	 */
	public static UserBean getLastLoginTimeByUserBean(String mobile) {
		UserBean user = getUserService().getLastLoginUser(mobile);
		return user;
	}
}
