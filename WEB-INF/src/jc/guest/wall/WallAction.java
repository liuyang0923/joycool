package jc.guest.wall;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.UserInfoUtil;

public class WallAction extends CustomAction{
	
	public static WallService service = new WallService();
	
	public WallAction(){
		
	}
	
	public WallAction(HttpServletRequest request){
		super(request);
	}
	
	/**
	 * 对传来的WallBean进行验证
	 * @param bean
	 * @return
	 */
	public boolean checkBean(WallBean bean){
		if (bean == null){
			setAttribute("tip","数据不存在.");
			return false;
		}
		String title = bean.getTitle();
		int uid = bean.getUid();
		String shortCont = bean.getShortCont();
		String content = bean.getContent();
		if (title == null || "".equals(title) || title.length() > 12){
			setAttribute("tip","没有输入标题或超过12字.");
			return false;
		}
		if (uid <= 0){
			setAttribute("tip","UID不存在.");
			return false;
		}
		UserBean user = UserInfoUtil.getUser(uid);
		if (user == null){
			setAttribute("tip","UID不存在.");
			return false;
		}
		if (shortCont == null || "".equals(shortCont) || shortCont.length() > 50){
			setAttribute("tip","没有输入简版内容或超过50字.");
			return false;
		}
		if (content == null || "".equals(content) || content.length() > 1500){
			setAttribute("tip","没有输入原版内容或超过1500字.");
			return false;
		}
		return true;
	}
	
	/**
	 * 随机取出N个墙
	 * @param count
	 * @return
	 */
	public List getWallRandom(int count){
		int total = SqlUtil.getIntResult("select count(id) from wall where visible=1", 5);
		if (total <= count){
			return service.getWallList("visible=1");
		} else {
			return service.getWallList(" id>=" + RandomUtil.nextIntNoZero(total-count) + " and visible=1 limit " + count);
		}
	}
}
