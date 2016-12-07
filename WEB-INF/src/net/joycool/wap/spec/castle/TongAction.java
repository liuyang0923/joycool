package net.joycool.wap.spec.castle;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class TongAction extends CastleBaseAction {

	public TongAction() {
		// TODO Auto-generated constructor stub
	}

	public TongAction(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	public TongAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		// TODO Auto-generated constructor stub
	}

	//public static Hash
	
	public void createTong(){
		if(this.hasParam("a")) {
			if(request.getParameter("a").equals("g")) {
				if(getUserResBean().getBuildingGrade(ResNeed.EMMBASSY_BUILD) < 3) {
					request.setAttribute("msg", "大使馆要3级才能创建联盟");
					return;
				}
				
				if(CastleUtil.getCastleUser(getUserBean().getId()).getTong() != 0) {
					request.setAttribute("msg", "你已经加入联盟");
					return;
				}
				String n = request.getParameter("n");
				if(n == null || n.length() == 0) {
					request.setAttribute("msg", "联盟名称不能为空");
					return;
				}
				if(StringUtil.getCLength(n) > 10) {
					request.setAttribute("msg", "联盟名称不能超过5个字");
					return;
				}
				TongBean tongBean = new TongBean();
				tongBean.setName(n);
				tongBean.setUid(this.userBean.getId());
				tongBean.setCreateTime(new Date());
				tongBean.setCount(1);
				castleService.addTong(tongBean);
				
				TongPowerBean tongPowerBean = new TongPowerBean();
				tongPowerBean.setPowerName("创始人");
				tongPowerBean.setTongId(tongBean.getId());
				tongPowerBean.setUid(this.userBean.getId());
				tongPowerBean.setPower(TongPowerBean.POWER_ALL);
				castleService.addTongPower(tongPowerBean);
				
				CastleUserBean castleUser = CastleUtil.getCastleUser(this.getUserBean().getId());
				synchronized(castleUser){
					castleUser.setTong(tongBean.getId());
					castleService.updateUserTong(getUserBean().getId(), tongBean.getId());
				}
				request.setAttribute("msg", "创建成功");
				return;
			}
			if(request.getParameter("a").equals("i")) {
				int x = this.getParameterInt("x");
				int y = this.getParameterInt("y");
				
				if(x < 0 || y < 0 || x >= CastleUtil.mapSize || y >= CastleUtil.mapSize) {
					request.setAttribute("msg", "输入坐标不正确");
					return;
				}
				int cid = CastleUtil.getMapCastleId(x, y);
				if(cid == 0) {
					request.setAttribute("msg", "没有该玩家");
					return;
				}
				CastleBean c = CastleUtil.getCastleById(cid);
				if(c == null) {
					request.setAttribute("msg", "没有该玩家");
					return;
				}
				int uid = c.getUid();
				if(CastleUtil.getCastleUser(uid).getTong() != 0) {
					request.setAttribute("msg", "该玩家已经加入联盟");
					return;
				}
				int tongId = CastleUtil.getCastleUser(getUserBean().getId()).getTong();
				
				TongBean tong = CastleUtil.getTong(tongId);
				
				TongPowerBean myPower = this.getCastleService().getTongPowerByUid(this.getLoginUser().getId());
				
				
				if(!myPower.isPowerInvite()) {
					request.setAttribute("msg", "没有该权限");
					return;
				}	
				
				if(castleService.getTongInviteToUid(uid, tongId)) {
					request.setAttribute("msg", "该玩家已经收到邀请");
					return;
				}
				
				CastleUserBean theUserBean = CastleUtil.getCastleUser(tong.getUid());
				List theCastleList = theUserBean.getCastleList();
				int grade = 0;
				if(theCastleList.size() > 0) {
					StringBuilder sb = new StringBuilder();
					for(int i = 0;i < theCastleList.size();i++) {
						if(i != 0)
							sb.append(',');
						sb.append(theCastleList.get(i));
					}
					grade = SqlUtil.getIntResult("select ifnull(max(grade),0) from castle_building where cid in (" + sb.toString() + ") and build_type=25", 5);
				}
				
				int max = grade == 0 ? 0 : ResNeed.getBuildingT(ResNeed.EMMBASSY_BUILD, grade).getValue();
				
				int total = castleService.getTongInviteFromTongCount(tongId);
				
				if(total + tong.getCount() >= max) {
					request.setAttribute("msg", "大使馆等级不足");
				} else {
					TongInviteBean bean = new TongInviteBean(getUserBean().getId(), uid, tongId);
					castleService.addTongInvite(bean);
					request.setAttribute("msg", "邀请成功");
				}
				return;
			}
			if(request.getParameter("a").equals("a")) {
				//int tongId = this.getParameterInt("tid");
				int id = this.getParameterInt("id");
				
				TongInviteBean bean = castleService.getTongInviteBean(id);
				
				if(bean == null || bean.getToUid() != userBean.getId()) {
					request.setAttribute("msg", "联盟邀请不存在");
					return;
				}
				if(getUserResBean().getBuildingGrade(ResNeed.EMMBASSY_BUILD) < 1) {
					request.setAttribute("msg", "你还没有建造大使馆，不能加入联盟");
					return;
				}
				CastleUserBean castleUserBean = CastleUtil.getCastleUser(getUserBean().getId());
				
				if(castleUserBean.getTong() != 0) {
					request.setAttribute("msg", "你已经加入联盟");
					return;
				}
				int tongId = bean.getTongId();
				castleService.deleteTongInvite(id);	// 删除这条邀请，下面如果联盟不存在，邀请也不用保留了
				
				TongBean tong = CastleUtil.getTong(tongId);
				
				if(tong == null) {
					request.setAttribute("msg", "该联盟已经被移除");
					return;
				}
//				UserResBean userResBean = CastleUtil.getUserResBeanByUid(tong.getUid());
//				int grade = userResBean.getBuildingGrade(ResNeed.EMMBASSY_BUILD);
//				int max = ResNeed.getBuildingT(ResNeed.EMMBASSY_BUILD, grade).getValue();
				
//				if(tong.getCount() >= max) {
//					request.setAttribute("msg", "该联盟人数已达到上限");
//				} else {
//					synchronized(castleUserBean) {
						castleUserBean.setTong(tongId);
						castleService.updateUserTong(getUserBean().getId(), tongId);
//					}
					TongBean tongBean = CastleUtil.getTong(tongId);
					
					synchronized(tongBean) {
						tongBean.setCount(tongBean.getCount() + 1);
						castleService.updateTongCount(tongId, 1);
					}
					request.setAttribute("msg", "加入联盟成功");
//				}
				
				return;
			}
			if(request.getParameter("a").equals("d")) {
				int id = this.getParameterInt("id");
				TongInviteBean bean = castleService.getTongInviteBean(id);
				if(bean == null) {
					request.setAttribute("msg", "删除成功");
					return;
				}
				if(bean.getToUid() == castle.getUid()) {	// 被邀请方删除
					castleService.deleteTongInvite(id);
					request.setAttribute("msg", "删除成功");
				} else if(bean.getTongId() == castleUser.getTong()) {	// 邀请方删除
					TongPowerBean myPower = castleService.getTongPowerByUid(castle.getUid());
					
					if(myPower.isPowerInvite()) {		
						castleService.deleteTongInvite(id);
						request.setAttribute("msg", "删除成功");
					} else {
						request.setAttribute("msg", "你没有权限执行该操作");
					}
				} else {
					request.setAttribute("msg", "你没有权限执行该操作");
				}
				return;
			}
		} else {
			if(getUserResBean().getBuildingGrade(ResNeed.EMMBASSY_BUILD) < 3) {
				request.setAttribute("msg", "大使馆要3级才能创建联盟");
				return;
			}
		}
	}
	
	//退出联盟
	public void quitTong() {
		if(this.hasParam("uid")) {
			TongBean tong = CastleUtil.getTong(castleUser.getTong());
			TongPowerBean myPower = getCastleService().getTongPowerByUid(castleUser.getUid());
			if(!myPower.isPowerDelete()) {
				request.setAttribute("msg", "没有权限执行该操作");
				return;
			}
			int uid = this.getParameterIntS("uid");
			CastleUserBean tongUser = CastleUtil.getCastleUser(uid);
			if(tongUser == null || tongUser.getTong() != castleUser.getTong()) {
				request.setAttribute("msg", "该玩家不属于你的联盟");
				return;
			}
			TongPowerBean power = castleService.getTongPowerByUid(uid);
			
			if(power != null && power.getPower() > 0) {
				request.setAttribute("msg", "高层不能被开除");
				return;
			}
			if(power != null)
				castleService.deleteTongPower(uid);
			synchronized(tong) {
				tong.setCount(tong.getCount() - 1);
				castleService.updateTongCount(castleUser.getTong(), -1);
			}
			synchronized(tongUser) {
				tongUser.setTong(0);
				castleService.updateUserTong(uid, 0);
			}
			request.setAttribute("msg", "开除成功");
		} else {
			//CastleUserBean userBean = CastleUtil.getCastleUser(this.getLoginUser().getId());
			if(castleUser.getTong() == 0) {
				request.setAttribute("msg", "你还没有加入联盟");
				return;
			}
			TongBean tong = CastleUtil.getTong(castleUser.getTong());
			
			if(tong == null) {
				request.setAttribute("msg", "该联盟已经被移除");
				return;
			}
			if(tong.getUid() == castleUser.getUid()) {
				if(this.hasParam("pwd")) {
					String pwd = request.getParameter("pwd");
					
					// 获取用户设置信息
					UserSettingBean userSetting = this.getLoginUser().getUserSetting();
					
					if(userSetting != null && userSetting.getBankPw() != null ) {
						if(pwd == null || !pwd.equals(userSetting.getBankPw())) {
							request.setAttribute("msg", "密码不正确");
							return;
						}
					}					
					List list = castleService.getTongUser(tong.getId());
					castleService.deleteTong(tong.getId());
					for(int i = 0; i < list.size(); i ++) {
						Integer uid = (Integer)list.get(i);
						CastleUserBean castleUserBean = CastleUtil.getCastleUserCache(uid.intValue());
						if(castleUserBean != null) {
							castleUserBean.setTong(0);
						}
					}
					request.setAttribute("msg", "移除联盟成功");
					request.setAttribute("del_tong", "d");
				}
			} else {
				synchronized(tong) {
					tong.setCount(tong.getCount() - 1);
					castleService.updateTongCount(castleUser.getTong(), -1);
				}
				synchronized(castleUser) {
					castleUser.setTong(0);
					castleService.updateUserTong(castle.getUid(), 0);
					castleService.deleteTongPower(castle.getUid());
				}
				request.setAttribute("msg", "退出联盟成功");
				request.setAttribute("del_tong", "d");
			}
		}
		
	}
	
	public void updateTong(){
		if(this.hasParam("a")) {
			TongPowerBean myPower = this.getCastleService().getTongPowerByUid(userBean.getId());
			int a = this.getParameterInt("a");
			if(a == 1) {
				if(myPower.isPowerName()) {
					String name = getParameterNoEnter("n");
					if(name == null || name.length() == 0) {
						request.setAttribute("msg", "长度不能为0");
						return;
					}
					
					if(name.length() > 10) {
						request.setAttribute("msg", "长度不能超过10");
						return;
					}
					
					int id = this.getCastleUser().getTong();
					TongBean tong = CastleUtil.getTong(id);
					
					synchronized(tong) {
						tong.setName(name);
						this.getCastleService().updateTongName(myPower.getTongId(), name);
					}
					request.setAttribute("msg", "修改成功");
				} else {
					request.setAttribute("msg", "你没有权限");
				}
			} else if(a == 2) {
				if(myPower.isPowerIntro()) {
					
					String info = request.getParameter("i");
					if(info == null || info.length() == 0) {
						request.setAttribute("msg", "长度不能为0");
						return;
					}
					
					if(info.length() > 50) {
						request.setAttribute("msg", "长度不能超过50");
						return;
					}
					
					int id = this.getCastleUser().getTong();
					TongBean tong = CastleUtil.getTong(id);
					
					synchronized(tong) {
						tong.setInfo(info);
						this.getCastleService().updateTongInfo(myPower.getTongId(), info);
					}
					
					request.setAttribute("msg", "修改成功");
				} else {
					request.setAttribute("msg", "你没有权限");
				}
			}
		}
	}
	
	
	public void deleteTongUser(){
//		if(this.hasParam("uid")) {
//			int uid = this.getParameterInt("uid");
//			if(uid == 0){
//				return;
//			}
//			
//			TongPowerBean power = this.getCastleService().getTongPowerByUid(uid);
//			
//			if(power != null && power.getPower() > 0) {
//				request.setAttribute("msg", "高层不能被开除");
//				return;
//			}
//			
//			TongPowerBean myPower = this.getCastleService().getTongPowerByUid(userBean.getId());
//			
//			TongBean tong = CastleUtil.getTong(this.getCastleUser().getTong());
//			CastleUserBean userBean = CastleUtil.getCastleUser(uid);
//			if(tong == null) {
//				request.setAttribute("msg", "该联盟已经被移除");
//				return;
//			}
//			
//			if(userBean.getTong() == 0) {
//				request.setAttribute("msg", "该玩家还没有加入联盟");
//				return;
//			}
//			
//			
//			if(!myPower.isPowerDelete()) {
//				request.setAttribute("msg", "没有权限执行该操作");
//				return;
//			}
//			
//			castleService.deleteTongPower(uid);
//			
//			synchronized(tong) {
//				tong.setCount(tong.getCount() - 1);
//				castleService.updateTongCount(userBean.getTong(), -1);
//			}
//			synchronized(userBean) {
//				userBean.setTong(0);
//				castleService.updateUserTong(userBean.getUid(), 0);
//			}
//			
//		}
	}
	// 删除联盟协议
	public void deleteAgree() {
		int id = getParameterInt("id");
		TongAgreeBean ta = castleService.getTongAgree("id=" + id);
		int tong = castleUser.getTong();
		
		if(ta == null) {
			tip("tip", "已经解除");
			return;
		}
		
		if(ta.getTongId() != tong) {
			tip("tip", "无法删除");
			return;
		}
		TongPowerBean myPower = castleService.getTongPowerByUid(userBean.getId());
		if(!myPower.isPowerDip()) {
			tip("tip", "没有权限删除");
			return;
		}
		
		DbOperation db = new DbOperation(5);
		db.executeUpdate("delete from castle_agree where id=" + id);
		if(ta.getType() == TongAgreeBean.AGREE_WAR) {
			db.executeUpdate("delete from castle_agree_apply where tong_id=" + ta.getTongId() + " and tong_id2=" + ta.getTongId2());
		} else {
			db.executeUpdate("delete from castle_agree where tong_id=" + ta.getTongId2() + " and tong_id2=" + ta.getTongId());
		}
		db.release();
		tip("tip", "成功删除");
	}
	
	// 删除联盟协议请求
	public void deleteAgreeApply() {
		int id = getParameterInt("id");
		TongAgreeBean ta = castleService.getTongAgreeApply("id=" + id);
		
		if(ta == null) {
			tip("tip", "已经删除");
			return;
		}
		
		int tong = castleUser.getTong();
		if(ta.getTongId() != tong && ta.getTongId2() != tong) {
			tip("tip", "无法删除");
			return;
		}
		TongPowerBean myPower = castleService.getTongPowerByUid(userBean.getId());
		if(myPower == null || !myPower.isPowerDip()) {
			tip("tip", "没有权限删除");
			return;
		}
		
		if(ta.getType() == TongAgreeBean.AGREE_WAR && ta.getTongId() != tong) {
			tip("tip", "无法删除");
			return;
		}

		SqlUtil.executeUpdate("delete from castle_agree_apply where id=" + id, 5);
		tip("tip", "成功删除");
	}
	
	
	//发出请求
	public void addAgreeApply(){
		int x = this.getParameterInt("x");
		int y = this.getParameterInt("y");
		
		int t = this.getParameterInt("t");
		
		TongPowerBean myPower = castleService.getTongPowerByUid(userBean.getId());
		if(myPower == null || !myPower.isPowerDip()) {
			tip("tip", "没有权限申请");
			return;
		}
		
		if(x < 0 || y < 0 || x >= CastleUtil.mapSize || y >= CastleUtil.mapSize) {
			request.setAttribute("msg", "输入坐标不正确");
			return;
		}
		int cid = CastleUtil.getMapCastleId(x, y);
		if(cid == 0) {
			request.setAttribute("msg", "没有该玩家");
			return;
		}
		
		if(cid == 0) {
			request.setAttribute("msg", "没有该玩家");
			return;
		}
		CastleBean c = CastleUtil.getCastleById(cid);
		if(c == null) {
			request.setAttribute("msg", "没有该玩家");
			return;
		}
		int uid = c.getUid();
		
		CastleUserBean user = CastleUtil.getCastleUser(uid);
		
		int tongId = user.getTong();
		if(tongId == 0 || tongId == castleUser.getTong()) {
			request.setAttribute("msg", "指定的联盟不正确");
			return;
		}
		
		TongAgreeBean ta = castleService.getTongAgreeApply("tong_id = " + castleUser.getTong() + " and tong_id2 = " + tongId);
		
		if(ta != null) {
			request.setAttribute("msg", "已经向该联盟发出邀请");
			return;
		}
		
		TongAgreeBean ta2 = castleService.getTongAgree("tong_id = " + castleUser.getTong() + " and tong_id2 = " + tongId);
		
		if(ta2 != null) {
			request.setAttribute("msg", "已经和该联盟有外交关系");
			return;
		}
		
		TongAgreeBean agreeBean = new TongAgreeBean();
		agreeBean.setTongId(castleUser.getTong());
		agreeBean.setTongId2(tongId);
		agreeBean.setType(t);
		
		castleService.addTongAgreeApply(agreeBean);
		
		if(t == TongAgreeBean.AGREE_WAR) {
			castleService.addTongAgree(agreeBean);
		}
		
		request.setAttribute("msg", "申请成功");
	}
	
	
	//接受请求
	public void acceptAgreeApply(){
		int id = getParameterInt("id");
		
		TongPowerBean myPower = castleService.getTongPowerByUid(userBean.getId());
	
		if(myPower == null || !myPower.isPowerDip()) {
			tip("tip", "没有权限申请执行该操作");
			return;
		}
		
		TongAgreeBean ta = castleService.getTongAgreeApply("id=" + id);
		
		if(ta == null) {
			tip("tip", "已经删除或接受");
			return;
		}
		
		int tong = castleUser.getTong();
		if(ta.getTongId2() != tong) {
			tip("tip", "无法接受");
			return;
		}
		
		TongAgreeBean ta2 = castleService.getTongAgree("tong_id = " + tong + " and tong_id2 = " + ta.getTongId());
		
		if(ta2 != null) {
			tip("tip", "和该联盟已经有外交关系");
			DbOperation db = new DbOperation(5);
			db.executeUpdate("delete from castle_agree_apply where id=" + id);
			db.release();
			return;
		}
		
		if(ta.getType() != TongAgreeBean.AGREE_WAR) {
			castleService.addTongAgree(ta);
		}
		
		int tong1 = ta.getTongId();
		ta.setTongId(ta.getTongId2());
		ta.setTongId2(tong1);
		castleService.addTongAgree(ta);
		
		DbOperation db = new DbOperation(5);
		db.executeUpdate("delete from castle_agree_apply where id=" + id);
		db.release();
	
		tip("tip", "接受成功");
	}
	
}
