package jc.search;

import java.io.IOException;
import java.util.*;
import javax.servlet.http.*;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedHashMap2;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.RandomUtil;
import jc.credit.CreditAction;
import jc.credit.CreditService;
import jc.credit.UserBase;

public class SearchAction extends CustomAction {
	public SearchAction(HttpServletRequest request) {
		super(request);
	}

	public static String[] astro = { "无", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座",
			"处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座" };
	public static String[] gender = { "无", "女", "男" };
	public static String[] aim = { "无", "恋人", "知已", "玩伴", "其它" };
	public static CreditService service = new CreditService();
	public static LinkedList hot = new LinkedList();
	public static LinkedHashMap2 paraList = new LinkedHashMap2(500, 1000);

	public List getProCity(int pro) {
		List cityList = service.getCityList(" hypo=" + pro);
		return cityList;
	}

	public String getShow(BeanSearch bean, String show) {
		int astro = bean.getAstro();
		int gender = bean.getGender();
		int aim = bean.getAim();
		if (astro > 0) {
			show = show + "/" + SearchAction.astro[astro];
		}
		if (gender > 0) {
			show = show + "/" + SearchAction.gender[gender];
		}
		if (aim > 0) {
			show = show + "/" + SearchAction.aim[aim];
		}
		if (!"".equals(show)) {
			show = show.substring(1);
		}
		return show;
	}

	/**
	 * 热门搜索
	 * 
	 * @param response
	 */
	public void updHot(HttpServletResponse response) {
		BeanSearch bean = new BeanSearch();
		int del = this.getParameterInt("del");
		if (del > 0 && del <= hot.size()) {
			hot.remove(del - 1);
			try {
				this.sendRedirect("search.jsp", response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		int city = this.getParameterInt("city");
		int astro = this.getParameterInt("astro");
		int gender = this.getParameterInt("gender");
		int aim = this.getParameterInt("aim");
		if (city < 1 && astro < 1 && gender < 1 && aim < 1)
			return;
		else {
			if (city > 0 && city < 502)
				bean.setCity(city);
			if (astro > 0 && astro <= this.astro.length)
				bean.setAstro(astro);
			if (gender > 0)
				bean.setGender(gender);
			if (aim > 0 && aim < this.aim.length)
				bean.setAim(aim);
			hot.addLast(bean);
			try {
				this.sendRedirect("search.jsp", response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获得搜索列表
	 * 
	 * @param cound
	 * @return
	 */
	private List searchUserList(String cound) {
		List list = service.searchUserBase(cound);
		return list;
	}

	/**
	 * 用户搜索结果
	 * 
	 * @return
	 */
	public List search() {
		StringBuilder cond = new StringBuilder();
		String cond1 = "";
		int birYear = this.getParameterInt("birYear");// 出生年
		int birMonth = this.getParameterInt("birMonth");// 出生月
		int birDay = this.getParameterInt("birDay");// 出生日
		int startId = this.getParameterInt("startId");// 数据从哪个ID开始查询
		int city = this.getParameterInt("city");// 同城
		int gender = this.getParameterInt("gender");// 异性
		int age = this.getParameterInt("age");// 年龄
		int astro = this.getParameterInt("astro");// 星座
		int aim = this.getParameterInt("aim");// 情感,目的
		int pageindex = this.getParameterInt("p");
		int type = this.getParameterInt("type");// 记录用户所在搜索页面1.首页(综合搜索)、2.交友、3.老乡、4.星座、5.高级搜索
		int from = this.getParameterInt("from");
		UserBean ub = this.getLoginUser();
		if (ub != null && pageindex <= 0 && from <= 0) {
			BeanPara pb = new BeanPara();
			pb.setBirYear(birYear);
			pb.setBirMonth(birMonth);
			pb.setBirDay(birDay);
			pb.setCity(city);
			pb.setGender(gender);
			pb.setAge(age);
			pb.setAstro(astro);
			pb.setAim(aim);
			pb.setType(type);
			paraList.put(new Integer(ub.getId()), pb);
		}
		if (birYear <= 0 && birMonth <= 0 && birDay <= 0 && city <= 0
				&& gender <= 0 && age <= 0 && astro <= 0 && aim <= 0) {
			if (ub != null) {
				BeanPara bp = (BeanPara) paraList.get(new Integer(ub.getId()));
				if (bp == null || from <= 0)
					return null;
				else {
					birMonth = bp.getBirMonth();
					birYear = bp.getBirYear();
					birDay = bp.getBirDay();
					gender = bp.getGender();
					astro = bp.getAstro();
					city = bp.getCity();
					type = bp.getType();
					age = bp.getAge();
					aim = bp.getAim();
				}
			}
		}
		if (type >= 0)
			request.setAttribute("type", new Integer(type));
		if (gender == 1)
			cond.append(" and gender=0");
		else if (gender == 2)
			cond.append(" and gender=1");
		if (startId > 0)
			cond.append(" and user_id>" + startId);
		if (city > 0)
			cond.append(" and city=" + city);
		if (age == 1)
			cond.append(" and bir_year>=1990 and bir_year<=2000");// 10-20岁
		if (age == 2)
			cond.append(" and bir_year>=1980 and bir_year<=1990");// 20-30岁
		if (age == 3)
			cond.append(" and bir_year>=1960 and bir_year<=1980");// 30-50岁
		if (age == 4)
			cond.append(" and bir_year<=1960");// 50以上
		if (astro > 0)
			cond.append(" and astro=" + astro);
		if (aim > 0)
			cond.append(" and aim=" + aim);
		if (birYear > 0 && birMonth > 0 && birDay > 0)
			cond.append(" and bir_year=" + birYear + " and bir_month="
					+ birMonth + " and bir_day=" + birDay);
		if (cond.length() > 4) {
			cond.append(" limit 30");
			cond1 = cond.substring(4);
		} else {
			cond.append("1 limit 30");
			cond1 = cond.toString();
		}
		return searchUserList(cond1);
	}

	/**
	 * 根据用户的性别,城市,随机给出5个人
	 * 
	 * @return
	 */
	public List randomSearch(List list, int stalimit, int endlimit) {
		int size = 0;
		if (list != null)
			size = 5 - list.size();
		else {
			list = new ArrayList();
		}
		if (stalimit < 0)
			stalimit = 0;
		if (endlimit < 50)
			endlimit = 50;
		StringBuilder cond1 = new StringBuilder();
		StringBuilder cond2 = new StringBuilder();
		StringBuilder cond3 = new StringBuilder();
		String coud = " 1 limit " + stalimit + "," + endlimit;// 没限制
		String coud1 = "";// 同城异性
		String coud2 = "";// 异性
		String coud3 = "";// 同城
		List recommendList = null;// 推荐列表
		List genderList = null;// 异性列表
		List cityList = null;// 同城列表
		List randomList = null;// 无限制列表
		UserBean ub = this.getLoginUser();
		if (ub != null) {
			UserBase base = CreditAction.getUserBaseBean(ub.getId());
			if (base != null) {
				if (base.getGender() == 1) {
					cond1.append(" and gender=0");
					cond2.append(" and gender=0");
				} else if (base.getGender() == 0) {
					cond1.append(" and gender=1");
					cond2.append(" and gender=1");
				}
				int city = base.getCity();
				if (city > 0) {
					cond1.append(" and city=" + city);
					cond3.append(" and city=" + city);
				}
			}
			if (cond1.length() > 4) {
				cond1.append(" limit " + stalimit + "," + endlimit);
				coud1 = cond1.substring(4);
			} else {
				cond1.append(" 1 limit " + stalimit + "," + endlimit);
				coud1 = cond1.toString();
			}
			if (cond2.length() > 4) {
				cond2.append(" limit " + stalimit + "," + endlimit);
				coud2 = cond2.substring(4);
			} else {
				cond2.append(" 1 limit " + stalimit + "," + endlimit);
				coud2 = cond2.toString();
			}
			if (cond3.length() > 4) {
				cond3.append(" limit " + stalimit + "," + endlimit);
				coud3 = cond3.substring(4);
			} else {
				cond3.append(" 1 limit " + stalimit + "," + endlimit);
				coud3 = cond3.toString();
			}
			recommendList = searchUserList(coud1);
			List templist = new ArrayList(list);
			recommendList = removeSame(templist, recommendList);
			if (recommendList.size() > size) {
				recommendList = getRandList(recommendList, size);
				return recommendList;
			} else {
				size = size - recommendList.size();
				genderList = searchUserList(coud2);
				templist = new ArrayList(list);
				templist.addAll(recommendList);
				genderList = removeSame(templist, genderList);
				if (genderList != null && genderList.size() > size) {
					recommendList.addAll(getRandList(genderList, size));
					return recommendList;
				} else {
					recommendList.addAll(genderList);
					size = size - genderList.size();
					cityList = searchUserList(coud3);
					templist = new ArrayList(list);
					templist.addAll(recommendList);
					cityList = removeSame(templist, cityList);
					if (cityList != null && cityList.size() > size) {
						recommendList.addAll(getRandList(cityList, size));
						return recommendList;
					} else {
						recommendList.addAll(cityList);
						size = size - cityList.size();
						randomList = searchUserList(coud);
						templist = new ArrayList(list);
						templist.addAll(recommendList);
						randomList = removeSame(templist, randomList);
						if (randomList != null && randomList.size() > size) {
							recommendList.addAll(getRandList(randomList, size));
							return recommendList;
						}
					}
				}
			}
		}
		return recommendList;
	}

	/**
	 * 
	 * @param black
	 *            去重参照(黑名单)black
	 * @param list
	 *            想要处理的list
	 * @return
	 */
	public List removeSame(List black, List list) {
		List random = new ArrayList();
		if (black == null || black.size() <= 0)
			return list;
		if (list == null || list.size() <= 0)
			return random;
		boolean isBlack = false;
		for (int i = 0; i < list.size(); i++) {
			String ll = (String) list.get(i);
			isBlack = false;
			for (int j = 0; j < black.size(); j++) {
				String bb = (String) black.get(j);
				if (ll.equals(bb)) {
					isBlack = true;
					break;
				}
			}
			if (!isBlack) {
				random.add(String.valueOf(ll));
			}
		}
		return random;
	}

	/**
	 * 从一个list里面随机抽取一定数目的元素
	 * 
	 * @param list
	 * @param size
	 * @return
	 */
	public List getRandList(List list, int size) {
		List rand = new ArrayList();
		if (list == null) {
			return rand;
		}
		if (size <= 0)
			return rand;
		int cound = 0;
		int i = RandomUtil.nextInt(list.size());
		while (cound < size) {
			while (rand.contains(list.get(i))) {
				i = RandomUtil.nextInt(list.size());
			}
			rand.add(list.get(i));
			cound++;
		}
		return rand;
	}

	/**
	 * 判断一个list 是否为空,true为空,false不为空!
	 * 
	 * @param list
	 * @return
	 */
	public boolean isNullList(List list) {
		if (list != null && list.size() > 0)
			return false;
		else
			return true;
	}
}
