package jc.guest.battle;
import java.util.ArrayList;
public class NameProperty {
	String name;
	int hp; //血量
	int attack; //攻击
	int defense; //防御
	int speed; //速度
	int impact;//命中
	int luck; //幸运
	long starttime; //开始时间
	long lasttime;  //上一次攻击时间 
	ArrayList depict; //打斗描述
	boolean  faint; //击晕状态
	long space;//攻击间隔
	String npname;
	public String getNpname() {
		return npname;
	}

	public void setNpname(String npname) {
		this.npname = npname;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getLasttime() {
		return lasttime;
	}

	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}

	public ArrayList getDepict() {
		return depict;
	}

	public void setDepict(ArrayList depict) {
		this.depict = depict;
	}

	
	public boolean isFaint() {
		return faint;
	}



	public long getSpace() {
		return space;
	}

	public void setSpace(long space) {
		this.space = space;
	}

	public void setFaint(boolean faint) {
		this.faint = faint;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getImpact() {
		return impact;
	}

	public void setImpact(int impact) {
		this.impact = impact;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}
	
}
