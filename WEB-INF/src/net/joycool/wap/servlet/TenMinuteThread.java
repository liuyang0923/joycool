package net.joycool.wap.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.charitarian.CharitarianBean;
import net.joycool.wap.bean.charitarian.CharitarianHistoryBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICharitarianService;
import net.joycool.wap.util.CharitarianCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * 
 * @author macq
 * 
 */
public class TenMinuteThread extends Thread {
	public void run() {
		ICharitarianService charitarianService = ServiceFactory
				.createCharitarianService();
		while (true) {
			try {
				Thread.sleep(1000 * 60 * 10);
			} catch (InterruptedException e) {
				return;
			}
			LogUtil.logTime("TenMinuteThread*****start");
			try {
				// 初始化
				int totalCount = 0;
				DbOperation dbOp = new DbOperation();
				dbOp.init();
				// 构建更新语句
				String query = "SELECT sum(count)c_id  from jc_charitarian ";
				ResultSet rs = dbOp.executeQuery(query);
				try {
					if (rs.next()) {
						totalCount = rs.getInt("c_id");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// 释放资源
				dbOp.release();
				// 如果慈善基金少于100万的时候,通知3个管理员
				if (totalCount < 100) {
					addNotice(431);
					addNotice(519610);
					addNotice(914727);
				}

				// 初始化
				Vector lackMoneyList = new Vector();
				// 获取所有在线用户
				ArrayList onlineList = OnlineUtil.getAllOnlineUser();
				String userId = null;
				UserStatusBean userStatus = null;
				//StoreBean store = null;
				long totalMoney = 0;
				// 循环在线用户id列表
				for (int i = 0; i < onlineList.size(); i++) {
					// 取得用户id
					userId = (String) onlineList.get(i);
					// 取得用户game_point
					userStatus = UserInfoUtil.getUserStatus(StringUtil
							.toInt(userId));
//					// 取得用户存款
//					store = BankCacheUtil.getBankStoreCahce(StringUtil
//							.toInt(userId));
//					if (store == null) {
//						store = new StoreBean();
//					}
					// 取得用户乐币加银行存款总额
					totalMoney = userStatus.getGamePoint() ;
					//totalMoney = userStatus.getGamePoint() + store.getMoney();
					// 如果总额小于5千,加入受赠列表
					if (totalMoney < Constants.CHARITARIAN_USER_MONEY && userStatus.getRank()<4) {
						lackMoneyList.add(userId);
					}
				}

				if (totalCount >= 1 && lackMoneyList.size()>0) {
					// 获取所有还有捐款额度的慈善家列表
					Vector charitarianeList = charitarianService
							.getCharitarianList("count>0");
					CharitarianBean charitariane = null;
					if (charitarianeList.size() > 0) {
						for (int i = 0; i < lackMoneyList.size(); i++) {
							// 获取将要获得捐助的用户ID
							userId = (String) lackMoneyList.get(i);
							// 随机抽取一个捐助者
							charitariane = (CharitarianBean) charitarianeList
									.get(RandomUtil.nextInt(charitarianeList.size()));
							//判断捐赠者和接受捐助者是不是同一个人,如果是同一个人就不接受捐赠
							if(charitariane.getUserId()==StringUtil.toInt(userId)){
								continue;
							}
							// 获取剩余捐助份数
							int count = charitariane.getCount();
							// 如果捐助份数为0,从列表中删除该用户
							if (count <= 0) {
								lackMoneyList.remove(charitariane);
								continue;
							}
							// 更新剩余捐助份数
							count = count - 1;
							// 更新内存中剩余捐助份数
							charitariane.setCount(count);
							// 更新数据库中剩余捐助份数
							boolean flag = CharitarianCacheUtil
									.updateBankStoreCahceById("count=" + count,
											"user_id="
													+ charitariane.getUserId(),
											charitariane.getUserId());
							if (flag) {
								// 更新乐币接受者的game_point字段
								flag = UserInfoUtil
										.updateUserCash(StringUtil.toInt(userId), Constants.CHARITARIAN_USER_MONEY,
												UserCashAction.GAME, "接受慈善捐赠");
								if (flag) {
									CharitarianHistoryBean CharitarianHistory = new CharitarianHistoryBean();
									CharitarianHistory
											.setCharitarianId(charitariane
													.getUserId());
									CharitarianHistory.setReceiveId(StringUtil
											.toInt(userId));
									charitarianService
											.addCharitarianHistory(CharitarianHistory);
								}
							}
							if (flag) {
								// 给捐赠接受者发送系统通知
								NoticeBean notice = new NoticeBean();
								notice.setUserId(StringUtil.toInt(userId));
								notice.setTitle("您获得" + Constants.CHARITARIAN_USER_MONEY + "乐币捐助!");
								notice.setContent("");
								notice.setType(NoticeBean.GENERAL_NOTICE);
								notice.setHideUrl("");
								notice.setLink("/charitarian/notice.jsp");
								ServiceFactory.createNoticeService().addNotice(notice);
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			LogUtil.logTime("TenMinuteThread*****end");
		}
	}

	// 发布系统消息
	public void addNotice(int userId) {
		NoticeBean notice = new NoticeBean();
		notice.setUserId(userId);
		notice.setTitle("慈善基金低于100万,请注意!");
		notice.setContent("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setHideUrl("");
		notice.setLink("/charitarian/index.jsp");
		ServiceFactory.createNoticeService().addNotice(notice);
	}
}
