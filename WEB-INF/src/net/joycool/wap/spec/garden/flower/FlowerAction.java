package net.joycool.wap.spec.garden.flower;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.spec.garden.*;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;

public class FlowerAction extends CustomAction {
	
	public static byte[] initLock = new byte[0];
	public static byte[] lock = new byte[0];
	public static FlowerService service = new FlowerService();
	
	// 优惠时长。由左至右分别为:雪山=0，山崖=0.9，平原=0.85，河畔=0.8
	public static float[] abate = { 0, 0, 0.9f, 0.85f, 0.8f };

	int loginUid = this.getLoginUserId();

	GardenUserBean guserBean = null;
	
	public FlowerAction(HttpServletRequest request) {
		super(request);
		FlowerUtil.getFlowerMap();
		FlowerUtil.getTaskMap();
		FlowerUtil.getEspecialMap();
	}

	// 取得登陆用户ID
	public int getLoginUserId() {
		if (this.getLoginUser() != null) {
			return loginUid = this.getLoginUser().getId();
		} else {
			return 0;
		}
	}

	// 返回登陆用户的现金[限制判断]
	public int getGold(int limit) {
		guserBean = GardenUtil.getUserBean(loginUid);
		if (guserBean == null) { 				  // 此用户没开通农场
			return -1;
		} else if (guserBean.getGold() < limit) { // 钱数小于限制
			return -1;
		}
		return guserBean.getGold();
	}

	// 返回登陆用户的现金[只在Action内部调用]
	public int getGold() {
		guserBean = GardenUtil.getUserBean(loginUid);
		return guserBean.getGold();
	}

	// 购买养殖地(buyField.jsp,addField.jsp中调用)
	public boolean buyField(int fieldPrice,int exp){
		boolean result = false;
		synchronized (lock) {
			// 调用GardenService里的方法扣钱
			GardenService gs = GardenService.getInstance();
			result = gs.updateUserGold(loginUid, this.getGold() - fieldPrice);
			guserBean.setGold(this.getGold() - fieldPrice);
			List fieldList = service.getField(loginUid);
			// 用户没买过地
			if (fieldList.size() == 0){
				if (result){
					// 更新用户土地信息(只有第一次玩花之境时才会走到这里)
					result = service.createField(loginUid);
				}
			} else {
				// 扣成就值
				FlowerUtil.updateExp(loginUid, 0-exp);
				if (result) {
					// 更新用户土地信息
					result = service.addField(loginUid, 1);
					result = service.addFieldCount(loginUid);
					FlowerUserBean fub = FlowerUtil.getUserBean(loginUid);
					fub.setFieldCount(fub.getFieldCount() + 1);
					
					// 写日志
					service.insertMessage("(user_id,content,from_uid,readed,create_time,type) values (" + loginUid + ",'购买了第" + (fieldList.size() + 1) + "块土地.',0,0,now(),3)");
				} 				
			}
		}
		return result;
	}
	
	// 升级一块土地
	public boolean updateField(int fieldId,int type,int money,int exp,List fieldList){
		boolean result = false;
		int serial = 0;
		// 看传过来的是用户的第几块地...
		FieldBean fb = null;
		for (int i = 0;i < fieldList.size();i++){
			fb = (FieldBean)fieldList.get(i);
			if (fb.getId() == fieldId){
				serial = i + 1;
				break;
			}
		}
		
		if (serial == 0){
			return result;
		}
		
		// 用户是否有足够的钱
		if (this.getGold() < money){
			this.setAttribute("err", 1);
			return result;
		}

		// 用户是否有足够的成就值
		FlowerUserBean fub = FlowerUtil.getUserBean(loginUid);
		if ((fub.getExp() - fub.getUsedExp()) < exp){
			this.setAttribute("err", 2);
			return result;
		}
		
		// 传回的土地ID是否为登陆用户所有
		fb = service.getFieldById(fieldId);
		if ( fb == null || fb.getUserId() != loginUid){
			this.setAttribute("err", 3);
			return result;			
		}
		
		// 传回的土地上是在否种着花
		if (fb.getField() != 0){
			this.setAttribute("err", 4);
			return result;
		}
		
		// 开始升级
		synchronized (initLock) {
			// 调用GardenService里的方法扣钱
			GardenService gs = GardenService.getInstance();
			result = gs.updateUserGold(loginUid, this.getGold() - money);
			guserBean.setGold(this.getGold() - money);
			// 扣成就值
			result = FlowerUtil.updateExp(loginUid, 0 - exp);
			result = service.changeFieldType(fieldId, type);
			
			// 写日志
			service.insertMessage("(user_id,content,from_uid,readed,create_time,type) values (" + loginUid + ",'将第" + serial +"块地升级为" + FlowerUtil.fieldTypeList.get(type) + ".',0,0,now(),4)");
		}
		return result;
	}
	
	// 初始化花园(fgarden.jsp中调用)
	public void gardenInit(int seedId,int fieldOrder) {
		boolean result = false;
		FieldBean fb = service.getFieldById(fieldOrder);
		if (fb != null){
			if (fb.getField() == 0 && seedId != 0 && this.isContainSeed(seedId)) { 		// 从页面传进了种子ID,并且再次确认用户真的有种子
				if (fb.getId() == fieldOrder){
					result = service.planting(fieldOrder, seedId); 						// 开始种花
				}
				result = service.descSeed(loginUid, seedId, 1); 						// 种数量-1
			}
		}
	}

	// 初始化好友的花园(fgarden2.jsp中调用)
	public FieldBean friendGardenInit(FieldBean fb){
		if (fb != null){
			FlowerUserBean fub = service.getFieldType(fb.getUserId());
			//好友的地里正在种花
			if (fb.getField() != 0){
				int growTime = 0;
				this.setAttribute("flowerName", FlowerUtil.getFlowerName(fb.field));
				growTime = abloomTime(fb.getCreateTime() / 1000, fb.getField(), fb.getType());
				if (growTime <= 0) { // 如果已经开花
					this.setAttribute("needTime", "abloom");
				} else { // 需要的时间
					this.setAttribute("needTime", DateUtil
							.formatTimeInterval3(growTime));
				}
			}
		}
		return fb;
	}
	
	// 采摘(pick.jsp中调用)
	public int pickMyFlower(int id) {
		boolean result = false;
		int growTime = 0;
		FieldBean fb = service.getFieldById(id) ;
		if (fb == null) {
			return 0;
		}
		// 参数判断(防止恶意刷新)
		if (fb.getField() <= 0 || fb.getType() < 1) {
			return 0;
		}
		growTime = abloomTime(fb.getCreateTime() / 1000, fb.getField(), fb.getType());
		// 再次判断用户的花是不是真的开了
		if (growTime <= 0) {
//			this.setAttribute("flowerName", FlowerUtil.getFlowerName(fb.field));
			// 数据库中增加一朵花
			result = service.addFlower(loginUid, fb.getField(), 1);
			// 增加成就值
			FlowerUtil.updateExp(loginUid, FlowerUtil.getFlower(fb.getField()).getGrowExp());
			// 清空土地
			result = service.pickFlower(id);
		}
		return fb.getField();
	}

	// 还剩多少小时开花呢？(pickMyFlower()与fgarden.jsp页面中调用)
	public int abloomTime(long time, int seedId, int fieldType) {
		int growTime = 0;
		// 剩余时间 = ( 种植需要的时长 - (系统现在时间 - 创建时间)) * 优惠 (单位：秒)
		if (fieldType == 1) {
			growTime = (int) (FlowerUtil.getFlower(seedId).getTime() - (System
					.currentTimeMillis() / 1000 - time));
		} else {
			growTime = (int) ((FlowerUtil.getFlower(seedId).getTime() - (System
					.currentTimeMillis() / 1000 - time)) * abate[fieldType]);
		}
		return growTime;
	}

	// 买种子(bflower.jsp中调用)
	public boolean buyFlowerSeed(int id, int count) {
		int money = 0;
		boolean result = false;
		// 加锁，买种子
		synchronized (lock) {
			money = FlowerUtil.getFlower(id).getPrice() * count;
			if (this.getGold() >= money) {
				GardenService gs = GardenService.getInstance();
				// 调用GardenService里的方法扣钱
				result = gs.updateUserGold(loginUid, this.getGold() - money);
				result = service.buyFlowerSeed(loginUid, id, count); // 最后一个参数是买种子的数量
				guserBean.setGold(this.getGold() - money);
			}
		}
		return result;
	}

	// 黄金成果(result.jsp中调用)
	public List flowerCompose(int type,int uid) {
		ArrayList list = new ArrayList();
		List userComposeList = service.getUserCompose("user_id = " + uid);
		List noComposeList = new ArrayList();
		if (type == 1) { 		// 精品
			FlowerComposeBean fcb = null;
			int fid = 0;
			for (int i = 1;i < FlowerUtil.getFlowerTypeList().size();i++){
				if (FlowerUtil.getFlower(i).getType() == 2){
					fid = FlowerUtil.getFlower(i).getId();
					noComposeList.add(new Integer(fid));
				}
			}
			for (int i = 0; i < userComposeList.size(); i++) {
				fcb = (FlowerComposeBean) userComposeList.get(i);
				if (FlowerUtil.getFlower(fcb.getFlowerId()).getType() == 2) {
					list.add(new Integer(fcb.getFlowerId()));
					noComposeList.remove(new Integer(fcb.getFlowerId()));
				}
			}
		} else if (type == 2) { // 珍贵
			FlowerComposeBean fcb = null;
			int fid = 0;
			for (int i = 1;i < FlowerUtil.getFlowerTypeList().size();i++){
				if (FlowerUtil.getFlower(i).getType() == 3){
					fid = FlowerUtil.getFlower(i).getId();
					noComposeList.add(new Integer(fid));
				}
			}
			for (int i = 0; i < userComposeList.size(); i++) {
				fcb = (FlowerComposeBean) userComposeList.get(i);
				if (FlowerUtil.getFlower(fcb.getFlowerId()).getType() == 3) {
					list.add(new Integer(fcb.getFlowerId()));
					noComposeList.remove(new Integer(fcb.getFlowerId()));
				}
			}
		} else if (type == 3) { // 稀有
			FlowerComposeBean fcb = null;
			int fid = 0;
			for (int i = 1;i < FlowerUtil.getFlowerTypeList().size();i++){
				if (FlowerUtil.getFlower(i).getType() == 4){
					fid = FlowerUtil.getFlower(i).getId();
					noComposeList.add(new Integer(fid));
				}
			}
			for (int i = 0; i < userComposeList.size(); i++) {
				fcb = (FlowerComposeBean) userComposeList.get(i);
				if (FlowerUtil.getFlower(fcb.getFlowerId()).getType() == 4) {
					list.add(new Integer(fcb.getFlowerId()));
					noComposeList.remove(new Integer(fcb.getFlowerId()));
				}
			}
		} else if (type == 4) { // 特殊
			FlowerComposeBean fcb = null;
			int fid = 0;
			for (int i = 1;i < FlowerUtil.getFlowerTypeList().size();i++){
				if (FlowerUtil.getFlower(i).getType() == 5){
					fid = FlowerUtil.getFlower(i).getId();
					noComposeList.add(new Integer(fid));
				}
			}
			for (int i = 0; i < userComposeList.size(); i++) {
				fcb = (FlowerComposeBean) userComposeList.get(i);
				// 药品算成品花
				if (FlowerUtil.getFlower(fcb.getFlowerId()).getType() == 5) {
					list.add(new Integer(fcb.getFlowerId()));
					noComposeList.remove(new Integer(fcb.getFlowerId()));
				}
			}
		}
		this.setAttribute("noComposeList", noComposeList);
		return list;
	}

	// 转换合成公式(result.jsp中调用。将"1+3"转换为"菊花+桃花"的形式)
	public StringBuffer getCompoundExp(String exp) {
		StringBuffer compoundExp = new StringBuffer();
		String[] exps = exp.split("\\+");
		try {
			for (int i = 0; i < exps.length; i++) {
				compoundExp.append(FlowerUtil.getFlowerName(Integer.parseInt(exps[i]))
						+ "+");
			}
		} catch (Exception e) {
			return compoundExp;
		}
		// 将最后一个"+"号去掉
		compoundExp = compoundExp.deleteCharAt(compoundExp.length() - 1);
		return compoundExp;
	}

	// 各品种鲜花数量的统计(用于ghose.jsp、lab.jsp与fgarden2.jsp的统计)
	public List flowerStat(int uid) {
		int tmp = 0;
		FlowerStoreBean fsb = null;
		List list1 = new ArrayList(); // 普通鲜花
		List list2 = new ArrayList(); // 精品..
		List list3 = new ArrayList(); // 珍贵..
		List list4 = new ArrayList(); // 稀有..
		List list5 = new ArrayList(); // 特殊物品
		List statList = new ArrayList();
		// 取得库存
		List storeList = service.getUserStore("user_id = " + uid
				+ " and count > 0 and type = 2");
		if (storeList.size() > 0) {
			for (int i = 0; i < storeList.size(); i++) {
				fsb = (FlowerStoreBean) storeList.get(i);
				tmp = fsb.getFlowerId();
				if (fsb.getCount() != 0) {
					if (FlowerUtil.getFlower(tmp).getType() == 1) {
						list1.add(fsb);
					} else if (FlowerUtil.getFlower(tmp).getType() == 2) {
						list2.add(fsb);
					} else if (FlowerUtil.getFlower(tmp).getType() == 3) {
						list3.add(fsb);
					} else if (FlowerUtil.getFlower(tmp).getType() == 4) {
						list4.add(fsb);
					} else if (FlowerUtil.getFlower(tmp).getType() == 5) {
						list5.add(fsb);
					}
				}
			}
			statList.add(list1);
			statList.add(list2);
			statList.add(list3);
			statList.add(list4);
			statList.add(list5);
		}
		return statList;
	}

	// 返回所有种子的数量(bag.jsp中调用)
	public List getStoreSeeds(String cond){
		List tmpList = new ArrayList();
		List seedList = new ArrayList();
		tmpList = service.getSeeds(cond);
		FlowerStoreBean fsb = null;
		for (int i=0;i<tmpList.size();i++){
			fsb = (FlowerStoreBean)tmpList.get(i);
			//如果取得的是种子
			if (fsb.getType() == 1){
				//并且不是商店里买得到的基本的种子
				if (FlowerUtil.getFlower(fsb.getFlowerId()).getType() != 1){
					seedList.add(fsb);
				}
			//如果取得的是花
			} else {
				//并且还是药品
				if (FlowerUtil.getFlower(fsb.getFlowerId()).getType() == 5){
					seedList.add(fsb);
				}
			}
		}
		return seedList;
	}
	
	// 从培养皿中删除一朵花(lab.jsp中频繁调用)
	public boolean deleteFromDish(int d, DishBean dish) {
		boolean result = false;
		if (d == 0){
			return result;
		}
		if (d == 1 && dish.getCultureDish1() != 0) {
			result = service
					.addFlowerFromDish(loginUid, dish.getCultureDish1());
		} else if (d == 2 && dish.getCultureDish2() != 0) {
			result = service
					.addFlowerFromDish(loginUid, dish.getCultureDish2());
		} else if (d == 3 && dish.getCultureDish3() != 0) {
			result = service
					.addFlowerFromDish(loginUid, dish.getCultureDish3());
		} else if (d == 4 && dish.getCultureDish4() != 0) {
			result = service
					.addFlowerFromDish(loginUid, dish.getCultureDish4());
		}
		result = service.deleteFromDishDB(d, loginUid);
		return result;
	}

	// 将培养皿转为List(validate.jsp中调用)
	public List dishToList(DishBean dish) {
		List compList = new ArrayList();
		// 将培养皿Bean中的花朵编号放到List中
		if (dish.getCultureDish1() != 0) {
			compList.add(new Integer(dish.getCultureDish1()));
		}
		if (dish.getCultureDish2() != 0) {
			compList.add(new Integer(dish.getCultureDish2()));
		}
		if (dish.getCultureDish3() != 0) {
			compList.add(new Integer(dish.getCultureDish3()));
		}
		if (dish.getCultureDish4() != 0) {
			compList.add(new Integer(dish.getCultureDish4()));
		}
		return compList;
	}

	/*  合成(comp.jsp中调用)
	 * 	培养皿中的state=0,但时间不为"1970-01-01",表示正在合成，但合成失败。
	 * 	        state=花朵的编号:正在合成，并且合成成功。合成后的花朵编号，就是state编号。
	 */
	public boolean doCompound() {
		boolean result = false;
		String tmp = "";
		String[] tmpSplit;
		DishBean dish = FlowerUtil.getUserDish(loginUid);
		List compList = this.dishToList(dish);

		// 开始合成
		if (compList.size() != 0) {
			for (int i = 1; i <= FlowerUtil.flowerList.size(); i++) {
				if (!"".equals(FlowerUtil.getFlower(i).getCompose())){
					tmp = FlowerUtil.getFlower(i).getCompose();
					// 如果公式中的花朵个数与用户培养皿中的花园数相等
					if (tmp.split("\\+").length == compList.size()) {
						tmpSplit = tmp.split("\\+");
						result = true;
						for (int j = 0; j < tmpSplit.length; j++) {
							if (!compList
									.contains(new Integer(tmpSplit[j]))) {
								result = false;
								break;
							}
						}
						if (result) {
							FlowerUtil.getFlower(i).getCompTime();
							result = SqlUtil.executeUpdate("update flower_culture_dish set state = " + i + ", time = now() where user_id = " + loginUid, 5);
							dish.setState(i);
							dish.setTime(System.currentTimeMillis());
//							// 写入管理员记录
//							FlowerLog log = new FlowerLog();
//							log.setFlag(0);
//							log.setUserId(loginUid);
//							log.setContent("[成功]开始合成" + FlowerUtil.getFlowerName(dish.getState()) + ".");
//							service.addNewLog(log);
//							return result;
						}
					}
				}
			}
		}
		result = SqlUtil.executeUpdate("update flower_culture_dish set time = now() where user_id = " + loginUid, 5);
		dish.setTime(System.currentTimeMillis());
//		// 写入管理员日志
//		FlowerLog log = new FlowerLog();
//		log.setFlag(0);
//		log.setUserId(loginUid);
//		log.setContent("[失败]开始合成试验.");
//		service.addNewLog(log);
		return result;
	}
	
	/*  合成,并且清空培养皿(comp.jsp中调用)
	 * 	培养皿中的state=0：没有进行合成
	 * 	        state=-1:正在进行合成，不过合成失败
	 *   state=花朵的编号:正在合成，并且合成成功。合成后的花朵编号，就是state编号
	 */
	public int doCompound2() {
//		System.out.println("用户ID:" + loginUid + " 调用doCompound2");
		//再次查看用户是否真的在合成，并且合成时间是否已过。
		DishBean dish = FlowerUtil.getUserDish(loginUid);
		List compList = this.dishToList(dish);
		if (dish.getState() == 0 && dish.getTime() == 0){
			// 没有合成
			return -1;
		} else if (dish.getState() == 0){		// 合成失败
			if ((FlowerUtil.COMP_FAIL_TIME - (System.currentTimeMillis() - dish.getTime())) <=0 ){
				// 合成时长已过
				// 删除相应的花
				for (int i = 0; i < compList.size(); i++) {
					service.descSeed(loginUid, ((Integer) compList.get(i)).intValue(),1);
				}
				// 清空培养皿
				service.clearDish(loginUid);
				// 清空培养皿的Bean
				dish.clearDish();
				// 增加经验值
				FlowerUtil.updateExp(loginUid, FlowerUtil.COMP_FAIL_EXP);
//				// 写入管理员记录
//				FlowerLog log = new FlowerLog();
//				log.setFlag(0);
//				log.setUserId(loginUid);
//				log.setContent("合成失败.");
//				service.addNewLog(log);
				// 向log中写入合成失败记录
				service.insertMessage("(user_id,content,from_uid,readed,create_time,type) values (" + loginUid + ",'合成实验失败.',0,0,now(),1)");
				return 0;
			} else {
				// 合成时长未过
				return -1;
			}
		} else if (dish.getState() > 0){		// 合成成功
			if ((FlowerUtil.getFlower(dish.getState()).getCompTime() - (System
					.currentTimeMillis() - dish.getTime())) <=0 ){
				int flowerId = dish.getState();
				// 合成时长已过
				this.setAttribute("compList", compList);
				// 清空培养皿
				service.clearDish(loginUid);
				// 清空培养皿的Bean
				dish.clearDish();
				if (FlowerUtil.getFlower(flowerId).getType() != 5) {
					// 增加种子数(与买种子的方法相同)
					service.buyFlowerSeed(loginUid, flowerId, 1);
				} else {
					// 药品要放到仓库中
					service.addFlower(loginUid, flowerId, 1);
				}
				// 将公式记录到数据库中
				if (!service.isContainCompose(loginUid, flowerId)){
					service.addCompose("(user_id,flower_id,create_time) values (" + loginUid + "," + flowerId + "," + "now() )");
				}
				// 增加经验值
				FlowerUtil.updateExp(loginUid, FlowerUtil.getFlower(flowerId).getSuccessExp());
//				// 写入管理员记录
//				FlowerLog log = new FlowerLog();
//				log.setFlag(0);
//				log.setUserId(loginUid);
//				log.setContent("合成" + FlowerUtil.getFlowerName(flowerId) + "成功.");
//				service.addNewLog(log);
				// 向log中写入合成成功记录
				service.insertMessage("(user_id,content,from_uid,readed,create_time,type) values (" + loginUid + ",'合成" + FlowerUtil.getFlowerName(flowerId) + "成功.',0,0,now(),1)");
				return flowerId;
			} else {
				// 合成时长未过
				return -1;
			}
		}
		// 向log中写入合成失败记录
		service.insertMessage("(user_id,content,from_uid,readed,create_time,type) values (" + loginUid + ",'合成实验失败.',0,0,now(),1)");
		return 0;
	}

	// 取得库存中药品(store.jsp中调用)
	public List getDrug() {
		FlowerStoreBean fsb = null;
		List drugList = new ArrayList();
		List storeList = service.getUserStore("user_id = " + loginUid
				+ " and type in (2,3)");
		for (int i = 0; i < storeList.size(); i++) {
			fsb = (FlowerStoreBean) storeList.get(i);
			if (FlowerUtil.getFlower(fsb.getFlowerId()).getType() == 5) {
				drugList.add(fsb);
			}
		}
		return drugList;
	}

	// 花市，卖出鲜花
	public boolean sellFlower(int flowerId,int price) {
		boolean result = false;
		// 再检查一遍用户是否有此花
		if (isContainFlower(flowerId)){
			synchronized (lock){
				FlowerBean fb = FlowerUtil.getFlower(flowerId);
				int lowPrice = fb.getPrice();
				int heightPrice = lowPrice * 3;
				if (price < lowPrice || price > heightPrice){
					this.setAttribute("tip", "你所填写的鲜花价格已经与要求不符,请重新填写.");
					return result;
				}
				if (price == 0 || price > 999999){
					this.setAttribute("tip", "价格输入错误.");
					return result;
				}
				result = service.sellFlower(" (flower_id,type,price,state,seller_uid,release_time) values (" + flowerId + "," + fb.getType() + "," + price + ",1," + loginUid + ",now())");
				result = service.descFlower(loginUid, flowerId, 1);
			}
		} else {
			request.setAttribute(tip, "你没有此花.");
			return result;
		}
		return result;
	}
	
	// 花市、购买鲜花
	public boolean buyFlower(){
		boolean result = false;
		/*
		 * 这里会有一些代码...
		 */
		synchronized (lock){
			GardenService gs = GardenService.getInstance();
			result = gs.updateUserGold(loginUid, this.getGold() /* - 一朵花的价格 */);
			guserBean.setGold(this.getGold() /* - 一朵花的价格 */);
			/*
			 * 扣钱的代码在这里...
			 */
		}
		return result;
	}
	
	// 检测表中是否包含一个指定的种子(fgarden.jsp，验证参数“s”时调用)
	public boolean isContainSeed(int flowerId) {
		return SqlUtil.getIntResult(
				"select id from flower_store where user_id=" + loginUid
						+ " and flower_id = " + flowerId
						+ " and type=1 and count>0", 5) > 0;
	}
	
	// 检测列表中是否包含一个指定的种子(lab.jsp，market2.jsp中验证参数“flowerId”时调用)
	public boolean isContainFlower(int flowerId) {
		return SqlUtil.getIntResult(
				"select id from flower_store where user_id=" + loginUid
						+ " and flower_id = " + flowerId
						+ " and type=2 and count>0", 5) > 0;
	}
	
	// 计算花的总量(lab.jsp中调用)
	public int getFlowerCount(ArrayList list) {
		int tmp = 0;
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				FlowerStoreBean fsb = (FlowerStoreBean) list.get(i);
				tmp += fsb.getCount();
			}
		}
		return tmp;
	}

	// 偷！！！
	public boolean stealLeaf(int fieldId,int friendId,int maxStealCount,int minute,int exp){
		// 取得朋友的地
		int growTime = 0;
		boolean result = false;
		FieldBean fb = service.getFieldById(fieldId);
		FlowerUserBean fub = FlowerUtil.getUserBean(friendId);	//朋友的userBean
		FlowerUserBean fub2 = FlowerUtil.getUserBean(loginUid);	//"我"的userBean
		if ( fb == null || fub == null ){
			this.setAttribute("tip", "土地不存在.");
			return result;
		} else if (fb.getUserId() != friendId){
			// 土地的所有者与传入的好友ID不相符
			this.setAttribute("tip", "此土地的主人不是你的好友.");
			return result;
		} else if((fub2.getStealCount() >= 10) && (DateUtil.dayDiff(new Date(),new Date(fub2.getStealTime()))) == 0){
			// 每个用户一天最多只能踩10个好友的地
			this.setAttribute("tip", "你今天已经捣蛋十次了,难道真的想成为捣蛋大王吗?还是明天再来吧!");
			return result;
		} else {
			// 看他地上的花开了没
			growTime = abloomTime(fb.getCreateTime() / 1000, fb.getField(),fb.getType());
			// 花还没开呢，可以偷   
			if (growTime > 0){
				synchronized (lock){
					int dayCount = 0;
					if (fub2.getStealTime() != 0){
						dayCount = DateUtil.dayDiff(new Date(),new Date(fub2.getStealTime()));
					}
					if (dayCount == 0){
						// 如果是今天，并且被偷的次数 < maxStealCount，偷!
						if (fub.getStealCount2() < maxStealCount){
							if (!fub.getStealList().contains(new Integer(loginUid))){
								result = SqlUtil.executeUpdate("update flower_field set create_time = date_add(create_time,INTERVAL '" + minute + "' MINUTE) where id = " + fieldId, 5);
								result = service.insertMessage("(user_id,content,from_uid,readed,create_time) values (" + friendId + ",'的花园踩踏1次'," + loginUid + ",0,now())");
								result = FlowerUtil.updateExp(loginUid, exp);
								fub.getStealList().add(new Integer(loginUid));
								fub.setStealTime2(System.currentTimeMillis());	// 好友被踩的时间
								fub.setStealCount2(fub.getStealCount2() + 1);	// 好友被踩的次数
								fub2.setStealTime(System.currentTimeMillis());	// 记录“我”踩的时间
								fub2.setStealCount(fub2.getStealCount() + 1);	// 记录“我”踩的次数
//								System.out.println("我被踩了==" + fub.getStealCount2() + "== 次");
							} else {
								this.setAttribute("tip", "你已经踩过他的地了.");
								result = false;
							}
						} else {
							this.setAttribute("tip", "我的地已被踩踏了" + maxStealCount + "次了.行行好吧,不要再踩了！");
//							result = service.updateFlowerUser("set exp = exp +" + exp  + " where user_id =" + loginUid);
//							result = false;
						}
					} else {
						// 不是同一日期，偷的次数=1,并更新日期
						if (DateUtil.dayDiff(new Date(),new Date(fub.getStealTime2())) ==0){
							if (fub.getStealCount2() > maxStealCount){
								this.setAttribute("tip", "我的地已被踩踏了" + maxStealCount + "次了.行行好吧,不要再踩了！");
								return false;
							} else {
								fub.setStealTime2(System.currentTimeMillis());	// 好友被踩的时间
								fub.setStealCount2(fub.getStealCount2() + 1);	// 好友被踩的次数
							}
						} else {
							fub.setStealTime2(System.currentTimeMillis());	// 好友被踩的时间
							fub.setStealCount2(1);							// 好友被踩的次数
						}
						result = SqlUtil.executeUpdate("update flower_field set create_time = date_add(create_time,INTERVAL '" + minute + "' MINUTE) where id = " + fieldId, 5);
						result = service.insertMessage("(user_id,content,from_uid,readed,create_time) values (" + friendId + ",'的花园踩踏1次'," + loginUid + ",0,now())");
						result = FlowerUtil.updateExp(loginUid, 1);
						fub.getStealList().clear();
						fub2.setStealTime(System.currentTimeMillis());	// 记录“我”踩的时间
						fub2.setStealCount(1);							// 记录“我”踩的次数
//						fub.getStealList().add(new Integer(friendId));
						fub.getStealList().add(new Integer(loginUid));
					}
				}
			}
		}
		return result;
	}
	
	// 检查任务是否完成(task.jsp调用)
	public FlowerUserBean checkTask(FlowerUserBean fub){
		if (fub != null){
			FlowerTask ft = FlowerUtil.getTaskInfo(fub.getTaskNum());
			switch(ft.getType()){
				case 1:{		// 第一类任务，检查是否有某个种子
					int id = SqlUtil.getIntResult("select id from flower_store where flower_id = " + ft.getFlowerId() + " and count > 0 and type = 1 and user_id = " + loginUid,5);
					if ( id > 0 ){
						// 领取奖励
						if (!fub.isTaskComplete()){
							fub = addMissionExp(fub);
							fub.setTaskComplete(true);
						}
						return fub;
					}
					break;
				}
				case 2:{		// 第二类任务，检查是否有某朵花
					int id = SqlUtil.getIntResult("select id from flower_store where flower_id = " + ft.getFlowerId() + " and count > 0 and type = 2 and user_id = " + loginUid,5);
					if ( id > 0 ){
						if (!fub.isTaskComplete()){
							fub = addMissionExp(fub);
							fub.setTaskComplete(true);
						}
						return fub;
					}					
					break;
				}
				case 3:{		// 第三类任务，检查是否完成了某种操作
					switch(ft.getFlowerId()){
						case 1:{	//1:是否完成了踩踏动作
							int id = SqlUtil.getIntResult("select id from flower_message where from_uid = " + loginUid,5);
							if ( id > 0 ){
								if (!fub.isTaskComplete()){
									fub = addMissionExp(fub);
									fub.setTaskComplete(true);
								}
								return fub;
							}
							break;
						}
						case 2:{	//2:是否使用了催化剂
							int id = SqlUtil.getIntResult("select id from flower_message where user_id = " + loginUid + " and type = 2",5);
							if ( id > 0 ){
								if (!fub.isTaskComplete()){
									fub = addMissionExp(fub);
									fub.setTaskComplete(true);
								}
								return fub;
							}
							break;
						}
						case 3:{	//3:是否升级了土地
							FieldBean fb = null;
							boolean result = false;
							List list = service.getField(loginUid);
							for (int i = 0;i < list.size();i++){
								fb = (FieldBean)list.get(i);
								if (fb.getType() > 1){
									result = true;
									break;
								}
							}
							if ( result ){
								if (!fub.isTaskComplete()){
									fub = addMissionExp(fub);
									fub.setTaskComplete(true);
									// 送一个稀有催化剂
									result = service.addSpecial(loginUid, 4, 1);
								}
								return fub;
							}
							break;
						}
					}
					break;
				}
			}
		}
		return fub;
	}
	
	// 完成任务，增加成就
	public FlowerUserBean addMissionExp(FlowerUserBean fub){
		int exp = FlowerUtil.getTaskInfo(fub.getTaskNum()).getExp();
		FlowerUtil.updateExp(loginUid, exp);
//		boolean result = service.updateFlowerUser("set exp = exp + " + exp + " where user_id = " + loginUid);
		return fub;
	}
	
	// 下一个任务(task.jsp中调用)
	public FlowerUserBean toNextTask(FlowerUserBean fub){
		// 再判断一次是否真的完成了任务
		fub = checkTask(fub);
		if (fub != null && fub.isTaskComplete()){
			// 最后一个任务做完后id不用+1
			if (fub.getTaskNum() <= FlowerUtil.getTaskMap().size()){
				fub.setTaskComplete(false);
				fub.setTaskNum(fub.getTaskNum() + 1);
				boolean result = service.updateFlowerUser("set task_num = task_num + 1 where user_id = " + loginUid);
			}
		}
		return fub;
	}
	
	// 买特殊物品
	public boolean buySpecialGoods(int count,int esp){
		boolean result = false;
		synchronized (lock){
			EspecialBean espBean = FlowerUtil.getEspecial(esp);
			
			// 先检查金钱是否足够
			if (this.getGold() < espBean.getMoney() * count){
				this.setAttribute("tip", "你没有足够的钱币.");
				return result;
			}
			
			// 再检查成就值是否足够
			FlowerUserBean fub = FlowerUtil.getUserBean(loginUid);
			if ((fub.getExp() - fub.getUsedExp()) < espBean.getExp() * count){
				this.setAttribute("tip", "你没有足够的成就值.");
				return result;
			}
			
			// 扣钱
			GardenService gs = GardenService.getInstance();
			result = gs.updateUserGold(loginUid, this.getGold() - espBean.getMoney() * count);
			guserBean.setGold(this.getGold() - espBean.getMoney() * count);
			
			// 扣成就
			FlowerUtil.updateExp(loginUid, 0 - espBean.getExp() * count);
			
			result = service.addSpecial(loginUid, espBean.getId(), count);
		}
		return result;
	}
	
	// 使用特殊物品
	public boolean useEspecialGoods(int id){
		boolean result = false;
		EspecialBean espBean = FlowerUtil.getEspecial(id);
//		System.out.println("使用的是 " + FlowerUtil.getEspecial(id).getName());
		if (espBean == null){
			this.setAttribute("tip", "此物品不存在.");
			return result;
		}
		List espList = service.getUserStore("user_id = " + loginUid + " and flower_id = " + espBean.getId() + " and type = 3");
		if (espList.size()==0){
			this.setAttribute("tip", "你没有此物品.");
			return result;
		}
		synchronized (lock){
			switch (espBean.type){
				case 1:{			// type = 1:合成加速
					// 看看用户是否在进行合成试验，或试验是否已完成
					DishBean dish = FlowerUtil.getUserDish(loginUid);
					if (dish.isSpecGoodsUsed()){
						this.setAttribute("tip", "已使用过催化剂.");
						return result;	
					}
					if (dish.getTime() != -28800000){
						long compTime = 0;
						// State = 0 表示这次合成是失败的
						if (dish.getState() == 0){
							compTime = FlowerUtil.COMP_FAIL_TIME - (System.currentTimeMillis() - dish.getTime());
						} else {
							compTime = FlowerUtil.getFlower(dish.getState()).getCompTime() - (System
									.currentTimeMillis() - dish.getTime());
						}
						if (compTime <= 0){
							this.setAttribute("tip", "合成试验已完成.");
							return result;
						}
					} else {
						this.setAttribute("tip", "你没有进行合成试验.");
						return result;
					}
					// 减少用户的时间，减少物品
					dish.setTime(dish.getTime() - (-1 * espBean.getTime() * 1000));
					dish.setSpecGoodsUsed(true);	// 标记此用户已经使用过催化剂了。
					result = service.updateDish(" set time = date_add(time,INTERVAL '" + espBean.getTime() + "' SECOND) where user_id = " + loginUid);
					result = service.descSpecial(loginUid, espBean.getId(), 1);
					// log中写入记录
					service.insertMessage("(user_id,content,from_uid,readed,create_time,type) values (" + loginUid + ",'使用了" + espBean.getName() + ".',0,0,now(),2)");
					break;
				}
			}
		}
		return result;
	}
	
	// 判断花的类别[返回类别编号]
	public int getSort(int flowerId) {
		return FlowerUtil.getFlower(flowerId).getType();
	}

	// 判断花的类别[返回类别名称]
	public String returnSortName(int flowerId) {
		return FlowerUtil.sortNames[FlowerUtil.getFlower(flowerId).getType()];
	}

	public float[] getAbate() {
		return abate;
	}

	public void setAbate(float[] abate) {
		this.abate = abate;
	}
	
//	//****************************************************
//	public boolean testDish(DishBean dish,List specList){
//		boolean result = false;
//		String tip = "";
//		System.out.println("用户ID:" + loginUid + " | 试验时间:" + dish.getTime() + " | 培养皿大小:" + this.dishToList(dish).size() + " | " + new Date());
//		if (dish.getTime() != -28800000 && this.dishToList(dish).size() != 0){
//			long compTime = 0;
//			result = true;
//			if (dish.getState() == 0){
//				compTime = FlowerUtil.COMP_FAIL_TIME - (System.currentTimeMillis() - dish.getTime());
//			} else {
//				compTime = FlowerUtil.getFlower(dish.getState()).getCompTime() - (System
//						.currentTimeMillis() - dish.getTime());
//			}
//			if (compTime <= 0){
//				System.out.println("用户ID:" + loginUid + " | 试验结束" + " | " + new Date());
//				tip = "<a href=\"comp.jsp?c=1\">合成实验结束</a>";
//			} else {
//				System.out.println("用户ID:" + loginUid + " | 进行中..." + " | " + new Date());
//				tip = "正在合成实验中," + DateUtil.formatTimeInterval3(compTime) + "后完成.";
//				if (specList.size() != 0 && !dish.isSpecGoodsUsed()){
//					tip += "<a href=\"bag.jsp?f=2\">使用特殊物品</a>";
//				}
//			}
//			this.setAttribute("tip", tip);
//		}
//		return result;
//	}
//	//****************************************************
}