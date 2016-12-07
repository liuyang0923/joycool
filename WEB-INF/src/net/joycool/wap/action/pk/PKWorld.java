package net.joycool.wap.action.pk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.joycool.wap.bean.pk.PKActBean;
import net.joycool.wap.bean.pk.PKEquipBean;
import net.joycool.wap.bean.pk.PKMObjBean;
import net.joycool.wap.bean.pk.PKMedicineBean;
import net.joycool.wap.bean.pk.PKMissionBean;
import net.joycool.wap.bean.pk.PKMonsterBean;
import net.joycool.wap.bean.pk.PKMonsterSkillBean;
import net.joycool.wap.bean.pk.PKNpcBean;
import net.joycool.wap.bean.pk.PKObjTypeBean;
import net.joycool.wap.bean.pk.PKUserBSkillBean;
import net.joycool.wap.bean.pk.PKUserBagBean;
import net.joycool.wap.bean.pk.PKUserBean;
import net.joycool.wap.bean.pk.PKUserSkillBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IPKService;
import net.joycool.wap.util.StringUtil;

/**
 * @author macq
 * @datetime 2007-1-31 下午01:02:08
 * @explain
 */
public class PKWorld {
	public static IPKService pkService = null;

	// pk系统在线用户map
	public static HashMap pkUserMap = null;

	// pk系统任务模块map
	public static HashMap pkMissionMap = null;

	// pk任务系统任务物品
	public static HashMap pkMObjMap = null;

	// pk系统怪物map
	public static HashMap pkMosterMap = null;

	// 所有场景map表
	public static HashMap pkActMap = null;

	// liuyi 2007-02-15 所有npc
	public static HashMap pkNpcMap = null;

	// 用户主动技能map表
	public static HashMap pkUserSkillMap = null;

	// 用户被动技能map表
	public static HashMap pkUserBSkillMap = null;

	// 怪兽主动技能map表
	public static HashMap pkMonsterSkillMap = null;

	// 攻防装备map表
	public static HashMap pkEquipMap = null;

	// 物品map表
	public static HashMap pkMedicineMap = null;

	public static IPKService getPKService() {
		if (pkService == null) {
			pkService = ServiceFactory.createPKService();
		}
		return pkService;
	}

	// 获取所有PK系统在线用户信息

	static byte[] lock1 = new byte[0];

	public static HashMap getPKUserMap() {
		if (pkUserMap != null) {
			return pkUserMap;
		}
		synchronized (lock1) {
			if (pkUserMap != null) {
				return pkUserMap;
			}
			pkUserMap = new HashMap();
			return pkUserMap;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取一个pk用户信息
	 * @datetime:2007-2-2 上午09:54:13
	 * @param pkUserId
	 * @return
	 */
	public static PKUserBean getPKUser(int pkUserId) {
		PKUserBean pkUser = (PKUserBean) getPKUserMap().get(
				new Integer(pkUserId));
		if (pkUser == null) {
			pkUser = getPKService().getPKUser(
					"user_id=" + pkUserId + " limit 0 , 1");
			if (pkUser != null) {
				// 获取用户行囊中物品包括身上的物品
				// Vector userBagList
				// =getPKService().getPKUserBagList("user_id="+pkUserId+" and
				// site_id=0");
				Vector userBagList = getPKService().getPKUserBagList(
						"user_id=" + pkUserId);
				Iterator it = userBagList.iterator();
				PKUserBagBean userBag = null;
				while (it.hasNext()) {
					userBag = (PKUserBagBean) it.next();
					int type = userBag.getType();
					// 0代表装备
					if (type == 0) {
						// 获取一个装备属性
						PKEquipBean epuip = (PKEquipBean) getPKEquip().get(
								new Integer(userBag.getEquipId()));
						if (epuip != null) {
							// 添加装备引用到用户行囊中
							userBag.setPorto(epuip);
							// PKEquipBean proto =(PKEquipBean)
							// userBag.getPorto();
							// String name = proto.getName();
						}
					}// 1代表药品
					else if (type == 1) {
						// 获取一个装备属性
						PKMedicineBean medicine = (PKMedicineBean) getPKMedicine()
								.get(new Integer(userBag.getEquipId()));
						if (medicine != null) {
							// 添加装备引用到用户行囊中
							userBag.setPorto(medicine);
							// PKMedicineBean proto =(PKMedicineBean)
							// userBag.getPorto();
							// String name = proto.getName();
						}
					}// 2代表暗器
					else if (type == 2) {

					}// 5代表任务物品
					else if (type == 5) {
						// 获取一个装备属性
						PKMObjBean mObj = (PKMObjBean) getPKMObjMap().get(
								new Integer(userBag.getEquipId()));
						if (mObj != null) {
							// 添加装备引用到用户行囊中
							userBag.setPorto(mObj);
							// PKMedicineBean proto =(PKMedicineBean)
							// userBag.getPorto();
							// String name = proto.getName();
						}
					}
				}
				// 添加用户行囊物品列表到用户信息中
				pkUser.setUserBagList(userBagList);
				// 设置用户第一次动作时间
				pkUser.setActionTime(System.currentTimeMillis());
				// 获取用户等级
				int rank = PKAction.getExperienceRank(pkUser.getExperience());
				// 设置用户等级
				pkUser.setRank(rank);
			}
		}
		return pkUser;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取场景内所有对象属性
	 * @datetime:2007-1-31 下午02:15:17
	 * @return
	 */
	static byte[] lock = new byte[0];

	public static HashMap loadPKWorld() {
		if (pkActMap != null) {
			return pkActMap;
		}
		// if (pkActMap == null) {
		synchronized (lock) {
			if (pkActMap != null) {
				return pkActMap;
			}
			// 初始化场景数组列表
			pkActMap = new HashMap();

			// 获取系统所有场景
			Vector actList = getPKService().getPKActList(" 1=1");
			for (int i = 0; i < actList.size(); i++) {
				// 获取一条场景记录
				PKActBean act = (PKActBean) actList.get(i);
				// 添加一个场景对象到场景数组列表中
				pkActMap.put(new Integer(act.getId()), act);
				// 获取一个场景所对应的怪兽(String类型,已逗号分隔)
				String[] monsterIDArray = act.getMonster().split(",");
				// 一个场景内怪物列表
				List monsterList = new ArrayList(0);
				PKMonsterBean monster = null;
				PKMonsterBean baseMonster = null;
				for (int j = 0; j < monsterIDArray.length; j++) {
					// 获取初始化怪物属性
					baseMonster = (PKMonsterBean) loadMoster().get(
							monsterIDArray[j]);
					if (baseMonster != null) {
						monster = new PKMonsterBean();
						// liuyi 2007-02-06 设置怪物初始属性 start
						buildMonster(monster, baseMonster.getId());
						// liuyi 2007-02-06 设置怪物初始属性 end
						monster.setIndex(j);
						// 添加怪物到怪物列表中
						monsterList.add(monster);
					}
				}
				act.setMonsterList(monsterList);
				Set userList = new HashSet(0);
				act.setPkUserList(userList);
				List logList = new LinkedList();
				act.setLog(logList);
			}

			// 加载每个场景的NPC(每个npc只能属于一个场景)
			pkNpcMap = loadNpc();
			Iterator iter = pkNpcMap.values().iterator();
			while (iter.hasNext()) {
				PKNpcBean npc = (PKNpcBean) iter.next();
				int sceneId = npc.getSceneId();
				PKActBean act = (PKActBean) pkActMap.get(new Integer(sceneId));
				if (act != null) {
					act.getNpcList().add(npc);
				}
			}
		}
		// }
		return pkActMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取所有怪物属性记录
	 * @datetime:2007-1-31 下午02:29:40
	 * @return
	 */
	public static HashMap loadMoster() {
		if (pkMosterMap != null) {
			return pkMosterMap;
		}
		if (pkMosterMap == null) {
			pkMosterMap = new HashMap();
			synchronized (pkMosterMap) {
				Vector mosterList = getPKService().getPKMonsterList(" 1=1");
				PKMonsterBean monster = null;
				for (int i = 0; i < mosterList.size(); i++) {
					monster = (PKMonsterBean) mosterList.get(i);
					if (monster != null) {
						pkMosterMap.put(monster.getId() + "", monster);
					}
				}
			}
		}
		return pkMosterMap;
	}

	/**
	 * liuyi 2007-02-15 读取npc到map里
	 * 
	 * @return
	 */
	public static HashMap loadNpc() {
		if (pkNpcMap != null) {
			return pkNpcMap;
		}
		if (pkNpcMap == null) {
			pkNpcMap = new HashMap();
			synchronized (pkNpcMap) {
				List npcList = getPKService().getPKNpcList(" 1=1 ");
				for (int i = 0; i < npcList.size(); i++) {
					PKNpcBean npc = (PKNpcBean) npcList.get(i);
					if (npc != null) {
						pkNpcMap.put(npc.getId() + "", npc);
					}
				}
			}
		}
		return pkNpcMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取所有怪物主动技能
	 * @datetime:2007-2-2 下午01:00:09
	 * @return
	 */
	public static HashMap getMosterSkill() {
		if (pkMonsterSkillMap != null) {
			return pkMonsterSkillMap;
		}
		if (pkMonsterSkillMap == null) {
			pkMonsterSkillMap = new HashMap();
			synchronized (pkMonsterSkillMap) {
				Vector mosterSkillList = getPKService().getPKMonsterSkillList(
						" 1=1");
				PKMonsterSkillBean monsterSkill = null;
				for (int i = 0; i < mosterSkillList.size(); i++) {
					monsterSkill = (PKMonsterSkillBean) mosterSkillList.get(i);
					if (monsterSkill != null) {
						pkMonsterSkillMap
								.put(new Integer(monsterSkill.getId()),
										monsterSkill);
					}
				}
			}
		}
		return pkMonsterSkillMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取所有用户主动技能
	 * @datetime:2007-2-2 下午01:01:05
	 * @return
	 */
	public static HashMap getUserSkill() {
		if (pkUserSkillMap != null) {
			return pkUserSkillMap;
		}
		if (pkUserSkillMap == null) {
			pkUserSkillMap = new HashMap();
			synchronized (pkUserSkillMap) {
				Vector userSkillList = getPKService()
						.getPKUserSkillList(" 1=1");
				PKUserSkillBean userSkill = null;
				for (int i = 0; i < userSkillList.size(); i++) {
					userSkill = (PKUserSkillBean) userSkillList.get(i);
					if (userSkill != null) {
						pkUserSkillMap.put(new Integer(userSkill.getId()),
								userSkill);
					}
				}
			}
		}
		return pkUserSkillMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 任务系统物品
	 * @datetime:2007-4-17 11:25:13
	 * @return
	 * @return HashMap
	 */
	public static HashMap getPKMObjMap() {
		if (pkMObjMap != null) {
			return pkMObjMap;
		}
		if (pkMObjMap == null) {
			pkMObjMap = new HashMap();
			synchronized (pkMObjMap) {
				Vector pkMObjMapList = getPKService().getPKMObjList("1=1");
				PKMObjBean pkMObj = null;
				for (int i = 0; i < pkMObjMapList.size(); i++) {
					pkMObj = (PKMObjBean) pkMObjMapList.get(i);
					if (pkMObj != null) {
						pkMObjMap.put(new Integer(pkMObj.getId()), pkMObj);
					}
				}
			}
		}
		return pkMObjMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取所有用户被动技能
	 * @datetime:2007-3-9 15:38:24
	 * @return
	 * @return HashMap
	 */
	public static HashMap getUserBSkill() {
		if (pkUserBSkillMap != null) {
			return pkUserBSkillMap;
		}
		if (pkUserBSkillMap == null) {
			pkUserBSkillMap = new HashMap();
			synchronized (pkUserBSkillMap) {
				Vector userBSkillList = getPKService().getPKUserBSkillList(
						" 1=1");
				PKUserBSkillBean userBSkill = null;
				for (int i = 0; i < userBSkillList.size(); i++) {
					userBSkill = (PKUserBSkillBean) userBSkillList.get(i);
					if (userBSkill != null) {
						pkUserBSkillMap.put(new Integer(userBSkill.getId()),
								userBSkill);
					}
				}
			}
		}
		return pkUserBSkillMap;
	}

	/**
	 * @author macq
	 * @explain : 获取所有攻防装备
	 * @datetime:2007-2-14
	 * @return
	 */
	public static HashMap getPKEquip() {
		if (pkEquipMap != null) {
			return pkEquipMap;
		}
		if (pkEquipMap == null) {
			pkEquipMap = new HashMap();
			synchronized (pkEquipMap) {
				Vector equipList = getPKService().getPKEuqipList(" 1=1");
				PKEquipBean equip = null;
				for (int i = 0; i < equipList.size(); i++) {
					equip = (PKEquipBean) equipList.get(i);
					if (equip != null) {
						pkEquipMap.put(new Integer(equip.getId()), equip);
					}
				}
			}
		}
		return pkEquipMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取所有物品
	 * @datetime:2007-3-7 14:20:58
	 * @return
	 * @return HashMap
	 */
	public static HashMap getPKMedicine() {
		if (pkMedicineMap != null) {
			return pkMedicineMap;
		}
		if (pkMedicineMap == null) {
			pkMedicineMap = new HashMap();
			synchronized (pkMedicineMap) {
				Vector medicineList = getPKService().getPKMedicineList(" 1=1");
				PKMedicineBean medicine = null;
				for (int i = 0; i < medicineList.size(); i++) {
					medicine = (PKMedicineBean) medicineList.get(i);
					if (medicineList != null) {
						pkMedicineMap.put(new Integer(medicine.getId()),
								medicine);
					}
				}
			}
		}
		return pkMedicineMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain：
	 * @datetime:2007-4-11 15:15:48
	 * @return HashMap
	 */
	static byte[] lock2 = new byte[0];

	public static HashMap getPKMission() {
		if (pkMissionMap != null) {
			return pkMissionMap;
		}
		synchronized (lock2) {
			if (pkMissionMap != null) {
				return pkMissionMap;
			}
			pkMissionMap = new HashMap();
			synchronized (pkMissionMap) {
				Vector missionList = getPKService().getPKMissionList(" 1=1");
				PKMissionBean mission = null;
				for (int i = 0; i < missionList.size(); i++) {
					mission = (PKMissionBean) missionList.get(i);
					if (missionList != null) {
						String[] objs = mission.getObjs().split(",");
						PKObjTypeBean objType = null;
						for (int j = 0; j < objs.length; j++) {
							objType = new PKObjTypeBean();
							String[] subObj = objs[j].split("-");
							// 数组
							// 0:代表类型
							// 1:代表类型下具体物品id
							// 2:代表数量
							int type = StringUtil.toInt(subObj[0]);
							int id = StringUtil.toInt(subObj[1]);
							int count = StringUtil.toInt(subObj[2]);
							objType.setType(type);
							objType.setId(id);
							objType.setCount(count);
							mission.getObjList().add(objType);
						}
						String[] prize = mission.getPrize().split(",");
						for (int k = 0; k < prize.length; k++) {
							objType = new PKObjTypeBean();
							String[] subPrize = prize[k].split("-");
							int type = StringUtil.toInt(subPrize[0]);
							int id = StringUtil.toInt(subPrize[1]);
							int count = StringUtil.toInt(subPrize[2]);
							objType.setType(type);
							objType.setId(id);
							objType.setCount(count);
							mission.getPrizeList().add(objType);
						}
						pkMissionMap.put(new Integer(mission.getId()), mission);
					}
				}
			}
		}
		return pkMissionMap;
	}

	/**
	 * liuyi 2007-02-06 清楚PK缓存
	 */
	public static void clear() {
		pkUserMap = null;
		pkMosterMap = null;
		pkActMap = null;
		pkUserSkillMap = null;
		pkMonsterSkillMap = null;
		pkEquipMap = null;
		pkMedicineMap = null;
		pkUserBSkillMap = null;
		pkMissionMap = null;
	}

	/**
	 * liuyi 2007-02-06 设置怪物初始属性
	 * 
	 * @param monster
	 * @param monsterId
	 */
	public static void buildMonster(PKMonsterBean monster, int monsterId) {
		if (monster == null)
			return;

		PKMonsterBean baseMonster = (PKMonsterBean) loadMoster().get(
				monsterId + "");
		if (baseMonster != null) {
			monster.setId(baseMonster.getId());
			monster.setName(baseMonster.getName());
			monster.setExperience(baseMonster.getExperience());
			monster.setPhysical(baseMonster.getPhysical());
			monster.setInitPhysical(baseMonster.getPhysical());
			monster.setEnergy(baseMonster.getEnergy());
			monster.setAggressivity(baseMonster.getAggressivity());
			monster.setRecovery(baseMonster.getRecovery());
			monster.setFlying(baseMonster.getFlying());
			monster.setLuck(baseMonster.getLuck());
			monster.setSkillTypeId(baseMonster.getSkillTypeId());
			monster.setDropTypeId(baseMonster.getDropTypeId());
			monster.setSkillId(baseMonster.getSkillId());
			monster.setDropTypeIdList(baseMonster.getDropTypeIdList());
			monster.setRate(baseMonster.getRate());
		}
	}

}
