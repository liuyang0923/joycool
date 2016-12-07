package net.joycool.wap.action.pet;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.util.ForbidUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.action.pet.PetUserBean;
/**
 * @author liq
 * @explain：乐宠
 * @datetime:2007-5-31
 */
public class PetAction extends CustomAction {

	UserBean loginUser;

	// 消息系统
	static INoticeService noticeService = ServiceFactory.createNoticeService();

	// 玩家拥有的宠物数量
	public Vector petList;

	// 宠物比赛场次计数器 MATCH[1]是长跑比赛
	public static int MATCH[] = new int[5];

	// 在线用户map
	public static HashMap userMap = new HashMap();
	
	//防套索道具发生作用的几率
	public static int STAGE_PROBABILITY = 50;

	// 用户无动作时间 1小时#
	public static long USER_INACTIVE = 60 * 1000 * 60;

	// 清除长时间无动作游戏
	public static long PET_MATCH_TIME = 1000 * 60 * 60 * 24;

	PetUserBean petUser = null;

	// 数据库类
	public static PetService server = new PetService();

	// load的锁
	public static Integer loadlock = new Integer(5);

	// session中的主键
	public static String PET_USER_KEY = "pet_user_key";

	// 一个帐号最多3个宠物
	public static int MAX_PET = 2;

	public PetAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
		check();
	}

	public PetUserBean getPetUser() {
		return petUser;
	}

	/**
	 * 
	 * @author guip
	 * @explain：宠物管理页面
	 * @datetime:2007-7-26 11:29:34
	 * @return void
	 */
	public void index() {
		if (petUser == null) {
			// 没有得到宠物信息
			if (petList.size() == 0) {
//					 没有宠物
				this.doTip("nopet", "您现在没有宠物，去领养一个吧");
			
			
			} else if (petList.size() >= MAX_PET) {
				// 达到最多宠物上限
				request.setAttribute("petList", petList);
				this.doTip("max", "您有已经达到宠物上限");
			} else {
				// 多个宠物但是未达到最高宠物上限
				request.setAttribute("petList", petList);
				this.doTip("some", "你有多个宠物，可以再领养");
			}
		} else {
			// 已经自动载入了
			this.doTip("success", "成功");
			request.setAttribute("petUser", petUser);
		}
	}

	/**
	 *  
	 * @author guip
	 * @explain：验证
	 * @datetime:2007-7-18 13:06:43
	 * @return void
	 */
	public void check() {
		if (loginUser != null){
			if(ForbidUtil.isForbid("game",loginUser.getId()))
				return;
			petUser = (PetUserBean) session.getAttribute(PET_USER_KEY);
			// session中没有宠物信息
			if (petUser == null) {
				// 搜一下玩家有几个宠物
				petList = server.getUserList("user_id =" + loginUser.getId()+ " and mark = 0 ",
						OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);
				// 此玩家只有一个宠物
				if (petList.size() == 1) {
					petUser = (PetUserBean) petList.elementAt(0);
					petUser = load(0, petUser.getId());
					// 更新fsUser当前session中的引用
					session.setAttribute(PET_USER_KEY, petUser);
					// 设定最新操作时间
					petUser.setLasttime(System.currentTimeMillis());
					// 放入map
					userMap.put(new Integer(petUser.getId()), petUser);
					request.setAttribute("petUser", petUser);
				} else {
	
				}
			} else {
				// session中有宠物信息
				dellMap();
				// 设定最新操作时间
				petUser.setLasttime(System.currentTimeMillis());
				// 更新fsUser当前session中的引用
				session.setAttribute(PET_USER_KEY, petUser);
				// 放入map
				userMap.put(new Integer(petUser.getId()), petUser);
				request.setAttribute("petUser", petUser);
			}
		}
	}

	/**
	 * 
	 * @author liq
	 * @explain：进入后宠物载入
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static PetUserBean load(int user_id, int id) {
		PetUserBean petBean;
		synchronized (loadlock) {
			petBean = (PetUserBean) userMap.get(new Integer(id));
			if (petBean == null) {
				if (user_id == 0) {
					petBean = server.getUser(" id =" + id);
				} else {
					petBean = server.getUser(" user_id = " + user_id
							+ " and id =" + id);
				}
				if (petBean != null) {
					userMap.put(new Integer(petBean.getId()), petBean);
				}
			}
		}
		return petBean;
	}

	/**
	 * 
	 * @author liq
	 * @explain：取得宠物类型的bean
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static PetTypeBean getPetTypeBean(int type){
		PetTypeBean petBean = server.getPet("id =" + type);
		return petBean;
	}
	
	/**
	 * 
	 * @author liq
	 * @explain：传入宠物类型id,取得宠物的名称,如,猫,狗
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static String getPetTypeName(int type){
		PetTypeBean bean = getPetTypeBean(type);
		if(bean != null){
			return bean.getName();
		}else{
			return "宠物";
		}
	}
	
	/**
	 * 
	 * @author liq
	 * @explain：切换用户后的宠物载入
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void loadPet() {
		int id = StringUtil.toInt(request.getParameter("id"));
		petUser = load(loginUser.getId(), id);
		dellMap();
		if (petUser != null) {
			// 更新fsUser当前session中的引用
			session.setAttribute(PET_USER_KEY, petUser);
			// 设定最新操作时间
			petUser.setLasttime(System.currentTimeMillis());
			doTip("success", "领取成功");
		} else {
			doTip(null, "此宠物不归您所有");
		}

		request.setAttribute("petUser", petUser);
	}

	// 判断当前数据是否同数据库中的数据相同
	public void dellMap() {
		if ((petUser != null) && petUser.isChange()) {
			// 数据库修改过
			// 宠物游戏数据
			int temp1 = petUser.getMatchid();
			int temp2 = petUser.getMatchtype();
			// 清空缓存
			// OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP,"SELECT *
			// from pet_user WHERE id =" + petUser.getId());
			// 重新载入数据库中的记录
			int temp_id = petUser.getId();
			userMap.remove(new Integer(temp_id));
			petUser = load(0, temp_id);
			// 重置为未修改
			petUser.setChange(false);
			// 宠物游戏数据
			petUser.setMatchid(temp1);
			petUser.setMatchtype(temp2);

		}
	}

	/**
	 * 
	 * @author liq
	 * @explain：取得宠物图片
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public String getImage() {
		String image = "";
		if (petUser != null) {
			image = Integer.toString(petUser.getType()) + "_";
			if (petUser.getAge() <= 5) {
				image = Integer.toString(petUser.getType()) + "_" + 1 + ".gif";
			} else if ((petUser.getAge() > 5) && (petUser.getAge() <= 20)) {
				image = Integer.toString(petUser.getType()) + "_" + 2 + ".gif";
			} else if ((petUser.getAge() > 20) && (petUser.getAge() <= 80)) {
				image = Integer.toString(petUser.getType()) + "_" + 3 + ".gif";
			} else {
				image = Integer.toString(petUser.getType()) + "_" + 3 + ".gif";
			}
		}
		
		return image;	
		
	}
	/**
	 *  
	 * @author guip
	 * @explain：赠送宠物的首页，好友列表
	 * @datetime:2007-7-27 14:12:17
	 * @return void
	 */
	public void givePetIndex() 	
	{  
		NUMBER_PER_PAGE = 10;
		int petId=StringUtil.toInt(request.getParameter("petId"));
		
		if(petId <=0){
			doTip("failure", "乐宠维护中!!!");
			return;
		}
		//接着判断宠物是否属于loginUser
		if(petUser.getId()!=petId)
		{
			doTip("failure", "此宠物不归您所有!!");
			return;
		}else{
		
		List friendList = UserInfoUtil.getUserFriends(loginUser.getId());
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		int totalHallCount = friendList.size();
		int totalHallPageCount = totalHallCount / NUMBER_PER_PAGE;
		if (totalHallCount % NUMBER_PER_PAGE != 0) {
			totalHallPageCount++;
		}
		if (pageIndex > totalHallPageCount - 1) {
			pageIndex = totalHallPageCount - 1;
		}
//		 取得要显示的消息列表
		
		int start = pageIndex * NUMBER_PER_PAGE;
		int end =  NUMBER_PER_PAGE ;
		if(start<0){start=0;}
		int startIndex = Math.min(start, totalHallCount);
		int endIndex = Math.min(start + end, totalHallCount);
		List friendList1= friendList.subList(startIndex,endIndex);

		String prefixUrl = "givepet.jsp?petId="+petId;
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("totalHallPageCount", new Integer(
				totalHallPageCount));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("friendList",friendList1);
		request.setAttribute("petId",String.valueOf(petId));
		this.doTip("success", "success");
		}
		
		/**guip
		 * 选择赠送的提示，防止不属于自己的宠物赠送
		 */
	}
	public void givePetChoice()
	{
		int userId = StringUtil.toInt(request.getParameter("userId"));
		int petId= StringUtil.toInt(request.getParameter("petId"));
		int id= petUser.getId();
		if(petUser.getId()!=petId)
		{
			doTip("failure", "此宠物不归您所有!!");
			return;
		}else
		{
			request.setAttribute("userId",String.valueOf(userId));
			this.doTip("success", "success");
		}
	}
	/**
	 *  
	 * @author guip
	 * @explain：赠送完毕的结果处理
	 * @datetime:2007-7-27 14:14:45
	 * @return void
	 */
	public void givePet() {
		int userId = StringUtil.toInt(request.getParameter("userId"));
		int petId= StringUtil.toInt(request.getParameter("petId"));
			// 搜一下玩家有几个宠物
		Vector	petList = server.getUserList("user_id =" + userId + " and mark = 0 ",
					OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);
		
		if (petList.size() <= MAX_PET-1) {
//			 更改数据库
			server.updateUser("user_id = " + userId," id = "+ petUser.getId());
//			 清空缓存
			OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_FIVE,
					"SELECT * from pet_user WHERE user_id =" + loginUser.getId() + " and mark = 0 ");
			OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_FIVE,
					"SELECT * from pet_user WHERE user_id =" + userId + " and mark = 0 ");
	        // 清空session中的已删除宠物信息
			petUser.setChange(true);
			session.removeAttribute(PET_USER_KEY);
			//清空map
			petUser.setUser_id(userId);
			request.setAttribute("userId",String.valueOf(userId));
			petUser =load(userId,petId);
			this.doTip("success", "success");
		} else
			doTip("failure", "好友已经达到宠物上限不能接受您的宠物");
	}
	
	
	/**
	 * 
	 * 
	 * @author liq
	 * @explain：购买宠物 mark=0的是用户可用的宠物
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void buyPet() {
		if (petList == null) {
			// 搜一下玩家有几个宠物
			petList = server.getUserList("user_id =" + loginUser.getId()+ " and mark = 0 ",
					OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);
		}
		if (petList.size() < MAX_PET) {
			Vector petList = server.getPetList("mark = 0");
			if (petList == null)
				doTip(null, "乐宠维护中!!!");
			else
				request.setAttribute("petList", petList);
			// 判断用户是否有道具
			int temp = UserBagCacheUtil.getUserBagById(14, loginUser.getId());
			if (temp > 0) {
				Vector hidesPetList = server.getPetList("mark = 1");
				if (hidesPetList == null)
					doTip(null, "乐宠维护中!!!");
				else
					request.setAttribute("hidesPetList", hidesPetList);
			}
			session.setAttribute("petBuyCheck", "ture");
		} else
			doTip("max", "您已经达到宠物上限");
	}
	/**
	 *  
	 * @author guip
	 * @explain：用变形卡换宠物
	 * @datetime:2007-7-26 10:50:20
	 * @return void
	 */
	public void changePet() {
		int tempCard = Integer.parseInt(request.getParameter("tempCard"));
			Vector petList = server.getPetList("mark = 0");
			if (petList == null)
				doTip(null, "乐宠维护中!!!");
			else
				request.setAttribute("petList", petList);
			// 判断用户是否有道具
			int temp = UserBagCacheUtil.getUserBagById(14, loginUser.getId());
			if (temp > 0) {
				Vector hidesPetList = server.getPetList("mark = 1");
				if (hidesPetList == null)
					doTip(null, "乐宠维护中!!!");
				else
					request.setAttribute("hidesPetList", hidesPetList);
			}
	}
	/**
	 *  
	 * @author guip
	 * @explain：判断是否有千里眼道具
	 * @datetime:2007-7-23 14:27:12
	 * @return
	 * @return int
	 */
	public int propPetResult(){
       //判断用户是否有道具
	    int temp = UserBagCacheUtil.getUserBagById(19, loginUser.getId());
		if(temp>0)
		{
			return temp;	
		}	
		return 0;
	}
	public int propPetDistortionCard(){
	       //判断用户是否有变形卡道具
		    int temp = UserBagCacheUtil.getUserBagById(23, loginUser.getId());
			if(temp>0)
			{
				return temp;	
			}	
			return 0;
		}
	public int propPetDistortion(){
	       //判断用户是否有变属性卡道具
		    int temp = UserBagCacheUtil.getUserBagById(22, loginUser.getId());
			if(temp>0)
			{
				return temp;	
			}	
			return 0;
		}
	/**
	 *  
	 * @author guip
	 * @explain：判断是否有改名道具
	 * @datetime:2007-7-24 13:27:49
	 * @return
	 * @return int
	 */
	public int propPetRename(){
	       //判断用户是否有道具
		    int temp = UserBagCacheUtil.getUserBagById(21, loginUser.getId());
			if(temp>0)
			{
				return temp;	
			}	
			return 0;
		}
	/**
	 * 
	 * @author liq
	 * @explain：购买宠物处理页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void buyPetResult() {
//		 防止刷新
		if (session.getAttribute("petBuyCheck") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("petBuyCheck");
		int pettype = StringUtil.toInt(request.getParameter("pettype"));
		// 取得宠物类型
//		PetTypeBean petBean = server.getPet("id =" + pettype);
		PetTypeBean petBean = getPetTypeBean(pettype);
		// 是否取得宠物类型数据,
		if (petBean == null) {
			doTip(null, "领养无效");
			return;
		}

		// 判断是否有权利领取特殊宠物
		if (petBean.getMark() == 1) {
			// 判断用户是否有道具
			int temp = UserBagCacheUtil.getUserBagById(14, loginUser.getId());
			if (temp <= 0) {
				doTip(null, "领养无效");
				return;
			}
			// 删除道具
			UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(), temp);
		}

		// 生成新的userbean
		petUser = new PetUserBean(loginUser.getId(), petBean, loginUser
				.getGender());

		Date date = new Date();

		// 加入数据库
		server.addUser(petUser);

		// 扣钱
		UserInfoUtil.updateUserStatus("game_point=game_point-"
				+ petBean.getPrice(), "user_id=" + loginUser.getId(), loginUser
				.getId(), UserCashAction.OTHERS, "玩家购买乐宠减少"
				+ petBean.getPrice() + "乐币");

		// 清空缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_FIVE,
				"SELECT * from pet_user WHERE user_id =" + loginUser.getId() + " and mark = 0 ");

		// 更新fsUser当前session中的引用
		session.setAttribute(PET_USER_KEY, petUser);
		userMap.put(new Integer(petUser.getId()), petUser);
		request.setAttribute("petUser", petUser);
		result = "success";
		request.setAttribute("result", result);
	}
	/**
	 *  
	 * @author guip
	 * @explain：换宠物的处理
	 * @datetime:2007-7-26 10:51:29
	 * @return void
	 */
	public void changePetResult() {
		int pettype = StringUtil.toInt(request.getParameter("pettype"));
		// 取得宠物类型
		PetTypeBean petBean = getPetTypeBean(pettype);
		// 是否取得宠物类型数据,
		if (petBean == null) {
			doTip(null, "领养无效");
			return;
		}

		// 判断是否有权利领取特殊宠物
		if (petBean.getMark() == 1) {
			// 判断用户是否有道具
			int temp = UserBagCacheUtil.getUserBagById(14, loginUser.getId());
			if (temp <= 0) {
				doTip(null, "领养无效");
				return;
			}
			// 删除道具
			UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(), temp);
		}
		int temp = UserBagCacheUtil.getUserBagById(23, loginUser.getId());
		UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(), temp);

		// 更改数据库
		server.updateUser("type = " + pettype," id = "+ petUser.getId());
//		 清空缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_FIVE,
				"SELECT * from pet_user WHERE user_id =" + loginUser.getId() + " and mark = 0 ");
		// 扣钱
		UserInfoUtil.updateUserStatus("game_point=game_point-"
				+ petBean.getPrice(), "user_id=" + loginUser.getId(), loginUser
				.getId(), UserCashAction.OTHERS, "玩家更换乐宠减少"
				+ petBean.getPrice() + "乐币");
        // 清空session中的已删除宠物信息
		session.removeAttribute(PET_USER_KEY);
		//清空map
		userMap.remove(new Integer(petUser.getId()));
	}
	/**
	 * z
	 * 
	 * @author liq
	 * @explain：积分换道具
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void changeStage() {

	}
	
	/**
	 * z
	 * 
	 * @author liq
	 * @explain：积分换道具
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void changeResult() {
		if(petUser == null){
			this.doTip(null, "您没有携带宠物");
			return;
		}
		
		if(!UserBagCacheUtil.checkUserBagCount(loginUser.getId())){
			this.doTip(null, "您的行囊已满");
			return;
		}
		
		int type = StringUtil.toInt(request.getParameter("type"));
		synchronized(loginUser.getLock()) {
			switch(type)
			{
			//宠物孟婆汤
			case 1:
				if(petUser.getLeftintegral() < 200){
					this.doTip(null, "您的积分点不够200。");
					return;
				}else{
					UserBagCacheUtil.addUserBagCache(loginUser.getId(), 13);
					doTip("success", "兑换成功");
					petUser.setLeftintegral(petUser.getLeftintegral() - 200);
					// 更新数据库
					server.updateUser(" leftintegral =" + petUser.getLeftintegral(), "id = "
							+ petUser.getId());

				}
				break;
			//隐藏宠物卡
			case 2:
				if(petUser.getLeftintegral() < 300){
					this.doTip(null, "您的积分点不够300");
					return;
				}else{
					UserBagBean userBag = new UserBagBean();
					userBag.setUserId(loginUser.getId());
					userBag.setProductId(14);
					userBag.setTypeId(4);
					userBag.setTime(1);
					UserBagCacheUtil.addUserBagCache(loginUser.getId(), 14);
					this.doTip("success", "兑换成功");
					petUser.setLeftintegral(petUser.getLeftintegral() - 300);
					// 更新数据库
					server.updateUser(" leftintegral =" + petUser.getLeftintegral(), "id = "
							+ petUser.getId());

				}
				break;
			//狙击步枪证
			case 3:
				if(petUser.getLeftintegral() < 15){
					this.doTip(null, "您的积分点不够15");
					return;
				}else{
					UserBagCacheUtil.addUserBagCache(loginUser.getId(), 12);
					this.doTip("success", "兑换成功");
					petUser.setLeftintegral(petUser.getLeftintegral() - 15);
					// 更新数据库
					server.updateUser(" leftintegral =" + petUser.getLeftintegral(), "id = "
							+ petUser.getId());

				}
				break;
	         //	千里眼
			case 4:
				if(petUser.getLeftintegral() < 10){
					this.doTip(null, "您的积分点不够10。");
					return;
				}else{
					UserBagCacheUtil.addUserBagCache(loginUser.getId(), 19);
					this.doTip("success", "兑换成功");
					petUser.setLeftintegral(petUser.getLeftintegral() - 10);
					// 更新数据库
					server.updateUser(" leftintegral =" + petUser.getLeftintegral(), "id = "
							+ petUser.getId());
				}
				break;
				 //	变形卡
			case 5:
				if(petUser.getLeftintegral() < 1600){
					this.doTip(null, "您的积分点不够1600。");
					return;
				} else {
					UserBagCacheUtil.addUserBagCache(loginUser.getId(), 23);
					this.doTip("success", "兑换成功");
					petUser.setLeftintegral(petUser.getLeftintegral() - 1600);
					// 更新数据库
							server.updateUser(" leftintegral =" + petUser.getLeftintegral(), "id = " + petUser.getId());
				}
				break;
				 //	打劫卡
	/*		case 6:
				if(petUser.getLeftintegral() < 10){
					this.doTip(null, "您的积分点不够10。");
					return;
				}else{
						// 添加行囊
						UserBagBean userBag = new UserBagBean();
						userBag.setUserId(loginUser.getId());
						userBag.setProductId(20);
						userBag.setTypeId(4);
						userBag.setTime(1);
						if(UserBagCacheUtil.addUserBagCache(userBag)){
						    this.doTip("success", "兑换成功");
						    petUser.setLeftintegral(petUser.getLeftintegral() - 100);
							// 更新数据库
							server.updateUser(" leftintegral =" + petUser.getLeftintegral(), "id = " + petUser.getId());
						}else
							this.doTip(null, "参数错误");
				}
				break;*/
			default:
				this.doTip(null, "参数错误");
				break;
			}
		}
	}
	
	/**
	 * 
	 * @author liq
	 * @explain：查看宠物详细信息
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void petintro() {
		int pettype = StringUtil.toInt(request.getParameter("pettype"));
		PetTypeBean petBean = server.getPet("id =" + pettype);
		request.setAttribute("petBean", petBean);
	}

	public void top() {
		int count = 0;
		// 玩家排名
		if (petUser != null) {
			count = server.getCount(" exp > "
					+ Integer.toString(petUser.getExp()) + " order by id");
			request.setAttribute("count", Integer.toString(count));
			request.setAttribute("petUser", petUser);
		}
		// 按照等级进行排名 1小时缓存
		// 坐庄页码
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		Vector topList = server.getUserList(
				" 1 = 1 and mark = 0 order by exp DESC limit 100",
				OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);
		// 取得总数

		int totalHallCount = topList.size();
		int totalHallPageCount = totalHallCount / NUMBER_PER_PAGE;
		if (totalHallCount % NUMBER_PER_PAGE != 0) {
			totalHallPageCount++;
		}
		if (pageIndex > totalHallPageCount - 1) {
			pageIndex = totalHallPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = Math.min(start + NUMBER_PER_PAGE, totalHallCount);
		Vector vector = new Vector();
		for (int i = start; i < end; i++)
			vector.add(topList.get(i));

		String prefixUrl = "top.jsp?";
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("totalHallPageCount", new Integer(
				totalHallPageCount));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("vector", vector);
	}

	public void todayTop(){

		int count = 0;
		// 玩家排名
		if (petUser != null) {
			count = server.getCount(" today > "
					+ Integer.toString(petUser.getToday()) + " order by id");
			request.setAttribute("count", Integer.toString(count));
			request.setAttribute("petUser", petUser);
		}
		// 按照等级进行排名 1小时缓存
		// 坐庄页码
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		Vector topList = server.getUserList(
				" 1 = 1 and mark = 0 order by today DESC limit 100",
				OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);
		// 取得总数

		int totalHallCount = topList.size();
		int totalHallPageCount = totalHallCount / NUMBER_PER_PAGE;
		if (totalHallCount % NUMBER_PER_PAGE != 0) {
			totalHallPageCount++;
		}
		if (pageIndex > totalHallPageCount - 1) {
			pageIndex = totalHallPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = Math.min(start + NUMBER_PER_PAGE, totalHallCount);
		Vector vector = new Vector();
		for (int i = start; i < end; i++)
			vector.add(topList.get(i));

		String prefixUrl = "todaytop.jsp?";
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("totalHallPageCount", new Integer(
				totalHallPageCount));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("vector", vector);
	
	}
	
	public void info() {
		// 按照等级进行排名 1小时缓存
		// 坐庄页码
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		Vector topList = server.getUserList(
				" 1 = 1 and mark = 0 order by integral DESC limit 50",
				OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);
		// 取得总数

		int totalHallCount = topList.size();
		int totalHallPageCount = totalHallCount / 5;
		if (totalHallCount % 5 != 0) {
			totalHallPageCount++;
		}
		if (pageIndex > totalHallPageCount - 1) {
			pageIndex = totalHallPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得要显示的消息列表
		int start = pageIndex * 5;
		int end = Math.min(start + 5, totalHallCount);
		Vector vector = new Vector();
		for (int i = start; i < end; i++)
			vector.add(topList.get(i));

		String prefixUrl = "info.jsp?";
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("totalHallPageCount", new Integer(
				totalHallPageCount));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("vector", vector);
		// 宠物排行
		// Vector topList = server.getUserList(
		// " 1 = 1 order by integral DESC limit 5",
		// OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);
		// if (topList != null) {
		// request.setAttribute("topList", topList);
		// }

		// 比赛信息
		int temp = MATCH[RUN];
		MatchRunBean matchrunbean;
		Vector match = new Vector(5);
		int number = 0;
		while ((number < 5) && (temp > 0)) {
			matchrunbean = (MatchRunBean) matchMap[RUN].get(new Integer(temp));
			temp--;
			if ((matchrunbean != null) && (matchrunbean.getCondition() != 0)) {
				match.add(matchrunbean);
				number++;
			}
		}
		request.setAttribute("match", match);
	}

	/**
	 * 
	 * @author liq
	 * @explain：职业介绍所页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void work() {
	}

	/**
	 * 
	 * @author liq
	 * @explain：打工处理页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void workResult() {
		int work = StringUtil.toInt(request.getParameter("work"));
		String temp = "";
		int change = 0;
		if ((work < 1) || (work > 3)) {
			this.doTip(null, null);
		} else if (petUser.getHealth() <= 30) {
			this.doTip(null, "您的宠物在重病中，不能再打工了！");
		} else if (petUser.getHungry() <= 30) {
			this.doTip(null, "您的宠物太饿了，没有力气再打工了！");
		} else {
			switch (work) {
			case 1:
				// 刷碗
				// 减少友好度
				petUser.setFriend(petUser.getFriend() - 1);
				// 减少清洁度
				petUser.setClear(petUser.getClear() - 2);
				change = (int) (5 * petUser.getExpMod());
				// 饥饿减一
				petUser.setHungry(petUser.getHungry() - 1);
				temp = "刷碗获得" + change + "点经验，心情-1";
				break;
			case 2:
				// 服务员
				// 减少友好度
				petUser.setFriend(petUser.getFriend() - 2);
				// 减少清洁度
				petUser.setClear(petUser.getClear() - 2);
				change = (int) (7 * petUser.getExpMod());
				// 饥饿减一
				petUser.setHungry(petUser.getHungry() - 1);
				temp = "做服务员获得" + change + "点经验，心情-2";
				break;
			case 3:
				// 搬运工
				// 减少友好度
				petUser.setFriend(petUser.getFriend() - 3);
				// 减少清洁度
				petUser.setClear(petUser.getClear() - 2);
				change = (int) (10 * petUser.getExpMod());
				// 饥饿减一
				petUser.setHungry(petUser.getHungry() - 1);
				temp = "当搬运工获得" + change + "点经验，心情-3";
				break;
			}
			// 如果清洁度小于30的话,加罚生命减1
			if (petUser.getClear() < 30)
				petUser.setHealth(petUser.getHealth() - 1);
			// 增加经验值
			petUser.setExp(petUser.getExp() + change);

			this.doTip("success", temp);
			// 更新数据库
			server.updateUser(" friend =" + petUser.getFriend() + ",exp ="
					+ petUser.getExp() + ",clear =" + petUser.getClear()
					+ ",rank =" + petUser.getRank() + ",hungry ="
					+ petUser.getHungry() + ",health =" + petUser.getHealth()
					+ ",spot =" + petUser.getSpot(), "id = " + petUser.getId());
			flush();
		}
	}

	/**
	 * 
	 * @author liq
	 * @explain：石头剪子布页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void jsb() {
		// 体力低于30就不能玩游戏 了
		if (petUser.getHealth() < 30) {
			this.doTip(null, "您的宠物生病了,不能再陪您玩游戏了!");
		}
	}

	/**
	 * 
	 * @author liq
	 * @explain：石头剪子布处理页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void jsbResult() {
		int jsb = StringUtil.toInt(request.getParameter("jsb"));
		String temp = "";
		String people = "";
		String pet = "";
		int rand = RandomUtil.nextIntNoZero(3);
		switch (jsb) {
		case 1:
			people = "剪刀";
			break;
		case 2:
			people = "石头";
			break;
		case 3:
			people = "布";
			break;
		}
		switch (rand) {
		case 1:
			pet = "剪刀";
			break;
		case 2:
			pet = "石头";
			break;
		case 3:
			pet = "布";
			break;
		}

		if (((rand == 1) && (jsb == 1)) || ((rand == 2) && (jsb == 2))
				|| ((rand == 3) && (jsb == 3))) {
			temp = "打平了";
		} else if (((rand == 1) && (jsb == 2)) || ((rand == 2) && (jsb == 3))
				|| ((rand == 3) && (jsb == 1))) {
			temp = "你赢了";
		} else if (((rand == 3) && (jsb == 2)) || ((rand == 2) && (jsb == 1))
				|| ((rand == 1) && (jsb == 3))) {
			temp = "你输了";
		}

		// 增加友好度
		petUser.setFriend(petUser.getFriend() + 3);

		this.doTip("success", temp);
		request.setAttribute("people", people);
		request.setAttribute("pet", pet);
		request.setAttribute("petUser", petUser);
		server.updateUser(" friend =" + petUser.getFriend(), "id = "
				+ petUser.getId());
		flush();
	}

	/**
	 * 
	 * @author liq
	 * @explain：投掷色子
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void sha() {
		// 体力低于30就不能玩游戏 了
		if (petUser.getHealth() < 30) {
			this.doTip(null, "您的宠物生病了,不能再陪您玩游戏了!");
		}
	}

	/**
	 * 
	 * @author liq
	 * @explain：投掷色子
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void shaResult() {
		String temp = "";
		String str_jsb = "";
		String str_rand = "";
		int people = RandomUtil.nextIntNoZero(6);
		int pet = RandomUtil.nextIntNoZero(6);
		// 宠物胜利
		if (pet > people) {
			temp = "你输了";
			// 人胜利
		} else if (pet < people) {
			temp = "你赢了";
			// 平
		} else {
			temp = "打平了";
		}

		// 增加友好度
		petUser.setFriend(petUser.getFriend() + 3);

		this.doTip("success", temp);
		request.setAttribute("people", Integer.toString(people));
		request.setAttribute("pet", Integer.toString(pet));
		request.setAttribute("petUser", petUser);
		server.updateUser(" friend =" + petUser.getFriend(), "id = "
				+ petUser.getId());
		flush();
	}

	/**
	 * 
	 * @author liq
	 * @explain：医院页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void hospital() {
		int temp = UserBagCacheUtil.getUserBagById(13, loginUser.getId());
		if (temp > 0)
			request.setAttribute("loginUser", Integer.toString(loginUser
					.getId()));
	}

	/**
	 * 
	 * @author liq
	 * @explain医院处理结果页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void hospitalResult() {
		String str = "参数错误";
		int hospital = StringUtil.toInt(request.getParameter("hospital"));
		if(hospital == 5){
			this.clearSpot();
			str = "您可以重新分配升级点了";
			flush();
			request.setAttribute("petUser", petUser);
			this.doTip("success", str);
			return;
		}
		else if(hospital == 6)
		{
			if(this.distortion()){
			str = "您已经把现有飞宠物值全部复制到了您的另一个宠物身上了";
			flush();
			request.setAttribute("petUser", petUser);
			this.doTip("success", str);
			return;
			}else{this.doTip("tip","只有一个宠物不能使用道具");}
		}
		else if (petUser.getHealth() >= 100) {
			str = "您的宠物很健康,不再需要治疗了!";
			this.doTip("success", str);
		}
		
		else{
			UserStatusBean statusBeans = UserInfoUtil.getUserStatus(loginUser
					.getId());
			int temp = 0;
			switch (hospital) {
			case 1:
				if(petUser.getHealth() <= 0){
					this.doTip("success", "赶快去抢救吧!");
					request.setAttribute("petUser", petUser);
					return;
				}else 
				if (statusBeans.getGamePoint() >= 3000) {
					petUser.setHealth(petUser.getHealth() + 5);
					temp = 3000;
					str = "吃药您的宠物恢复了5点生命值";
				} else {
					this.doTip("success", "您的乐币不够");
					request.setAttribute("petUser", petUser);
					return;
				}
				break;
			case 2:
				if(petUser.getHealth() <= 0){
					this.doTip("success", "赶快去抢救吧!");
					request.setAttribute("petUser", petUser);
					return;
				}else 
				if (statusBeans.getGamePoint() >= 5000) {
					petUser.setHealth(petUser.getHealth() + 10);
					temp = 5000;
					str = "打针您的宠物恢复了10点生命值";
				} else {
					this.doTip("success", "您的乐币不够");
					request.setAttribute("petUser", petUser);
					return;
				}
				break;
			case 3:
				if(petUser.getHealth() <= 0){
					this.doTip("success", "赶快去抢救吧!");
					request.setAttribute("petUser", petUser);
					return;
				}else 
				if (statusBeans.getGamePoint() >= 5000) {
					petUser.setHealth(petUser.getHealth() + 30);
					temp = 50000;
					str = "住院您的宠物恢复了30点生命值";
				} else {
					this.doTip("success", "您的乐币不够");
					request.setAttribute("petUser", petUser);
					return;
				}
				break;
			case 4:
				if (statusBeans.getGamePoint() >= 100000) {
					petUser.setHealth(50);
					temp = 100000;
					str = "您的宠物终于被抢救过来了";
				} else {
					this.doTip("success", "您的乐币不够");
					request.setAttribute("petUser", petUser);
					return;
				}
				break;
			default:
				break;
			}
			// 扣钱
			UserInfoUtil.updateUserStatus("game_point=game_point-" + temp,
					"user_id=" + loginUser.getId(), loginUser.getId(),
					UserCashAction.OTHERS, "玩家为乐宠治病减" + temp + "乐币");
			this.doTip("success", str);
			server.updateUser(" health =" + petUser.getHealth(), "id = "
					+ petUser.getId());
			flush();
			request.setAttribute("petUser", petUser);
		}
	}

	/**
	 * 
	 * @author liq
	 * @explain：重置宠物属性点
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void clearSpot() {
		// 判断是否具有重置宠物属性的道具
		int temp = UserBagCacheUtil.getUserBagById(13, loginUser.getId());
		if ((temp > 0) && (petUser != null)) {
			// 取得宠物类型
			PetTypeBean petBean = server.getPet("id =" + petUser.getType());
			if (petBean == null) {
				this.doTip(null, "参数错误");
				return;
			}
			// 重置属性值
			petUser.setAgile(petBean.getAgile());
			petUser.setIntel(petBean.getIntel());
			petUser.setStrength(petBean.getStrength());
			petUser.setSpot((petUser.getRank() - 1) * 10);
			// 更新数据库
			server.updateUser(" agile =" + petUser.getAgile() + ",intel ="
					+ petUser.getIntel() + ",strength ="
					+ petUser.getStrength() + ",spot =" + petUser.getSpot(),
					"id = " + petUser.getId());
			// 删除道具
			UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(), temp);
			flush();
			request.setAttribute("petUser", petUser);
		} else
			this.doTip(null, "参数错误");
	}
	/**
	 *  
	 * @author guip
	 * @explain：把自己的一个宠物的值付给另一个宠物,变属性卡
	 * @datetime:2007-7-25 9:05:20
	 * @return void
	 */
	public boolean distortion()
	{
		//另外的宠物id
		int otherId = 0;
		//取得当前使用的宠物id
		int id = StringUtil.toInt(request.getParameter("petId"));
//		 判断是否具有重置宠物属性的道具
		int temp = UserBagCacheUtil.getUserBagById(22, loginUser.getId());
		if ((temp > 0) && (petUser != null)) {
			// 取得宠物list
			petList = server.getUserList("user_id =" + loginUser.getId()+ " and mark = 0 ",
					OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);
			int size =petList.size();
			if (size <2) {
				this.doTip(tip, "只有一个宠物不能使用道具");
				return false;
			}
			for(int i=0;i<size;i++)
			{
				PetUserBean petUser =(PetUserBean)petList.get(i);
				if(petUser.getId()!=id)
				{
				 otherId = petUser.getId();
				PetUserBean otherUserBean=server.getUser("id="+otherId);
				PetUserBean petUserbean = server.getUser("id="+id);
              //更新数据库另一个宠物的更新
				server.updateUser(" agile =" + petUserbean.getAgile() + ",intel ="
						+ petUserbean.getIntel() + ",strength ="
						+ petUserbean.getStrength() + ",spot =" + petUserbean.getSpot()+",exp = "
						+ petUserbean.getExp()+ ",rank = " + petUserbean.getRank()+",integral = "
						+ petUserbean.getIntegral(),
						"id = " + otherId);
             //更新数据库原来的宠物恢复默认值
				server.updateUser(" agile ="+2+",intel ="+4+",strength ="+2+",spot ="+0+",exp = "+0
						+",rank = "+1+",integral = "+0,"id = " + id);
                  //删除道具
				UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(), temp);
				flush();
//				 清空map中的数据
				userMap.remove(new Integer(otherUserBean.getId()));
				userMap.remove(new Integer(petUserbean.getId()));
				//清缓存
				OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_FIVE);
				return true;
				}else{continue;}
			}
			
		} else
			this.doTip(null, "参数错误");
		return false;
	}

	/**
	 * 
	 * @author liq
	 * @explain：商店页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void shop() {
	}

	/**
	 * 
	 * @author liq
	 * @explain：购买商品处理页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void shopResult() {
		if (petUser.getHealth() <= 0) {
			this.doTip("full", "您的宠物处于死亡状态,喂食也无济于事了,快去医院吧!");
		} else if (petUser.getHungry() >= 100) {
			this.doTip("full", "您的宠物已经很饱了,再也再吃就撑到了!");
		} else {
			UserStatusBean statusBeans = UserInfoUtil.getUserStatus(loginUser
					.getId());
			int shop = StringUtil.toInt(request.getParameter("shop"));
			int temp = 0;
			switch (shop) {
			case 1:
				if (statusBeans.getGamePoint() > 1000) {
					temp = 1000;
					petUser.setHungry(petUser.getHungry() + 5);
				} else {
					this.doTip(null, "您的乐币不够");
				}
				break;
			case 2:
				if (statusBeans.getGamePoint() > 2000) {
					temp = 2000;
					petUser.setHungry(petUser.getHungry() + 10);
				} else {
					this.doTip(null, "您的乐币不够");
				}
				break;
			case 3:
				if (statusBeans.getGamePoint() > 3500) {
					temp = 3500;
					petUser.setHungry(petUser.getHungry() + 15);
				} else {
					this.doTip(null, "您的乐币不够");
				}
				break;
			case 4:
				if (statusBeans.getGamePoint() > 5000) {
					temp = 5000;
					petUser.setHungry(petUser.getHungry() + 20);
				} else {
					this.doTip(null, "您的乐币不够");
				}
				break;
			}

			if (!"failure".equals(result)) {
				// 扣钱
				UserInfoUtil.updateUserStatus("game_point=game_point-" + temp,
						"user_id=" + loginUser.getId(), loginUser.getId(),
						UserCashAction.OTHERS, "玩家为乐宠购买食物减" + temp + "乐币");
				request.setAttribute("petUser", petUser);
				server.updateUser(" hungry =" + petUser.getHungry(), "id = "
						+ petUser.getId());
				flush();
			}
			;
		}
	}

	/**
	 * 
	 * @author liq
	 * @explain：商店页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void chear() {
	}

	/**
	 * 
	 * @author liq
	 * @explain：比赛列表页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void match() {
	}

	/**
	 * 
	 * @author liq
	 * @explain：购买商品处理页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void chearResult() {
		if (petUser.getHealth() <= 0) {
			this.doTip("full", "您的宠物处于死亡状态,不需要再清洁了,快去医院吧!");
		} else if (petUser.getClear() >= 100) {
			this.doTip("full", "您的宠物已经很干净了！！！");
		} else {
			UserStatusBean statusBeans = UserInfoUtil.getUserStatus(loginUser
					.getId());
			int clear = StringUtil.toInt(request.getParameter("clear"));
			int temp = 0;
			switch (clear) {
			case 1:
				if (statusBeans.getGamePoint() > 1000) {
					temp = 1000;
					petUser.setClear(petUser.getClear() + 10);
				} else {
					this.doTip(null, "您的乐币不够");
				}
				break;
			case 2:
				if (statusBeans.getGamePoint() > 2000) {
					temp = 2000;
					petUser.setClear(petUser.getClear() + 20);
				} else {
					this.doTip(null, "您的乐币不够");
				}
				break;
			case 3:
				if (statusBeans.getGamePoint() > 5000) {
					temp = 5000;
					petUser.setClear(petUser.getClear() + 30);
				} else {
					this.doTip(null, "您的乐币不够");
				}
				break;
			}

			if (!"failure".equals(result)) {
				// 扣钱
				UserInfoUtil.updateUserStatus("game_point=game_point-" + temp,
						"user_id=" + loginUser.getId(), loginUser.getId(),
						UserCashAction.OTHERS, "玩家为乐宠购买清洁用品减" + temp + "乐币");
				request.setAttribute("petUser", petUser);
				server.updateUser(" clear =" + petUser.getClear(), "id = "
						+ petUser.getId());
				flush();
			}
			
		}
	}

	/**
	 *  
	 * @author guip
	 * @explain：销户处理页面
	 * @datetime:2007-7-18 11:33:48
	 * @return void
	 */
	public void killResult() {
		int kill = StringUtil.toInt(request.getParameter("kill"));
		if (kill == 2) {
			if(petUser != null) {
	            //修改指定用户的mark标志位
				server.updateUser("mark=1","id = " + petUser.getId());
				// 清空缓存
				OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_FIVE,
						"SELECT * from pet_user WHERE user_id ="
								+ loginUser.getId() + " and mark = 0 ");
				// 清空map中的数据
				userMap.remove(new Integer(petUser.getId()));
	
				// 清空session中的已删除宠物信息
				session.removeAttribute(PET_USER_KEY);
				// 扣钱
				UserInfoUtil.updateUserStatus("game_point=game_point-" + 3000,
						"user_id=" + loginUser.getId(), loginUser.getId(),
						UserCashAction.OTHERS, "玩家乐宠销户减少" + 3000 + "乐币");
			}
			this.doTip("success", "销户成功");

		}
	}

	/**
	 * 
	 * @author guip
	 * @explain：重命名页面
	 * @datetime:2007-7-25 11:29:34
	 * @return void
	 */
	public void rename() {
		//int temp = StringUtil.toInt(request.getParameter("temp"));
       //更改特殊道具为不可用
		//UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(), temp);
	}

	/**
	 * 
	 * @author liq
	 * @explain 游戏页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void game() {
	}

	/**
	 * 
	 * @author liq
	 * @explain：注销宠物页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void kill() {
	}

	/**
	 * 
	 * @author guip
	 * @explain：重命名处理页面
	 * @datetime:2007-7-26 11:29:34
	 * @return void
	 */
	public void renameResult() {
		String name = request.getParameter("name");
		if(name == null)
			name = "";
		else
			name = StringUtil.noEnter(name);
		if (name.length() > 1 && name.length() < 13) {
			if(petUser!=null){
			petUser.setName(name);
			server.updateUser(" name = '" + StringUtil.toSql(petUser.getName()) + "'",
					" id = " + petUser.getId());
			doTip("success", "命名成功");
			}else{
				petList = server.getUserList("user_id =" + loginUser.getId()+ " and mark = 0 ",
						OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);	
				petUser = (PetUserBean) petList.elementAt(0);
				petUser = load(0, petUser.getId());
				// 更新fsUser当前session中的引用
				session.setAttribute(PET_USER_KEY, petUser);
//				 放入map
				userMap.put(new Integer(petUser.getId()), petUser);
				request.setAttribute("petUser", petUser);
				petUser.setName(name);
				server.updateUser(" name = " + "'" + StringUtil.toSql(petUser.getName()) + "'",
						" id = " + petUser.getId());
				doTip("success", "命名成功");
			}
		} else {
			doTip("success", "命名无效");
		}

		flush();
		// 清空缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_FIVE,
				"SELECT * from pet_user WHERE user_id =" + loginUser.getId()+ " and mark = 0 ");

	}

	/**
	 * 
	 * @author liq
	 * @explain：切换宠物处理页面
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void cut() {
		// 移除session中的用户信息
		session.removeAttribute(PET_USER_KEY);
		this.doTip("success", "宠物切换成功");
	}

	public static int involution(int x) {
		// 经验公式
		// (n*n + n)*50 + 100
		return (x * x + x) * 50 + 100;
		// if (x <= 1)
		// return 1;
		// else
		// return 2 << (x - 2);
	}

	/**
	 * 
	 * @author liq
	 * @explain：用户的更新操作后，更新session中的宠物信息
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void flush() {
		int temp = petUser.getId();
		// 清空session中信息
		petUser.setChange(true);
		session.removeAttribute(PET_USER_KEY);
		petUser = load(0, temp);
		// 向session中放入新的宠物信息
		session.setAttribute(PET_USER_KEY, petUser);
	}

	/**
	 * 
	 * @author liq
	 * @explain： 获取所有浮生记在线用户信息
	 * @datetime:2007-3-26 17:49:48
	 * @return
	 * @return HashMap
	 */
	public static HashMap getMap() {
		return userMap;
	}

	/**
	 * 
	 * @author liq
	 * @explain，看其他人的宠物信息,一页显示多个信息
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static void viewotherpet(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		Vector petList = server.getUserList("user_id =" + id + " and mark = 0 ",
				OsCacheUtil.PET_CACHE_FLUSH_PERIOD_FIVE);
		if (petList != null) {
			UserBean user = UserInfoUtil.getUser(id);
			request.setAttribute("user", user);
			request.setAttribute("petList", petList);
		}
	
	}

	/**
	 * 
	 * @author liq
	 * @explain，看其单个宠物信息,只显示一个信息
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static void viewpet(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		PetUserBean petBean = load(0, id);
		if (petBean != null) {
			UserBean user = UserInfoUtil.getUser(petBean.getUser_id());
			request.setAttribute("user", user);
			request.setAttribute("petBean", petBean);
		}
		
	}
	/**
	 *  
	 * @author guip
	 * @explain：用千里眼查看详细信息
	 * @datetime:2007-7-23 14:54:31
	 * @param request
	 * @return void
	 */
	public void viewpetall(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		int temp = StringUtil.toInt(request.getParameter("temp"));
		PetUserBean petBean = load(0, id);
       //		 更改道具的使用次数
		UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(), temp);
		if (petBean != null) {
			UserBean user = UserInfoUtil.getUser(petBean.getUser_id());
			request.setAttribute("user", user);
			request.setAttribute("petBean", petBean);
			
		}
		
	}

	/**
	 * 
	 * @author liq
	 * @explain：宠物加升级点
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void spot() {
		Vector petList = server.getPetList("1 = 1");
		PetTypeBean petBean = new PetTypeBean();
		Iterator iter = petList.iterator();
		while (iter.hasNext()) {
			petBean = (PetTypeBean) iter.next();
			if (petBean.getId() == petUser.getType())
				break;
		}
		request.setAttribute("petBean", petBean);
		request.setAttribute("petUser", petUser);

	}

	/**
	 * 
	 * @author liq
	 * @explain宠物加升级点结果处理
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public void spotResult() {
		int type = StringUtil.toInt(request.getParameter("type"));
		Vector petList = server.getPetList("1 = 1");
		PetTypeBean petBean = new PetTypeBean();
		Iterator iter = petList.iterator();
		while (iter.hasNext()) {
			petBean = (PetTypeBean) iter.next();
			if (petBean.getId() == petUser.getType())
				break;
		}

		switch (type) {
		// 敏捷
		case 1:
			if (petBean.getAl() <= petUser.getSpot()) {
				petUser.setAgile(petUser.getAgile() + 1);
				petUser.setSpot(petUser.getSpot() - petBean.getAl());
			}
			break;
		// 智力
		case 2:
			if (petBean.getIn() <= petUser.getSpot()) {
				petUser.setIntel(petUser.getIntel() + 1);
				petUser.setSpot(petUser.getSpot() - petBean.getIn());
			}
			break;
		// 力量
		case 3:
			if (petBean.getSt() <= petUser.getSpot()) {
				petUser.setStrength(petUser.getStrength() + 1);
				petUser.setSpot(petUser.getSpot() - petBean.getSt());
			}
			break;
		}
		// 更新数据库
		server.updateUser(" agile =" + petUser.getAgile() + ",intel ="
				+ petUser.getIntel() + ",strength =" + petUser.getStrength()
				+ ",spot =" + petUser.getSpot(), "id = " + petUser.getId());
		flush();
		request.setAttribute("petUser", petUser);
	}

	/**
	 * 
	 * @author liq
	 * @explain，每30分钟跑一次，定时处理用户数据
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static void task() {
		// 清空用户宠物列表缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP_FIVE);
		PetService server = new PetService();
		Vector petList = server.getAllUserList("1 = 1");
		PetUserBean user = null;
		Object[] objs = petList.toArray();
		long curtime = System.currentTimeMillis();
		for (int i = 0;i < objs.length;i++) {
			user = (PetUserBean) objs[i];
			if (user.getHealth() > 0) {
				if (user.getHungry() > 50) {
					// 只减饥饿值
					user.setHungry(user.getHungry() - user.getTenacious());
				} else if (user.getHungry() > 0) {
					// 减少饥饿值和生命值
					user.setHungry(user.getHungry() - user.getTenacious());
					user.setHealth(user.getHealth() - 5);
				} else {
					// 只减少生命值
					user.setHealth(user.getHealth() - 5);
				}

				server.updateUser(" hungry =" + user.getHungry() + ", health ="
						+ user.getHealth(), "id = " + user.getId());

				// hashmap中有这个人的记录,通知其重新load数据库
				if (userMap.get(new Integer(user.getId())) != null) {
					PetUserBean temp = (PetUserBean) userMap.get(new Integer(
							user.getId()));
					if (curtime - temp.getLasttime() > USER_INACTIVE) {
						// 超时从map中去掉
						temp.setChange(true);
						userMap.remove(new Integer(user.getId()));
					} else {
						// 没有超时,设置标志位,更新session
						temp.setChange(true);
					}
				}
			}
		}

		// 清空长时间不开始的长跑
		MatchRunBean matchrunbean;
		PetUserBean petBean;
		objs = matchMap[RUN].values().toArray();
		for (int i = 0;i < objs.length;i++) {
			matchrunbean = (MatchRunBean) objs[i];
			// 12小时以上的,未开始的,或者结束的游戏
			if ((curtime - matchrunbean.getCreatetime() > PET_MATCH_TIME)
					&& (matchrunbean.getCondition() != 1)) {
				PlayerBean[] playbean = matchrunbean.getPlayer();
				for (int j = 0; j < playbean.length; j++) {
					// 取得宠物id
					if (playbean[j] != null) {
						petBean = (PetUserBean) userMap.get(new Integer(
								playbean[j].getPetid()));
						// 比赛结束,取消标记,可以参加新的 比赛了
						if ((petBean != null)
								&& (petBean.getMatchid() == matchrunbean
										.getId())
								&& (petBean.getMatchtype() == matchrunbean
										.getType())) {
							petBean.setMatchid(0);
							petBean.setMatchtype(0);
						}
					}
				}
				matchMap[RUN].remove(new Integer(matchrunbean.getId()));
			}
		}

		// 清空长时间不开始的游泳
		objs = matchMap[2].values().toArray();
		for (int i = 0;i < objs.length;i++) {
			matchrunbean = (MatchRunBean) objs[i];
			// 12小时以上的,未开始的,或者结束的游戏
			if ((curtime - matchrunbean.getCreatetime() > PET_MATCH_TIME)
					&& (matchrunbean.getCondition() != 1)) {
				PlayerBean[] playbean = matchrunbean.getPlayer();
				for (int j = 0; j < playbean.length; j++) {
					// 取得宠物id
					if (playbean[j] != null) {
						petBean = (PetUserBean) userMap.get(new Integer(
								playbean[j].getPetid()));
						// 比赛结束,取消标记,可以参加新的 比赛了
						if ((petBean != null)
								&& (petBean.getMatchid() == matchrunbean
										.getId())
								&& (petBean.getMatchtype() == matchrunbean
										.getType())) {
							petBean.setMatchid(0);
							petBean.setMatchtype(0);
						}
					}
				}
				matchMap[2].remove(new Integer(matchrunbean.getId()));
			}
		}

		// 清空长时间不开始的攀岩
		objs = matchMap[4].values().toArray();
		for (int i = 0;i < objs.length;i++) {
			matchrunbean = (MatchRunBean) objs[i];
			// 12小时以上的,未开始的,或者结束的游戏
			if ((curtime - matchrunbean.getCreatetime() > PET_MATCH_TIME)
					&& (matchrunbean.getCondition() != 1)) {
				PlayerBean[] playbean = matchrunbean.getPlayer();
				for (int j = 0; j < playbean.length; j++) {
					// 取得宠物id
					if (playbean[j] != null) {
						petBean = (PetUserBean) userMap.get(new Integer(
								playbean[j].getPetid()));
						// 比赛结束,取消标记,可以参加新的 比赛了
						if ((petBean != null)
								&& (petBean.getMatchid() == matchrunbean
										.getId())
								&& (petBean.getMatchtype() == matchrunbean
										.getType())) {
							petBean.setMatchid(0);
							petBean.setMatchtype(0);
						}
					}
				}
				matchMap[4].remove(new Integer(matchrunbean.getId()));
			}
		}

		
		// 清空长时间不开始赌注游戏
		objs = matchMap[3].values().toArray();
		for (int i = 0;i < objs.length;i++) {
			matchrunbean = (MatchRunBean) objs[i];
			// 12小时以上的,未开始的,或者结束的游戏
			if ((curtime - matchrunbean.getCreatetime() > PET_MATCH_TIME)
					&& (matchrunbean.getCondition() > 1)) {
				matchMap[3].remove(new Integer(matchrunbean.getId()));
			}
		}
		// 清空用户宠物列表缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 长跑游戏的map
	public static TreeMap[] matchMap = { new TreeMap(), new TreeMap(),
			new TreeMap(), new TreeMap(), new TreeMap() };

	// 长跑游戏的类型id
	public static int RUN = 1;

	// 单位时间内，标准的距离
	public static int LONG = 200;

	// 一圈的长度
	public static int CIRCLELONG = 600;

	// 分页数
	public static Integer lockrun = new Integer(5);

	// 分页数
	public static int NUMBER_PER_PAGE = 10;

	// 游戏中设定的玩家人数
	public static int RUN_PLAYNUMBER = 8;

	// 共多少圈
	public static int CIRCLENUMBER = 3;

	public static int TOTAL_LONG = CIRCLELONG * CIRCLENUMBER;

	public static String[] STAGE_STRING = { "火箭靴", "马蜂窝", "套索","蜘蛛网","二踢脚","香蕉皮" };

	public void runing() {
		// 游戏场次id
		int id = StringUtil.toInt(request.getParameter("id"));
		// 游戏类型
		int type = StringUtil.toInt(request.getParameter("type"));
		MatchRunBean matchrunbean = (MatchRunBean) matchMap[RUN]
				.get(new Integer(id));
		if (matchrunbean != null) {
			request.setAttribute("matchrunbean", matchrunbean);
			this.doTip("wait", "游戏中");
		} else {
			this.doTip(null, "此游戏不存在");
		}
		if (petUser != null)
			request.setAttribute("petUser", petUser);
	}

	// 非第一个玩家加入游戏
	public void runIndex() {
		// 坐庄页码
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得总数
		int totalHallCount = matchMap[RUN].size();
		int totalHallPageCount = totalHallCount / NUMBER_PER_PAGE;
		if (totalHallCount % NUMBER_PER_PAGE != 0) {
			totalHallPageCount++;
		}
		if (pageIndex > totalHallPageCount - 1) {
			pageIndex = totalHallPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Iterator iter = matchMap[RUN].values().iterator();
		int number = 0;
		if (totalHallCount > NUMBER_PER_PAGE) {
			for (int i = 0; i < totalHallCount - (pageIndex + 1)
					* NUMBER_PER_PAGE; i++)
				iter.next();
			number = NUMBER_PER_PAGE;
		}
		number = totalHallCount;

		Vector vector = new Vector(number);
		int temp = 0;
		while (iter.hasNext()) {
			vector.add(iter.next());
			temp++;
			if (pageIndex + 1 != totalHallPageCount) {
				if (temp >= NUMBER_PER_PAGE)
					break;
			} else {
				if (temp >= totalHallCount - pageIndex * NUMBER_PER_PAGE)
					break;
			}

		}

		String prefixUrl = "runindex.jsp?type=1";
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("totalHallPageCount", new Integer(
				totalHallPageCount));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("vector", vector);

		request.setAttribute("petUser", petUser);

	}

	// 非第一个玩家加入游戏
	public void matchact() {
		String url = "/pet/runing.jsp?";
		// 游戏场次id
		int task = StringUtil.toInt(request.getParameter("task"));
		// 第一个宠物玩家加入游戏
		if (task == 1) {
			if ((petUser.getHungry() < 50) || (petUser.getClear() < 50)
					|| (petUser.getHealth() < 50) || (petUser.getRank() < 6)) {
				doTip(null, "您的宠物不符合参加比赛的条件!");
				request.setAttribute("url", "/pet/runindex.jsp");
			} else if ((petUser.getMatchid() == 0)
					&& (petUser.getMatchtype() == 0)) {
				// 第一个宠物玩家创立游戏
				// 游戏类型
				int type = StringUtil.toInt(request.getParameter("type"));
				if (type == 1) {

					// 新建一个比赛的bean
					MatchRunBean matchrunbean = new MatchRunBean(RUN_PLAYNUMBER);
					synchronized (lockrun) {
						// 设定长跑比赛计数器,以及treemap的key
						MATCH[RUN]++;
						matchrunbean.setId(MATCH[RUN]);
						// 设定游戏类型
						matchrunbean.setType(type);
						// 将宠物标记为游戏状态
						petUser.setMatchid(matchrunbean.getId());
						// 赛跑类型
						petUser.setMatchtype(RUN);
						// 将宠物放入玩家列表
						matchrunbean.addPlayer(petUser);
						// 将比赛加入长跑比赛的map中
						matchMap[RUN].put(new Integer(matchrunbean.getId()),
								matchrunbean);
					}
					request.setAttribute("matchrunbean", matchrunbean);

					url = url + "id=" + matchrunbean.getId() + "&type="
							+ matchrunbean.getType();
					request.setAttribute("url", url);
					doTip("wait", "wait");
				} else
					doTip(null, "参数错误");
			} else {
				doTip(null, "参数错误");
			}
			// 非第一个宠物玩家加入游戏
		} else if (task == 2) {
			if ((petUser.getHungry() < 50) || (petUser.getClear() < 50)
					|| (petUser.getHealth() < 50) || (petUser.getRank() < 6)) {
				doTip(null, "您的宠物不符合参加比赛的条件!");
				request.setAttribute("url", "/pet/runindex.jsp");
			} else if ((petUser.getMatchtype() == 0)
					&& (petUser.getMatchid() == 0)) {
				// 游戏场次id
				int id = StringUtil.toInt(request.getParameter("id"));
				// 游戏类型
				int type = StringUtil.toInt(request.getParameter("type"));
				synchronized (lockrun) {
					MatchRunBean matchrunbean = (MatchRunBean) matchMap[RUN]
							.get(new Integer(id));
					if (matchrunbean == null) {
						doTip(null, "参数错误");
					} else {
						// 宠物加入游戏
						if (matchrunbean.getCondition() != 0) {
							request.setAttribute("matchrunbean", matchrunbean);
							url = url + "id=" + matchrunbean.getId() + "&type="
									+ matchrunbean.getType();
							request.setAttribute("url", url);
						} else {
							// 将宠物标记为游戏状态
							petUser.setMatchid(matchrunbean.getId());
							// 赛跑类型
							petUser.setMatchtype(RUN);
							// 将宠物放入玩家列表
							matchrunbean.addPlayer(petUser);
							// 判断是否开始游戏
							if (matchrunbean.getPeoplenumber() == RUN_PLAYNUMBER) {
								// 开始游戏
								this.doTip("wait", "游戏中");
								startGame(matchrunbean, loginUser.getId());
							} else
								this.doTip("wait", "等待中");
							request.setAttribute("matchrunbean", matchrunbean);

							url = url + "id=" + matchrunbean.getId() + "&type="
									+ matchrunbean.getType();
							request.setAttribute("url", url);
						}
					}
				}
			} else {
				doTip(null, "只能同時參加一項比賽");
			}
			// 退出游戏
		} else if (task == 3) {
			int id = StringUtil.toInt(request.getParameter("return"));
			// 退出游戏
			if ((id != -1) && (petUser.getMatchtype() != 0)
					&& (petUser.getMatchid() != 0)) {
				// 删除比赛中的数据
				synchronized (lockrun) {
					MatchRunBean matchrunbean = (MatchRunBean) matchMap[RUN]
							.get(new Integer(petUser.getMatchid()));
					if (matchrunbean != null) {

						if (matchrunbean.getCondition() == 0) {
//							// 等待游戏的宠物玩家数量为零时,删除比赛数据
							matchrunbean.exitPlayer(petUser);
							if (matchrunbean.getPeoplenumber() <= 0)
								matchMap[RUN].remove(new Integer(matchrunbean
										.getId()));
							// 删除宠物bean中的游戏状态
							petUser.setMatchid(0);
							petUser.setMatchtype(0);
							
						} else if (matchrunbean.getCondition() == 2) {
							petUser.setMatchid(0);
							petUser.setMatchtype(0);
						}
						request.setAttribute("url", "/pet/runindex.jsp");
					} else {
						doTip(null, "参数错误");
					}
				}

			}
			// 使用道具
		} else if (task == 4) {		// 使用道具
			
			if ((petUser.getMatchtype() != 0) && (petUser.getMatchid() != 0)) {
				MatchRunBean matchrunbean = (MatchRunBean) matchMap[RUN]
						.get(new Integer(petUser.getMatchid()));
				if ((matchrunbean != null)
						&& (matchrunbean.getCondition() == 1)) {
					PlayerBean[] playbean = matchrunbean.getPlayer();
					// 取得是第几名使用道具
					int order = 0;
					for (order = 0; order < RUN_PLAYNUMBER; order++)
						if ((playbean[order] != null)
								&& (playbean[order].getPetid() == petUser
										.getId()))
							break;
					// 如果本人已经过终点线，道具无效
					if (playbean[order].getPosition() < TOTAL_LONG) {
						int item = playbean[order].getStage()[0];	// 正使用的道具
						// order这个人使用道具
						if (item == 1) {
							// 给自己加30米
							playbean[order].setPosition(playbean[order]
									.getPosition() + 30);
							matchrunbean.addLog("嗖一声！"
									+ StringUtil.toWml(playbean[order]
											.getName()) + "腾云驾雾般冲向终点");
							// 给第一的人减30米
							// 此人不是第一名
						} else if (item == 2 && order > 0 && (playbean[0].getPosition() < TOTAL_LONG)) {
							playbean[0].setPosition(playbean[0].getPosition() - 30);
							matchrunbean.addLog(StringUtil
									.toWml(playbean[order].getName())
									+ "把手中的马蜂窝丢到"
									+ StringUtil.toWml(playbean[0].getName())
									+ "的头上，"
									+ StringUtil.toWml(playbean[0].getName())
									+ "被上千只马蜂包围了");
							// 给前边的人减50米
							// 此人不是第一名
						} else if (item == 3 && order > 0 && (playbean[order - 1].getPosition() < TOTAL_LONG)) {
							//判断是否有防弹衣道具
							int temp = UserBagCacheUtil.getUserBagById(18, playbean[order - 1].getUserid());
							if ((temp > 0)&&(RandomUtil.percentRandom(STAGE_PROBABILITY))) {
								// 删除道具
								UserBagCacheUtil.UseUserBagCacheById(playbean[order - 1].getUserid(), temp);
     							matchrunbean.addLog(StringUtil.toWml(StringUtil.toWml(playbean[order - 1].getName())) + "的主人给它穿了防弹衣，套索失效了！");
							}else{
								playbean[order - 1].setPosition(playbean[order - 1]
								     									.getPosition() - 30);
								     							matchrunbean.addLog(StringUtil
								     									.toWml(playbean[order].getName())
								     									+ "用套索套住了"
								     									+ StringUtil.toWml(playbean[order - 1]
								     											.getName())
								     									+ "的脖子，"
								     									+ StringUtil.toWml(playbean[order - 1]
								     											.getName()) + "的速度慢了下来");
							}
						} else if (item == 4 && order > 0) {	//使用蜘蛛网
							int orderPosi = playbean[order].getPosition();
							for (int i = 0; i < order; i++) {
								int posi = playbean[i].getPosition()- orderPosi;

								if (posi <= 100 && posi >=50 && playbean[i].getPosition() < TOTAL_LONG) {
									playbean[i].setPosition(playbean[i].getPosition() - 50);
								}
							}
							matchrunbean.addLog(StringUtil.toWml(playbean[order].getName()) + "撒出蜘蛛网，网住了前面在中路游的好几个家伙!");
						} else if (item == 5) {		//使用二踢脚
							int sid = StringUtil.toInt(request.getParameter("id"));

							if(sid>=0 && sid < RUN_PLAYNUMBER && playbean[sid].getPosition() < TOTAL_LONG)
							{
								playbean[sid].setPosition(playbean[sid].getPosition()-30);
								matchrunbean.addLog(StringUtil.toWml(playbean[order].getName())+"发射二踢脚把"
										+ StringUtil.toWml(playbean[sid]
												.getName()) + "炸上了天!");
							}
							else if(sid == -1){
								doTip("send", "send");
								String url1 = "/pet/player2.jsp?id="+ matchrunbean.getId() + "&type=" + matchrunbean.getType();
								request.setAttribute("url",url1);
								return;
							}

						} else if (item == 6 && order < RUN_PLAYNUMBER-1 && playbean[order+1].getPosition() < TOTAL_LONG) {		// //使用香蕉皮
							playbean[order+1].setPosition(playbean[order+1].getPosition() - 30);
							matchrunbean.addLog(StringUtil.toWml(playbean[order+1].getName())
									+ "踩到了香蕉皮，摔了个四仰八叉!");
						} else {
							this.doTip(null, "参数错误");
						}
					}
					playbean[order].changeStage();
					this.doTip("wait", "游戏中");
					request.setAttribute("matchrunbean", matchrunbean);
					url = url + "id=" + matchrunbean.getId() + "&type="
							+ matchrunbean.getType();
					request.setAttribute("url", url);
				} else
					this.doTip(null, "参数错误");
			}
		}
		request.setAttribute("petUser", petUser);
	}

	// 游戏开始前要做的事
	public void startGame(MatchRunBean matchrunbean, int id) {
		MatchEventBean matchEventBean;
		MatchFactorBean factorBean;
		PetUserBean petBean;
		float change = 0;
		int temp = 0;

		// 开始游戏

		PlayerBean[] playbean = matchrunbean.getPlayer();
		// 计算因子
		factorBean = server.getFactor("id =" + matchrunbean.getType());
		for (int j = 0; j < playbean.length; j++) {
			// 取得宠物id
			// petBean = (PetUserBean) userMap.get(new Integer(playbean[j]
			// .getPetid()));
			// if (petBean == null)
			petBean = load(0, playbean[j].getPetid());
			if (petBean != null) {
				// 加入消息系统,如果是最后进入游戏那个玩家的话,就不发信息
				if (petBean.getUser_id() != id) {
					NoticeBean notice = new NoticeBean();
					notice.setTitle(petBean.getName() + "开始比赛了,快去看吧");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setUserId(petBean.getUser_id());
					notice.setHideUrl("/pet/runindex.jsp");
					notice.setLink("/pet/runing.jsp?id=" + matchrunbean.getId()
							+ "&type=" + matchrunbean.getType());
					// liq_2007-7-16_增加宠物消息类型_start
					notice.setMark(NoticeBean.PET);
					// liq_2007-7-16_增加宠物消息类型_end
					noticeService.addNotice(notice);
				}
				// 属性因子发生作用
				playbean[j].setFactor(factor(petBean, factorBean));

				// 饥饿减少10
				petBean.setHungry(petBean.getHungry() - 10);
				server.updateUser(" hungry =" + petBean.getHungry(), "id = "
						+ petBean.getId());

			}
		}
		matchrunbean.setCondition(1);

	}

	// 计算因子
	public static int factor(PetUserBean petBean, MatchFactorBean factorBean) {
		int x = (int) (petBean.getHealth()
				* (factorBean.getAl() * Math.sqrt(petBean.getAgile())
						+ factorBean.getIn() * Math.sqrt(petBean.getIntel()) + factorBean
						.getSt()
						* Math.sqrt(petBean.getStrength())) / 100);

		return x;
	}

	// 我的比赛
	public void myMatch() {
		if((petUser.getMatchtype() >= matchMap.length)||(petUser.getMatchtype() < 0)){
			// 游戏类型参数错误
			this.doTip(null, "游戏类型参数错误");
			petUser.setMatchid(0);
			petUser.setMatchtype(0);
			return;
		}
		
		MatchRunBean matchrunbean = null;
		try{
		matchrunbean = (MatchRunBean) matchMap[petUser
				.getMatchtype()].get(new Integer(petUser.getMatchid()));
		}catch(Exception ex){
			System.out.println(ex);
			System.out.println("PetAction.myMatch()");
			this.doTip(null, "游戏参数错误");
			petUser.setMatchid(0);
			petUser.setMatchtype(0);
		}
		
		String url = "";
		if (matchrunbean != null) {
			// 判断玩家是否被锁死掉了
			if (ifMyMatch(matchrunbean)) {
				switch (matchrunbean.getType()) {
				case 1:
					url = "/pet/runing.jsp?";
					url = url + "id=" + matchrunbean.getId() + "&type="
							+ matchrunbean.getType();
					request.setAttribute("url", url);
					this.doTip("wait", "游戏中");
					break;
				case 2:
					url = "/pet/swimming.jsp?";
					url = url + "id=" + matchrunbean.getId() + "&type="
							+ matchrunbean.getType();
					request.setAttribute("url", url);
					this.doTip("wait", "游戏中");
					break;
				case 4:
					url = "/pet/climbing.jsp?";
					url = url + "id=" + matchrunbean.getId() + "&type="
							+ matchrunbean.getType();
					request.setAttribute("url", url);
					this.doTip("wait", "游戏中");
					break;
				default:
					// 游戏类型参数错误
					this.doTip(null, "比赛类型参数错误");
					petUser.setMatchid(0);
					petUser.setMatchtype(0);
					break;
				}
			} else {
				// 找不到玩家所属比赛，被锁死
				this.doTip(null, "此游戏不存在");
				petUser.setMatchid(0);
				petUser.setMatchtype(0);
			}
		} else {
			petUser.setMatchid(0);
			petUser.setMatchtype(0);
			this.doTip(null, "游戏不存在");
		}
	}


	public boolean ifMyMatch(MatchRunBean matchrunbean) {
		PlayerBean[] playbean = matchrunbean.getPlayer();
		for (int i = 0; i < playbean.length; i++) {
			if ((playbean[i] != null)&&(playbean[i].getPetid() == petUser.getId())) {
				return true;
			}
		}
		return false;
	}

	public static int getCardRate = 30;	// 第一名获得宠物精英卡的百分比
	/**
	 * 
	 * @author liq
	 * @explain，
	 * @datetime:2007-6-5 11:29:34
	 * @return void
	 */
	public static void runtask() {
		MatchFactorBean factorBean;
		String log;
		Vector eventList;
		MatchEventBean matchEventBean;
		PetUserBean petBean = new PetUserBean();
		float change = 0;
		int factor = 0;
		Iterator iter = matchMap[RUN].values().iterator();
		while (iter.hasNext()) {
			MatchRunBean matchrunbean = (MatchRunBean) iter.next();

			if (matchrunbean.getCondition() == 0) {
				// 建立后等待中
			} else if (matchrunbean.getCondition() == 1) {
				// 取得随机时间列表
				eventList = server.getMatchEventList("gameid ="
						+ matchrunbean.getType());
				// 游戏过程中
				PlayerBean[] playbean = matchrunbean.getPlayer();
				for (int i = 0; i < RUN_PLAYNUMBER; i++) {
					if (playbean[i].getPosition() < TOTAL_LONG) {
						// ////////////////////////////////////////////////////////////////////////////////////////////////////
						// 宠物属性因子发生作用
						// 取得宠物id
						petBean = (PetUserBean) userMap.get(new Integer(
								playbean[i].getPetid()));
						if(petBean == null)
							continue;
						if (playbean[i].getFactor() > 0)
							factor = RandomUtil
									.nextInt(playbean[i].getFactor());
						change = LONG;
						// /////////////////////////////////////////////////////////////////////////////////////////////////
						// 发生了随机事件
						if (RandomUtil
								.percentRandom(MatchEventBean.PROBABILITY)) {
							// 取得随机事件
							// 取得具体的事件
							matchEventBean = (MatchEventBean) eventList
									.get(RandomUtil.nextInt(eventList.size()));
							// 事件对长跑成绩发生影响
							change = change + change
									* matchEventBean.getFactor();

							// 将发生的时间记录对应比赛的log中，在页面上显示
							log = matchEventBean.getDescription().replace(
									"@", StringUtil.toWml(petBean.getName()));
							matchrunbean.addLog(log);
						} else {
							// 没有发生随机事件
						}
						// /////////////////////////////////////////////////////////////////////////////////////////////////
						// 总距离加上单位时间内的距离 以及属性因子的影响
						playbean[i].setPosition(playbean[i].getPosition()
								+ (int) change + factor);

						if (playbean[i].getPosition() >= TOTAL_LONG) {
							matchrunbean.addLog(StringUtil.toWml(petBean
									.getName())
									+ "已经冲过了终点线");
						}
					} else {
						// 过终点后每圈加10000
						playbean[i]
								.setPosition(playbean[i].getPosition() + 10000);
					}
					factor = 0;
				}
				// /////////////////////////////////////////////////////////////////////////////////////////////////
				// 根据宠物的位置排序 0-8对应第一到第八
				Arrays.sort(playbean);

				// 比赛结束,判断排名倒地一的人是否跑完
				if (TOTAL_LONG <= playbean[playbean.length - 1].getPosition()) {
					matchrunbean.setCondition(2);

					for (int j = 0; j < playbean.length; j++) {
						// 取得宠物id
						petBean = (PetUserBean) userMap.get(new Integer(
								playbean[j].getPetid()));
						// 比赛结束,取消标记,可以参加新的 比赛了
						if(petBean == null)
							continue;
						petBean.setMatchid(0);
						petBean.setMatchtype(0);

						// 加入消息系统
						NoticeBean notice = new NoticeBean();
						notice.setTitle(petBean.getName() + "取得了第" + (j + 1)
								+ "名,快去看吧");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setUserId(petBean.getUser_id());
						notice.setHideUrl("/pet/runindex.jsp");
						notice.setLink("/pet/runing.jsp?task=4&id="
								+ matchrunbean.getId() + "&type="
								+ matchrunbean.getType());
						// liq_2007-7-16_增加宠物消息类型_start
						notice.setMark(NoticeBean.PET);
						// liq_2007-7-16_增加宠物消息类型_end
						noticeService.addNotice(notice);
						if (j == 0) {
							// 根据成绩给玩家加积分
							petBean.setIntegral(petBean.getIntegral() + 4);
							petBean.setLeftintegral(petBean.getLeftintegral() + 4);
							petBean.setToday(petBean.getToday() + 4);
							// 更新数据库
							server.updateUser(" integral=integral + 4,leftintegral=leftintegral+4,today=today+4 " , "id = "+ petBean.getId());
							// 第一名有一定概率获得乐酷宠物卡
							if(RandomUtil.percentRandom(getCardRate)) {
								UserBagCacheUtil.addUserBagCacheNotice(petBean.getUser_id(), 55, null);
							}
						} else if (j == 1) {
							petBean.setIntegral(petBean.getIntegral() + 2);
							petBean.setLeftintegral(petBean.getLeftintegral() + 2);
							petBean.setToday(petBean.getToday() + 2);
							// 更新数据库
							server.updateUser(" integral=integral + 2,leftintegral=leftintegral+2,today=today+2 " , "id = "+ petBean.getId());
						} else if (j == 2) {
							petBean.setIntegral(petBean.getIntegral() + 1);
							petBean.setLeftintegral(petBean.getLeftintegral() + 1);
							petBean.setToday(petBean.getToday() + 1);
							// 更新数据库
							server.updateUser(" integral=integral + 1,leftintegral=leftintegral+1,today=today+1 " , "id = "+ petBean.getId());
						}
					}
				}

				// 道具，每圈8个人中挑两个给道具，道具随机
				// 第一名的出现几率和其他人不同
				playbean[0].inputStage(RandomUtil.randomRateInt(randomRate, randomTotal) + 1);
				for(int i = 1;i < RUN_PLAYNUMBER;i++) {
					playbean[i].inputStage(RandomUtil.randomRateInt(randomRate2, randomTotal2) + 1);
				}

			} else {
				// 结束后
			}
		}
	}

	protected static int randomRate[] = {10,0,0,0,10,10};	// 除了第一名的道具出现几率
	protected static int randomRate2[] = {10,3,10,10,10,0};	// 第一名的道具出现几率
	protected static int randomTotal;
	protected static int randomTotal2;
	static {
		randomTotal = RandomUtil.sumRate(randomRate);
		randomTotal2 = RandomUtil.sumRate(randomRate2);
	}
	
}