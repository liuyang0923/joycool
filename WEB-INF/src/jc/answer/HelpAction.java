package jc.answer;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.*;

import java.util.*;

public class HelpAction extends CustomAction {
	public HelpAction(HttpServletRequest request) {
		super(request);
	}

	public static HelpService service = new HelpService();

	public String format(long time) {
		long now = System.currentTimeMillis();
		if (DateUtil.dayDiff(now, time) == 0) {
			return DateUtil.sformatTime(time);
		} else {
			return DateUtil.formatDate2(time);
		}
	}

	public void checkDel() {
		int delpid = this.getParameterInt("delpid");
		int delaid = this.getParameterInt("delaid");
		if (delaid > 0) {
			BeanAnswer ba = service.getAnswer(delaid);
			BeanProblem bp = null;
			if (ba != null)
				bp = service.getProblem(ba.getPid());
			if (bp != null) {
				if (bp.getNumReply() >= 1)
					service
							.upd("update help_problem set num_reply=num_reply-1 where id="
									+ bp.getId());
				if (ba.getFlag() == 1) {
					int num = service.get222AnswerList(
							" del=0 and flag=1 and p_id=" + bp.getId()
									+ " order by use_time desc").size();
					if (num == 1)
						service
								.upd("update help_problem set issolved=0 where id="
										+ bp.getId());
					if (num >= 3)
						service
								.upd("update help_problem set isover=0 where id="
										+ bp.getId());
				}
			}
			service.upd("update help_answer set del=1 where id=" + delaid);
		}
		if (delpid > 0) {
			service.upd("update help_answer set del=1 where p_id=" + delpid);
			service.upd("update help_problem set del=1 where id=" + delpid);
		}
	}

	public void updHpPost(int pid) {
		service.upd("update help_problem set num_view=num_view+1 where id="
				+ pid);
	}

	public int goBack() {
		UserBean ub = this.getLoginUser();
		if (ub == null)
			return 99;
		ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("forum",ub.getId());
		if(forbid != null) {
			doTip("tip", "已经被禁止发贴 - " + forbid.getBak());
			return 99;
		}
		String title = this.getParameterNoEnter("title");
		String prize = this.getParameterNoEnter("prize");
		String pcont = this.getParameterString("pcont");
		int uid = ub.getId();
		int gender = ub.getGender();
		if (title == null || title.replaceAll(" ", "").length() == 0)
			return 1;// 标题不能为空!
		else if (prize == null || prize.length() == 0)
			return 2;// 奖品不能为空!
		else if (title.length() > 20)
			return 4;// 标题长度不能超过20
		else if (prize.length() > 50)
			return 5;// 奖品长度不能超过50
		else if (pcont != null && pcont.length() > 1000)
			return 6;// 内容长度不能超过1000
		else if (gender == 1)
			return 7;// 你好，有偿求助中只有女性才可求助，快去帮她们解决问题吧。
		else if (!service.IfCanPub(uid))
			return 8;// 发帖未超过一天
		else if (!service.isOvered(uid))
			return 9;// 上一帖还未结束
		else {
			service
					.upd("insert into help_problem (create_time,last_reply_time,user_id,p_title,prize,p_cont) values(now(),now(),"
							+ uid
							+ ",'"
							+ StringUtil.toSql(title)
							+ "','"
							+ StringUtil.toSql(prize)
							+ "','"
							+ StringUtil.toSql(pcont) + "')");
			return 10;// 发帖成功!
		}
	}

	public int goBack2(int pid) {
		UserBean ub = this.getLoginUser();
		if (ub == null)
			return 99;
		ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("forum",ub.getId());
		if(forbid != null) {
			doTip("tip", "已经被禁止发贴 - " + forbid.getBak());
			return 99;
		}
		String asw = this.getParameterString("asw");
		int uid = ub.getId();
		BeanProblem bp = this.getProb(pid);
		if (bp == null)
			return 3;// 没有这个帖子
		else if (asw == null || asw.replaceAll(" ", "").length() == 0)
			return 1;// 输入内容不能为空!
		else if (asw.length() > 100)
			return 2;// 内容长度不能超过100字!
		else if (!this.isCooldown("", 3000))
			return 5;// 操作太快了
		else if (bp.getIsOver() == 1)
			return 3;// 已经结束的帖子
		else if (bp.getUid() == uid)
			return 4;// 自己的帖子不能回复
		else {
			service
					.upd("update help_problem set num_reply=num_reply+1,last_reply_time=now() where id="
							+ pid);
			service
					.upd("insert into help_answer (create_time,p_id,user_id,a_cont)values(now(),+"
							+ pid
							+ ","
							+ uid
							+ ",'"
							+ StringUtil.toSql(asw)
							+ "')");
			return 0;
		}
	}

	public int goBack3(int pid) {
		UserBean ub = this.getLoginUser();
		if (ub == null)
			return 99;
		ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("forum",ub.getId());
		if(forbid != null) {
			doTip("tip", "已经被禁止发贴 - " + forbid.getBak());
			return 99;
		}
		int vt = this.getParameterInt("vt");
		int aid = this.getParameterInt("aid");
		int uid = ub.getId();
		BeanProblem bp = service.getProblem(pid);
		BeanVote bv = service.isVote(pid, uid);
		if (bp == null)
			return 12;// 帖子不存在
		else if (vt > 2)
			return 0;// 没有此投票
		else if (vt > 0 && bp.getUid() == uid)
			return 7;// 不能对自己的帖子投票
		else if (vt > 0 && bv != null)
			return bv.getFlag();// 已经投过票
		else if (aid > 0 && bp.getIsOver() == 1)
			return 9;// 帖子已经结束
		else if (vt > 0) {
			service
					.upd("insert into help_vote (create_time,p_id,user_id,flag)values(now(),"
							+ pid + "," + uid + "," + vt + ")");
			if (vt == 1)
				service
						.upd("update help_problem set num_flower=num_flower+1,last_reply_time=now() where id="
								+ pid);
			else
				service
						.upd("update help_problem set num_egg=num_egg+1,last_reply_time=now() where id="
								+ pid);
			return 3;// 投票成功!
		} else if (aid > 0 && bp.getUid() != uid)
			return 4;// 不是自己的帖子
		else if (aid > 0 && !service.isAnswerExist(pid, aid))
			return 5;// 没有此回复
		else if (aid > 0) {
			if (service.get222AnswerList(
					" del=0 and flag=1 and p_id=" + pid
							+ " order by use_time desc").size() > 2)
				return 8;// 你好，一个帖子只可以采用3个答案.
			else {
				service
						.upd("update help_answer set flag=1,use_time=now() where id="
								+ aid);
				service
						.upd("update help_problem set issolved=1,last_reply_time=now() where id="
								+ pid);
				if (service.get222AnswerList(
						" del=0 and flag=1 and p_id=" + pid
								+ " order by use_time desc").size() == 3)// 如果已经采用2条了,就直接完帖
					service.upd("update help_problem set isover=1 where id="
							+ pid);
				return 6;// 采用成功
			}
		} else if (this.getParameterNoEnter("o") != null) {
			if (bp.getUid() == uid) {
				service.upd("update help_problem set isover=1 where id=" + pid);
				service
						.upd("update help_problem set last_reply_time=now() where id="
								+ pid);
				return 10;
			} else {
				return 11;
			}
		} else
			return 13;
	}

	/**
	 * 获得帖子列表
	 * 
	 * @return
	 */
	public List getPrombList() {
		List list = null;
		int uid = 0;
		if (this.getLoginUser() != null)
			uid = this.getLoginUser().getId();
		int tp = this.getParameterInt("tp");
		if (uid > 0) {
			if (tp == 1) {
				list = service.get222ProblemList(" del=0 and user_id=" + uid
						+ " order by last_reply_time desc");
			} else if (tp == 2) {
				list = service.getProblemList2(uid);
			} else if (tp == 3) {
				list = service
						.get222ProblemList(" del=0 and (num_reply >= 30 or num_view >= 80)order by last_reply_time desc");
			} else {
				list = service
						.get222ProblemList(" del=0 order by last_reply_time desc");
			}
		} else {
			list = service
					.get222ProblemList(" del=0 order by last_reply_time desc");
		}
		return list;
	}

	/**
	 * 获得具体帖子
	 * 
	 * @return
	 */
	public BeanProblem getProb(int pid) {
		return service.getProblem(pid);
	}

	/**
	 * 所有回复(前台)
	 * 
	 * @return
	 */
	public List getAnswerAll(int pid) {
		return service.getAnswerAll(pid);
	}

}
