/**
 * 
 */
package net.joycool.wap.action.jcadmin;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.jcadmin.UserCashBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserCashService;
import net.joycool.wap.util.Constants;

/**
 * @author Administrator zhul 2006-10-08 关于用户乐币流动状况的类
 * 
 */
public class UserCashAction {

	// 用户乐币流动分类
	// 游戏
	public static final int GAME = 1;

	// 赌博
	public static final int WAGER = 2;

	// 银行
	public static final int BANK = 3;

	// 赠送
	public static final int PRESENT = 4;

	// 机会卡
	public static final int CARD = 5;

	// 打猎
	public static final int HUNT = 6;

	// liuyi 20070102 股票修改 start
	public static final int STOCK = 7;

	// 其他
	public static final int OTHERS = 0;

	public static IUserCashService usercashService = ServiceFactory
			.createUserCashService();

	UserBean loginUser;

	// session=null;

	public UserCashAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
	}

	// 按ID查询
	public Vector getUserCash(int uid) {

		Vector vecUserCash = null;
		vecUserCash = usercashService.getUserCashList("user_id= " + uid
				+ " order by create_datetime desc");
		// request.getse

		return vecUserCash;
	}

	// 按ID和项目
	public Vector getUserCash(int uid, int type) {
		Vector vecUserCash = null;
		vecUserCash = usercashService.getUserCashList("user_id= " + uid
				+ " and type = " + type + " order by create_datetime desc");
		return vecUserCash;
	}

	// 写入用户现金流信息
	public boolean addUserCash(int uid, int type, String reason) {
		boolean result = false;
		UserCashBean usercash = new UserCashBean();
		usercash.setReason(reason);
		usercash.setType(type);
		usercash.setUserId(uid);
		result = usercashService.addUserCash(usercash);
		return result;
	}

}
