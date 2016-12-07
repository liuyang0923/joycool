package jc.guest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

//import jc.user.UserAction2;
//import jc.user.UserBean2;

import net.joycool.wap.cache.LinkedHashMap2;
import net.joycool.wap.framework.CustomAction;
//import net.joycool.wap.util.SqlUtil;

public class GuestAction extends CustomAction{
	
	public static GuestService service = new GuestService();
	public static byte[] initLock = new byte[0];
	public static byte[] initLock2 = new byte[0];
	public static LinkedHashMap guestMap = new LinkedHashMap();					// 游客列表
	public static LinkedList chatList = new LinkedList();							// 聊天记录
	public static LinkedList changedUserList = new LinkedList();					// 刚修改昵称的用户列表
	public static LinkedHashMap2 chatUserList = new LinkedHashMap2(500, 1000); 	// 聊天用户列表
	public static List chatActionList = null;										// 动作列表
	public static int MAX_COUNT = 500;												// 
	public static int MAX_RECORD_COUNT = 1000;										// 聊天记录上限(测试时限制10条)
	
	public GuestAction(){}
	
	public GuestAction(HttpServletRequest request){
		super(request);
		addChatActionList();
	}
	
	public static LinkedList getChangedUserList() {
		return changedUserList;
	}

	public static void setChangedUserList(LinkedList changedUserList) {
		GuestAction.changedUserList = changedUserList;
	}
	
	public static void clearChangedUserList(){
		changedUserList.clear();
	}
	
	public static void clearGuestMap(){
		guestMap.clear();
	}
	public static GuestService getService() {
		return service;
	}

	public static void setService(GuestService service) {
		GuestAction.service = service;
	}

	public static List getChatUserList() {
		return new ArrayList(chatUserList.keySet());
	}
	
	public static LinkedHashMap getGuestMap() {
		return guestMap;
	}

	public static LinkedList getChatList() {
		return chatList;
	}

	public static void setChatList(LinkedList chatList) {
		GuestAction.chatList = chatList;
	}

	public static List getChatActionList() {
		return chatActionList;
	}

	public static void setChatActionList(List chatActionList) {
		GuestAction.chatActionList = chatActionList;
	}

//	// 取得游客ID
//	public Guest getGuestBean(){
//		Guest guest = null;
//		HttpSession session = request.getSession(false);
//		if (session.getAttribute("guestId") != null){
//			String guestId = session.getAttribute("guestId").toString();
//			guest = (Guest)guestMap.get(new Integer(guestId));
//		}
//		return guest;
//	}
	
	// 取得一位游客
	public GuestUserInfo getGuestById(int guestId){
		return (GuestUserInfo)guestMap.get(new Integer(guestId));
	}
//	public static int guestId = 100;
//	public static int guestIdMax = 9990;
//	// 创建一位游客
//	public Guest createGuest(){
//		Guest guest = new Guest();
////		UserAction2 action2 = new UserAction2(request);
////		UserBean2 usera = action2.getLoginUser2();
//		synchronized (initLock2){
//		    guest.setId(guestId++);
//		    if(guestId > guestIdMax)
//		    	guestId = 100;
//		    guest.setUid(this.getLoginUser()==null?0:this.getLoginUser().getId());
////		    if (usera==null){
//		    	guest.setNickName("游客" + guest.getId());
////		    } else {
////		    	guest.setNickName(usera.getNickName());
////		    }
//		    guest.setGender(3);
//		    guest.setCreateTime(System.currentTimeMillis());
//		    guestMap.put(new Integer(guest.getId()), guest);
//		    session.setAttribute("guestId", new Integer(guest.getId()));
//		}
//		return guest;
//	}
	
	/**
	 * 一游客进入聊天室
	 * @param guestUser
	 */
	public void enter(GuestUserInfo guestUser){
		if (guestUser == null){
			return;
		}
		boolean isContains = guestMap.containsKey((new Integer(guestUser.getId())));
		if (!isContains){
			// 不存在，放入map中
			guestMap.put(new Integer(guestUser.getId()), guestUser);
		}
	}
	
//	// 注册游客
//	public boolean register(Guest guest){
//		if (guest == null){
//			return false;
//		}
//		if (service.getGuest(" phone='" + guest.getPhone() + "'") != null){
//			request.setAttribute("tip", "此号码已被注册,请更换号码.");
//			return false;
//		}
//		guest.setDbId(service.addGuest(guest));
////		delUser(guest);
////		addUser(guest);
//		return true;
//	}
//	
//	// 游客登陆
//	public boolean login(Guest guest){
//		Guest guest2 = service.getGuest(" phone='" + guest.getPhone() + "' and password='" + guest.getPassword() + "'");
//		if (guest == null){
//			request.setAttribute("tip", "用户不存在.");
//			return false;
//		} else {
//			if (guest2 == null){
//				request.setAttribute("tip", "手机号或密码错误.");
//				return false;
//			}
//			guest2.setDbId(guest2.getId());
//			guest2.setId(guest.getId());
////			delUser(guest2);
//			addUser(guest2);
////			guestMap.remove(new Integer(guest2.getId()));
//			guestMap.put(new Integer(guest2.getId()), guest2);
//		}
//		return true;
//	}
	
	public static List addChatActionList() {
		if (chatActionList != null){
			return chatActionList;
		}
		synchronized (initLock){
			if (chatActionList != null){
				return chatActionList;
			}
			// 载入游客聊天动作
			chatActionList = service.getChatActionList("1");
		}
		return chatActionList;
	}
	
	// 将某一游客加入用户列表
	public boolean addUser(GuestUserInfo guestUser){
		synchronized(chatUserList) {
			if (guestUser != null){
				chatUserList.put(new Integer(guestUser.getId()), null);
			}
		}
		return true;
	}
	
	// 将某位游客清除出列表
	public void delUser(GuestUserInfo guestUser){
		chatUserList.remove(new Integer(guestUser.getId()));
	}
	
	// 将一刚改名的游客加入列表
	/**
	 * ???有问题???
	 */
	public void addToChangedList(int guestId){
		changedUserList.remove(new Integer(guestId));
		if (changedUserList.size() > MAX_COUNT){
			changedUserList.remove(changedUserList.get(0));
		} 
		changedUserList.add(new Integer(guestId));
	}
	
//	public List getChatUserList(){
//		List list = new ArrayList();
//		Iterator iter = chatUserMap.entrySet().iterator();
//		while(iter.hasNext()){
//			Map.Entry entry = (Map.Entry) iter.next(); 
//			Integer key = (Integer)entry.getKey();
//			list.add(key);
//			if (list.size() > 100){
//				break;
//			}
//		}
//		return list;
//	}
	
	// 游客聊天
	public String doGuestChat(GuestUserInfo guestUser1,GuestUserInfo guestUser2,ChatAction ca){
		if (guestUser1 == null || guestUser2 == null || ca == null){
			return "";
		}
		GuestChat chat = new GuestChat();
		String content = ca.getContent();
		content = content.replace("$N", guestUser1.getUserName2());
		content=content.replace("$n", guestUser2.getUserName2());
		chat.setGid1(guestUser1.getId());
		chat.setNcName1(guestUser1.getUserName2());
		chat.setGid2(guestUser2.getId());
		chat.setNcName2(guestUser2.getUserName2());
		chat.setContent(content);
		chat.setCreateTime(System.currentTimeMillis());
		chatList.add(chat);
		// 如果到了上限,则把第一个删除掉
		if (chatList.size() > MAX_RECORD_COUNT){
			chatList.remove(0);
		}
	    this.doPrivChat(chat);
		return content;
	}
	
	// 退出聊天室
	public void exitHall(GuestUserInfo guestUser){
		if (guestUser != null){
			chatUserList.remove(new Integer(guestUser.getId()));
		}
	}
	
	// 私聊,写入数据库
	public boolean doPrivChat(GuestChat chat){
		if (chat == null){
			return false;
		}
		Integer key = new Integer(chat.getGid1());
		synchronized(chatUserList) {
			chatUserList.remove(key);
			chatUserList.put(key, null);
		}
		service.addChat(chat);
		return false;
	}
	
	// 从聊天列表中清除某人
	public void delFromChatUserList(int guestId){
		chatUserList.remove(new Integer(guestId));
	}
	
	// 清空聊天列表
	public void clearChatUserList(){
		chatUserList.clear();
	}
}