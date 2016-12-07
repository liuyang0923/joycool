package net.joycool.wap.spec.bottle;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;

public class BottleAction extends CustomAction {

	public static BottleService service = new BottleService();
	public static List bottleCountList = new ArrayList();
	public static byte[] lock = new byte[0];

	public BottleAction() {
	}

	// 调用父类的构造方法
	public BottleAction(HttpServletRequest request) {
		super(request);
		if (bottleCountList.size() == 0) {
			synchronized (lock) {
				if (bottleCountList.size() == 0)
					bottleCountList = SqlUtil.getIntList("select id from bottle_info where status=1 order by id", 5);
			}
		}
	}

	// 如果每次增加一个瓶子，要先将新瓶子的ID存入列表，之后再写入数据库
	public boolean createBottle(int userId, String content, String title,
			String mood) {
		BottleBean bb = new BottleBean();
		bb.setUserId(userId);
		bb.setContent(content);
		bb.setTitle(title);
		bb.setMood(mood);
		boolean result = service.createBottle(bb);
		if (!result)
			return false;

		synchronized (lock) {
			// System.out.println("总瓶子量：" + (bottleCountList.size()+1) + " " +
			// db.getLastInsertId());
			bottleCountList.add(new Integer(bb.getId()));
		}
		return true;
	}

	// 随机取出5个瓶子
	public List getFiveBottles() {
		synchronized (lock) {
			List list = new ArrayList();
			// BottleBean bb = null;
			if (bottleCountList.size() <= 5) {
				list = service.getBottles();
			} else {
				// count = bottleCountList.size();
				list = service.getRandomBottles(bottleCountList.size() - 5);
				// for (int i = 0; i < count; i++) {
				// tmp = RandomUtil.nextInt(bottleCountList.size());
				// repeat.add(String.valueOf(tmp));
				// System.out.println("执行：i=" + i + " tmp=" + tmp);
				// bb = (BottleBean) bottleCountList.get(tmp);
				// list.add(service.getBottleById(bb.getId()));
				// }
			}
			return list;
		}
	}

	// 是否可以创建一个漂流瓶
	public boolean canCreateBottle(int user_id) {
		// 1、根据用户ID，取得他放出的，现在仍然有效的瓶子;
		//System.out.println("canCreateBottle=" + user_id);
		ArrayList al = service.getBottleByUid(user_id);
		for (int i = 0; i < al.size(); i++) {
			// 2、如果这个有效的瓶子是在一天内放出的话，则不许放新的瓶子。
			BottleBean bb = (BottleBean) al.get(i);
			if (DateUtil
					.dayDiff(bb.getCreateTime(), System.currentTimeMillis()) < 1) {
				return false;
			}
		}
		return true;
	}
}
