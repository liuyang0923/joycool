package net.joycool.wap.framework;

import net.joycool.wap.util.StringUtil;

public class Integer2 {
	final int a,b;
	
	public Integer2(int a, int b) {
		this.a = a;
		this.b = b;
	}

	public boolean equals(Object obj) {
		if(obj instanceof Integer2) {
			Integer2 i = (Integer2)obj;
			return i.getA() == a && i.getB() == b;
		}
		return false;
	}

	public int hashCode() {
		return a ^ b;
	}

	public String toString() {
		return a + "-" + b;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}
	
	public static Integer2 parse(String s) {
		if(s == null)
			return null;
		String[] ss = s.split("-");
		if(ss.length != 2)
			return null;
		int a = StringUtil.toInt(ss[0]);
		int b = StringUtil.toInt(ss[1]);
		if(a == -1 || b == -1)
			return null;
		return new Integer2(a, b);
	}
}
