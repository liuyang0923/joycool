package jc.family.game.fruit;

import jc.family.game.vs.VsGameBean;
import jc.family.game.vs.VsUserBean;

public class FruitUserBean extends VsUserBean {
	
	int operateCount; // 记录用户的操作次数
	int updTeckCount;// 升级科技成功次数
	int throwFruitCount;// 扔出的水果数
	int beatFruitCount;// 消灭水果数

	FruitUserBean(){
		this.operateCount=0;
		this.updTeckCount=0;
		this.beatFruitCount=0;
	}
	
	public void init(VsGameBean game) {
		FruitGameBean fg = (FruitGameBean)game;
		fg.getFruitFamilyBean(getSide()).getUserList().add(this);
	}

	public int getUpdTeckCount() {
		return updTeckCount;
	}

	public void setUpdTeckCount(int updTeckCount) {
		this.updTeckCount = updTeckCount;
	}

	public int getThrowFruitCount() {
		return throwFruitCount;
	}

	public void setThrowFruitCount(int throwFruitCount) {
		this.throwFruitCount = throwFruitCount;
	}

	public int getBeatFruitCount() {
		return beatFruitCount;
	}

	public void setBeatFruitCount(int beatFruitCount) {
		this.beatFruitCount = beatFruitCount;
	}

	public int getOperateCount() {
		return operateCount;
	}

	public void setOperateCount(int operateCount) {
		this.operateCount = operateCount;
	}
}
