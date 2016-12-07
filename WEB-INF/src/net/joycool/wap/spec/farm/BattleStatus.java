package net.joycool.wap.spec.farm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.service.impl.DummyServiceImpl;
import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.LockUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 玩家战斗数据
 * @datetime:1007-10-24
 */
public class BattleStatus implements Cloneable{
	static DummyServiceImpl dummyService = new DummyServiceImpl();
	
	public int hp;		// 血最大值
	public int mp;		// 气最大值
	public int sp;		// 体力最大值
	
//	 人物基本属性
	public int attr1;
	public int attr2;
	public int attr3;
	public int attr4;
	public int attr5;
	int attr6;
	int attr7;
	
	public int attack1, attack1Float;
	int attack2;
	int attack3;
	int attack4;
	
	public int attackSpeed;		// 攻击速度
	public int attackSpeedAdd;		// 攻击速度
	public int attackInterval;		// 攻击间隔，毫秒为单位
	
	public int defense1;
	int defense2;
	int defense3;
	
	int luck;		// 幸运，增加伤害上限概率
	
	List equipList = new ArrayList();	// 装备了的物品，int[2]格式 物品-套装，如果套装为0则表示非套装
	List setList = new ArrayList();		// 套装物品，int[2]的格式，套装-数量
	
	public int ds;		// 致命
	public float cb;		// 爆发
	
	public int element1;		// 金 +防御
	public int element2;		// 木 +血
	public int element3;		// 水 +气
	public int element4;		// 火 +爆发
	public int element5;		// 土 +体
	public int element6;		// 风
	public int element7;		// 雷
	
	public BattleStatus() {
		
	}
	public BattleStatus(FarmUserBean user) {
		init(user);
	}
	
	public void init(FarmUserBean user) {
		int rank = user.getProRank(FarmProBean.PRO_BATTLE);
		
		luck = 0;
		hp = 50 + rank * 5;
		mp = 50 + rank * 5;
		sp = 50 + rank * 1;
		attack1 = 2;
		defense1 = 5;
		attackSpeed = 20;
		attackSpeedAdd = 0;
		attack1Float = 1 + rank;
		ds = 5;
		cb = 0.1f;
		
		
		attr1 = user.getAttr1();
		attr2 = user.getAttr2();
		attr3 = user.getAttr3();
		attr4 = user.getAttr4();
		attr5 = user.getAttr5();
		
		equipList.clear();
		FarmUserEquipBean[] equips = user.getEquip();
		for(int i = 0;i < equips.length;i++) {
			FarmUserEquipBean equip = equips[i];
			if(equip != null && equip.getUserbagId() != 0) {
				UserBagBean userBag = UserBagCacheUtil.getUserBagCache(equip.getUserbagId());
				if(userBag == null || userBag.getTime() < 2)		// 耐久度1表示已经损坏
					continue;
				DummyProductBean item = dummyService.getDummyProducts(userBag.getProductId());
				List attrList = item.getAttributeList();
				for(int ia = 0;ia < attrList.size();ia++) {
					int[] attr = (int[])attrList.get(ia);
					applyAttr(attr);
				}
				ItemSetBean set = FarmWorld.one.getItemToSet(item.getId());
				if(set != null) {
					int[] s = {item.getId(), set.getId()};
					equipList.add(s);
					addSet(set.getId());
				} else {
					int[] s = {item.getId(), 0};
					equipList.add(s);
				}
			}
		}
		for(int is = 0;is < setList.size();is++) {
			int[] s2 = (int[])setList.get(is);
			ItemSetBean set = FarmWorld.one.getItemSet(s2[0]);
			for(int isc = 0;isc < set.getCountList().size();isc++) {
				Integer ic = (Integer)set.getCountList().get(isc);
				if(ic.intValue() <= s2[1])
					applyAttr((int[])set.getAttributeList().get(isc));
			}
		}
		
		attack1 += (float)attr1 / 2 + (float)attr2 / 6;
		defense1 += attr2 * 5;
		hp += attr3 * 10;
		mp += attr4 * 6;
		sp += attr5 * 3;
		ds += attr2 / 10;
		
		int sumElement = element1 + element2 + element3 + element4 + element5 + element6 + element7;
		hp += sumElement * 10;
		mp += sumElement * 6;
		sp += sumElement * 3;
		
		defense1 += element1 * 5;
		hp += element2 * 5;
		mp += element3 * 3;
		cb += element4 * 0.03f;
		sp += element5 * 2;
		
		attackInterval = FarmWorld.attackInterval(attackSpeed, attackSpeedAdd);
	}
	
	void addSet(int setId) {
		for(int is = 0;is < setList.size();is++) {
			int[] s2 = (int[])setList.get(is);
			if(s2[0] == setId) {
				s2[1]++;
				return;
			}
		}
		int[] s2 = {setId, 1};
		setList.add(s2);
	}
	
	// 应用装备的属性
	public void applyAttr(int[] attr) {
		switch(attr[0]) {
		case 1: {		// 加基础攻击力
			attack1 += attr[1];
			attack1Float += attr[2];
		} break;
		case 2: {		// 加浮动攻击力
			
		} break;
		case 3: {		// 加基础防御力
			defense1 += attr[1];
		} break;
		case 4: {		// 加hp
			hp += attr[1];
		} break;
		case 5: {		// 加mp
			mp += attr[1];
		} break;
		case 6: {		// 加sp
			sp += attr[1];
		} break;
		case 9: {		// 加攻击速度
			attackSpeedAdd += attr[1];
		} break;
		case 10: {		// 更改攻击速度
			attackSpeed = attr[1];
		} break;
		case 11: {		// 加属性1
			attr1 += attr[1];
		} break;
		case 12: {		// 加属性2
			attr2 += attr[1];
		} break;
		case 13: {		// 加属性3
			attr3 += attr[1];
		} break;
		case 14: {		// 加属性4
			attr4 += attr[1];
		} break;
		case 15: {		// 加属性5
			attr5 += attr[1];
		} break;
		case 21: {		// 加致命
			ds += attr[1];
		} break;
		case 22: {		// 加爆发
			cb += (float)attr[1] / 100;
		} break;
		case 23: {		// 加幸运
			luck += attr[1];
		} break;
		case 24: {		// 加命中

		} break;
		case 25: {		// 加闪避

		} break;
		case 31: {		// 加元素
			element1 += attr[1];
		} break;
		case 32: {		// 加元素
			element2 += attr[1];
		} break;
		case 33: {		// 加元素
			element3 += attr[1];
		} break;
		case 34: {		// 加元素
			element4 += attr[1];
		} break;
		case 35: {		// 加元素
			element5 += attr[1];
		} break;
		case 36: {		// 加元素
			element6 += attr[1];
		} break;
		case 37: {		// 加元素
			element7 += attr[1];
		} break;
		}
	}
	// 是否装备了某个物品
	public boolean hasEquipItem(int itemId) {
		for(int i = 0;i < equipList.size();i++) {
			int[] equip = (int[])equipList.get(i);
			if(equip[0] == itemId)
				return true;
		}
		return false;
	}
	// 某个套装的数量
	public int equipSetCount(int setId) {
		for(int i = 0;i < setList.size();i++) {
			int[] set = (int[])setList.get(i);
			if(set[0] == setId)
				return set[1];
		}
		return 0;
	}
	public BattleStatus cloneBS() {
		try {
			return (BattleStatus)super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
