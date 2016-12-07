package jc.guest.battle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jc.guest.GuestHallAction;
import jc.guest.GuestUserInfo;

import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.StringUtil;

public class GamepageAction extends CustomAction {
	public static LinkedList nplistLink = new LinkedList();
	public static HashMap top = new HashMap();
	public static HashMap toplose = new HashMap();
	public static Topbean[] topbean;
	public static Topbean[] topbeanlose;
	public static int date = Calendar.getInstance().get(Calendar.DATE);

	public GamepageAction() {
	}

	public GamepageAction(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 开打.如果执行正确，返回0，用户没钱玩，返回1.
	 * @param request
	 * @param response
	 * @param guestUser
	 * @return
	 * @throws IOException
	 */
	public int fight(HttpServletRequest request, HttpServletResponse response,GuestUserInfo guestUser) throws IOException {
		HttpSession session = request.getSession();
//		request.setCharacterEncoding("UTF-8");
		
		GuestHallAction guestAction = new GuestHallAction(request);
		
		// 获取数据
		String username1 = this.getParameterString("username1");
		String username2 = this.getParameterString("username2");
		// String username1 = this.getParameterNoEnter("username1");
		// String username2 = this.getParameterNoEnter("username2");

		// 去尾部空格
		if (username1 != null && username2 != null) {
			username1 = username1.trim();
			username2 = username2.trim();
		}
		// 用来取nplistLink里的值，k指数组下标
		Namebean nb = new Namebean();
		int k = StringUtil.toInt(request.getParameter("k"));

		if (k > -1) {
			if (k >= nplistLink.size()) {
				if (k != 0)
					k = nplistLink.size() - 1;
			}
			nb = (Namebean) nplistLink.get(k);
			username1 = nb.getUsername1();
			username2 = nb.getUsername2();
		}
		Stored stored = (Stored) session.getAttribute("stored");
		NameProperty npusername1 = null;
		NameProperty npusername2 = null;
		ArrayList depict = null;

		// firstdead用来判断是否是第一次死亡.
		int firstdead = 2;
		if (stored != null) {
			npusername1 = stored.getNp1();
			npusername2 = stored.getNp2();
			depict = stored.getDepict();
			firstdead = stored.getFirstdead();
		}

		int first = 1;// 初始化判断
		if (StringUtil.toInt(request.getParameter("first")) == -1) {
			first = 1;
		} else {
			first = StringUtil.toInt(request.getParameter("first"));
		}
		// 初始化
		if (first == 0) {
			if (k < 0){
				// 扣游币
				if (guestUser != null){
					// 扣除1游币
					if (guestUser.getMoney() > 1){
						guestAction.updateMoney(guestUser.getId(), -1);
					} else {
						// 穷鬼...
						return 1;
					}
				}
			}
			depict = new ArrayList();
			npusername1 = init(username1);
			npusername2 = init(username2);
			npusername1.setStarttime(1);
			npusername2.setStarttime(1);
			npusername1.setLasttime(npusername1.getStarttime());
			npusername2.setLasttime(npusername2.getStarttime());
			stored = new Stored();
			stored.setError(check(username1, username2, stored));
			stored.setNp1(npusername1);
			stored.setNp2(npusername2);
			stored.setRi(new Random(npusername1.getHp() + npusername2.getHp()));
			first = first + 1;
			firstdead = 0;// 死亡判断
			stored.setFirstdead(firstdead);
			stored.setDepict(depict);
			if (check(username1, username2, stored)) {
				nb.setUsername1(username1);
				nb.setUsername2(username2);
				// 如果不是从nplistLink中取出的名字，就将它添加到nplistLink
				if (k == -1) {
					boolean add = true;
					for (int i = 0; i < nplistLink.size(); i++) {
						Namebean nb1 = (Namebean) nplistLink.get(i);
						if (username1.equals(nb1.getUsername1()) && username2.equals(nb1.getUsername2()))
							add = false;
					}
					if (add)
						nplistLink.addFirst(nb);
				}

				if (nplistLink.size() > 30)
					nplistLink.removeLast();
			}
			session.setAttribute("stored", stored);
		}

		// 如果没有死亡就执行下面代码
		if (firstdead != 1) {
			if (npusername1.getSpeed() > npusername2.getSpeed() || npusername1.getSpeed() == npusername2.getSpeed()
					&& stored.getRi().nextInt(2) == 0) {
				NameProperty tmp = npusername1;
				npusername1 = npusername2;
				npusername2 = tmp;
			}
			// 如果user1的最后攻击时间加上攻击间隔，小于user2的最后攻击时间，就让user1攻击两次.
			if (npusername1.getLasttime() + npusername1.getSpace() < npusername2.getLasttime()) {
				battle(depict, npusername1, npusername2, stored);
				npusername1.setLasttime(npusername1.getLasttime() + npusername1.getSpace());
				battle(depict, npusername1, npusername2, stored);
				npusername1.setLasttime(npusername1.getLasttime() + npusername1.getSpace());
			} else if (npusername2.getLasttime() + npusername2.getSpace() < npusername1.getLasttime()) {
				battle(depict, npusername2, npusername1, stored);
				npusername2.setLasttime(npusername2.getLasttime() + npusername2.getSpace());
				battle(depict, npusername2, npusername1, stored);
				npusername2.setLasttime(npusername2.getLasttime() + npusername2.getSpace());
			} else {
				battle(depict, npusername1, npusername2, stored);
				npusername1.setLasttime(npusername1.getLasttime() + npusername1.getSpace());
				battle(depict, npusername2, npusername1, stored);
				npusername1.setLasttime(npusername2.getLasttime() + npusername2.getSpace());
			}

			session.setAttribute("stored", stored);

		}
		return 0;
	}

	// 验证用户输入
	public boolean check(String username1, String username2, Stored stored) {
		if (username1 != null && username2 != null) {
			username1 = username1.trim();
			username2 = username2.trim();
		}
		if (username1 == null || "".equals(username1) || "".equals(username2) || username2 == null) {
			stored.setErrormessage("请您输入对战名字!");
			return false;
		} else if (username1.length() > 10 || username2.length() > 10) {
			stored.setErrormessage("您输入的名字过长,请重新输入!");
			return false;
		} else if (username1.equals(username2)) {
			stored.setErrormessage("不能与自己对战!");
			return false;
		}
		return true;
	}

	// 普通攻击
	// 攻击方对防守方造成int((攻击方.攻击-攻击方.攻击*防守方.防御/500）*random(0.9,1.1)))点伤害
	public String common(NameProperty acctackuser, NameProperty defenseuser, Random r) {

		// 受伤害值
		int attack = (int) ((acctackuser.getAttack() - acctackuser.getAttack() * defenseuser.getDefense() / 500) * (0.9 + r
				.nextDouble() * 0.2));
		// 修改HP，减去伤害值
		int hp = defenseuser.getHp() - attack;
		if (hp < 0)
			hp = 0;
		defenseuser.setHp(hp);
		// 描述
		// 描述1
		StringBuilder sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(acctackuser.getName());
		sbdepict1.append("]飞起一脚,正中[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("]的胸口,对[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("]造成");
		sbdepict1.append(attack);
		sbdepict1.append("点伤害");
		String depict1 = sbdepict1.toString();
		// 描述2
		sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(acctackuser.getName());
		sbdepict1.append("]一板砖拍到[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("]头上,[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("]受到");
		sbdepict1.append(attack);
		sbdepict1.append("点伤害");
		String depict2 = sbdepict1.toString();
		// 描述3
		sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(acctackuser.getName());
		sbdepict1.append("]掏出一把AK-47,一顿狂扫,[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("]受到");
		sbdepict1.append(attack);
		sbdepict1.append("点伤害");
		String depict3 = sbdepict1.toString();
		// 随机输出描述
		String[] depict = { depict1, depict2, depict3 };
		int ri = r.nextInt(3);
		return depict[ri];

	}

	// 未命中
	public String impact(NameProperty acctackuser, NameProperty defenseuser, Random r) {
		// 描述
		// 描述1
		StringBuilder sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(acctackuser.getName());
		sbdepict1.append("]猛冲向[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("],结果左脚绊右脚,摔了一个四脚朝天");
		String depict1 = sbdepict1.toString();
		// 描述2
		sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("]躲开了[");
		sbdepict1.append(acctackuser.getName());
		sbdepict1.append("]的攻击仰天长笑:信春哥得永生");
		String depict2 = sbdepict1.toString();
		// 描述3
		sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(acctackuser.getName());
		sbdepict1.append("]一刀劈向[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("],[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("]不慌不忙地闪过了这次攻击");

		String depict3 = sbdepict1.toString();
		// 描述4
		sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("]人品大爆发,如同风一般闪掉了[");
		sbdepict1.append(acctackuser.getName());
		sbdepict1.append("]一记铁拳");
		String depict4 = sbdepict1.toString();
		// 随机输出描述
		String[] depict = { depict1, depict2, depict3, depict4 };
		int ri = r.nextInt(4);
		return depict[ri];

	}

	// 未命中+反击
	// int((防守方.攻击-防守方.攻击*攻击方.防御/1000）*random(0.9,1.1)))
	public String defense(NameProperty acctackuser, NameProperty defenseuser, Random r) {
		// 受伤害值
		int attack = (int) ((defenseuser.getAttack() - defenseuser.getAttack() * acctackuser.getDefense() / 1000) * (0.9 + r
				.nextDouble() * 0.2));
		// 修改HP,减去伤害值
		int hp = acctackuser.getHp() - attack;
		if (hp < 0)
			hp = 0;
		acctackuser.setHp(hp);
		// 描述
		//描述1
		StringBuilder sbdepict1 = new StringBuilder();
		sbdepict1.append("[" );
		sbdepict1.append(defenseuser.getName());
		sbdepict1.append("]傻人有傻福,[");
		sbdepict1.append(acctackuser.getName() );
		sbdepict1.append("]被石头绊倒,受到");
		sbdepict1.append(attack);
		sbdepict1.append("点伤害");
		String depict1 =sbdepict1.toString();
		//描述2
		sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(acctackuser.getName());
		sbdepict1.append("]掏出皮带一顿猛抽,结果全打到自己脸上,血量降低");
		sbdepict1.append(attack);
		sbdepict1.append("点");
		String depict2 = sbdepict1.toString();
		// 随机输出描述
		String[] depict = { depict1, depict2 };
		int ri = r.nextInt(2);
		return depict[ri];

	}

	// 特殊事件
	public String special(ArrayList depicts, NameProperty acctackuser, NameProperty defenseuser, Random r) {
		String depict = "";
		StringBuilder sbdepict1;
		switch (r.nextInt(5)) {
		case 0:
			// 暴击
			// int((攻击方.攻击-攻击方.攻击*防守方.防御/500）*1.5*random(0.9,1.1)))点伤害
			int attack = (int) ((acctackuser.getAttack() - acctackuser.getAttack() * defenseuser.getDefense() / 500) * 1.5 * (0.9 + r
					.nextDouble() * 0.2));
			// 修改HP,减去伤害值
			int hp = defenseuser.getHp() - attack;
			if (hp < 0)
				hp = 0;
			defenseuser.setHp(hp);
			// 描述
			// 描述1
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]使出了必杀技,对[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]造成了");
			sbdepict1.append(attack);
			sbdepict1.append("点的致命伤害");
			String depict1 = sbdepict1.toString();
			// 描述2
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]一招正中[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]的要害,对其造成");
			sbdepict1.append(attack);
			sbdepict1.append("点致命伤害");
			String depict2 = sbdepict1.toString();
			// 随机输出描述
			int ri0 = r.nextInt(2);
			String[] depictArray = { depict1, depict2 };
			depict = depictArray[ri0];
			depicts.add(depict);
			break;
		case 1:
			// 暴打
			// 攻击方对防守方进行4次攻击,每次攻击对防守方造成int((攻击方.攻击-攻击方.攻击*防守方.防御/2000）

			int allattack = 0;
			// 描述1
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]趁[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]一不留神,将其扑倒一顿乱捶:");
			depict1 = sbdepict1.toString();
			// 描述2
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]发怒了,把[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]摁在地上一顿暴打:");
			depict2 = sbdepict1.toString();
			int ri1 = r.nextInt(2);
			String[] depictArray1 = { depict1, depict2 };
			depict = depictArray1[ri1];
			depicts.add(depict);
			for (int i = 0; i < 4; i++) {
				int attack1 = (int) ((acctackuser.getAttack() - acctackuser.getAttack() * defenseuser.getDefense()
						/ 2000) * (0.9 + r.nextDouble() * 0.2));
				sbdepict1 = new StringBuilder();
				sbdepict1.append("[");
				sbdepict1.append(defenseuser.getName());
				sbdepict1.append("]受到");
				sbdepict1.append(attack1);
				sbdepict1.append("点伤害");
				depicts.add(sbdepict1.toString());
				allattack = allattack + attack1;
			}

			// 修改Hp
			hp = defenseuser.getHp() - allattack;
			if (hp < 0)
				hp = 0;
			defenseuser.setHp(hp);
			// 随机输出描述

			break;
		case 2:
			// 中毒
			// 防守方.血量减为当前80%
			// 修改HP
			int attack2 = (int) (defenseuser.getHp() * 0.2);

			hp = defenseuser.getHp() - attack2;
			if (hp < 0)
				hp = 0;
			defenseuser.setHp(hp);
			// 描述
			// 描述1
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]将有毒物质注入[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]体内,[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]损失");
			sbdepict1.append(attack2);
			sbdepict1.append("点血");
			depict1 = sbdepict1.toString();
			// 描述2
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]向[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]投掷臭鸡蛋,[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]一口吞下后中毒,损失");
			sbdepict1.append(attack2);
			sbdepict1.append("点血");
			depict2 = sbdepict1.toString();
			// 描述3
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]把臭袜子塞到了[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]的嘴里,[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]中毒损失");
			sbdepict1.append(attack2);
			sbdepict1.append("点血");
			String depict3 = sbdepict1.toString();
			// 随机输出描述
			String[] depictArray2 = { depict1, depict2, depict3 };
			int ri2 = r.nextInt(3);
			depict = depictArray2[ri2];
			depicts.add(depict);
			break;
		case 3:
			// 诅咒
			// 防守方攻击、防御、速度、命中、幸运分别扣除5点（不可低于该属性的取值范围下限）
			// 描述1
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]在地上画了个圈,口中念念有词:画个圈圈诅咒你.");

			depict1 = sbdepict1.toString();
			// 描述2
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]掏出一个小人,用针扎啊扎,[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]受到诅咒.");
			depict2 = sbdepict1.toString();
			// 描述3
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]大喊一声:我代表月亮诅咒你,[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]受到诅咒.");
			depict3 = sbdepict1.toString();
			// 修改属性
			int getAttack = defenseuser.getAttack() - 5;
			int getDefense = defenseuser.getDefense() - 5;
			int getSpeed = defenseuser.getSpeed() - 5;
			int getImpact = defenseuser.getImpact() - 5;
			int getLuck = defenseuser.getLuck() - 5;
			if (getAttack < 30)
				getAttack = 30;
			if (getDefense < 30)
				getDefense = 30;
			if (getSpeed < 50)
				getSpeed = 50;
			if (getImpact < 50)
				getImpact = 50;
			if (getLuck < 0)
				getLuck = 0;
			defenseuser.setAttack(getAttack);
			defenseuser.setDefense(getDefense);
			defenseuser.setSpeed(getSpeed);
			defenseuser.setImpact(getImpact);
			defenseuser.setLuck(getLuck);
			// 随机输出描述
			String[] depictArray3 = { depict1, depict2, depict3 };
			int ri3 = r.nextInt(3);
			depict = depictArray3[ri3];
			depicts.add(depict);
			break;
		case 4:
			// 击晕
			// 防守方丧失下一次攻击机会,攻击方回复20%当前血量
			int attack4 = (int) (acctackuser.getHp() * 0.2);
			// 修改HP
			hp = acctackuser.getHp() + attack4;
			if (hp > 1000)
				hp = 1000;
			acctackuser.setHp(hp);
			defenseuser.setFaint(true);// 修改为被击晕状态
			// 描述
			// 描述1
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]抄起条凳砸晕了[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("],并趁机回复");
			sbdepict1.append(attack4);
			sbdepict1.append("点血");
			depict1 = sbdepict1.toString();
			// 描述2
			sbdepict1 = new StringBuilder();
			sbdepict1.append("[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]一拳锤在[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]脸上,[");
			sbdepict1.append(defenseuser.getName());
			sbdepict1.append("]疼的晕了过去,[");
			sbdepict1.append(acctackuser.getName());
			sbdepict1.append("]趁机回复");
			sbdepict1.append(attack4);
			sbdepict1.append("点血");
			depict2 = sbdepict1.toString();
			// 随机输出描述
			String[] depictArray4 = { depict1, depict2 };
			int ri4 = r.nextInt(2);
			depict = depictArray4[ri4];
			depicts.add(depict);
			break;
		}
		return depict;

	}

	// 死亡
	public String dead(NameProperty acctackuser, NameProperty defenseuser, Random r) {

		// 死亡
		String deadname = "";
		String winname = "";
		if (acctackuser.getHp() <= 0) {
			deadname = acctackuser.getName();
			winname = defenseuser.getName();
		}
		if (defenseuser.getHp() <= 0) {
			winname = acctackuser.getName();
			deadname = defenseuser.getName();
		}
		// 描述1
		StringBuilder sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(deadname);
		sbdepict1.append("]终于支撑不住,倒地身亡");
		String depict1 = sbdepict1.toString();
		// 描述2
		sbdepict1 = new StringBuilder();
		sbdepict1.append("[");
		sbdepict1.append(winname);
		sbdepict1.append("]对奄奄一息的[");
		sbdepict1.append(deadname);
		sbdepict1.append("]大笑道:小样跟我斗?");
		String depict2 = sbdepict1.toString();

		if (top.get(winname) == null) {
			Topbean topbean = new Topbean();
			ArrayList winlist = new ArrayList();
			ArrayList loselist = new ArrayList();
			winlist.add(winname);
			loselist.add(deadname);
			topbean.setWinname(winlist);
			topbean.setLosename(loselist);
			topbean.setTimes(1);
			top.put(winname, topbean);
		} else {
			boolean put = true;
			Topbean topbean = (Topbean) top.get(winname);
			ArrayList losename = topbean.getLosename();

			for (int i = 0; i < losename.size(); i++) {
				if ((losename.get(i).toString()).equals(deadname))
					put = false;
			}
			if (put) {
				topbean.setTimes(topbean.getTimes() + 1);
				losename.add(deadname);
				topbean.setLosename(losename);
				top.put(winname, topbean);
			}
		}
		if (toplose.get(deadname) == null) {
			Topbean topbeanlose = new Topbean();
			ArrayList winlist = new ArrayList();
			ArrayList loselist = new ArrayList();
			winlist.add(winname);
			loselist.add(deadname);
			topbeanlose.setWinname(winlist);
			topbeanlose.setLosename(loselist);
			topbeanlose.setTimes(1);
			toplose.put(deadname, topbeanlose);
		} else {
			boolean put = true;
			Topbean topbeanlose = (Topbean) toplose.get(deadname);
			ArrayList win = topbeanlose.getWinname();
			for (int i = 0; i < win.size(); i++) {
				if ((win.get(i).toString()).equals(winname))
					put = false;
			}
			if (put) {
				topbeanlose.setTimes(topbeanlose.getTimes() + 1);
				win.add(winname);
				topbeanlose.setWinname(win);
				toplose.put(deadname, topbeanlose);
			}
		}
		// 随机输出描述
		String[] depictArray = { depict1, depict2 };
		int ri = r.nextInt(2);
		return depictArray[ri];
	}

	public void battle(ArrayList nplist, NameProperty np1, NameProperty np2, Stored stored) {
		Judge judge = new Judge();
		ArrayList depict = stored.getDepict();
		if ((np1.getHp() > 0) && (np2.getHp() > 0)) {// 死亡判断
			if (!np2.isFaint()) {// 击晕判断
				if (judge.impact(np1, stored.getRi())) {// 命中判定
					if (judge.special(np1, stored.getRi())) {// 特殊事件判定
						special(depict, np1, np2, stored.getRi());
					} else {
						depict.add(common(np1, np2, stored.getRi()));
					}// 特殊事件判定结束
				} else {
					if (judge.defense(np2, stored.getRi())) {// 判断未命中+反击
						depict.add(defense(np1, np2, stored.getRi()));
					} else {
						depict.add(impact(np1, np2, stored.getRi()));
					}

				}// 命中判定结束
			} else {
				np2.setFaint(false);
			}// 击晕判断结束
		} else {
			int firstdead = stored.getFirstdead();
			if (firstdead == 0) {
				depict.add(dead(np1, np2, stored.getRi()));
				firstdead = firstdead + 1;
				stored.setFirstdead(firstdead);

			}
		}// 死亡判断结束
		stored.setDepict(depict);
	}

	public class Judge {
		/**
		 * 
		 * @param acctackuser
		 *            攻击方的命中
		 * @return
		 */
		// 命中判定
		// (攻击方命中+1)/100
		public boolean impact(NameProperty acctackuser, Random r) {
			int impact = (acctackuser.getImpact() + 1);
			if ((impact) >= r.nextInt(101))
				return true;
			return false;
		}

		/**
		 * 
		 * @param np
		 *            防御方的幸运
		 * @return
		 */
		// 未命中+反击
		// (防御方.幸运+1)/1000
		public boolean defense(NameProperty defenseuser, Random r) {
			int defense = (defenseuser.getLuck() + 1);
			if ((defense) >= r.nextInt(1001))
				return true;
			return false;
		}

		/**
		 * 
		 * @param np攻击方的幸运
		 * @return
		 */
		// 特殊事件
		// (攻击方.幸运+1)/1000
		public boolean special(NameProperty acctackuser, Random r) {
			int special = (acctackuser.getLuck() + 1);
			if ((special) >= r.nextInt(1001))
				return true;
			return false;
		}

	}

	// InitProperty.java
	/**
	 * 初始化属性
	 * 
	 * @param username
	 * @return int[]
	 */
	public NameProperty init(String username) {
		byte[] md = MD5.encrypt(username.getBytes());
		int[] property = new int[11];// 存储得到的 两位数字
		int k = 0;// 用于数组的初始化
		String s = "";// 存储MD5码的绝对值
		for (int i = 0; i < md.length; i++) {
			s += new Integer(Math.abs(md[i])).toString();
		}
		for (int i = 0; i < s.length(); i += 2) {
			if (i > 21)
				break;
			property[k] = Integer.parseInt(((String) s.subSequence(i, i + 2)));// 取两位数
			k++;
		}
		// 名字属性初始化
		int hp = (300 + 300 * (property[0] + property[6] + 2) / 200); // 血量
		int attack = (30 + 69 * (property[1] + property[7] + 2) / 200); // 攻击
		int defense = (30 + 69 * (property[2] + property[8] + 2) / 200);// 防御
		int speed = (50 + 49 * (property[3] + property[9] + 2) / 200);// 速度
		int impact = (50 + 49 * (property[4] + property[10] + 2) / 200);// 命中
		int luck = (99 * (property[5] + 1) / 100);// 幸运
		if (hp < 300)
			hp = 300;
		if (hp > 600)
			hp = 600;
		if (attack < 30)
			attack = 30;
		if (attack > 99)
			attack = 99;
		if (defense < 30)
			defense = 30;
		if (defense > 99)
			defense = 99;
		if (speed < 50)
			speed = 30;
		if (speed > 99)
			speed = 99;
		if (impact < 50)
			impact = 30;
		if (impact > 99)
			impact = 99;
		if (impact < 0)
			impact = 0;
		if (impact > 99)
			impact = 99;
		long space = (long) (100 / ((double) speed) * 1000);// 攻击间隔
		NameProperty np = new NameProperty();// 名字属性bean
		np.setHp(hp);
		np.setAttack(attack);
		np.setDefense(defense);
		np.setSpeed(speed);
		np.setImpact(impact);
		np.setLuck(luck);
		np.setName(username);
		np.setFaint(false);
		np.setSpace(space);
		return np;
	}

	// 排行
	public void top() {
		// 胜利榜单
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);// 改为前一天的日期
		if (date == calendar.get(Calendar.DATE)) {
			calendar.add(Calendar.DATE, +1);// 加到当前日期
			date = calendar.get(Calendar.DATE);
			HashMap top = GamepageAction.top;
			Set set = top.keySet();
			Iterator iter = set.iterator();
			topbean = new Topbean[set.size()];
			int k = 0;
			while (iter.hasNext()) {
				topbean[k] = (Topbean) (top.get(iter.next()));
				k = k + 1;
			}
			for (int i = topbean.length - 1; i >= 0; i--) {
				for (int j = topbean.length - 1; j > 0; j--) {
					Topbean topi = topbean[j];
					Topbean topj = topbean[j - 1];
					if (topi.getTimes() > topj.getTimes()) {
						Topbean temp = topbean[j];
						topbean[j] = topbean[j - 1];
						topbean[j - 1] = temp;
					}
				}
			}
			// 失败榜单
			HashMap toplose = GamepageAction.toplose;
			set = toplose.keySet();
			iter = set.iterator();
			topbeanlose = new Topbean[set.size()];
			k = 0;
			while (iter.hasNext()) {
				topbeanlose[k] = (Topbean) (toplose.get(iter.next()));
				k = k + 1;
			}
			for (int i = topbeanlose.length - 1; i >= 0; i--) {
				for (int j = topbeanlose.length - 1; j > 0; j--) {
					Topbean topi = topbeanlose[j];
					Topbean topj = topbeanlose[j - 1];
					if (topi.getTimes() > topj.getTimes()) {
						Topbean temp = topbeanlose[j];
						topbeanlose[j] = topbeanlose[j - 1];
						topbeanlose[j - 1] = temp;
					}
				}
			}
			GamepageAction.top = new HashMap();// 删除前一日的map
			GamepageAction.toplose = new HashMap();// 删除前一日的map
		}
	}
}
