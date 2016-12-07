package net.joycool.wap.spec.farm;

import java.text.DecimalFormat;
import java.util.*;

import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.spec.farm.bean.ItemSetBean;

/**
 * @author zhouj
 * @explain： util工具类
 * @datetime:1007-10-24
 */
public class FarmUtil {
	public static int[] getIntsFromList(List list, int match) {
		for(int i = 0;i < list.size();i++) {
			int[] a = (int[])list.get(i);
			if(a[0] == match)
				return a;
		}
		return null;
	}
	public static String getIntsFromList(List list, int match, int c) {
		int[] a = getIntsFromList(list, match);
		if(a == null)
			return "0";
		return String.valueOf(a[c]);
	}
	
	static DecimalFormat numFormat = new DecimalFormat("0.0");
	public static String formatNumber(float number) {
		return numFormat.format(number);
	}
	
	// 返回对应物品的一项属性，如果不存在则返回null
	public static int[] getItemAttrbute(List attrList, int i) {
		for(int ia = 0;ia < attrList.size();ia++) {
			int[] attr = (int[])attrList.get(ia);
			if(attr[0] == i)
				return attr;
		}
		return null;
	}
	public static float[] itemGradeAdd = {1.0f, 0.6f, 1.0f, 1.1f, 1.25f, 1.4f, 1.6f};
	public static float[] itemGradeAddRank = {1.0f, -2.0f, 0.0f, 1.6f, 3.8f, 5.8f, 8.0f};
	public static float[] itemGradeAttributeAdd = {0.0f, 0.0f, 0.0f, 1.0f, 1.35f, 1.8f, 2.4f, 3.4f, 5.0f};
	public static float[] itemAttrbutePartAdd = {0f, 1.0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 
		1.0f, 1.0f, 0.8f, 0.7f, 0.75f, 0.85f, 0.9f, 0.6f, 0.0f, 0.0f};
	public static float[] itemDefensePartAdd = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 
		1.0f, 0.81f, 0.63f, 0.62f, 0.0f, 0.0f, 0.87f, 0.53f, 0.0f, 0.0f};
	public static float[] itemAttrbuteAdd = {0.0f, 0.0f, 0.0f, 0.0f, 0.1f, 0.15f, 0.3f, 0.0f, 0.0f, 0.0f, 0.0f, 
		1.0f, 1.0f, 1.0f, 0.8f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
		2.0f, 0.3f, 1.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 
		3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 5.0f, 5.0f, 0.0f, 0.0f, 0.0f};
	// 返回参考数据（制作装备的时候作为参考）
	public static String getRefData(DummyProductBean item) {
		List attrList = item.getAttributeList();
		if(attrList.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		float sumAttr = 0;
		for(int ia = 0;ia < attrList.size();ia++) {
			int[] attr = (int[])attrList.get(ia);
			switch(attr[0]) {
			case 1: {
				sb.append("攻击力:");
				float real = (float)Math.pow(1.06, item.getRank()) * 6.0f * itemGradeAdd[item.getGrade()];
				float cur = attr[1] + (float)(attr[2]) / 2;
				sb.append(formatNumber(real));
				sb.append("/");
				addColorString(sb, formatNumber(cur), real, cur);
			} break;
			case 3: {
				sb.append("防御:");
				float gradeLevel = item.getRank() + itemGradeAddRank[item.getGrade()];
				if(gradeLevel < 1)
					gradeLevel = 1;
				float def = (gradeLevel * 0.6f + 5.0f) / 100.0f;
				float real = (gradeLevel * 40 + 200) / (1 - def) * def / 4.5f * itemDefensePartAdd[item.getClass2()];
				float cur = attr[1];
				sb.append(formatNumber(real));
				sb.append("/");
				addColorString(sb, formatNumber(cur), real, cur);
			} break;
//			case 21:
//			case 22: {
//				if(item.getRank() <= 10)	// 0.5 - 1
//					sumAttr += (itemAttrbuteAdd[attr[0]] * item.getRank() / 20 + 0.5) * attr[1];
//				else if(item.getRank() <= 20)	// 1 - 2
//					sumAttr += (itemAttrbuteAdd[attr[0]] * item.getRank() / 10 + 1) * attr[1];
//				else				// 2 - 4
//					sumAttr += (itemAttrbuteAdd[attr[0]] * item.getRank() / 5 - 2) * attr[1];
//			} break;
			default:
				sumAttr += itemAttrbuteAdd[attr[0]] * attr[1];
			}
		}
		if(sumAttr > 0) {
			sb.append(",属性:");
			float real = ((float)Math.pow(1.05, item.getRank()) * 2.0f) * itemAttrbutePartAdd[item.getClass2()] * itemGradeAttributeAdd[item.getGrade()];
			float cur = sumAttr;
			sb.append(formatNumber(real));
			sb.append("/");
			addColorString(sb, formatNumber(cur), real, cur);
		}
		return sb.toString();
	}
	public static float[] setCountAdd = {0.0f, 0.0f, 0.4f, 0.37f, 0.34f, 0.3f, 0.275f, 0.26f, 0.25f, 0.24f, 0.23f};
	public static String getSetRefData(ItemSetBean set) {
		
		List attrList = set.getAttributeList();
		if(attrList.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		float sumAttr = 0;
		for(int ia = 0;ia < attrList.size();ia++) {
			int[] attr = (int[])attrList.get(ia);
			sumAttr += itemAttrbuteAdd[attr[0]] * attr[1];
		}
		if(sumAttr > 0) {
			float real = 0;
			for(int j = 0;j < set.getItemList().size();j++) {
				Integer iid = (Integer)set.getItemList().get(j);
				DummyProductBean item = FarmWorld.getItem(iid.intValue());
				real += ((float)Math.pow(1.05, item.getRank()) * 2.0f) * itemAttrbutePartAdd[item.getClass2()] * itemGradeAttributeAdd[item.getGrade()];
			}
			real *= setCountAdd[set.getItemCount()];
			sb.append("属性:");
			float cur = sumAttr;
			sb.append(formatNumber(real));
			sb.append("/");
			addColorString(sb, formatNumber(cur), real, cur);
		}
		return sb.toString();
	}
	// 返回1 2 3对应A B C
	public static char getOptionChar(int answer) {
		return (char)('A' + answer);
	}
	
	public static void addColorString(StringBuilder sb, String add, float real, float cur) {
		float dis = (cur - real) / real;
		if(dis < -0.1f) {
			sb.append("<font color=#00CC00><b>");
			sb.append(add);
			sb.append("</b></font>");
		} else if(dis > 0.1f) {
			sb.append("<font color=red><b>");
			sb.append(add);
			sb.append("</b></font>");
		} else {
			sb.append(add);
		}
	}
}
