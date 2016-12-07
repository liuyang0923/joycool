package net.joycool.wap.action.job.punch;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author zhouj
 * @explain： 打小强
 * @datetime:2007-9-1 10:10
 */
public class PunchAction extends CustomAction{
	
	public static String PUNCH_USER_KEY = "punch_user_key";

	PunchUserBean punchUser = null;
	
	static int MAX_ROOM = 3;
	static PunchWorld[] worlds = {new PunchWorld(), new PunchWorld(), new PunchWorld()};
	
	UserBean loginUser = null;
	
	public PunchAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
		punchUser = (PunchUserBean)session.getAttribute(PUNCH_USER_KEY);
		if(punchUser == null) {
			punchUser = new PunchUserBean();
			session.setAttribute(PUNCH_USER_KEY, punchUser);
		}
	}
	
	public void index() {
		
	}

	public void view() {
		if(request.getParameter("r") != null) {
			int room = getParameterInt("r");
			if(room >= 0 && room < MAX_ROOM)
				punchUser.setRoom(room);
		}

	}
	
	public void punch() {
		PunchWorld world = getWorld();
		int pos = getParameterInt("p");	// 打的第几个
		if(System.currentTimeMillis() - punchUser.getActionTime() < 2500) {		// 操作间隔
			tip(null, "动作太快了，歇会吧");
		} else {
			byte type = world.punch(pos);
			if(type < 0){
				tip(null);
			} else {
				punchUser.setActionTime(System.currentTimeMillis());
				String tip = null;
				if(type == 0) {
					tip = "什么都没打到，再接再厉吧";
				} else {
					int reward = world.getTypeMoney(type);
					
					tip = "恭喜您打中了" + world.getTypeName(type) + "，获得奖励" + reward + "乐币";
					
					UserInfoUtil.updateUserCash(loginUser.getId(), reward,
							UserCashAction.OTHERS, "打小强给用户增加" + reward + "乐币");
					if(type == 3)
						punchUser.addNum(0);
					else if(type == 4)
						punchUser.addNum(1);
				}
				tip("success", tip);
			}
		}
	}

	/**
	 * @return Returns the world.
	 */
	public PunchWorld getWorld() {
		return worlds[punchUser.getRoom()];
	}

	/**
	 * @return Returns the punchUser.
	 */
	public PunchUserBean getPunchUser() {
		return punchUser;
	}
}
