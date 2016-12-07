package net.joycool.wap.action.job;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.job.SpiritBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.UserInfoUtil;

public class SpiritAction {

	public static int DEALFAILURE = 3;

	public static int WRONGNUM = 2;

	public static int DEALOK = 1;

	private IUserService userService = ServiceFactory.createUserService();

	private IJobService jobService = ServiceFactory.createJobService();

	UserBean loginUser;

	public SpiritAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
	}



	/**
	 * 检测用户是否有足够的乐币进行按键
	 * 
	 * @param weaponId
	 * @return
	 */
	public String checkUserPoint(int spiritId) {
		int spiritPrice = getSpirit(spiritId).getPrice();
		if (!haveEnoughpoint(spiritPrice)) {
			// 乐币不足
			return "您的乐币不足以进行操作！";
		}
		return "";
	}

	/**
	 * 判断用户的钱是否足够
	 * 
	 * @param money
	 * @return true or false
	 */
	public boolean haveEnoughpoint(int point) {
	
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());

		int currentpoint = usb.getGamePoint();
		if (currentpoint >= point) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 扣用户乐币从user_status并增加反倭值
	 * 
	 * @param money
	 */
	public void deductUserpoint(int point,int spirit) {
		
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
	
		int setpoint = 0;
		int setspirit=0;
		int currentspirit=usb.getSpirit();
		int currentpoint = usb.getGamePoint();

		if (haveEnoughpoint(point)) {
			setpoint = currentpoint - point;
			setspirit=currentspirit+spirit;
	
		}
	
		UserInfoUtil.updateUserStatus("game_point= "+ setpoint+",spirit="+setspirit, "user_id="
				+ loginUser.getId(), loginUser.getId(), UserCashAction.GAME, "静国神社扣钱"+point);

	
	}


public void deductUserpoint(int point) {
		
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
	
		int setpoint = 0;
			
		int currentpoint = usb.getGamePoint();
		
		int num = currentpoint;
		if (haveEnoughpoint(point)) {
			setpoint = currentpoint - point;
			num = point;
			
		}
	
		UserInfoUtil.updateUserStatus("game_point= "+ setpoint+",spirit=0", "user_id="
				+ loginUser.getId(), loginUser.getId(), UserCashAction.GAME, "静国神社扣钱"+point);

	
	}



	/**
	 * 通过spiritId 取得 spirit
	 * 
	 * @param spiritId
	 * @return
	 */
	public  SpiritBean getSpirit(int spiritId) {
		SpiritBean spirit=null;
		spirit=jobService.getSpirit("id="+spiritId);
	
		return spirit;

	}

	
	public  Vector getSpirit() {
	Vector vecSpirit=null;
	vecSpirit=jobService.getSpiritList("1=1");
	return vecSpirit;
	
	}




}
