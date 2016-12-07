package net.joycool.wap.action.bank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForward;

import jc.util.VerifyUtil;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.bank.LoadBean;
import net.joycool.wap.bean.bank.MoneyLogBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.jcadmin.UserCashBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.UserbagItemServiceImpl;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.admin.AdminAction;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbUtil;

public class BankAction {

	public final static String BANK_PW = "bankPW";

	private static IBankService bankService = ServiceFactory
			.createBankService();

	private static IUserService userService = ServiceFactory
			.createUserService();

	private static HashMap rankLoadMoneyMap;

	UserBean loginUser;
	static UserbagItemServiceImpl serviceImpl =new UserbagItemServiceImpl();

	public BankAction() {
	}

	public BankAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
	}

	/**
	 * 作者:马长青 日期:2006-8-4 详细判断用户是否可以贷款方法
	 * 
	 * @param request
	 */
	public void loan(HttpServletRequest request) {
		HttpSession session = request.getSession();
		// 获取用户存款
		long money = this.getUserStore();
		// 判断是否有存款
		if (money > 0) {
			request.setAttribute("result", "saving");
			request.setAttribute("money", String.valueOf(money));
			return;
		}
		// 判断用户是否有贷款
		boolean flag = this.userHaveLoad();
		if (flag) {
			// 用户当前的贷款
			long exitLoan = this.getUserLoadMoney();
			// 用户实际可以贷款的金额
			long isCanLoan = this.getUserCanLoadMoney();
			// 判断用户可贷款金额为负的时候让可贷款金额变为零
			if (isCanLoan < 0) {
				isCanLoan = 0;
			}
			request.setAttribute("result", "existLoan");
			request.setAttribute("exitLoan", String.valueOf(exitLoan));
			request.setAttribute("isCanLoan", String.valueOf(isCanLoan));
			session.setAttribute("success", "loan");
			return;
		}
		// 用户没有贷款
		// 用户实际可以贷款的金额
		long canLoan = this.getUserCanLoadMoney();
		request.setAttribute("CanLoan", String.valueOf(canLoan));
		session.setAttribute("success", "loan");
		return;
	}

	/**
	 * 作者:马长青 日期:2006-8-4 详细处理用户贷款方法
	 * 
	 * @param request
	 */
	public void loanResult(HttpServletRequest request) {
		// 获取用户申请贷款金额和贷款时间
		String money = request.getParameter("money");
		String hour = request.getParameter("hour");
		// 转换数值型
		int number = StringUtil.toInt(money);
		int time = StringUtil.toInt(hour);
		// 判断用户输入数据是否合法
		if (number <= 0 || time <= 0 || time > 72) {
			request.setAttribute("result", "input");
			request.setAttribute("money", money);
			request.setAttribute("hour", hour);
			return;
		}
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end
		// 判断用户输入贷款金额是否超过一亿
		// if (loginUser.getUs().getGamePoint() > 100000000) {
		if (usb.getGamePoint() > 100000000) {
			request.setAttribute("result", "overstep");
			return;
		}
		// 判断用户时候有存款
		long saving = this.getUserStore();
		if (saving > 0) {
			request.setAttribute("result", "saving");
			return;
		}
		// 判断用户是否有贷款
		boolean flag = this.userHaveLoad();
		if (flag) {
			// 获取判断用户金额是否正确
			int mark = this.isCanLoadSomeMoney(Integer.parseInt(money));
			// 贷款金额大于实际能贷款金额
			if (mark == Constants.GERATERTHAN) {
				// 得到用户实际可贷款的最大金额
				long isCanLoan = this.getUserCanLoadMoney();
				// 判断用户可贷款金额为负的时候让可贷款金额变为零
				if (isCanLoan < 0) {
					isCanLoan = 0;
				}
				request.setAttribute("result", "failure");
				request.setAttribute("mark", String.valueOf(mark));
				request.setAttribute("money", money);
				request.setAttribute("isCanLoan", String.valueOf(isCanLoan));
				request.setAttribute("hour", hour);
				return;
			}// 贷款金额小于实际能贷款最小金额
			else if (mark == Constants.LESSTHAN) {
				// 获取用户当前等级
				// Integer rank = new Integer(loginUser.getUs().getRank());
				Integer rank = new Integer(usb.getRank());
				// 对用用户等级所能贷款额度
				long canLoadMoney = ((Long) getRankLoadMoneyMap().get(rank))
						.longValue();
				request.setAttribute("result", "failure");
				request.setAttribute("mark", String.valueOf(mark));
				request.setAttribute("money", money);
				request.setAttribute("canLoadMoney", String
						.valueOf(canLoadMoney));
				request.setAttribute("hour", hour);
				return;
			}// 当用户实际可贷款金额小对应该用户等级贷款金额最小值时,一次待出所有剩余可贷款金额
			else if (mark == Constants.LESSTHANTURE) {
				// 得到用户实际可贷款的最大金额
				money = String.valueOf(this.getUserCanLoadMoney());
				// 添加贷款记录
				flag = this.doAddLoad(Long.parseLong(money), Integer
						.parseInt(hour));
				if (flag) {
					// 更新用户乐币
					// userService.updateUserStatus("game_point=game_point+"
					// + Long.parseLong(money), "user_id="
					// + loginUser.getId());
					// zhul_2006-08-11 modify userstatus start
					UserInfoUtil.updateUserCash(loginUser.getId(), Long.parseLong(money),
							UserCashAction.BANK, "贷款" + Long.parseLong(money)
									+ "乐币.");
					// zhul_2006-08-11 modify userstatus end

					// 更新session中的值
					// int point = (int) (loginUser.getUs().getGamePoint() +
					// Long
					// .parseLong(money));
//					int point = (int) (usb.getGamePoint() + Long
//							.parseLong(money));
					// loginUser.getUs().setGamePoint(point);
					if (Integer.parseInt(hour) == 1) {
						// 给用户发通知
						NoticeBean notice = new NoticeBean();
						notice.setUserId(loginUser.getId());
						notice.setTitle("您的贷款1小时后到期，注意及时归还！");
						notice.setContent("");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setHideUrl("");
						notice.setLink("/bank/accountQuery.jsp");
						NoticeUtil.getNoticeService().addNotice(notice);
					}
				}
				// 取得所有贷款列表
				Vector loadMoneyList = this.getUserLoadMoneyList();
				// 传值到页面
				request.setAttribute("loadMoneyList", loadMoneyList);
				return;
			}// 正常
			else {
			}
		}
		// 用户没有贷款
		// 取得用户等级
		// Integer rank = new Integer(loginUser.getUs().getRank());
		Integer rank = new Integer(usb.getRank());
		// 取得用户等级对应的贷款额度
		long canLoadMoney = ((Long) getRankLoadMoneyMap().get(rank))
				.longValue();
		// 判断用户输入贷款金额是否小于该用户申请贷款额度的最小值
		if ((long) canLoadMoney < number) {
			long isCanLoan = this.getUserCanLoadMoney();
			request.setAttribute("result", "failure");
			request.setAttribute("mark", String.valueOf(Constants.GERATERTHAN));
			request.setAttribute("money", money);
			request.setAttribute("isCanLoan", String.valueOf(isCanLoan));
			request.setAttribute("hour", hour);
			return;
		}// 判断用户输入贷款金额是否大于该用户申请贷款额度的最大值
		else if ((long) (canLoadMoney * 0.1) > number) {
			request.setAttribute("result", "failure");
			request.setAttribute("mark", String.valueOf(Constants.LESSTHAN));
			request.setAttribute("money", money);
			request.setAttribute("canLoadMoney", String.valueOf(canLoadMoney));
			request.setAttribute("hour", hour);
			return;
		}// 正常状态贷款
		else {
			flag = this
					.doAddLoad(Long.parseLong(money), Integer.parseInt(hour));
			if (flag) {
				// userService
				// .updateUserStatus("game_point=game_point+"
				// + Long.parseLong(money), "user_id="
				// + loginUser.getId());
				// zhul_2006-08-11 modify userstatus start
				UserInfoUtil.updateUserCash(loginUser.getId(), Long.parseLong(money),
						UserCashAction.BANK, "贷款" + Long.parseLong(money)
								+ "乐币.");
				// zhul_2006-08-11 modify userstatus end

				// int point = (int) (loginUser.getUs().getGamePoint() + Long
				// .parseLong(money));
//				int point = (int) (usb.getGamePoint() + Long.parseLong(money));
				// loginUser.getUs().setGamePoint(point);
				if (Integer.parseInt(hour) == 1) {
					// 给用户发通知
					NoticeBean notice = new NoticeBean();
					notice.setUserId(loginUser.getId());
					notice.setTitle("您的贷款1小时后到期，注意及时归还！");
					notice.setContent("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setHideUrl("");
					notice.setLink("/bank/accountQuery.jsp");
					NoticeUtil.getNoticeService().addNotice(notice);
				}
			}
			Vector loadMoneyList = this.getUserLoadMoneyList();
			request.setAttribute("loadMoneyList", loadMoneyList);
			return;
		}
	}

	/**
	 * 作者:马长青 日期:2006-8-4 计算贷款到期时间
	 * 
	 * @return
	 */
	public String getLeaveTime(String datetime, String expireTime) {
		String result = null;
		try {
			// 解析当前时间
			Date dt1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
					.parse(datetime);
			// 解析贷款到期时间
			Date dt2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
					.parse(expireTime);
			// 获得当前时间与到期时间的差值
			long seconds = (dt2.getTime() - dt1.getTime()) / 1000;
			// 转换相差的天数
			long date = seconds / (24 * 60 * 60);
			// 转换相差的小时数
			long hour = (seconds - date * 24 * 60 * 60) / (60 * 60);
			// 转换相差的分钟数
			long minut = (seconds - date * 24 * 60 * 60 - hour * 60 * 60) / (60);
			// 转换相差的秒数
			long second = (seconds - date * 24 * 60 * 60 - hour * 60 * 60 - minut * 60);
			// 判断显示时间
			if (minut < 0 || second < 0) {
				result = "已过期";
			} else if (date > 0) {
				result = (date * 24 + hour) + "小时";
			} else if (hour <= 0) {
				result = minut + "分";
			} else if (minut < 0) {
				result = second + "秒";
			} else {
				result = hour + "小时";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回时间
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public String getLoadAccrual(double number) {
		BigDecimal b = new BigDecimal(number);
		number = b.setScale(4, BigDecimal.ROUND_DOWN).doubleValue();
		DecimalFormat df = new DecimalFormat("#,##0.###");
		String cstr = df.format(number);
		return cstr;
	}

	/**
	 * 
	 * @return
	 */
	public UserBean getLoginUser() {
		return loginUser;
	}

	/**
	 * 等级-贷款额度map key：rank id value load_limit
	 * 
	 * @return
	 */
	public static HashMap getRankLoadMoneyMap() {
		if (rankLoadMoneyMap != null) {
			return rankLoadMoneyMap;
		}

		rankLoadMoneyMap = bankService.getRankLoadMoneyMap();

		return rankLoadMoneyMap;
	}

	public void clearRankLoadMoneyMap() {
//		this.rankLoadMoneyMap = null;
	}

	/**
	 * 获得用户存款值，0为没有存款
	 * 
	 * @return
	 */
	public long getUserStore() {
		long storeMoney = 0;
		// macq_2006-11-27_添加存款缓存_strat
		StoreBean storeBean = BankCacheUtil
				.getBankStoreCache(loginUser.getId());
		// StoreBean storeBean = bankService.getStore("user_id="
		// + loginUser.getId());
		// macq_2006-11-27_添加存款缓存_end
		if (storeBean != null) {
			// 有存款
			storeMoney = storeBean.getMoney();
		}
		return storeMoney;
	}

	/**
	 * 用户是否有贷款，true为有贷款
	 * 
	 * @return
	 */
	public boolean userHaveLoad() {
		boolean result = false;
		// 不包括过期的贷款
		// int loadNum = bankService.getLoadCount("user_id=" + loginUser.getId()
		// + " and expire_time > NOW()");
		// 包括过期的贷款
		int loadNum = bankService.getLoadCount("user_id=" + loginUser.getId());
		if (loadNum != 0) {
			// 有贷款
			result = true;
		}
		return result;
	}

	/**
	 * 获得用户所有贷款的值
	 * 
	 * @return
	 */
	public long getUserLoadMoney() {

		long loadMoney = 0;
		// 不包括过期的贷款
		// Vector loadMoneyList = bankService.getLoadList("user_id="
		// + loginUser.getId() + " and expire_time > NOW()");
		// 包括过期的贷款
		Vector loadMoneyList = bankService.getLoadList("user_id="
				+ loginUser.getId());
		if (loadMoneyList.size() != 0) {
			// 有存款
			Iterator iterator = (Iterator) loadMoneyList.iterator();
			while (iterator.hasNext()) {
				loadMoney += ((LoadBean) iterator.next()).getMoney();
			}
		}
		return loadMoney;
	}

	/**
	 * 获得用户所有贷款列表
	 * 
	 * @return
	 */
	public Vector getUserLoadMoneyList() {
		// 不包括过期的贷款
		// Vector loadMoneyList = bankService.getLoadList("user_id="
		// + loginUser.getId() + " and expire_time > NOW()");
		// 包括过期的贷款
		Vector loadMoneyList = bankService.getLoadList("user_id="
				+ loginUser.getId());
		if (loadMoneyList.size() != 0) {
			return loadMoneyList;
		} else {
			return null;
		}
	}

	/**
	 * 获得用户能够贷款的值
	 * 
	 * @return
	 */
	public long getUserCanLoadMoney() {
		long canLoadMoney = 0;
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end
		// Integer rank = new Integer(loginUser.getUs().getRank());
		// macq_如果用户等级大于45就默认等级等于45_2007-3-2_start
		int rankBase = usb.getRank();
		if (rankBase > 45) {
			rankBase = 45;
		}
		// macq_如果用户等级大于45就默认等级等于45_2007-3-2_end
		Integer rank = new Integer(rankBase);
		// 等级-贷款额度
		canLoadMoney = ((Long) getRankLoadMoneyMap().get(rank)).longValue();
		if (userHaveLoad()) {
			// 有贷款
			canLoadMoney = ((Long) getRankLoadMoneyMap().get(rank)).longValue();
			if (userHaveLoad()) {
				// 有贷款
				canLoadMoney -= getUserLoadMoney();
			}
		}
		return canLoadMoney;
	}

	/**
	 * 当前用户是否能贷到给定数目的款
	 * 
	 * @param inputValue
	 * @return
	 */
	public int isCanLoadSomeMoney(int inputValue) {
		int result = Constants.LOANTRUE;
		// 判断用户输入贷款金额是否大于可以贷款的额度
		if (getUserCanLoadMoney() < inputValue) {
			return result = Constants.GERATERTHAN;
		}
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		// 取得用户等级
		// Integer rank = new Integer(loginUser.getUs().getRank());
		Integer rank = new Integer(usb.getRank());
		// 取得用户等级对应的贷款额度
		long canLoadMoney = ((Long) getRankLoadMoneyMap().get(rank))
				.longValue();
		// 判断用户输入贷款金额是否小于该用户申请贷款额度的最小值
		if ((long) (canLoadMoney * 0.1) > inputValue) {
			// 判断用户实际可贷款金额小于该用户申请贷款额度的最小值
			if (getUserCanLoadMoney() < canLoadMoney * 0.1) {
				return result = Constants.LESSTHANTURE;
			}// 判断用户输入贷款金额是否小于该用户申请贷款额度的最小值
			else {
				return result = Constants.LESSTHAN;
			}
		}
		// 可以贷款
		return result;
	}

	/**
	 * 当前用户的贷款利息比率
	 * 
	 * @return
	 */
	public double getUserCurrentLoadAccrual() {
		// 初始贷款利息比率
		double accrual = Constants.BANK_LOAD_ACCRUAL;
		if (userHaveLoad()) {
			// 有贷款
			int loadDays = bankService.getLoadHour("user_id="
					+ loginUser.getId() + " order by create_time desc");
			if (loadDays != 0) {
				switch (loadDays) {
				case Constants.BANK_LOAD_1_DAYS:
					accrual = Constants.BANK_LOAD_1_DAYS_ACCRUAL;
					break;
				case Constants.BANK_LOAD_2_DAYS:
					accrual = Constants.BANK_LOAD_2_DAYS_ACCRUAL;
					break;
				case Constants.BANK_LOAD_3_DAYS:
					accrual = Constants.BANK_LOAD_3_DAYS_ACCRUAL;
					break;
				case Constants.BANK_LOAD_4_DAYS:
					accrual = Constants.BANK_LOAD_4_DAYS_ACCRUAL;
					break;
				case Constants.BANK_LOAD_5_DAYS:
					accrual = Constants.BANK_LOAD_5_DAYS_ACCRUAL;
					break;
				case Constants.BANK_LOAD_6_DAYS:
					accrual = Constants.BANK_LOAD_6_DAYS_ACCRUAL;
					break;
				case Constants.BANK_LOAD_7_DAYS:
					accrual = Constants.BANK_LOAD_7_DAYS_ACCRUAL;
					break;
				case Constants.BANK_LOAD_8_DAYS:
					accrual = Constants.BANK_LOAD_8_DAYS_ACCRUAL;
					break;
				case Constants.BANK_LOAD_9_DAYS:
					accrual = Constants.BANK_LOAD_9_DAYS_ACCRUAL;
					break;
				case Constants.BANK_LOAD_10_DAYS:
					accrual = Constants.BANK_LOAD_10_DAYS_ACCRUAL;
					break;
				default:
					accrual = Constants.BANK_LOAD_LAST_DAYS_ACCRUAL;
				}
			}
		}

		return accrual * 0.01;
	}

	/**
	 * 当前用户的存款利息比率
	 * 
	 * @return
	 */
	public double getUserCurrentStoreAccrual() {
		return Constants.BANK_STORE_ACCRUAL * 0.01;
	}

	public boolean doAddLoad(long money, int hours) {
		boolean result = false;

		LoadBean loadBean = new LoadBean();
		loadBean.setUserId(loginUser.getId());
		loadBean.setMoney(money);
		loadBean.setDelayTime(hours);

		MoneyLogBean moneyLogBean = new MoneyLogBean();
		moneyLogBean.setUserId(loginUser.getId());
		moneyLogBean.setMoney(money);
		moneyLogBean.setType(Constants.BANK_LOAD_TYPE);

		result = bankService.addLoad(loadBean, moneyLogBean);
		return result;
	}

	/**
	 * 
	 * @author macq
	 * @explain：增加银行操作日志
	 * @datetime:2007-8-29 11:12:50
	 * @param bean
	 * @return
	 * @return boolean
	 */
	public static boolean addMoneyLog(int userId, int rUserId, long money,
			int type) {
		MoneyLogBean bean = new MoneyLogBean();
		bean.setUserId(userId);
		bean.setRUserId(rUserId);
		bean.setMoney(money);
		bean.setType(type);
		return bankService.addMoneyLog(bean);
	}

	/**
	 * fanys 2006-08-03 扣钱
	 * 
	 * 每隔一小时调用一次该方法
	 */
	public static void deduct() {
		UserStatusBean userStatus;
		LoadBean load = null;
		MoneyLogBean log = new MoneyLogBean();
		String info = null;
		Vector loadList = bankService.getLoadList(" now()>expire_time");
		for (int i = 0; i < loadList.size(); i++) {
			load = (LoadBean) loadList.get(i);
			// userStatus = userService.getUserStatus("user_id="
			// + load.getUserId());

			userStatus = UserInfoUtil.getUserStatus(load.getUserId());
			if (userStatus == null)
				continue;
			// 如果用户乐币数大于贷款数,强行扣钱,同时删除用户贷款信息,增加一条还款记录
			if (userStatus.getGamePoint() > load.getMoney()) {

				// userService.updateUserStatus("game_point=game_point-"
				// + load.getMoney(), "user_id=" + load.getUserId());
				// zhul_2006-08-11 modify userstatus start
				UserInfoUtil.updateUserCash(load.getUserId(), -load.getMoney(), UserCashAction.BANK,
						"用户贷款到期，用户乐币数大于贷款数,强行还贷" + load.getMoney() + "乐币");
				// zhul_2006-08-11 modify userstatus end
				// 把用户加入到变更用户信息队列里面
				// UserInfoUtil.addUserInfo(userStatus.getUserId());
				log.setUserId(load.getUserId());
				log.setMoney(load.getMoney());
				log.setType(Constants.BANK_RETURN_TYPE);
				bankService.deleteLoad(load, log);
				info = " 您因拖欠贷款现扣本息" + load.getMoney();
				noticeUser(load.getUserId(), info);
			} else {
				bankService.deleteLoad("id=" + load.getId());
				degrade(userStatus, load);
			}
		}
	}

	/**
	 * fanys 2006-08-03 降级
	 * 
	 * @param userStatus
	 * @param load
	 */
	public static void degrade(UserStatusBean userStatus, LoadBean load) {
		int rank = 0;
		String info = null;
		rank = userStatus.getRank();
		if (rank > 0) {// 降级
			rank--;
			info = " 您因拖欠贷款降1级";
			// userService.updateUserStatus("rank=" + rank, "user_id="
			// + userStatus.getUserId());
			// zhul_modify_us 2006-08-14 start
			UserInfoUtil.updateUserStatus("rank=" + rank, "user_id="
					+ userStatus.getUserId(), userStatus.getUserId(),
					UserCashAction.BANK, "您因拖欠贷款降1级");
			// zhul_modify_us 2006-08-14 end

			// 把用户加入到变更用户信息队列里面
			// UserInfoUtil.addUserInfo(userStatus.getUserId());
		} else if (rank == 0) {// 经验值清零
			info = "您因拖欠贷款被清空经验值";
			// userService.updateUserStatus("point=0", "user_id="
			// + userStatus.getUserId());
			// zhul_modify_us 2006-08-14 start
			UserInfoUtil.updateUserStatus("point=0", "user_id="
					+ userStatus.getUserId(), userStatus.getUserId(),
					UserCashAction.BANK, "您因拖欠贷款被清空经验值");
			// zhul_modify_us 2006-08-14 end

			// 把用户加入到变更用户信息队列里面
			// UserInfoUtil.addUserInfo(userStatus.getUserId());

		}
		noticeUser(userStatus.getUserId(), info);

	}

	/**
	 * 到期没还钱时通知用户
	 * 
	 * @param userId
	 * @param title
	 */
	public static void noticeUser(int userId, String title) {

		// 给用户发通知
		NoticeBean notice = new NoticeBean();
		notice.setUserId(userId);
		notice.setTitle(title);
		notice.setContent("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setHideUrl("");
		notice.setLink("/user/userInfo.jsp");
		NoticeUtil.getNoticeService().addNotice(notice);
	}

	/**
	 * 获得用户贷款单条记录 通过user_id
	 * 
	 * @param condition
	 * @return
	 */
	public LoadBean getLoad(int id) {
		return bankService.getLoad("id=" + id);
	}

	/**
	 * 检测用户是否有足够的乐币还款 true为可以还款
	 * 
	 * @param inputValue
	 * @return
	 */
	public boolean isCanReturnLoadMoney(int inputValue) {
		boolean result = false;
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end
		// if (loginUser.getUs().getGamePoint() >= inputValue) {
		if (usb.getGamePoint() >= inputValue) {
			result = true;
		}
		return result;
	}

	/**
	 * 还款
	 * 
	 * @param id
	 */
	public void returnMoney(int id) {
//		UserStatusBean userStatus;
		LoadBean load = getLoad(id);
		// 扣钱
		UserInfoUtil.updateUserCash(loginUser.getId(), -load.getMoney(),
				UserCashAction.BANK, "银行扣用户乐币" + load.getMoney());

		MoneyLogBean log = new MoneyLogBean();
		log.setUserId(load.getUserId());
		log.setMoney(load.getMoney());
		log.setType(Constants.BANK_RETURN_TYPE);
		// 删除该笔贷款，往jc_bank_log里面插入一条还款记录
		bankService.deleteLoad(load, log);

	}

	/**
	 * 判断用户的钱是否足够
	 * 
	 * @param money
	 * @return true or false
	 */
	public boolean haveEnoughMoney(int money) {
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end
		// int currentMoney = loginUser.getUs().getGamePoint();
		int currentMoney = usb.getGamePoint();
		if (currentMoney >= money) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将时间转换成秒 fanys 2006-08-04
	 * 
	 * @param time
	 * @return
	 */
	public static long getSeconds(String time) {
		long seconds = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = df.parse(time);
			seconds = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			seconds = 0;
		}

		return seconds / 1000;
	}

	/**
	 * fanys 2006-08-04
	 * 
	 * 每隔一小时调用一次改方法 计算用户贷款利息
	 */
	public static void calcLoadInterest() {
		LoadBean load = null;
		long createTime = 0;
		long expireTime = 0;
		int hour = 0;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, -1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String compareTime = df.format(c.getTime());
		// 当前时间减去1小时得到比较时间,得到是不是到了可以计算利息的时刻
		// 如果创建时间小于这个时间,则应该计算利息
		Vector loadList = bankService.getLoadList(" create_time<='"
				+ compareTime + "'");
		for (int i = 0; i < loadList.size(); i++) {
			load = (LoadBean) loadList.get(i);
			hour = (int) (expireTime - createTime) / 3600;//
			bankService.updateLoad("money=money*"
					+ getUserCurrentLoadAccrual(hour), "id=" + load.getId() + " and money < 1000000000");

		}

	}

	/**
	 * fanys 2006-08-04 贷款小时不同,利率就不同
	 * 
	 * @param hour
	 * @return
	 */
	public static double getUserCurrentLoadAccrual(int hour) {
		double accrual = 0;
		switch (hour) {
		case Constants.BANK_LOAD_1_DAYS:
			accrual = Constants.BANK_LOAD_1_DAYS_ACCRUAL;
			break;
		case Constants.BANK_LOAD_2_DAYS:
			accrual = Constants.BANK_LOAD_2_DAYS_ACCRUAL;
			break;
		case Constants.BANK_LOAD_3_DAYS:
			accrual = Constants.BANK_LOAD_3_DAYS_ACCRUAL;
			break;
		case Constants.BANK_LOAD_4_DAYS:
			accrual = Constants.BANK_LOAD_4_DAYS_ACCRUAL;
			break;
		case Constants.BANK_LOAD_5_DAYS:
			accrual = Constants.BANK_LOAD_5_DAYS_ACCRUAL;
			break;
		case Constants.BANK_LOAD_6_DAYS:
			accrual = Constants.BANK_LOAD_6_DAYS_ACCRUAL;
			break;
		case Constants.BANK_LOAD_7_DAYS:
			accrual = Constants.BANK_LOAD_7_DAYS_ACCRUAL;
			break;
		case Constants.BANK_LOAD_8_DAYS:
			accrual = Constants.BANK_LOAD_8_DAYS_ACCRUAL;
			break;
		case Constants.BANK_LOAD_9_DAYS:
			accrual = Constants.BANK_LOAD_9_DAYS_ACCRUAL;
			break;
		case Constants.BANK_LOAD_10_DAYS:
			accrual = Constants.BANK_LOAD_10_DAYS_ACCRUAL;
			break;
		default:
			accrual = Constants.BANK_LOAD_LAST_DAYS_ACCRUAL;
		}
		return accrual * 0.01 + 1;

	}

	/**
	 * fanys 2006-08-04 离用户还款时间还差一小时的时候提醒用户还钱
	 * 
	 * 
	 */
	public static void noticeUser() {
		LoadBean load = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		String currentTime = df.format(c.getTime());
		c.add(Calendar.HOUR, 1);
		String compareTime = df.format(c.getTime());
//		long currentSeconds = Calendar.getInstance().getTimeInMillis() / 1000;
		Vector loadList = bankService.getLoadList("expire_time>=' "
				+ currentTime + "' and expire_time<'" + compareTime + "' ");
		for (int i = 0; i < loadList.size(); i++) {
			load = (LoadBean) loadList.get(i);
			// 给用户发通知
			NoticeBean notice = new NoticeBean();
			notice.setUserId(load.getUserId());
			notice.setTitle("您的贷款1小时后到期，注意及时归还！");
			notice.setContent("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setHideUrl("");
			notice.setLink("/bank/accountQuery.jsp");
			NoticeUtil.getNoticeService().addNotice(notice);
		}
	}

	/**
	 * 存款柜台：返回用户有无贷款，用户存款，利率信息
	 * 
	 * @param request
	 */

	public void storeCounter(HttpServletRequest request) {
		// 用户有无贷款
		if (userHaveLoad()) {
			request.setAttribute("hasLoad", new Boolean(true));
		} else {
			request.setAttribute("hasLoad", new Boolean(false));
		}
		// 用户存款额
		request.setAttribute("userStore", getUserStore() + "");

		// 当前存款利率
		request.setAttribute("interestRate", getUserCurrentStoreAccrual() + "");
	}

	/**
	 * 获得用户是否有存款记录
	 * 
	 * @return
	 */
	public boolean ifUserStore() {
		// macq_2006-11-27_添加存款缓存_strat
		StoreBean storeBean = BankCacheUtil
				.getBankStoreCache(loginUser.getId());
		if (storeBean != null)
			// 有存款记录
			return true;
		else
			// 无存款记录
			return false;
	}

	/**
	 * 用户存款
	 * 
	 * @param request
	 */
	public void store(HttpServletRequest request) {
		
		String moneyS = request.getParameter("money");
		int money = StringUtil.toInt(moneyS);
		if (money <= 0) {
			request.setAttribute("tip",
					"存款失败!输入字符无效!所填写的存款数目应是0--2,100,000,000之间的自然数。");
			return;
		} else if (money > 2100000000) {
			request.setAttribute("tip", "抱歉!本银行一次受理最大金额为2,100,000,000.请分次存款。");
			return;
		}
		synchronized(loginUser.getLock()) {
			UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());

			if (money > usb.getGamePoint()) {
				request.setAttribute("tip", "抱歉!您没有那么多乐币。");
				return;
			}
			// int userMoney = loginUser.getUs().getGamePoint() - money;
			int userMoney = usb.getGamePoint() - money;
			// 事务处理用户存款
			Connection conn = DbUtil.getConnection();
			PreparedStatement psm = null;
			try {
				conn.setAutoCommit(false);
	
				// update user status game_point
				psm = conn
						.prepareStatement("UPDATE user_status SET game_point=? WHERE user_id=?");
				psm.setInt(1, userMoney);
				psm.setInt(2, loginUser.getId());
				psm.executeUpdate();
				// 更改session中用户的乐币数
				// loginUser.getUs().setGamePoint(userMoney);
	
				conn.commit();
				
	//			 add or update user store account
				BankCacheUtil.updateBankStoreCacheById(money, loginUser.getId(), 0, Constants.BANK_STORE_TYPE);
				
				UserInfoUtil.flushUserStatus(loginUser.getId());
				// mcq_2006-11-24-更新用户存款缓存_start
				//BankCacheUtil.flushBankStoreById(loginUser.getId());
				// mcq_2006-11-24-更新用户存款缓存_end
			} catch (Exception e) {
				try {
					conn.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// 更改session中用户的乐币数
				// loginUser.getUs().setGamePoint(userMoney + money);
			} finally {
				try {
					psm.close();
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// zhul 2006-10-09 对用户现金流进行记录 start
		UserCashBean userCash = new UserCashBean();
		userCash.setUserId(loginUser.getId());
		userCash.setType(UserCashAction.BANK);
		userCash.setReason("用户到银行存款" + money);
		UserCashAction.usercashService.addUserCash(userCash);
		// zhul 2006-10-09 对用户现金流进行记录 end

		// 用户存款额
		request.setAttribute("userStore", getUserStore() + "");

		// 当前存款利率
		request.setAttribute("interestRate", getUserCurrentStoreAccrual() + "");

	}

	/**
	 * 用户取款
	 * 
	 * @param request
	 */
	public void withdraw(HttpServletRequest request) {
		
		String moneyS = request.getParameter("money");
		int money = StringUtil.toInt(moneyS);
		
		synchronized(loginUser.getLock()) {
			UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
			// zhul_modify us _2006-08-14_获取用户状态信息 end
			int gamePoint = usb.getGamePoint();
			if (gamePoint < 0) {
				gamePoint = 0;
			}
			// 当前存款利率
			request.setAttribute("interestRate", getUserCurrentStoreAccrual() + "");

			long storeMoney = this.getUserStore();
			long totalMoney = (long) gamePoint + (long) money;
			if (money <= 0) {
				request.setAttribute("tip",
						"取款失败!输入字符无效!所填写的取款数目应是0--2,100,000,000之间的自然数。");
				// 用户存款额
				request.setAttribute("userStore", storeMoney + "");
				return;
			} else if (money > 2100000000) {
				request.setAttribute("tip", "抱歉!本银行一次受理最大金额为2,100,000,000.请分次取款。");
				// 用户存款额
				request.setAttribute("userStore", storeMoney + "");
				return;
			} else if (money > storeMoney) {
				request.setAttribute("tip", "抱歉!您没有那么多存款。");
				// 用户存款额
				request.setAttribute("userStore", storeMoney + "");
				return;
				// } else if ((loginUser.getUs().getGamePoint() + money) < 0
				// || (loginUser.getUs().getGamePoint() + money) > 2100000000) {
			} else if (totalMoney > 2100000000) {
				request.setAttribute("tip", "抱歉!按照中央银行规定您手中乐币不能超过21亿.");
				// 用户存款额
				request.setAttribute("userStore", storeMoney + "");
				return;
			}
	
			// int userMoney = loginUser.getUs().getGamePoint() + money;
			int userMoney = gamePoint + money;
			// 事务处理用户取款
			Connection conn = DbUtil.getConnection();
			PreparedStatement psm = null;
			try {
				conn.setAutoCommit(false);
	
				// update user status game_point
				psm = conn
						.prepareStatement("UPDATE user_status SET game_point=? WHERE user_id=?");
				psm.setInt(1, userMoney);
				psm.setInt(2, loginUser.getId());
				psm.executeUpdate();
				// 更改session中用户的乐币数
				// loginUser.getUs().setGamePoint(userMoney);
	
				conn.commit();
				
	//			 add or update user store account
				BankCacheUtil.updateBankStoreCacheById(-money, loginUser.getId(), 0, Constants.BANK_WITHDRAW_TYPE);
				
				UserInfoUtil.flushUserStatus(loginUser.getId());
				// mcq_2006-11-24-更新用户存款缓存_start
				//BankCacheUtil.flushBankStoreById(loginUser.getId());
				// mcq_2006-11-24-更新用户存款缓存_end
			} catch (Exception e) {
				try {
					conn.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// 更改session中用户的乐币数
				// loginUser.getUs().setGamePoint(userMoney - money);
			} finally {
				try {
					psm.close();
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// zhul 2006-10-09 对用户现金流进行记录 start
		UserCashBean userCash = new UserCashBean();
		userCash.setUserId(loginUser.getId());
		userCash.setType(UserCashAction.BANK);
		userCash.setReason("用户从银行取款" + money);
		UserCashAction.usercashService.addUserCash(userCash);
		// zhul 2006-10-09 对用户现金流进行记录 end

		// 用户存款额
		request.setAttribute("userStore", getUserStore() + "");

	}

	/**
	 * 用户个人业务查询
	 * 
	 * @param request
	 */
	public void accountQuery(HttpServletRequest request) {
		// 用户存款额
		request.setAttribute("userStore", getUserStore() + "");

		// 用户贷款,分页
		int NUM_PER_PAGE = 5;
		int totalCount = bankService.getLoadCount("user_id="
				+ loginUser.getId());
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		String condition = "user_id=" + loginUser.getId()
				+ " ORDER BY id LIMIT " + pageIndex * NUM_PER_PAGE + ","
				+ NUM_PER_PAGE;
		Vector loadList = bankService.getLoadList(condition);

		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("loadList", loadList);

	}

	/**
	 * fanys 2006-08-04 更新用户存款,每隔一小时更新一次
	 */
	public static void calcStoreInterest() {
		long currentTime = Calendar.getInstance().getTimeInMillis() / 1000;

		double accrual = 1 + Constants.BANK_STORE_ACCRUAL * 0.01;// 存款利率
		if (accrual == 1)
			return;
		// macq_2006-11-27_添加存款更新缓存_start
		BankCacheUtil.updateBankStoreCahce("money=money*" + accrual, "floor("
				+ currentTime + "-UNIX_TIMESTAMP(time))/3600>=1");
		// bankService.updateStore("money=money*" + accrual, "floor("
		// + currentTime + "-UNIX_TIMESTAMP(time))/3600>=1");
		// macq_2006-11-27_添加存款更新缓存_end
	}

	/**
	 * 
	 * 一小时执行一次的任务
	 */
	public static void hourTask() {
		try {
			// 逾期不还扣钱
			deduct();
			// 计算贷款利息
			calcLoadInterest();
			// 存款利息
			calcStoreInterest();
			// 到期提醒用户还款
			noticeUser();
		} catch (Exception ex) {

		}

	}

	/**
	 * 
	 * @author macq
	 * @explain：设置银行密码
	 * @datetime:2007-7-23 15:27:12
	 * @param request
	 * @return void
	 */
	public void bankPW(HttpServletRequest request) {
		UserSettingBean userSetting = loginUser.getUserSetting();
		if (!userSetting.getBankPw().equals("")) {
			request.setAttribute("result", "true");
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：银行密码处理
	 * @datetime:2007-7-23 15:49:44
	 * @param request
	 * @return void
	 */
	public void bankPWRs(HttpServletRequest request) {
		// 新密码
		String npw = StringUtil.noEnter(request.getParameter("npw"));
		// 判断新密码
		if (npw == null) {
			request.setAttribute("tip", "新密码不能为空");
			request.setAttribute("result", "failure");
			return;
		} else if (npw.length() > 20) {
			request.setAttribute("tip", "对不起，银行密码最多为20位数字或字母");
			request.setAttribute("result", "failure");
			return;
		}
		// 获取用户设置信息
		UserSettingBean userSetting = userService.getUserSetting("user_id="
				+ loginUser.getId());
		if (userSetting != null) {
			// 判断银行密码是否为初始值
			if (!userSetting.getBankPw().equals("")) {
				String opw = StringUtil.noEnter(request.getParameter("opw"));
				if (opw == null) {
					request.setAttribute("tip", "旧密码不能为空");
					request.setAttribute("result", "failure");
					return;
				} else if (opw.equals("")) {
					request.setAttribute("tip", "旧密码不能为空");
					request.setAttribute("result", "failure");
					return;
				} else if (opw.length() > 20) {
					request.setAttribute("tip", "对不起，银行密码最多为20位数字或字母");
					request.setAttribute("result", "failure");
					return;
				} else if (!userSetting.getBankPw().equals(opw)) {
					request.setAttribute("tip", "对不起，旧密码错误，密码修改失败");
					request.setAttribute("result", "failure");
					return;
				} else if (npw.equals(opw)) {
					request.setAttribute("tip", "密码设置成功!");
					request.setAttribute("result", "failure");
					return;
				}
			}
			// 更新用户理财密码
			userService
					.updateUserSetting("bank_pw='" + StringUtil.toSql(npw)
							+ "',update_datetime=now()", "user_id="
							+ loginUser.getId());
			AdminAction.addUserLog(loginUser.getId(), 3, npw);
		} else {
			// 添加理财密码
			userSetting = new UserSettingBean();
			userSetting.setUserId(loginUser.getId());
			userSetting.setBankPw(npw);
			userService.addUserSetting(userSetting);
			AdminAction.addUserLog(loginUser.getId(), 3, npw);
		}
		loginUser.getUserSetting().setBankPw(npw);
		request.getSession().removeAttribute(BankAction.BANK_PW);
		if (npw.equals("")) {
			request.setAttribute("tip", "银行密码被取消!");
		} else {
			request.setAttribute("tip", "银行密码设置成功!");
		}
		request.setAttribute("result", "success");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：
	 * @datetime:2007-7-23 16:29:06
	 * @param request
	 * @return void
	 */
	public void bankPWCheck(HttpServletRequest request) {
		// 登录返回页面
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = "/wapIndex.jsp";
		}
		request.setAttribute("backTo", backTo);
		// 判断用户是否设置银行密码
		UserSettingBean userSetting = loginUser.getUserSetting();
		if (userSetting.getBankPw().equals("")) {
			request.getSession().setAttribute(BankAction.BANK_PW, "ture");
			request.setAttribute("result", "null");
			return;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：银行登录
	 * @datetime:2007-7-23 16:35:51
	 * @param request
	 * @return void
	 */
	public void bankLogin(HttpServletRequest request) {
		// 登录返回页面
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = "/wapIndex.jsp";
		}
		request.setAttribute("backTo", backTo);
		// 新密码
		String pw = StringUtil.noEnter(request.getParameter("pw"));
		
		String ip = request.getRemoteAddr();
		int vres = VerifyUtil.getVerifyCountByKey(new Integer(loginUser.getId()));
		if(vres >= 10) {	// 错太多了，5分钟内禁止登陆
			request.setAttribute("tip", "密码错误次数过多，请在5分钟后再试！");
			request.setAttribute("result", "failure");
			return;
		}
		
		// 判断新密码
		if (pw == null) {
			request.setAttribute("tip", "密码不能为空");
			request.setAttribute("result", "failure");
			return;
		} else if (pw.equals("")) {
			request.setAttribute("tip", "密码不能为空");
			request.setAttribute("result", "failure");
			return;
		} else if (pw.length() > 20) {
			request.setAttribute("tip", "对不起，银行密码最多为20位数字或字母");
			request.setAttribute("result", "failure");
			return;
		}
		// 获取用户设置信息
		UserSettingBean userSetting = loginUser.getUserSetting();
		if (userSetting != null && !pw.equals(userSetting.getBankPw())) {
			request.setAttribute("tip", "对不起，银行密码错误,请重新输入");
			request.setAttribute("result", "failure");
			VerifyUtil.logFail(loginUser.getId(), pw, ip);
			return;
		}
		request.getSession().setAttribute(BankAction.BANK_PW, "ture");
		request.setAttribute("tip", "登录成功!");
		request.setAttribute("result", "success");
		return;
	}
	
	public static boolean chenkUserBankPW(HttpSession session) {
		UserBean user = (UserBean) session
				.getAttribute(Constants.LOGIN_USER_KEY);
		if (user == null) {
			return false;
		} else if (user.getUserSetting().getBankPw().equals("")) {
			return false;
		} else if (session.getAttribute(BankAction.BANK_PW) != null) {
			return false;
		}
		return true;
	}
	public static Vector getBankLogList(int userId)
	{
		String key = getKey(userId);
		// 从缓存中取
		Vector bankLogList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.BANK_LOG_CACHE_GROUP,
				OsCacheUtil.BANK_LOG_FLUSH_PERIOD);
		// 缓存中没有
		if (bankLogList == null) {
			// 从数据库中取
			String condition = "user_id=" + userId;
			bankLogList = serviceImpl.getBankLogList(condition);
			// 为空
			if (bankLogList == null) {
				return null;
			}
			// 放到缓存中
			OsCacheUtil.put(key, bankLogList,
					OsCacheUtil.BANK_LOG_CACHE_GROUP);
		}
		return bankLogList;
	}
	public static String getKey(int userId) {
		return String.valueOf(userId);
	}
	
}
