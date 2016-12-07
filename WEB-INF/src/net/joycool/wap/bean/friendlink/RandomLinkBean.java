package net.joycool.wap.bean.friendlink;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

public class RandomLinkBean {
	int id;

	int linkId;

	int appearRate;

	// 判断是否附加手机号标志
	int mark;

	int count;

	/**
	 * @return Returns the appearRate.
	 */
	public int getAppearRate() {
		return appearRate;
	}

	/**
	 * @param appearRate
	 *            The appearRate to set.
	 */
	public void setAppearRate(int appearRate) {
		this.appearRate = appearRate;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the linkId.
	 */
	public int getLinkId() {
		return linkId;
	}

	/**
	 * @param linkId
	 *            The linkId to set.
	 */
	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	// zhul_2006-07-13 功能：随机插取一个友链，并返回友链的相关内容 start
	public String getRandomLink1(HttpServletRequest request) {
		TreeMap linkMap = LoadResource.getLinkMap();
		// 插取随机链接
		int sum = ((Integer) linkMap.get(new Integer(1000000))).intValue();
		int rand;
		try {
			rand = RandomUtil.nextInt(sum) + 1;
		} catch (Exception e) {
			return "";
		}
		// rand = 271;
		// 查找随机链接相对应的记录
		Set keys = linkMap.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			int area = key.intValue();
			if (area >= rand) {
				RandomLinkBean randomLink = (RandomLinkBean) linkMap.get(key);
				// liuyi 2006-09-21 pv统计 start
				String url = "http://wap.joycool.net/entry/jumpout.jsp?linkId="
						+ randomLink.getLinkId() + "&amp;from=jc";
				// 判断是否传递手机号
				if (randomLink.getMark() == 1) {
					// 获取用户手机号
					HttpSession session = request.getSession();
					String mobile = (String) session.getAttribute("userMobile");
					// 判断是否得到手机号
					if (mobile != null && !mobile.equals("")
							&& !mobile.equals("null")) {
						url = url + "&amp;_mn_=" + mobile;
					}
				}
				return url;
				// return "<img src=\"" + url + "\" alt=\"loading...\"/>";
				// liuyi 2006-09-21 pv统计 end
			}
		}

		return null;
	}

	// zhul＿2006-07-11 end

	/**
	 * macq_2007-4-11_欺骗友链地址二级页面—_start
	 */
	public String getRandomLink(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer friendLinkCount1 = (Integer) session
				.getAttribute("frienlinkCount");
		int friendLinkCount = StringUtil.toInt(friendLinkCount1 + "");
		if (friendLinkCount != -1) {
			String url = null;
			Integer frienSublinkId = (Integer) session
					.getAttribute("frienSublinkId");
			// 如果友链id有问题！重置数据
			if (frienSublinkId == null) {
				session.removeAttribute("frienlinkCount");
				session.removeAttribute("frienSublinkId");
				return url;
			}
			// 访问次数递减
			friendLinkCount--;
			// 获取友链2级页面数据map
			TreeMap randomSubLinkMap = LoadResource.getLinkSubMap();
			List randSubLinkList = (List) randomSubLinkMap.get(frienSublinkId);
			int linkListSize = randSubLinkList.size();
			int randomId = RandomUtil.nextInt(linkListSize);
			RandomSubLinkBean randomSubLink = (RandomSubLinkBean) randSubLinkList
					.get(randomId);
			if (randomSubLink != null) {
				url = randomSubLink.getUrl();
			}
			session
					.setAttribute("frienlinkCount",
							new Integer(friendLinkCount));
			if (friendLinkCount <= 0) {
				session.removeAttribute("frienlinkCount");
				session.removeAttribute("frienSublinkId");
			}
			return url;
		} else {
			TreeMap linkMap = LoadResource.getLinkMap();
			// 插取随机链接
			int sum = ((Integer) linkMap.get(new Integer(1000000))).intValue();
			int rand;
			try {
				rand = RandomUtil.nextInt(sum) + 1;
			} catch (Exception e) {
				return "";
			}
			// rand = 271;
			// 查找随机链接相对应的记录
			Set keys = linkMap.keySet();
			Iterator it = keys.iterator();
			while (it.hasNext()) {
				Integer key = (Integer) it.next();
				int area = key.intValue();
				if (area >= rand) {
					RandomLinkBean randomLink = (RandomLinkBean) linkMap
							.get(key);
					// liuyi 2006-09-21 pv统计 start
					String url = "http://wap.joycool.net/entry/jumpout.jsp?linkId="
							+ randomLink.getLinkId() + "&amp;from=jc";
					// 判断是否传递手机号
					if (randomLink.getMark() == 1) {
						// 获取用户手机号
						String mobile = (String) session
								.getAttribute("userMobile");
						// 判断是否得到手机号
						if (mobile != null && !mobile.equals("")
								&& !mobile.equals("null")) {
							url = url + "&amp;_mn_=" + mobile;
						}
					}
					// 向session中放置友链二级访问次数
					// //// rule
					int randcount =0;
					int linkRandCount = randomLink.getCount();
					if (linkRandCount > 0) {
						randcount = RandomUtil.nextInt(100);
						if (randcount <= 25) {
							randcount = 0;
						} else if (randcount <= 75) {
							randcount = 1;
						} else {
							randcount = RandomUtil.nextInt(linkRandCount);
						}
					}
					if (randcount > 0) {
						TreeMap randomSubLinkMap = LoadResource.getLinkSubMap();
						List randSubLinkList = (List) randomSubLinkMap
								.get(new Integer(randomLink.getLinkId()));
						if (randSubLinkList != null) {
							session.setAttribute("frienlinkCount", new Integer(
									randcount));
							// 向session中放置友链id
							session.setAttribute("frienSublinkId", new Integer(
									randomLink.getLinkId()));
						}
					}
					return url;
					// return "<img src=\"" + url + "\" alt=\"loading...\"/>";
					// liuyi 2006-09-21 pv统计 end
				}
			}
		}
		return null;
	}

	/**
	 * @return 返回 mark。
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            要设置的 mark。
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
