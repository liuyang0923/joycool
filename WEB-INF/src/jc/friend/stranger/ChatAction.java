package jc.friend.stranger;

import javax.servlet.http.*;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

public class ChatAction extends CustomAction {

	public static ChatService service = new ChatService();
	public static StrgBean strgbean = new StrgBean();

	public ChatAction(HttpServletRequest request, HttpServletResponse response) {
		super(request);
	}

	/**
	 * 记录聊天数据
	 * 
	 * @param uid
	 * @param strgid
	 * @param roomId
	 * @return
	 */
	public int instChats(int uid, int strgid, String roomId) {
		String cont = this.getParameterNoEnter("cont");
		if (cont != null && !"".equals(cont)
				&& "post".equalsIgnoreCase((request.getMethod()))) {
			if (this.isCooldown("chat", 3000)) {
				service
						.update("insert into chats (create_time,from_id,to_id,room_id,cont) values(now(),"
								+ uid
								+ ","
								+ strgid
								+ ",'"
								+ StringUtil.toSql(roomId)
								+ "','"
								+ StringUtil.toSql(cont) + "')");
				return 7;
			} else {
				return 5;
			}
		} else if (cont != null && cont.length() > 50)
			return 3;// 长度过长
		else if (cont == null)
			return 1;// 刚开始页面或者自动刷新页面
		else
			return 4;// 输入为空
	}

	/**
	 * 插入用户匹配角色
	 * 
	 * @param strgid
	 * @return
	 */
	public boolean instChater(int strgid) {
		UserBean ub = this.getLoginUser();
		int uid = 0;
		if (ub != null) {
			uid = ub.getId();
			String roomId = uid + "" + strgid;
			service
					.update("insert into chat (user_id,stranger_id,room_id,create_time)values("
							+ strgid
							+ ","
							+ uid
							+ ",'"
							+ StringUtil.toSql(roomId) + "',now())");
			return service
					.update("insert into chat (user_id,stranger_id,room_id,create_time)values("
							+ uid
							+ ","
							+ strgid
							+ ",'"
							+ StringUtil.toSql(roomId) + "',now())");
		}
		return false;
	}

	/**
	 * 更新聊天信息
	 * 
	 * @param roomId
	 * @return
	 */
	public boolean updateChat(String roomId) {
		return service
				.update("update chats set isshow=isshow+1 where room_id='"
						+ StringUtil.toSql(roomId) + "' and isshow<2");
	}

	/**
	 * 撤除用户匹配角色
	 * 
	 * @param uid
	 * @return
	 */
	public boolean deleteCht(int uid) {
		return service.update("delete from chat where user_id=" + uid);
	}

	/**
	 * 陌生人匹配
	 * 
	 * @param uid
	 * @return
	 */
	public static int getStranger(int uid) {
		int strgId = strgbean.getStrgId();
		synchronized (strgbean) {
			if (strgId == 0) {
				strgbean.setStrgId(uid);
				return 0;
			} else if (strgId == uid)
				return 0;
			else {
				strgbean.setStrgId(0);
				return strgId;
			}
		}
	}

	/**
	 * 对方是否在线
	 * 
	 * @param strgid
	 * @return
	 */
	public boolean isOnline(int strgid) {
		// if(OnlineUtil.getOnlineBean(String.valueOf(strgid)) != null)
		// return true;
		// else
		return false;
	}

	/**
	 * 清空上次发言框中的内容
	 * 
	 * @return
	 */
	public int GetSign() {
		int sign = this.getParameterInt("sign");
		int roll = RandomUtil.nextInt(99999);
		while (roll == sign) {
			roll = RandomUtil.nextInt(99999);
		}
		return roll;
	}

}
