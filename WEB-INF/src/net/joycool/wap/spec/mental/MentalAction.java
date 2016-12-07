package net.joycool.wap.spec.mental;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;

public class MentalAction extends CustomAction{
	
	public static MentalService service = new MentalService();
	
	public MentalAction(){}
	
	public MentalAction(HttpServletRequest request){
		super(request);
	}
	
	// 取得用户
	public MentalUser getCurrentUser(){
		if (this.getLoginUser() == null){
			return null;
		}
		int uid = this.getLoginUser().getId();
		if (uid == 0){
			return null;
		}
		return service.getMentalUser(" user_id=" + uid);
	}
	
	// 获取正确的题目
	public MentalQuestion getCurrentQuestion(String nextTimeStr){
		MentalQuestion question = null;
		MentalUser mentalUser = getCurrentUser();
		
		if (mentalUser == null){
			mentalUser = new MentalUser();
			mentalUser.setUserId(this.getLoginUser().getId());
			SqlUtil.executeUpdate("insert into mental_user (user_id) values (" + this.getLoginUser().getId() + ")", 4);
		} else if (mentalUser.getQueNow() >= 9){
			// 这里还要判断上一次做题时间是否在2月前。
			long newTime = this.service.getTimePastDays(nextTimeStr, mentalUser.getUserId());
			if (newTime > 0){
				if (newTime >= System.currentTimeMillis()){
					this.setAttribute("tip", "再次测试需要等待一段的时间哦^-^.");
					return null;
				} else {
					// 已经过了N个月
					SqlUtil.executeUpdate("update mental_user set answer='',create_time='1970-01-01',que_now=0 where user_id=" + mentalUser.getUserId(), 4);
					mentalUser.setAnswer("");
					mentalUser.setCreateTime(-28800000);
					mentalUser.setQueNow(0);
				}
			} else {
				this.setAttribute("tip", "参数错误.");
				return null;
			}
		}
		if (mentalUser.getQueNow() < 8){
			question = service.getQuestion(" que_id=" + (mentalUser.getQueNow() + 1));
		} else {
			this.setAttribute("tip", "over");
			return null;
		}
		return question;
	}
	
	// 答题(同时返回下一题)
	public MentalQuestion writeUserAnswer(int answerId,int questionId){
		MentalQuestion question = null;

		MentalUser mentalUser = getCurrentUser();
		
		if (mentalUser == null){
			this.setAttribute("tip", "用户不存在.");
			return null;
		} else if (mentalUser.getQueNow() >= 8){
			this.setAttribute("tip", "over");
			return null;
		} else {
			String tmp[];
			question = service.getQuestion(" que_id=" + (mentalUser.getQueNow() + 1));
			// 如果输入的题目编号与用户该答的题目编号不符(比如用户点了后退键后再答题)
			if (question.getQueId() != questionId){
				this.setAttribute("tip", "不可修改,请重新答题.");
				return null;
			}
			tmp = question.getAnswer().split("\r\n");
			if (answerId > tmp.length){
				this.setAttribute("tip", "答案编号错误.");
				return null;
			} else {
				SqlUtil.executeUpdate("update mental_user set answer='" + mentalUser.getAnswer() + answerId + "|',que_now=que_now+1 where user_id=" + mentalUser.getUserId(), 4);
				mentalUser.setQueNow(mentalUser.getQueNow()+1);
				if (mentalUser.getQueNow() + 1 > 8){
					// 答完所有的题,计时半年.
					SqlUtil.executeUpdate("update mental_user set create_time=now(),que_now=que_now+1 where user_id=" + mentalUser.getUserId(), 4);
					this.setAttribute("tip", "over");
					return null;
				} else {
					question = service.getQuestion(" que_id=" + (mentalUser.getQueNow() + 1));
				}
			}
		}
		return question;
	}
	
	// 返回测试结果
	public MentalQuestion getUserResult(int mode){
		MentalQuestion question = null;
		if (mode < 1){
			return question;
		}
		question = this.service.getQuestion(" que_id=" + mode);
		return question;
	}
}