package net.joycool.wap.action.job;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.RankBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.job.CardBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.CountMap;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.UserInfoUtil;

public class CardAction {

	static IUserService userService = ServiceFactory.createUserService();

	public static String[] typeId = {"穷神卡","财神卡","衰神卡","福神卡","均富卡","教会卡","天使卡","革命卡","升级卡","牛市卡","道具卡"};

	public static String[] valueType = {"数值","百分比"};
	
	public static String[] actionField = {"乐币","经验","等级","物品",};
	
	public static String[]  actionDirection ={"加","减","","物品"};
	
	UserBean loginUser;

	HttpSession session = null;

	static IDummyService dummyService = ServiceFactory.createDummyService();
	
	public static CountMap countMap = new CountMap();

	public CardAction() {
	}

	public CardAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		session = request.getSession();
	}

	public UserBean getLoginUser() {
		return loginUser;
	}

	public IDummyService getDummyService() {
		return dummyService;
	}

	public void checkUserPoint(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// 查找用户是否有足够的钱购买机会卡
		
		// UserStatusBean userStatusBean = userService.getUserStatus("user_id =
		// "
		// + loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean userStatusBean = UserInfoUtil.getUserStatus(loginUser
				.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		if (userStatusBean != null) {
			int userMoney = userStatusBean.getGamePoint();
			if (userMoney < Constants.JOB_CARD_MONEY) {
				// 用户钱小于一个机会卡所需的钱数
				request.setAttribute("msg", "1");
				return;
			}
		}
		// 扣用户乐币从user_status
		// zhul_2006-08-11 modify userstatus start
		UserInfoUtil.updateUserCash(loginUser.getId(), -Constants.JOB_CARD_MONEY, UserCashAction.CARD, "买机会卡扣钱"
						+ Constants.JOB_CARD_MONEY + "乐币");
		// zhul_2006-08-11 modify userstatus end

		// 更改session中用户的乐币数
		// loginUser.getUs().setGamePoint(
		// loginUser.getUs().getGamePoint() - Constants.JOB_CARD_MONEY);

		// add by zhangyi 2006-07-24 for stat user money history start
		MoneyAction.addMoneyFlowRecord(Constants.CARD,
				Constants.JOB_CARD_MONEY, Constants.SUBTRATION, loginUser
						.getId());
		// add by zhangyi 2006-07-24 for stat user money history end

		// 机分卡抽取过程跟踪
		request.getSession().setAttribute("Buying", "true");
	}

	public void dealUserStatusOfBuyCard(CardBean cardBean) {
		int[] count = CardAction.countMap.getCount(loginUser.getId());
		count[0]++;

		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		// 查找用户是否有足够的钱购买机会卡
		// UserStatusBean userStatusBean = userService.getUserStatus("user_id =
		// "
		// + loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean userStatusBean = UserInfoUtil.getUserStatus(loginUser
				.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		if (userStatusBean != null) {
			int userMoney = userStatusBean.getGamePoint();
			int userPoint = userStatusBean.getPoint();

			int money = 0;

			if (cardBean.getActionfield() == 0) {
				// 乐币处理
				if (cardBean.getValueType() == 0) {
					// 直接处理值类型的卡
					int num = cardBean.getActionValue();
					if(count[0] > 1000)
						num = 0;
					if (cardBean.getActionDirection() == 0) {
						// 增加情况
						num = getNotOutOfNum(userMoney, num);
						money = userMoney + num;
						// add by zhangyi 2006-07-24 for stat user money history
						// start
						MoneyAction.addMoneyFlowRecord(Constants.CARD, num,
								Constants.PLUS, loginUser.getId());
						// add by zhangyi 2006-07-24 for stat user money history
						// end
					} else if (cardBean.getActionDirection() == 1) {
						// 减去情况
						if (userMoney < num) {
							money = 0;
							num = userMoney;
						} else {
							money = userMoney - num;
						}
						// add by zhangyi 2006-07-24 for stat user money history
						// start
						MoneyAction.addMoneyFlowRecord(Constants.CARD, num,
								Constants.SUBTRATION, loginUser.getId());
						// add by zhangyi 2006-07-24 for stat user money history
						// end
					}
				} else {
					// 百分比概率类型的卡
					int num = (int) (userMoney * 0.01f * cardBean
							.getActionValue());
					if(count[0] > 1000)
						num = 0;
					if (cardBean.getActionDirection() == 0) {
						// 增加情况
						num = getNotOutOfNum(userMoney, num);
						money = userMoney + num;
						// add by zhangyi 2006-07-24 for stat user money history
						// start
						MoneyAction.addMoneyFlowRecord(Constants.CARD, num,
								Constants.PLUS, loginUser.getId());
						// add by zhangyi 2006-07-24 for stat user money history
						// end
					} else if (cardBean.getActionDirection() == 1) {
						// 减去情况
						if (userMoney < num) {
							money = 0;
							num = userMoney;
						} else {
							money = userMoney - num;
						}
						// add by zhangyi 2006-07-24 for stat user money history
						// start
						MoneyAction.addMoneyFlowRecord(Constants.CARD, num,
								Constants.SUBTRATION, loginUser.getId());
						// add by zhangyi 2006-07-24 for stat user money history
						// end
					}
				}
				// 更改用户的乐币数
				long add = money - userMoney;
				// zhul_2006-08-11 modify userstatus start
				UserInfoUtil.updateUserCash(loginUser.getId(), add, UserCashAction.CARD,
						"买机会卡，卡中结果影响乐币数：" + add);
				// zhul_2006-08-11 modify userstatus end

				// 更改session中用户的乐币数
				// loginUser.getUs().setGamePoint(money);

			} else if (cardBean.getActionfield() == 1) {
				// 经验处理
				if (cardBean.getValueType() == 0) {
					// 直接处理值类型的卡
					int num = cardBean.getActionValue();
					if (cardBean.getActionDirection() == 0) {
						// 增加情况
						num = getNotOutOfNum(userPoint, num);
						RankAction.addPoint(loginUser, num);

					} else if (cardBean.getActionDirection() == 1) {
						// 减去情况
						subPoint(loginUser, num);
					}

				} else {
					// 百分比概率类型的卡
					int num = (int) (userPoint * 0.01 * cardBean
							.getActionValue());
					if (userPoint < 0) {
						num = (int) (userPoint * 0.01 * (cardBean
								.getActionValue() - 2 * cardBean
								.getActionValue()));
					}
					if (cardBean.getActionDirection() == 0) {
						// 增加情况
						num = getNotOutOfNum(userPoint, num);
						RankAction.addPoint(loginUser, num);

					} else if (cardBean.getActionDirection() == 1) {
						// 减去情况
						subPoint(loginUser, num);
					}
				}
			} else if (cardBean.getActionfield() == 2) {
				// 等级处理
				// 得到当前用户等级
				// int rank = loginUser.getUs().getRank();
				int rank = usb.getRank();
				int nextRank = rank + 1;
				// 取得下一等级信息
				RankBean jcRank = (RankBean) LoadResource.getRankMap().get(
						new Integer(nextRank));
				// 如果没有下一等级的话,不更新用户等级
				if (jcRank != null) {
					// 更新数据库记录
					UserInfoUtil.updateUserStatus("rank=" + nextRank
							+ ",point=0", "user_id=" + loginUser.getId(),
							loginUser.getId(), UserCashAction.CARD,
							"买机会卡更改用户等级为" + nextRank + "级");

					// 更新session对象引用
					// loginUser.getUs().setRank(nextRank);
					// loginUser.getUs().setPoint(0);
				}
			} else if (cardBean.getActionfield() == 3) {
				// 得到行囊的随机物品  macq_2007-6-28_更改排除狩猎道具
				Vector dummyList = getDummyService().getDummyProductList(
						"dummy_id<=3");
				if (dummyList != null&&dummyList.size()>0) {
					int random = RandomUtil.nextInt(dummyList.size());
					DummyProductBean dummy = (DummyProductBean) dummyList
							.get(random);
					session.setAttribute("randomuserbag", dummy);
				}

				// 插入用户行囊表
				// 更新用户信息的user_bag字段

				// session.setAttribute("nowuserbag", "nowuserbag");

			}
		}
	}

	// zhul_2006-07-11 功能：随机插取一张机会卡，并返回本机会卡的相关内容 start
	public CardBean getRandomCard() {
		TreeMap cardMap = LoadResource.getCardMap();
		// 插取随机卡
		int sum = ((Integer) cardMap.get(new Integer(10000))).intValue();
		int rand = RandomUtil.nextInt(sum) + 1;
		// rand = 353;
		// 查找随机卡相对应的记录
		Set keys = cardMap.keySet();
		Iterator it = keys.iterator();

		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			int area = key.intValue();
			if (area >= rand) {
				CardBean card = (CardBean) cardMap.get(key);
				// System.out.println("////card:"+card.getCardName()+"---"+card.getActionValue()+"----"+card.getActionDirection());
				return card;
			}
		}

		return null;
	}

	// zhul＿2006-07-11 end

	// zhul _2006-07-11_减经验值的处理 start
	public void subPoint(UserBean user, int subPoint) {
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(user.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		// 得到当前用户等级
		// int rank = user.getUs().getRank();
		int rank = usb.getRank();
		// 得到当前用户经验值
		// int point = user.getUs().getPoint() - subPoint;
		int point = usb.getPoint() - subPoint;
		// log.debug("user current point are " + point);
		//
		// if(point<0)
		// {
		// // 更新数据库记录
		// // 更新session对象引用
		// user.getUs().setRank(rank);
		// user.getUs().setPoint(0);
		//				
		// }else
		// {
		// 更新数据库记录
		// zhul_modify_us 2006-08-14 start
		UserInfoUtil.updateUserStatus("rank=" + rank + ",point=" + point,
				"user_id=" + user.getId(), loginUser.getId(),
				UserCashAction.CARD, "买机会卡更改用户等级及经验值");
		// zhul_modify_us 2006-08-14 end
		// 更新session对象引用
		// user.getUs().setRank(rank);
		// user.getUs().setPoint(point);
		// }
	}

	// zhul _2006-07-11_减经验值的处理 end

	public int getNotOutOfNum(int userNum, int addNum) {
		if (Constants.SYS_MAX_INT - addNum < userNum) {
			addNum = Constants.SYS_MAX_INT - userNum;
		}
		return addNum;
	}

	/*
	 * public static void main(String[] args) { int maxsPoint = 2147483647; //
	 * int userPoint = 1000112500; double pointy = maxsPoint * 100 * 0.015; int
	 * pointD = (int) (maxsPoint * 0.01 * 101); // int result = (int)pointD;
	 * System.out.println(pointD); // System.out.println(result); //
	 * System.out.println(userPoint-point); }
	 */
}