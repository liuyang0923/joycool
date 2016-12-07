package net.joycool.wap.spec.farm;

import java.util.List;

import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;
import net.joycool.wap.cache.*;

/**
 * @author zhouj
 * @explain： 帮派
 * @datetime:1007-10-24
 */
public class FarmTongWorld {
	public static ICacheMap farmTongCache = CacheManage.farmTong;
	public static ICacheMap farmTongUserCache = CacheManage.farmTongUser;
	public static TongService service = new TongService();
	
	public static TongBean getTong(int id) {
		Integer key = new Integer(id);
		synchronized(farmTongCache) {
			TongBean tong = (TongBean) farmTongCache.get(key);
			if (tong == null) {	
				tong = service.getTong("id=" + id);
				if(tong == null) {
					return null;
				}
				farmTongCache.put(key, tong);
			}
			return tong;
		}
	}
	
	public static TongUserBean getTongUser(int id) {
		Integer key = new Integer(id);
		synchronized(farmTongUserCache) {
			TongUserBean tong = (TongUserBean) farmTongUserCache.get(key);
			if (tong == null) {	
				tong = service.getTongUser("user_id=" + id);
				if(tong == null) {
					return null;
				}
				farmTongUserCache.put(key, tong);
			}
			return tong;
		}
	}
	
	public static List getTongUserList(int tongId) {
		return SqlUtil.getIntList("select user_id from farm_tong_user where tong_id="
				+ tongId + " order by duty desc,id asc", 4);
	}
	// 离开门派
	public static void leaveTong(FarmUserBean farmUser, TongBean tong) {
		farmUser.setTongUser(null);
		tong.setCount(tong.getCount() - 1);
		DbOperation dbOp = new DbOperation(4);
		dbOp.executeUpdate("delete from farm_tong_user where user_id=" + farmUser.getUserId());
		dbOp.executeUpdate("update farm_tong set count=count-1 where id=" + tong.getId());
		dbOp.release();
	}
	// 加入门派
	public static void enterTong(FarmUserBean farmUser, TongBean tong) {
		TongUserBean tu = new TongUserBean();
		tu.setUserId(farmUser.getUserId());
		tu.setTongId(tong.getId());
		tu.setCreateTime(System.currentTimeMillis());
		service.addTongUser(tu);
		
		farmUser.setTongUser(tu);
		tong.setCount(tong.getCount() + 1);
		DbOperation dbOp = new DbOperation(4);
		dbOp.executeUpdate("update farm_tong set count=count+1 where id=" + tong.getId());
		dbOp.release();
	}
}
