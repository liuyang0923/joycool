package net.joycool.wap.util;

import java.util.HashMap;
import java.util.Vector;

public class ContentList {
	public static Vector contentList;
	public static Vector postList;
	public static HashMap postMap = new HashMap();		// 禁止发言
	public static Vector salutatoryManList;
	public static Vector salutatoryWomanList;
	public static void initContent() {
		contentList = new Vector();
	}
	public static void initPostList() {
		postList = new Vector();
	}
	public static void initSalutatoryManList() {
		salutatoryManList = new Vector();
	}
	public static void initSalutatoryWomanList() {
		salutatoryWomanList = new Vector();
	}
	 public static Vector getContentList() {
        if (contentList == null || contentList.size() == 0) {
        	initContent();
        }
        return contentList;
    }
	 public static Vector getPostList() {
        if (postList == null || postList.size() == 0) {
        	initPostList();
        }
        return postList;
	}
	 public static Vector getSalutatoryManList() {
	        if (salutatoryManList == null || salutatoryManList.size() == 0) {
	        	initSalutatoryManList();
	        }
	        return salutatoryManList;
		}
	 public static Vector getSalutatoryWomanList() {
	        if (salutatoryWomanList == null || salutatoryWomanList.size() == 0) {
	        	initSalutatoryWomanList();
	        }
	        return salutatoryWomanList;
		}
}
