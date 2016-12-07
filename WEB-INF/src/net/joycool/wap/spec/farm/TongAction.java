package net.joycool.wap.spec.farm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.spec.farm.bean.*;
/**
 * @author zhouj
 * @explain： 帮会
 * @datetime:1007-10-24
 */
public class TongAction extends FarmAction{
	public static FarmWorld world = FarmWorld.one;
	public static FarmNpcWorld nWorld = FarmNpcWorld.one;
	

	public TongAction(HttpServletRequest request) {
		super(request);
		FarmAction.now = System.currentTimeMillis();
	}
	
	public TongAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		FarmAction.now = System.currentTimeMillis();
	}

// 开除
	public void expel() {
		TongUserBean tu = farmUser.getTongUser();
		if(tu == null || tu.getDuty() < 8) {
			tip("tip", "只有帮主和四大法王可以开除弟子");
			return;
		}
		int id = getParameterInt("id");
		FarmUserBean user = world.getFarmUserCache(id);
		if(user == null) {
			tip("tip", "玩家不存在或不在线");
			return;
		}
		TongUserBean tu2 = FarmTongWorld.getTongUser(id);
		if(tu2 == null || tu2.getTongId() != tu.getTongId()) {
			tip("tip", "非本帮弟子");
			return;
		}
		if(tu2.getDuty() >= tu.getDuty() || tu2.getDuty() >= 8) {
			tip("tip", "无法开除此人");
			return;
		}
		FarmTongWorld.leaveTong(user, FarmTongWorld.getTong(tu.getTongId()));
		tip("tip", user.getNameWml() + "被开除出本门派");
	}
	// 邀请加入门派
	public void invite() {
		int id = getParameterInt("id");
		FarmUserBean user = world.getFarmUserCache(id);
		if(user.getTongUser() != null) {
			tip("tip", "对方已经加入其他门派");
			return;
		}
		if(user.getInviteTongUser() != 0) {
			tip("tip", "对方正在被邀请中");
			return;
		}
		TongUserBean tu = farmUser.getTongUser();
		if(tu == null || tu.getDuty() < 8) {
			tip("tip", "无法邀请对方加入");
			return;
		}
		TongBean tong = FarmTongWorld.getTong(tu.getTongId());
		if(tong == null) {
			tip("tip", "不存在的门派");
			return;
		}
		user.setInviteTongUser(farmUser.getUserId());
		tip("tip", "成功邀请" + user.getNameWml() + "加入本门派");
	}
	
	public void join() {
		if(hasParam("r")) {		// 拒绝加入
			farmUser.setInviteTongUser(0);
			tip("tip", "拒绝了加入门派的邀请");
			return;
		}
		if(farmUser.getInviteTongUser() == 0) {
			tip("tip", "没有查询到门派邀请");
			return;
		}
		if(farmUser.getTongUser() != null) {
			tip("tip", "已经加入了门派");
			return;
		}
		FarmUserBean user = world.getFarmUserCache(farmUser.getInviteTongUser());
		if(user == null || user.getTongUser() == null) {
			tip("tip", "邀请还无法生效");
			return;
		}
		TongBean tong = FarmTongWorld.getTong(user.getTongUser().getTongId());
		if(tong == null) {
			tip("tip", "不存在的门派");
			return;
		}
		FarmTongWorld.enterTong(farmUser, tong);
		farmUser.setInviteTongUser(0);
		tip("tip", "应邀加入了[门派]" + tong.getNameWml());
	}
	
	public void leave() {
		TongUserBean tu = farmUser.getTongUser();
		if(tu == null) {
			tip("tip", "不属于任何门派");
			return;
		}
		if(tu.getDuty() >= 8) {
			tip("tip", "帮主和四大法王不能离开!");
			return;
		}

		TongBean tong = FarmTongWorld.getTong(farmUser.getTongUser().getTongId());
		if(tong == null) {
			tip("tip", "不存在的门派");
			return;
		}
		FarmTongWorld.leaveTong(farmUser, tong);
		tip("tip", "离开了[门派]" + tong.getNameWml());
	}
}
