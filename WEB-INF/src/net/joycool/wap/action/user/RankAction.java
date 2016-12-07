package net.joycool.wap.action.user;

import java.util.HashMap;
import java.util.Vector;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.RankActionBean;
import net.joycool.wap.bean.RankBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Encoder;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RankAction {
	static Log log = LogFactory.getLog(RankAction.class);

	private static IUserService userService = ServiceFactory
			.createUserService();

	private static INoticeService noticeService = ServiceFactory
			.createNoticeService();

	
	// 直接升级
	public static void addRank(UserBean user) {
		user.setUs(UserInfoUtil.getUserStatus(user.getId()));
		int rank = user.getUs().getRank();
		RankBean jcRank = (RankBean) LoadResource.getRankMap().get(
				new Integer(rank + 1));
		if (jcRank != null) {
			addPoint(user, jcRank.getNeedPoint() - user.getUs().getPoint());
		}
	}
	
	public static void addPoint(UserBean user, int addPoint) {
		// 判断是否用户登陆
		if (user == null) {
			return;
		}
		// macq_2006-12-15_判断更新用户行囊标志位_start
		boolean mark = false;
		// macq_2006-12-15_判断更新用户行囊标志位_end
		// fanys2006-08-11
		user.setUs(UserInfoUtil.getUserStatus(user.getId()));
		// 得到当前用户等级
		int rank = user.getUs().getRank();
		// 得到当前用户经验值
		int point = user.getUs().getPoint() + addPoint;
		// log.debug("user current point are " + point);
		//
		int nextRank = rank + 1;
		// 取得下一等级信息
		RankBean jcRank = (RankBean) LoadResource.getRankMap().get(
				new Integer(nextRank));
		// 如果没有下一等级的话,不更新用户等级
		if (jcRank != null) {
			// 取得下一等级经验值
			int needPoint = jcRank.getNeedPoint();
			// 判断当前用户经验值是否满足升级条件,如果满足更新用户等级信息
			if (point >= needPoint) {
				// 更新等级
				rank = jcRank.getRankId();
				// 更新积分
				point = point - needPoint;
				// 判断用户性别
				int flag = user.getGender();
				String title = null;
				if (flag == 1) {
					title = jcRank.getMaleName();
				} else {
					title = jcRank.getFemaleName();
				}
				nextRank = rank + 1;
				// 等到升级后下一等级信息
				jcRank = (RankBean) LoadResource.getRankMap().get(
						new Integer(nextRank));
				// 判断是否有下一个等级的动作
				if (jcRank == null) {
					// macq_2006-12-15_升级的时候增加行囊容量_start
					mark = true;
					// macq_2006-12-15_升级的时候增加行囊容量_end
					// 加入消息系统
					NoticeBean notice = new NoticeBean();
					notice.setUserId(user.getId());
					notice.setTitle("升级成功!");
					notice.setContent("升级成功!您现在的头衔是" + title + ",等级是" + rank);
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setHideUrl("");
					notice.setLink("");
					noticeService.addNotice(notice);
				} else {
					// 升级所需经验值
					int newNeedPoint = Math.abs(jcRank.getNeedPoint() - point);
					// 下次升级时候对应动作列表
					Vector actionList = userService
							.getRankActionList("rank_id=" + nextRank);

					StringBuilder sb = new StringBuilder();
					sb.append("升级成功!您现在的头衔是");
					sb.append(title);
					sb.append(",等级是");
					sb.append(rank);
					sb.append(",离升级还需要");
					sb.append(newNeedPoint);
					sb.append("点经验值,升级后你就可以给他(她)发送");
					RankActionBean rankAction = null;
					for (int i = 0; i < actionList.size(); i++) {
						if (i >= 1) {
							sb.append(",");
						}
						rankAction = (RankActionBean) actionList.get(i);
						sb.append(rankAction.getActionName());
					}
					sb.append("的动作了哦~");
					// macq_2006-12-15_升级的时候增加行囊容量_start
					mark = true;
					// macq_2006-12-15_升级的时候增加行囊容量_end
					// 加入消息系统
					NoticeBean notice = new NoticeBean();
					notice.setUserId(user.getId());
					notice.setTitle("升级成功!");
					notice.setContent(sb.toString());
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setHideUrl("");
					notice.setLink("");
					noticeService.addNotice(notice);
				}
			}
		}
		if (mark) {
			// 更新数据库记录
			// macq_2006-12-15_升级的时候增加行囊容量_start
			UserInfoUtil.updateUserStatus("rank=" + rank + ",point=" + point
					+ ",user_bag=15+rank*2", "user_id=" + user.getId(), user
					.getId(), UserCashAction.OTHERS, "增加经验值,更新行囊大小");
			// userService.updateUserStatus("rank=" + rank + ",point=" + point,
			// "user_id=" + user.getId());
			// 更新session对象引用
			user.getUs().setRank(rank);
			user.getUs().setPoint(point);
			user.getUs().setUserBag(15+rank*2);
			// macq_2006-12-15_升级的时候增加行囊容量_end
		} else {
			// 更新数据库记录
			// fanys2006-08-11
			UserInfoUtil.updateUserRank(user.getId(), rank, point, null);
			// userService.updateUserStatus("rank=" + rank + ",point=" + point,
			// "user_id=" + user.getId());
			// 更新session对象引用
			user.getUs().setRank(rank);
			user.getUs().setPoint(point);
		}
	}

	/**
	 * mcq_2006-8-29_增加用户四步走的第一步(登录提示用户ID和密码)_start
	 * 
	 * @param loginUser
	 * @return
	 */
	public static String getUserInfo(UserBean loginUser) {
		String nickName = StringUtil.toWml(loginUser.getNickName());
		int userId = loginUser.getId();
		String password = Encoder.decrypt(loginUser.getPassword());
		String content = "乐酷管理员温馨提示：尊敬的" + nickName + "，最近发生一些恶意用户盗用别人昵称的事件，"
				+ "希望您记住自己的ID:" + userId + "和密码:" + password
				+ "，以后多用这个登陆，以保障您的利益！";
		return content.toString();
	}

	// zhul add new method 2006-06-12 start 新登录用户传入方法得到用户当前等级状态的字符串
	public static String getRandStatus(UserBean loginUser) {
		// 查看用户是否更改过积分
		// fanys2006-08-11
		// loginUser = UserInfoUtil.getUserInfo(loginUser);
		loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
		// 求头衔
		HashMap map = LoadResource.getRankMap();
		String name = null;
		RankBean rank = (RankBean) map.get(new Integer(loginUser.getUs()
				.getRank()));
		if (loginUser.getGender() == 1) {
			name = rank.getMaleName();
		} else {
			name = rank.getFemaleName();
		}
		// 求距上一级分差
		RankBean rankHigh = (RankBean) map.get(new Integer(loginUser.getUs()
				.getRank() + 1));
		int currentPoint = loginUser.getUs().getPoint();
		if (rankHigh == null) {
			return "您现在头衔是" + name + ",等级是" + loginUser.getUs().getRank()
					+ ",经验值是" + currentPoint + "点.";
		}
		int needPoint = Math.abs(rankHigh.getNeedPoint() - currentPoint);
		// 求升级后的动作
		Vector actionList = userService.getRankActionList("rank_id="
				+ (loginUser.getUs().getRank() + 1));
		StringBuilder sb = new StringBuilder();
		RankActionBean rankAction = null;
		for (int i = 0; i < actionList.size(); i++) {
			if (i >= 1) {
				sb.append(",");
			}
			rankAction = (RankActionBean) actionList.get(i);
			sb.append(rankAction.getActionName());
		}

		return "您现在头衔是" + name + ",等级是" + loginUser.getUs().getRank() + ",经验值是"
				+ currentPoint + "点,离升级还需要" + needPoint + "点经验值,升级后你就可以给他(她)发送"
				+ sb.toString() + "的动作了哦~";
	}
	// zhul add new method 2006-06-12 end 新登录用户传入方法得到用户当前等级状态的字符串
}
