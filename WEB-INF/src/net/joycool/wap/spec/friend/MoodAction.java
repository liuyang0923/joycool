package net.joycool.wap.spec.friend;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

public class MoodAction extends CustomAction {

	public static List moodType = new ArrayList();
	public static MoodService service=new MoodService();
	
	int uid = 0;
	int moodId=0;
	boolean result;
	
	static {
		moodType.add("无");
		moodType.add("无聊");
		moodType.add("可爱");
		moodType.add("开心");
		moodType.add("高兴");
		moodType.add("偷笑");
		moodType.add("生气");
		moodType.add("抓狂");
		moodType.add("无奈");
		moodType.add("痛哭");
		moodType.add("诅咒");
		moodType.add("晕~");
		moodType.add("寒~");
		moodType.add("疲倦");
		moodType.add("羞愧");
		moodType.add("惊讶");
		moodType.add("欢呼");
		moodType.add("喜爱");
		moodType.add("得意");
		moodType.add("恐惧");
		moodType.add("囧~");
		moodType.add("呼呼");
		moodType.add("调皮");
		moodType.add("亲亲");
		moodType.add("疑惑");
		moodType.add("沉默");
		moodType.add("沮丧");
		moodType.add("一般");
		moodType.add("奋斗");
		moodType.add("鄙视");
		moodType.add("猪头");
	}
	public static String getMoodTypeName(int i) {
		return (String)moodType.get(i);
	}
	
	public MoodAction(){}
	
	//调用父类的构造方法
	public MoodAction(HttpServletRequest request) {
		super(request);
	}

	public static List getMoodType() {
		return moodType;
	}
	
	public boolean reply(){
		//回复心情
		int id=this.getParameterInt("id");
		MoodBean mb=service.getMoodById(id);
		if(mb==null){
//			try {
//				this.sendRedirect("mood.jsp", response);
				return false;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		uid=mb.getUserId();
		//15秒内只能有一个回复一条
		if(this.isCooldown("chat",15000)){
			String reply=this.getParameterString("reply");
			//过滤
			reply = reply.trim();
			if("".equals(reply)){
				tip="回复不能为空";
				this.setAttribute("tip", tip);
				this.setAttribute("moodId", mb.getId());
				return false;
			}
			result=service.replyById(this.getLoginUser().getId(),id,reply);
			//回复次数加1
			service.increaseReplyCount(id);
			if (result){
				tip="回复成功";
			}else{
				tip="回复失败，请与管理员联系。";
			}
		}else{
			result=false;
			tip="你回复的太快了";
		}
		this.setAttribute("id", mb.getId());
		this.setAttribute("tip", tip);
		return result;
	}
	
	public boolean create(int type){
		//创建心情(要传入心情类型)
		uid = this.getLoginUser().getId();
		if(this.isCooldown("chat",15000)){
			String mood=this.getParameterNoEnter("mood");
			//过滤特殊字符串
			mood = mood.trim();
			if("".equals(mood)){
				tip="心情不可为空";
				this.setAttribute("tip", tip);
				//this.setAttribute("moodId", mb.getId());
				return false;
			}
			result=service.createNewMood(uid,mood,type);
			if (result){
				tip="创建成功";
				//改变状态
				UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
				loginUser.status =String.valueOf(moodType.get(type));
				
				ActionTrend.addTrend(loginUser.getId(), 10, "%1写下%3", 
						loginUser.getNickName(), "心情:" + moodType.get(type), 
						"/beacon/mo/mood.jsp?uid=" + loginUser.getId());
			}else{
				tip="创建失败，请与管理员联系。";
			}
		}else{
			result=false;
			tip="您发表的太快了";
		}
		this.setAttribute("tip", tip);
		return result;
	}
	
	public boolean rdel(){
		//删除一个回复
		int replyId=this.getParameterInt("id");
		MoodReply mr=service.getReplyById(replyId);
		if(mr == null) {
			this.setAttribute("tip", "删除失败");
			return result;
		}
		int	logoUid=this.getLoginUser().getId();		//登陆者ID
		uid=mr.getUserId();								//此心情主人的ID
		moodId=mr.getMoodId();
		MoodBean mood = service.getMoodById(moodId);
		if(mood == null) {
			this.setAttribute("moodId", moodId);
			this.setAttribute("tip", "删除失败");
			return result;
		}
		int ownerUid = mood.getUserId();
		//ID相等才可以删除
		if (logoUid==uid || logoUid == ownerUid){
			result=service.deleteReply(replyId);
			if (result){
				tip="删除成功";
				//回复次数减少1
				service.decreaseReplyCount(moodId);
			}else{
				tip="删除失败";
			}
			this.setAttribute("moodId", moodId);
			this.setAttribute("tip", tip);
			return result;
		}
		return false;
	}
	
	public boolean mdel(){
		//删除一个心情（此心情下的回复一并删除）
		moodId=this.getParameterInt("id");
		MoodBean mb=service.getMoodById(moodId);
		uid=mb.getUserId();
		result=service.deleteMood(moodId,uid);
		if (result){
			tip="删除成功";
		}else{
			tip="删除失败，请与管理员联系。";
		}
		this.setAttribute("tip", tip);
		return result;
	}
	
	public static List getFriendMoodList(int uid) {
		List friendList = UserInfoUtil.getUserFriends(uid);//userService.getUserFriendList(uid);
		if(friendList == null || friendList.size() == 0) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder(256);
		
		sb.append("user_id in(");
		for(int i = 0;i < friendList.size(); i++) {
			String userId = (String)friendList.get(i);
			sb.append(userId);
			if((i + 1) < friendList.size()) {
				sb.append(",");
			}
		}
		
		sb.append(')');
		return service.getMoodUserList(sb.toString());
	}
}