package net.joycool.wap.spec.farm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 用户身上的装备
 * @datetime:1007-10-24
 */
public class FarmUserEquipBean {
	public static String[] partNames = {"武器", "身", "头", "手", "脚", "项链", "戒指", "戒指", "裤子", "披风", "其他", "其他"};
	public static int partCount = 10;
	public static int[] partOrder = {0,1,8,2,3,4,9,5,6,7};
	public static int[] partClass = {1, 11, 12, 13, 14, 16, 15, 15, 17, 18};		// 每个部位可以使用的二级分类class2
	int id;
	int userId;
	int part;		// 部位
	int userbagId;	// 物品
	
	public String getPartName() {
		return partNames[part];
	}
	public static String getPartName(int part) {
		return partNames[part];
	}
	
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the part.
	 */
	public int getPart() {
		return part;
	}
	/**
	 * @param part The part to set.
	 */
	public void setPart(int part) {
		this.part = part;
	}
	/**
	 * @return Returns the userbagId.
	 */
	public int getUserbagId() {
		return userbagId;
	}
	/**
	 * @param userbagId The userbagId to set.
	 */
	public void setUserbagId(int userbagId) {
		this.userbagId = userbagId;
	}
	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
